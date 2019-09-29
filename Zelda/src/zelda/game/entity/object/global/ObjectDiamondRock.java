package zelda.game.entity.object.global;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.game.entity.object.FrameObject;


public class ObjectDiamondRock extends FrameObject {

	@Override
	public void initialize() {
		setBreakSprite(Animations.EFFECT_BREAK_ROCKS);
		setSpriteEntitySource(4, 1);
	}
	
	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("solid",         true);
		objectData.addProperty("switchable",    true);
		objectData.addProperty("switch_stays",  true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(4, 0);
	}

	@Override
	public FrameObject clone() {
		return new ObjectDiamondRock();
	}
}
