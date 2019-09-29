package zelda.game.entity.projectile;

import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.util.Direction;
import zelda.game.control.GameInstance;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.EntityObject;
import zelda.game.entity.object.FrameObject;
import zelda.game.monster.Monster;
import zelda.game.player.Player;
import zelda.game.world.tile.GridTile;


public abstract class Projectile extends EntityObject {
	protected int angledDir;



	// ================== CONSTRUCTORS ================== //

	public Projectile() {
		this.angledDir = 0;
		this.collideWithWorld = true;
		this.affectedByGravity = false;
		this.destroyedInHoles = false;
		this.dynamicDepth = false;
		this.drawShadow = true;
		this.destroyedOutsideFrame = true;

		hardCollisionBox = new CollisionBox(-1, -1, 2, 2);
		softCollisionBox = new CollisionBox(-1, -1, 2, 2);

		setDepth(0);
	}

	public Projectile(GameInstance game, Vector position, double z,
			Vector velocity) {
		this();

		this.game = game;
		this.position = new Vector(position);
		this.velocity = new Vector(velocity);
		this.zPosition = z;
	}
	

	public void onHitMonster(Monster m) {}

	public void onHitPlayer(Player player) {}

	public void onHitPlayerShield(Player player) {}

	public void crash() {
		destroy();
	}



	// =================== ACCESSORS =================== //



	// ==================== MUTATORS ==================== //

	public void setAngledDirection(int dir) {
		this.angledDir = dir;
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void update() {
		// Check hit frame objects.
		FrameObject obj = (FrameObject) Collision.getInstanceMeeting(
				hardCollidable, position.plus(velocity), FrameObject.class);
		if (obj != null) {
			obj.onHitByProjectile(this);
		}
		
		// Check hit solids.
		if (!Collision.canDodgeCollisions(this))
			crash();
		
		// Check hit monsters.
		Monster m = (Monster) Collision.getInstanceMeeting(softCollidable,
				position, Monster.class);
		if (m != null && isNearZ(m) && !m.isPassable())
			onHitMonster(m);

		// Check hit player.
		Player player = game.getPlayer();
		if (isNearZ(player)) {
			if (player.itemShield.isBlocking()
					&& Collision.isTouching(softCollidable,
							player.getShieldCollidable())
					& !player.isPassable()) {
				onHitPlayerShield(game.getPlayer());
			}
			else if (Collision.isTouching(softCollidable, game.getPlayer())
					&& !game.getPlayer().isPassable()) {
				onHitPlayer(game.getPlayer());
			}
		}

		// Update sprite direction-variation.
		sprite.setVariation(angledDir);
		super.update();
	}
}
