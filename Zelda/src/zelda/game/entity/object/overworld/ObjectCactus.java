package zelda.game.entity.object.overworld;

import zelda.common.geometry.Point;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.object.FrameObject;


public class ObjectCactus extends FrameObject {

	@Override
	public void initialize() {
		collisionBox = new CollisionBox(2, 2, 12, 12);
	}
	
	@Override
	public void onPlayerTouch() {
		game.getPlayer().damage(2, getCenter());
	}
	
	@Override
	public Point createSpriteSource() {
		return new Point(5, 3);
	}

	@Override
	public FrameObject clone() {
		return new ObjectCactus();
	}
}
