package entity.weapons;

import entity.Entity;
import game.CollisionType;
import geometry.Rectangle;
import geometry.Vector;
import graphics.Sprite;
import graphics.library.Library;

import java.awt.Graphics2D;

import tile.Tile;
import world.Room;

public class ThrowableObject extends Entity {
	
	// ======================= Members ========================
	

	// ===================== Constructors =====================
	
	/** Constructs the default entity. */
	public ThrowableObject() {
		super();
	}
	/** Constructs an entity with the given id. */
	public ThrowableObject(String id) {
		super(id);
	}
	/** Initializes the entity and sets up the container variables. */
	public void initialize(Room room) {
		super.initialize(room);
	}
	
	// ======================= Updating =======================
	
	/** Called every step, before collision, to update the entity's state. */
	public void preupdate() {
		super.preupdate();
	}
	/** Called every step to check collisions. */
	public void updateCollissions() {
		super.updateCollissions();
	}
	/** Called every step, before movement, to update the entity's state. */
	public void update() {
		super.update();
	}
	/** Called every step to move the entity. */
	public void updateMovement() {
		moveEntity();
	}
	/** Called every step, after movement, to update the entity's state. */
	public void postupdate() {
		super.postupdate();
	}
	/** Called every step, after drawing, to update the entity's state. */
	public void postdraw() {
		super.postdraw();
	}
	
	// ======================= Drawing ========================
	
	/** Called every step to draw the entity in the room shallow. */
	public void draw(Graphics2D g, Vector point) {
		drawSurfaceOverlay(g, point);
	}
	
	// ====================== Collision =======================
	
