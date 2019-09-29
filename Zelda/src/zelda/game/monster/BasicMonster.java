package zelda.game.monster;

import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.util.GMath;



public abstract class BasicMonster extends Monster {
	protected int motionTimer;
	protected int motionDuration;
	protected int pauseTimer;
	protected int pauseDuration;
	protected boolean paused;

	protected int typeColor;
	protected int typeItem;

	protected double speed;
	protected boolean dirBasedMovement;
	protected int numDirectionAngles;
	protected int stopTimeMin;
	protected double moveSpeed;
	protected int stopTimeMax;
	protected int moveTimeMin;
	protected int moveTimeMax;
	protected boolean flying;

	// protected int shootTimeMin;
	// protected int shootTimeMax;
	// protected int shootOdds;
	// protected MonsterProjectile projectile;



	// ================== CONSTRUCTORS ================== //

	public BasicMonster() {
		this(TYPE_NONE, TYPE_NONE);
	}

	public BasicMonster(int typeColor, int typeItem) {
		super();

		this.typeColor = typeColor;
		this.typeItem = typeItem;

		dirBasedMovement = true;
		numDirectionAngles = 4;
		moveSpeed = 0.5;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMin = 30;
		moveTimeMax = 80;
		reboundOffFrameEdge = false;
		reboundOffWalls = false;
		speed = 0;
		flying = false;

		motionTimer = 0;
		motionDuration = 0;
		pauseTimer = 0;
		pauseDuration = 0;
		paused = false;
		moving = false;

		sprite = new Sprite(Resources.SHEET_MONSTERS);
		sprite.newAnimation(Animations.createStrip(12, 0, 22, 0));
	}


	
	// =================== ACCESSORS =================== //

	protected boolean inLineWithPlayer(double leeway) {
		return (Math.abs(position.y - game.getPlayer().getPosition().y) < leeway
			|| Math.abs(position.x - game.getPlayer().getPosition().x) < leeway);
	}
	
	
	
	// ==================== MUTATORS ==================== //

	protected void facePlayer() {
		faceDir = (int) (GMath.direction(getCenter(), game.getPlayer()
				.getCenter())
				/ GMath.HALF_PI + 0.5) % 4;
		if (dirBasedMovement)
			dir = faceDir;
	}

	protected void faceRandomDirection() {
		faceDir = GMath.random.nextInt(4);
		if (dirBasedMovement)
			dir = faceDir;
	}

	protected void pause(int duration) {
		paused = true;
		pauseTimer = 0;
		pauseDuration = duration;
		velocity.zero();
	}

	protected void endPause() {
		paused = false;
	}

	protected void startMoving() {
		startMoving(GMath.random.nextInt(numDirectionAngles));
	}

	protected void startMoving(int moveDir) {
		moving = true;
		motionTimer = 0;
		motionDuration = moveTimeMin;
		if (stopTimeMax - moveTimeMin > 0)
			motionDuration += GMath.random.nextInt(moveTimeMax - moveTimeMin);
		speed = moveSpeed;

		dir = moveDir;
		if (dirBasedMovement)
			faceDir = dir;
		velocity.setPolar(moveSpeed, moveDir
				* (GMath.TWO_PI / numDirectionAngles));
	}

	protected void stopMoving() {
		moving = false;
		motionTimer = 0;
		motionDuration = stopTimeMin;
		if (stopTimeMax - stopTimeMin > 0)
			motionDuration += GMath.random.nextInt(stopTimeMax - stopTimeMin);
		velocity.zero();

	}

	protected void chasePlayer() {
		velocity.setPolar(0.5,
				GMath.direction(getCenter(), game.getPlayer().getCenter()));
	}

	protected void roam() {
		if (moving) {
			velocity.setPolar(speed, dir * (GMath.TWO_PI / numDirectionAngles));
						
			if (reboundOffWalls) {
				Vector velPrev = new Vector(velocity);
				
				velocity.y = 0;
				if (placeColliding()) {
					velPrev.x = -velPrev.x;
				}

				velocity.set(0, velPrev.y);
				if (placeColliding())
					velPrev.y = -velPrev.y;
				
				velocity.set(velPrev);
				dir = (int) ((velocity.direction() / (GMath.TWO_PI / numDirectionAngles)) + 0.5);
			}
			

			if (motionTimer > motionDuration)
				stopMoving();
		}
		else {
			if (motionTimer > motionDuration)
				startMoving();
		}

		motionTimer++;
	}

	public void updateMotion() {
		roam();
	}

	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void updateAI() {
		if (onGround() || flying) {
			if (paused) {
				pauseTimer++;
				if (pauseTimer > pauseDuration)
					endPause();
			}
			else
				updateMotion();
		}
	}
}
