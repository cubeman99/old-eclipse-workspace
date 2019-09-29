package main;

import geometry.Point;
import geometry.Vector;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

/**
 * A static mouse listener class that handles mouse buttons and wheel movement.
 * @author	David Jordan
 */
public class Mouse extends MouseAdapter implements MouseWheelListener, WindowFocusListener {

	// ================== Button Definitions ==================
	
	public static Button left		= new Button(MouseEvent.BUTTON1);
	public static Button middle		= new Button(MouseEvent.BUTTON2);
	public static Button right		= new Button(MouseEvent.BUTTON3);

	// ====================== Variables =======================
	
	private static final int	NUM_BUTTONS			= 4;
	
	private static byte[]		buttonState			= new byte[NUM_BUTTONS];
	private static boolean[]	rawButtonState		= new boolean[NUM_BUTTONS];
	private static boolean[]	buttonClicked		= new boolean[NUM_BUTTONS];
	private static boolean[]	rawButtonClicked	= new boolean[NUM_BUTTONS];
	private static int			rawWheelMovement	= 0;
	private static int			wheelMovement		= 0;
	private static Point		position			= new Point();
	private static Point		positionLast		= new Point();
	private static boolean		mouseInWindow		= false;

	// ===================== Constructors =====================

	/** Constructs the listener by setting the mouse states. */
	public Mouse() {
		for (int i = 0; i < NUM_BUTTONS; i++) {
			buttonState[i]		= 0;
			rawButtonState[i]	= false;
			buttonClicked[i]	= false;
			rawButtonClicked[i]	= false;
		}
	}
	
	// =================== Implementations ====================
	
	/** Called whenever a mouse button is pressed. */
	public void mousePressed(MouseEvent e) {
		rawButtonState[e.getButton()] = true;
	}
	/** Called whenever a mouse button is released. */
	public void mouseReleased(MouseEvent e) {
		rawButtonState[e.getButton()] = false;
	}
	/** Called whenever a mouse button is clicked. */
	public void mouseClicked(MouseEvent e) {
		rawButtonClicked[e.getButton()] = true;
	}
	/** Called whenever the mouse wheel is moved. */
	public void mouseWheelMoved(MouseWheelEvent e) {
		rawWheelMovement = e.getWheelRotation();
	}
	/** Called whenever the window gains focus. */
	public void windowGainedFocus(WindowEvent e) {
		
	}
	/** Called whenever the window loses focus. */
	public void windowLostFocus(WindowEvent e) {
		// Reset all button states:
		for (int i = 0; i < NUM_BUTTONS; i++) {
			rawButtonState[i]	= false;
			rawButtonClicked[i]	= false;
		}
		rawWheelMovement = 0;
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the mouse button states. */
	public static void update() {
		// Update each of the mouse buttons:
		for (int i = 0; i < NUM_BUTTONS; i++) {
			// Update button clicked:
			buttonClicked[i]	= rawButtonClicked[i];
			rawButtonClicked[i]	= false;
			
			// Update button state:
			if (rawButtonState[i]) {
				if (buttonState[i] <= 1)
					buttonState[i] = 3;
				else
					buttonState[i] = 2;
			}
			else {
				if (buttonState[i] >= 2)
					buttonState[i] = 1;
				else
					buttonState[i] = 0;
			}
		}
		
		// Update mouse wheel:
		wheelMovement    = rawWheelMovement;
		rawWheelMovement = 0;
		java.awt.Point msPosition = Main.panel.getMousePosition();
		
		// Update mouse position:
		positionLast.set(position);
	
		mouseInWindow = (msPosition != null);
		if (mouseInWindow) {
			position.set(msPosition.x, msPosition.y);
		}
	}
	/** Called when the mouse button states need to be reset to the default. */
	public static void reset() {
		// Reset all button states:
		for (int i = 0; i < NUM_BUTTONS; i++) {
			rawButtonState[i]	= false;
			rawButtonClicked[i]	= false;
		}
		rawWheelMovement = 0;
	}

	// ===================== Mouse Events =====================

	/** Returns a button class with the given button code. */
	public static Button getButton(int buttonCode) {
		return new Button(buttonCode);
	}
	/** Returns true if the mouse wheel was scrolled up. */
	public static boolean wheelUp() {
		return (wheelMovement < 0);
	}
	/** Returns true if the mouse wheel was scrolled down. */
	public static boolean wheelDown() {
		return (wheelMovement > 0);
	}
	/** Returns a vector of the mouse position. */
	public static Vector getVector() {
		return new Vector(position.x, position.y);
	}
	/** Returns a vector of the last mouse position. */
	public static Vector getVectorLast() {
		return new Vector(positionLast.x, positionLast.y);
	}
	/** Returns a vector of the distance mouse the mouse moved. */
	public static Vector getVectorDistance() {
		return new Vector(position.x - positionLast.x, position.y - positionLast.y);
	}
	/** Returns a vector of the mouse position. */
	public static Point getPoint() {
		return new Point(position.x, position.y);
	}
	/** Returns a vector of the last mouse position. */
	public static Point getPointLast() {
		return new Point(positionLast.x, positionLast.y);
	}
	/** Returns a vector of the distance mouse the mouse moved. */
	public static Point getPointDistance() {
		return new Point(position.x - positionLast.x, position.y - positionLast.y);
	}
	/** Returns true if the mouse is inside the window. */
	public static boolean isMouseInsideWindow() {
		return mouseInWindow;
	}

	// =================== Button Sub-Class ===================
	
	/** A sub-class that represents a mouse button. */
	public static class Button {
		
		// ======================= Members ========================
		
		/** The code that represents the mouse button. */
		private int buttonIndex;
		
		// ===================== Constructors =====================

		/** Initialize a button with the specified mouse button. */
		public Button(int buttonIndex) {
			this.buttonIndex = buttonIndex;
		}

		// ===================== Mouse States =====================

		/** Returns true if the mouse button was pressed. */
		public boolean pressed() {
			return buttonState[buttonIndex] == 3;
		}
		/** Returns true if the mouse button is down. */
		public boolean down() {
			return buttonState[buttonIndex] >= 2;
		}
		/** Returns true if the mouse button was released. */
		public boolean released() {
			return buttonState[buttonIndex] == 1;
		}
		/** Returns true if the mouse button is up. */
		public boolean up() {
			return buttonState[buttonIndex] <= 1;
		}
		/** Returns true if the mouse button was clicked. */
		public boolean clicked() {
			return buttonClicked[buttonIndex];
		}
	}
}