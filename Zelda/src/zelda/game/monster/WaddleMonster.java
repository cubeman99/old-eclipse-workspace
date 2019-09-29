package zelda.game.monster;

import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.util.GMath;


public abstract class WaddleMonster extends Monster {
	protected int timer;
	protected int duration;
	protected int intervalMin;
	protected int intervalMax;
	protected double numDirectionAngles;
	protected double moveSpeed;


	public WaddleMonster() {
		super();

		timer = 0;
		duration = 0;
		intervalMin = 0;
		intervalMax = 0;
		numDirectionAngles = 4;
		moveSpeed = 0.5;

		sprite = new Sprite(Resources.SHEET_MONSTERS);
		sprite.newAnimation(Animations.createStrip(12, 0, 22, 0));
	}

	@Override
	public void updateAI() {
		if (onGround()) {
			timer++;

			if (Collision.placeMeetingSolid(hardCollidable,
					position.plus(velocity.x, 0)))
				velocity.x = -velocity.x;
			if (Collision.placeMeetingSolid(hardCollidable,
					position.plus(0, velocity.y)))
				velocity.y = -velocity.y;

			if (timer > duration
					|| Collision.placeMeetingSolid(hardCollidable,
							position.plus(velocity))) {
				duration = intervalMin;
				if (intervalMax - intervalMin > 0)
					duration += GMath.random.nextInt(intervalMax - intervalMin);

				timer = 0;

				double div = GMath.TWO_PI / numDirectionAngles;
				double dir = GMath.random.nextDouble() * GMath.TWO_PI;
				dir = Math.round(dir / div) * div;
				velocity.setPolar(moveSpeed, dir);
			}
		}
	}
}
