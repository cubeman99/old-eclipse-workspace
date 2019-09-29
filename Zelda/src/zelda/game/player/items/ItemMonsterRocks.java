package zelda.game.player.items;

import zelda.common.Resources;
import zelda.game.entity.projectile.Projectile;
import zelda.game.entity.projectile.ProjectileRock;
import zelda.game.player.Player;


public class ItemMonsterRocks extends AbstractItemMonsterShooter {

	public ItemMonsterRocks(Player player) {
		super(player, "Rocks");

		name[0] = "Rocks";
		description[0] = "Spit deadly rocks.";
		ammo[0] = player.currencyArrows; // TODO

		setSlotIcons(Resources.SHEET_ICONS_THIN, 14, 1);
		setEquippedSlotIcons(Resources.SHEET_ICONS_LARGE, 1, 3);

		entityTracker.setMaxNumEntities(1);

		twoHanded = true;
	}

	@Override
	protected Projectile getProjectile() {
		return new ProjectileRock(this, player.getCenter(),
				player.getZPosition(), player.getDir() * 2);
	}
}
