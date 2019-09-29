package zelda.game.monster.walkMonster;

import zelda.common.graphics.Animation;
import zelda.common.graphics.DynamicAnimation;


public class MonsterGhini extends WalkMonster {
	public MonsterGhini() {
		super(TYPE_NONE, TYPE_NONE);

		health.fill(5);

		reboundOffFrameEdge = false;
		reboundOffWalls = false;
		collideWithWorld = false;
		avoidWalls = false;
		flying = true;

		contactDamage = 2;
		moveSpeed = 0.5;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMin = 50;
		moveTimeMax = 90;

		standAnimation = new DynamicAnimation(new Animation()
				.addFrame(4, 0, 22).addFrame(4, 1, 22), new Animation()
				.addFrame(4, 2, 22).addFrame(4, 3, 22));
		moveAnimation = standAnimation;
		sprite.newAnimation(standAnimation);
	}

	@Override
	public MonsterGhini clone() {
		return new MonsterGhini();
	}

	@Override
	protected void startMoving(int moveDir) {
		super.startMoving(moveDir);
		faceDir = dir / 2;
	}

	@Override
	public void update() {
		super.update();
		zPosition = 1;
	}
}
