


import java.awt.Graphics;


public class Weapon extends Entity {
	public Timer fireTimer		= new Timer();
	public Timer reloadTimer	= new Timer();
	public Timer scopeZoomTimer = new Timer();
	public boolean scopeZoomed	= false;
	
	
	// Adjustable Variables:
	public String name				= "NULL WEAPON";
	public String weaponType		= "NULL WEAPON TYPE";
	public int maxPackAmmo			= 50;
	public int maxClipAmmo			= 6;
	public int packAmmo				= maxPackAmmo;		// Ammo not loaded
	public int clipAmmo				= maxClipAmmo;		// Ammo in the clip
	public double fireTime			= 0.5;				// Time between shots
	public double reloadTime		= 0.5;				// Reload time in seconds
	public boolean hasMagazine		= false;			// Determines Reloading type
	public boolean reloading		= false;
	public double speedFactor		= 1.0;				// Speed factor that slows the Unit while Holding this weapon
	public double fireSpeedFactor	= 1.0;				// Speed factor that slows the Unit while Firing this weapon
	
	public int bulletCount			= 10;				// Bullets fired per shot
	public double bulletSpeed		= 15;				// Speed of bullets
	public double dirSpread			= 15;				// Angle range bullets can be shot at (determines accuracy)
	public double bulletDamage		= 10;
	
	public int scopeType			= 0; // Scope Type: {0 = no scope, 1 = long scope, 2 = red dot scope}
	
	public Weapon() {
		fireTimer.start();
		reloadTimer.start();
	}
	
	public void initialize() {
		packAmmo		= maxPackAmmo;
		clipAmmo		= maxClipAmmo;
	}
	
	public Bullet getBulletType(Unit owner, double x, double y, double direction, double speed, double damage) {
		return new BulletPistol(owner, x, y, direction, speed, damage);
	}
	
	public boolean fire(Unit owner, double x, double y, double direction) {
		if (fireTimer.getSeconds() >= fireTime && !reloading && clipAmmo > 0) {
			fireTimer.start();
			fireTimer.reset();
			clipAmmo -= 1;
			for (int i = 0; i < bulletCount; i += 1) {
				double dir = direction - (dirSpread / 2) + (dirSpread * Game.random.nextDouble());
				Game.addEntity(new BulletPistol(owner, x, y, dir, this.bulletSpeed, this.bulletDamage));
			}
			if (clipAmmo == 0)
				reload();
			return true;
		}
		return false;
	}
	
	public boolean isFiring() {
		return (fireTimer.getSeconds() < fireTime);
	}
	
	public boolean reload() {
		if (!reloading && clipAmmo < maxClipAmmo && packAmmo > 0) {
			reloading = true;
			reloadTimer.reset();
			reloadTimer.start();
			
			return true;
		}
		return false;
	}
	
	public void giveMaxAmmo() {
		packAmmo = maxPackAmmo;
		clipAmmo = maxClipAmmo;
	}
	
	public void update() {
		if (reloading) {
			if (reloadTimer.getSeconds() >= reloadTime) {
				reloading = false;
				
				// Refill clip ammo from pack
				if (hasMagazine) {
					int tempAmmo = clipAmmo;
					clipAmmo += Math.min(packAmmo, maxClipAmmo - clipAmmo);
					packAmmo -= clipAmmo - tempAmmo;
				}
				else {
					packAmmo -= 1;
					clipAmmo += 1;
					if (clipAmmo < maxClipAmmo) {
						reload();
					}
				}
			}
		}
	}
	
	public void draw(Graphics g) {
	}
}
