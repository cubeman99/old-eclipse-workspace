package zelda.game.entity.effect;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.collision.Collidable;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.game.entity.CollisionBox;
import zelda.game.world.tile.GridTile;


public class EffectFallingObject extends Effect implements Collidable {
	private Vector holeCenter;

	public EffectFallingObject(Vector position) {
		super(Resources.SPRITE_FALLING_OBJECT, position);

		holeCenter = new Vector(position);

		ArrayList<GridTile> tiles = Collision.getTilesMeeting(this,
				CollisionBox.TILE_BOX);

		// Find the hole this effect is falling toward.
		for (GridTile t : tiles) {
			if (t.isEnabled() && t.getProperties().getBoolean("hole", false)) {
				Point c = new Point(t.getPosition().plus(8, 8));
				holeCenter = new Vector(c);
			}
		}
	}

	@Override
	public void update() {
		super.update();

		// Move towards the center of the hole.
		if (Math.abs(position.x - holeCenter.x) > 0.5)
			position.x += 0.5 * Math.signum(holeCenter.x - position.x);
		if (Math.abs(position.y - holeCenter.y) > 0.5)
			position.y += 0.5 * Math.signum(holeCenter.y - position.y);
	}

	@Override
	public Vector getPosition() {
		return new Vector(position);
	}

	@Override
	public CollisionBox getCollisionBox() {
		return new CollisionBox(-1, -1, 2, 2);
	}

	@Override
	public boolean isSolid() {
		return false;
	}
}
