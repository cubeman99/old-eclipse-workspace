package zelda.game.entity.effect;

import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.game.entity.Entity;


public class Effect extends Entity {
	protected Vector position;
	protected Sprite sprite;


	public Effect() {
		this.sprite = null;
		this.position = new Vector();
	}

	public Effect(Sprite sprite, Vector position) {
		super();
		this.sprite = new Sprite(sprite);
		this.position = new Vector(position);
	}

	protected void onFinish() {
		destroy();
	}

	@Override
	public void update() {
		if (sprite != null) {
			if (sprite.isAnimationDone()) {
				onFinish();
			}
			sprite.update();
		}
	}

	@Override
	public void draw() {
		if (sprite != null)
			Draw.drawSprite(sprite, position);
	}
}
