package com.base.game.wolf3d.tile;

import com.base.engine.common.GMath;
import com.base.engine.common.Rect2f;
import com.base.engine.common.Vector2f;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Texture;
import com.base.game.wolf3d.LevelFileData;
import com.base.game.wolf3d.tile.mesh.MeshTile;

public abstract class Unit extends ObjectTile {
	protected int health;
	protected int healthMax;
	protected float collisionSize;
	protected Vector2f velocity;
	protected float fov;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Unit(String name, Texture texture) {
		super(name, texture, true);
		
		velocity = new Vector2f();
		fov = GMath.HALF_PI;
	}
	
	
	
	// ============== OVERRIDABLE METHODS ============== //
	
	public void onHeal(int amount) {}
	public void onDamage(int amount) {}
	public void onDie() {}
	
	

	// =================== ACCESSORS =================== //

    public boolean canSee(Vector2f v) {
		Vector2f vec = v.minus(getPosition());
		Vector2f dir = getDirection();
		float angle  = GMath.smallestAngleBetween(dir.getDirection(), vec.getDirection());
    	return (angle <= fov * 0.5f && getLevel().castRay(getPosition(), v) == null);
    }
    
    public float getFOV() {
		return fov;
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}
	
	public float getCollisionSize() {
		return collisionSize;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getHealthMax() {
		return healthMax;
	}
	
	public boolean isAtFullHealth() {
		return (health >= healthMax);
	}
	
	public boolean isDead() {
		return (health <= 0);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void heal(int amount) {
		health = Math.max(0, Math.min(healthMax, health + amount));
		onHeal(amount);
	}
	
	public void damage(int amount) {
		boolean dead = (health >= 0 && health - amount <= 0);
		health = Math.max(0, Math.min(healthMax, health - amount));
		onDamage(amount);
		if (dead)
			onDie();
	}
	
	protected void initHealth(int healthMax) {
		this.healthMax = healthMax;
		this.health = healthMax;
	}
	

	
	protected void checkCollisions() {
		Rect2f box = getCollisionBox();
		
		// Collide with walls.
		for (int y = 0; y < getLevel().getData().getHeight(); y++) {
			for (int x = 0; x < getLevel().getData().getWidth(); x++) {
    			if (LevelFileData.isWall(getLevel().getData().getData(x, y))) {
    				checkCollision(box, new Rect2f(x, y, 1, 1));
    			}
			}
		}
		
		// Collide with mesh tiles.
		for (MeshTile tile : getLevel().getMeshTiles()) {
			if (tile.isSolid())
				checkCollision(box, tile.getCollisionBox());
		}
		
		// Collide with object tiles.
		for (SceneObject object : getLevel().getObjects()) {
			if (object instanceof Tile && object != this) {
				Tile tile = (Tile) object;
				if (tile.isSolid())
    				checkCollision(box, tile.getCollisionBox());
			}
		}
		
		// Collide with player
		if (getPlayer() != this)
			checkCollision(box, getPlayer().getCollisionBox());
	}
	
	protected void checkCollision(Rect2f box, Rect2f solid) {
		Rect2f newBox = new Rect2f(box.getPosition().plus(velocity), box.getSize());
		
		if (newBox.getMaxX() > solid.getMinX() &&
			newBox.getMinX() < solid.getMaxX() && 
			box.getMaxY() > solid.getMinY() &&
			box.getMinY() < solid.getMaxY())
		{
			velocity.x = 0;
		}

		if (newBox.getMaxY() > solid.getMinY() &&
			newBox.getMinY() < solid.getMaxY() &&
			box.getMaxX() > solid.getMinX() &&
			box.getMinX() < solid.getMaxX())
		{
			velocity.y = 0;
		}
	}
	
	protected void updateMovement() {
		checkCollisions();
		setPosition(getPosition().plus(velocity));
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //

	@Override
	public Rect2f getCollisionBox() {
		return new Rect2f(
				getTransform().getPosition().x - (collisionSize / 2),
				getTransform().getPosition().z - (collisionSize / 2),
				collisionSize, collisionSize);
	}
}
