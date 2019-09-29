package zelda.game.entity.object.dungeon;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.Animations;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.tile.GridTile;


public class ObjectMineCart extends FrameObject {
	
	public ObjectMineCart() {
		super();
		imageSheet = Resources.SHEET_GENERAL_TILES;
		sprite.newAnimation(Animations.MINE_CART.getVariant(0));
		pushDelay = 6;
	}
	
	@Override
	public void initialize() {
		Point loc = getLocation();
		if (frame.contains(loc)) {
			GridTile t = frame.getGridTile(loc);
			int type = t.getProperties().getInt("track_type", -1);
			if (type >= 1 && type <= 2) {
				sprite.newAnimation(Animations.MINE_CART.getVariant(type - 1));
			}
		}
	}
	
	@Override
	public boolean move(int dir) {
		game.getPlayer().enterCart(this);
		return true;
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("solid", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(0, 3);
	}

	@Override
	public FrameObject clone() {
		return new ObjectMineCart();
	}
}
