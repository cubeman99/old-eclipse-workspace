package zelda.game.entity.object;

import zelda.common.Sounds;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Sprite;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.EntityObject;
import zelda.game.entity.effect.Effect;
import zelda.main.Sound;


public class CarriedFrameObject extends EntityObject {
	private Sprite spriteBreakEffect;
	private Sound soundBreak;
	
	public CarriedFrameObject(FrameObject obj) {
		super();

		hardCollisionBox = new CollisionBox(-2, -2, 4, 4);
		softCollisionBox = new CollisionBox(-2, -2, 4, 4);
		
		soundBreak = obj.getSoundBreak();
		sprite = new Sprite(obj.spriteEntity);
		spriteBreakEffect = null;
		if (obj.getBreakEffectSprite() != null)
			spriteBreakEffect = new Sprite(obj.getBreakEffectSprite());
		sprite.setOrigin(8, 14);
	}

	private void breakObject() {
		destroy();
		if (spriteBreakEffect != null) {
			game.addEntity(new Effect(new Sprite(spriteBreakEffect),
					getPosition().minus(new Vector(sprite.getOrigin()))));
			Sounds.play(soundBreak);
		}
	}


	@Override
	protected void onLand() {
		if (!isDestroyed())
			breakObject();
	}

	@Override
	public void update() {
		super.update();

		if (colliding)
			breakObject();
	}
}
