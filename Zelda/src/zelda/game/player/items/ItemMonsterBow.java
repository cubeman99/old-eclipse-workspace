package zelda.game.player.items;

import zelda.common.Resources;
import zelda.game.entity.projectile.Projectile;
import zelda.game.entity.projectile.ProjectileArrow;
import zelda.game.player.Player;


public class ItemMonsterBow extends AbstractItemMonsterShooter {

	public ItemMonsterBow(Player player) {
		super(player, "MonsterBow");

		name[0] = "Bow";
		description[0] = "Use to shoot arrows.";
		ammo[0] = player.currencyArrows; // TODO

		setSlotIcons(Resources.SHEET_ICONS_THIN, 13, 1);
		setEquippedSlotIcons(Resources.SHEET_ICONS_LARGE, 0, 3);

		entityTracker.setMaxNumEntities(1);

		twoHanded = true;
	}

	@Override
	protected Projectile getProjectile() {
		ProjectileArrow p = new ProjectileArrow(this, player.getCenter(),
				player.getZPosition(), player.getDir() * 2);
		p.getVelocity().setLength(2);
		return p;
	}
}
