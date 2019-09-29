package zelda.game.entity.object.dungeon.color;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.Animations;
import zelda.common.properties.Property;
import zelda.common.util.Colors;
import zelda.common.util.Direction;
import zelda.game.entity.Entity;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.object.MovingColorCube;
import zelda.game.entity.object.MovingFrameObject;
import zelda.game.world.Frame;


public class ObjectColorCube extends FrameObject {
	private static final Point[][] colorMappings = {
    	{new Point(0, 5), new Point(1, 3), new Point(2, 4)},
    	{new Point(0, 2), new Point(1, 4), new Point(3, 5)}};
	private int index;
	private boolean needsToCheck;



	public ObjectColorCube() {
		super();
		imageSheet   = Resources.SHEET_COLOR_CUBE;
		needsToCheck = false;
		soundMove    = Sounds.OBJECT_SWITCH;
	}

	@Override
	public void onChangeProperty(Property p) {
		super.onChangeProperty(p);
		
		if (p.hasName("index")) {
			index = p.getInt();
			sprite.newAnimation(new Animation(0, index));
		}

	}
	
	private void checkSlots() {
		ArrayList<Entity> entities = game.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof ObjectColorCubeSlot) {
				ObjectColorCubeSlot slot = (ObjectColorCubeSlot) entities.get(i);
				slot.checkColor();
			}
		}
	}

	private int getColor() {
		if (index < 2)
			return Colors.RED;
		if (index < 4)
			return Colors.BLUE;
		return Colors.YELLOW;
	}

	@Override
	public void initialize() {
		properties.define("index", 0);
		index = properties.getInt("index", 0);

		setBreakSprite(Animations.EFFECT_BREAK_ROCKS);

		sprite.newAnimation(new Animation(0, index));
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid",   true);
		objectData.addProperty("movable", true);
		objectData.addProperty("index",   0);
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		sprite.newAnimation(new Animation(0, properties.getInt("index", 4)));
		super.drawTileSprite(pos, frame);
	}

	@Override
	protected MovingFrameObject createMoveObject(int dir) {
		return new MovingColorCube(this, dir);
	}
	
	@Override
	public void onPreMove(int dir, MovingFrameObject obj) {
		checkSlots();
	}

	@Override
	public void onMove(int dir, MovingFrameObject obj) {
		int axis       = dir % 2;
		Animation anim = new Animation();
		obj.setMoveSpeed(1);
		
		if (dir < 2) {
			for (int i = 0; i < 3; i++) {
				AnimationFrame a = new AnimationFrame(4, i + (axis * 3) + 1,
						index);
				if (axis == 0)
					a.addPart(new Point(i + 1, 6), new Vector(0, -16));
				anim.addFrame(a);
			}
		}
		

		for (int i = 0; i < colorMappings[axis].length; i++) {
			Point map = colorMappings[axis][i];
			if (map.x == index || map.y == index) {
				if (map.x == index)
					index = map.y;
				else
					index = map.x;
				break;
			}
		}

		properties.set("color", getColor());
		
		if (dir >= 2) {
			for (int i = 0; i < 3; i++) {
				AnimationFrame a = new AnimationFrame(4, 3 + (axis * 3) - i,
						index);
				if (axis == 0)
					a.addPart(new Point(3 - i, 6), new Vector(0, -16));
				anim.addFrame(a);
			}
		}
		
		obj.getSprite().newAnimation(false, anim);
		sprite.newAnimation(new Animation(0, index));
		properties.set("index", index);
		needsToCheck = true;
	}

	@Override
	public void update() {
		super.update();
		
		if (!isDestroyed()) {
    		if (needsToCheck) {
    			needsToCheck = false;
    			checkSlots();
    		}
		}
		sprite.newAnimation(new Animation(0, index));
	}

	@Override
	public Point createSpriteSource() {
		return new Point(0, properties.getInt("index", 0));
	}

	@Override
	public FrameObject clone() {
		return new ObjectColorCube();
	}
}
