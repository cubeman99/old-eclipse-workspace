package zelda.game.entity.object.dungeon.door;

import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.properties.Property;
import zelda.common.util.Direction;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;
import zelda.game.world.tile.ObjectTile;


public abstract class ObjectDoor extends FrameObject {
	protected int dir;
	protected boolean opened;
	protected boolean staysOpen;
	protected Animation animationOpen;
	protected Animation animationClose;
	protected int sourceX;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public ObjectDoor(int sourceX) {
		super();
		dir = Direction.DOWN;

		properties.set("dir", Direction.DOWN);
		this.sourceX = sourceX;
		
		sprite.newAnimation(new Animation(sourceX, 4 + dir));
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean isOpen() {
		return properties.getBoolean("open", false);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Open the door. **/
	public void open() {
		if (!isOpen()) {
			opened = true;
			properties.set("open", true);
			properties.set("solid", false);
			
			if (frame.hasBegun()) {
				Sounds.OBJECT_DOOR.play();
				sprite.newAnimation(
						false, new Animation().addFrame(6, new Point(sourceX, 4 + dir),
								Direction.lengthVector(8, (dir + 2) % 4)).addFrame(6, -1, -1));
				if (staysOpen)
					objectTile.getProperties().set("open", true);
			}
			else {
				sprite.newAnimation(false, new Animation(-1, -1));
			}
		}
	}
	
	/** Close the door. **/
	public void close() {
		if (isOpen()) {
    		opened = false;
			if (frame.hasBegun()) {
    			Sounds.OBJECT_DOOR.play();
        		if (properties.getBoolean("closable", true)) {
            		properties.set("open", false);
        			properties.set("solid", true);
            		sprite.newAnimation(false,
            				new Animation().addFrame(6, new Point(sourceX, 4 + dir),
            				Direction.lengthVector(8, (dir + 2) % 4)).addFrame(6,
            				sourceX, 4 + dir));
        		}
			}
			else {
        		properties.set("open", false);
    			properties.set("solid", true);
				sprite.newAnimation(false, new Animation(sourceX, 4 + dir));
			}
		}
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void onChangeProperty(Property p) {
		super.onChangeProperty(p);

		if (p.hasName("open") && (opened != properties.getBoolean("open", false))) {
			if (!opened)
				open();
			else
				close();
		}
		
		if (p.hasName("dir")) {
			dir = p.getInt();
			sprite.newAnimation(new Animation(sourceX, 4 + dir));
		}
	}

	@Override
	public void initialize() {
		properties.define("open", false);
		
		animationOpen = new Animation()
			.addFrame(6, new Point(sourceX, 4 + dir), Direction.lengthVector(8, (dir + 2) % 4))
			.addFrame(6, sourceX, 4 + dir);
		animationClose = new Animation()
			.addFrame(6, new Point(sourceX, 4 + dir), Direction.lengthVector(8, (dir + 2) % 4))
			.addFrame(6, -1, -1);
		
		String link = properties.get("door_link", "");
		if (!link.isEmpty()) {
			ObjectTile t = game.getLevel().getObjectTile(link);
			if (t != null && t.getProperties().getBoolean("open", false)) {
				properties.set("open", true);
			}
		}
		
		opened = properties.getBoolean("open", false);
		dir    = properties.getInt("dir", 0);
		properties.set("solid", !opened);
		
		if (!opened) {
			if (Collision.isTouching(this, game.getPlayer().getHardCollidable())) {
				properties.set("solid", false);
				sprite.newAnimation(false, new Animation(-1, -1));
			}
			else {
				sprite.newAnimation(false, new Animation(sourceX, 4 + dir));
			}
		}
		else {
			sprite.newAnimation(false, new Animation(-1, -1));
		}

		setDepth(500);
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("solid", true);
		objectData.addProperty("dir", 3);
		objectData.addProperty("closable", true);
		objectData.addProperty("door_link");
		objectData.addProperty("open", false);
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		sprite.newAnimation(new Animation(sourceX, 4 + 
				properties.getInt("dir")));
		super.drawTileSprite(pos, frame);
	}

	@Override
	public void update() {
		super.update();
		
		if (!opened && !isSolid()) {
			if (!Collision.isTouching(this, game.getPlayer().getHardCollidable())) {
				game.getPlayer().recordFrameEnterPosition();
				close();
			}
		}
		
		sprite.update();
	}

	@Override
	public Point createSpriteSource() {
		return new Point(sourceX, 4 + dir);
	}
}
