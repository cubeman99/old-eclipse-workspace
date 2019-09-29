package com.base.game.wolf3d.tile;

import com.base.engine.audio.AudioEngine;
import com.base.engine.audio.SoundEmitter;
import com.base.engine.audio.SoundListener;
import com.base.engine.common.GMath;
import com.base.engine.common.Line2f;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Keyboard;
import com.base.engine.core.Mouse;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Window;
import com.base.game.wolf3d.DynamicAnimation;
import com.base.game.wolf3d.RayCast;
import com.base.game.wolf3d.Room;
import com.base.game.wolf3d.Sprite;
import com.base.game.wolf3d.SpriteAnimation;
import com.base.game.wolf3d.Weapon;
import com.base.game.wolf3d.Wolf3D;
import com.base.game.wolf3d.event.EventDie;
import com.base.game.wolf3d.tile.enemy.BasicEnemy;
import com.base.game.wolf3d.tile.enemy.Enemy;

public class Player extends Unit implements SoundListener {
	public static final int KEY_FORWARD    = Keyboard.KEY_W;
	public static final int KEY_BACK       = Keyboard.KEY_S;
	public static final int KEY_LEFT       = Keyboard.KEY_A;
	public static final int KEY_RIGHT      = Keyboard.KEY_D;
	public static final int KEY_TURN_LEFT  = Keyboard.KEY_LEFT;
	public static final int KEY_TURN_RIGHT = Keyboard.KEY_RIGHT;
	public static final int KEY_SHOOT      = Keyboard.KEY_SPACE;
	public static final int KEY_USE        = Keyboard.KEY_E;
	public static final int KEY_SPRINT     = Keyboard.KEY_LSHIFT;
	
	private float speed;
	private float sprintSpeed;
	private float sensitivity;
	private int ammo;
	private int ammoMax;
	private boolean[] keys;
	
	private float eyeLevel;
	private Camera camera;
	private Weapon[] weapons;
	private int weaponIndex;
	private Texture[][] weaponSheet;
	private DynamicAnimation gunAnimation;
	private Sprite weaponSprite;
	private boolean shot;
	private boolean mouseLocked;
	private boolean shooting;
	private boolean sprinting;
	private RayCast rayCast;
	
	
	

	// ================== CONSTRUCTORS ================== //
	
	public Player() {
		super("Player", null);
		
		AudioEngine.setListener(this);
		
		eyeLevel      = 0.5f;
		speed         = 4.0f;
		sprintSpeed   = 8.0f;
		sensitivity   = 0.12f;
		collisionSize = 0.6f;
		ammoMax       = 99;
		ammo          = 99;
		initHealth(100);
		
		sprinting     = false;
		shooting      = false;
		mouseLocked   = false;
		shot          = false;
		
		rayCast = new RayCast();
		
		Mouse.setMousePosition(Window.getCenter());
		Mouse.setCursorState(false);
		mouseLocked = true;
		
		// Attatch a camera.
		fov    = (float) Math.toRadians(60.0f);
		camera = new Camera(fov, Wolf3D.VIEWPORT_ASPECT_RATIO, 0.01f, 1000.0f);
		camera.getTransform().getPosition().setY(eyeLevel);
		addChild(camera);
		
		// Account for aspect ratio that increases fov.
		fov = 2.0f * GMath.atan(Wolf3D.VIEWPORT_ASPECT_RATIO * GMath.tan(fov * 0.5f));

		keys = new boolean[Wolf3D.NUM_KEYS];
		weapons = new Weapon[] {
			new Weapon("Knife",     0.9f, 0.6f, 0, 4, false,  true, false, Wolf3D.SOUND_KNIFE),
			new Weapon("Pistol",      20, 0.3f, 0, 4, false, false,  true, Wolf3D.SOUND_PISTOL),
			new Weapon("Machine Gun", 20, 0.2f, 2, 4,  true, false,  true, Wolf3D.SOUND_MACHINE_GUN),
			new Weapon("Chain Gun",   20, 0.1f, 2, 3,  true, false,  true, Wolf3D.SOUND_CHAIN_GUN)
		};
		
		getWeapon(Wolf3D.WEAPON_KNIFE).unlock();
		getWeapon(Wolf3D.WEAPON_PISTOL).unlock();
		weaponIndex = Wolf3D.WEAPON_PISTOL;
		
		
		// Create weapon animations.
		weaponSheet     = Wolf3D.SHEET_WEAPONS;
		weaponSprite    = new Sprite();
		gunAnimation    = new DynamicAnimation(Wolf3D.NUM_WEAPONS);
		
//		gunAnimation = Util.createAnimationStrip2(weaponSheet, 0, 0, 5, 4);
		
		for (int i = 0; i < Wolf3D.NUM_WEAPONS; i++) {
			SpriteAnimation anim = new SpriteAnimation();
			for (int j = 0; j < 5; j++)
				anim.addFrame(weaponSheet[(j + 1) % 5][i]);
			gunAnimation.setVariant(i, anim);
		}
		
		weaponSprite.newAnimation(false, gunAnimation);
		weaponSprite.moveToLastFrame();
		weaponSprite.setSpeed(15);
		weaponSprite.setVariation(weaponIndex);
	}

	
	
