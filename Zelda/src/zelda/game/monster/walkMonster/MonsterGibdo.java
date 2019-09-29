package zelda.game.monster.walkMonster;

import zelda.common.graphics.Animation;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.reactions.ReactionCause;
import zelda.game.monster.Monster;
import zelda.game.monster.MonsterStalfos;


public class MonsterGibdo extends WalkMonster {
	public MonsterGibdo() {
		super(TYPE_NONE, TYPE_NONE);

		health.fill(4);

		bumpable = false;
		moveSpeed = 0.375;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMin = 50;
		moveTimeMax = 90;
		contactDamage = 2;

		setReaction(ReactionCause.FIRE, new BURN(0));
		setReaction(ReactionCause.EMBER_SEED, new BURN(0));
		setReaction(ReactionCause.ROD_FIRE, new BURN(0));

		standAnimation = new DynamicAnimation(new Animation()
				.addFrame(6, 4, 20).addFrame(6, 5, 20));
		moveAnimation = standAnimation;
		sprite.newAnimation(standAnimation);
	}

	@Override
	public MonsterGibdo clone() {
		return new MonsterGibdo();
	}

	@Override
	protected void endBurn() {
		super.endBurn();
		// TODO: Create an orange staflos.

		destroy();
		Monster m = new MonsterStalfos();
		m.setPosition(position);
		game.addEntity(m);
	}
}
