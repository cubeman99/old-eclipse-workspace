package game;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import common.Vector;


public class Mouse extends MouseAdapter implements MouseWheelListener {
	public static Button left           = new Button(MouseEvent.BUTTON1);
	public static Button middle         = new Button(MouseEvent.BUTTON2);
	public static Button right          = new Button(MouseEvent.BUTTON3);
	private static Button[] buttons     = {left, middle, right};
	private static int x                = 0;
	private static int y                = 0;
	private static int xprev			= 0;
	private static int yprev			= 0;
	private static int rawWheelMovement = 0;
	private static int wheelMovement    = 0;
	
	
	// Sub-class Button
	public static class Button {
		protected int buttonIndex;
		protected boolean rawDown;
		protected boolean down;
		protected boolean downPrev;
		protected boolean rawClicked;
		protected boolean clicked;
		
		public Button(int buttonIndex) {
			this.buttonIndex = buttonIndex;
			this.rawDown     = false;
			this.down        = false;
			this.downPrev    = false;
			this.rawClicked  = false;
			this.clicked     = false;
		}
		
		public boolean down() {
			return down;
		}
		
		public boolean pressed() {
			return (down && !downPrev);
		}
		
		public boolean released() {
			return (!down && downPrev);
		}
		
		public boolean clicked() {
			return clicked;
		}
	}
	
	// Mouse Coordinates
	public static int x() {
		return x;
	}

	public static int y() {
		return y;
	}
	
	public static Point position() {
		return new Point(x, y);
	}
	
	public static Vector getVector() {
		return new Vector(x, y);
	}

	public static Vector getVectorPrevious() {
		return new Vector(xprev, yprev);
	}
	
	public static boolean wheelUp() {
		return (wheelMovement < 0);
	}

	public static boolean wheelDown() {
		return (wheelMovement > 0);
	}
	
	
	// Return if the mouse is in an area (rectangle)
	public static boolean inArea(int x1, int y1, int x2, int y2) {
		return (x >= x1 && y >= y1 && x < x2 && y < y2);
	}
	
	public static boolean inArea(Point p1, Point p2) {
		return (x >= p1.x && y >= p1.y && x < p2.x && y < p2.y);
	}
	
	public static boolean inArea(Rectangle r) {
		return (x >= r.x && y >= r.y && x < r.x + r.width && y < r.y + r.width);
	}


	@Override
	public void mousePressed(MouseEvent e) {
		for (Button b : buttons) {
			if (b.buttonIndex == e.getButton()) {
				b.rawDown = true;
				break;
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		for (Button b : buttons) {
			if (b.buttonIndex == e.getButton()) {
				b.rawDown = false;
				break;
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		for (Button b : buttons) {
			if (b.buttonIndex == e.getButton()) {
				b.rawClicked = true;
				break;
			}
		}
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		rawWheelMovement = e.getWheelRotation();
	}
	
	
	public static void update() {
		// Update the button states:
		for (Button b : buttons) {
			b.downPrev = b.down;
			b.down     = b.rawDown;
			b.clicked  = b.rawClicked;
		}
		
		// Update the previous mouse coordinates:
		xprev = x;
		yprev = y;
		
		// Update the scroll wheel movement:
		wheelMovement    = rawWheelMovement;
		rawWheelMovement = 0;

		// Update the mouse coordinates:
		Point msPosition = Main.game.getMousePosition();
		if (msPosition != null) {
			x = (int) msPosition.x;
			y = (int) msPosition.y;
		}
	}
}
