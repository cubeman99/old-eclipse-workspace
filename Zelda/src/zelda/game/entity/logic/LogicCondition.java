package zelda.game.entity.logic;

import zelda.common.graphics.Animation;


public class LogicCondition extends LogicEntity {
	
	public LogicCondition() {
		super();
		sprite.newAnimation(new Animation(0, 1));
	}
	
	private void checkCondition() {
		String out = properties.script("condition", this, frame);
		
		if (Boolean.parseBoolean(out)) {
			if (!properties.getBoolean("triggered", false)
					|| !properties.getBoolean("trigger_once", false))
			{
				properties.script("event_trigger", this, frame);
				properties.set("triggered", true);
			}
		}
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("condition", "");
		objectData.addProperty("trigger_once", false);
		objectData.addProperty("triggered", false);
		objectData.addEvent("event_trigger", "");
	}
	
	@Override
	public void postBegin() {
		super.postBegin();
		checkCondition();
	}
	
	@Override
	public void update() {
		checkCondition();
	}
	
	@Override
	public LogicCondition clone() {
		return new LogicCondition();
	}
}
