package zelda.game.entity.object.dungeon;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.game.control.text.Message;
import zelda.game.entity.object.FrameObject;


public class ObjectPot extends FrameObject {

	@Override
	public void initialize() {
		setBreakSprite(Animations.EFFECT_BREAK_ROCKS);
		setSpriteEntitySource(2, 1);
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid",      true);
		objectData.addProperty("carriable",  true);
		objectData.addProperty("movable",    true);
		objectData.addProperty("switchable", true);
		objectData.addProperty("swordable",  true);
		objectData.addProperty("swordable_level", 1);
	}

	@Override
	public boolean move(int dir) {
		if (game.getPlayer().getInventory()
				.hasItem(game.getPlayer().itemBracelet))
			return super.move(dir);
		game.readMessage(new Message("Oof! It<ap>s heavy!"));
		return true;
	}

	@Override
	public Point createSpriteSource() {
		return new Point(2, 0);
	}

	@Override
	public FrameObject clone() {
		return new ObjectPot();
	}
}
