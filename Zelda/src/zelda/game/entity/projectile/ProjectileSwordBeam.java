package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.Direction;
import zelda.common.util.GMath;
import zelda.game.control.GameInstance;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.Entity;
import zelda.game.entity.effect.EffectCling;
import zelda.game.monster.Monster;


public class ProjectileSwordBeam extends Projectile {

	public ProjectileSwordBeam(GameInstance game, Vector center, double z,
			int dir) {
		super(game, center
				.plus(Vector.polarVector(8, GMath.toRadians(dir * 90))), z,
				Vector.polarVector(3, GMath.toRadians(dir * 90)));

		this.sprite = new Sprite(Resources.SHEET_PLAYER_ITEMS,
				Animations.ITEM_SWORD_BEAM);
		this.sprite.setOrigin(8, 8);

		this.drawShadow = false;

		setAngledDirection(dir);

		setDepth(-1000);


		position.add(0, 0);
		if (Direction.isHorizontal(dir)) {
			position.y += 4;
			hardCollisionBox = new CollisionBox(-1, -5, 2, 1);
		}
		else {
			hardCollisionBox = new CollisionBox(-1, -1, 2, 2);
		}

		if (dir == Direction.UP)
			position.add(-4, 0);
		else if (dir == Direction.DOWN)
			position.add(3, 0);
	}

	@Override
	public void onHitMonster(Monster m) {
		m.triggerReaction(ReactionCause.SWORD_BEAM, 0, this, position);
		destroy();
	}

	@Override
	public void crash() {
		Entity cling = new EffectCling(position.minus(0, zPosition), true);
		game.addEntity(cling);
		super.crash();
	}
}
