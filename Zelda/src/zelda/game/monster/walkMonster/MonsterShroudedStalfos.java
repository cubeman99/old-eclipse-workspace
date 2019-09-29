package zelda.game.monster.walkMonster;



public class MonsterShroudedStalfos extends MonsterMoblin {

	public MonsterShroudedStalfos(int typeItem) {
		super(COLOR_GREEN, typeItem);

		health.fill(2);
		createMoveStandAnimations(0, 7, 2, 4);
	}

	@Override
	public MonsterShroudedStalfos clone() {
		return new MonsterShroudedStalfos(typeItem);
	}
}
