package zelda.game.entity.object.dungeon;

import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.game.entity.object.FrameObject;


public class ObjectPlant extends FrameObject {
	private int growTimer;
	private boolean cut;


	@Override
	public void initialize() {
		growTimer = 0;
		cut = false;

		setBreakSprite(Animations.EFFECT_LEAVES);
	}
	
	@Override
	public void setup() {
		super.setup();
		objectData.addProperty("swordable", true);
		objectData.addProperty("boomerangable", true);
	}
	
	@Override
	public void breakObject() {
		sword();
	}

	@Override
	public Point createSpriteSource() {
		return new Point(4, 3);
	}

	@Override
	public FrameObject clone() {
		return new ObjectPlant();
	}

	@Override
	public void update() {
		super.update();
		sprite.update();

		if (cut) {
			growTimer++;
			if (growTimer == 8 * 60) {
				sprite.newAnimation(false, new Animation().addFrame(12, 4, 2));
			}
			if (growTimer > 8 * 60 && sprite.isAnimationDone()) {
				growTimer = 0;
				cut = false;
				sprite.newAnimation(true, new Animation(4, 3));
			}
		}
	}

	@Override
	public void sword() {
		if (!cut) {
			createDrop();
			createBreakEffect();
			sprite.newAnimation(true, new Animation(5, 2));
			growTimer = 0;
			cut = true;
		}
	}
}
