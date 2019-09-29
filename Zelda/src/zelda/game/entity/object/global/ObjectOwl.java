package zelda.game.entity.object.global;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.Sprite;
import zelda.game.control.text.Message;
import zelda.game.entity.effect.Effect;
import zelda.game.entity.object.FrameObject;
import zelda.game.entity.projectile.Projectile;
import zelda.game.entity.projectile.ProjectileSeed;


public class ObjectOwl extends FrameObject {
	private boolean activated;
	private int timer;
	private Point[] effectPositions;
	private int effectIndex;
	private Sprite effectSprite;



	public ObjectOwl() {
		super();
		properties.set("advice_text", "");
		imageSheet = Resources.SHEET_GENERAL_TILES;

		effectSprite = new Sprite(Resources.SHEET_SPECIAL_EFFECTS,
				new Animation().addFrame(9, 9, 8).addFrame(9, 9, 10)
						.addFrame(9, 9, 10).addFrame(9, 9, 11), false)
				.setOrigin(8, 8);

		effectPositions = new Point[] {new Point(10, 7), new Point(2, 8),
				new Point(15, 10), new Point(2, 4), new Point(7, 14),
				new Point(13, 1)};
	}

	public Message getMessage() {
		return new Message(properties.get("advice_text", "..."));
	}

	public void activate() {
		if (!activated) {
			activated = true;
			timer = 0;
			effectIndex = 0;
		}
	}
	
	@Override
	public void onHitByProjectile(Projectile proj) {
		super.onHitByProjectile(proj);
		if (proj instanceof ProjectileSeed) {
			if (((ProjectileSeed) proj).getSeedIndex() == 4) {
				activate();
				((ProjectileSeed) proj).crash();
			}
		}
	}
	
	@Override
	public void initialize() {
		activated   = false;
		timer       = 0;
		effectIndex = 0;
	}
	
	@Override
	public void setup() {
		super.setup();

		objectData.addProperty("advice_text", "");
		objectData.addProperty("solid", true);
	}

	@Override
	public Point createSpriteSource() {
		return new Point(0, 0);
	}

	@Override
	public FrameObject clone() {
		return new ObjectOwl();
	}

	@Override
	public void update() {
		super.update();

		if (activated) {
			timer++;
			if (timer % 8 == 0 && effectIndex < effectPositions.length) {
				game.addEntity(new Effect(effectSprite, position
						.plus(new Vector(effectPositions[effectIndex++]))));
			}
			if (timer == 48) {
				sprite.newAnimation(new Animation(new AnimationFrame(1)
						.addPart(1, 0, -8, 0).addPart(2, 0, 8, 0)));
			}
			if (timer > 83) {
				game.readMessage(getMessage());
				activated = false;
			}
		}
		else {
			sprite.newAnimation(new Animation(0, 0));
		}
	}
}
