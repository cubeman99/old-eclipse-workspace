package zelda.game.monster.walkMonster;

import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.Entity;
import zelda.game.entity.projectile.MonsterProjectile;
import zelda.game.entity.projectile.MonsterProjectileArrow;
import zelda.game.entity.projectile.MonsterProjectileBoomerang;
import zelda.game.entity.projectile.MonsterProjectileRock;
import zelda.game.entity.projectile.MonsterProjectileSpear;
import zelda.game.monster.BasicMonster;
import zelda.game.world.Frame;



public abstract class WalkMonster extends BasicMonster {
	protected static final double CHASE_DISTANCE = 48;

	protected boolean shoots;
	protected MonsterProjectile projectile;
	protected int projectileShootOdds;
	protected boolean aimProjectiles;
	protected boolean shooting;
	protected Entity shootEntity;
	protected boolean chasing;
	protected Sprite spriteSword;
	protected boolean avoidWalls;
	protected boolean avoidFrameEdge;
	protected DynamicAnimation standAnimation;
	protected DynamicAnimation moveAnimation;
	protected DynamicAnimation[] moveStandAnimations;



	// ================== CONSTRUCTORS ================== //

	public WalkMonster(int typeColor, int typeItem) {
		super(typeColor, typeItem);

		moveSpeed = 0.5;
		stopTimeMin = 40;
		stopTimeMax = 80;
		moveTimeMin = 40;
		moveTimeMax = 80;
		numDirectionAngles = 4;
		dir = GMath.random.nextInt(4);
		projectileShootOdds = 5;
		shoots = true;
		avoidWalls = true;
		avoidFrameEdge = true;
		aimProjectiles = false;
		shooting = false;
		shootEntity = null;
		collisionAutoDodge = true;

		if (typeItem == ITEM_ARROW)
			projectile = new MonsterProjectileArrow();
		else if (typeItem == ITEM_ROCK)
			projectile = new MonsterProjectileRock();
		else if (typeItem == ITEM_SPEAR)
			projectile = new MonsterProjectileSpear();
		else if (typeItem == ITEM_BOOMERANG)
			projectile = new MonsterProjectileBoomerang();

		if (typeItem == ITEM_SWORD) {
			spriteSword = new Sprite(Resources.SHEET_MONSTERS,
					Animations.ITEM_MONSTER_SWORD[typeColor]);

			shieldCollisionBoxes = new CollisionBox[] {
					new CollisionBox(8, 10, 15, 4),
					new CollisionBox(10, -7, 4, 15),
					new CollisionBox(-7, 10, 15, 4),
					new CollisionBox(2, 7, 4, 15)};
		}
	}



	// ==================== MUTATORS ==================== //

	protected void shoot() {
		if (projectile == null)
			return;

		pause(30);
		shooting = true;

		if (typeItem == ITEM_BOOMERANG)
			sprite.changeAnimation(moveAnimation);

		MonsterProjectile p = projectile.clone();
		p.initialize(this, faceDir);
		game.addEntity(p);
		shootEntity = p;
	}

	protected void createMoveStandAnimations(int sx, int sy, int framesPerDir,
			int numDirs) {
		Animation base = new Animation();
		for (int i = 0; i < framesPerDir; i++)
			base.addFrame(6, sx + i, sy);
		moveStandAnimations = Animations.createMoveStandAnimations(base,
				framesPerDir, 0);
		moveAnimation = moveStandAnimations[0];
		standAnimation = moveStandAnimations[1];
		sprite.newAnimation(standAnimation);
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	protected void stopMoving() {
		super.stopMoving();

		sprite.changeAnimation(standAnimation);

		if (shoots && projectile != null
				&& GMath.random.nextInt(projectileShootOdds) == 0) {
			if (aimProjectiles)
				facePlayer();
			else
				faceRandomDirection();

			shoot();
		}
	}

	@Override
	protected void endPause() {
		super.endPause();
		if (shooting)
			shooting = false;
	}

	@Override
	protected void startMoving(int moveDir) {
		super.startMoving(moveDir);
		sprite.changeAnimation(moveAnimation);
	}

	@Override
	protected void roam() {
		super.roam();

		// Avoid Walls.
		if (avoidWalls && placeColliding()) {
			startMoving((dir + 1 + GMath.random.nextInt(3)) % 4);
		}

		// Avoid frame edge
		if (avoidFrameEdge && placeOutsideOfFrame(position.plus(velocity))) {
			startMoving((dir + 1 + GMath.random.nextInt(3)) % 4);
		}
	}

	@Override
	public void updateMotion() {
		sprite.setSpeed(moveSpeed);

		collisionDodgeSpeed = speed;

		if (shooting && typeItem == ITEM_BOOMERANG) {
			pauseDuration = 1;
		}

		if (chasing) {
			facePlayer();
			chasePlayer();
			motionTimer++;
			sprite.changeAnimation(moveAnimation);
			sprite.setSpeed(moveSpeed * 2);

			if (motionTimer > 60) {
				motionTimer = 0;
				sprite.newAnimation(standAnimation);
				pause(15);
				chasing = false;
				motionTimer = 0;
			}

			if (getDistanceToPlayer() > CHASE_DISTANCE) {
				chasing = false;
				motionTimer = 0;
			}
		}
		else {
			roam();

			sprite.setSpeed(moveSpeed);

			if (typeItem == ITEM_SWORD) {
				if (getDistanceToPlayer() < CHASE_DISTANCE
						&& GMath.random.nextInt(30) == 0) {
					motionTimer = 0;
					chasing = true;
					facePlayer();
					sprite.newAnimation(standAnimation);
					pause(10);
				}
			}
		}
	}

	@Override
	public void draw() {
		if (typeItem == ITEM_SWORD) {
			spriteSword.setVariation(dir);
			spriteSword.setFrameIndex(sprite.getFrameIndex());
		}
		
		if (typeItem == ITEM_SWORD && dir == Direction.UP)
			Draw.drawSprite(spriteSword, position.minus(0, zPosition));

		super.draw();

		if (typeItem == ITEM_SWORD && dir != Direction.UP)
			Draw.drawSprite(spriteSword, position.minus(0, zPosition));
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		super.drawTileSprite(pos, frame);
		if (typeItem == ITEM_ARROW)
			Draw.drawImage(Resources.SHEET_MONSTER_ITEMS, 0, 0, pos.x, pos.y);
		else if (typeItem == ITEM_BOOMERANG)
			Draw.drawImage(Resources.SHEET_MONSTER_ITEMS, 6, 0, pos.x, pos.y);
		else if (typeItem == ITEM_SWORD)
			Draw.drawImage(Resources.SHEET_MONSTERS, typeColor * 4, 0, pos.x, pos.y);
	}
}
