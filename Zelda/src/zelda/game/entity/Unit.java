package zelda.game.entity;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collidable;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.ImageSheet;
import zelda.common.graphics.Sprite;
import zelda.common.util.Currency;
import zelda.common.util.GMath;
import zelda.game.control.GameInstance;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.object.overworld.ObjectGrass;
import zelda.game.world.tile.GridTile;
import zelda.main.Sound;


/**
 * Unit.
 * 
 * @author David Jordan
 */
public abstract class Unit extends EntityObject {
	protected Currency health;

	protected CollisionBox collisionBoxSpecial;
	protected CollisionBox[] shieldCollisionBoxes;
	protected Collidable shieldCollidable;

	protected boolean hurt;
	protected int hurtDuration;
	protected boolean hurtFlickering;
	protected int flickerDuration;
	protected int hurtTimer;
	protected boolean bumpable;
	protected boolean bumped;
	protected int bumpDuration;
	protected int bumpTime;
	protected int bumpTimer;
	protected boolean controllable;
	protected FrameObject surface;
	
	protected Sound soundDamage;
	protected Sound soundBump;
	
	protected boolean moving;
	protected int dir;
	protected int faceDir;

	protected ImageSheet sheetNormal;
	protected ImageSheet sheetHurt;
	protected boolean slipping;
	protected int slipTimer;
	protected boolean inGrass;
	protected Sprite spriteGrass;
	protected double slipSpeed;
	protected boolean fallsInHoles;
	protected boolean passable;


	// ================== CONSTRUCTORS ================== //

	public Unit() {
		this(null);
	}

	public Unit(GameInstance game) {
		super(game);

		hurtDuration    = 16;
		bumpDuration    = 16;
		bumpTime        = 16;
		flickerDuration = 32;


		hardCollisionBox       = new CollisionBox(0, 0, 16, 16);
		softCollisionBox       = new CollisionBox(0, 0, 16, 16);
		collisionAutoDodge     = true;
		collisionDodgeSpeed    = 1;
		collisionDodgeDistance = 6;
		ledgePassable          = false;
		halfSolidPassable      = false;
		
		surface        = null;
		sprite         = null;
		health         = new Currency("Health", 12);
		hurt           = false;
		hurtFlickering = false;
		hurtTimer      = 0;
		bumpable       = true;
		bumped         = false;
		bumpTimer      = 0;
		slipping       = false;
		slipTimer      = 0;
		inGrass        = false;
		spriteGrass    = new Sprite(Resources.SPRITE_EFFECT_GRASS);
		slipSpeed      = 0.5;
		fallsInHoles   = true;
		passable       = false;
		
		soundDamage = Sounds.MONSTER_HURT;
		soundBump   = Sounds.OBJECT_BOMB_BOUNCE;
		
		collisionBoxSpecial = new CollisionBox(7, 13, 2, 2);
		moving              = false;
		dir                 = 0;
		faceDir             = 0;
		controllable        = true;
		destroyedInHoles    = false;


		shieldCollisionBoxes = null;
		shieldCollidable     = new Collidable() {
			public boolean isSolid() {
				return false;
			}
			public Vector getPosition() {
				return position;
			}
			public CollisionBox getCollisionBox() {
				if (shieldCollisionBoxes == null
						|| faceDir >= shieldCollisionBoxes.length)
					return null;
				return shieldCollisionBoxes[faceDir];
			}
		};
	}



	protected void onDamage() {
	}

	public void die() {
	}



	// =================== ACCESSORS =================== //

	public Vectangle getCollisionVect() {
		return hardCollisionBox.getVect(position);
	}

	public Collidable getShieldCollidable() {
		return shieldCollidable;
	}

	public boolean isSlipping() {
		return slipping;
	}

	public int getFaceDir() {
		return faceDir;
	}

	public int getDir() {
		return dir;
	}

	public int getHealth() {
		return health.get();
	}

	public int getMaxHealth() {
		return health.getMax();
	}

	public boolean isMoving() {
		return moving;
	}
	
	public boolean isOnSurface() {
		if (surface != null)
			return true;
		ArrayList<Entity> objs = Collision.getInstancesMeeting(
				getOrigin(), this, FrameObject.class);
		for (int i = 0; i < objs.size(); i++) {
			if (((FrameObject) objs.get(i)).isSurface()) {
				surface = (FrameObject) objs.get(i);
				return true;
			}
		}
		return false;
	}

	public boolean isAnimationDone() {
		return sprite.isAnimationDone();
	}

	public Vector getOrigin() {
		return getPosition().plus(8, 14);
	}

	public ImageSheet getNormalSheet() {
		if (hurtFlickering && (hurtTimer / 4) % 2 == 0)
			return sheetHurt;
		return sheetNormal;
	}

	public boolean isPassable() {
		return passable;
	}
	
	public boolean isControllable() {
		return controllable;
	}



	// ==================== MUTATORS ==================== //

