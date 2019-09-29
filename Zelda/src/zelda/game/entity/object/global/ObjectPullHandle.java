package zelda.game.entity.object.global;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;


public class ObjectPullHandle extends FrameObject {
	public static final int MAX_LENGTH = 64;
	private boolean holding;
	private boolean pulling;
	private int pullTimer;
	private boolean canPull;
	
	
	public ObjectPullHandle() {
		super();
		imageSheet = Resources.SHEET_GENERAL_TILES;
	}
	
	public int getDir() {
		return properties.getInt("dir", 0);
	}
	
	public boolean setLength(double length) {
		if (length >= MAX_LENGTH) {
			properties.set("length", MAX_LENGTH);
			return true;
		}
		properties.set("length", length);
		return false;
	}
	
	public double getLength() {
		return properties.getDouble("length", 0);
	}
	
	private void createCollisionBox() {
		Vectangle box = new Vectangle(6, 6, 4, 4);
		box.extend(getLength() + 12, getDir());
		if (getDir() == 3)
			box.extend(4, 3);
		collisionBox = new CollisionBox(box);
	}
	
	@Override
	public void onGrab() {
		if (game.getPlayer().getDir() == (getDir() + 2) % 4) {
			if (!holding) {
				holding = true;
				Sounds.PLAYER_PICKUP.play();
			}
			double placeDist = (getDir() == 3 ? 4 : (getDir() == 1 ? 5 : 2));
    		game.getPlayer().setPositionByCenter(getCenter().plus(
    				Direction.lengthVector(16 + getLength() + placeDist, getDir())));
		}
	}
	
	@Override
	public void onPull() {
		if (game.getPlayer().getDir() == (getDir() + 2) % 4) {
    		pulling = true;
    		
    		if (canPull) {
    			pullTimer++;
    			
    			double lengthPrev = getLength();
    			double dist       = 0.25;
    			double length     = lengthPrev + dist;
    			
    			if (pullTimer == 1 && lengthPrev < MAX_LENGTH - GMath.EPSILON)
    				Sounds.OBJECT_MOVE.play();
    			
    			if (setLength(length)) {
    				dist = MAX_LENGTH - length;
    				properties.script("event_extend", this, frame);
    				if (lengthPrev < MAX_LENGTH - GMath.EPSILON) {
        				Sounds.OBJECT_MOVE.stop();
    					Sounds.OBJECT_CHEST_OPEN.play();
    				}
    			}
    			
    			double placeDist = (getDir() == 3 ? 4 : (getDir() == 1 ? 5 : 2));
    			game.getPlayer().setPositionByCenter(getCenter().plus(
    					Direction.lengthVector(16 + getLength() + placeDist, getDir())));
    			createCollisionBox();
    		}
    		else {
    			game.getPlayer().setAnimation(Animations.PLAYER_GRAB);
    		}
		}
	}
	
	@Override
	public void update() {
		super.update();
		
//		if (holding && game.getPlayer().itemBracelet.isPulling())
//			onPull();
		
		double length = getLength();
		if (length > 0 && !holding) {
			length -= 0.25;
			setLength(length);
			if (length <= 0) {
				properties.script("event_retract", this, frame);
				Sounds.OBJECT_DOOR.play();
			}
			createCollisionBox();
		}

		if (!pulling) {
			pullTimer = 0;
			canPull   = true;
		}
		else if (canPull) {
			if (pullTimer >= 40) {
				canPull   = false;
				pullTimer = 20;
			}
		}
		else {
			if (pullTimer-- <= 0)
				canPull = true;
		}
		
		pulling = false;
		if (!game.getPlayer().itemBracelet.isGrabbing()) {
			if (holding && getLength() > MAX_LENGTH - GMath.EPSILON) {
				Sounds.OBJECT_MOVE.stop();
				Sounds.OBJECT_CHEST_OPEN.play();
			}
			holding = false;
		}
	}
	
	@Override
	public void initialize() {
		setBreakSprite(Animations.EFFECT_BREAK_ROCKS);
		sprite.newAnimation(new Animation(7, 3 + getDir()));
		createCollisionBox();
		holding = false;
		pulling = false;
		canPull = true;
		pullTimer = 0;
	}
	
	@Override
	public void setup() {
		super.setup();
		
		objectData.addProperty("solid", true);
		objectData.addProperty("dir", 3);
		objectData.addProperty("length", 0);
		
		objectData.addEvent("event_extend", "Called when the handle is fully extended.");
		objectData.addEvent("event_retract",  "Called when the handle retracts to its resting position.");
	}
	
	@Override
	public void draw() {
		double length = getLength();
		int dir       = getDir();
		Vector extension = Direction.lengthVector(8 + length, dir);
		
		if (length > 0) {
			for (double i = 0; i < length; i += 16) {
				Vector add = Direction.lengthVector(8 + i, dir);
				Draw.drawImage(imageSheet, new Point(dir % 2 == 0 ? 5 : 6, 3),
						position.plus(add));
			}
		}
		
		Draw.drawSprite(sprite, position.plus(extension));
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		imageSheet = Resources.SHEET_GENERAL_TILES;
		sprite.setSheet(imageSheet);
		sprite.newAnimation(new Animation(7, 3 + getDir()));

		double length = getLength();
		int dir       = getDir();
		Vector extension = Direction.lengthVector(8 + length, dir);
		
		if (length > 0) {
			for (double i = 0; i < length; i += 16) {
				Vector add = Direction.lengthVector(8 + i, dir);
				Draw.drawImage(imageSheet, new Point(dir % 2 == 0 ? 5 : 6, 3),
						new Vector(pos).plus(add));
			}
		}
		
		Draw.drawSprite(sprite, new Vector(pos).plus(extension));
	}

	@Override
	public Point createSpriteSource() {
		return new Point();
	}

	@Override
	public FrameObject clone() {
		return new ObjectPullHandle();
	}
}
