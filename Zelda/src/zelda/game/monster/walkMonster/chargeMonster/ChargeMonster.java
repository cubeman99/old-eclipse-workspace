package zelda.game.monster.walkMonster.chargeMonster;

import zelda.common.geometry.Vector;
import zelda.common.util.GMath;
import zelda.game.monster.walkMonster.WalkMonster;


public abstract class ChargeMonster extends WalkMonster {
	protected double chargeSpeed;
	protected double chargeAcceleration;
	protected boolean charging;
	protected int chargeInLineLeeway;



	public ChargeMonster() {
		this(TYPE_NONE, TYPE_NONE);
	}

	public ChargeMonster(int typeColor, int typeItem) {
		super(typeColor, typeItem);

		chargeSpeed = 1.25;
		chargeAcceleration = 0.04;
		moveSpeed = 0.375;
		stopTimeMin = 0;
		stopTimeMax = 0;
		moveTimeMin = 50;
		moveTimeMax = 80;
		charging = false;
		chargeInLineLeeway = 8;
	}

	protected void charge() {
		int dir = (int) (GMath.direction(getCenter(), game.getPlayer()
				.getCenter())
				/ GMath.HALF_PI + 0.5) % 4;
		startMoving(dir);
		charging = true;
	}

	protected boolean inLineWithPlayer() {
		return (Math.abs(position.y - game.getPlayer().getPosition().y) < chargeInLineLeeway || Math
				.abs(position.x - game.getPlayer().getPosition().x) < chargeInLineLeeway);
	}

	protected void checkForCharge() {
		if (inLineWithPlayer())
			charge();
	}

	protected void updateCharge() {
		speed = Math.min(chargeSpeed, speed + chargeAcceleration);
		motionDuration = 1;
		motionTimer = 0;
	}

	@Override
	protected void startMoving(int moveDir) {
		super.startMoving(moveDir);
		charging = false;
	}

	@Override
	protected void endPause() {
		super.endPause();
		charging = false;
	}

	@Override
	public boolean bump(Vector sourcePos) {
		charging = false;
		return super.bump(sourcePos);
	}

	@Override
	public void updateMotion() {
		if (charging) {
			updateCharge();
		}
		else {
			checkForCharge();
		}
		
		super.updateMotion();
	}
}
