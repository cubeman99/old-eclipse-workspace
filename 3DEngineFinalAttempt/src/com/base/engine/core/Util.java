package com.base.engine.core;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_NONE;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import com.base.engine.common.Matrix4f;
import com.base.engine.common.Vector3f;
import com.base.engine.rendering.Bitmap;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.resourceManagement.TextureResource;
import com.base.game.wolf3d.DynamicAnimation;
import com.base.game.wolf3d.SpriteAnimation;

public class Util {
	public static void printArray(int[] array) {
		System.out.print("[");
		for (int i = 0; i < array.length; i++) {
			if (i > 0)
				System.out.print(", ");
			System.out.print(array[i]);
		}
		System.out.println("]");
	}
	
	public static String getFileExtension(String fileName) {
		String[] splitArray = fileName.split("\\.");
		return splitArray[splitArray.length - 1];
	}
	
	public static FloatBuffer createFloatBuffer(int size) {
		return BufferUtils.createFloatBuffer(size);
	}

	public static IntBuffer createIntBuffer(int size) {
		return BufferUtils.createIntBuffer(size);
	}

	public static ByteBuffer createByteBuffer(int size) {
		return BufferUtils.createByteBuffer(size);
	}
	
	public static IntBuffer createFlippedBuffer(Integer... values) {
		IntBuffer buffer = createIntBuffer(values.length);
		for (int i = 0; i < values.length; i++)
			buffer.put(values[i]);
		buffer.flip();
		
		return buffer;
	}
	
	public static IntBuffer createFlippedBuffer(int... values) {
		IntBuffer buffer = BufferUtils.createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Vertex[] vertices) {
		FloatBuffer buffer = createFloatBuffer(vertices.length * Vertex.SIZE);
		
		for (int i = 0; i < vertices.length; i++) {
			buffer.put(vertices[i].getPos().getX());
			buffer.put(vertices[i].getPos().getY());
			buffer.put(vertices[i].getPos().getZ());
			buffer.put(vertices[i].getTexCoord().getX());
			buffer.put(vertices[i].getTexCoord().getY());
			buffer.put(vertices[i].getNormal().getX());
			buffer.put(vertices[i].getNormal().getY());
			buffer.put(vertices[i].getNormal().getZ());
			buffer.put(vertices[i].getTangent().getX());
			buffer.put(vertices[i].getTangent().getY());
			buffer.put(vertices[i].getTangent().getZ());
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Matrix4f value) {
		FloatBuffer buffer = createFloatBuffer(4 * 4);
		
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++)
				buffer.put(value.get(i, j));
		}
		
