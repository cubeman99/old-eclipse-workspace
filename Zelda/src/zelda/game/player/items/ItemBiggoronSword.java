package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.player.Player;


public class ItemBiggoronSword extends AbstractItemSwing {

	public ItemBiggoronSword(Player player) {
		super(player, "BiggoronSword");
		name[0] = "Biggoron<ap>s Sword";
		description[0] = "A powerful, two-handed sword.";
		rewardMessages[0] = "You got the <red>Biggoron<ap>s Sword<red>!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;

		setSlotIcons(Resources.SHEET_ICONS_LARGE, 3, 1);

		slotEquippedIcons[0] = new Sprite(Resources.SHEET_ICONS_LARGE,
				new Animation(new AnimationFrame().addPart(4, 1, 0, 0).addPart(
						5, 1, 16, 0)));

		isSword = true;
		twoHanded = true;
		swingItemLength = 22;
	}
	
	@Override
	protected Vector getDrawOffset() {
		return new Vector();
	}
	
	@Override
	public void update() {
		super.update();

		if (keyPressed() && !player.isBusy() && !player.isItemBusy())
		{
			swingDefault(player.getDir(), 5, Animations.BIGGORON_SWORD_SWING,
					Animations.PLAYER_SWING_BIGGORON_SWORD);
			Sounds.ITEM_BIGGORON_SWORD.play();
		}
	}
}
