package com.base.game.wolf3d.tile.enemy;

import com.base.engine.audio.AudioEngine;
import com.base.engine.audio.Sound;
import com.base.engine.common.GMath;
import com.base.engine.common.Point;
import com.base.engine.common.Vector2f;
import com.base.game.wolf3d.DynamicAnimation;
import com.base.game.wolf3d.Level;
import com.base.game.wolf3d.Room;
import com.base.game.wolf3d.Wolf3D;
import com.base.game.wolf3d.tile.ObjectSprite;
import com.base.game.wolf3d.tile.PatrolNode;
import com.base.game.wolf3d.tile.Tile;



public abstract class BasicEnemy extends Enemy {
	public static final int STAND      = 0;
	public static final int PATROL     = 1;
	public static final int PAIN       = 2;
	public static final int SHOOT      = 3;
	public static final int CHASE      = 4;
	public static final int AMBUSH     = 5;
	public static final int NUM_STATES = 6;
	
	protected DynamicAnimation animStand;
	protected DynamicAnimation animWalk;
	protected DynamicAnimation animDie;
	protected DynamicAnimation animPain1;
	protected DynamicAnimation animPain2;
	protected DynamicAnimation animDraw;
	protected DynamicAnimation animShoot;
	protected int state;
	protected int numShots;
	protected int shotIndex;
	protected float shootTimer;
	protected float chaseTimer;
	protected Path path;
	protected int pathNodeIndex;
	
	protected float animationSpeed;
	protected float patrolAnimSpeed;
	protected float chaseAnimSpeed;
	protected float drawSpeed;
	protected float shootDelay;
	protected float aimDelay;
	protected int minShots;
	protected int maxShots;
	protected int damageFrameIndex;
	protected boolean feelsPain;
	
	protected Sound soundAlert;
	
	

	// ================== CONSTRUCTORS ================== //

	public BasicEnemy(String name) {
		super(name);
		
		
		
		state          = STAND;
		numShots       = 1;
		shotIndex      = 0;
		shootTimer     = 0;
		chaseTimer     = 0;
		path           = null;
		pathNodeIndex  = 0;

		fov              = GMath.QUARTER_PI * 3;
		animationSpeed   = 7;
		patrolAnimSpeed  = 4;
		chaseAnimSpeed   = 7;
		drawSpeed        = 5;
		damageFrameIndex = 0;
		scoreValue       = 100;
		range            = 50;
		speed            = 1.5f;
		patrolSpeed      = 0.5f;
		aimDelay         = 0.7f;
		shootDelay       = 1.0f;
		minShots         = 1;
		maxShots         = 1;
		feelsPain        = false;
		drop             = null;
		initHealth(25);
    	
		sprite.setSpeed(7);
		
		soundAlert = null;
	}
	
	@Override
	public void initialize() {
		super.initialize();
		
		setDirection(getData().getInt("direction", 0) * GMath.QUARTER_PI);
		state = getData().getInt("state", state);
		sprite.newAnimation(true, animStand);
	}
	
	

	// =================== ACCESSORS =================== //
	
	public int getState() {
		return state;
	}
	
	public int calculateDamage() {
		float dist = getPlayer().getPosition().distanceTo(getPosition());
		return hitscan(getPlayer().isSprinting(), getPlayer().canSee(getPosition()), dist);
	}
	
