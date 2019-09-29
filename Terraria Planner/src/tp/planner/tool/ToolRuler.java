package tp.planner.tool;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import tp.common.Line;
import tp.common.Point;
import tp.common.Rectangle;
import tp.common.graphics.Draw;
import tp.main.Keyboard;
import tp.main.Mouse;

public class ToolRuler extends Tool {
	private Point dragPoint;
	private boolean dragging;
	private Rectangle measureBox;
	
	
	public ToolRuler() {
		super("Ruler Tool", "t", KeyEvent.VK_T);
		this.dragPoint  = null;
		this.dragging   = false;
		this.measureBox = null;
	}
	
	private void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
		Point v = control.getViewPosition();
		g.drawLine(x1 - v.x, y1 - v.y, x2 - v.x, y2 - v.y);
	}

	@Override
	public void update() {
		Point ms = control.getCursorPoint();
		
		if (dragging) {
			measureBox = new Rectangle(Math.min(dragPoint.x , ms.x), Math.min(dragPoint.y,  ms.y),
					Math.abs(dragPoint.x - ms.x), Math.abs(dragPoint.y - ms.y));

			// Hold shift to make a striaght line:
			if (Keyboard.shift.down()) {
				if (Math.abs(ms.x - dragPoint.x) > Math.abs(ms.y - dragPoint.y)) {
					measureBox = new Rectangle(Math.min(dragPoint.x , ms.x), dragPoint.y,
							Math.abs(dragPoint.x - ms.x), 0);
				}
				else {
					measureBox = new Rectangle(dragPoint.x, Math.min(dragPoint.y,  ms.y),
							0, Math.abs(dragPoint.y - ms.y));
				}
			}
			
			if (!Mouse.left.down()) {
				dragging = false;
				measureBox = null;
			}
		}
		else if (Mouse.left.pressed() && !control.getHUD().isBusy()) {
			dragging = true;
			dragPoint = control.getCursorPoint();
		}
	}
	
	@Override
	public void draw(Graphics g) {
		int S   = 16;
		int S2  = 8;
		int W   = 8;
		Point v = control.getViewPosition();
		g.setColor(Color.YELLOW);
		
		if (measureBox != null) {
			if (measureBox.getWidth() == 0 && measureBox.getHeight() == 0) {
				g.drawOval((measureBox.getX1() * S) + S2 - 4 - v.x, (measureBox.getY1() * S) + S2 - 4 - v.y, 8, 8);
				drawLine(g, measureBox.getX1() * S, (measureBox.getY1() * S) + S2, (measureBox.getX1() + 1) * S, (measureBox.getY1() * S) + S2);
				drawLine(g, (measureBox.getX1() * S) + S2, measureBox.getY1() * S, (measureBox.getX1() * S) + S2, (measureBox.getY1() + 1) * S);
			}
			else if (measureBox.getHeight() == 0) {
				int x1 = measureBox.getMinX();
				int x2 = measureBox.getMaxX();
				int y  = measureBox.getY1();
				
				drawLine(g, x1 * S, (measureBox.getY1() * S) + S2, (x2 + 1) * S, (measureBox.getY1() * S) + S2);
				for (int x = x1; x <= x2 + 1; x++) {
					int w = (x == x1 || x == x2 + 1 ? S : W);
					drawLine(g, x * S, (y * S) + S2 - (w / 2), x * S, (y * S) + S2 + (w / 2));
				}

				Draw.drawString("" + (measureBox.getWidth() + 1), ((x1 + x2 + 1) * S) / 2 - v.x, (y * S) - 8 - v.y, Draw.CENTERED, Draw.BOTTOM);
			}
			else if (measureBox.getWidth() == 0) {
				int y1 = measureBox.getMinY();
				int y2 = measureBox.getMaxY();
				int x  = measureBox.getX1();
				
				drawLine(g, (measureBox.getX1() * S) + S2, y1 * S, (measureBox.getX1() * S) + S2, (y2 + 1) * S);
				for (int y = y1; y <= y2 + 1; y++) {
					int w = (y == y1 || y == y2 + 1 ? S : W);
					drawLine(g, (x * S) + S2 - (w / 2), y * S, (x * S) + S2 + (w / 2), y * S);
				}

				Draw.drawString("" + (measureBox.getHeight() + 1), (x * S) - 8 - v.x, ((y1 + y2 + 1) * S) / 2 - v.y, Draw.RIGHT, Draw.MIDDLE);
			}
			else {
				int x1 = measureBox.getMinX();
				int x2 = measureBox.getMaxX();
				int y1 = measureBox.getMinY();
				int y2 = measureBox.getMaxY();
				
				g.drawRect(measureBox.getX1() * S - v.x, measureBox.getY1() * S - v.y, (measureBox.getWidth() + 1) * S, (measureBox.getHeight() + 1) * S);
				for (int x = x1; x <= x2 + 1; x++) {
					drawLine(g, x * S, (y1 * S), x * S, (y1 * S) + W);
					drawLine(g, x * S, ((y2 + 1) * S) - W, x * S, (y2 + 1) * S);
				}
				for (int y = y1; y <= y2 + 1; y++) {
					drawLine(g, (x1 * S), y * S, (x1 * S) + W, y * S);
					drawLine(g, ((x2 + 1) * S) - W, y * S, (x2 + 1) * S, y * S);
				}
				
				Point center = new Point(((x1 + x2 + 1) * S) / 2, ((y1 + y2 + 1) * S) / 2);
				Draw.drawString("" + (measureBox.getWidth() + 1), center.x - v.x, (y1 * S) - 8 - v.y, Draw.CENTERED, Draw.BOTTOM);
				Draw.drawString("" + (measureBox.getHeight() + 1), (x1 * S) - 8 - v.x, center.y - v.y, Draw.RIGHT, Draw.MIDDLE);
				Draw.drawString((measureBox.getWidth() + 1) + " x " + (measureBox.getHeight() + 1), center.x - v.x, center.y - v.y, Draw.CENTERED, Draw.MIDDLE);
			}
		}
	}
}
