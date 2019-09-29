package zelda.game.entity.object.dungeon;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.object.MovingFrameObject;


public class ObjectMovableBlock extends FrameObject {
	@Override
	public void initialize() {
		setSpriteEntitySource(2, 9);
		setBreakSprite(Animations.EFFECT_BREAK_ROCKS);
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid",   true);
		objectData.addProperty("movable", true);
		objectData.addProperty("move_once", true);
	}

	public boolean canMove(int dir) {
		if (!properties.getBoolean("movable", true))
			return false;
		if (properties.getBoolean("move_once", true)
				&& properties.getBoolean("moved", false))
			return false;
		return properties.nullOrEquals("move_dir", dir);

	}

	@Override
	public boolean move(int dir) {
		if (canMove(dir))
			return super.move(dir);
		return false;
	}

	@Override
	public void onMove(int dir, MovingFrameObject obj) {
		properties.set("moved", true);
		properties.set("moved_dir", dir);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(1, 9);
	}

	@Override
	public FrameObject clone() {
		return new ObjectMovableBlock();
	}
}
