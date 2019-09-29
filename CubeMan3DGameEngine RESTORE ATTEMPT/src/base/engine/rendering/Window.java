package base.engine.rendering;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import base.engine.common.Vector;

public class Window {
	
	// =================== ACCESSORS =================== //

	/** Return whether the window is requested to be closed. **/
	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}

	/** Get the width of the window in pixels. **/
	public static int getWidth() {
		return Display.getDisplayMode().getWidth();
	}
	
	/** Get the height of the window in pixels. **/
	public static int getHeight() {
		return Display.getDisplayMode().getHeight();
	}
	
	/** Return the window's title caption. **/
	public static String getTitle() {
		return Display.getTitle();
	}
	
	/** Return the center coordinate of the window as a vector. **/
	public Vector getCenter() {
		return new Vector(getWidth() / 2, getHeight() / 2);
	}
	
	

	// ==================== MUTATORS ==================== //
	
	/** Create the window with the given width, height and title caption. **/
	public static void createWindow(int width, int height, String title) {
		Display.setTitle(title);
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Keyboard.create();
			Mouse.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	/** Render the window. **/
	public static void render() {
		Display.update();
	}
	
	/** Close the window. **/
	public static void dispose() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
}
