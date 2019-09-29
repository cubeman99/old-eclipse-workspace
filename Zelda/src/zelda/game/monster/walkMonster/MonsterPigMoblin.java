package zelda.game.monster.walkMonster;


public class MonsterPigMoblin extends MonsterMoblin {

	public MonsterPigMoblin(int typeColor, int typeItem) {
		super(typeColor, typeItem);

		health.fill(1);
		if (typeColor == COLOR_BLUE)
			health.fill(2);
		
		int y = (typeColor == COLOR_BLUE ? 1 : 0);
		createMoveStandAnimations(8, 4 + y, 2, 4);
	}

	@Override
	public MonsterPigMoblin clone() {
		return new MonsterPigMoblin(typeColor, typeItem);
	}
}
