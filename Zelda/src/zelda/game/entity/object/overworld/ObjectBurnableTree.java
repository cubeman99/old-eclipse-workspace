package zelda.game.entity.object.overworld;

import zelda.common.geometry.Point;
import zelda.game.entity.object.FrameObject;


public class ObjectBurnableTree extends FrameObject {

	@Override
	public void initialize() {
		
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid",    true);
		objectData.addProperty("burnable", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(6, 2);
	}

	@Override
	public FrameObject clone() {
		return new ObjectBurnableTree();
	}
}
