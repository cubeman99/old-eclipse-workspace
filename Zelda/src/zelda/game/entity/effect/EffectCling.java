package zelda.game.entity.effect;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Sprite;


public class EffectCling extends Effect {

	public EffectCling(Vector position) {
		this(position, false);
	}

	public EffectCling(Vector position, boolean light) {
		super(new Sprite(light ? Resources.SPRITE_CLING_LIGHT
				: Resources.SPRITE_CLING), position);
		setDepth(-500);
		
		Sounds.EFFECT_CLING.play();
	}
}
