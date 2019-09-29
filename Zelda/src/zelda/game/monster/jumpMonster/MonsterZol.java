package zelda.game.monster.jumpMonster;

import zelda.common.graphics.Animation;


public class MonsterZol extends JumpMonster {
	private int jumpIndex;
	private int numJumps;
	private boolean burrowing;
	private boolean burrowed;
	private Animation animationBurrow;
	private Animation animationUnburrow;



	public MonsterZol(int typeColor) {
		super(typeColor);

		health.fill(1);
		contactDamage = 2;

		moveSpeed = 0.75;
		stopTimeMin = 48;
		stopTimeMax = 48;
		jumpVelocityMin = 1.5;
		jumpVelocityMax = 1.5;
		gravity = 0.08;
		numJumps = 4;

		burrowed = true;
		passable = true;
		burrowing = false;
		jumpIndex = 0;

		int y = (typeColor == COLOR_GREEN ? 13 : 14);
		animationBurrow = new Animation().addFrame(8, 11, y)
				.addFrame(16, 12, y).addFrame(16, 13, y);
		animationUnburrow = new Animation().addFrame(16, 13, y).addFrame(16,
				12, y);

		animationIdle = new Animation(11, y);
		animationJump = new Animation(12, y);
		sprite.newAnimation(animationIdle);
	}

	private void burrow() {
		burrowed = true;
		burrowing = true;
		passable = true;
		sprite.newAnimation(false, animationBurrow);
	}

	private void unburrow() {
		burrowed = false;
		burrowing = true;
		passable = true;
		jumpIndex = 0;
		sprite.newAnimation(false, animationUnburrow);
	}

	@Override
	public MonsterZol clone() {
		return new MonsterZol(typeColor);
	}

	@Override
	public void updateMotion() {
		if (burrowing) {
			passable = true;

			if (sprite.isAnimationDone()) {
				burrowing = false;
				sprite.newAnimation(animationIdle);

				if (!burrowed) {
					jump();
					velocity.zero();
					sprite.newAnimation(animationIdle);
				}
			}
		}
		else if (burrowed) {
			passable = true;
			if (getDistanceToPlayer() < 48)
				unburrow();
		}
		else {
			passable = false;
			super.updateMotion();
		}
	}

	@Override
	public void die() {
		super.die();

		if (typeColor == COLOR_RED) {
			// Make 2 gels
			for (int i = 0; i < 2; i++) {
				MonsterGel m = new MonsterGel();
				m.setFrame(frame);
				m.setPosition(position);
				game.addEntity(m);
			}
		}
	}

	@Override
	protected void onJump() {
		super.onJump();
		jumpIndex++;
	}

	@Override
	protected void endJump() {
		super.endJump();
		if (jumpIndex > numJumps) {
			burrow();
		}
	}

	@Override
	public void draw() {
		if (burrowing || !burrowed)
			super.draw();
	}
}
