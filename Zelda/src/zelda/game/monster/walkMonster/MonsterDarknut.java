package zelda.game.monster.walkMonster;

import zelda.common.reactions.ReactionCause;


public class MonsterDarknut extends MonsterMoblin {

	public MonsterDarknut(int typeColor, int typeItem) {
		super(typeColor, typeItem);

		health.fill(3);

		moveSpeed = 0.5;
		stopTimeMin = 0;
		stopTimeMax = 15;
		moveTimeMax = 40;
		moveTimeMin = 80;
		aimProjectiles = true;
		projectileShootOdds = 3;
		contactDamage = 4;

		setReaction(ReactionCause.BOOMERANG, new EFFECT_CLING());

		if (typeColor == COLOR_BLUE)
			health.fill(5);

		int y = 0;
		if (typeColor == COLOR_BLUE)
			y = 1;
		else if (typeColor == COLOR_GOLD)
			y = 2;
		createMoveStandAnimations(0, 4 + y, 2, 4);
	}

	@Override
	public MonsterDarknut clone() {
		return new MonsterDarknut(typeColor, typeItem);
	}
}
