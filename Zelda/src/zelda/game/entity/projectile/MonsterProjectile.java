package zelda.game.entity.projectile;

import zelda.common.Sounds;
import zelda.common.graphics.Animation;
import zelda.common.util.GMath;
import zelda.game.monster.Monster;
import zelda.game.player.Player;


public abstract class MonsterProjectile extends Projectile {
	protected Monster monster;
	protected double speed;
	protected int damage;
	protected Animation animationCrash;


	public MonsterProjectile() {
		super();
		speed = 1;
		damage = 2;
		animationCrash = null;
	}

	@Override
	public abstract MonsterProjectile clone();

	@Override
	public void onHitPlayerShield(Player player) {
		crash();
		Sounds.ITEM_SHIELD_DEFLECT.play();
	}

	public void initialize(Monster monster, int dir) {
		this.monster = monster;

		position.set(monster.getCenter());
		angledDir = dir;
		velocity.setPolar(speed, dir * GMath.HALF_PI);
	}
}
