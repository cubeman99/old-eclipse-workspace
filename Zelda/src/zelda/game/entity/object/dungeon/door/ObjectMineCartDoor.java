package zelda.game.entity.object.dungeon.door;


public class ObjectMineCartDoor extends ObjectDoor {
	public ObjectMineCartDoor() {
		super(2);
	}

	@Override
	public ObjectMineCartDoor clone() {
		return new ObjectMineCartDoor();
	}
}
