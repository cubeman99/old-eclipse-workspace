package zelda.game.entity.object.global;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.game.entity.object.FrameObject;


public class ObjectCrystal extends FrameObject {

	@Override
	public void initialize() {
		setBreakSprite(Animations.EFFECT_BREAK_ROCKS);
		setSpriteEntitySource(1, 1);
	}
	
	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("solid",         true);
		objectData.addProperty("swordable",     true);
		objectData.addProperty("bombable",      true);
		objectData.addProperty("carriable",     true);
		objectData.addProperty("boomerangable", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(1, 0);
	}

	@Override
	public FrameObject clone() {
		return new ObjectCrystal();
	}
}
