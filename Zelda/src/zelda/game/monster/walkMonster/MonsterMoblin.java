package zelda.game.monster.walkMonster;


public class MonsterMoblin extends WalkMonster {

	public MonsterMoblin(int typeItem) {
		this(TYPE_NONE, typeItem);
	}

	public MonsterMoblin(int typeColor, int typeItem) {
		super(typeColor, typeItem);

		health.fill(2);

		moveSpeed = 0.5;
		stopTimeMin = 0;
		stopTimeMax = 15;
		moveTimeMax = 40;
		moveTimeMin = 80;
		projectileShootOdds = 5;
		aimProjectiles = (typeColor != COLOR_RED || typeItem == ITEM_BOOMERANG);
		contactDamage = 2;

		if (typeColor == COLOR_BLUE)
			health.fill(3);

		int y = 0;
		if (typeColor == COLOR_BLUE)
			y = 1;
		else if (typeColor == COLOR_GOLD)
			y = 2;
		createMoveStandAnimations(8, 1 + y, 2, 4);
	}

	@Override
	public MonsterMoblin clone() {
		return new MonsterMoblin(typeColor, typeItem);
	}
}