	// =================== ACCESSORS =================== //
    
	public int getWeaponIndex() {
		return weaponIndex;
	}
	
	public boolean hasKey(int keyIndex) {
		return keys[keyIndex];
	}
	
	public RayCast getRayCast() {
		return rayCast;
	}
	
	public boolean hasMaxAmmo() {
		return (ammo >= ammoMax);
	}
	
	public boolean isSprinting() {
		return sprinting;
	}
	
	public Sprite getWeaponSprite() {
		return weaponSprite;
	}

	public Weapon getWeapon(int index) {
		return weapons[index];
	}
	
	public Weapon getWeapon() {
		return weapons[weaponIndex];
	}
	
	public boolean isShooting() {
		return shooting;
	}
	
	public int getAmmo() {
		return ammo;
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void takeDamage(Tile source, int damage) {
		damage(damage);
		if (isDead() && !Wolf3D.GOD_MODE)
			getGame().playEvent(new EventDie(source.getPosition()));
	}
	
	public void unlockWeapon(int index) {
		if (!getWeapon(index).isUnlocked()) {
    		weapons[index].unlock();
    		switchWeapon(index);
		}
	}
	
	public void switchWeapon(int newWeaponIndex) {
		if (getWeapon(newWeaponIndex).isUnlocked() && (ammo > 0 || !getWeapon(newWeaponIndex).usesAmmo())) {
    		weaponIndex = newWeaponIndex;
    		interruptShooting();
		}
	}
	
	public void interruptShooting() {
		shooting = false;
		shot = false;
		weaponSprite.moveToLastFrame();
	}
	
	public void giveKey(int keyIndex) {
		keys[keyIndex] = true;
	}
	
	public void giveAmmo(int amount) {
		ammo = Math.max(0, Math.min(ammoMax, ammo + amount));
	}
	
	public void move(Vector3f dir, float amt) {
		getTransform().getPosition().add(dir.times(amt));
	}
	
	public void turn(float amount) {
		getTransform().rotate(Vector3f.Y_AXIS, (float) Math.toRadians(amount));
	}
	
	public boolean canShoot() {
		return (ammo > 0 || !getWeapon().usesAmmo());
	}
	
	public boolean shoot() {
		shooting = false;
		shot     = false;
		
		if (canShoot()) {
    		shooting = true;
    		weaponSprite.resetAnimation();
			if (getWeapon().usesAmmo())
				ammo--;
		}
		
		return shooting;
	}
	
	public void fireBullet() {
		Enemy closestMonster = null;
		float minDistance = 100000;
		shot = true;
		
		AudioEngine.playSound(getWeapon().getSound(), this);
		
		
		Vector2f pos = getPosition();
		Vector2f dir = getDirection();
		Vector2f vec = getLevel().castRay(pos, pos.plus(dir.times(getWeapon().getRange())));
		if (vec != null)
			minDistance = vec.distanceTo(pos);
		
		// Collide with enemies.
		for (SceneObject obj : getLevel().getObjects()) {
			if (obj instanceof Enemy) {
				Enemy monster = (Enemy) obj;
				Vector2f monsterPos = monster.getPosition();
				Vector2f vecToMonster = monsterPos.minus(pos);
				
				float a = GMath.smallestAngleBetween(dir.getDirection(), vecToMonster.getDirection());
				float dist = vecToMonster.length() * (float) Math.tan(a);
				float distance = vecToMonster.length();
				
				if (a < GMath.HALF_PI && dist < monster.getHitSize() / 2 && distance < getWeapon().getRange()) {
					// Shot!
					if (distance < minDistance) {
						minDistance = distance;
						closestMonster = monster;
					}
				}
			}
		}
		
		// Damage an enemy (if one was hit).
		if (closestMonster != null && !closestMonster.isDestroyed()) {
			if (minDistance < 4 || GMath.random.nextInt(255) / 12 > minDistance) {
    			int damage = 0;
    			int rand = GMath.random.nextInt(255);
    			
    			if (minDistance >= 2)
    				damage = rand / 6;
    			else
    				damage = rand / 4;
    			
    			if (weaponIndex == Wolf3D.WEAPON_KNIFE) {
    				damage = rand / 16;
        			if (closestMonster.getOrientation() >= 3 && closestMonster.getOrientation() <= 5)
        				damage = rand / 6;
    			}
    			
    			closestMonster.damage(damage);
			}
		}
		
		// Alert enemies in the same and adjacent rooms.
		Room room = getRoom();
		if (room != null)
			room.alertEnemies();
		getLevel().alertEnemies(pos);
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void initialize() {
		super.initialize();
		getLevel().getGame().getEngine().getRenderingEngine().setMainCamera(camera);
	}
	
	@Override
	public void onDamage(int amount) {
		getGame().setFade(Draw2D.RED, 0.2f);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		
		// Update movement.
		sprinting = Keyboard.isKeyDown(KEY_SPRINT);
		float movAmt = (sprinting ? sprintSpeed : speed) * delta;
		velocity.zero();
		
		if(Keyboard.isKeyDown(KEY_FORWARD))
			velocity.add(getTransform().getRotation().getForward().getXZ().normalize());
		if(Keyboard.isKeyDown(KEY_BACK))
			velocity.sub(getTransform().getRotation().getForward().getXZ().normalize());
		if(Keyboard.isKeyDown(KEY_LEFT))
			velocity.add(getTransform().getRotation().getLeft().getXZ().normalize());
		if(Keyboard.isKeyDown(KEY_RIGHT))
			velocity.add(getTransform().getRotation().getRight().getXZ().normalize());
		velocity.multiply(movAmt);
		updateMovement();
		
		
		// Update view.
		Vector2f centerPosition = Window.getCenter();
		
		if (Keyboard.isKeyDown(KEY_TURN_LEFT))
			turn(-2);
		if (Keyboard.isKeyDown(KEY_TURN_RIGHT))
			turn(2);
		
		if (mouseLocked) {
			Vector2f deltaPos = Mouse.getPosition().minus(centerPosition);
			boolean rotX = (deltaPos.getX() != 0);
			boolean rotY = (deltaPos.getY() != 0);
			
			if(rotX)
				turn(deltaPos.getX() * sensitivity);
//			if(rotY)
//				getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(deltaPos.getY() * sensitivity));

			if(rotX || rotY)
				Mouse.setMousePosition(centerPosition);
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Mouse.setCursorState(true);
				mouseLocked = false;
			}
		}
		else if (Mouse.left.pressed()) {
			Mouse.setMousePosition(centerPosition);
			Mouse.setCursorState(false);
			mouseLocked = true;
		}
		
		
		// Weapon switching.
		if (Mouse.onWheelUp()) {
			for (int i = 0; i < Wolf3D.NUM_WEAPONS; i++) {
				int index = GMath.mod(weaponIndex + i + 1, Wolf3D.NUM_WEAPONS);
				if (getWeapon(index).isUnlocked() && (ammo > 0 || !getWeapon(index).usesAmmo())) {
					switchWeapon(index);
					break;
				}
			}
		}
		if (Mouse.onWheelDown() || (getWeapon().usesAmmo() && ammo == 0)) {
			for (int i = 0; i < Wolf3D.NUM_WEAPONS; i++) {
				int index = GMath.mod(weaponIndex - i - 1, Wolf3D.NUM_WEAPONS);
				if (getWeapon(index).isUnlocked() && (ammo > 0 || !getWeapon(index).usesAmmo())) {
					switchWeapon(index);
					break;
				}
			}
		}
		for (int i = 0; i < Wolf3D.NUM_WEAPONS; i++) {
			if (Keyboard.isKeyDown(Keyboard.KEY_1 + i)) {
				switchWeapon(i);
				break;
			}
		}
		
		
		// Update ray cast.
		Vector2f pos = getPosition();
		Vector2f dir = getDirection();
		rayCast.trajectory = new Line2f(pos, pos.plus(dir.times(10000)));
		getLevel().castRay(rayCast);
		
		// Update shooting.
		weaponSprite.update(delta, weaponIndex);
		if (shooting) {
			if (weaponSprite.getFrameIndex() == 2 && !shot) {
				fireBullet();
			}
			
			if (weaponSprite.getFrameIndex() > getWeapon().getAnimFireEnd() - 1) {
				if (canShoot() && getWeapon().isAutomatic() && (Keyboard.isKeyDown(KEY_SHOOT) || Mouse.left.down())) {
					shoot();
					weaponSprite.setFrameIndex(getWeapon().getAnimFireStart() - 1);
				}
			}
			if (weaponSprite.isAnimationDone(delta)) {
				shooting = false;
			}
		}
		if (!shooting && (Keyboard.isKeyPressed(KEY_SHOOT) || Mouse.left.down())) {
			shoot();
		}
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		// Don't render.
	}
	
	@Override
	public Quaternion getSoundOrientation() {
		return getTransform().getTransformedRotation();
	}
}
