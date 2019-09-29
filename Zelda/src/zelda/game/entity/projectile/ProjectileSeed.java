package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.Direction;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.Entity;
import zelda.game.entity.effect.Effect;
import zelda.game.entity.object.dungeon.ObjectLantern;
import zelda.game.entity.object.global.ObjectOwl;
import zelda.game.monster.Monster;
import zelda.game.player.items.Item;
import zelda.game.player.items.ItemSatchel;


public class ProjectileSeed extends PlayerProjectile {
	private int seedIndex;
	private int bounceCount;

	public ProjectileSeed(Item item, int seedIndex, Vector pos, double z,
			int dir) {
		super(item, pos, z, Direction.angledLengthVector(3, dir));

		this.seedIndex = seedIndex;
		this.sprite = new Sprite(Resources.SHEET_ICONS_SMALL, seedIndex, 0);
		this.sprite.setOrigin(4, 4);

		hardCollisionBox = new CollisionBox(-3, -7, 6, 1);
		softCollisionBox = new CollisionBox(-4, -8, 6, 3);

		bounceCount = 0;
//		collideWithWorld = false;
		reboundOffWalls = true;
		
		if (!Collision.canDodgeCollisions(this))
			createSeedEffect();
		
		setAngledDirection(dir);
	}
	
	public int getSeedIndex() {
		return seedIndex;
	}

	@Override
	public void onHitMonster(Monster m) {
		m.triggerReaction(ReactionCause.SEEDS[seedIndex], 0, this, position);
		destroy();
	}

	public void createSeedEffect() {
		Entity e = new Effect(new Sprite(Resources.SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_SEEDS[seedIndex], false).setOrigin(8, 8),
				position.minus(0, zPosition));
		playerItem.getEntityTracker().replaceEntity(this, e);
		game.addEntity(e);
		destroy();
	}
	
	@Override
	public void onRebound() {
		if (!isDestroyed()) {
    		bounceCount++;
    		if (bounceCount >= 3)
    			createSeedEffect();
		}
	}

	@Override
	public void crash() {
		// Do nothing.
	}

	@Override
	public void update() {
		super.update();
		
		if (seedIndex == ItemSatchel.TYPE_EMBER_SEED) {
			ObjectLantern obj = (ObjectLantern) Collision.getInstanceMeeting(
					softCollidable, position.plus(velocity.scaledBy(2)),
					ObjectLantern.class);
			if (obj != null && !obj.isLit()) {
				obj.light();
				destroy();
			}
		}
		if (Collision
				.placeMeetingSolid(hardCollidable, position.plus(velocity))) {
			//bounceCount += 10;
		}

//		if (bounceCount >= 3)
//			createSeedEffect();
	}
}
