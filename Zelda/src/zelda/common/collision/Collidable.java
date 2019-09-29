package zelda.common.collision;

import zelda.common.geometry.Vector;
import zelda.game.entity.CollisionBox;


public interface Collidable {
	public abstract Vector getPosition();

	public abstract CollisionBox getCollisionBox();

	public abstract boolean isSolid();
}
