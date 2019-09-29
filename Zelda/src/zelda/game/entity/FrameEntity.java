package zelda.game.entity;

import java.util.ArrayList;
import zelda.common.geometry.Point;
import zelda.common.properties.FrameEntityData;
import zelda.common.properties.Properties;
import zelda.common.properties.Property;
import zelda.common.properties.PropertyHolder;
import zelda.game.control.script.Function;
import zelda.game.world.Frame;
import zelda.game.world.tile.ObjectTile;

public abstract class FrameEntity extends Entity implements PropertyHolder {
	protected FrameEntityData objectData;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public FrameEntity() {
		super();
		objectData = null;
	}
	
	
	
	// =============== ABSTRACT METHODS =============== //
	
	public abstract void update();
	
	public abstract void draw();
	
	
	
	// =================== ACCESSORS =================== //
	
	public FrameEntityData getObjectData() {
		return objectData;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setObjectData(FrameEntityData objectData) {
		this.objectData = objectData;
	}
	
	public void begin() {
		hidden = objectData.getProperties().getBoolean("hidden", false);
	}
	
	public void onFrameBegin() {
		objectData.getProperties().script("event_begin", this, frame);
	}
	
	public void postBegin() {
		
	}
	
	public void setup() {
		objectData.addProperty("id", 0,
				"The name used to identify this object.The name used to identify this object.");
		objectData.addProperty("hidden", false,
				"The hidden state of the object. A hidden object still exists while not being updated or drawn.");
		
		objectData.addEvent("event_create", 
				"Called when the object is constructed.");
		objectData.addEvent("event_begin", 
				"Called when the frame begins.");
		objectData.addEvent("event_destroy",
				"Called when the object is destroyed.");
		
		objectData.addFunction(new Function("destroy") {
			@Override
			public String execute(ArrayList<String> args, PropertyHolder holder, Frame frame) {
				destroy();
				return "";
			}
		});
	}

	public void drawTileSprite(Point pos, Frame frame) {
		// TODO Auto-generated method stub
		
	}
	
	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if (objectData != null) {
			objectData.getProperties().script("event_destroy", this, frame);
		}
	}
	
	public Properties getProperties() {
		if (objectData == null)
			return null;
		return objectData.getProperties();
	}
	
	@Override
	public void onChangeProperty(Property p) {
		// TODO Auto-generated method stub
		if (p.hasName("hidden")) {
			hidden = p.getBoolean();
		}
	}
	
	@Override
	public FrameEntity clone() {
		return null;
	}
}
