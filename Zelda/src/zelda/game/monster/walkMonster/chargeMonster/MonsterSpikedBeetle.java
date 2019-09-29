package zelda.game.monster.walkMonster.chargeMonster;

import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.reactions.ReactionCause;
import zelda.common.reactions.ReactionEffect;
import zelda.common.util.GMath;
import zelda.game.entity.Entity;
import zelda.game.player.Player;


public class MonsterSpikedBeetle extends ChargeMonster {
	private static final int VULNERABLE_TIME = 180;
	private static final int SHAKE_DELAY = 120;

	private boolean vulnerable;
	private int timer;
	private double hitDirection;
	private DynamicAnimation animationNormal;
	private DynamicAnimation animationVulnerable;
	private int[] shakeOffsets;


	public MonsterSpikedBeetle() {
		super();

		health.fill(1);
		contactDamage = 2;
		moveSpeed = 0.375;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMax = 40;
		moveTimeMin = 80;
		chargeSpeed = 1.25;
		chargeAcceleration = 0.04;

		vulnerable = false;
		timer = 0;

		shakeOffsets = new int[] {0, -1, -2, -1, 0, 1, 2, 1};

		setReaction(ReactionCause.SHIELD, new SHIELD_HIT());
		setReaction(ReactionCause.SHOVEL, new SHOVEL_HIT());

		setReaction(ReactionCause.FIRE, new SHELLED(new BURN(1)));
		setReaction(ReactionCause.EMBER_SEED, new SHELLED(new BURN(1)));
		setReaction(ReactionCause.SCENT_SEED, new SHELLED(new SEED_REACTION(1,
				new DAMAGE(1))));
		setReaction(ReactionCause.MYSTERY_SEED, new SHELLED(new MYSTERY_SEED()));
		setReaction(ReactionCause.ROD_FIRE, new SHELLED(new BURN(1)));
		setReaction(ReactionCause.ARROW, new SHELLED(new DAMAGE(1)));
		setReaction(ReactionCause.SWORD_BEAM, new SHELLED(new DAMAGE(1)));
		setReaction(ReactionCause.SWORD, new SHELLED(new DAMAGE(1, 2, 3)));
		setReaction(ReactionCause.BIGGORON_SWORD, new SHELLED(new DAMAGE(3)));
		setReaction(ReactionCause.BOOMERANG, new SHELLED(new STUN()));
		setReaction(ReactionCause.BOMB, new SHELLED(new DAMAGE(1)));
		setReaction(ReactionCause.SWITCH_HOOK, new SHELLED(new DAMAGE(1)));


		animationNormal = new DynamicAnimation(new Animation().addFrame(6, 6,
				18).addFrame(6, 7, 18));
		animationVulnerable = new DynamicAnimation(new Animation().addFrame(3,
				8, 18).addFrame(3, 9, 18));

		standAnimation = animationNormal;
		moveAnimation = animationNormal;
		sprite.newAnimation(animationNormal);
	}

	@Override
	public MonsterSpikedBeetle clone() {
		return new MonsterSpikedBeetle();
	}

	private void hit() {
		vulnerable = true;
		bounces = true;
		charging = false;
		timer = 0;
		hitDirection = GMath
				.direction(game.getPlayer().getPosition(), position);
		zVelocity = 1.5;
		standAnimation = animationVulnerable;
		moveAnimation = animationVulnerable;
		sprite.newAnimation(animationVulnerable);
		velocity.setPolar(1, hitDirection);
	}

	@Override
	public void updateMotion() {
		if (onGround()) {
			if (vulnerable) {
				timer++;
				if (timer > VULNERABLE_TIME) {
					timer = 0;
					vulnerable = false;
					standAnimation = animationNormal;
					moveAnimation = animationNormal;
					sprite.newAnimation(animationNormal);
					zVelocity = 1.5;
					velocity.setPolar(-1, hitDirection);
					bounces = false;
				}
			}
			else
				super.updateMotion();
		}
	}

	@Override
	protected int getDrawOffset() {
		if (timer > SHAKE_DELAY)
			return (shakeOffsets[timer % shakeOffsets.length] + super
					.getDrawOffset());
		return super.getDrawOffset();
	}

	protected final class SHELLED implements ReactionEffect {
		private ReactionEffect reaction;

		public SHELLED(ReactionEffect reaction) {
			this.reaction = reaction;
		}

		@Override
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			if (vulnerable) {
				reaction.trigger(reactionCuase, level, source, sourcePos);
			}
			else {
				// TODO: cling
				game.getPlayer().bump(getCenter());
			}
		}
	}

	protected final class SHOVEL_HIT implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			if (!vulnerable && onGround())
				hit();
		}
	}

	protected final class SHIELD_HIT implements ReactionEffect {
		public void trigger(int reactionCuase, int level, Entity source,
				Vector sourcePos) {
			Player player = game.getPlayer();
			if (!vulnerable && onGround())
				hit();
			else if (onGround())
				bump(player.getCenter());
			player.bump(getCenter());
		}
	}
}
