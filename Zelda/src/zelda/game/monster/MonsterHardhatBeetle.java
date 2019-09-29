package zelda.game.monster;

import zelda.common.Resources;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;


public class MonsterHardhatBeetle extends BasicMonster {

	public MonsterHardhatBeetle() {
		super();

		moveSpeed = 0.375;

		sprite = new Sprite(Resources.SHEET_MONSTERS);
		sprite.newAnimation(Animations.createStrip(12, 11, 21, 2));

		setReaction(ReactionCause.FIRE, null);
		setReaction(ReactionCause.EMBER_SEED, new BUMP());
		setReaction(ReactionCause.SCENT_SEED, new BUMP());
		setReaction(ReactionCause.ROD_FIRE, null);
		setReaction(ReactionCause.ARROW, new COMBO(new PROJECTILE_CRASH(),
				new BUMP()));
		setReaction(ReactionCause.SWORD_BEAM, new COMBO(new EFFECT_CLING(true),
				new BUMP()));
		setReaction(ReactionCause.SWORD, new BUMP());
		setReaction(ReactionCause.BIGGORON_SWORD, new BUMP());
		setReaction(ReactionCause.BOOMERANG, new BUMP());
		setReaction(ReactionCause.BOMB, new BUMP());
		setReaction(ReactionCause.SWITCH_HOOK, new BUMP());
	}

	@Override
	public MonsterHardhatBeetle clone() {
		return new MonsterHardhatBeetle();
	}

	@Override
	public void updateAI() {
		chasePlayer();
	}
}
