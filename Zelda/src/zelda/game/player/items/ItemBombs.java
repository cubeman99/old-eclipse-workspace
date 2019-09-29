package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.graphics.Sprite;
import zelda.game.entity.Bomb;
import zelda.game.entity.Entity;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.player.Player;


public class ItemBombs extends AbstractItemHold {
	private Entity bombObject;
	private boolean bombExploded;

	public ItemBombs(Player player) {
		super(player, "Bombs");
		name[0] = "Bombs";
		description[0] = "Very explosive.";
		rewardMessages[0] = "You got a <red>Bomb Bag<red>!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;

		ammo[0] = player.currencyBombs;

		collectableAmmoAmount = 5;
		collectableSprites[0] = new Sprite(Resources.SHEET_PLAYER_ITEMS, 2, 8)
				.setOrigin(8, 12);

		setSlotIcons(Resources.SHEET_ICONS_THIN, 13, 0);

		pickupGrabTime = 4;
		bombObject = null;
		bombExploded = false;
	}

	public void setBombExploded(boolean bombExploded) {
		this.bombExploded = bombExploded;
	}

	public void setBombObject(Entity bombObject) {
		this.bombObject = bombObject;
	}

	public void pickupBomb(Bomb b) {
		bombObject = b;
		bombExploded = false;
		b.setGame(player.getGame());
		pickupObject(b);
	}

	@Override
	public void update() {
		if (bombObject != null && bombObject.isDestroyed())
			bombObject = null;

		if (keyPressed() && !player.isBusy() && !player.isItemBusy()
				&& bombObject == null && hasAmmo()) {
			pickupBomb(new Bomb(player));
			useAmmo();
		}

		if (holding && bombObject != null && !bombExploded)
			((Bomb) bombObject).burnFuse();

		if (!player.isBusy() && !player.isItemBusy() && !holding && !pickingUp
				&& bombObject != null && !bombExploded
				&& (keyPressed() || player.itemBracelet.keyPressed())
				&& Collision.isTouching((Bomb) bombObject, player)) {
			player.getGame().removeEntity(bombObject);
			pickupBomb((Bomb) bombObject);
		}

		super.update();
	}
}
