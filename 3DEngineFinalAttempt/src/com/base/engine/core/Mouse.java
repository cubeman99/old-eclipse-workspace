package com.base.engine.core;

import java.awt.Point;
import com.base.engine.common.Vector2f;
import com.base.engine.rendering.Window;


public class Mouse {
	public static MouseButton left   = new MouseButton(0);
	public static MouseButton right  = new MouseButton(1);
	public static MouseButton middle = new MouseButton(2);
	public static MouseButton mouse4 = new MouseButton(3);
	public static MouseButton mouse5 = new MouseButton(4);

	protected static final int NUM_MOUSE_BUTTONS = 5;
	protected static boolean[] buttonStates      = new boolean[NUM_MOUSE_BUTTONS];
	protected static boolean[] buttonPrevStates  = new boolean[NUM_MOUSE_BUTTONS];
	private static Point position                = new Point();
	private static Point positionLast            = new Point();
	private static boolean mouseInWindow         = false;
	private static int wheelMovement             = 0;
	
	

	// =================== ACCESSORS =================== //

	/** Get the x component of the mouse position. **/
	public static int getX() {
		return position.x;
	}

	/** Get the y component of the mouse position. **/
	public static int getY() {
		return position.y;
	}

	/** Get the mouse position as a Vector. **/
	public static Vector2f getPosition() {
		return new Vector2f(position.x, position.y);
	}

	/** Get the previous mouse position as a Vector. **/
	public static Vector2f getPositionLast() {
		return new Vector2f(positionLast.x, positionLast.y);
	}

	/** Check if the mouse wheel is moved up. **/
	public static boolean onWheelUp() {
		return (wheelMovement > 0);
	}

	/** Check if the mouse wheel is moved down. **/
	public static boolean onWheelDown() {
		return (wheelMovement < 0);
	}

	/** Check if the mouse is inside the window. **/
	public static boolean isInWindow() {
		return mouseInWindow;
	}
	
	

	// ==================== MUTATORS ==================== //
	
	/** Check if the mouse is inside the window. **/
	public static void setMousePosition(Vector2f pos) {
		org.lwjgl.input.Mouse.setCursorPosition((int) pos.x, Window.getHeight() - (int) pos.y);
	}
	
	/** Set the visibility state of the cursor. **/
	public static void setCursorState(boolean enabled) {
		org.lwjgl.input.Mouse.setGrabbed(!enabled);
	}
	
	/** Update button states, wheel movement, and mouse coordinates. **/
	protected static void update() {
		// Update the scroll wheel movement:
		wheelMovement = org.lwjgl.input.Mouse.getDWheel();
		
		// Update the mouse coordinates:
		positionLast.x = position.x;
		positionLast.y = position.y;

		position.x    = org.lwjgl.input.Mouse.getX();
		position.y    = Window.getHeight() - org.lwjgl.input.Mouse.getY();
		mouseInWindow = org.lwjgl.input.Mouse.isInsideWindow();

		// Update the button states:
		for (int i = 0; i < NUM_MOUSE_BUTTONS; i++) {
			buttonPrevStates[i] = buttonStates[i];
			buttonStates[i] = org.lwjgl.input.Mouse.isButtonDown(i);
		}
	}
}
