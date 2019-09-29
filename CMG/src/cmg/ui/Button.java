package cmg.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import cmg.graphics.Draw;
import cmg.math.geometry.Point;

public class Button extends Component {
	private String text;
	private int type;
	
	public Button(String text, int type) {
		super();
		this.text = text;
		this.type = type;
	}
	
	@Override
	public void render() {
		Draw.setColor(Color.RED);
		
		Graphics2D g = Draw.getGraphics();
		boolean over    = mouseOver();
		boolean pressed = (over && Mouse.current.left.down());
		Color c1 = (mouseOver() ? new Color(54, 191, 254) : new Color(15, 153, 217));
		Color c2 = (mouseOver() ? new Color(2, 152, 222)  : new Color(1, 119, 175));
		Color c3 = (mouseOver() ? new Color(64, 194, 254)  : new Color(15, 153, 217, 128));

		UIDraw.setGraphics(g);
		if (type == 0) {
			Color outlineColor = new Color(255, 255, 255, (mouseOver() && !pressed) ? 70 : 40);
			Color textColor    = (over ? (pressed ? new Color(255, 255, 255, 128) : Color.WHITE) : c1);
			UIDraw.drawUIBox(rect, text, pressed, new Color(25, 29, 37), outlineColor, textColor);
		}
		else if (type == 1) {
			UIDraw.drawUIBox(rect, text, pressed, c1, UIDraw.TRANSPARENT, Color.WHITE);
		}
	}
}
