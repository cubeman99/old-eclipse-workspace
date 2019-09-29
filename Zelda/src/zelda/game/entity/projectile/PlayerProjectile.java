package zelda.game.entity.projectile;

import zelda.common.geometry.Vector;
import zelda.game.player.Player;
import zelda.game.player.items.Item;


public class PlayerProjectile extends Projectile {
	protected Player player;
	protected Item playerItem;

	public PlayerProjectile(Item item, Vector position, double z,
			Vector velocity) {
		super(item.getPlayer().getGame(), position, z, velocity);

		this.playerItem = item;
		this.player = item.getPlayer();
	}

	public Player getPlayer() {
		return player;
	}
}
