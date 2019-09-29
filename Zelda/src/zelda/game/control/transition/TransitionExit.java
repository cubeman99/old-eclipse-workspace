package zelda.game.control.transition;

import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.game.control.GameInstance;
import zelda.game.world.Frame;


public class TransitionExit extends TransitionTunnel {
	private int exitTimer;
	private boolean exited;


	public TransitionExit(GameInstance game, Frame nextFrame, Vector warpPos) {
		super(game, nextFrame, warpPos);
		exitTimer = 0;
		exited = false;
	}


	@Override
	public void update() {
		if (!exited) {
			game.getPlayer().getPosition().add(0, 1);
			if (exitTimer++ > 16) {
				exited = true;
				game.getPlayer().setPosition(warpPosition);
			}
		}
		else {
			super.update();
		}
	}

	@Override
	public void draw() {
		if (!exited) {
			Draw.setViewPosition(0, -16);
			frameOld.draw();
			game.getPlayer().draw();
		}
		else {
			super.draw();
		}
	}
}
