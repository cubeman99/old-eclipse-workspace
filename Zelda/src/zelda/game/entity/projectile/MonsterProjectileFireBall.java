package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.graphics.Animations;
import zelda.common.util.GMath;
import zelda.game.monster.Monster;
import zelda.game.player.Player;


public class MonsterProjectileFireBall extends MonsterProjectile {

	public MonsterProjectileFireBall() {
		super();
		speed = 1.5;
		damage = 2;
		collideWithWorld = false;
	}

	@Override
	public void initialize(Monster monster, int dir) {
		super.initialize(monster, dir);
		angledDir = 0;
		velocity.zero();
		sprite.setSheet(Resources.SHEET_SPECIAL_EFFECTS);
		sprite.newAnimation(Animations.EFFECT_FIREBALL);
		sprite.setOrigin(8, 8);

		velocity.setPolar(
				speed,
				GMath.direction(position, monster.getGame().getPlayer()
						.getCenter()));
	}

	@Override
	public MonsterProjectileFireBall clone() {
		return new MonsterProjectileFireBall();
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
	public void update() {
		super.update();
	}
}
