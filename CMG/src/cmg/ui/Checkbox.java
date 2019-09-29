package cmg.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import cmg.graphics.Draw;
import cmg.math.geometry.Point;

public class Checkbox extends Component {
	private String text;
	private boolean checked;
	
	public Checkbox(boolean checked, String text) {
		super();
		this.text    = text;
		this.checked = checked;
		rect.size.set(20, 20);
	}
	
	@Override
	public void step() {
		super.step();
		if (mouseOver() && Mouse.current.left.released()) {
			checked = !checked;
		}
	}
	
	@Override
	public void render() {
		boolean over    = mouseOver();
		boolean pressed = (over && Mouse.current.left.down());
		Color c1 = (mouseOver() ? new Color(54, 191, 254) : new Color(15, 153, 217));
		Color c2 = (mouseOver() ? new Color(2, 152, 222)  : new Color(1, 119, 175));
		Color c3 = (mouseOver() ? new Color(64, 194, 254)  : new Color(15, 153, 217, 128));
		
		// Draw box.
		UIDraw.setGraphics(Draw.getGraphics());
		Color outlineColor = new Color(255, 255, 255, (mouseOver() && !pressed) ? 70 : 40);
		Color textColor    = (over ? (pressed ? new Color(255, 255, 255, 128) : Color.WHITE) : c1);
		UIDraw.drawUIBox(rect, "", pressed, new Color(25, 29, 37), outlineColor, c1);
		
		// Draw check.
		if (checked)
			UIDraw.drawCheck(rect.getCenter().x + (pressed ? 1 : 0), rect.getCenter().y + (pressed ? 1 : 0), c1);
		
		// Draw label.
		UIDraw.setColor(new Color(255, 255, 255, 180));
		UIDraw.drawString(text, rect.getX2() + 10, rect.getCenter().y - 2, Draw.LEFT, Draw.MIDDLE);
	}
}
