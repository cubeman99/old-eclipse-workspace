package zelda.game.entity.object.dungeon;

import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.properties.Property;
import zelda.common.util.Direction;
import zelda.game.control.event.EventOpenChest;
import zelda.game.entity.object.FrameObject;
import zelda.main.Keyboard;


public class ObjectChest extends FrameObject {

	public ObjectChest() {
		properties.define("opened", false);
	}

	public void open() {
		Sounds.OBJECT_CHEST_OPEN.play();
		
		properties.set("opened", true);
		sprite.newAnimation(new Animation(10, 8));
		game.playEvent(new EventOpenChest(this));
		objectTile.getProperties().set("hidden", false);
		
		objectData.getSource().getProperties().set("opened", true);
	}
	
	@Override
	public void onChangeProperty(Property p) {
		super.onChangeProperty(p);
		
//		if (p.hasName("hidden"))
//			objectTile.getProperties().set("hidden", p.get());
	}

	@Override
	public void onAction(int dir) {
		if (dir == Direction.UP && !properties.getBoolean("opened", false)) {
			Keyboard.action.clear();
			open();
		}
	}

	@Override
	public void update() {
		super.update();


	}

	@Override
	public void initialize() {
		properties.define("opened", false);

		if (properties.getBoolean("opened", false))
			sprite.newAnimation(new Animation(10, 8));
	}

	@Override
	public Point createSpriteSource() {
		return new Point(9, 8);
	}

	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("solid", true);
		
		objectData.addProperty("opened", false, "Whether the chest has been opened already.");
		objectData.addProperty("reward", "key", "The name of the reward inside the chest.");
		
		objectData.addEvent("event_open", "", "Called when the chest is opened.");
	}

	@Override
	public FrameObject clone() {
		return new ObjectChest();
	}
}
