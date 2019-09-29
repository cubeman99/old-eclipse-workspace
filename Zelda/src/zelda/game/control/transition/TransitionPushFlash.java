package zelda.game.control.transition;

import java.awt.Color;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.game.control.GameInstance;


public class TransitionPushFlash extends FrameTransition {
	private double opacity;
	private boolean fading;
	private Point relativePos;

	public TransitionPushFlash(GameInstance game, Point relativeFramePos) {
		super(game);

		fading = false;
		opacity = 0.0;
		relativePos = new Point(relativeFramePos);

		frameNew = game.getLevel().getFrame(
				frameOld.getLocation().plus(relativeFramePos));
	}

	@Override
	public void update() {
		if (!fading) {
			opacity += 0.038;

			if (opacity >= 1.0) {
				fading = true;
				opacity = 1.0;
				game.getPlayer().getPosition()
						.add(new Vector(relativePos).scaledBy(16));
				game.getPlayer()
						.getPosition()
						.sub(new Vector(relativePos.times(game.getLevel()
								.getFrameSize().times(16, 16))));
			}
		}
		else {
			opacity -= 0.038;

			if (opacity < 0) {
				opacity = 0;
				end();
			}
		}
	}

	@Override
	public void draw() {
		Draw.setViewPosition(0, -16);

		if (fading) {
			// Draw new frame.
			Draw.setViewPosition(0, -16);
			frameNew.draw();
		}
		else {
			// Draw old frame.
			Draw.setViewPosition(0, -16);
			frameOld.draw();
		}

		game.getPlayer().draw();

		Draw.setViewPosition(0, 0);
		Draw.setColor(new Color(1, 1, 1, (float) opacity));
		Draw.fillRect(new Rectangle(new Point(0, 0), Settings.VIEW_SIZE.plus(0,
				16)));
	}
}
