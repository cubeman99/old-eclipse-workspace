package zelda.game.entity.object.overworld;

import zelda.common.geometry.Point;
import zelda.game.entity.object.FrameObject;


public class ObjectDirtPile extends FrameObject {

	@Override
	public void initialize() {
		
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid",   true);
		objectData.addProperty("digable", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(6, 3);
	}

	@Override
	public FrameObject clone() {
		return new ObjectDirtPile();
	}
}
