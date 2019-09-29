package zelda.game.entity.effect;

import zelda.common.geometry.Vector;
import zelda.common.graphics.Sprite;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.EntityObject;


public class EffectNEW extends EntityObject {

	public EffectNEW(Sprite sprite, Vector position) {
		super();

		this.sprite = new Sprite(sprite);
		this.position = new Vector(position);
		softCollisionBox = new CollisionBox(-2, -2, 4, 4);
		hardCollisionBox = new CollisionBox(-2, -2, 4, 4);

		collideWithWorld = false;
		collideWithFrameBoundaries = false;
		collisionAutoDodge = false;
		collisionDodgeable = false;
		reboundOffFrameEdge = false;
		reboundOffWalls = false;
		affectedByGravity = false;
		destroyedOutsideFrame = false;
		destroyedInHoles = false;
		drawShadow = false;
		bounces = false;
		dynamicDepth = false;
	}

	protected void onFinish() {
		destroy();
	}

	@Override
	public void update() {
		super.update();
		if (sprite.isAnimationDone())
			onFinish();
	}
}
