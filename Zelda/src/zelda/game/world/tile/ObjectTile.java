package zelda.game.world.tile;

import zelda.common.collision.Collidable;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vector;
import zelda.common.properties.FrameEntityData;
import zelda.common.properties.Properties;
import zelda.common.properties.Property;
import zelda.game.entity.FrameEntity;
import zelda.game.world.Frame;


public class ObjectTile extends AbstractTile {
	private Point position;
	private boolean frameRunning;
	private FrameEntity frameObject;
	private FrameEntity createdObject;
	private FrameEntityData entityData;
	
	

	// ================== CONSTRUCTORS ================== //

	public ObjectTile(Frame frame, Point sourcePos, Properties p) {
		super(frame, sourcePos, p);
		
		frameObject   = null;
		createdObject = null;
		frameRunning  = false;
		position      = new Point();
		entityData    = new FrameEntityData(this, properties);
		
		properties.define("enabled", true);
	}



	// ==================== ACCESSORS ==================== //
	
	public boolean isEnabled() {
		return properties.getBoolean("enabled", true);
	}
	
	public Point getPosition() {
		return position;
	}
	
	public FrameEntityData getEntityData() {
		return entityData;
	}
	
	public FrameEntity getFrameObject() {
		return frameObject;
	}

	public Point getLocation() {
		return new Point(new Vector(position).scaledByInv(16).add(0.5, 0.5));
	}
	
	public Properties getSaveProperties() {
		// Filter out default properties.
		Properties props = new Properties(entityData.getProperties());
		for (int i = 0; i < entityData.getProperties().getNumProperties(); i++) {
			Property p = entityData.getProperties().getProperty(i);
			if (p != null && !defaultProperties.existEquals(p.getName(), p.get())) {
				props.set(p);
			}
		}
		
		return props;
	}
	
	public Rectangle getRect() {
		if (frameObject instanceof Collidable) {
			Collidable c = (Collidable) frameObject;
			if (c.getCollisionBox() != null) {
				return new Rectangle(
        			position.plus(new Point(c.getCollisionBox().getBox().corner)),
        			new Point(c.getCollisionBox().getBox().size)
    			);
			}
		}
		return new Rectangle(position.x, position.y, 16, 16);
	}



	// =================== MUTATORS ==================== //
	
	public void setFrameObject(FrameEntity e) {
		frameObject = e.clone();
		frameObject.setObjectData(entityData);
		frameObject.setup();
		defaultProperties.set(entityData.getProperties());
	}
	
	public void setPosition(Point position) {
		this.position.set(position);
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public Properties getProperties() {
		return entityData.getProperties();
	}
	
	@Override
	public void setProperties(Properties p) {
		super.setProperties(p);
		entityData.getProperties().set(p);
	}
	
	@Override
	public void onChangeProperty(Property p) {
		super.onChangeProperty(p);
		
	}
	
	@Override
	public void onEnterFrame() {
		frameRunning  = true;
		createdObject = null;
		if (frameObject != null && isEnabled()) {
			createdObject = frameObject.clone();
			
			FrameEntityData data = new FrameEntityData(this);
			createdObject.setObjectData(data);
			createdObject.setup();
			data.getProperties().set(entityData.getProperties());
			
			createdObject.setGame(frame.getGame());
			createdObject.setFrame(frame);
			createdObject.begin();
			frame.addEntity(createdObject);
			createdObject.getProperties().script("event_create", createdObject, frame);
		}
	}
	
	public void onPostEnter() {
		if (createdObject != null)
			createdObject.postBegin();
	}
	
	@Override
	public void onFrameBegin() {
		if (createdObject != null)
			createdObject.onFrameBegin();
	}

	@Override
	public void onLeaveFrame() {
		frameRunning  = false;
		createdObject = null;
	}

	@Override
	public void update() {
		if (createdObject != null && !frame.getEntities().contains(createdObject))
			createdObject = null;
	}

	@Override
	public void draw() {
		if (frameObject != null && !frameRunning) {
			frameObject.drawTileSprite(position, frame);
		}
	}

}
