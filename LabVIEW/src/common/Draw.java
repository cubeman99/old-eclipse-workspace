package common;

import java.awt.Color;
import java.awt.Graphics;
import simulator.IOPoint;

public class Draw {
	private static Graphics graphics;
	
	
	public static void setGraphics(Graphics g) {
		graphics = g;
	}
	
	public static Graphics getGraphics() {
		return graphics;
	}
	
	
	public static void draw(IOPoint iop) {
		Point anchor = new Point(iop.absX(), iop.absY());
		
		int l = 6;
		Point p = new Point();
		if (iop.horizontal)
			p.x = l * (int) GMath.sign(iop.offset.x - (iop.gateLink.size.x / 2));
		else
			p.y = l * (int) GMath.sign(iop.offset.y - (iop.gateLink.size.y / 2));
		
		graphics.setColor(Color.BLACK);
		graphics.drawLine(anchor.x, anchor.y, anchor.x + p.x, anchor.y + p.y);
		
		graphics.setColor(iop.type.getColor());
		int r = 4;
		graphics.fillOval(iop.absX() - r, iop.absY() - r, 2 * r - 1, 2 * r - 1);
		r = 2;
		graphics.setColor(Color.WHITE);
		graphics.fillOval(iop.absX() - r, iop.absY() - r, 2 * r - 1, 2 * r - 1);
	}
}
