package zelda.game.monster.walkMonster;

import zelda.common.graphics.Animations;
import zelda.common.graphics.DynamicAnimation;


public class MonsterSandCrab extends WalkMonster {
	public MonsterSandCrab() {
		super(TYPE_NONE, TYPE_NONE);

		health.fill(1);

		moveSpeed = 0.25;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMin = 40;
		moveTimeMax = 80;
		contactDamage = 2;


		standAnimation = new DynamicAnimation(Animations.createStrip(3, 11, 15,
				2));
		moveAnimation = standAnimation;
		sprite.newAnimation(moveAnimation);
	}

	@Override
	public MonsterSandCrab clone() {
		return new MonsterSandCrab();
	}

	@Override
	protected void startMoving() {
		super.startMoving();
		if (dir % 2 == 0)
			speed = 1.25;
		else
			speed = 0.375;
	}
}
