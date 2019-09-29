package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.GMath;
import zelda.game.entity.Entity;
import zelda.game.entity.effect.DeadProjectile;
import zelda.game.monster.Monster;
import zelda.game.player.items.Item;


public class ProjectileArrow extends PlayerProjectile {
	protected Animation animationCrash;

	public ProjectileArrow(Item item, Vector center, double z, int dir) {
		super(item, center.plus(Vector.polarVector(4, GMath.toRadians(dir * 45))), z,
				Vector.polarVector(3, GMath.toRadians(dir * 45)));

		this.sprite = new Sprite(Resources.SHEET_PLAYER_ITEMS, Animations.ITEM_ARROW_PLAYER);
		this.sprite.setOrigin(8, 8);
		this.animationCrash = Animations.EFFECT_ARROW_DEAD;

		setAngledDirection(dir);
	}

	@Override
	public void onHitMonster(Monster m) {
		m.triggerReaction(ReactionCause.ARROW, 0, this, position);
		destroy();
	}

	@Override
	public void crash() {
		Entity e;

		if (animationCrash == null)
			e = new DeadProjectile(this, sprite.getAnimation());
		else
			e = new DeadProjectile(this, Animations.EFFECT_ARROW_DEAD);

		playerItem.getEntityTracker().replaceEntity(this, e);
		game.addEntity(e);
		super.crash();
		Sounds.EFFECT_CLING.play();
	}
}
