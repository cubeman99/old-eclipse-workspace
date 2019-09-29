package zelda.game.monster.jumpMonster;

import zelda.common.graphics.Animation;
import zelda.common.util.GMath;
import zelda.game.monster.BasicMonster;


public class JumpMonster extends BasicMonster {
	protected double jumpVelocityMin;
	protected double jumpVelocityMax;
	protected Animation animationIdle;
	protected Animation animationJump;
	protected boolean jumping;



	public JumpMonster() {
		this(TYPE_NONE);
	}

	public JumpMonster(int typeColor) {
		super(typeColor, TYPE_NONE);

		jumping = false;
		animationIdle = new Animation(0, 0);
		animationJump = null;
		jumpVelocityMin = 2;
		jumpVelocityMax = 2;

		motionTimer = 0;
		motionDuration = 0;
		stopTimeMin = 50;
		stopTimeMax = 80;

		bounces = false;
	}

	protected void onJump() {
		velocity.setPolar(moveSpeed,
				GMath.direction(getCenter(), game.getPlayer().getCenter()));
	}

	protected void endJump() {
		velocity.zero();
		jumping = false;
		motionTimer = 0;
		motionDuration = stopTimeMin;
		sprite.newAnimation(animationIdle);
		if (stopTimeMax - stopTimeMin > 0)
			motionDuration += GMath.random.nextInt(stopTimeMax - stopTimeMin);
	}

	protected void jump() {
		if (onGround() && !jumping)
			jumpOverride();
	}

	protected void jumpOverride() {
		zVelocity = jumpVelocityMin;
		if (jumpVelocityMax - jumpVelocityMin > 0)
			zVelocity += GMath.random.nextDouble()
					* (jumpVelocityMax - jumpVelocityMin);
		jumping = true;
		onJump();
		if (animationJump != null)
			sprite.newAnimation(animationJump);
	}

	@Override
	public void updateMotion() {
		if (jumping) {
			if (onGround())
				endJump();
		}
		else {
			motionTimer++;
			if (motionTimer > motionDuration)
				jump();
		}
	}
}
