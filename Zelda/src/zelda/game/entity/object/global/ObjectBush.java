package zelda.game.entity.object.global;

import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.game.entity.object.FrameObject;


public class ObjectBush extends FrameObject {

	@Override
	public void initialize() {
		setBreakSprite(Animations.EFFECT_LEAVES);
		setSpriteEntitySource(0, 1);
		soundBreak = Sounds.EFFECT_LEAVES;
	}
	
	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("solid",         true);
		objectData.addProperty("swordable",     true);
		objectData.addProperty("switchable",    true);
		objectData.addProperty("burnable",      true);
		objectData.addProperty("bombable",      true);
		objectData.addProperty("carriable",     true);
		objectData.addProperty("boomerangable", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(0, 0);
	}

	@Override
	public FrameObject clone() {
		return new ObjectBush();
	}
}
