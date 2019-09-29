package zelda.game.entity.effect;

import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Sprite;


public class EffectKeyUsed extends Effect {

	public EffectKeyUsed(Point blockPosition) {
		super(new Sprite(Resources.SHEET_ICONS_THIN), new Vector(blockPosition));

		sprite.newAnimation(false, new Animation().addFrame(8, 0, 4, 4, -4)
				.addFrame(20, 0, 4, 4, -12));

		setDepth(-500);
	}
}
