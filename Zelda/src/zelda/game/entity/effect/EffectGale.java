package zelda.game.entity.effect;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;


public class EffectGale extends Effect {
	private static final int DELAY = 30;
	private Sprite objectSprite;
	private int timer;
	private int zPosition;
	private int[] offsets;


	public EffectGale(Sprite sprite, Vector position) {
		super(Resources.SPRITE_EFFECT_GALE, position);
		objectSprite = new Sprite(sprite);
		timer = 0;
		zPosition = 0;
		offsets = new int[] {0, 2, 0, -2};
		Sounds.EFFECT_GALE_SEED.play();
	}

	@Override
	public void update() {
		super.update();
		timer++;
		if (timer > DELAY) {
			zPosition += 4;
			if (position.y - zPosition < 16)
				destroy();
		}
	}

	@Override
	public void preDraw() {
		if (zPosition > 0) {
			Draw.drawSprite(Resources.SPRITE_SHADOW, position);
		}
	}

	@Override
	public void draw() {
		int xOffset = (zPosition == 0 ? offsets[timer % offsets.length] : 0);
		Draw.drawSprite(objectSprite,
				position.minus(8 + xOffset, 8 + zPosition));
		Draw.drawSprite(sprite, position.minus(8, 8 + zPosition));
	}
}
