package zelda.game.entity;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.game.entity.object.FrameObject;
import zelda.game.monster.Monster;


public class BombExplosion extends EntityObject {

	public BombExplosion(Vector position) {
		super();
		this.position = new Vector(position);
		this.sprite = new Sprite(Resources.SPRITE_EFFECT_BOMB_EXPLOSION);
		this.drawShadow = false;
		this.affectedByGravity = false;
		this.destroyedInHoles = false;
		this.collideWithWorld = false;

		hardCollisionBox = new CollisionBox(-12, -12, 24, 24);
		softCollisionBox = new CollisionBox(-12, -12, 24, 24);
	}

	@Override
	public void update() {
		super.update();
		ArrayList<Entity> entities = Collision.getInstancesMeeting(
				softCollidable, Entity.class);

		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);

			if (entities.get(i) instanceof FrameObject) {
				FrameObject obj = (FrameObject) entities.get(i);

				if (obj.isBombable())
					obj.bomb();
			}

			else if (entities.get(i) instanceof Unit
					&& sprite.getFrameIndex() >= 3) {
				if (e instanceof Monster)
					((Monster) e).triggerReaction(ReactionCause.BOMB, 0, this,
							position);
				else
					((Unit) e).damage(2, getCenter());
			}
		}

		if (sprite.isAnimationDone())
			destroy();
	}
}
