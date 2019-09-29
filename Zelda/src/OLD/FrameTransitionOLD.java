package OLD;

import java.awt.Graphics;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.game.control.GameInstance;
import zelda.game.world.Frame;
import zelda.game.world.Level;


public class FrameTransitionOLD {
	private static final double TRANSITION_SPEED = 4;
	private static final int TRANSITION_DELAY = 8;
	private GameInstance game;
	private Point relativePos;
	private Vector viewPosOld;
	private Vector viewPosNew;
	private Frame frameOld;
	private Frame frameNew;
	private Vector maxViewPos;
	private Vector viewOffset;
	private Vector viewGoal;
	private boolean done;
	private int delayTimer;



	public FrameTransitionOLD(GameInstance game, Point relativeFramePos) {
		this.game = game;
		this.relativePos = relativeFramePos;

		Level level = game.getLevel();
		frameOld = level.getCurrentFrame();
		frameNew = level.getFrame(frameOld.getLocation().plus(relativePos));

//		viewPosOld = game.getViewPosition();
		viewPosNew = new Vector(relativePos).times(new Vector(
				Settings.VIEW_SIZE));
		maxViewPos = new Vector(level.getFrameSize().times(16, 16)
				.minus(Settings.VIEW_SIZE));
		viewOffset = new Vector();
		viewPosNew = maxViewPos.minus(viewPosOld);
		viewGoal = new Vector(relativeFramePos.times(Settings.VIEW_SIZE));
		done = false;
		delayTimer = 0;

		if (relativeFramePos.equals(1, 0))
			viewPosNew = new Vector(0, viewPosOld.y);
		else if (relativeFramePos.equals(0, -1))
			viewPosNew = new Vector(viewPosOld.x, maxViewPos.y);
		else if (relativeFramePos.equals(-1, 0))
			viewPosNew = new Vector(maxViewPos.x, viewPosOld.y);
		else if (relativeFramePos.equals(0, 1))
			viewPosNew = new Vector(viewPosOld.x, 0);

		frameNew.enter();
	}

	public boolean isDone() {
		return done;
	}

	public void draw(Graphics g) {
		if (delayTimer++ > TRANSITION_DELAY) {
			Vector add = new Vector(relativePos).scale(TRANSITION_SPEED);
			viewOffset.add(add);
			game.getPlayer()
					.getPosition()
					.add(new Vector(relativePos)
							.scaledBy((14 * TRANSITION_SPEED)
									/ viewGoal.length()));
			game.getPlayer().itemBracelet.updateHoldObject();
			game.getPlayer().itemBombs.updateHoldObject();
		}

		if (viewOffset.distanceTo(viewGoal) < TRANSITION_SPEED) {
			done = true;
			game.getLevel().nextFrame(relativePos);

			game.getPlayer()
					.getPosition()
					.sub(new Vector(relativePos.times(game.getLevel()
							.getFrameSize().times(16, 16))));
			game.getPlayer().recordFrameEnterPosition();
			game.getPlayer().itemBracelet.updateHoldObject();
			game.getPlayer().itemBombs.updateHoldObject();
//			game.focusViewOnPlayer();

			frameOld.leave();
		}
		else {
			// Draw new frame.
			Vector v = viewPosNew.minus(viewGoal).plus(viewOffset).minus(0, 16);
			Draw.setViewPosition(v);
			frameNew.draw();

			// Draw old frame.
			Draw.setViewPosition(viewPosOld.plus(viewOffset).minus(0, 16));
			frameOld.draw();

			game.getPlayer().draw();
		}
	}
}
