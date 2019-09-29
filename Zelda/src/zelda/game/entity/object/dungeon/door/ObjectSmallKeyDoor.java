package zelda.game.entity.object.dungeon.door;

import zelda.common.geometry.Point;
import zelda.game.entity.effect.EffectKeyUsed;
import zelda.game.world.Dungeon;


public class ObjectSmallKeyDoor extends ObjectLockedDoor {

	public ObjectSmallKeyDoor() {
		super(1, "You need a <red>key<red> for this door!");
	}
	
	@Override
	public boolean hasKey() {
		Dungeon dun = game.getLevel().getDungeon();
		return (dun != null && dun.hasSmallKey());
	}

	@Override
	public void useKey() {
		Dungeon dun = game.getLevel().getDungeon();
		if (dun != null)
			dun.useSmallKey();
	}
	
	@Override
	public void createKeyEffect() {
		game.addEntity(new EffectKeyUsed(new Point(position)));
	}

	@Override
	public ObjectSmallKeyDoor clone() {
		return new ObjectSmallKeyDoor();
	}
}
