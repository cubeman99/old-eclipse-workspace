package zelda.game.entity;

import java.awt.Color;
import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Settings;
import zelda.common.Sounds;
import zelda.common.collision.Collidable;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.game.control.GameInstance;
import zelda.game.entity.effect.Effect;
import zelda.game.entity.effect.EffectFallingObject;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;
import zelda.main.Keyboard;
import zelda.main.Sound;


/**
 * EntityObject.
 * 
 * @author David Jordan
 */
public class EntityObject extends FrameEntity implements Collidable, Cloneable {
	// Visual
	protected int baseDepth;
	protected Sprite sprite;

	// Position and Velocity
	protected Vector position;
	protected Vector velocity;
	protected double zPosition;
	protected double zVelocity;
	protected double gravity;

	// Collision
	protected boolean solid;
	protected Collidable hardCollidable;
	protected Collidable softCollidable;
	protected CollisionBox hardCollisionBox;
	protected CollisionBox softCollisionBox;
	protected double collisionDodgeDistance;
	protected double collisionDodgeSpeed;
	protected boolean colliding;
	protected boolean[] collidingDirs;
	protected boolean passedOverLedge;
	protected boolean passingOverLedge;
	
	// Sound
	protected Sound soundBounce;
	
	// Flags
	protected boolean collideWithWorld;
	protected boolean collideWithFrameBoundaries;
	protected boolean collisionAutoDodge;
	protected boolean collisionDodgeable;
	protected boolean ledgePassable; // Whether the object can pass over ledges
	protected boolean halfSolidPassable; // Whether the object can pass over half-solids (ex: railings)
	protected boolean reboundOffFrameEdge;
	protected boolean reboundOffWalls;
	protected boolean affectedByGravity;
	protected boolean destroyedOutsideFrame;
	protected boolean destroyedInHoles;
	protected boolean drawShadow;
	protected boolean bounces;
	protected boolean dynamicDepth;
	protected boolean inPuddle;



	// ================== CONSTRUCTORS ================== //

	public EntityObject(GameInstance game) {
		this();
		this.game = game;
	}

	public EntityObject() {
		super();

		this.position               = new Vector();
		this.velocity               = new Vector();
		this.zPosition              = 0;
		this.zVelocity              = 0;
		this.colliding              = false;
		this.collidingDirs          = new boolean[] {false, false, false, false};
		this.inPuddle               = false;
		this.collisionDodgeDistance = 8;
		this.collisionDodgeSpeed    = 1;
		this.passedOverLedge        = false;
		this.passingOverLedge       = false;
		
		this.hardCollisionBox = null;
		this.softCollisionBox = new CollisionBox(-1, -1, 2, 2);
		this.sprite           = new Sprite();
		this.baseDepth        = 0;
		this.gravity          = Settings.GRAVITY;

		this.solid                      = false;
		this.collideWithWorld           = true;
		this.collideWithFrameBoundaries = false;
		this.collisionAutoDodge         = false;
		this.collisionDodgeable         = true;
		this.ledgePassable              = true;
		this.halfSolidPassable          = true;
		this.reboundOffFrameEdge        = false;
		this.reboundOffWalls            = false;
		this.affectedByGravity          = true;
		this.destroyedOutsideFrame      = true;
		this.destroyedInHoles           = true;
		this.drawShadow                 = true;
		this.bounces                    = true;
		this.dynamicDepth               = true;

		hardCollidable = new Collidable() {
			public boolean isSolid() {
				return false;
			}

			public Vector getPosition() {
				return position;
			}

			public CollisionBox getCollisionBox() {
				return hardCollisionBox;
			}
		};

		softCollidable = new Collidable() {
			public boolean isSolid() {
				return false;
			}

			public Vector getPosition() {
				return position;
			}

			public CollisionBox getCollisionBox() {
				return softCollisionBox;
			}
		};

		setDepth(baseDepth);
	}



	// =================== ACCESSORS =================== //

	public boolean inAir() {
		return (zPosition > 0 || zVelocity > 0);
	}

	public boolean onGround() {
		return !inAir();
	}

	public Point getLocation() {
		return new Point(position.scaledByInv(16).add(0.5, 0.5));
	}

	public Vector getVelocity() {
		return velocity;
	}

	public double getZPosition() {
		return zPosition;
	}

	public Vector getCenter() {
		return position;
	}
	
	public Vector getOrigin() {
		return position;
	}

	public double getZVelocity() {
		return zVelocity;
	}

	public Sprite getSprite() {
		return sprite;
	}
	
	public boolean isLedgePassable() {
		return ledgePassable;
	}
	
	public boolean isHalfSolidPassable() {
		return halfSolidPassable;
	}
	
	public boolean isPassingOverLedge() {
		return passingOverLedge;
	}
	
