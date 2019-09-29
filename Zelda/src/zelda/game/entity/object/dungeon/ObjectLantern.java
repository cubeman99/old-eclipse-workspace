package zelda.game.entity.object.dungeon;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.graphics.Sprite;
import zelda.common.properties.Property;
import zelda.common.util.Colors;
import zelda.game.entity.object.FrameObject;


/**
 * 
 * @author David Jordan
 */
public class ObjectLantern extends FrameObject {
	private Animation animationLit;
	private Animation animationUnlit;
	private boolean lit;


	public ObjectLantern() {
		animationLit   = Animations.createStrip(16, 1, 8, 4);
		animationUnlit = new Animation(0, 8);
	}
	
	public boolean isLit() {
		return lit;
	}
	
	public void light() {
		if (!lit) {
			lit = true;
			properties.set("lit", true);
			sprite.newAnimation(lit ? animationLit : animationUnlit);
			properties.script("event_light", this, frame);
		}
	}
	
	@Override
	public void onChangeProperty(Property p) {
		super.onChangeProperty(p);
		
		if (p.hasName("lit")) {
			lit = p.getBoolean();
			sprite.newAnimation(lit ? animationLit : animationUnlit);
			if (lit)
				light();
		}
	}
	
	@Override
	public void initialize() {
		lit = properties.getBoolean("lit", false);
	}
	
	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("lit", false);
		objectData.addProperty("solid", true);
		objectData.addEvent("event_light", "Called when the lantern is lit.");
	}

	@Override
	public void update() {
		super.update();
		
		sprite.update();
	}

	@Override
	public Point createSpriteSource() {
		return new Point(lit ? 1 : 0, 8);
	}

	@Override
	public FrameObject clone() {
		return new ObjectLantern();
	}

	@Override
	public void draw() {
		super.draw();
	}
}
