package zelda.game.entity.effect;

import zelda.common.Resources;
import zelda.common.drops.DropList;
import zelda.common.geometry.Vector;
import zelda.game.entity.EntityObject;


public class EffectMonsterExplosion extends Effect {
	private DropList drops;

	public EffectMonsterExplosion(DropList drops, Vector position) {
		super(Resources.SPRITE_EFFECT_MONSTER_EXPLOSION, position);
		this.drops = drops;
	}

	@Override
	protected void onFinish() {
		super.onFinish();
		if (drops != null) {
			EntityObject e = drops.createDropObject();
			if (e != null) {
				e.setPosition(position.plus(0, 4));
				game.addEntity(e);
			}
		}
	}
}
