package zelda.game.entity.seeds;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.game.entity.effect.Effect;
import zelda.game.entity.object.global.ObjectOwl;
import zelda.game.player.Player;
import zelda.game.player.items.ItemSatchel;


public class MysterySeed extends Seed {

	public MysterySeed(Player player) {
		this(player, new Vector());
	}

	public MysterySeed(Player player, Vector position) {
		super(player, position, ItemSatchel.TYPE_MYSTERY_SEED);
	}

	@Override
	public void use() {
		addSeedEntity(new Effect(new Sprite(Resources.SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_MYSTERY_SEED, false).setOrigin(8, 8),
				position.minus(0, zPosition + 4)));
		Sounds.EFFECT_MYSTERY.play();
	}

	@Override
	public void update() {
		super.update();
		
		if (zPosition < 10) {
			ObjectOwl owl = (ObjectOwl) Collision.getInstanceMeeting(
					softCollidable, position, ObjectOwl.class);
			if (owl != null) {
				owl.activate();
				use();
				destroy();
			}
		}
	}
}
