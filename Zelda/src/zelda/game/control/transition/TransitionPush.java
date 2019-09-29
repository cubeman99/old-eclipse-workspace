package zelda.game.control.transition;

import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.common.util.Direction;
import zelda.game.control.GameInstance;
import zelda.game.world.Level;


public class TransitionPush extends FrameTransition {
	private static final double TRANSITION_SPEED = 4;
	private static final int TRANSITION_DELAY = 8;
	private Point relativePos;
	private Vector viewPosOld;
	private Vector viewPosNew;
	private Vector maxViewPos;
	private Vector viewOffset;
	private Vector viewGoal;
	private int delayTimer;



	public TransitionPush(GameInstance game, Point relativeFramePos) {
		super(game);

		Level level = game.getLevel();
		relativePos = new Point(relativeFramePos);
		viewPosOld = game.getView().getPosition();
		viewPosNew = new Vector(relativeFramePos).times(new Vector(
				Settings.VIEW_SIZE));
		maxViewPos = new Vector(level.getFrameSize().times(16, 16)
				.minus(Settings.VIEW_SIZE));
		viewOffset = new Vector();
		viewPosNew = maxViewPos.minus(viewPosOld);
		viewGoal = new Vector(relativeFramePos.times(Settings.VIEW_SIZE));
		delayTimer = 0;

		if (relativeFramePos.equals(1, 0))
			viewPosNew = new Vector(0, viewPosOld.y);
		else if (relativeFramePos.equals(0, -1))
			viewPosNew = new Vector(viewPosOld.x, maxViewPos.y);
		else if (relativeFramePos.equals(-1, 0))
			viewPosNew = new Vector(maxViewPos.x, viewPosOld.y);
		else if (relativeFramePos.equals(0, 1))
			viewPosNew = new Vector(viewPosOld.x, 0);

		frameNew = game.getLevel().getFrame(
				frameOld.getLocation().plus(relativeFramePos));
	}
	
	private Vector getNewPlayerPosition() {
		return game.getPlayer().getPosition().plus(
				new Vector(relativePos).scaledBy(16))
				.minus(new Vector(
				relativePos.times(game.getLevel()
				.getFrameSize().times(16, 16))));
	}
	
	@Override
	public void begin() {
		super.begin(getNewPlayerPosition());
	}

	@Override
	public void update() {
		delayTimer++;
		if (delayTimer > TRANSITION_DELAY) {
			Vector add = new Vector(relativePos).scale(TRANSITION_SPEED);
			viewOffset.add(add);
			game.getPlayer().getPosition().add(new Vector(relativePos)
					.scaledBy((14 * TRANSITION_SPEED) / viewGoal.length()));
			game.getPlayer().itemBracelet.updateHoldObject();
			game.getPlayer().itemBombs.updateHoldObject();
		}
		
		if (viewOffset.distanceTo(viewGoal) < TRANSITION_SPEED) {
			game.getPlayer().getPosition().sub(new Vector(
					relativePos.times(game.getLevel()
					.getFrameSize().times(16, 16))));
			end();
		}
	}

	@Override
	public void preDraw() {

	}

	@Override
	public void draw() {
		// Draw new frame.
		Vector v = viewPosNew.minus(viewGoal).plus(viewOffset).minus(0, 16);
		Draw.setViewPosition(v);
		frameNew.draw();

		// Draw old frame.
		Draw.setViewPosition(viewPosOld.plus(viewOffset).minus(0, 16));
		frameOld.draw();

		game.getPlayer().draw();

		game.getHud().draw();
	}
}
