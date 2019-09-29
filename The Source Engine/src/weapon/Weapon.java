package weapon;

import main.Keyboard;
import main.Main;
import main.Mouse;
import common.GMath;
import common.Timer;
import common.Vector;
import entity.unit.Unit;


/**
 * Weapon.
 * 
 * @author David Jordan
 */
public abstract class Weapon {
	private static final double MIN_RECOIL   = 1.0;
	private static final double MAX_RECOIL   = 4.0;
	private static final double RECOIL_DECAY = 0.90;
	
	// Custom Weapon Stats:
	protected String name				= "Null Weapon";
	protected int maxPackAmmo			= 0;    // Ammo stored away in your pack.
	protected int maxClipAmmo			= 0;    // Ammo stored in the magazine/clip.
	protected int reloadTime			= 0;    // milliseconds
	protected ReloadType reloadType		= null; // The reloading mechanism type.

	protected FireMode[] fireModes      = {};   // Supported firing modes
	protected int burstFireRounds		= 0;    // How many shots to fire on burst fire mode
	protected double fireRate		 	= 0;    // rounds per minute
	protected int bulletsPerRound    	= 0;    // bullets per round
	protected double effectiveRange     = 0;    // meters
	protected double maximumRange       = 0;    // meters
	protected double muzzleVelocity	 	= 0;    // meters per second
	protected double weight				= 0;    // kilograms
	protected double bulletDamage		= 0;    // Maximum damage that the bullet deals on impact.
	protected double recoilFactor		= 0;    // Amount for recoil to scale after each shot.
	
	// Private weapon Variables:
	public Unit owner					= null;
	private int packAmmo		 		= 0;
	private int clipAmmo	 			= 0;
	private Timer fireTimer				= new Timer();
	private Timer reloadTimer			= new Timer();
	private double bulletDirSpread		= 0;
	private int fireModeIndex           = 0;
	private int burstFiringCount        = 0;
	private boolean reloadOnEmptyClip   = true;
	private double recoil               = MIN_RECOIL;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Weapon(Unit owner) {
		this.owner = owner;
	}
	
	
	
	// =================== ACCESSORS =================== //

	/** Return the current selective fire mode of the weapon. **/
	public FireMode getFireMode() {
		return fireModes[fireModeIndex];
	}

	/** Return whether the weapon's clip currently has any ammo. **/
	public boolean clipHasAmmo() {
		return (clipAmmo > 0);
	}

	/** Return whether the pack currently has any ammo stored in it. **/
	public boolean packHasAmmo() {
		return (packAmmo > 0);
	}

	/** Return the weapon's name. **/
	public String getName() {
		return name;
	}

	/** Return amount of ammo currently stored in the pack. **/
	public int getPackAmmo() {
		return packAmmo;
	}

	/** Return amount of ammo currently in the weapon's clip. **/
	public int getClipAmmo() {
		return clipAmmo;
	}

	/** Return the maximum ammo that can be stored in the weapon's clip. **/
	public int getMaxClipAmmo() {
		return maxClipAmmo;
	}

	/** Return the maximum ammo that can be stored in the pack. **/
	public int getMaxPackAmmo() {
		return maxPackAmmo;
	}
	
	/** Return whether the weapon is being reloaded. **/
	public boolean isReloading() {
		return reloadTimer.isRunning();
	}

	/** Return the percentage of completion for reloading. **/
	public double getReloadPercentage() {
		return reloadTimer.getPercentage();
	}

	/** Return the percentage of recoil compared to the maximum recoil. **/
	public double getRecoilPercentage() {
		return ((recoil - MIN_RECOIL) / (MAX_RECOIL - MIN_RECOIL));
	}