	public boolean hasPassedOverLedge() {
		return passedOverLedge;
	}
	
	public boolean isCollisionDodgeable() {
		return collisionDodgeable;
	}

	public double getCollisionDodgeDistance() {
		return collisionDodgeDistance;
	}

	public boolean autoCollisionDodges() {
		return collisionAutoDodge;
	}

	public boolean reboundsOffWalls() {
		return reboundOffWalls;
	}

	public Collidable getHardCollidable() {
		return hardCollidable;
	}

	public Collidable getSoftCollidable() {
		return softCollidable;
	}

	public CollisionBox getHardCollisionBox() {
		return hardCollisionBox;
	}

	public CollisionBox getSoftCollisionBox() {
		return hardCollisionBox;
	}

	public boolean isNearZ(EntityObject other) {
		return (Math.abs(zPosition - other.getZPosition()) < 4); // TODO: magic
																 // number!
	}

	public boolean isOutsideFrame() {
		return (position.x < 0 || position.y < 0
				|| position.x > game.getFrame().getWidth() * 16 || position.y > game
				.getFrame().getHeight() * 16);
	}

	public boolean placeOutsideOfFrame(Collidable c, Vector pos) {
		Vectangle v = new Vectangle(c, pos);
		Vectangle vf = frame.getVect();
		return (v.getX1() < 0 || v.getX2() > vf.getX2() || v.getY1() < 0 || v
				.getY2() > vf.getY2());
	}

	public boolean placeOutsideOfFrame(Vector pos) {
		return placeOutsideOfFrame(hardCollidable, pos);
	}

	public Vector getShadowDrawPosition() {
		return position;
	}



	// ==================== MUTATORS ==================== //

	public void setPosition(Vector position) {
		this.position.set(position);
	}

	public void setPositionByCenter(Vector newCenter) {
		position.set(newCenter.minus(getCenter().minus(getPosition())));
	}

	public void setVelocity(Vector velocity) {
		this.velocity.set(velocity);
	}

	public void setZPosition(double zPosition) {
		this.zPosition = zPosition;
	}

	public void setZVelocity(double zVelocity) {
		this.zVelocity = zVelocity;
	}
	
