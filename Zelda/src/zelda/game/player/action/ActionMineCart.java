package zelda.game.player.action;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collidable;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.properties.Properties;
import zelda.common.util.Direction;
import zelda.common.util.Rail;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.object.dungeon.ObjectMineCart;
import zelda.game.entity.object.dungeon.door.ObjectMineCartDoor;
import zelda.game.player.Player;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;
import zelda.game.world.tile.ObjectTile;

public class ActionMineCart extends PlayerAction implements Collidable {
	private int dir;
	private int moveSpeed;
	private Sprite cartSprite;
	private Sprite cartBackSprite;
	private Vector playerPosAdd;
	private Frame startFrame;
	private boolean hasChangedFrames;
	
	
	
	public ActionMineCart(Player p) {
		super(p);
		
		dir            = 0;
		moveSpeed      = 1;
		cartSprite     = new Sprite(Resources.SHEET_GENERAL_TILES);
		cartBackSprite = new Sprite(Resources.SHEET_GENERAL_TILES);
		cartSprite.newAnimation(Animations.MINE_CART_FRONT);
		cartBackSprite.newAnimation(Animations.MINE_CART_BACK);
	}
	
	public void start(ObjectMineCart cart) {
		player.getPosition().set(cart.getPosition());
		Sounds.OBJECT_MINE_CART.loop();
		
		// Hide the cart:
		cart.getObjectData().getSource().getProperties().set("enabled", false);
		cart.destroy();

		Point loc      = cart.getLocation();
		Frame frame    = player.getFrame();
		GridTile track = frame.getGridTile(loc);
		int trackType  = track.getProperties().getInt("track_type", -1);
		int axis       = trackType - 1;
		
		if (trackType >= 1 && trackType <= 2) {
    		for (int i = 0; i < 2; i++) {
    			int testDir = axis + (i * 2);
    			GridTile t = frame.getGridTile(loc.plus(Direction
    					.getDirPoint(testDir)));
    			if (t.getProperties().existEquals("track_type", 0)) {
    				dir = (testDir + 2) % 4;
    				break;
    			}
    		}
		}
		
		playerPosAdd = new Vector();
		player.resetAnimation();
	}
	
	private void exit(Point exitLoc) {
		Frame frame = game.getFrame();
		boolean found = false;
		Point loc = new Point(player.getPosition()
				.scaledByInv(16).add(0.5, 0.5));
		if (hasChangedFrames)
			player.setFrameEnterPosition(new Vector(exitLoc.scaledBy(16)));
		player.setPosition(new Vector(exitLoc.scaledBy(16)));
		player.setZPosition(0);
		player.setZVelocity(0);
		Sounds.OBJECT_MINE_CART.stop();
		
		// Check for existing cart tiles.
		for (int i = 0; i < frame.getObjectTiles().size(); i++) {
			ObjectTile testTile = frame.getObjectTiles().get(i);
			if (testTile.getFrameObject() instanceof ObjectMineCart
					&& testTile.getPosition().equals(loc.scaledBy(16)))
			{
				testTile.getProperties().set("enabled", true);
				testTile.onEnterFrame();
				found = true;
				break;
			}
		}

		// Create a new cart object tile.
		if (!found) {
			ObjectTile cartTile = new ObjectTile(frame, new Point(6, 1), new Properties());
			cartTile.setPosition(loc.scaledBy(16));
			cartTile.setFrameObject(new ObjectMineCart());
			cartTile.onEnterFrame();
			frame.addObjectTile(cartTile);
		}
		
		player.beginAction(player.actionNormal);
	}
	
	private void alignWithGrid() {
		Vector snapPos = player.getPosition();
		int comp       = (dir + 1) % 2;
		snapPos.setComp(comp, (int) ((snapPos.comp(comp)
				/ 16) + 0.5) * 16);
	}
	
	public void update() {
		player.getPosition().sub(playerPosAdd);
		Frame frame = player.getFrame();
		Vector pos  = player.getPosition();
		Point loc   = new Point(pos.scaledByInv(16).add(0.5, 0.5));
		
		player.setDir(player.actionNormal.checkMoveKeyDir());
		
		if (game.getFrame() != startFrame)
			hasChangedFrames = true;
		
		if (frame.contains(loc)) {
			GridTile t    = frame.getGridTile(loc);
			int trackType = t.getProperties().getInt("track_type", -1);

			if (pos.distanceTo(new Vector(loc.scaledBy(16))) < 3) {
				
				// Check for turns.
				if (Rail.isOfType(trackType, Rail.TYPE_CORNER)) {
    				int nextDir = Rail.getNextDir(trackType, dir);
    				if (nextDir >= 0 && dir != nextDir) {
    					dir = nextDir;
    					pos.set(loc.scaledBy(16));
    					alignWithGrid();
    				}
				}
				
				Point nextLoc = loc.plus(Direction.getDirPoint(dir));

				// Check for opening doors.
				ObjectMineCartDoor door = (ObjectMineCartDoor) Collision
						.getInstanceMeeting(this,
								new Vector(nextLoc.scaledBy(16)),
								ObjectMineCartDoor.class);
				if (door != null && !door.isOpen())
					door.open();
				
				// Check for a stopping point.
				if (frame.contains(nextLoc)) {
					GridTile t2    = frame.getGridTile(nextLoc);
					if (t2.getProperties().existEquals("track_type", 0)) {
						exit(nextLoc);
						return;
					}
				}
				
				// Check for a blocked path.
				if (Collision.placeMeetingSolid(this, new Vector(nextLoc.scaledBy(16)))) {
					dir = (dir + 2) % 4;
				}
			}
		}
		
		player.getVelocity().zero();
		player.getPosition().add(Direction.lengthVector(moveSpeed, dir));
		game.setViewFollowFocus(player.getCenter());
		
		playerPosAdd.set(0, -9);
		if (Direction.isHorizontal(dir) && cartSprite.getFrameIndex() == 1)
			playerPosAdd.add(0, 1);
		else if (Direction.isVertical(dir) && cartSprite.getFrameIndex() == 1)
			playerPosAdd.sub(1, 0);
		player.getPosition().add(playerPosAdd);
		
		// Update the sprites.
		cartSprite.update(dir % 2);
		cartBackSprite.setFrameIndex(cartSprite.getFrameIndex());
		cartBackSprite.setVariation(dir % 2);
	}
	
	public void drawUnder() {
		Draw.drawSprite(cartBackSprite, player.getPosition().minus(playerPosAdd));
	}
	
	public void drawOver() {
		Draw.drawSprite(cartSprite, player.getPosition().minus(playerPosAdd));
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void begin() {
		player.setDrawShadow(false);
		player.setCollideWithWorld(false);
		player.setPassable(true);
		player.setDefaultMoveStandAnimations(Animations.PLAYER_RIDING);
		hasChangedFrames = false;
		startFrame       = game.getFrame();
	}
	
	@Override
	public void end() {
		player.setDrawShadow(true);
		player.setDefaultMoveStandAnimations(Animations.PLAYER_DEFAULT);
	}

	@Override
	public Vector getPosition() {
		return player.getPosition();
	}

	@Override
	public CollisionBox getCollisionBox() {
		return new CollisionBox(0, 0, 16, 16);
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
