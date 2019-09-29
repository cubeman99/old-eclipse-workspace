package zelda.game.monster.jumpMonster;

import zelda.common.Settings;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Animation;
import zelda.common.reactions.ReactionCause;
import zelda.common.util.GMath;
import zelda.game.entity.CollisionBox;


public class MonsterGel extends JumpMonster {
	private boolean attatched;
	private int attatchTimer;


	public MonsterGel() {
		super();

		health.fill(1);
		contactDamage = 0;

		softCollisionBox = new CollisionBox(4, 4, 8, 8);
		hardCollisionBox = new CollisionBox(4, 4, 8, 8);

		moveSpeed = 1;
		motionDuration = 80 + GMath.random.nextInt(40);
		stopTimeMin = 80;
		stopTimeMax = 120;
		jumpVelocityMin = 2;
		jumpVelocityMax = 2;
		gravity = Settings.GRAVITY;

		attatched = false;
		attatchTimer = 0;

		animationIdle = new Animation().addFrame(8, 13, 14).addFrame(8, 14, 14);
		animationJump = new Animation(13, 14);
		sprite.newAnimation(animationIdle);


		setReaction(ReactionCause.SWITCH_HOOK, new DAMAGE(1));
		setReaction(ReactionCause.BOOMERANG, new DAMAGE(1));
	}

	private void detatch() {
		attatched = false;
		passable = false;
		flying = false;
		Vector vel = game.getPlayer().getVelocity();
		jumpOverride();
		game.getPlayer().setItemBusy(false);

		if (vel.length() > GMath.EPSILON)
			velocity.set(vel.lengthVector(moveSpeed / 2).inverse());
		else
			velocity.setPolar(moveSpeed * 0.75, GMath.random.nextDouble()
					* GMath.TWO_PI);
	}

	private void attatch() {
		attatchTimer = 0;
		attatched = true;
		passable = true;
		flying = true;
		game.getPlayer().getInventory().interruptItems();
		game.getPlayer().setItemBusy(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (attatched) {
			game.getPlayer().setItemBusy(false);
		}
	}

	@Override
	protected void hitPlayer() {
		if (!jumping && !attatched)
			attatch();
	}

	@Override
	public MonsterGel clone() {
		return new MonsterGel();
	}

	@Override
	public void updateMotion() {
		if (attatched) {
			passable = true;
			attatchTimer++;
			game.getPlayer().setItemBusy(true);

			if (attatchTimer > 120) {
				detatch();
			}
		}
		else {
			super.updateMotion();
		}
	}

	@Override
	protected int getDrawOffset() {
		int offset = super.getDrawOffset();
		if (attatched) {
			if ((attatchTimer / 2) % 4 == 0)
				offset += 2;
			else
				offset -= 1;
		}
		return offset;
	}

	@Override
	public void draw() {
		if (!attatched)
			super.draw();
	}

	@Override
	public void postDraw() {
		if (attatched) {
			position.set(game.getPlayer().getPosition());

			if (attatchTimer % 5 > 0)
				super.draw();
		}
	}
}
