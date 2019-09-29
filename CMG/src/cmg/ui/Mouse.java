package cmg.ui;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import cmg.math.geometry.Point;
import cmg.math.geometry.Vector;


/**
 * A  mouse listener class that handles mouse buttons and wheel movement.
 * 
 * @author David Jordan
 */
public class Mouse extends MouseAdapter implements MouseWheelListener {
	public static final Cursor BLANK_CURSOR;
	static {
		BufferedImage cursorImg = new BufferedImage(16, 16,
				BufferedImage.TYPE_INT_ARGB);
		BLANK_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
				cursorImg, new java.awt.Point(0, 0), "blank cursor");
	}
	public static Mouse current = null;

	public MouseButton left;
	public MouseButton middle;
	public MouseButton right;
	private MouseButton[] buttons;
	private int rawWheelMovement;
	private int wheelMovement;
	private Point position;
	private Point positionLast;
	private boolean mouseInWindow;
	private Window window;
	private Cursor cursor;

	

	// ================== CONSTRUCTORS ================== //

	public Mouse(Window wnd) {
		window       = wnd;
		left         = new MouseButton(MouseEvent.BUTTON1);
		middle       = new MouseButton(MouseEvent.BUTTON2);
		right        = new MouseButton(MouseEvent.BUTTON3);
		buttons      = new MouseButton[] {left, middle, right};
		cursor       = Cursor.getDefaultCursor();
		position     = new Point();
		positionLast = new Point();
	}



	// =================== ACCESSORS =================== //

	/** Get the x-position of the mouse cursor. **/
	public int x() {
		return position.x;
	}

	/** Get the y-position of the mouse cursor. **/
	public int y() {
		return position.y;
	}

	/** Get the previous x-position of the mouse cursor. **/
	public int xPrevious() {
		return positionLast.x;
	}

	/** Get the previous y-position of the mouse cursor. **/
	public int yPrevious() {
		return positionLast.y;
	}

	/** Get the mouse position as a Point. **/
	public Point position() {
		return new Point(position);
	}

	/** Get the previous mouse position as a Point. **/
	public Point positionLast() {
		return new Point(positionLast);
	}

	/** Get the mouse position as a Vector. **/
	public Vector getVector() {
		return new Vector(position);
	}

	/** Get the previous mouse position as a Vector. **/
	public Vector getVectorPrevious() {
		return new Vector(positionLast.x, positionLast.y);
	}

	/** Check if the mouse wheel is moved up. **/
	public boolean wheelUp() {
		return (wheelMovement < 0);
	}

	/** Check if the mouse wheel is moved down. **/
	public boolean wheelDown() {
		return (wheelMovement > 0);
	}

	/** Check if the mouse is inside the window. **/
	public boolean inWindow() {
		return mouseInWindow;
	}

	/** Get the mouse cursor. **/
	public Cursor getCursor() {
		return cursor;
	}

	
	// ==================== MUTATORS ==================== //

	/** Set the mouse cursor to a cursor constant. **/
	public void setCursor(int cursorIndex) {
		cursor = new Cursor(cursorIndex);
	}

	/** Set the mouse cursor. **/
	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	/** Set the mouse cursor to the default mouse cursor. **/
	public void setDefaultCursor() {
		cursor = Cursor.getDefaultCursor();
	}
	
	/** Release all the mouse buttons. **/
	public void clear() {
		for (int i = 0; i < buttons.length; i++)
			buttons[i].rawDown = false;
	}

	/** Update button states, wheel movement, and mouse coordinates. **/
	public void update() {
		// Update the button states:
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].downPrev = buttons[i].down;
			buttons[i].down     = buttons[i].rawDown;
			buttons[i].clicked  = buttons[i].rawClicked;
		}

		// Update the scroll wheel movement:
		wheelMovement    = rawWheelMovement;
		rawWheelMovement = 0;

		// Update the mouse coordinates:
		positionLast.set(position);
		java.awt.Point ms = window.getWindowContent().getMousePosition();
		mouseInWindow     = (ms != null);
		if (mouseInWindow)
			position.set(ms.x, ms.y);
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO: component.requestFocusInWindow();
		buttons[e.getButton() - 1].rawDown = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		buttons[e.getButton() - 1].rawDown = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		buttons[e.getButton() - 1].rawClicked = true;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		rawWheelMovement = e.getWheelRotation();
	}
}
