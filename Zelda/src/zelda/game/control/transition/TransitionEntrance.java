package zelda.game.control.transition;

import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.game.control.GameInstance;
import zelda.game.world.Frame;


public class TransitionEntrance extends TransitionTunnel {
	private static final int WALK_DELAY_TIME = 14;
	private int enterTimer;
	private boolean entered;
	private int walkDelayTimer;



	public TransitionEntrance(GameInstance game, Frame nextFrame, Vector warpPos) {
		super(game, nextFrame, warpPos);
		enterTimer = 0;
		entered = false;
		walkDelayTimer = 0;
		game.getPlayer().setPosition(warpPosition);
	}

	// @Override
	// protected void end() {
	// if (!entered) {
	// entered = true;
	// }
	// else {
	// super.e();
	// game.getPlayer().setOnWarpPoint(true);
	// }
	// }

	@Override
	public void update() {
		if (entered) {
			game.getPlayer().getPosition().add(0, -1);
			if (enterTimer++ > 14) {
				entered = true;
			}
		}
		else {
			super.update();
		}
	}

	@Override
	public void draw() {
		if (entered) {
			Draw.setViewPosition(0, -16);
			frameOld.draw();
			game.getPlayer().draw();
		}
		else {
			super.draw();
		}
	}
}