		buffer.flip();
		return buffer;
	}
	
	public static String[] removeEmptyStrings(String[] data) {
		ArrayList<String> result = new ArrayList<String>();
		
		for(int i = 0; i < data.length; i++) {
			if(!data[i].equals(""))
				result.add(data[i]);
		}
		
		String[] res = new String[result.size()];
		result.toArray(res);
		return res;
	}
	
	public static int[] toIntArray(Integer[] data) {
		int[] result = new int[data.length];
		
		for(int i = 0; i < data.length; i++)
			result[i] = data[i].intValue();
		
		return result;
	}
	

	
	public static Texture[][] createTextureSheet(Bitmap image, int numSpritesX, int numSpritesY,
			int spriteWidth, int spriteHeight, int seperation)
	{
		return createTextureSheet(image, numSpritesX, numSpritesY, spriteWidth, spriteHeight, seperation, seperation);
	}

	
	public static Texture[][] createTextureSheet(Bitmap image, int numSpritesX, int numSpritesY,
			int spriteWidth, int spriteHeight, int seperationX, int seperationY)
	{
		Texture[][] sheet = new Texture[numSpritesX][numSpritesY];
		
    	for (int spriteY = 0; spriteY < numSpritesY; spriteY++) {
    		for (int spriteX = 0; spriteX < numSpritesX; spriteX++) {
    			ByteBuffer data = Util.createByteBuffer((spriteWidth * spriteHeight) * 4);

				for (int y = 0; y < spriteHeight; y++) {
					for (int x = 0; x < spriteWidth; x++) {
    	    			int pixel = image.getPixel(
    	    					(spriteX * (spriteWidth  + seperationX)) + x,
    	    					(spriteY * (spriteHeight + seperationY)) + y);
    	    		
    	    			data.put((byte) ((pixel >> 16) & 0xFF)); // Red
    	    			data.put((byte) ((pixel >>  8) & 0xFF)); // Green
    	    			data.put((byte) ((pixel      ) & 0xFF)); // Blue
    	    			data.put((byte) ((pixel >> 24) & 0xFF)); // Alpha value
        			}
    			}
    			
    			data.flip();
    			
    			sheet[spriteX][spriteY] = new Texture(spriteWidth, spriteHeight, data,
    					GL_TEXTURE_2D, GL_NEAREST, GL_RGBA, GL_RGBA, false, GL_NONE);
    		}
    	}
    	
    	return sheet;
	}
	
	
	public static ByteBuffer createNormalMapFromDispMap(String fileName, float dispMapScale) {
		try {
			BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
			int width        = image.getWidth();
			int height       = image.getHeight();
			int[] pixels     = image.getRGB(0, 0, width, height, null, 0, width);
			ByteBuffer data  = Util.createByteBuffer((width * height) * 4);
			int[][] disp     = new int[width][height];
			int[] hn = new int[4];
			
			for (int i = 0; i < width * height; i++) {
				disp[i % width][i / width] = pixels[i] & 0xFF;
			}
			
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Vector3f normal = new Vector3f(0, 0, 0);
					
					if (x > 0 && y > 0 && x < width - 1 && y < height - 1) {
    					int h = disp[x][y];
    					hn[0] = disp[x + 1][y];
    					hn[1] = disp[x][y + 1];
    					hn[2] = disp[x - 1][y];
    					hn[3] = disp[x][y - 1];
    					
    					Vector3f dx = new Vector3f(hn[0] - hn[2], 0, 1).normalize();
    					Vector3f dy = new Vector3f(0, hn[1] - hn[3], 1).normalize();
    					normal.add(dx).add(dy).normalize();
    					
//    					normal.negate();
    					/*
    					for (int i = 0; i < 4; i++) {
    						Vector3f dh = new Vector3f(hn[i] - h, 0, 1);
    						dh.normalize();
    						dh.rotate((float) Math.PI * 0.5f * i, new Vector3f(0, 0, -1));
    						dh.normalize();
    						normal.add(dh);
    					}
    					normal = normal.normalize();
    					*/
					}
					else {
						normal.set(0, 0, 1);
					}
					
					
					data.put((byte) (normal.x * 255)); // Red
					data.put((byte) (normal.y * 255)); // Green
					data.put((byte) (normal.z * 255)); // Blue
					data.put((byte) 255);              // Alpha of 1.0
				}
			}
			
			data.flip();
			return data;
			
		}
		catch (Exception e) {
			System.err.println("Error loading image file \"" + fileName + "\"");
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
	public static ByteBuffer createNoiseMap(int width, int height, int depth) {
		ByteBuffer data       = Util.createByteBuffer((width * height) * 4);
//		int numOctaves        = 4;
		
		int z = depth / 2;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float n = PerlinNoise3f.perlinNoise(x / 70.f, y / 70.f, 1, 1.0f, 6);
				n = (n * 0.5f) + 0.5f;
				byte noise = (byte) (n * 255);
				
				data.put((byte) (noise)); // Red
				data.put((byte) (noise)); // Green
				data.put((byte) (noise)); // Blue
				data.put((byte) 255);     // Alpha of 1.0
			}
		}
		
		data.flip();
		return data;
	}
	
	public static ByteBuffer createNoiseNormalMap(int width, int height, int depth) {
		ByteBuffer data = Util.createByteBuffer((width * height) * 4);
		float e = 0.000001f;
		int octaves = 5;
		
		int z = depth / 2;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				float sx = x / 70.0f;
				float sy = y / 70.0f;
				float sz = 1;
				float n = PerlinNoise3f.perlinNoise(sx, sy, sz, 1.0f, octaves);
				
				float dx = PerlinNoise3f.perlinNoise(sx + e, sy, sz, 1.0f, octaves) - n;
				float dy = PerlinNoise3f.perlinNoise(sx, sy + e, sz, 1.0f, octaves) - n;
				float dz = PerlinNoise3f.perlinNoise(sx, sy, sz + e, 1.0f, octaves) - n;
				Vector3f normal = new Vector3f(dx, dy, dz).normalize();
				
				data.put((byte) (normal.x * 255)); // Red
				data.put((byte) (normal.y * 255)); // Green
				data.put((byte) (normal.z * 255)); // Blue
				data.put((byte) 255);     // Alpha of 1.0
			}
		}
		
		data.flip();
		return data;
	}
	
	
	public static DynamicAnimation createAnimationStrip(Texture[][] sheet, int startX, int startY, int length, int numVariants) {
		DynamicAnimation anim = new DynamicAnimation(numVariants);
		int width = sheet.length;

		for (int i = 0; i < numVariants; i++)
			anim.setVariant(i, new SpriteAnimation());
		
		int x = startX;
		int y = startY;
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < numVariants; j++) {
				anim.getVariant(j).addFrame(sheet[x][y]);
				
				x++;
				if (x >= width) {
					x -= width;
					y++;
				}
			}
		}
		
		return anim;
	}
	
	
	public static DynamicAnimation createAnimationStrip2(Texture[][] sheet, int startX, int startY, int length, int numVariants) {
		DynamicAnimation anim = new DynamicAnimation(numVariants);
		int width = sheet.length;

		for (int i = 0; i < numVariants; i++)
			anim.setVariant(i, new SpriteAnimation());
		
		int x = startX;
		int y = startY;
		for (int j = 0; j < numVariants; j++) {
			for (int i = 0; i < length; i++) {
				anim.getVariant(j).addFrame(sheet[x][y]);
				
				x++;
				if (x >= width) {
					x -= width;
					y++;
				}
			}
		}
		
		return anim;
	}
}
