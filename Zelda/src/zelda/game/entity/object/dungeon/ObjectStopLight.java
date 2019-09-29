package zelda.game.entity.object.dungeon;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.Sprite;
import zelda.common.properties.Property;
import zelda.common.properties.PropertyHolder;
import zelda.game.control.script.Function;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.EntityObject;
import zelda.game.entity.object.FrameObject;
import zelda.game.world.Frame;

public class ObjectStopLight extends FrameObject {
	private EntityObject arm;
	private boolean raised;
	
	public ObjectStopLight() {
		imageSheet = Resources.SHEET_GENERAL_TILES;
		
		arm = null;
		collisionBox = new CollisionBox(8, 0, 8, 16);
		sprite.newAnimation(new Animation().addFrame(new AnimationFrame(1)
			.addPart(1, 4, 0, -16).addPart(1, 5, 0, 0)));
	}
	
	@Override
	public void onChangeProperty(Property p) {
		super.onChangeProperty(p);
		
		if (p.hasName("raised") && isRaised() != raised) {
			if (p.getBoolean())
				raise();
			else
				lower();
		}
	}
	
	public boolean isRaised() {
		return properties.getBoolean("raised", false);
	}
	
	public void toggle() {
		if (isRaised())
			lower();
		else
			raise();
	}
	
	public void raise() {
		Sounds.OBJECT_STOP_LIGHT.play();
		raised = true;
		properties.set("raised", true);
		arm.getSprite().newAnimation(
				false, new Animation().addFrame(
				new AnimationFrame(8).addPart(2, 4, 16, -16)
				.addPart(2, 5, 16, 0)).addFrame(-1, -1));
		sprite.newAnimation(false, new Animation().addFrame(new AnimationFrame(8)
		.addPart(1, 4, 0, -16).addPart(1, 5, 0, 0)).addFrame(new AnimationFrame(1)
				.addPart(0, 4, 0, -16).addPart(0, 5, 0, 0)));
	}
	
	public void lower() {
		Sounds.OBJECT_STOP_LIGHT.play();
		raised = false;
		properties.set("raised", false);
		arm.getSprite().newAnimation(
				false, new Animation().addFrame(
				new AnimationFrame(8).addPart(2, 4, 16, -16)
				.addPart(2, 5, 16, 0)).addFrame(4, 4, 4, 16, 0));
		sprite.newAnimation(new Animation().addFrame(new AnimationFrame(8)
				.addPart(1, 4, 0, -16).addPart(1, 5, 0, 0)));
	}
	
	@Override
	public void update() {
		super.update();
		sprite.update();
	}
	
	@Override
	public void initialize() {
		collisionBox = new CollisionBox(8, 0, 8, 16);
		setDepth((int) (-position.y + 14));
		
		arm = new Arm(getPosition());
		game.addEntity(arm);
		raised = isRaised();
		
		if (isRaised()) {
			arm.getSprite().newAnimation(new Animation(-1, -1));
			sprite.newAnimation(new Animation().addFrame(new AnimationFrame(8)
					.addPart(0, 4, 0, -16).addPart(0, 5, 0, 0)));
		}
		else {
			arm.getSprite().newAnimation(new Animation(4, 4, 4, 16, 0));
			sprite.newAnimation(new Animation().addFrame(new AnimationFrame(8)
					.addPart(1, 4, 0, -16).addPart(1, 5, 0, 0)));
		}
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("solid", true);
		objectData.addProperty("raised", false);
		
		objectData.addFunction(new Function("raise") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				raise();
				return "";
			}
		});
		
		objectData.addFunction(new Function("lower") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				lower();
				return "";
			}
		});
		
		objectData.addFunction(new Function("toggle") {
			@Override
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				toggle();
				return "";
			}
		});
	}
	
	@Override
	public Point createSpriteSource() {
		return new Point(1, 5);
	}

	@Override
	public FrameObject clone() {
		return new ObjectStopLight();
	}
	
	@Override
	public void drawTileSprite(Point pos, Frame frame) {
		if (isRaised()) {
			sprite.newAnimation(new Animation().addFrame(new AnimationFrame(1)
					.addPart(0, 4, 0, -16).addPart(0, 5, 0, 0)));
		}
		else {
			sprite.newAnimation(new Animation().addFrame(new AnimationFrame(1)
					.addPart(1, 4, 0, -16).addPart(1, 5, 0, 0).addPart(4, 4, 16, 0)));
		}
		super.drawTileSprite(pos, frame);
	}
	
	private class Arm extends EntityObject {
		public Arm(Vector pos) {
			sprite = new Sprite(Resources.SHEET_GENERAL_TILES,
					new Animation(1, 3, 5, 16, 0));
			solid            = true;
			position         = new Vector(pos);
			hardCollisionBox = new CollisionBox(16, 0, 16, 8);
			softCollisionBox = new CollisionBox(16, 0, 16, 8);
			collideWithWorld = false;
		}
		@Override
		public void update() {
			solid = !isRaised();
		}
	}
}