	public int hitscan(boolean isSprinting, boolean inSight, float dist) {
		int speed  = (isSprinting ? 160 : 256);
		int look   = (inSight ? 16 : 8);
		int rand1  = GMath.random.nextInt(256);
		int damage = 0;
		
		// Check if it was a hit.
		if (rand1 < (speed - (dist * look))) {
    		int rand2 = GMath.random.nextInt(256);
    		if (dist < 2)
    			damage = rand2 / 4;
    		else if (dist < 4)
    			damage = rand2 / 8;
    		else
    			damage = rand2 / 16;
		}
		
		return damage;
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void alert() {
		if (state == STAND || state == PATROL) {
//			System.out.println(name + " alerted!"); // TODO
			startChasing();
			AudioEngine.playSound(soundAlert, this);
		}
	}
	
	protected void startChasing() {
		state = CHASE;
		chaseTimer = 0;
		sprite.newAnimation(true, animWalk);
		sprite.setSpeed(7);
	}
	
	protected void startShooting() {
		shootTimer = 0;
		shotIndex  = 0;
		numShots   = minShots;
		if (maxShots > minShots)
			numShots += GMath.random.nextInt(maxShots - minShots);
		state      = SHOOT;
		sprite.newAnimation(false, animDraw); // Draw weapon.
		
		if (aimDelay == 0)
			shoot();
	}
	
	protected void shoot() {
		sprite.newAnimation(false, animShoot);
		shootTimer = shootDelay;
		shotIndex++;
		if (damageFrameIndex == 0)
			damagePlayer();
	}
	
	protected void damagePlayer() {
		if (!getPlayer().isDead()) {
			int damage = calculateDamage();
			
			if (damage > 0)
				getPlayer().takeDamage(this, damage);
		}
	}
	
	private void createPatrolPath() {
		Level level = getLevel();
		Point start = new Point(getPosition());
		Point dir   = new Point(getDirection().times(1.5f));
		Point pos   = start.plus(dir);
		path        = new Path();
		path.addNode(start.x + 0.5f, start.y + 0.5f);
		
		for (int i = 0; i < 100 && !pos.equals(start); i++) {
			if (!level.inBounds(pos)) {
				path = null;
				break;
			}
			Tile tile = level.getTile(pos);
			
			if (tile instanceof PatrolNode) {
				path.addNode(pos.x + 0.5f, pos.y + 0.5f);
				dir.set(tile.getDirection().times(1.5f));
				i = 0;
			}
			pos.add(dir);
		}
	}
	
	public void updateState(float delta) {
		Vector2f vecToPlayer = getPlayer().getPosition().minus(getPosition());
		Vector2f myDir       = getDirection();
		Vector2f dirToPlayer = vecToPlayer.normalized();
		float distToPlayer   = vecToPlayer.length();
		float angle          = GMath.smallestAngleBetween(myDir.getDirection(), dirToPlayer.getDirection());
		boolean canSeePlayer = canSeePlayer();
		
		sprite.setSpeed(animationSpeed);
		velocity.zero();
		
		// Stand idly and check if player is in sight.
    	if (state == STAND) {
    		if (angle < fov / 2 && canSeePlayer) {
    			alert();
    		}
    	}
    	
		// Stunned for a small amount of time.
    	else if (state == PAIN) {
    		sprite.setSpeed(animationSpeed);
    		if (sprite.isAnimationDone(delta))
    			startChasing();
    	}

		// Wait to ambush player once seen.
    	else if (state == AMBUSH) {
    		if (angle < fov / 2 && canSeePlayer) {
    			state = STAND;
    			alert();
    		}
    	}
    	
		// Follow a patrol path.
    	else if (state == PATROL) {
    		if (path == null)
    			createPatrolPath();
    		if (path == null)
    			state = STAND;
    		else {
        		Vector2f node = path.getNode(pathNodeIndex);
        		Vector2f vecToNode = node.minus(getPosition());
        		if (vecToNode.length() < 0.1f) {
        			pathNodeIndex = (pathNodeIndex + 1) % path.size();
        			node = path.getNode(pathNodeIndex);
            		vecToNode = node.minus(getPosition());
        		}
        		
        		velocity = vecToNode.lengthVector(patrolSpeed * delta);
        		setDirection(velocity.normalized());
    			sprite.changeAnimation(true, animWalk);
        		sprite.setSpeed(patrolAnimSpeed);
        		
        		if (angle < fov / 2 && canSeePlayer) {
        			alert();
        		}
    		}
    	}
    	
		// Move toward the player.
    	else if (state == CHASE) {
			setDirection(dirToPlayer);
			velocity.set(dirToPlayer.normalized().times((speed) * delta));
			shootTimer -= delta;

    		sprite.setSpeed(chaseAnimSpeed);
			
    		// Check if in range to shoot.
    		if (distToPlayer < range && canSeePlayer() && (shootTimer <= 0 || distToPlayer < 1)) {
    			startShooting();
    		}
    	}
    	
		// Shoot at the player.
    	else if (state == SHOOT) {
    		sprite.setSpeed(animationSpeed);
    		if (sprite.getDynamicAnimation() == animDraw) {
    			sprite.setSpeed(drawSpeed);
    			shootTimer += delta;
    			
    			if (shootTimer > aimDelay) {
    				shoot();
    			}
    		}
    		else {
    			shootTimer -= delta;
    			
    			if (damageFrameIndex > 0 && sprite.getFrameIndex() == damageFrameIndex && sprite.getLastFrameIndex() < damageFrameIndex) {
    				damagePlayer();
    			}
    			
        		if (sprite.isAnimationDone(delta) && shootTimer <= 0) {
            		// Shoot if in range.
        			if ((distToPlayer <= 1 || shotIndex < numShots) && canSeePlayer()) {
        				shoot();
            		}
        			// Else, return to chase state.
        			else {
        				state = CHASE;
        				shootTimer = 0.5f + GMath.random.nextFloat() * 0.8f;
        				sprite.newAnimation(true, animWalk);
        			}
        		}
    		}
    	}
	}
	

	
	// ================ IMPLEMENTATIONS ================ //
    
    @Override
    public void onDamage(int amount) {
    	if (feelsPain) {
    		state = PAIN;
    		sprite.newAnimation(false, GMath.random.nextBoolean() ? animPain1 : animPain2);
    	}
    	else {
        	alert();
    	}

		// Alert enemies in the same room.
		Room room = getRoom();
		if (room != null) {
			room.alertEnemies();
		}
		getLevel().alertEnemies(getPosition());
    }
    
	@Override
	public void onDie() {
		super.onDie();

		// Create a corpse.
		ObjectSprite corpse = new ObjectSprite(getGame(), 0.85f, 0.85f, animDie, 10);
		corpse.getTransform().setPosition(getTransform().getPosition());
		getLevel().addObject(corpse);
	}
	
	@Override
	public void updateAI(float delta) {
		updateState(delta);
	}
}
