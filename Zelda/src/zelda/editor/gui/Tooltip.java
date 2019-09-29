package zelda.editor.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import zelda.common.geometry.Point;
import zelda.main.Mouse;


public class Tooltip {
	private String text;
	private Point position;
	private int timer;
	private double opacity;
	
	public Tooltip() {
		text     = "";
		position = new Point();
		timer    = 0;
		opacity  = 0.0;
	}
	
	public void set(String tooltipText, int x, int y) {
		timer = 0;
//		position.set(x, y);
		if (!text.equals(tooltipText)) {
			text = tooltipText;
			position.set(Mouse.x(), Mouse.y() + 20);
		}
	}
	
	public void update() {
		if (timer++ > 8) {
			opacity -= 0.08;
			if (opacity <= 0) {
				opacity = 0;
				text    = "";
			}
		}
		else {
			opacity = Math.min(1, opacity + 0.08);
		}
	}
	
	public void draw(Graphics2D g) {
//		Graphics2D g = Draw.getGraphics();

		if (!text.isEmpty()) {
			int dx = position.x;
			int dy = position.y;
			
			java.awt.Rectangle r = g.getFontMetrics().getStringBounds(text, g).getBounds();
			r.grow(4, 2);
			r.setLocation(dx, dy);
			
			g.setColor(new Color(255, 255, 220, (int) (opacity * 255)));
			g.fill(r);
			g.setColor(new Color(0, 0, 0, (int) (opacity * 255)));
			g.draw(r);
			
			g.drawString(text, r.x + 4, r.y + r.height - 6);
		}
	}
}
