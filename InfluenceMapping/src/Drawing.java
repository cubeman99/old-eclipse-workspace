

import java.awt.Color;
import java.awt.Graphics;

public class Drawing {
	public Drawing() {
	}
	
	
	public static void drawHealthbar(Graphics g, Rect r, double amount, Color minColor, Color maxColor, Color backColor) {
		Color col = minColor;
		int span = (int) (amount * r.width);
		
		g.setColor(backColor);
		g.fillRect((int) r.x, (int) r.y, (int) r.width, (int) r.height);
		g.setColor(col);
		g.fillRect((int) r.x, (int) r.y, span, (int) r.height);
	}
}
