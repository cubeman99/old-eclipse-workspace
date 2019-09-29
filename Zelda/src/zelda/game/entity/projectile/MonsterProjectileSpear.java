package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.graphics.Animations;
import zelda.game.monster.Monster;
import zelda.game.player.Player;


public class MonsterProjectileSpear extends MonsterProjectileArrow {

	public MonsterProjectileSpear() {
		super();
		speed = 3;
		damage = 3;
		collideWithWorld = false;
	}

	@Override
	public MonsterProjectileSpear clone() {
		return new MonsterProjectileSpear();
	}

	@Override
	public void onHitPlayer(Player player) {
		player.damage(damage, position);
		destroy();
	}

	@Override
	public void onHitPlayerShield(Player player) {
		destroy();
	}

	@Override
	public void crash() {
		// Don't destroy...
	}

	@Override
	public void initialize(Monster monster, int dir) {
		super.initialize(monster, dir);

		sprite.setSheet(Resources.SHEET_MONSTER_ITEMS);
		sprite.newAnimation(Animations.ITEM_MONSTER_SPEAR);
		sprite.setOrigin(8, 8);
		animationCrash = null;
	}
}
