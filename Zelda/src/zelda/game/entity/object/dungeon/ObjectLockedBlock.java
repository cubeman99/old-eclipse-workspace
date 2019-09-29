package zelda.game.entity.object.dungeon;

import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.game.control.text.Message;
import zelda.game.entity.effect.EffectKeyUsed;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Dungeon;


public class ObjectLockedBlock extends FrameObject {

	public boolean hasKey() {
		Dungeon dun = game.getLevel().getDungeon();
		return (dun != null && dun.hasSmallKey());
	}
	
	public void useKey() {
		Dungeon dun = game.getLevel().getDungeon();
		if (dun != null)
			dun.useSmallKey();
	}
	
	@Override
	public void initialize() {
		
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid", true);
	}
	
	@Override
	public void update() {
		super.update();
		pushDelay = (hasKey() ? 6 : 20);
	}
	
	@Override
	public boolean move(int dir) {
		if (hasKey()) {
			useKey();
			// TODO: Poof effect.
			game.addEntity(new EffectKeyUsed(new Point(position)));
			objectData.getSource().getProperties().set("enabled", false);
			destroy();
			Sounds.OBJECT_CHEST_OPEN.play();
			Sounds.COLLECT_ITEM.play();
		}
		else {
			game.readMessage(new Message("You need a <red>key<red> for this block!"));
		}
		return true;
	}

	@Override
	public Point createSpriteSource() {
		return new Point(3, 9);
	}

	@Override
	public FrameObject clone() {
		return new ObjectLockedBlock();
	}
}
