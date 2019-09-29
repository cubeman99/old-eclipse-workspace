package zelda.game.entity.object.dungeon.door;

import zelda.common.Sounds;
import zelda.game.control.text.Message;

public abstract class ObjectLockedDoor extends ObjectDoor {
	private String nokeyMessage;
	
	public ObjectLockedDoor(int sourceX, String nokeyMessage) {
		super(sourceX);
		this.nokeyMessage = nokeyMessage;
		pushDelay = 6;
		staysOpen = true;
	}
	
	public abstract boolean hasKey();
	
	public abstract void useKey();
	
	public abstract void createKeyEffect();
	
	@Override
	public void update() {
		super.update();
		pushDelay = (hasKey() ? 6 : 20);
	}
	
	@Override
	public boolean move(int dir) {
		if (!opened) {
			if (hasKey()) {
    			useKey();
    			createKeyEffect();
    			open();
				Sounds.COLLECT_ITEM.play();
			}
			else {
				game.readMessage(new Message(nokeyMessage));
			}
			return true;
		}
		return false;
	}
}
