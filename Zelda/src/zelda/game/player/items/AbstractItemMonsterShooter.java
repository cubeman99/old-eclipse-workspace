package zelda.game.player.items;

import zelda.game.entity.projectile.Projectile;
import zelda.game.player.Player;


public abstract class AbstractItemMonsterShooter extends Item {
	private static final int SHOOT_TIME = 9;

	private boolean shooting;
	private int shootTimer;


	public AbstractItemMonsterShooter(Player player, String defaultName) {
		super(player, defaultName);

		entityTracker.setMaxNumEntities(1);
		shooting = false;
		shootTimer = 0;
	}

	protected abstract Projectile getProjectile();

	private void shoot() {
		if (hasAmmo() && entityTracker.canMakeEntity()) {
			shooting = true;
			shootTimer = 0;
			useAmmo();
			player.setBusy(true);

			Projectile p = getProjectile();
			player.getGame().addEntity(p);
			entityTracker.makeEntity(p);
		}
	}

	@Override
	public void interrupt() {
		shooting = false;
		player.setBusy(false);
	}

	@Override
	public void update() {
		super.update();

		if (shooting) {
			if (shootTimer++ >= SHOOT_TIME) {
				shooting = false;
				player.setBusy(false);
			}
		}
		else if (!player.isBusy() && !player.isItemBusy() && keyPressed()) {
			shoot();
		}
	}
}
