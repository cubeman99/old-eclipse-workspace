package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.graphics.Animations;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.player.Player;


public class ItemShield extends Item {
	private boolean blocking;

	public ItemShield(Player player) {
		super(player, "Shield");
		numLevels = 3;

		name[0] = "Wooden Shield";
		description[0] = "A small shield.";
		rewardMessages[0] = "You got the <red>Wooden Shield<red>!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;
		name[1] = "Iron Shield";
		description[1] = "A large shield.";
		rewardMessages[1] = "You got the <red>Iron Shield<red>!";
		rewardHoldTypes[1] = CollectableReward.TYPE_TWO_HAND;
		name[2] = "Mirror Shield";
		description[2] = "A reflective shield.";
		rewardMessages[2] = "You got the <red>Mirror Shield<red>!";
		rewardHoldTypes[2] = CollectableReward.TYPE_TWO_HAND;

		setSlotIcons(Resources.SHEET_ICONS_THIN, 3, 0, 4, 0, 5, 0);

		blocking = false;
	}

	public boolean isBlocking() {
		return blocking;
	}

	@Override
	public void onChangeLevel() {
		if (!player.isBusy() && !player.isItemBusy()) {
			if (!blocking)
				player.setMoveStandAnimations(Animations.PLAYER_SHIELD[level > 0 ? 1
						: 0]);
			else
				player.setMoveStandAnimations(Animations.PLAYER_SHIELD_HOLD[level > 0 ? 1
						: 0]);

		}
	}
	
	@Override
	public void interrupt() {
		if (blocking) {
    		blocking = false;
			player.setDefaultMoveStandAnimations(
					Animations.PLAYER_SHIELD[level > 0 ? 1 : 0]);
		}
	}

	@Override
	public void onStart() {
		player.setDefaultMoveStandAnimations(
				Animations.PLAYER_SHIELD[level > 0 ? 1 : 0]);
	}

	@Override
	public void update() {
		if (blocking) {
			if (!keyDown() || player.isBusy() || player.isItemBusy()) {
				blocking = false;
				player.setDefaultMoveStandAnimations(
						Animations.PLAYER_SHIELD[level > 0 ? 1 : 0]);
			}
		}
		else if (keyDown() && !player.isBusy() && !player.isItemBusy()) {
			blocking = true;
			player.setDefaultMoveStandAnimations(
					Animations.PLAYER_SHIELD_HOLD[level > 0 ? 1 : 0]);
			if (keyPressed())
				Sounds.ITEM_SHIELD.play();
		}
	}

	@Override
	public void onEnd() {
		blocking = false;
		player.setDefaultMoveStandAnimations(Animations.PLAYER_DEFAULT);
		player.resetMoveStandAnimations();
	}
}
