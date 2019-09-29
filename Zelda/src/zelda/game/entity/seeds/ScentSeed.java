package zelda.game.entity.seeds;

import zelda.common.geometry.Vector;
import zelda.game.player.Player;
import zelda.game.player.items.ItemSatchel;


public class ScentSeed extends Seed {

	public ScentSeed(Player player) {
		this(player, new Vector());
	}

	public ScentSeed(Player player, Vector position) {
		super(player, position, ItemSatchel.TYPE_SCENT_SEED);
	}

	@Override
	public void use() {
		addSeedEntity(new ScentPod(position));
	}
}
