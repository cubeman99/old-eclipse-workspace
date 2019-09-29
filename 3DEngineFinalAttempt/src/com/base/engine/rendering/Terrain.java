package com.base.engine.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Util;
import com.base.engine.core.Vertex;

public class Terrain {
	/** Load a terrain mesh from the given heightmap. **/
	public static Mesh loadTerrian(String fileName) {
		try {
    		BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
    		int width    = image.getWidth();
    		int height   = image.getHeight();
    		int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
    		Vertex[] vertices = new Vertex[width * height];
    		int[] indeces     = new int[(width - 1) * (height - 1) * 6];
    		float[][] heightmap = new float[width][height];
    		

    		for (int i = 0; i < width * height; i++) {
    			heightmap[i % width][i / width] = ((pixels[i] & 0xFF) / 255.0f);
    		}
    		
    		smoothHeightMap(heightmap, width, height, 0.75f, 5);
    		
    		for (int i = 0; i < width * height; i++) {
    			int x        = i % width;
    			int y        = i / width;
    			float h      = heightmap[x][y];
    			Vector3f pos = new Vector3f(x / (float) width, h, y / (float) height);
    			vertices[i]  = new Vertex(pos, new Vector2f(pos.x * 100, pos.z * -100));
    			
    			if (x < width - 2 && y < height - 2) {
//    	    		System.out.println("x = " + x);
//    	    		System.out.println("y = " + y);
    				int index = (y * width * 6) + (x * 6);
    				indeces[index + 0] = i;
    				indeces[index + 1] = i + width;
    				indeces[index + 2] = i + 1;

    				indeces[index + 3] = i + 1;
    				indeces[index + 4] = i + width;
    				indeces[index + 5] = i + width + 1;
    			}
    		}
    		
    		return new Mesh(vertices, indeces, true, true);
		}
		catch (Exception e) {
			System.err.println("Error loading heightmap \"" + fileName + "\"");
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
	}
	
	private static void smoothHeightMap(float[][] heightmap, int width, int height, float smooth, int numPasses) {
		for (int i = 0; i < numPasses; i++) {
    		/* Rows, left to right */
    		for(int x = 1; x < width; x++)
    		    for (int y = 1; y < height; y++)
    			heightmap[x][y] = heightmap[x-1][y] * (1-smooth) + 
    					heightmap[x][y] * smooth;
    
    		/* Rows, right to left*/
    		for(int x = width - 2; x >= 0; x--)
    		    for (int y = 1; y < height; y++)
    		    	heightmap[x][y] = heightmap[x+1][y] * (1-smooth) + 
    		    			heightmap[x][y] * smooth;
    
    		/* Columns, bottom to top */
    		for(int x = 0; x < width; x++)
    		    for (int y = 1; y < height; y++)
    		    	heightmap[x][y] = heightmap[x][y-1] * (1-smooth) + 
    				      heightmap[x][y] * smooth;
    
    		/* Columns, top to bottom */
    		for(int x = 0; x < width; x++)
    		    for (int y = height - 2; y >= 0; y--)
    			heightmap[x][y] = heightmap[x][y+1] * (1-smooth) + 
    				      heightmap[x][y] * smooth;
    	}
	}
}
