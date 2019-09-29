package zelda.game.player.items;

import zelda.common.Resources;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.player.Player;


public class ItemFeather extends Item {

	public ItemFeather(Player player) {
		super(player, "Feather");
		numLevels = 2;

		name[0] = "Roc<ap>s Feather";
		description[0] = "A nice lift.";
		rewardMessages[0] = "You found <red>Roc<ap>s Feather<red>!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;
		name[1] = "Roc<ap>s Cape";
		description[1] = "A wind-riding cape.";
		rewardMessages[0] = "You found <red>Roc<ap>s Cape<red>!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;

		setSlotIcons(Resources.SHEET_ICONS_THIN, 2, 1, 3, 1);


	}

	@Override
	public void update() {
		if (keyPressed() && !player.isBusy() && !player.isSlipping()
				&& !player.itemBracelet.isHolding()) {
			player.jump();
		}
	}
}
