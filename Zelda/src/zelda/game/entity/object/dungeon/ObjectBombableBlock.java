package zelda.game.entity.object.dungeon;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.game.entity.object.FrameObject;


public class ObjectBombableBlock extends FrameObject {

	@Override
	public void initialize() {
		setBreakSprite(Animations.EFFECT_BREAK_ROCKS);
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid",    true);
		objectData.addProperty("bombable", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(0, 9);
	}

	@Override
	public FrameObject clone() {
		return new ObjectBombableBlock();
	}
}
