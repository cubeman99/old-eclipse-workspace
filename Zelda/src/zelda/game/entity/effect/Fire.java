package zelda.game.entity.effect;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collidable;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vectangle;
import zelda.common.geometry.Vector;
import zelda.common.reactions.ReactionCause;
import zelda.game.control.GameInstance;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.Entity;
import zelda.game.entity.object.FrameObject;
import zelda.game.monster.Monster;


public class Fire extends Effect {
	private GameInstance game;


	public Fire(GameInstance game, Vector position) {
		super(Resources.SPRITE_FIRE, position);
		this.game = game;
		Sounds.EFFECT_EMBER_SEED.play();
	}

	@Override
	public void update() {
		super.update();

		Collidable c = new Collidable() {
			public boolean isSolid() {
				return false;
			}

			public Vector getPosition() {
				return position;
			}

			public CollisionBox getCollisionBox() {
				return new CollisionBox(-4, -4, 8, 8);
			}
		};
		Monster m = (Monster) Collision.getInstanceMeeting(c, Monster.class);
		if (m != null)
			m.triggerReaction(ReactionCause.FIRE, 0, this, position);
	}

	@Override
	public void onDestroy() {
		Vectangle vect = new Vectangle(position.x - 8, position.y - 8, 16, 16);
		ArrayList<FrameObject> burnables = new ArrayList<FrameObject>();

		// Make a list of all burnable objects touching this fire:
		for (int i = 0; i < game.getEntities().size(); i++) {
			Entity e = game.getEntities().get(i);

			if (e instanceof FrameObject) {
				FrameObject obj = (FrameObject) e;
				if (obj.isBurnable() && vect.touches(obj.getVect()))
					burnables.add(obj);
			}
		}

		// Find the closest burnable object.
		double closestDist = 0;
		FrameObject closestObj = null;

		for (int i = 0; i < burnables.size(); i++) {
			double dist = new Vector(position).distanceTo(new Vector(burnables
					.get(i).getCenter()));
			if (dist < closestDist || closestObj == null) {
				closestDist = dist;
				closestObj = burnables.get(i);
			}
		}

		// Burn the closest burnable object.
		if (closestObj != null) {
			closestObj.burn();
		}
	}
}
