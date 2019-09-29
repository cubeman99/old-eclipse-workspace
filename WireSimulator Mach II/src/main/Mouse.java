package main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Mouse extends MouseAdapter {
	private static int mouseX 				= 0;
	private static int mouseY 				= 0;
	private static boolean rawLeftDown   	= false;
	private static boolean rawRightDown  	= false;
	private static boolean rawMiddleDown	= false;
	private static boolean mbLeftDown   	= false;
	private static boolean mbRightDown  	= false;
	private static boolean mbMiddleDown		= false;
	private static boolean mbLeftDownPrev   = false;
	private static boolean mbRightDownPrev  = false;
	private static boolean mbMiddleDownPrev = false;

	
	// Mouse Coordinates
	public static int x() {
		return mouseX;
	}

	public static int y() {
		return mouseY;
	}
	
	public static Point position() {
		return new Point(mouseX, mouseY);
	}
	
	// Mouse Button Down
	public static boolean left() {
		return mbLeftDown;
	}
	
	public static boolean right() {
		return mbRightDown;
	}

	public static boolean middle() {
		return mbMiddleDown;
	}

	// Mouse Button Pressed
	public static boolean leftPressed() {
		return (mbLeftDown && !mbLeftDownPrev);
	}

	public static boolean rightPressed() {
		return (mbRightDown && !mbRightDownPrev);
	}

	public static boolean middlePressed() {
		return (mbMiddleDown && !mbMiddleDownPrev);
	}
	
	// Mouse Button Released
	public static boolean leftReleased() {
		return (!mbLeftDown && mbLeftDownPrev);
	}

	public static boolean rightReleased() {
		return (!mbRightDown && mbRightDownPrev);
	}

	public static boolean middleReleased() {
		return (!mbMiddleDown && mbMiddleDownPrev);
	}
	
	// Return if the mouse is in an area (rectangle)
	public static boolean inArea(int x1, int y1, int x2, int y2) {
		return (mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2);
	}
	
	public static boolean inArea(Point p1, Point p2) {
		return (mouseX >= p1.x && mouseY >= p1.y && mouseX < p2.x && mouseY < p2.y);
	}
	
	public static boolean inArea(Rectangle r) {
		return (mouseX >= r.x && mouseY >= r.y && mouseX < r.x + r.width && mouseY < r.y + r.width);
	}
	
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			rawLeftDown = true;
		else if (e.getButton() == MouseEvent.BUTTON2)
			rawMiddleDown = true;
		else if (e.getButton() == MouseEvent.BUTTON3) {
			rawRightDown = true;
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1)
			rawLeftDown = false;
		else if (e.getButton() == MouseEvent.BUTTON2)
			rawMiddleDown = false;
		else if (e.getButton() == MouseEvent.BUTTON3)
			rawRightDown = false;
	}
	
	public static void update() {
		
		// Update the mouse buttons
		mbLeftDownPrev   = mbLeftDown;
		mbMiddleDownPrev = mbMiddleDown;
		mbRightDownPrev  = mbRightDown;
		mbLeftDown		 = rawLeftDown;
		mbMiddleDown	 = rawMiddleDown;
		mbRightDown		 = rawRightDown;

		// Update the mouse Coordinates
		Point msPosition = Main.game.getMousePosition();
		if (msPosition != null) {
			mouseX = (int) msPosition.x;
			mouseY = (int) msPosition.y;
		}
	}
}
