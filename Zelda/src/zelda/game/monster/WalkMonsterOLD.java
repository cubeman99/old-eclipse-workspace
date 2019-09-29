package zelda.game.monster;

import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.Entity;
import zelda.game.entity.projectile.MonsterProjectile;
import zelda.game.entity.projectile.MonsterProjectileArrow;
import zelda.game.entity.projectile.MonsterProjectileBoomerang;
import zelda.game.entity.projectile.MonsterProjectileRock;
import zelda.game.player.Player;


public abstract class WalkMonsterOLD extends Monster {
	protected int timer;
	protected int duration;
	protected int standTimeMin;
	protected int standTimeMax;
	protected int moveTimeMin;
	protected int moveTimeMax;
	protected MonsterProjectile projectile;
	protected int projectileShootOdds;
	protected boolean aimProjectiles;
	protected double moveSpeed;
	protected boolean shooting;
	protected Entity shootEntity;
	protected boolean chasing;
	protected boolean paused;
	protected Sprite spriteSword;
	protected int typeColor;
	protected int typeItem;

	protected DynamicAnimation standAnimation;
	protected DynamicAnimation moveAnimation;
	protected DynamicAnimation[] moveStandAnimations;


	public WalkMonsterOLD(int typeColor, int typeItem) {
		super();

		this.typeColor = typeColor;
		this.typeItem = typeItem;

		duration = 0;
		standTimeMin = 40;
		standTimeMax = 80;
		moveTimeMin = 40;
		moveTimeMax = 80;
		dir = GMath.random.nextInt(4);
		moving = true;
		projectileShootOdds = 5;
		aimProjectiles = false;
		moveSpeed = 0.5;
		shooting = false;
		shootEntity = null;

		spriteSword = new Sprite(Resources.SHEET_MONSTERS,
				Animations.ITEM_MONSTER_SWORD[typeColor]);

		if (typeItem == ITEM_ARROW)
			projectile = new MonsterProjectileArrow();
		else if (typeItem == ITEM_ROCK)
			projectile = new MonsterProjectileRock();
		else if (typeItem == ITEM_BOOMERANG)
			projectile = new MonsterProjectileBoomerang();
	}



	@Override
	public void draw() {
		spriteSword.setVariation(dir);
		spriteSword.setFrameIndex(sprite.getFrameIndex());

		if (typeItem == ITEM_SWORD && dir == Direction.UP)
			Draw.drawSprite(spriteSword, position.minus(0, zPosition));

		super.draw();

		if (typeItem == ITEM_SWORD && dir != Direction.UP)
			Draw.drawSprite(spriteSword, position.minus(0, zPosition));
	}

	protected void onShoot() {
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

	@Override
	public void updateAI() {
		timer++;
		sprite.setSpeed(moveSpeed);
		Player player = game.getPlayer();
		double distToPlayer = getCenter().distanceTo(player.getCenter());

		if (shootEntity != null && shootEntity.isDestroyed())
			shootEntity = null;

		if (paused) {
			if (timer > duration) {
				paused = false;
				timer = 0;
				duration = 0;
			}
		}
		else if (shooting) {
			if (typeItem == ITEM_BOOMERANG && shootEntity != null)
				duration = timer;
			if (timer > duration) {
				shooting = false;
			}
		}
		else if (chasing) {
			if (duration == 0) {
				duration = 70;
			}
			sprite.changeAnimation(moveAnimation);

			if (timer > duration) {
				chasing = true;
				paused = true;
				timer = 0;
				duration = 15;
				velocity.zero();
				sprite.changeAnimation(standAnimation);
			}
			else {
				if (distToPlayer > 48 || timer > duration) {
					chasing = false;
					moving = true;
					timer = 0;
					duration = moveTimeMin;
					velocity.zero();
				}
				velocity.setPolar(0.5,
						GMath.direction(getCenter(), player.getCenter()));
				sprite.setSpeed(1);

				dir = (int) (GMath.direction(getCenter(), player.getCenter())
						/ GMath.HALF_PI + 0.5) % 4;
			}
		}
		else {
			if (moving) {
				sprite.changeAnimation(moveAnimation);
				velocity.setPolar(moveSpeed, dir * GMath.HALF_PI);

				if (distToPlayer < 48 && typeItem == ITEM_SWORD) {
					chasing = true;
					paused = true;
					timer = 0;
					duration = 15;
					velocity.zero();
					sprite.changeAnimation(standAnimation);
				}

				// Avoid Walls.
				if (Collision.placeMeetingSolid(hardCollidable,
						position.plus(velocity))) {
					dir = (dir + GMath.random.nextInt(3)) % 4;
					velocity.setPolar(moveSpeed, dir * GMath.HALF_PI);
				}

				if (timer >= duration) {
					// Stop Moving.
					velocity.zero();
					timer = 0;
					moving = false;
					duration = standTimeMin;
					if (standTimeMax - standTimeMin > 0)
						duration += GMath.random.nextInt(standTimeMax
								- standTimeMin);

					// Shoot Projectile!
					if (projectile != null
							&& GMath.random.nextInt(projectileShootOdds) == 0) {
						onShoot();
						shooting = true;

						if (aimProjectiles) {
							dir = (int) (GMath.direction(getCenter(),
									player.getCenter())
									/ GMath.HALF_PI + 0.5) % 4;
						}
						else
							dir = GMath.random.nextInt(4);

						duration = 30;
						if (typeItem != ITEM_BOOMERANG)
							sprite.changeAnimation(standAnimation);
						MonsterProjectile p = projectile.clone();
						p.initialize(this, dir);
						game.addEntity(p);
						shootEntity = p;
					}
				}
			}
			if (!moving && !shooting) {
				sprite.changeAnimation(standAnimation);

				// Start Moving.
				if (timer >= duration) {
					timer = 0;
					moving = true;
					dir = GMath.random.nextInt(4);
					duration = moveTimeMin;
					if (moveTimeMax - moveTimeMin > 0)
						duration += GMath.random.nextInt(moveTimeMax
								- moveTimeMin);
				}
			}
		}
	}
}
