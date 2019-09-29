package zelda.game.entity.projectile;

import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.GMath;
import zelda.game.entity.Entity;
import zelda.game.entity.effect.Fire;
import zelda.game.entity.object.dungeon.ObjectLantern;
import zelda.game.monster.Monster;
import zelda.game.player.Player;


public class ProjectileRodFire extends Projectile {
	private Player player;

	public ProjectileRodFire(Player player, Vector center, double direction) {
		super(player.getGame(), center.plus(Vector.polarVector(8,
				GMath.toRadians(direction))), player.getZPosition(), Vector
				.polarVector(2, GMath.toRadians(direction)));

		this.player = player;
		this.sprite = new Sprite(Resources.SHEET_PLAYER_ITEMS,
				Animations.ITEM_MAGIC_ROD_FIRE);
		this.sprite.setOrigin(8, 8);
	}

	@Override
	public void onHitMonster(Monster m) {
		m.triggerReaction(ReactionCause.ROD_FIRE, 0, this, position);
		destroy();
	}

	@Override
	public void crash() {
		ObjectLantern obj = (ObjectLantern) Collision.getInstanceMeeting(
				softCollidable, position, ObjectLantern.class);
		if (obj != null && !obj.isLit()) {
			obj.light();
		}
		else {
			Entity fire = new Fire(game, position);
			game.addEntity(fire);
			player.itemMagicRod.replaceFireObject(this, fire);
		}
		

		destroy();
	}
}
