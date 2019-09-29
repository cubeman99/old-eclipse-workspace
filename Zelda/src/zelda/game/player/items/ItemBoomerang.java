package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.graphics.Animations;
import zelda.game.entity.Entity;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.projectile.ProjectileBoomerang;
import zelda.game.player.Player;


public class ItemBoomerang extends Item {
	private Entity boomerang;
	private boolean throwing;

	public ItemBoomerang(Player player) {
		super(player, "Boomerang");
		numLevels = 2;

		name[0] = "Boomerang";
		description[0] = "Always comes back to you.";
		rewardMessages[0] = "You got a <red>Boomerang<red>!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;
		name[1] = "Magic Boomerang";
		description[1] = "A remote-control weapon.";
		rewardMessages[1] = "You got the <red>Magic Boomerang<red>!";
		rewardHoldTypes[1] = CollectableReward.TYPE_TWO_HAND;

		setSlotIcons(Resources.SHEET_ICONS_THIN, 4, 1, 5, 1);

		usedInMineCart = true;
		throwing = false;
		boomerang = null;
	}

	@Override
	public void update() {
		if (boomerang != null && boomerang.isDestroyed())
			boomerang = null;

		if (throwing) {
			if (player.isAnimationDone()) {
				throwing = false;
				player.setBusy(false);
				player.resetAnimation();
			}
		}
		else if (keyPressed() && !player.isBusy() && !player.isItemBusy()
				&& boomerang == null) {
			boomerang = new ProjectileBoomerang(player, player.getPosition()
					.plus(8, 8), player.getAngledDir() * 45);
			throwing = true;
			player.setAnimation(false, Animations.PLAYER_THROW);
			player.getGame().addEntity(boomerang);
			player.setBusy(true);
		}
	}
}
