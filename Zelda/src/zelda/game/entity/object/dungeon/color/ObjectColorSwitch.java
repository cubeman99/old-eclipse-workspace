package zelda.game.entity.object.dungeon.color;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.properties.Property;
import zelda.common.util.Colors;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.object.dungeon.ObjectAbstractSwitch;
import zelda.game.world.Frame;


public class ObjectColorSwitch extends ObjectAbstractSwitch {
	
	public ObjectColorSwitch() {
		super();
		imageSheet = Resources.SHEET_GENERAL_TILES;
		sprite.newAnimation(new Animation(4, 0));
	}
	
	public int getColor() {
		return properties.getInt("color", Colors.BLUE);
	}
	
	public void setColor(int color) {
		properties.set("color", color);
		onChangeProperty(properties.getProperty("color"));
	}
	
	@Override
	public void onChangeProperty(Property p) {
		super.onChangeProperty(p);
		if (p.hasName("color"))
			sprite.newAnimation(new Animation(getColor() == Colors.BLUE ? 4 : 3, 0));
	}
	
	@Override
	public void toggle() {
		if (getColor() == Colors.BLUE)
			setColor(Colors.RED);
		else
			setColor(Colors.BLUE);
		super.toggle();
	}
	
	@Override
	public void initialize() {
		super.initialize();
		onChangeProperty(properties.getProperty("color"));
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("color", Colors.BLUE);
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		sprite.newAnimation(new Animation(4, 0));
		super.drawTileSprite(pos, frame);
	}
	
	@Override
	public FrameObject clone() {
		return new ObjectColorSwitch();
	}
}
