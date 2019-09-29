package zelda.game.monster.walkMonster;

import zelda.common.graphics.Animation;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.reactions.ReactionCause;


public class MonsterArmos extends WalkMonster {
	public MonsterArmos(int typeColor) {
		super(typeColor, TYPE_NONE);

		health.fill(2);

		bumpable = false;
		moveSpeed = 0.5;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMin = 50;
		moveTimeMax = 90;
		contactDamage = 1;

		setReaction(ReactionCause.BOMB, new KILL());
		setReaction(ReactionCause.SCENT_SEED, new DAMAGE(1));
		setReaction(ReactionCause.BIGGORON_SWORD, new COMBO(
				new BUMP_OVERRIDE(), new DAMAGE(3)));
		setReaction(ReactionCause.BOOMERANG, new IF_LEVEL(1, new COMBO(
				new DAMAGE(1), new STUN())));

		setReaction(ReactionCause.SHOVEL, new PLAYER_BUMP());
		setReaction(ReactionCause.SWORD, new PLAYER_BUMP(new EFFECT_CLING()));
		setReaction(ReactionCause.FIRE, null);
		setReaction(ReactionCause.EMBER_SEED, null);
		setReaction(ReactionCause.ROD_FIRE, null);
		setReaction(ReactionCause.GALE_SEED, null);
		setReaction(ReactionCause.SWITCH_HOOK, new EFFECT_CLING());
		setReaction(ReactionCause.SWORD_BEAM, new EFFECT_CLING(true));
		setReaction(ReactionCause.ARROW, new PROJECTILE_CRASH());

		if (typeColor == COLOR_BLUE) {
			health.fill(3);
			setReaction(ReactionCause.SWORD, new DAMAGE(1));
			setReaction(ReactionCause.BOOMERANG, new IF_LEVEL(1, new COMBO(
					new DAMAGE(2), new STUN())));
		}

		int y = (typeColor == COLOR_RED ? 23 : 24);
		standAnimation = new DynamicAnimation(new Animation()
				.addFrame(10, 0, y).addFrame(10, 1, y));
		moveAnimation = standAnimation;
		sprite.newAnimation(standAnimation);
	}

	@Override
	public MonsterArmos clone() {
		return new MonsterArmos(typeColor);
	}

}
