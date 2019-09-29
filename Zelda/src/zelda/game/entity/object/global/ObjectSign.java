package zelda.game.entity.object.global;

import java.util.ArrayList;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animations;
import zelda.common.properties.PropertyHolder;
import zelda.common.util.Direction;
import zelda.game.control.script.Function;
import zelda.game.control.text.Message;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;
import zelda.main.Keyboard;


public class ObjectSign extends FrameObject {

	public ObjectSign() {
		super();
		properties.set("text", "");
		properties.set("sideText", "You can<ap>t read it from here!");
	}

	public Message getMessage(int dir) {
		return new Message(properties.get(dir == Direction.NORTH ? "text"
				: "sideText", "..."));
	}
	
	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("solid",         true);
		objectData.addProperty("swordable",     true);
		objectData.addProperty("switchable",    true);
		objectData.addProperty("burnable",      true);
		objectData.addProperty("carriable",     true);
		objectData.addProperty("boomerangable", true);
		objectData.addProperty("swordable_level", 1);

		objectData.addProperty("text", "...", "The message when read from the front.");
		objectData.addProperty("sideText", "You can<ap>t read it from here!", "The message when read from the side.");
		objectData.addProperty("readable", true, "Whether the sign can be read.");
		objectData.addProperty("read", false, "Whether the sign has been read or not.");
		
		objectData.addEvent("event_read", "", "Called when the sign is read.");
		
		objectData.addFunction(new Function("read") {
			public String execute(ArrayList<String> args, PropertyHolder holder, Frame frame) {
				game.readMessage(new Message(properties.get("text", "")));
				return "";
			}
		});
	}
	
	@Override
	public void onAction(int dir) {
		Keyboard.action.clear();
		game.readMessage(getMessage(dir));
	}

	@Override
	public void initialize() {
		setBreakSprite(Animations.EFFECT_BREAK_SIGN);
		setSpriteEntitySource(5, 1);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(5, 0);
	}

	@Override
	public FrameObject clone() {
		return new ObjectSign();
	}
}
