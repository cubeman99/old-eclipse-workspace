package zelda.game.monster.jumpMonster;

import zelda.common.graphics.Animation;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.GMath;


public class MonsterPolsVoice extends JumpMonster {
	public MonsterPolsVoice() {
		super();

		health.fill(1);
		contactDamage = 2;

		moveSpeed = 0.5;
		motionDuration = 45;
		stopTimeMin = 45;
		stopTimeMax = 45;
		jumpVelocityMin = 1.3;
		jumpVelocityMax = 1.3;
		gravity = 0.06;

		animationIdle = new Animation(4, 19);
		animationJump = new Animation(5, 19);
		sprite.newAnimation(animationIdle);


		setReaction(ReactionCause.FIRE, null);
		setReaction(ReactionCause.EMBER_SEED, new BUMP());
		setReaction(ReactionCause.SCENT_SEED, new BUMP());
		setReaction(ReactionCause.ROD_FIRE, new BUMP());
		setReaction(ReactionCause.ARROW, new COMBO(new PROJECTILE_CRASH(),
				new BUMP()));
		setReaction(ReactionCause.SWORD_BEAM, new COMBO(new EFFECT_CLING(true),
				new BUMP()));
		setReaction(ReactionCause.SWORD, new BUMP());
		setReaction(ReactionCause.BIGGORON_SWORD, new BUMP());
		setReaction(ReactionCause.BOOMERANG, new BUMP());
		setReaction(ReactionCause.SWITCH_HOOK, new BUMP());
	}

	@Override
	public MonsterPolsVoice clone() {
		return new MonsterPolsVoice();
	}

	@Override
	protected void onJump() {
		// Jump in a random direction.
		velocity.setPolar(moveSpeed, GMath.random.nextInt(8) * GMath.QUARTER_PI);

		// Occasionally jump toward the player.
		if (GMath.random.nextInt(10) == 0) {
			super.onJump();
			velocity.scale(1.7);
			zVelocity *= 1.3;
		}
	}
}
