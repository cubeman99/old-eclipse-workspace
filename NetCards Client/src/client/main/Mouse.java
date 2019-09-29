package client.main;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;


public class Mouse extends MouseAdapter implements MouseWheelListener {
	private static int mouseX 				  = 0;
	private static int mouseY 				  = 0;
	private static boolean rawLeftDown   	  = false;
	private static boolean rawRightDown  	  = false;
	private static boolean rawMiddleDown	  = false;
	private static boolean mbLeftDown    	  = false;
	private static boolean mbRightDown     	  = false;
	private static boolean mbMiddleDown	 	  = false;
	private static boolean mbLeftDownPrev     = false;
	private static boolean mbRightDownPrev    = false;
	private static boolean mbMiddleDownPrev   = false;
	private static boolean rawLeftClicked     = false;
	private static boolean rawMiddleClicked   = false;
	private static boolean rawRightClicked    = false;
	private static boolean mbLeftClicked      = false;
	private static boolean mbMiddleClicked    = false;
	private static boolean mbRightClicked     = false;
	private static int rawWheelMovement		  = 0;
	private static int wheelMovement		  = 0;

	
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
	
	// Mouse Button Clicked
	public static boolean leftClicked() {
		return mbLeftClicked;
	}
	
	public static boolean rightClicked() {
		return mbRightClicked;
	}

	public static boolean middleClicked() {
		return mbMiddleClicked;
	}
	
	
	public static boolean scrollUp() {
		return (wheelMovement < 0);
	}

	public static boolean scrollDown() {
		return (wheelMovement > 0);
	}
	
	// Return if the mouse is in an area (rectangle)
	public static boolean inArea(int x1, int y1, int x2, int y2) {
		return (mouseX >= x1 && mouseY >= y1 && mouseX < x2 && mouseY < y2);
	}
	
	public static boolean inArea(Point p1, Point p2) {
		return (mouseX >= p1.x && mouseY >= p1.y && mouseX < p2.x && mouseY < p2.y);
	}
	
	public static boolean inArea(Rectangle r) {
		return (mouseX >= r.x && mouseY >= r.y && mouseX < r.x + r.width && mouseY < r.y + r.height);
	}
	
	public void mousePressed(MouseEvent e) {
		Main.frame.requestFocus();
		if (e.getButton() == MouseEvent.BUTTON1)
			rawLeftDown = true;
		else if (e.getButton() == MouseEvent.BUTTON2)
			rawMiddleDown = true;
		else if (e.getButton() == MouseEvent.BUTTON3)
			mbRightDown = true;
	}
	
	public void mouseReleased(MouseEvent e) {
		Main.frame.requestFocus();
		if (e.getButton() == MouseEvent.BUTTON1)
			rawLeftDown = false;
		else if (e.getButton() == MouseEvent.BUTTON2)
			rawMiddleDown = false;
		else if (e.getButton() == MouseEvent.BUTTON3)
			rawRightDown = false;
	}
	public void mouseClicked(MouseEvent e) {
		Main.frame.requestFocus();
		if (e.getButton() == MouseEvent.BUTTON1)
			rawLeftClicked = true;
		else if (e.getButton() == MouseEvent.BUTTON2)
			rawMiddleClicked = true;
		else if (e.getButton() == MouseEvent.BUTTON3)
			rawRightClicked = true;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		rawWheelMovement = e.getWheelRotation();
	}
	
	public static void update() {
		
		// Update the mouse buttons
		mbLeftDownPrev   = mbLeftDown;
		mbMiddleDownPrev = mbMiddleDown;
		mbRightDownPrev  = mbRightDown;
		mbLeftDown		 = rawLeftDown;
		mbMiddleDown	 = rawMiddleDown;
		mbRightDown		 = rawRightDown;
		
		mbLeftClicked    = rawLeftClicked;
		mbMiddleClicked  = rawMiddleClicked;
		mbRightClicked   = rawRightClicked;
		rawLeftClicked   = false;
		rawMiddleClicked = false;
		rawRightClicked  = false;
		
		wheelMovement    = rawWheelMovement;
		rawWheelMovement = 0;
		
		

		// Update the mouse Coordinates
		Point msPosition = Main.game.getMousePosition();
		if (msPosition != null) {
			mouseX = (int) msPosition.x;
			mouseY = (int) msPosition.y;
		}
	}
}
