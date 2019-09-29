package zelda.game.entity.object.dungeon.door;

import zelda.common.geometry.Point;


public class ObjectBasicDoor extends ObjectDoor {
	
	public ObjectBasicDoor() {
		super(0);
	}

	@Override
	public void initialize() {
		super.initialize();
		properties.define("auto_check", false);
		properties.define("condition", "false");
	}

	@Override
	public void update() {
		super.update();
		
		if (properties.getBoolean("auto_check", false)) {
			boolean condition = Boolean.parseBoolean(properties.script(
					"condition", this, frame));
			if (condition != opened) {
				if (condition)
					open();
				else
					close();
			}
		}
	}

	@Override
	public Point createSpriteSource() {
		return new Point(0, 18);
	}

	@Override
	public ObjectBasicDoor clone() {
		return new ObjectBasicDoor();
	}
}
