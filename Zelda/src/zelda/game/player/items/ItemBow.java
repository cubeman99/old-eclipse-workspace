package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.game.entity.Entity;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.projectile.ProjectileArrow;
import zelda.game.player.Player;


public class ItemBow extends Item {
	private boolean shooting;

	public ItemBow(Player player) {
		super(player, "Bow");
		name[0] = "Wooden Bow";
		description[0] = "Weapon of a marksman.";
		ammo[0] = player.currencyArrows;
		rewardMessages[0] = "You got a <red>Wooden Bow<red>!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;

		usedInMineCart = true;
		collectableAmmoAmount = 5;
		collectableSprites[0] = new Sprite(Resources.SHEET_PLAYER_ITEMS, 0, 11)
				.setOrigin(8, 9);

		setSlotIcons(Resources.SHEET_ICONS_THIN, 13, 1);
		entityTracker.setMaxNumEntities(2);

		shooting = false;
	}

	private void shoot() {
		if (hasAmmo() && entityTracker.canMakeEntity()) {
			shooting = true;
			useAmmo();

			player.setBusy(true);
			player.setAnimation(false, Animations.PLAYER_THROW);

			Entity e = new ProjectileArrow(this, player.getPosition()
					.plus(8, 8), player.getZPosition(), player.getAngledDir());

			player.getGame().addEntity(e);
			entityTracker.makeEntity(e);
			
			Sounds.MONSTER_SHOOT.play();
		}
	}

	@Override
	public void interrupt() {
		shooting = false;
		player.setBusy(false);
		player.resetAnimation();
	}

	@Override
	public void update() {
		super.update();

		if (shooting) {
			if (player.isAnimationDone()) {
				shooting = false;
				player.resetAnimation();
				player.setBusy(false);
			}
		}
		else if (!player.isBusy() && !player.isItemBusy() && keyPressed()) {
			shoot();
		}
	}
}