	public boolean bumpOverride(Vector sourcePos) {
		bumped = true;
		bumpTimer = 0;
		bumpTime = bumpDuration;
		controllable = false;
		velocity.zero();

		if (sourcePos != null) {
			double dir = GMath.direction(getCenter(), sourcePos);
			dir = (Math.round(dir / (GMath.QUARTER_PI * 0.5)))
					* (GMath.QUARTER_PI * 0.5);
			velocity.setPolar(1, dir).negate();
		}

		return true;
	}

	public boolean bump(Vector sourcePos) {
		if (!bumped && bumpable)
			return bumpOverride(sourcePos);
		return false;
	}

	public boolean damage(int damageAmount, Vector damageSource) {
		if (!hurt && (!bumpable || bump(damageSource))) {
			if (damageAmount > 0) {
				Sounds.play(soundDamage);
				hurt = true;
				hurtFlickering = true;
				hurtTimer = 0;
				health.take(damageAmount);
				onDamage();
			}
			return true;
		}
		return false;
	}

	public void setAnimation(Animation animation) {
		setAnimation(new DynamicAnimation(animation));
	}

	public void setAnimation(boolean looped, Animation animation) {
		setAnimation(looped, new DynamicAnimation(animation));
	}

	public void setAnimation(DynamicAnimation animation) {
		sprite.newAnimation(animation);
	}

	public void setAnimation(boolean looped, DynamicAnimation animation) {
		sprite.newAnimation(animation);
		sprite.setLooped(looped);
	}
	
	public void changeAnimation(Animation animation) {
		changeAnimation(new DynamicAnimation(animation));
	}
	
	public void changeAnimation(DynamicAnimation animation) {
		sprite.changeAnimation(animation);
	}
	
	public void setPassable(boolean passable) {
		this.passable = passable;
	}
	
	public void setDir(int dir) {
		this.dir = dir;
	}

	public void setHealth(int health) {
		this.health.set(health);
	}

	public void setMaxHealth(int maxHealth) {
		health.setMax(maxHealth);
	}
	
	protected GridTile getHole() {
		ArrayList<GridTile> tiles = Collision.getTilesMeeting(
				hardCollidable, CollisionBox.TILE_BOX);
		for (int i = 0; i < tiles.size(); i++) {
			GridTile t = tiles.get(i);
			if (t.isHole())
				return t;
		}
		return null;
	}
	
	@Override
	protected void checkTiles() {
		super.checkTiles();
		slipping = false;
		inGrass  = false;

		ArrayList<Entity> objs = Collision.getInstancesMeeting(
				getOrigin(), this, FrameObject.class);

		// Check for surfaces.
		for (int i = 0; i < objs.size(); i++) {
			FrameObject obj = (FrameObject) objs.get(i);
			if (obj.isSurface()) {
				surface = (FrameObject) objs.get(i);
			}
			if (obj instanceof ObjectGrass)
				inGrass = true;
		}

		// Check slipping.
		if (surface == null && onGround()) {
			GridTile t = getHole();
			
			if (t != null && fallsInHoles) {
				slipping = true;
				slipTimer++;
				if (position.advanceToward(t.getPosition(), slipSpeed))
					onFallInHole(null);
			}
		}
		
		if (!slipping)
			slipTimer = 0;
	}

	protected void endBump() {
		controllable = true;
		bumped = false;
		velocity.zero();
	}



	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	protected boolean checkIfFallen() {
		if (slipping || isOnSurface() || inAir())
			return false;
		return super.checkIfFallen();
	}
	
	@Override
	public void update() {
		updateZPosition();
		updateDepth();
		checkTiles();
		if (!slipping)
			checkIfFallen();

		sprite.setSheet(sheetNormal);
		if (bumped) {
			bumpTimer++;
			if (bumpTimer > bumpTime)
				endBump();
		}
		if (hurt || hurtFlickering) {
			hurtTimer++;
			if (hurt && hurtTimer > hurtDuration) {
				hurt = false;
				if (health.empty())
					die();
			}
			if (hurtTimer > flickerDuration)
				hurtFlickering = false;
			if (hurtFlickering && (hurtTimer / 4) % 2 == 0)
				sprite.setSheet(sheetHurt);
		}
	}

	@Override
	public void postUpdate() {
		Vector velPrev = new Vector(velocity);
		if (surface != null) {
			velocity.add(surface.getVelocity());
			surface = null;
		}
		
		super.postUpdate();
		
		if (Math.abs(velocity.x) > Math.abs(velPrev.x) || GMath.sign(velocity.x) != GMath.sign(velPrev.x))
			velocity.x = velPrev.x;
		if (Math.abs(velocity.y) > Math.abs(velPrev.y) || GMath.sign(velocity.y) != GMath.sign(velPrev.y))
			velocity.y = velPrev.y;
		
		if (inGrass) {
			spriteGrass.setSpeed(velocity.length());
			spriteGrass.update();
		}
	}

	@Override
	public void postDraw() {
		super.postDraw();
		if (inGrass)
			Draw.drawSprite(spriteGrass, getCenter());
		drawWadeEffect();
	}

	@Override
	protected void updateSprite() {
		if (sprite != null)
			sprite.update(faceDir);
	}
	
	@Override
	public Vector getShadowDrawPosition() {
		return position.plus(8, 14);
	}

	@Override
	public Vector getCenter() {
		return position.plus(8, 8);
	}
}
