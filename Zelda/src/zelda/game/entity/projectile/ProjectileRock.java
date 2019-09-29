package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.game.player.items.Item;


public class ProjectileRock extends ProjectileArrow {

	public ProjectileRock(Item item, Vector center, double z, int dir) {
		super(item, center, z, dir);

		this.sprite = new Sprite(Resources.SHEET_MONSTER_ITEMS,
				Animations.ITEM_ROCK_PROJECTILE);
		this.sprite.setOrigin(8, 8);
		this.animationCrash = null;
		this.velocity.setLength(2);
	}

}
