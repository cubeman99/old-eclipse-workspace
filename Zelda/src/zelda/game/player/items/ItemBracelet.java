package zelda.game.player.items;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.graphics.Animations;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.Direction;
import zelda.game.entity.Entity;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.object.global.ObjectSomariaBlock;
import zelda.game.monster.Monster;
import zelda.game.player.Player;
import zelda.main.Keyboard;


public class ItemBracelet extends AbstractItemHold {
	private boolean grabbing;
	private FrameObject grabObject;
	private int grabTimer;
	private boolean pulling;
	

	public ItemBracelet(Player player) {
		super(player, "Bracelet");
		numLevels = 2;

		name[0] = "Power Bracelet";
		description[0] = "A strength booster.";
		rewardMessages[0] = "You got the <red>Power Bracelet<red> Hold the button and press <pad><n>to lift heavy objects!";
		rewardHoldTypes[0] = CollectableReward.TYPE_TWO_HAND;
		name[1] = "Power Gloves";
		description[1] = "Used to lift large objects.";
		rewardMessages[1] = "You got the <red>Power Gloves<red>!";
		rewardHoldTypes[1] = CollectableReward.TYPE_TWO_HAND;


		setSlotIcons(Resources.SHEET_ICONS_THIN, 0, 1, 1, 1);

		pickupGrabTime = 7;
		grabbing = false;
		pulling  = false;
		grabObject = null;
		grabTimer = 0;
	}
	
	public boolean isGrabbing() {
		return grabbing;
	}
	
	public boolean isPulling() {
		return pulling;
	}
	
	@Override
	public void update() {
		pulling = false;
		
		if (!holding) {
			if (grabbing) {
				ArrayList<Entity> entities = Collision.getInstancesMeeting(
							player.getHardCollidable(),
							player.getPosition().plus(
							Direction.lengthVector(1,
							player.getDir())), FrameObject.class);

				for (int i = 0; i < entities.size(); i++) {
					FrameObject obj = (FrameObject) entities.get(i);
					obj.onGrab();
				}
				
				if (Keyboard.arrows[(player.getDir() + 2) % 4].down()) {
					grabTimer++;
					player.setAnimation(true, Animations.PLAYER_PULL);
					pulling = true;
					
					if (grabTimer > 10 && grabObject != null) {
						pickupObject(grabObject.pickup());
						grabbing = false;
						grabObject = null;
					}
					for (int i = 0; i < entities.size(); i++) {
						FrameObject obj = (FrameObject) entities.get(i);
						obj.onPull();
					}
				}
				else {
					grabTimer = 0;
					player.setAnimation(true, Animations.PLAYER_GRAB);
				}

				if (!keyDown()) {
					grabbing = false;
					grabObject = null;
					player.resetAnimation();
					player.setBusy(false);
				}
			}
			else if (keyDown() && !player.isBusy() && !player.isItemBusy()) {
				Entity e = Collision.getInstanceMeeting(
						player,
						player.getPosition().plus(
								Direction.lengthVector(4, player.getDir())),
						Entity.class);

				if (e != null && e instanceof Monster) {
					((Monster) e).triggerReaction(ReactionCause.BRACELET, this);
				}

				else if (e != null && e instanceof ObjectSomariaBlock) {
					pickupObject(((ObjectSomariaBlock) e).pickup());
				}

				else if (Collision.placeMeetingSolid(
						player.getHardCollidable(),
						player.getPosition().plus(
								Direction.lengthVector(1, player.getDir())))) {
					grabbing = true;
					grabTimer = 0;
					player.setAnimation(true, Animations.PLAYER_GRAB);
					player.setBusy(true);

					FrameObject obj = (FrameObject) Collision
							.getInstanceMeeting(
									player.getHardCollidable(),
									player.getPosition().plus(
											Direction.lengthVector(1,
													player.getDir())),
									FrameObject.class);

					if (obj != null && obj.isCarriable()) {
						grabObject = obj;
					}
					

					ArrayList<Entity> entities = Collision.getInstancesMeeting(
								player.getHardCollidable(),
								player.getPosition().plus(
								Direction.lengthVector(1,
								player.getDir())), FrameObject.class);

					for (int i = 0; i < entities.size(); i++) {
						FrameObject obj2 = (FrameObject) entities.get(i);
						obj2.onGrab();
					}
				}
			}
		}

		super.update();
	}
}
