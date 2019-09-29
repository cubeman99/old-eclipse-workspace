package com.base.engine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;

import com.base.engine.core.Util;
import com.base.engine.rendering.resourceManagement.Image;
import com.base.engine.rendering.resourceManagement.ResourceManager;
import com.base.engine.rendering.resourceManagement.TGALoader;
import com.base.engine.rendering.resourceManagement.TextureResource;


public class Texture {
	private static HashMap<String, TextureResource> loadedTextures = new HashMap<String, TextureResource>();
	public static final Texture defaultTexture = new Texture("unknown_texture.jpg");
	
	private TextureResource resource;
	
	public static int DEFAULT_FILTERING = GL_LINEAR_MIPMAP_LINEAR;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Texture(String fileName) {
    	this(fileName, GL_TEXTURE_2D, DEFAULT_FILTERING, GL_RGBA, GL_RGBA, false, GL_NONE);
    }
	
	public Texture(int width, int height, ByteBuffer data) {
		this(width, height, data, GL_TEXTURE_2D, DEFAULT_FILTERING, GL_RGBA, GL_RGBA, false, GL_NONE);
    }
	
	public Texture(String fileName, boolean isCubeMap, boolean multipleSides)
	{

		TextureResource loadedTextureResource = loadedTextures.get(fileName);
		
		if (loadedTextureResource != null) {
			resource = loadedTextureResource;
			resource.addReference();
		}
		else {
			// Load the texture from the file.
    		try {
    			int width  = 0;
    			int height = 0;
    			
    			
    			BufferedImage image = ImageIO.read(ResourceManager.createFile(fileName, ResourceManager.TEXTURE_DIRECTORY));
    			width            = image.getWidth();
    			height           = image.getHeight();
    			int[] pixels     = image.getRGB(0, 0, width, height, null, 0, width);
    			ByteBuffer data  = Util.createByteBuffer((width * height) * 4);
    			boolean hasAlpha = image.getColorModel().hasAlpha();
    			
    			for (int i = 0; i < width * height; i++) {
					int pixel = pixels[i];
					data.put((byte) ((pixel >> 16) & 0xFF)); // Red
					data.put((byte) ((pixel >>  8) & 0xFF)); // Green
					data.put((byte) ((pixel      ) & 0xFF)); // Blue
					
					if (hasAlpha)
						data.put((byte) ((pixel >> 24) & 0xFF)); // Alpha value
					else	
						data.put((byte) 0xFF); // Alpha of 1.0
    			}
    			data.flip();
    			
    			ByteBuffer[] dataMap = new ByteBuffer[6];
    			for (int i = 0; i < 6; i++)
    				dataMap[i] = data.duplicate();
    			
    			resource = new TextureResource(GL_TEXTURE_CUBE_MAP, width, height, 1,
    					dataMap, new float[] {GL_LINEAR}, new int[] {GL_RGBA},
    					new int[] {GL_RGBA}, false, new int[] {GL_NONE});
    		}
    		catch (Exception e) {
    			System.err.println("Error loading texture \"" + fileName + "\"");
    			e.printStackTrace();
    			System.exit(1);
    		}
    
			loadedTextures.put(fileName, resource);
    	}
	}
	
	public Texture(String fileName, boolean isCubeMap)
	{

		TextureResource loadedTextureResource = loadedTextures.get(fileName);

        String[] prefix = new String[] {
        	/*
    		"front_", // x+
    		"back_",  // x-
    		"right_", // y+ 
    		"left_",  // y-
    		"top_",   // z+
    		"bottom_" // z-
    		*/
        	"left_",  // y-
    		"right_", // y+ 
    		"top_",   // z+
    		"bottom_", // z-
        	"front_", // x+
        	"back_",  // x-
		};
		
		if (loadedTextureResource != null) {
			resource = loadedTextureResource;
			resource.addReference();
		}
		else {
			// Load the texture from the file.
    		try {
    			ByteBuffer[] data = new ByteBuffer[6];
    			int width  = 0;
    			int height = 0;
    			
    			for (int map = 0; map < 6; map++) {
        			BufferedImage image = ImageIO.read(ResourceManager.createFile(prefix[map] + fileName, ResourceManager.TEXTURE_DIRECTORY));
        			width            = image.getWidth();
        			height           = image.getHeight();
        			int[] pixels     = image.getRGB(0, 0, width, height, null, 0, width);
        			data[map]        = Util.createByteBuffer((width * height) * 4);
        			boolean hasAlpha = image.getColorModel().hasAlpha();
        			
        			for (int i = 0; i < width * height; i++) {
    					int pixel = pixels[i];
    					data[map].put((byte) ((pixel >> 16) & 0xFF)); // Red
    					data[map].put((byte) ((pixel >>  8) & 0xFF)); // Green
    					data[map].put((byte) ((pixel      ) & 0xFF)); // Blue
    					
    					if (hasAlpha)
    						data[map].put((byte) ((pixel >> 24) & 0xFF)); // Alpha value
    					else	
    						data[map].put((byte) 0xFF); // Alpha of 1.0
        			}
        			data[map].flip();
    			}

    			resource = new TextureResource(GL_TEXTURE_CUBE_MAP, width, height, 1,
    					data, new float[] {GL_LINEAR}, new int[] {GL_RGBA},
    					new int[] {GL_RGBA}, false, new int[] {GL_NONE});
    		}
    		catch (Exception e) {
    			System.err.println("Error loading texture \"" + fileName + "\"");
    			e.printStackTrace();
    			System.exit(1);
    		}
    
			loadedTextures.put(fileName, resource);
    	}
	}
	
