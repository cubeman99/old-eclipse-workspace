package zelda.game.player.items;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.Entity;
import zelda.game.entity.EntityObject;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.effect.EffectDirt;
import zelda.game.entity.object.FrameObject;
import zelda.game.monster.Monster;
import zelda.game.player.Player;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;


public class ItemShovel extends Item {
	private boolean digging;
	private int digTimer;


	public ItemShovel(Player player) {
		super(player, "Shovel");
		name[0] = "Shovel";
		description[0] = "A handy tool.";
		rewardMessages[0] = "You got the <red>Shovel<red>! Now start digging!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;

		setSlotIcons(Resources.SHEET_ICONS_THIN, 15, 0);

		digging = false;
		digTimer = 0;
	}

	@Override
	public void interrupt() {
		digging = false;
		player.setBusy(false);
		player.resetAnimation();
	}

	@Override
	public void update() {
		if (digging) {
			digTimer++;

			if (digTimer == 3) {
				Frame frame = player.getGame().getFrame();
				boolean dug = false;
				Vector effectPos = new Vector();
				
				// Find the tile position to dig.
				int dir = player.getDir();
				Vector v;
				if (dir % 2 == 0)
					v = player.getCenter().plus(dir == 0 ? 6 : -6, 4);
				else
					v = player.getCenter().plus(0, dir == 3 ? 7 : -7);

				Point tilePos = new Point(v.scaledByInv(16));
				GridTile t = frame.getGridTile(tilePos);
				
				// First check for frame objects to dig up.
				Vector p = player.getCenter().plus(
						Vector.polarVector(8, player.getDir() * GMath.HALF_PI));
				for (Entity e : frame.getEntities()) {
					if (e instanceof FrameObject) {
						FrameObject obj = (FrameObject) e;
						if (obj.getVect().contains(p)) {
							if (obj.dig()) {
								dug = true;
								effectPos = new Vector(obj.getCenter());
							}
						}
					}
				}
				
				// Otherwise, attempt to dig up a background tile. 
				if (t != null && !dug) {
					if (t.isEnabled() && t.dig()) {
						dug = true;
						effectPos = new Vector(tilePos).scale(16).add(8, 8);
					}
				}
				
				// Create dig effect and sounds.
				if (dug) {
					player.getGame().addEntity(
							new EffectDirt(effectPos, player.getDir()));

					EntityObject e = player.getPurse().dropsDig
							.createDropObject();
					if (e != null) {
						e.digUp(new Vector(tilePos.scaledBy(16)).plus(8, 12),
								player.getDir());
						player.getGame().addEntity(e);
					}
					Sounds.ITEM_SHOVEL.play();
				}
				else {
					Sounds.EFFECT_CLING.play();
				}
				
				// Trigger monster shovel reactions.
				Monster m = (Monster) Collision
						.getInstanceMeeting(
								player,
								player.getPosition().plus(
										Direction.lengthVector(4,
												player.getFaceDir())),
								Monster.class);
				if (m != null) {
					m.triggerReaction(ReactionCause.SHOVEL, this);
				}
			}
			if (digTimer >= 24) {
				digging = false;
				player.setBusy(false);
				player.resetAnimation();
			}
		}
		if (keyPressed() && player.onGround() && (digging || !player.isBusy())
				&& !player.isItemBusy()) {
			digging = true;
			digTimer = 0;
			player.setBusy(true);
			player.setAnimation(false, Animations.PLAYER_DIG);
		}
	}

	@Override
	public void onEnd() {

	}
}
