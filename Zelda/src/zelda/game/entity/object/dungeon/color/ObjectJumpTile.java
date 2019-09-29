package zelda.game.entity.object.dungeon.color;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.util.Colors;
import zelda.common.util.GMath;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.object.FrameObject;
import zelda.game.player.Player;
import zelda.game.world.Frame;


public class ObjectJumpTile extends FrameObject {
	private static ArrayList<ObjectJumpTile> markedTiles = new ArrayList<ObjectJumpTile>();
	private boolean touchingPlayer;
	
	public ObjectJumpTile() {
		super();
		imageSheet = Resources.SHEET_GENERAL_TILES;
		touchingPlayer = false;
	}
	
	public int getColor() {
		return properties.getInt("color", -1);
	}
	
	public void setColor(int color) {
		properties.set("color", color);
		int c = getColor();
		sprite.newAnimation(new Animation(6, (c == Colors.RED ? 0
				: (c == Colors.YELLOW ? 1 : 2))));
	}
	
	public void changeColor() {
		if (getColor() == Colors.RED)
			setColor(Colors.YELLOW);
		else if (getColor() == Colors.YELLOW)
			setColor(Colors.BLUE);
		else
			setColor(Colors.RED);
		properties.script("event_change_color", this, frame);
	}
	
	@Override
	public void initialize() {
		setColor(getColor());
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("color", Colors.RED);

		objectData.addEvent("event_change_color", "Called when the player jumps on this tile thus changing its color.");
	}
	
	@Override
	public void update() {
		super.update();
		
		Player p = game.getPlayer();
		Vector edge = new Vector(new Point(p.getOrigin().scaledByInv(16).plus(0.5, 0.5))).scale(16);
		double distx = Math.abs(edge.x - p.getOrigin().x);
		double disty = Math.abs(edge.y - p.getOrigin().y);
		
		if (p.inAir()) {
			if (!markedTiles.contains(this) && Collision.isTouching(p.getOrigin(), this) && !touchingPlayer)
				markedTiles.add(this);
		}
		else {
			markedTiles.clear();
			touchingPlayer = Collision.isTouching(p.getOrigin(), this);
		}
		
		if (markedTiles.contains(this) && distx >= 2 && disty >= 0
				&& p.getVelocity().length() > GMath.EPSILON && p.inAir()
				&& p.getZPosition() + p.getZVelocity() <= 0)
		{
				changeColor();
				Sounds.COLLECT_ITEM.play();
		}
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		setColor(getColor());
		super.drawTileSprite(pos, frame);
	}
	
	@Override
	public void preDraw() {
		super.draw();
	}
	
	@Override
	public void draw() {
		// Don't draw.
	}
	
	@Override
	public Point createSpriteSource() {
		return new Point(0, 0);
	}

	@Override
	public FrameObject clone() {
		return new ObjectJumpTile();
	}
}
