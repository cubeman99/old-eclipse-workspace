package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.graphics.Animations;
import zelda.game.entity.effect.DeadProjectile;
import zelda.game.monster.Monster;
import zelda.game.player.Player;


public class MonsterProjectileArrow extends MonsterProjectile {

	public MonsterProjectileArrow() {
		super();
		speed = 2;
		damage = 2;
	}

	@Override
	public MonsterProjectileArrow clone() {
		return new MonsterProjectileArrow();
	}

	@Override
	public void initialize(Monster monster, int dir) {
		super.initialize(monster, dir);

		sprite.setSheet(Resources.SHEET_MONSTER_ITEMS);
		sprite.newAnimation(Animations.ITEM_MONSTER_ARROW);
		sprite.setOrigin(8, 8);
		animationCrash = Animations.EFFECT_MONSTER_ARROW_DEAD;
	}

	@Override
	public void crash() {
		if (animationCrash == null)
			game.addEntity(new DeadProjectile(this, sprite.getAnimation()));
		else
			game.addEntity(new DeadProjectile(this, animationCrash));

		destroy();
	}

	@Override
	public void onHitPlayer(Player player) {
		player.damage(damage, position);
		destroy();
	}

	@Override
	public void update() {
		super.update();

		if (game.getPlayer().checkSwordTouch(this)) {
			Sounds.ITEM_SHIELD_DEFLECT.play();
			crash();
		}
	}
}
