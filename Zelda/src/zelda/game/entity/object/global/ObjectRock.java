package zelda.game.entity.object.global;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.game.entity.object.FrameObject;


public class ObjectRock extends FrameObject {

	@Override
	public void initialize() {
		setBreakSprite(Animations.EFFECT_BREAK_ROCKS);
		setSpriteEntitySource(3, 1);
	}

	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("solid",     true);
		objectData.addProperty("carriable", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(3, 0);
	}

	@Override
	public FrameObject clone() {
		return new ObjectRock();
	}
}
