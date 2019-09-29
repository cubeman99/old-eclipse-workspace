package zelda.game.monster;

import zelda.common.Resources;
import zelda.common.graphics.Animation;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.GMath;
import zelda.game.entity.projectile.MonsterProjectileFireBall;


public class MonsterGopongaFlower extends Monster {
	private static final int OPEN_DURATION = 60;
	private static final int CLOSED_DURATION = 120;
	private static final int SHOOT_DELAY = 20;
	private static final int SHOOT_PAUSE = 10;
	private boolean open;
	private boolean shooting;
	private int timer;

	public MonsterGopongaFlower() {
		super();

		health.fill(1);

		sprite = new Sprite(Resources.SHEET_MONSTERS);
		sprite.newAnimation(new Animation(1, 15));
		contactDamage = 2;
		fallsInHoles = false;

		open = false;
		shooting = false;
		timer = 0;

		properties.set("fixed_spawn", true);

		setReaction(ReactionCause.BOOMERANG, new KILL());
		setReaction(ReactionCause.SWORD, null);
		setReaction(ReactionCause.SHIELD, null);
		setReaction(ReactionCause.SWORD_BEAM, new EFFECT_CLING(true));
		setReaction(ReactionCause.ARROW, new PROJECTILE_CRASH());
		setReaction(ReactionCause.SCENT_SEED, null);
		setReaction(ReactionCause.PEGASUS_SEED, null);
		setReaction(ReactionCause.GALE_SEED, null);
		setReaction(ReactionCause.SWITCH_HOOK, new KILL());
	}

	@Override
	public MonsterGopongaFlower clone() {
		return new MonsterGopongaFlower();
	}

	@Override
	public void updateAI() {
		timer++;

		if (shooting && timer > SHOOT_DELAY + SHOOT_PAUSE) {
			shooting = false;
			MonsterProjectileFireBall proj = new MonsterProjectileFireBall();
			proj.initialize(this, 0);
			game.addEntity(proj);
		}

		if (open) {
			if (timer == SHOOT_DELAY && GMath.random.nextInt(6) == 0) {
				shooting = true;
			}
			if (timer > OPEN_DURATION) {
				open = false;
				timer = 0;
				sprite.newAnimation(new Animation(1, 15));
			}
		}
		else {
			if (timer > CLOSED_DURATION) {
				open = true;
				timer = 0;
				sprite.newAnimation(new Animation(2, 15));
			}
		}
	}

	@Override
	public void draw() {
		super.draw();
		if (shooting) {
			Sprite spr = new Sprite(Resources.SHEET_SPECIAL_EFFECTS, 3, 0);
			Draw.drawSprite(spr, position);
		}
	}
}
