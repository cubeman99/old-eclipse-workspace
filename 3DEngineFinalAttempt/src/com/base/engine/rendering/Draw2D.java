package com.base.engine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import com.base.engine.common.Rect2f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;

public class Draw2D {
	public static final Vector3f BLACK      = new Vector3f(0, 0, 0);
	public static final Vector3f WHITE      = new Vector3f(1, 1, 1);
	public static final Vector3f GRAY       = new Vector3f(0.5f);
	public static final Vector3f LIGHT_GRAY = new Vector3f(0.75f);
	public static final Vector3f DARK_GRAY  = new Vector3f(0.25f);
	public static final Vector3f RED        = new Vector3f(1, 0, 0);
	public static final Vector3f MAROON     = new Vector3f(0.5f, 0, 0);
	public static final Vector3f ORANGE     = new Vector3f(1, 0.5f, 0);
	public static final Vector3f YELLOW     = new Vector3f(1, 1, 0);
	public static final Vector3f OLIVE      = new Vector3f(0.5f, 0.5f, 0);
	public static final Vector3f GREEN      = new Vector3f(0, 1, 0);
	public static final Vector3f DARK_GREEN = new Vector3f(0, 0.5f, 0);
	public static final Vector3f CYAN       = new Vector3f(0, 1, 1);
	public static final Vector3f TEAL       = new Vector3f(0, 0.5f, 0.5f);
	public static final Vector3f BLUE       = new Vector3f(0, 0, 1);
	public static final Vector3f NAVY       = new Vector3f(0, 0, 0.5f);
	public static final Vector3f MAGENTA    = new Vector3f(1, 0, 1);
	public static final Vector3f PURPLE     = new Vector3f(0.5f, 0, 0.5f);
	
	public static final int TOP    = 0;
	public static final int MIDDLE = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT   = 0;
	public static final int CENTER = 1;
	public static final int RIGHT  = 2;
	
	private static Font font;
	
	
	
	// ==================== GENERAL ==================== //

	public static void setColor(Vector3f color) {
		glColor4f(color.x, color.y, color.z, 1);
	}
	
	public static void setColor(float r, float g, float b) {
		glColor4f(r, g, b, 1);
	}
	
	public static void setColor(Vector3f color, float alpha) {
		glColor4f(color.x, color.y, color.z, alpha);
	}
	
	public static void setColor(float r, float g, float b, float a) {
		glColor4f(r, g, b, a);
	}
	
	public static void setFont(Font font) {
		Draw2D.font = font;
	}
	
	public static void defineVertex(Vector2f v) {
		glVertex2f(v.x, v.y);
	}
	
	
	
	// ===================== LINES ===================== //

	public static void drawLine(float x1, float y1, float x2, float y2) {
        glBegin(GL_LINES);
        glVertex2f(x1, y1);
        glVertex2f(x2, y2);
		glEnd();
	}
	
	public static void drawLine(Vector2f end1, Vector2f end2) {
        glBegin(GL_LINES);
        defineVertex(end1);
        defineVertex(end2);
		glEnd();
	}
	
	
	
	// =================== RECTANGLES =================== //

	public static void drawRect(float x, float y, float width, float height) {
        glBegin(GL_LINE_LOOP);
        defineRect(x, y, width, height);
        glEnd();
	}
	
	public static void drawRect(Vector2f pos, Vector2f size) {
        glBegin(GL_LINE_LOOP);
		defineRect(pos.x, pos.y, size.x, size.y);
        glEnd();
	}
	
	public static void drawRect(Rect2f r) {
        glBegin(GL_LINE_LOOP);
		defineRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        glEnd();
	}

	public static void fillRect(float x, float y, float width, float height) {
        glBegin(GL_QUADS);
        defineRect(x, y, width, height);
        glEnd();
	}
	
	public static void fillRect(Vector2f pos, Vector2f size) {
        glBegin(GL_QUADS);
		defineRect(pos.x, pos.y, size.x, size.y);
        glEnd();
	}
	
	public static void fillRect(Rect2f r) {
        glBegin(GL_QUADS);
		defineRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
        glEnd();
	}
	
	private static void defineRect(float x, float y, float width, float height) {
        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
	}
	
	
	
	// ==================== CIRCLES ==================== //

	public static void fillCircle(Vector2f pos, float radius) {
		fillCircle(pos, radius, 12);
	}
	
	public static void drawCircle(Vector2f pos, float radius) {
        drawCircle(pos, radius, 12);
	}
	
	public static void fillCircle(Vector2f pos, float radius, int detail) {
        glBegin(GL_POLYGON);
		defineCircle(pos, radius, detail);
		glEnd();
	}
	
	public static void drawCircle(Vector2f pos, float radius, int detail) {
		glBegin(GL_LINE_LOOP);
		defineCircle(pos, radius, detail);
		glEnd();
	}
	
	private static void defineCircle(Vector2f pos, float radius, int detail) {
		for (int i = 0; i < detail; i++) {
			float angle = (i / (float) detail) * (float) Math.PI * 2;
    		glVertex2f(pos.x + (float) Math.cos(angle) * radius, pos.y + (float) Math.sin(angle) * radius);
		}
	}
	
	

	// ==================== IMAGES ==================== //
	
	public static void drawTexture(Texture tex, float x, float y, float width, float height) {
		glBindTexture(GL_TEXTURE_2D, tex.getResource().getTextureID(0));
		
		glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(x, y);
        glTexCoord2f(1, 0);
        glVertex2f(x + width, y);
        glTexCoord2f(1, 1);
        glVertex2f(x + width, y + height);
        glTexCoord2f(0, 1);
        glVertex2f(x, y + height);
        glEnd();
        
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	
	
	// ===================== TEXT ===================== //
	
	public static void drawText(String text, float x, float y, float size) {
		drawText(text, x, y, size, LEFT, TOP);
	}
	
	public static void drawText(String text, float x, float y, float size, int hAlign) {
		drawText(text, x, y, size, hAlign, TOP);
	}
	
	public static void drawText(String text, float x, float y, float size, int hAlign, int vAlign) {
		float offsetX = 0;
		float offsetY = 0;
		if (hAlign > 0)
			offsetX = -text.length() * size;
		if (vAlign > 0)
			offsetY = -(text.length() * size) * font.getCharAspectRatio();
		if (hAlign == CENTER)
			offsetX *= 0.5f;
		if (vAlign == MIDDLE)
			offsetY *= 0.5f;
		
		for (int i = 0; i < text.length(); i++) {
			font.drawChar(text.charAt(i), x + offsetX + (i * size), y + offsetY, size);
		}
	}
}
