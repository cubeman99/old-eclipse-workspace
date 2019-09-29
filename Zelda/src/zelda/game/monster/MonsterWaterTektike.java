package zelda.game.monster;

import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.game.entity.CollisionBox;
import zelda.game.world.tile.GridTile;


public class MonsterWaterTektike extends BasicMonster {

	public MonsterWaterTektike() {
		super();

		health.fill(2);
		contactDamage = 2;
		sprite = new Sprite(Resources.SHEET_MONSTERS);
		sprite.newAnimation(Animations.createStrip(12, 7, 15, 2));

		moveSpeed = 0.1;
		fallsInHoles = false;
		reboundOffFrameEdge = true;
		numDirectionAngles = 8;
		moveTimeMin = 60;
		moveTimeMax = 70;
		stopTimeMin = 0;
		stopTimeMax = 0;

		setReaction(ReactionCause.SWITCH_HOOK, new DAMAGE(1));
	}

	@Override
	public MonsterWaterTektike clone() {
		return new MonsterWaterTektike();
	}

	@Override
	protected void performCollisions() {
		super.performCollisions();

		// Collide with water tiles:
		for (int x = 0; x < frame.getWidth(); x++) {
			for (int y = 0; y < frame.getHeight(); y++) {
				GridTile t = frame.getGridTile(x, y);

				if (!t.getProperties().getBoolean("water", false)) {
					Collision.performCollision(this, CollisionBox.TILE_BOX,
							t.getPosition());
				}
			}
		}
	}

	@Override
	public void updateAI() {
		super.updateAI();

		if (moving) {
			if (motionTimer > motionDuration - 20)
				speed = Math.max(0.1, speed - 0.03);
			else
				speed = Math.min(1.5, speed + 0.03);
			// if (motionTimer > motionDuration - 20)
			// velocity.setLength(Math.max(0.1, velocity.length() - 0.03));
			// else
			// velocity.setLength(Math.min(1.5, velocity.length() + 0.03));
		}
	}
}
