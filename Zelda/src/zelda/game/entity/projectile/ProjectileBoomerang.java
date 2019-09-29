package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.GMath;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.collectable.Collectable;
import zelda.game.entity.effect.EffectCling;
import zelda.game.entity.object.FrameObject;
import zelda.game.monster.Monster;
import zelda.game.player.Player;


public class ProjectileBoomerang extends Projectile {
	private static final int[] FLY_TIME = {41, 100};
	private Player player;
	private int flyTimer;
	private int flyTime;
	private boolean returning;
	private Collectable collectable;
	private double speed;
	private int level;



	public ProjectileBoomerang(Player player, Vector center, double direction) {
		super(player.getGame(), center, player.getZPosition(), Vector
				.polarVector(1.5, GMath.toRadians(direction)));

		this.level = player.itemBoomerang.getLevel();

		this.destroyedOutsideFrame = false;
		this.sprite = new Sprite(Resources.SHEET_PLAYER_ITEMS,
				Animations.ITEM_BOOMERANG_PLAYER[level]);
		this.sprite.setOrigin(8, 8);

		this.speed = (level > 0 ? 2 : 1.5);
		this.velocity.setLength(speed);
		this.flyTime = (level > 0 ? FLY_TIME[1] : FLY_TIME[0]);

		this.flyTimer = 0;
		this.player = player;
		this.returning = false;
		this.collectable = null;

		softCollisionBox = new CollisionBox(-3, -3, 6, 6);
		Sounds.ITEM_BOOMERANG.loop();
	}

	@Override
	public void onHitMonster(Monster m) {
		if (!returning) {
			returning = true;
			m.triggerReaction(ReactionCause.BOOMERANG, level, this, position);
		}
	}

	@Override
	public void crash() {
		if (!returning) {
			returning = true;
			game.addEntity(new EffectCling(position));
		}
	}

	@Override
	public void update() {
		super.update();

		if (level > 0) {
			FrameObject obj = (FrameObject) Collision.getInstanceMeeting(
					hardCollidable, getPosition(), FrameObject.class);
			if (obj != null && obj.isMagicBoomerangable())
				obj.breakObject();
		}

		if (returning) {
			collideWithWorld = false;
			
			// Move toward the player.
			Vector goal = player.getPosition().plus(8, 8);
			velocity.set(new Vector(position, goal).setLength(speed));

			// Return to the player.
			if (position.distanceTo(goal) < 2) {
				if (collectable != null)
					collectable.collect();
				destroy();
				Sounds.ITEM_BOOMERANG.stop();
			}
		}
		else {
			// Return on hit frame edge or after a certain amount of time.
			if (++flyTimer > flyTime || isOutsideFrame())
				returning = true;

			// Check for collectables.
			Collectable e = (Collectable) Collision.getInstanceMeeting(
					softCollidable, getPosition(), Collectable.class);
			if (e != null && e.isReady()) {
				returning = true;
				collectable = e;
				collectable.getSprite().setFrameIndex(0);
				collectable.destroy();
			}
		}
	}

	@Override
	public void draw() {
		super.draw();

		// Draw the picked-up collectable.
		if (collectable != null)
			Draw.drawSprite(collectable.getSprite(), getPosition().plus(0, 3));
	}
}
