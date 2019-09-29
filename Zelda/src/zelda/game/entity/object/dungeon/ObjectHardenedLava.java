package zelda.game.entity.object.dungeon;

import zelda.common.geometry.Point;
import zelda.common.graphics.TileAnimations;
import zelda.game.entity.effect.EffectFallingObject;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;


public class ObjectHardenedLava extends FrameObject {
	
	@Override
	public void initialize() {
		sprite.newAnimation(TileAnimations.LAVA_HARDENED);
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("surface", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(8, 9);
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		sprite.newAnimation(TileAnimations.LAVA_HARDENED);
		super.drawTileSprite(pos, frame);
	}

	@Override
	public FrameObject clone() {
		return new ObjectHardenedLava();
	}
}
