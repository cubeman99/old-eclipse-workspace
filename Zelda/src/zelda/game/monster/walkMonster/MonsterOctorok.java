package zelda.game.monster.walkMonster;

import zelda.common.reactions.ReactionCause;


public class MonsterOctorok extends WalkMonster {
	public MonsterOctorok(int typeColor) {
		super(typeColor, ITEM_ROCK);

		health.fill(1);

		moveSpeed = 0.5;
		stopTimeMin = 30;
		stopTimeMax = 60;
		moveTimeMin = 30;
		moveTimeMax = 50;
		projectileShootOdds = 5;
		aimProjectiles = (typeColor != COLOR_RED);
		contactDamage = (typeColor == COLOR_RED ? 1 : 2);

		setReaction(ReactionCause.SWORD, new DAMAGE(1, 2, 2));
		setReaction(ReactionCause.BIGGORON_SWORD, new DAMAGE(2));

		int y = 0;
		if (typeColor == COLOR_BLUE) {
			y = 1;
			health.fill(2);
			contactDamage = 2;
			aimProjectiles = true;
		}
		else if (typeColor == COLOR_GOLD) {
			y = 2;
			health.fill(25);
			contactDamage = 4;
			aimProjectiles = true;
			projectileShootOdds = 2;
			// 25 hits sword1
			// 11 hits sword3

		}
		createMoveStandAnimations(0, 1 + y, 2, 4);
	}

	@Override
	public MonsterOctorok clone() {
		return new MonsterOctorok(typeColor);
	}
}
