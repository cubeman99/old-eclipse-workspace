package zelda.game.monster.walkMonster.chargeMonster;

import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.DynamicAnimation;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.Direction;
import zelda.common.util.GMath;


public class MonsterLynel extends ChargeMonster {

	public MonsterLynel(int typeColor) {
		super(typeColor, ITEM_SPEAR);

		health.fill(4);
		contactDamage = 4;

		moveSpeed = 0.5;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMax = 40;
		moveTimeMin = 80;
		chargeSpeed = 1.25;
		chargeAcceleration = 0.1;
		chargeInLineLeeway = 5;

		shoots = false;
		projectileShootOdds = 10;
		aimProjectiles = true;


		setReaction(ReactionCause.SWORD, new DAMAGE(1, 2, 2));
		setReaction(ReactionCause.BIGGORON_SWORD, new DAMAGE(2));
		setReaction(ReactionCause.SWITCH_HOOK, new EFFECT_CLING());
		setReaction(ReactionCause.GALE_SEED, null);
		// setReaction(ReactionCause.SCENT_SEED, new DAMAGE(1)); // TODO: dont
		// pause when hit.

		int y = 9;
		if (typeColor == COLOR_BLUE) {
			y = 11;
			health.fill(6);
			aimProjectiles = true;
		}
		else if (typeColor == COLOR_GOLD) {
			y = 13;
			health.fill(40);
			projectileShootOdds = 6;
		}


		standAnimation = new DynamicAnimation(new Animation().addFrame(
				new AnimationFrame(6).addPart(5, y, -8, 0).addPart(6, y, 8, 0))
				.addFrame(
						new AnimationFrame(6).addPart(5, y, -8, 0).addPart(6,
								y, 8, 0)), new Animation().addFrame(6, 9, y)
				.addFrame(6, 9, y), new Animation().addFrame(
				new AnimationFrame(6).addPart(5, y + 1, -8, 0).addPart(6,
						y + 1, 8, 0)).addFrame(
				new AnimationFrame(6).addPart(5, y + 1, -8, 0).addPart(6,
						y + 1, 8, 0)), new Animation().addFrame(6, 9, y + 1)
				.addFrame(6, 9, y + 1));

		moveAnimation = new DynamicAnimation(new Animation().addFrame(
				new AnimationFrame(6).addPart(5, y, -8, 0).addPart(6, y, 8, 0))
				.addFrame(
						new AnimationFrame(6).addPart(7, y, -8, 0).addPart(8,
								y, 8, 0)), new Animation().addFrame(6, 9, y)
				.addFrame(6, 10, y), new Animation().addFrame(
				new AnimationFrame(6).addPart(5, y + 1, -8, 0).addPart(6,
						y + 1, 8, 0)).addFrame(
				new AnimationFrame(6).addPart(7, y + 1, -8, 0).addPart(8,
						y + 1, 8, 0)), new Animation().addFrame(6, 9, y + 1)
				.addFrame(6, 10, y + 1));

		sprite.newAnimation(standAnimation);
		sprite.setVariation(Direction.DOWN);
	}

	@Override
	public MonsterLynel clone() {
		return new MonsterLynel(typeColor);
	}

	@Override
	protected void checkForCharge() {
		if (motionTimer < moveTimeMin / 2)
			return;

		if (inLineWithPlayer()) {
			if (GMath.random.nextInt(3) == 0) {
				charge();
			}
			else if (GMath.random.nextInt(5) == 0) {
				facePlayer();
				shoot();
			}
		}
	}

	@Override
	protected void updateCharge() {
		speed = Math.min(chargeSpeed, speed + chargeAcceleration);

		if (inLineWithPlayer()) {
			if (GMath.random.nextInt(20) == 0) {
				charge();
			}
		}
	}

	@Override
	protected void startMoving(int moveDir) {
		boolean chargingPrev = charging;
		double speedPrev = speed;

		super.startMoving(moveDir);

		if (chargingPrev && GMath.random.nextInt(3) == 0) {
			charging = true;
			speed = speedPrev;
		}
		else {
			if (GMath.random.nextInt(projectileShootOdds) == 0) {
				facePlayer();
				shoot();
			}
		}
	}
}
