package zelda.game.monster.walkMonster.chargeMonster;

import zelda.common.graphics.Animation;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.reactions.ReactionCause;


public class MonsterRope extends ChargeMonster {

	public MonsterRope() {
		super();

		health.fill(1);
		contactDamage = 2;

		moveSpeed = 0.375;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMax = 40;
		moveTimeMin = 80;
		chargeSpeed = 1.25;
		chargeAcceleration = 0.04;
		dirBasedMovement = false;

		setReaction(ReactionCause.SWITCH_HOOK, new DAMAGE(1));


		standAnimation = new DynamicAnimation(new Animation().addFrame(12, 4,
				22).addFrame(12, 5, 22), new Animation().addFrame(12, 6, 22)
				.addFrame(12, 7, 22));
		moveAnimation = standAnimation;
		sprite.newAnimation(standAnimation);
	}

	@Override
	protected void startMoving(int moveDir) {
		super.startMoving(moveDir);
		if (dir % 2 == 0)
			faceDir = dir / 2;
	}

	@Override
	protected void charge() {
		super.charge();
		if (dir % 2 == 0)
			faceDir = dir / 2;
	}

	@Override
	public MonsterRope clone() {
		return new MonsterRope();
	}
}
