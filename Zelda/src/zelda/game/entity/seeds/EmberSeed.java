package zelda.game.entity.seeds;

import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.game.entity.effect.Fire;
import zelda.game.entity.object.dungeon.ObjectLantern;
import zelda.game.entity.object.global.ObjectOwl;
import zelda.game.player.Player;
import zelda.game.player.items.ItemSatchel;


public class EmberSeed extends Seed {

	public EmberSeed(Player player) {
		this(player, new Vector());
	}

	public EmberSeed(Player player, Vector position) {
		super(player, position, ItemSatchel.TYPE_EMBER_SEED);
	}

	@Override
	public void use() {
		addSeedEntity(new Fire(game, position.minus(0, 5)));
	}
	
	@Override
	public void update() {
		super.update();
		
		if (zPosition < 4) {
			ObjectLantern obj = (ObjectLantern) Collision.getInstanceMeeting(
					softCollidable, position, ObjectLantern.class);
			if (obj != null && !obj.isLit()) {
				obj.light();
				destroy();
			}
		}
	}
}
