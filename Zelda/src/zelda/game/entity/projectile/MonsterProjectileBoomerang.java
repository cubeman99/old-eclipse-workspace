package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.game.entity.effect.EffectCling;
import zelda.game.monster.Monster;
import zelda.game.player.Player;


public class MonsterProjectileBoomerang extends MonsterProjectile {
	private boolean returning;
	private int flyTimer;
	private int flyTime;

	public MonsterProjectileBoomerang() {
		super();
		speed = 1.5;
		damage = 2;
		returning = false;
		flyTimer = 0;
		flyTime = 41;
		destroyedOutsideFrame = false;
	}

	@Override
	public void initialize(Monster monster, int dir) {
		super.initialize(monster, dir);
		angledDir = 0;
		sprite.setSheet(Resources.SHEET_MONSTER_ITEMS);
		sprite.newAnimation(Animations.ITEM_MONSTER_BOOMERANG);
		sprite.setOrigin(8, 8);
	}

	@Override
	public MonsterProjectileBoomerang clone() {
		return new MonsterProjectileBoomerang();
	}

	@Override
	public void onHitPlayer(Player player) {
		player.damage(damage, position);
		returning = true;
	}

	@Override
	public void onHitPlayerShield(Player player) {
		returning = true;
	}

	@Override
	public void crash() {
		if (!returning) {
			returning = true;
			game.addEntity(new EffectCling(position));
		}
	}

	@Override
	public void update() {
		super.update();

		if (monster == null || monster.isDestroyed()) {
			destroy();
		}
		else if (returning) {
			// Move toward the monster.
			Vector goal = monster.getCenter();
			velocity.set(new Vector(position, goal).setLength(speed));

			// Return to the monster.
			if (position.distanceTo(goal) < 2)
				destroy();
		}
		else {
			// Return on hit frame edge or after a certain amount of time.
			if (++flyTimer > flyTime || isOutsideFrame())
				returning = true;
		}
	}
}
