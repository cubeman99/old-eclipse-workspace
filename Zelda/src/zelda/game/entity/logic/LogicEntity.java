package zelda.game.entity.logic;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.properties.Properties;
import zelda.common.properties.Property;
import zelda.game.entity.FrameEntity;
import zelda.game.world.Frame;


public abstract class LogicEntity extends FrameEntity
{
	protected Properties properties;
	protected Sprite sprite;
	
	
	
	public LogicEntity() {
		sprite     = new Sprite(Resources.SHEET_LOGIC_TILES, 0, 0);
		properties = new Properties();
	}

	
	// =================== ACCESSORS =================== //
	
	public Sprite getSprite() {
		return sprite;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void update() {}

	@Override
	public void draw() {
		// Don't draw.
	}
	
	@Override
	public void setup() {
		super.setup();
		
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public void onChangeProperty(Property p) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void begin() {
		super.begin();
		properties = objectData.getProperties();
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		Draw.drawSprite(sprite, pos);
	}
}