	/** Return the range of direction values that bullets can be fired with. **/
	public double getDirSpread() {
		return (bulletDirSpread * recoil);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Update the weapon. **/
	private void update(boolean isPlayerWeapon) {
		
		if (isPlayerWeapon) {
    		// Left Mouse Button: Fire the weapon
			if (Mouse.left.down() && (getFireMode() == FireMode.AUTOMATIC || Mouse.left.pressed())) {
				fire();
			}
    		
    		// Middle Mouse Button: Change selective fire mode:
    		if (Mouse.middle.pressed()) {
    			fireModeIndex++;
    			if (fireModeIndex >= fireModes.length)
    				fireModeIndex = 0;
    		}
		}
		
		// Update burst fire:
		if (burstFiringCount > 0) {
			fire();
		}

		// Dampen Recoil over time.
		if (recoil > MIN_RECOIL && fireTimer.isExpired())
			recoil = GMath.max(MIN_RECOIL, recoil * RECOIL_DECAY);
		
		// Reload: Reload weapon:
		if (isPlayerWeapon && Keyboard.reload.pressed())
			reload();
		
		// Handle finishing reloading.
		if (reloadTimer.isRunning() && reloadTimer.isExpired()) {
			reloadTimer.stop();

			if (packAmmo > 0) {
    			if (reloadType == ReloadType.MAGAZINE) {
        			int transfer = Math.min(maxClipAmmo - clipAmmo, packAmmo);
        			packAmmo -= transfer;
        			clipAmmo += transfer;
    			}
    			else if (reloadType == ReloadType.CLIP) {
    				clipAmmo++;
    				reload();
    			}
			}
		}
	}
	
	/** Fire one round, returning if it was able to fire. **/
	public boolean fire() {
		if (!clipHasAmmo() || !fireTimer.isExpired() || isReloading())
			return false;
		
		// Fire Weapon:
		for (int i = 0; i < bulletsPerRound; i++) {
			double spread = bulletDirSpread * recoil;
			double dir    = owner.direction + (spread * 0.5) - (GMath.random.nextDouble() * spread);
    		Vector bulletVelocity = Vector.polarVector(muzzleVelocity, dir);
    		bulletVelocity.scale(1.0 / (double) Main.FPS);
    		Bullet b = new Bullet(owner, owner.getPosition(), bulletVelocity, bulletDamage);
    		owner.control.addEntity(b);
		}

		clipAmmo--;
		fireTimer.start();
		
		// Apply recoil:
		recoil = GMath.min(MAX_RECOIL, recoil + recoilFactor);
		
		// Reload if clip is empty or update burst fire:
		if (!clipHasAmmo() && reloadOnEmptyClip)
			reload();
		else if (getFireMode() == FireMode.BURST) {
			burstFiringCount++;
			if (burstFiringCount >= burstFireRounds)
				burstFiringCount = 0;
		}
		
		return true;
	}
	
	/** Start reloading the weapon, returning if it was able to start reloading. **/
	public boolean reload() {
		if (clipAmmo == maxClipAmmo || !packHasAmmo() || reloadTimer.isRunning())
			return false;
		
		reloadTimer.start();
		burstFiringCount = 0;
		
		return true;
	}
	
	/** Update the weapon without player controls. **/
	public void update() {
		update(false);
	}
	
	/** Update the weapon with player controls. **/
	public void updatePlayerWeapon() {
		update(true);
	}

	/** Initialize a weapon after it has been setup by a sub-class. **/
	protected void initialize() {
		fireTimer.setExpireTime((int) (60000.0 / fireRate));
		reloadTimer.setExpireTime(reloadTime);
		clipAmmo      = maxClipAmmo;
		packAmmo      = maxPackAmmo;
		bulletDirSpread = 20.0 / effectiveRange;
		
		if (fireModes.length == 0)
			fireModes = new FireMode[] {FireMode.AUTOMATIC};
	}
	
	/** Reset this weapon to have full ammo, and renew other stats. **/
	public void renew() {
		initialize();
	}
	
	
	
	// ============== ENUMERATED CLASSES ============== //
	
	/** Sub-class to represent the different selective fire modes. **/
	public enum FireMode {
		AUTOMATIC("Automatic"),
		SEMI_AUTO("Semi-Automatic"),
		BURST("Burst Fire");
		
		FireMode(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		private String name;
	}

	/** Sub-class to represent the different reload types. **/
	public enum ReloadType {MAGAZINE, CLIP}
}
