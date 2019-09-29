package com.base.game.wolf3d.tile.enemy;

import com.base.engine.common.GMath;
import com.base.engine.common.Vector2f;
import com.base.game.wolf3d.Sprite;
import com.base.game.wolf3d.tile.Unit;
import com.base.game.wolf3d.tile.pickups.Pickup;

public abstract class Enemy extends Unit {
	protected Sprite sprite;
	protected int orientation;
	
	protected Pickup drop;
	protected int scoreValue;
	protected float range;
	protected float patrolSpeed;
	protected float speed;
	protected float hitSize;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
    public Enemy(String name) {
    	super(name, null);
    	
    	hitSize       = 0.5f;
    	collisionSize = 0.5f;
    	orientation   = 0;
    	sprite = new Sprite();
    }
    
    
    // =================== ACCESSORS =================== //
    
    public boolean canSeePlayer() {
    	return getLevel().castRay(getPosition(), getPlayer().getPosition()) == null;
    }
    
    public float getHitSize() {
		return hitSize;
	}
    
    public int getOrientation() {
		return orientation;
	}
    
    public Pickup getDrop() {
		return drop;
	}
    
    public Sprite getSprite() {
		return sprite;
	}
    
    
    
    // ==================== MUTATORS ==================== //
   
    public void pain() {
    	
    }
    
    public void updateAI(float delta) {
    	
    }
    
    
    
    // ================ IMPLEMENTATIONS ================ //
    
    @Override
    public void onDie() {
		// Create a drop.
    	Pickup drop = getDrop();
		if (drop != null) {
    		getLevel().addObject(drop.clone(), getTransform().getPosition().plus(0, 0, 0.05f));
		}
		
		// Add score and destroy.
		getGame().addScore(scoreValue);
		destroy();
    }
    
    @Override
    public void update(float delta) {
    	super.update(delta);
    	
		// Calculate orientation.
		Vector2f vecToPlayer = getPlayer().getPosition().minus(getPosition());
		Vector2f myDir       = getDirection();
		Vector2f dirToPlayer = vecToPlayer.normalized();
		float angle = GMath.angleBetween(myDir.getDirection(), dirToPlayer.getDirection());
		orientation = GMath.mod((int) ((angle / GMath.TWO_PI) * 8 + 0.5f), 8);
		
		// Update AI.
		updateAI(delta);
		
		// Check collisions.
		updateMovement();
		
    	// Update animation.
    	sprite.update(delta, orientation);
    	setDrawTexture(sprite.getCurrentFrame());
    }
}
