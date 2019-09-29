package zelda.game.entity.object.overworld;

import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.game.entity.object.FrameObject;


public class ObjectGrass extends FrameObject {

	@Override
	public void initialize() {
		setBreakSprite(Animations.EFFECT_GRASS_LEAVES);
		soundBreak = Sounds.EFFECT_LEAVES;
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("surface",       true);
		objectData.addProperty("swordable",     true);
		objectData.addProperty("burnable",      true);
		objectData.addProperty("bombable",      true);
		objectData.addProperty("movable",       true);
		objectData.addProperty("boomerangable", true);
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
		return new Point(0, 3);
	}

	@Override
	public FrameObject clone() {
		return new ObjectGrass();
	}
}
