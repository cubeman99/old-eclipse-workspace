package zelda.game.entity.object.dungeon.color;

import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.util.Colors;
import zelda.game.entity.object.FrameObject;
import zelda.game.player.Player;
import zelda.game.world.Frame;


public class ObjectColorTile extends FrameObject {
	
	public ObjectColorTile() {
		super();
		imageSheet = Resources.SHEET_GENERAL_TILES;
	}
	
	public int getColor() {
		return properties.getInt("color", -1);
	}
	
	public void setColor(int color) {
		properties.set("color", color);
		int c = getColor();
		sprite.newAnimation(new Animation(5, (c == Colors.RED ? 0
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

		objectData.addEvent("event_change_color", "Called when the tile's color is changed.");
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
		return new ObjectColorTile();
	}
}
