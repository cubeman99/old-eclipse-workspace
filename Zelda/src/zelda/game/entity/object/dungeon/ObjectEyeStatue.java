package zelda.game.entity.object.dungeon;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.object.FrameObject;


public class ObjectEyeStatue extends FrameObject {

	@Override
	public void initialize() {
		
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("solid", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(4, 9);
	}

	@Override
	public FrameObject clone() {
		return new ObjectEyeStatue();
	}

	@Override
	public void draw() {
		super.draw();

		int dir = ((int) (GMath.direction(new Vector(getCenter()), game
				.getPlayer().getCenter())
				/ GMath.QUARTER_PI + 0.5) + 8) % 8;

		Animation anim = new Animation().addFrame(1, 2, 1, 0, 0);
		Sprite eye = new Sprite(Resources.SHEET_GENERAL_TILES, anim);
		Draw.drawSprite(eye, position.plus(new Vector(Direction.getAngledDirPoint(dir))));
	}
}
