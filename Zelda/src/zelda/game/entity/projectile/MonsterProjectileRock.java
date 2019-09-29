package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.graphics.Animations;
import zelda.game.monster.Monster;


public class MonsterProjectileRock extends MonsterProjectileArrow {

	public MonsterProjectileRock() {
		super();
		speed = 2;
		damage = 2;
	}

	@Override
	public MonsterProjectileRock clone() {
		return new MonsterProjectileRock();
	}

	@Override
	public void initialize(Monster monster, int dir) {
		super.initialize(monster, dir);

		sprite.setSheet(Resources.SHEET_MONSTER_ITEMS);
		sprite.newAnimation(Animations.ITEM_MONSTER_ROCK);
		sprite.setOrigin(8, 8);
		animationCrash = null;
	}
}
