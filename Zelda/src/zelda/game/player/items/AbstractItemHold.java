package zelda.game.player.items;

import zelda.common.Sounds;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.EntityObject;
import zelda.game.player.Player;
import zelda.main.Keyboard;


/**
 * Represents an item that makes the player be able to hold and throw items.
 * 
 * @author David
 */
public class AbstractItemHold extends Item {
	protected int[] pullDrawOffsets;

	protected boolean holding;
	protected boolean pickingUp;
	protected int throwTimer;
	protected int pickupTimer;
	protected EntityObject holdObject;
	protected int pickupGrabTime;


	// ================== CONSTRUCTORS ================== //

	public AbstractItemHold(Player player, String name) {
		super(player, name);

		holding = false;
		pickingUp = false;
		throwTimer = 0;
		pickupTimer = 0;
		holdObject = null;
		pullDrawOffsets = new int[] {-4, -2, -4, -1};
		pickupGrabTime = 6;
	}



	// =================== ACCESSORS =================== //

	public boolean isHolding() {
		return holding;
	}



	// ==================== MUTATORS ==================== //

	public void updateHoldObject() {
		if (holding && holdObject != null) {
			holdObject.setZPosition(12);
			holdObject.setPosition(player.getCenter().plus(0, 5));

			if ((player.getDir() == Direction.LEFT || player.getDir() == Direction.RIGHT)
					&& player.getSprite().getFrameIndex() == 0)
				holdObject.setZPosition(holdObject.getZPosition() + 1);
		}
	}

	public void pickupObject(EntityObject obj) {
		pickingUp = true;
		holdObject = obj;
		pickupTimer = 0;
		player.setBusy(true);
		Sounds.PLAYER_PICKUP.play();
	}

	protected void startHolding() {
		pickingUp = false;
		holding = true;
		player.resetAnimation();
		player.setMoveStandAnimations(Animations.PLAYER_HOLDING);
		player.setItemBusy(true);
		player.setBusy(false);
	}

	protected void throwObject() {
		drop();
		throwTimer = 1;
		player.setBusy(true);
		player.setAnimation(true, Animations.PLAYER_THROW);
	}

	protected void drop() {
		holding = false;
		player.resetMoveStandAnimations();
		player.resetAnimation();
		player.setItemBusy(false);

		if (holdObject != null) {
			if (player.isMoving()) {
				holdObject.setVelocity(Vector.polarVector(1.5, player.getDir()
						* GMath.HALF_PI));
				holdObject.setZVelocity(1.0);
			}
			player.getGame().addEntity(holdObject);
			holdObject = null;
		}
	}


	// =============== INHERITED METHODS =============== //

	@Override
	public void interrupt() {
		drop();
		throwTimer = 0;
		player.setBusy(false);
		player.resetAnimation();
	}

	@Override
	public void update() {
		if (holding && (holdObject == null || holdObject.isDestroyed())) {
			holding = false;
			player.setItemBusy(false);
			player.resetMoveStandAnimations();
			player.resetAnimation();
		}

		if (!holding && throwTimer > 0) {
			if (throwTimer++ > 4) {
				throwTimer = 0;
				player.setBusy(false);
				player.resetMoveStandAnimations();
				player.resetAnimation();
			}
		}

		if (pickingUp) {
			pickupTimer++;
			holdObject.setZPosition(0);
			holdObject.setPosition(player.getCenter().plus(0, 5));
			holdObject.setPosition(player.getCenter().plus(0, 5)
					.plus(Direction.lengthVector(8, player.getDir())));
			player.setAnimation(Animations.PLAYER_PULL);

			if (pickupTimer > pickupGrabTime) {
				holdObject.setZPosition(8);
				player.setAnimation(Animations.PLAYER_PUSH);
				holdObject.setPosition(player.getCenter().plus(0, 5)
						.plus(Direction.lengthVector(2, player.getDir())));
			}

			if (pickupTimer > pickupGrabTime + 4)
				startHolding();
		}

		if (holding) {
			updateHoldObject();
			if (Keyboard.action.pressed()) {
				throwObject();
				Sounds.PLAYER_THROW.play();
			}
		}
	}

	@Override
	public void drawOver() {
		if (holdObject != null && (pickingUp || holding))
			holdObject.draw();
	}
}
