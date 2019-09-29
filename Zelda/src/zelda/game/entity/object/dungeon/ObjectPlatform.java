package zelda.game.entity.object.dungeon;

import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.properties.Property;
import zelda.common.util.Direction;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;


public class ObjectPlatform extends FrameObject {
	private Point size;
	
	
	public ObjectPlatform() {
		super();
		properties.set("width", 1);
		properties.set("height", 1);
		
		imageSheet = Resources.SHEET_GENERAL_TILES;
		size       = new Point(1, 1);
	}
	
	@Override
	public void onChangeProperty(Property p) {
		super.onChangeProperty(p);
		
		if (p.hasName("width") || p.hasName("height")) {
			AnimationFrame frame = new AnimationFrame();
			int w = properties.getInt("width", 1);
			int h = properties.getInt("height", 1);
			System.out.println(properties.getPropertiesList().contains(p));
			for (int x = 0; x < w; x++) {
				for (int y = 0; y < h; y++) {
					frame.addPart(1, 1, x * 16, y * 16);
				}
			}
			collisionBox = new CollisionBox(0, 0, w * 16, h * 16);
			sprite.newAnimation(new Animation(frame));
		}
	}
	
	@Override
	public void initialize() {
		size.set(properties.getInt("width", 1),
				properties.getInt("height", 1));
		
		AnimationFrame frame = new AnimationFrame();
		int w = properties.getInt("width", 1);
		int h = properties.getInt("height", 1);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				frame.addPart(1, 1, x * 16, y * 16);
			}
		}
		
		collisionBox = new CollisionBox(0, 0, w * 16, h * 16);
		sprite.newAnimation(new Animation(frame));
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("surface", true);
		objectData.addProperty("width",  1);
		objectData.addProperty("height", 1);
	}
	
	@Override
	public void preDraw() {
		super.draw();
	}
	
	@Override
	public void draw() {
		// Don't draw.
	}
	
	@Override
	public Point createSpriteSource() {
		return new Point(1, 1);
	}

	@Override
	public FrameObject clone() {
		return new ObjectPlatform();
	}
}
