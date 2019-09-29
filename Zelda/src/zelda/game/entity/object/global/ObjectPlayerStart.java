package zelda.game.entity.object.global;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.game.entity.object.FrameObject;


public class ObjectPlayerStart extends FrameObject {

	public ObjectPlayerStart() {
		super();
		imageSheet = Resources.SHEET_PLAYER;
		sprite.setSheet(imageSheet);
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("enabled", false);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(6, 0);
	}

	@Override
	public FrameObject clone() {
		return new ObjectPlayerStart();
	}
}
