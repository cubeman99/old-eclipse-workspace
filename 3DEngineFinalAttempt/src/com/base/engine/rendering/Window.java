package com.base.engine.rendering;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import com.base.engine.common.Vector2f;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL11.*;


public class Window {
	private static int lastWidth;
	private static int lastHeight;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public static void createWindow(int width, int height, String title) {
		Display.setTitle(title);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
            Display.setVSyncEnabled(true);
			Display.create();
			lastWidth  = width;
			lastHeight = height;
			
			Keyboard.create();
			Mouse.create();
		}
		catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	

	// =================== ACCESSORS =================== //
	
	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}

	public static int getWidth() {
		return Display.getDisplayMode().getWidth();
	}

	public static int getHeight() {
		return Display.getDisplayMode().getHeight();
	}
	
	public static Vector2f getCenter() {
		return new Vector2f(getWidth() * 0.5f, getHeight() * 0.5f);
	}

	public static float getAspectRatio() {
		return ((float) getWidth() / (float) getHeight());
	}

	public static String getTitle() {
		return Display.getTitle();
	}
	
	public static boolean isFullScreen() {
		return Display.isFullscreen();
	}
	
	

	// ==================== MUTATORS ==================== //

	public static void bindAsRenderTarget(int x, int y, int width, int height) {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		glViewport(x, y, width, height);
		glViewport(0, 0, getWidth(), getHeight());
//		glEnable(GL_SCISSOR_TEST);
	}
	
	public static void bindAsRenderTarget() {
		bindAsRenderTarget(0, 0, getWidth(), getHeight());
	}
	
	public static void render() {
		Display.update();
	}

	public static void dispose() {
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
	}
	
	public static void setFullScreen(boolean fullScreen) {
		try {
			if (fullScreen) {
				lastWidth  = getWidth();
				lastHeight = getHeight();
				Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
			}
			else {
				Display.setDisplayMode(new DisplayMode(lastWidth, lastHeight));
			}
		}
		catch (LWJGLException e) {
			System.err.println("Error: could not change full-screen mode.");
			e.printStackTrace();
		}
	}
}
