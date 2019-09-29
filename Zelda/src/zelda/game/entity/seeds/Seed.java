package zelda.game.entity.seeds;

import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.collision.Collision;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animations;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.game.entity.CollisionBox;
import zelda.game.entity.Entity;
import zelda.game.entity.EntityObject;
import zelda.game.entity.effect.Effect;
import zelda.game.monster.Monster;
import zelda.game.player.Player;


public class Seed extends EntityObject {
	private Player player;
	private int seedIndex;

	public Seed(Player player, Vector position, int seedIndex) {
		super(player.getGame());
		setDepth(-400);

		hardCollisionBox = new CollisionBox(-1, -1, 2, 2);
		softCollisionBox = new CollisionBox(-1, -1, 2, 2);

		this.seedIndex = seedIndex;
		this.position = new Vector(position);
		this.player = player;

		sprite = new Sprite(Resources.SHEET_ICONS_SMALL, seedIndex, 0);
		sprite.setOrigin(4, 6);

		this.collideWithWorld = false;
	}

	public void createSeedEffect() {
		Entity e = new Effect(new Sprite(Resources.SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_SEEDS[seedIndex], false).setOrigin(8, 8),
				position.minus(0, zPosition));
		setSeedEntity(e);
		game.addEntity(e);
		destroy();
	}

	public void addSeedEntity(Entity e) {
		game.addEntity(e);
		setSeedEntity(e);
	}

	public void setSeedEntity(Entity e) {
		player.itemSatchel.setSeedObject(seedIndex, e);
	}

	public void use() {
		createSeedEffect();
	}

	@Override
	protected void onLand() {
		// TODO
		// use();

		use();
		destroy();
	}

	@Override
	public void update() {
		super.update();

		if (zPosition < 10) {
			Monster m = (Monster) Collision.getInstanceMeeting(softCollidable,
					position, Monster.class);
			if (m != null && !m.isPassable()) {
				m.triggerReaction(ReactionCause.SEEDS[seedIndex], 0, this,
						position);
				destroy();
			}
		}
	}
}
