package zelda.game.entity.logic;

import java.util.ArrayList;
import zelda.common.graphics.Animation;
import zelda.common.properties.PropertyHolder;
import zelda.game.control.script.Function;
import zelda.game.world.Frame;


public class LogicRelay extends LogicEntity {
	
	public LogicRelay() {
		super();
		sprite.newAnimation(new Animation(1, 1));
	}
	
	public boolean canTrigger() {
		return (properties.getBoolean("trigger_enabled")
				&& (!properties.getBoolean("triggered", false)
				|| !properties.getBoolean("trigger_once", false)));
	}
	
	public void trigger() {
		if (canTrigger()) {
			System.out.println(properties.get("id") + " Triggered!");
			properties.script("event_trigger", this, frame);
			properties.set("triggered", true);
		}
		else {
			System.out.println(properties.get("id") + " Can't trigger!");
		}
	}
	
	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("trigger_enabled", true);
		objectData.addProperty("trigger_once", false);
		objectData.addProperty("triggered", false);
		objectData.addEvent("event_trigger", "");
		
		objectData.addFunction(new Function("trigger") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				trigger();
				return "";
			}
		});
		
		objectData.addFunction(new Function("toggle") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				properties.set("trigger_enabled",
						!properties.getBoolean("trigger_enabled"));
				return "";
			}
		});
		
		objectData.addFunction(new Function("disable_trigger") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				properties.set("trigger_enabled", false);
				return "";
			}
		});
		
		objectData.addFunction(new Function("enable_trigger") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				properties.set("trigger_enabled", true);
				return "";
			}
		});
	}
	
	@Override
	public LogicRelay clone() {
		return new LogicRelay();
	}
}