	public Texture(int width, int height, ByteBuffer data,
			int textureTarget, float filter, int internalFormat,
			int format, boolean clamp, int attachment)
	{
		resource = new TextureResource(textureTarget, width, height, 1,
				new ByteBuffer[] {data},
				new float[] {filter},
				new int[] {internalFormat},
				new int[] {format},
				clamp,
				new int[] {attachment});
	}

    public Texture(String fileName, int textureTarget, float filter,
    		int internalFormat, int format, boolean clamp, int attachment)
    {
		TextureResource loadedTextureResource = loadedTextures.get(fileName);

		if (loadedTextureResource != null) {
			resource = loadedTextureResource;
			resource.addReference();
		}
		else {
			// Load the texture from the file.
    		try {
    			String ext = Util.getFileExtension(fileName);
    			ByteBuffer data;
    			int width;
    			int height;
    			
    			if (ext.equalsIgnoreCase("TGA")) {
    				Image image = TGALoader.loadImage(ResourceManager.getPath(fileName, ResourceManager.TEXTURE_DIRECTORY)); 
    				data = image.getData();
    				width = image.getWidth();
    				height = image.getHeight();
    			}
    			else {
        			BufferedImage image = ImageIO.read(ResourceManager.createFile(fileName, ResourceManager.TEXTURE_DIRECTORY));
        			width            = image.getWidth();
        			height           = image.getHeight();
        			int[] pixels     = image.getRGB(0, 0, width, height, null, 0, width);
        			data  = Util.createByteBuffer((width * height) * 4);
        			boolean hasAlpha = image.getColorModel().hasAlpha();
        			
        			for (int i = 0; i < width * height; i++) {
    					int pixel = pixels[i];
    					data.put((byte) ((pixel >> 16) & 0xFF)); // Red
    					data.put((byte) ((pixel >>  8) & 0xFF)); // Green
    					data.put((byte) ((pixel      ) & 0xFF)); // Blue
    					
    					if (hasAlpha)
    						data.put((byte) ((pixel >> 24) & 0xFF)); // Alpha value
    					else	
    						data.put((byte) 0xFF); // Alpha of 1.0
        			}
        			data.flip();
    			}
    			
        		resource = new TextureResource(textureTarget, width, height, 1,
        				new ByteBuffer[] {data},
        				new float[] {filter},
        				new int[] {internalFormat},
        				new int[] {format},
        				clamp,
        				new int[] {attachment});
    		}
    		catch (Exception e) {
    			System.err.println("Error loading texture \"" + fileName + "\"");
    			e.printStackTrace();
    			System.exit(1);
    		}
    
			loadedTextures.put(fileName, resource);
    	}
    }
	
	

	// =================== ACCESSORS =================== //

	public TextureResource getResource() {
		return resource;
	}
	
	public int getWidth() {
		return resource.getWidth();
	}
	
	public int getHeight() {
		return resource.getHeight();
	}
	
	public float getAspectRatio() {
		return resource.getWidth() / (float) resource.getHeight();
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void bind(int samplerSlot) {
		glActiveTexture(GL_TEXTURE0 + samplerSlot);
		resource.bind(0);
	}
	
	public void bindAsRenderTarget() {
		resource.bindAsRenderTarget();
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	protected void finalize() {
		if (resource.removeReference()) {
			loadedTextures.remove(this);
			System.out.println("Removed texture.");
		}
	}
	
	
	
	// ================ STATIC METHODS ================ //
	
//	private static TextureResource loadTexture(String fileName) {
//		try {
//			BufferedImage image = ImageIO.read(new File("./res/textures/" + fileName));
//			int width    = image.getWidth();
//			int height   = image.getHeight();
//			int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
//			
//			ByteBuffer buffer = Util.createByteBuffer((width * height) * 4);
//			boolean hasAlpha = image.getColorModel().hasAlpha();
//			
//			// Put the pixel data into the byte buffer.
//			for (int y = 0; y < height; y++) {
//				for (int x = 0; x < width; x++) {
//					int pixel = pixels[(y * width) + x];
//					
//					buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
//					buffer.put((byte) ((pixel >>  8) & 0xFF)); // Green
//					buffer.put((byte) ((pixel      ) & 0xFF)); // Blue
//					
//					if (hasAlpha)
//						buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha value
//					else	
//						buffer.put((byte) 0xFF); // Alpha of 1.0
//				}
//			}
//			buffer.flip();
//
//			int id = glGenTextures();
//			glBindTexture(GL_TEXTURE_2D, id);
//
//			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
//			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
//
//			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//
//			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
//			
//			return new TextureResource(id);
//		}
//		catch (Exception e) {
//			System.err.println("Error loading texture \"" + fileName + "\"");
//			e.printStackTrace();
//			System.exit(1);
//		}
//		
//		return null;
//	}
}
