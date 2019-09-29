package com.base.engine.rendering;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import com.base.engine.common.Point;
import com.base.engine.common.Vector2f;
import com.base.engine.rendering.resourceManagement.ResourceManager;

public class Font {
	private Texture texture;
	private String fileName;
	private int[] charMap;
	private int columnSize;
	private Vector2f charSize;
	private float charAspectRatio;
	
	
	public Font(String fileName, int charWidth, int charHeight, int columnSize) {
		String temp = ResourceManager.TEXTURE_DIRECTORY;
		ResourceManager.TEXTURE_DIRECTORY = ResourceManager.FONT_DIRECTORY;
		this.texture = new Texture(fileName);
		ResourceManager.TEXTURE_DIRECTORY = temp;
		
		this.fileName   = fileName;
		this.columnSize = columnSize;
		this.charSize   = new Vector2f(charWidth / (float) texture.getWidth(),
				charHeight / (float) texture.getHeight());
		this.charAspectRatio = charHeight / (float) charWidth;
		
		charMap = new int[255];
		for (int i = 0; i < 255; i++)
			charMap[i] = i;
	}
	
	public float getCharAspectRatio() {
		return charAspectRatio;
	}
	
	public Vector2f getCharSize() {
		return charSize;
	}
	
	public void drawChar(char c, float x, float y, float xScale) {
		drawChar(c, x, y, xScale, xScale * charAspectRatio);
	}

	public void drawChar(char c, float x, float y, float xScale, float yScale) {
		glBindTexture(GL_TEXTURE_2D, texture.getResource().getTextureID(0));
		float sx = (charMap[c] % columnSize) * charSize.x;
		float sy = (charMap[c] / columnSize) * charSize.y;
		
		glBegin(GL_QUADS);
        glTexCoord2f(sx, sy);
        glVertex2f(x, y);
        glTexCoord2f(sx + charSize.x, sy);
        glVertex2f(x + xScale, y);
        glTexCoord2f(sx + charSize.x, sy + charSize.y);
        glVertex2f(x + xScale, y + yScale);
        glTexCoord2f(sx, sy + charSize.y);
        glVertex2f(x, y + yScale);
        glEnd();
        
		glBindTexture(GL_TEXTURE_2D, 0);
	}
}
