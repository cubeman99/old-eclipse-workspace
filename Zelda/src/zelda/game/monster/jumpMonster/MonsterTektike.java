package zelda.game.monster.jumpMonster;

import zelda.common.graphics.Animation;
import zelda.common.util.GMath;


public class MonsterTektike extends JumpMonster {
	public MonsterTektike(int typeColor) {
		super(typeColor);

		health.fill(typeColor == COLOR_ORANGE ? 1 : 2);
		contactDamage = 2;

		moveSpeed = 1;
		stopTimeMin = 80;
		stopTimeMax = 120;
		motionDuration = 80 + GMath.random.nextInt(40);
		jumpVelocityMin = 2;
		jumpVelocityMax = 2;
		gravity = 0.08;

		int x = typeColor == COLOR_ORANGE ? 12 : 10;
		animationIdle = new Animation().addFrame(16, x, 8).addFrame(16, x + 1,
				8);
		animationJump = new Animation(x + 1, 8);
		sprite.newAnimation(animationIdle);
	}

	@Override
	public MonsterTektike clone() {
		return new MonsterTektike(typeColor);
	}
}
