package zelda.game.entity.effect;

import zelda.common.Resources;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.util.Direction;
import zelda.game.entity.Entity;


public class EffectDirt extends Entity {
	private Vector position;
	private Vector velocity;
	private int timer;
	private int dir;
	private Sprite sprite;


	public EffectDirt(Vector position, int dir) {
		this.position = new Vector(position);
		this.velocity = new Vector(0, -1);
		this.dir = dir;
		this.sprite = new Sprite(Resources.SHEET_EFFECTS,
				Animations.EFFECT_DIRT).setOrigin(0, -4);


		if (dir == Direction.RIGHT)
			velocity.set(0.5, -2.5);
		else if (dir == Direction.LEFT)
			velocity.set(-0.5, -2.5);
		else if (dir == Direction.UP)
			velocity.set(0, -2.4);
		else
			velocity.set(0, -1.5);
	}

	@Override
	public void update() {
		sprite.update(dir);
		position.add(velocity);

		velocity.add(0, 0.3);
		if (timer++ >= 16)
			destroy();
	}

	@Override
	public void draw() {
		Draw.drawSprite(sprite, position);
	}
}
