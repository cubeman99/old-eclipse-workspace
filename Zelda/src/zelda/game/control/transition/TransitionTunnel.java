package zelda.game.control.transition;

import java.awt.Color;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.game.control.GameInstance;
import zelda.game.world.Frame;


public class TransitionTunnel extends FrameTransition {
	private int timer;
	private int[] sideWidths;
	protected Vector warpPosition;
	
	public TransitionTunnel(GameInstance game, Frame nextFrame, Vector warpPos) {
		super(game);

		this.warpPosition = new Vector(warpPos);

		timer = 0;
		sideWidths = new int[] {Settings.VIEW_SIZE.x / 2,
				Settings.VIEW_SIZE.x / 2};

		this.frameNew = nextFrame;
		
		game.getView().centerAt(game.getPlayer());
	}

	@Override
	public void update() {
		timer++;

		if (timer > 6) {
			sideWidths[(timer + 1) % 2] -= 8;
			
			if (sideWidths[0] <= 0 && sideWidths[1] <= 0) {
				end();
			}
		}
	}

	@Override
	public void draw() {
		Draw.setViewPosition(game.getView().getPosition().minus(0, 16));
		frameNew.draw();
		game.getPlayer().draw();

		Draw.setViewPosition(0, 0);
		game.getHud().draw();
		Draw.setViewPosition(0, -16);
		Draw.setColor(new Color(248, 208, 136));
		Draw.fillRect(new Rectangle(new Point(0, 0), new Point(sideWidths[0],
				Settings.VIEW_SIZE.y)));
		Draw.fillRect(new Rectangle(new Point(Settings.VIEW_SIZE.x
				- sideWidths[1], 0), new Point(sideWidths[1],
				Settings.VIEW_SIZE.y)));
	}
}
