package zelda.game.player.action;

import java.util.ArrayList;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.util.Direction;
import zelda.game.player.Player;
import zelda.game.world.tile.GridTile;


public class ActionLedgeJump extends PlayerAction {
	private boolean extendsBeyondFrame;
	private Vector velocity;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public ActionLedgeJump(Player player) {
		super(player);
		
	}
	


	// =================== ACCESSORS =================== //
	
	private boolean canLedgeJump() {
		return (!player.inAir()
				&& !player.itemBracelet.isHolding()
				&& !player.itemBombs.isHolding());
	}
	
	public boolean checkLedgeJump(boolean moving, double moveSpeed) {
		if (Collision.canDodgeCollisions(player) || !canLedgeJump() || !moving)
			return false;
		
		boolean foundLedge = false;
		Vector checkPos = player.getPosition().plus(
				Direction.lengthVector(moveSpeed, player.getDir()));
		ArrayList<GridTile> tiles = Collision.getTilesMeeting(
				player.getHardCollidable(), checkPos);
		
		for (int i = 0; i < tiles.size(); i++) {
			GridTile t = tiles.get(i);
			
			if (t.isLedge(player.getDir()))
				foundLedge = true;
			else if (t.isSolid()) {
				foundLedge = false;
				break;
			}
		}
		if (foundLedge) {
			player.beginAction(player.actionLedgeJump);
			return true;
		}
		return false;
	}
	
	

	// ==================== MUTATORS ==================== //
	

	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void begin() {
		velocity = new Vector();
		
		Sounds.PLAYER_JUMP.play();
		player.setCollideWithWorld(false);
		player.setPassable(true);
		player.setZVelocity(1.5);
		player.getVelocity().zero();
		player.setAnimation(false, Animations.PLAYER_JUMP);
		
		int dir = player.getDir();
		player.getPosition().add(Direction.lengthVector(1, dir));
		
		// Find the distance of the ledge.
		Vector pos = new Vector(player.getPosition());
		double distance = 0;
		while (Collision.placeMeetingSolid(player.getHardCollidable(), pos)) {
			distance += 1;
			pos.add(Direction.lengthVector(1, dir));
		}
		
		if (player.placeOutsideOfFrame(pos)) {
			extendsBeyondFrame = true;
			player.setZVelocity(0.8);
			Vector p = new Vector(player.getPosition().x, game.getFrame().getVect().getY2() - 14);
			player.setZPosition(p.y - player.getPosition().y);
			player.setPosition(p);
		}
		else {
			extendsBeyondFrame = false;
			double jumpTime = 25;
			double speed    = Math.max(0.7, Math.min(5, distance / jumpTime));
			velocity = Direction.lengthVector(speed, dir);
		}
		
	}
	
	@Override
	public void update() {
		player.setCollideWithWorld(false);
		if (!Collision.placeMeetingSolid(player.getHardCollidable()) && player.onGround()) {
			player.setCollideWithWorld(true);
			player.setPassable(false);
			player.getVelocity().zero();
			player.resetAnimation();
			player.setCurrentAction(player.actionNormal);
			player.actionNormal.begin();
		}
		else {
			player.setVelocity(velocity);
//			player.getPosition().add(velocity);
		}
	}
}
