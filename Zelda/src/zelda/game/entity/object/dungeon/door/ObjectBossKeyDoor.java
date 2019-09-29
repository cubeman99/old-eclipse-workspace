package zelda.game.entity.object.dungeon.door;

import zelda.common.geometry.Point;
import zelda.game.entity.effect.EffectBossKeyUsed;
import zelda.game.world.Dungeon;

public class ObjectBossKeyDoor extends ObjectLockedDoor {

	public ObjectBossKeyDoor() {
		super(3, "This keyhole is different!");
	}
	
	@Override
	public boolean hasKey() {
		Dungeon dun = game.getLevel().getDungeon();
		return (dun != null && dun.hasItem("boss_key"));
	}

	@Override
	public void useKey() {
		// Boss keys are not used up.
	}
	
	@Override
	public void createKeyEffect() {
		game.addEntity(new EffectBossKeyUsed(new Point(position)));
	}

	@Override
	public ObjectBossKeyDoor clone() {
		return new ObjectBossKeyDoor();
	}
}
