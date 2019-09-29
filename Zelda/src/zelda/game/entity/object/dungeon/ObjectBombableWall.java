package zelda.game.entity.object.dungeon;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;


public class ObjectBombableWall extends FrameObject {
	
	@Override
	public void bomb() {
		super.bomb();
		// Stay broken forever.
		objectData.getSource().getProperties().set("enabled", false);
	}
	
	@Override
	public void initialize() {
		sprite.newAnimation(new Animation(createSpriteSource()));
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid",    true);
		objectData.addProperty("bombable", true);
		objectData.addProperty("dir", 3);
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		sprite.newAnimation(new Animation(createSpriteSource()));
		super.drawTileSprite(pos, frame);
	}
	
	@Override
	public Point createSpriteSource() {
		return new Point(4, 4 + properties.getInt("dir", 0));
	}

	@Override
	public FrameObject clone() {
		return new ObjectBombableWall();
	}
}
