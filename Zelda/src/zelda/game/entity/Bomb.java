package zelda.game.entity;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Sprite;
import zelda.game.player.Player;


public class Bomb extends EntityObject {
	private static final int FUSE_TIME = 108; // Total number of frames before
											  // exploding.
	private static final int TICK_TIME = 36; // Number of frames ticking.

	private Player player;
	private int timer;

	public Bomb(Player player) {
		this(player, new Vector());
	}

	public Bomb(Player player, Vector position) {
		super(player.getGame());
		this.player = player;
		this.position = new Vector(position);
		this.timer = 0;

		hardCollisionBox = new CollisionBox(-2, 5 - 13, 4, 8);
		softCollisionBox = new CollisionBox(-2, 5 - 13, 4, 8);
		
		soundBounce = Sounds.OBJECT_BOMB_BOUNCE;
		
		sprite = new Sprite(Resources.SPRITE_BOMB);
		sprite.setSpeed(0);
	}

	public void burnFuse() {
		if (!isDestroyed()) {
			timer++;

			if (timer > FUSE_TIME - TICK_TIME) {
				sprite.setSpeed(0.5);
			}

			if (timer > FUSE_TIME) {
				// Explode!
				BombExplosion exp = new BombExplosion(position.minus(0, 4));
				exp.setZPosition(zPosition);
				Sounds.EFFECT_BOMB_EXPLODE.play();

				player.itemBombs.setBombObject(game.addEntity(exp));
				player.itemBombs.setBombExploded(true);
				destroy();
			}
		}
	}

	@Override
	public void update() {
		super.update();

		burnFuse();
	}
}