	/** Returns true if the entity is colliding with a wall in the specified direction. */
	protected boolean isCollidingInDirection(int direction) {
		return colliding[direction];
	}
	/** Returns true if the entity is colliding with the ground. */
	protected boolean isCollidingWithGround() {
		return (z <= 0.0);
	}
	/** Translates the entity the distance of its velocity and z speed. */
	protected void moveEntity() {
		position.add(velocity);
		z += zspeed;
	}
	/** Adds gravity to the z speed. */
	protected void addGravity() {
		zspeed -= 0.15;
	}
	/** Bounces the entity in the colliding direction with the specified speed. */
	protected void bounceInDirection(Vector speed) {
		if (colliding[2])
			velocity.x = speed.x;
		if (colliding[3])
			velocity.y = speed.y;
		if (colliding[0])
			velocity.x = -speed.x;
		if (colliding[1])
			velocity.y = -speed.y;
	}
	/** Bounces the entity on the ground. */
	protected void bounceGround() {
		zspeed = -zspeed * 0.5;
		velocity.scale(0.5);
		if (zspeed < 0.05)
			zspeed = 0.0;
		if (Math.abs(velocity.x) < 0.05)
			velocity.x = 0.0;
		if (Math.abs(velocity.y) < 0.05)
			velocity.y = 0.0;
	}
	/** Called to prevent the entity from getting stuck in solid objects. */
	protected void moveToCollideWithTiles() {
		Rectangle[] sideBounds = new Rectangle[4];
		sideBounds[0] = new Rectangle(bounds.point.plus(bounds.size.x - 1, 0), new Vector(1, bounds.size.y));
		sideBounds[1] = new Rectangle(bounds.point.plus(0, bounds.size.y - 1), new Vector(bounds.size.x, 1));
		sideBounds[2] = new Rectangle(bounds.point, new Vector(1, bounds.size.y));
		sideBounds[3] = new Rectangle(bounds.point, new Vector(bounds.size.x, 1));

		Vector newVelocity = new Vector(velocity);
		
		for (int i = 0; i < 4; i++)
			colliding[i] = false;
		
		// Check collisions to the left
		if (room.isCollidingWithTile(position.plus(velocity.x, 0), sideBounds[2], CollisionType.allSolid) &&
				velocity.x < 0) {
			colliding[2] = true;
			// Reduce velocity to prevent collision
			for (int i = 0; i >= (int)velocity.x ; i--) {
				if (!room.isCollidingWithTile(position.plus(velocity.x - i, 0), sideBounds[2], CollisionType.allSolid)) {
					newVelocity.x = velocity.x - i;
					break;
				}
				else if (i == (int)velocity.x) {
					newVelocity.x = 0;
				}
			}
		}
		// Check collisions to the top
		if (room.isCollidingWithTile(position.plus(0, velocity.y), sideBounds[3], CollisionType.allSolid) &&
				velocity.y < 0) {
			colliding[3] = true;
			// Reduce velocity to prevent collision
			for (int i = 0; i >= (int)velocity.y ; i--) {
				if (!room.isCollidingWithTile(position.plus(0, velocity.y - i), sideBounds[3], CollisionType.allSolid)) {
					newVelocity.y = velocity.y - i;
					break;
				}
				else if (i == (int)velocity.y) {
					newVelocity.y = 0;
				}
			}
		}
		// Check collisions to the right
		if (room.isCollidingWithTile(position.plus(velocity.x, 0), sideBounds[0], CollisionType.allSolid) &&
				velocity.x > 0) {
			colliding[0] = true;
			// Reduce velocity to prevent collision
			for (int i = 0; i <= (int)velocity.x ; i++) {
				if (!room.isCollidingWithTile(position.plus(velocity.x - i, 0), sideBounds[0], CollisionType.allSolid)) {
					newVelocity.x = velocity.x - i;
					break;
				}
				else if (i == (int)velocity.x) {
					newVelocity.x = 0;
				}
			}
		}
		// Check collisions to the bottom
		if (room.isCollidingWithTile(position.plus(0, velocity.y), sideBounds[1], CollisionType.allSolid) &&
				velocity.y > 0) {
			colliding[1] = true;
			// Reduce velocity to prevent collision
			for (int i = 0; i <= (int)velocity.y ; i++) {
				if (!room.isCollidingWithTile(position.plus(0, velocity.y - i), sideBounds[1], CollisionType.allSolid)) {
					newVelocity.y = velocity.y - i;
					break;
				}
				else if (i == (int)velocity.y) {
					newVelocity.y = 0;
				}
			}
		}
		
		velocity = newVelocity;
	}
	/** Called to prevent the entity from walking on solid entities. */
	protected void moveToCollideWithEntities() {
		Rectangle[] sideBounds = new Rectangle[4];
		sideBounds[0] = new Rectangle(bounds.point.plus(bounds.size.x - 1, 0), new Vector(1, bounds.size.y));
		sideBounds[1] = new Rectangle(bounds.point.plus(0, bounds.size.y - 1), new Vector(bounds.size.x, 1));
		sideBounds[2] = new Rectangle(bounds.point, new Vector(1, bounds.size.y));
		sideBounds[3] = new Rectangle(bounds.point, new Vector(bounds.size.x, 1));
		
		Vector newVelocity = new Vector(velocity);
		
		Entity[] entityList = room.getEntityCollisions(position.plus(velocity), bounds, CollisionType.allSolid);
		
		// Respond to all currently colliding entities
		for (Entity entity : entityList) {
			// Check collisions to the left
			if (room.isCollidingWithEntity(position.plus(velocity.x, 0), sideBounds[2], entity) &&
				velocity.x - entity.velocity.x < 0) {
				// Reduce velocity to prevent collision
				for (int i = 0; i >= (int)(velocity.x - entity.velocity.x); i--) {
					if (!room.isCollidingWithEntity(position.plus(velocity.x - i, 0), sideBounds[2], entity)) {
						newVelocity.x = velocity.x - i;
						break;
					}
					else if (i == (int)(velocity.x - entity.velocity.x)) {
						newVelocity.x = entity.velocity.x;
					}
				}
			}
			// Check collisions to the top
			if (room.isCollidingWithEntity(position.plus(0, velocity.y), sideBounds[3], entity) &&
				velocity.y - entity.velocity.y < 0) {
				// Reduce velocity to prevent collision
				for (int i = 0; i >= (int)(velocity.y - entity.velocity.y); i--) {
					if (!room.isCollidingWithEntity(position.plus(0, velocity.y - i), sideBounds[3], entity)) {
						newVelocity.y = velocity.y - i;
						break;
					}
					else if (i == (int)(velocity.y - entity.velocity.y)) {
						newVelocity.y = entity.velocity.y;
					}
				}
			}
			// Check collisions to the right
			if (room.isCollidingWithEntity(position.plus(velocity.x, 0), sideBounds[0], entity) &&
				velocity.x - entity.velocity.x > 0) {
				// Reduce velocity to prevent collision
				for (int i = 0; i <= (int)(velocity.x - entity.velocity.x); i++) {
					if (!room.isCollidingWithEntity(position.plus(velocity.x - i, 0), sideBounds[0], entity)) {
						newVelocity.x = velocity.x - i;
						break;
					}
					else if (i == (int)(velocity.x - entity.velocity.x)) {
						newVelocity.x = entity.velocity.x;
					}
				}
			}
			// Check collisions to the bottom
			if (room.isCollidingWithEntity(position.plus(0, velocity.y), sideBounds[1], entity) &&
				velocity.y - entity.velocity.y > 0) {
				// Reduce velocity to prevent collision
				for (int i = 0; i <= (int)(velocity.y - entity.velocity.y); i++) {
					if (!room.isCollidingWithEntity(position.plus(0, velocity.y - i), sideBounds[1], entity)) {
						newVelocity.y = velocity.y - i;
						break;
					}
					else if (i == (int)(velocity.y - entity.velocity.y)) {
						newVelocity.y = entity.velocity.y;
					}
				}
			}
		}
		
		velocity = newVelocity;
	}
	/** Adjusts the entity to the side of the tiles. */
	protected void adjustToSideOfTiles() {
		Vector newVelocity = new Vector(velocity);
		
		// Adjust to the side of the left tile
		if (colliding[2]) {
			Rectangle[] adjustBounds = new Rectangle[4];
			adjustBounds[0] = new Rectangle(bounds.point.plus(-1, 2), new Vector(1, 1));
			adjustBounds[1] = new Rectangle(bounds.point.plus(-1, bounds.size.y - 1), new Vector(1, 1));
			adjustBounds[2] = new Rectangle(bounds.point.plus(-1, bounds.size.y - 2), new Vector(1, 1));
			adjustBounds[3] = new Rectangle(bounds.point.plus(-1, 0), new Vector(1, 1));
			if (room.isCollidingWithTile(position, adjustBounds[0], CollisionType.allNonSolid) &&
				room.isCollidingWithTile(position, adjustBounds[1], CollisionType.allSolid)) {
				// Adjust up
				newVelocity.y = -1;
			}
			else if (room.isCollidingWithTile(position, adjustBounds[2], CollisionType.allNonSolid) &&
					 room.isCollidingWithTile(position, adjustBounds[3], CollisionType.allSolid)) {
				// Adjust down
				newVelocity.y = 1;
			}
		}
		// Adjust to the side of the top tile
		if (colliding[3]) {
			Rectangle[] adjustBounds = new Rectangle[4];
			adjustBounds[0] = new Rectangle(bounds.point.plus(1, -1), new Vector(1, 1));
			adjustBounds[1] = new Rectangle(bounds.point.plus(bounds.size.x - 1, -1), new Vector(1, 1));
			adjustBounds[2] = new Rectangle(bounds.point.plus(bounds.size.x - 2, -1), new Vector(1, 1));
			adjustBounds[3] = new Rectangle(bounds.point.plus(0, -1), new Vector(1, 1));
			if (room.isCollidingWithTile(position, adjustBounds[0], CollisionType.allNonSolid) &&
				room.isCollidingWithTile(position, adjustBounds[1], CollisionType.allSolid)) {
				// Adjust left
				newVelocity.x = -1;
			}
			else if (room.isCollidingWithTile(position, adjustBounds[2], CollisionType.allNonSolid) &&
					 room.isCollidingWithTile(position, adjustBounds[3], CollisionType.allSolid)) {
				// Adjust right
				newVelocity.x = 1;
			}
		}
		// Adjust to the side of the right tile
		if (colliding[0]) {
			Rectangle[] adjustBounds = new Rectangle[4];
			adjustBounds[0] = new Rectangle(bounds.point.plus(bounds.size.x, 2), new Vector(1, 1));
			adjustBounds[1] = new Rectangle(bounds.point.plus(bounds.size.x, bounds.size.y - 1), new Vector(1, 1));
			adjustBounds[2] = new Rectangle(bounds.point.plus(bounds.size.x, bounds.size.y - 2), new Vector(1, 1));
			adjustBounds[3] = new Rectangle(bounds.point.plus(bounds.size.x, 0), new Vector(1, 1));
			if (room.isCollidingWithTile(position, adjustBounds[0], CollisionType.allNonSolid) &&
				room.isCollidingWithTile(position, adjustBounds[1], CollisionType.allSolid)) {
				// Adjust up
				newVelocity.y = -1;
			}
			else if (room.isCollidingWithTile(position, adjustBounds[2], CollisionType.allNonSolid) &&
					 room.isCollidingWithTile(position, adjustBounds[3], CollisionType.allSolid)) {
				// Adjust down
				newVelocity.y = 1;
			}
		}
		// Adjust to the side of the bottom tile
		if (colliding[1]) {
			Rectangle[] adjustBounds = new Rectangle[4];
			adjustBounds[0] = new Rectangle(bounds.point.plus(1, bounds.size.y), new Vector(1, 1));
			adjustBounds[1] = new Rectangle(bounds.point.plus(bounds.size.x - 1, bounds.size.y), new Vector(1, 1));
			adjustBounds[2] = new Rectangle(bounds.point.plus(bounds.size.x - 2, bounds.size.y), new Vector(1, 1));
			adjustBounds[3] = new Rectangle(bounds.point.plus(0, bounds.size.y), new Vector(1, 1));
			if (room.isCollidingWithTile(position, adjustBounds[0], CollisionType.allNonSolid) &&
				room.isCollidingWithTile(position, adjustBounds[1], CollisionType.allSolid)) {
				// Adjust left
				newVelocity.x = -1;
			}
			else if (room.isCollidingWithTile(position, adjustBounds[2], CollisionType.allNonSolid) &&
					 room.isCollidingWithTile(position, adjustBounds[3], CollisionType.allSolid)) {
				// Adjust right
				newVelocity.x = 1;
			}
		}
		
		velocity = newVelocity;
	}
	/** Updates the surface the entity is on. */
	protected void updateSurface() {
		Rectangle standingBounds = new Rectangle(bounds.point.plus(bounds.size.x / 2, bounds.size.y - 2), new Vector(1, 1));
		
		int lastSurfaceType = surfaceType;
		
		if (room.isCollidingWithEntity(position, standingBounds, CollisionType.platform)) {
			// Platforms always override the tile surface
			surfaceType = CollisionType.platform;
		}
		else {
			if (z == 0) {
				Tile[] tiles = room.getTileCollisions(position, standingBounds, CollisionType.all);
				if (tiles.length == 0)
					surfaceType = CollisionType.boundary;
				else
					surfaceType = room.getTileTypeAtPosition(position.plus(standingBounds.point));
			}
			else {
				// Tile surface doesn't apply if the entity is in the air
				surfaceType = CollisionType.air;
			}
		}
		
		if (surfaceType == CollisionType.air && surfaceType != lastSurfaceType)
			surfaceSprite.setAnimation(Library.animations.shadow);
		else if (surfaceType == CollisionType.shallowWater && surfaceType != lastSurfaceType)
			surfaceSprite.setAnimation(Library.animations.wade);
		else if (surfaceType == CollisionType.grass && surfaceType != lastSurfaceType) {
			surfaceSprite.setAnimation(Library.animations.grass);
			surfaceSprite.setSpeed(0);
		}
		
	}
	/** Called every step to update the motion of the surface. */
	protected void addSurfaceMotion() {
		
		Rectangle standingBounds = new Rectangle(bounds.point.plus(4, 7), new Vector(1, 1));
		
		switch (surfaceType) {
		case CollisionType.hole:
		case CollisionType.pit:
			Vector tileCenter = room.getTileAtPosition(position.plus(standingBounds.point)).getCenter();
			Vector playerCenter = position.plus(8, 8);
			Vector distance = tileCenter.minus(playerCenter);
			//System.out.println(distance);

			// Gravitate towards the center
			double gravitateSpeed = 0.5;
			if (distance.x <= -gravitateSpeed)
				velocity.x -= gravitateSpeed;
			if (distance.y <= -gravitateSpeed)
				velocity.y -= gravitateSpeed;
			if (distance.x >= gravitateSpeed)
				velocity.x += gravitateSpeed;
			if (distance.y >= gravitateSpeed)
				velocity.y += gravitateSpeed;
			break;
			
		case CollisionType.waterLeft:
			velocity.x -= 1;
			break;
		case CollisionType.waterUp:
			velocity.y -= 1;
			break;
		case CollisionType.waterRight:
			velocity.x += 1;
			break;
		case CollisionType.waterDown:
			velocity.y += 1;
			break;
		}
	}
}
