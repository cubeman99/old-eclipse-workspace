package zelda.game.control.event;

import java.awt.Color;
import zelda.common.Settings;
import zelda.common.geometry.Rectangle;
import zelda.common.graphics.Draw;

public class EventScreenSplit extends Event {
	private static final int SPLIT_DELAY = 6;
	private int[] sideWidths;
	private int timer;
	
	@Override
	public void begin() {
		timer = 0;
		sideWidths = new int[]
				{Settings.VIEW_SIZE.x / 2,
				 Settings.VIEW_SIZE.x / 2};
	}
	
	@Override
	public void update() {
		if (timer++ >= SPLIT_DELAY) {
			sideWidths[(timer + 1) % 2] -= 8;
			if (sideWidths[0] <= 0 && sideWidths[1] <= 0)
				end();
		}
	}
	
	@Override
	public void draw() {
		Draw.setViewPosition(0, -16);
		Draw.setColor(new Color(248, 208, 136));
		Draw.fillRect(new Rectangle(0, 0, sideWidths[0],
				Settings.VIEW_SIZE.y));
		Draw.fillRect(new Rectangle(Settings.VIEW_SIZE.x - sideWidths[1],
				0, sideWidths[1], Settings.VIEW_SIZE.y));
	}
}
