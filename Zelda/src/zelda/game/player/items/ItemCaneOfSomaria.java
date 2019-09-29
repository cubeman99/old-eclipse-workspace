package zelda.game.player.items;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Settings;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.util.Direction;
import zelda.game.entity.Entity;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.effect.EffectCreateSomariaBlock;
import zelda.game.entity.object.global.ObjectSomariaBlock;
import zelda.game.player.Player;


public class ItemCaneOfSomaria extends AbstractItemSwing {

	public ItemCaneOfSomaria(Player player) {
		super(player, "CaneOfSomaria");
		name[0] = "Cane of Somaria";
		description[0] = "Used to create blocks.";
		rewardMessages[0] = "You got the <red>Cane of Somaria<red>!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;

		setSlotIcons(Resources.SHEET_ICONS_THIN, 10, 1);
	}

	@Override
	protected void onFinishSwing() {
		super.onFinishSwing();

		ArrayList<Entity> entities = player.getGame().getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e instanceof EffectCreateSomariaBlock)
				e.destroy();
			else if (e instanceof ObjectSomariaBlock) {
				((ObjectSomariaBlock) e).breakObject();
				((ObjectSomariaBlock) e).getSoundBreak().stop();
			}
		}

		Vector v = player.getCenter().plus(
				Direction.lengthVector(19, player.getDir()));
		Point p = new Point(v).scaledByInv(Settings.TS).scaledBy(Settings.TS);
		player.getGame().addEntity(
				new EffectCreateSomariaBlock(player, new Vector(p)));
	}

	@Override
	public void update() {
		super.update();

		if (keyPressed()
				&& (swinging || (!player.isBusy() && !player.isItemBusy())))
		{
			swingDefault(player.getDir(), 3, Animations.CANE_SWING,
					Animations.PLAYER_SWING);
			Sounds.ITEM_SWING.play();
		}
	}
}
