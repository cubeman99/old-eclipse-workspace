package zelda.game.entity.object.dungeon;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.properties.Property;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;


public class ObjectLever extends ObjectAbstractSwitch {
	
	public ObjectLever() {
		super();
		sprite.newAnimation(new Animation(isToggled() ? 5 : 6, 8));
	}
	
	public boolean isToggled() {
		return properties.getBoolean("toggled", false);
	}
	
	public void toggle() {
		properties.set("toggled", !isToggled());
		onChangeProperty(properties.getProperty("toggled"));
		super.toggle();
	}
	
	@Override
	public void initialize() {
		super.initialize();
		sprite.newAnimation(new Animation(isToggled() ? 5 : 6, 8));
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("toggled", false);
	}
	@Override
	public void onChangeProperty(Property p) {
		super.onChangeProperty(p);
		if (p.hasName("toggled")) {
			sprite.newAnimation(new Animation(isToggled() ? 5 : 6, 8));
		}
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		sprite.newAnimation(new Animation(isToggled() ? 5 : 6, 8));
		super.drawTileSprite(pos, frame);
	}

	@Override
	public FrameObject clone() {
		return new ObjectLever();
	}
}
