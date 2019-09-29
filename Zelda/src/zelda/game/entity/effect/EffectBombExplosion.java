package zelda.game.entity.effect;

import zelda.common.Resources;
import zelda.common.geometry.Vector;


public class EffectBombExplosion extends Effect {

	public EffectBombExplosion(Vector position) {
		super(Resources.SPRITE_EFFECT_BOMB_EXPLOSION, position);
	}
}
