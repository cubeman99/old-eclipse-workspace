package zelda.game.entity.effect;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Sprite;


public class EffectBossKeyUsed extends Effect {

	public EffectBossKeyUsed(Point blockPosition) {
		super(new Sprite(Resources.SHEET_ICONS_LARGE), new Vector(blockPosition));

		sprite.newAnimation(false, new Animation().addFrame(8, 2, 5, 0, -4)
				.addFrame(20, 2, 5, 0, -12));

		setDepth(-500);
	}
}
