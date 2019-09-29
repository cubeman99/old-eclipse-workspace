package zelda.game.world.tile;

import zelda.common.geometry.Point;
import zelda.common.properties.Properties;
import zelda.common.properties.Property;
import zelda.common.properties.PropertyHolder;
import zelda.game.world.Frame;


public abstract class AbstractTile implements PropertyHolder {
	protected Frame frame;
	protected Point sourcePosition;
	protected Properties properties;
	protected Properties defaultProperties;
	


	// ================== CONSTRUCTORS ================== //
	
	public AbstractTile(Frame frame, Point sourcePos, Properties p) {
		this.frame = frame;
		this.sourcePosition = new Point(sourcePos);
		this.properties = p;
		
		defaultProperties = new Properties(p);
		defaultProperties.define("id", "0");
		
		properties.define("id", "0"); // TODO: unique id's

	}



	// =============== ABSTRACT METHODS =============== //

	public abstract void onEnterFrame();
	
	public abstract void onFrameBegin();

	public abstract void onLeaveFrame();

	public abstract void update();

	public abstract void draw();



	// =================== ACCESSORS =================== //

	public Frame getFrame() {
		return frame;
	}

	public Point getSourcePosition() {
		return sourcePosition;
	}
	
	public Properties getDefaultProperties() {
		return defaultProperties;
	}



	// ==================== MUTATORS ==================== //

	public void setProperties(Properties p) {
		properties.set(p);
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void onChangeProperty(Property p) {
		// TODO
	}

	@Override
	public Properties getProperties() {
		return properties;
	}
}
