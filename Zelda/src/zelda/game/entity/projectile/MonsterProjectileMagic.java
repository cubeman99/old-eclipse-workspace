package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.graphics.Animations;
import zelda.game.monster.Monster;


public class MonsterProjectileMagic extends MonsterProjectileArrow {

	public MonsterProjectileMagic() {
		super();
		speed = 2;
		damage = 3;
	}

	@Override
	public MonsterProjectileMagic clone() {
		return new MonsterProjectileMagic();
	}

	@Override
	public void initialize(Monster monster, int dir) {
		super.initialize(monster, dir);

		sprite.setSheet(Resources.SHEET_MONSTER_ITEMS);
		sprite.newAnimation(Animations.ITEM_MONSTER_MAGIC);
		sprite.setOrigin(8, 8);
		animationCrash = null;
	}
}