	public void setDrawShadow(boolean drawShadow) {
		this.drawShadow = drawShadow;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void setColliding(boolean colliding) {
		this.colliding = colliding;
	}
	
	public void setCollideWithWorld(boolean collideWithWorld) {
		this.collideWithWorld = collideWithWorld;
	}
	
	public void setCollideWithFrameBoundaries(boolean collideWithFrameBoundaries) {
		this.collideWithFrameBoundaries = collideWithFrameBoundaries;
	}
	
	public void setCollisionAutoDodge(boolean collisionAutoDodge) {
		this.collisionAutoDodge = collisionAutoDodge;
	}
	
	public void setCollisionDir(int dir, boolean colliding) {
		collidingDirs[dir] = colliding;
	}
	
	public void setPassedOverLedge(boolean passedOverLedge) {
		this.passedOverLedge = passedOverLedge;
	}
	
	public void setPassingOverLedge(boolean passingOverLedge) {
		this.passingOverLedge = passingOverLedge;
	}

	public void digUp(Vector pos, int dir) {
		position.set(pos);
		velocity.set(Direction.lengthVector(0.6, dir));
		zVelocity = 1.5;
	}

	protected void land() {
		onLand();
	}

	protected void bounce() {
		if (zVelocity < -1.00) {
			zPosition = 0.1;
			zVelocity = -zVelocity * 0.5;
		}
		else {
			zVelocity = 0;
			velocity.zero();
		}
		Sounds.play(soundBounce);

		if (velocity.length() > 0.25)
			velocity.scale(0.5);
		else
			velocity.zero();
	}

	protected boolean checkIfFallen() {
		if (zPosition > 0)
			return false;
		
		GridTile t = Collision.getTile(getOrigin());
		if (t != null) {
    		if (t.isHole()) {
    			EffectFallingObject e = null;
    			if (destroyedInHoles) {
    				destroy();
    				e = new EffectFallingObject(getCenter());
    				Sounds.EFFECT_FALL.play();
    				game.addEntity(e);
    			}
    			onFallInHole(e);
    			return true;
    		}
    		else if (t.isWater()) {
    			if (destroyedInHoles) {
    				destroy();
        			game.addEntity(new Effect(
        					Resources.SPRITE_EFFECT_SPLASH_WATER, getCenter()));
        			Sounds.PLAYER_WADE.play();
    			}
    			onFallInWater();
    			return true;
    		}
    		else if (t.isLava()) {
    			if (destroyedInHoles) {
    				destroy();
    				game.addEntity(new Effect(
    						Resources.SPRITE_EFFECT_SPLASH_LAVA, getCenter()));
    				Sounds.PLAYER_WADE.play();
    			}
    			onFallInLava();
    			return true;
    		}
		}
		
		
		return false;
	}

	protected void updateZPosition() {
		// Update z position and velocity.
		if (zPosition > 0 || zVelocity != 0) {
			// Add the z-velocity to the z-position.
			zPosition = Math.max(zPosition + zVelocity, 0);

			// Apply gravity.
			if (affectedByGravity) {
				zVelocity = Math.max(-Settings.TERMINAL_Z_VELOCITY, zVelocity
						- gravity);
			}

			// Check if landed.
			if (zPosition <= 0 && zVelocity < 0) {
				checkIfFallen();
				if (!isDestroyed())
					land();
				zPosition = 0;
				if (bounces && !isDestroyed())
					bounce();
				else
					zVelocity = 0;

			}
		}
		else
			zVelocity = 0;
	}

	protected void updateDepth() {
		if (dynamicDepth)
			setDepth(baseDepth - (int) position.y);
	}


	protected void checkTiles() {
		inPuddle = false;
		GridTile t = Collision.getTile(getOrigin());
		
		if (onGround() && t != null) {
			if (t.getProperties().getBoolean("puddle", false))
				inPuddle = true;
		}
	}
	
	protected void performBoundaryCollisions(Collidable c) {
		CollisionBox box = c.getCollisionBox();
		if (box != null) {
			Vectangle v  = new Vectangle(c);
			Vectangle vf = frame.getVect();

			if (v.getX1() < 0) {
				position.x = -box.getBox().corner.x;
				velocity.x = (reboundOffFrameEdge ? -velocity.x : 0);
			}
			else if (v.getX2() > vf.getX2()) {
				position.x = vf.getX2() - v.getWidth()- box.getBox().corner.x;
				velocity.x = (reboundOffFrameEdge ? -velocity.x : 0);
			}
			if (v.getY1() < 0) {
				position.y = -box.getBox().corner.y;
				velocity.y = (reboundOffFrameEdge ? -velocity.y : 0);
			}
			else if (v.getY2() > vf.getY2()) {
				position.y = vf.getY2() - v.getHeight() - box.getBox().corner.y;
				velocity.y = (reboundOffFrameEdge ? -velocity.y : 0);
			}
		}
	}
	
	protected void performSolidCollisions() {
		Collision.performCollisions(this);
	}
	
	protected void performCollisions() {
		if (collideWithWorld)
			performSolidCollisions();
		if (collideWithFrameBoundaries)
			performBoundaryCollisions(hardCollidable);
	}

	protected void updatePosition() {
		position.add(velocity);
	}

	protected void updateSprite() {
		if (sprite != null)
			sprite.update();
	}

	protected void drawWadeEffect() {
		if (inPuddle)
			Draw.drawSprite(Resources.SPRITE_EFFECT_WADE, getCenter());
	}



	// ============== DO-NOTHING METHODS ============== //

	public void onRebound() {}
	
	protected void onLand() {}

	protected void onFallInHole(EffectFallingObject e) {}

	protected void onFallInWater() {}

	protected void onFallInLava() {}



	// =============== INHERITED METHODS =============== //

	@Override
	public void update() {
		// Update falling.
		updateZPosition();

		// Set the depth.
		updateDepth();

		// Check if fallen in a hole/water/lava.
		if (!isDestroyed())
			checkIfFallen();

		// Destroy when outside of frame.
		if (destroyedOutsideFrame && isOutsideFrame())
			destroy();
		
		// Check special tiles like puddles.
		checkTiles();
	}

	@Override
	public void postUpdate() {
		// Perform Collisions.
		performCollisions();

		// Add the velocity to the position.
		updatePosition();
		
		// Update sprite and animation.
		updateSprite();
	}

	@Override
	public void draw() {
		if (sprite != null)
			Draw.drawSprite(sprite, position.minus(0, zPosition));
	}

	@Override
	public void preDraw() {
		if (zPosition > 0 && drawShadow)
			Draw.drawSprite(Resources.SPRITE_SHADOW, getShadowDrawPosition());
	}

	@Override
	public void postDraw() {
		drawWadeEffect();

		// TODO: DEBUG
		if (Keyboard.F4.down()) {
			if (softCollisionBox != null) {
				Draw.drawVect(softCollisionBox.getVect(position), Color.GREEN);
			}
			if (hardCollisionBox != null) {
				Draw.drawVect(hardCollisionBox.getVect(position), Color.RED);
			}
		}
	}



	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		Draw.drawSprite(sprite, pos);
	}
	
	@Override
	public EntityObject clone() {
		return null;
	}

	@Override
	public CollisionBox getCollisionBox() {
		return softCollisionBox;
	}

	@Override
	public Vector getPosition() {
		return position;
	}

	@Override
	public boolean isSolid() {
		return solid;
	}
}
