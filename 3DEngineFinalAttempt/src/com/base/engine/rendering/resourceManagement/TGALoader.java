package com.base.engine.rendering.resourceManagement;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import com.base.engine.core.Util;

/**
 * A utility to load TGAs.
 */
public class TGALoader {
	/**
	 * Flip the endian-ness of the short
	 * 
	 * @param signedShort The short to flip
	 * @return The flipped short
	 */
	private static short flipEndian(short signedShort) {
		int input = signedShort & 0xFFFF;
		return (short) (input << 8 | (input & 0xFF00) >>> 8);
	}
	
	
	/**
	 * Load the given file as a .tga image.
	 */
	public static Image loadImage(String fileName) {
		try {
    		byte red, green, blue;
    		FileInputStream fis     = new FileInputStream(new File(fileName));
    		BufferedInputStream bis = new BufferedInputStream(fis, 100000);
    		DataInputStream dis     = new DataInputStream(bis);
    
    		// Read in the header.
    		short idLength     = (short) dis.read();
    		short colorMapType = (short) dis.read();
    		short imageType    = (short) dis.read();
    		short cMapStart    = flipEndian(dis.readShort());
    		short cMapLength   = flipEndian(dis.readShort());
    		short cMapDepth    = (short) dis.read();
    		short xOffset      = flipEndian(dis.readShort());
    		short yOffset      = flipEndian(dis.readShort());
    		int width          = flipEndian(dis.readShort());
    		int height         = flipEndian(dis.readShort());
    		short pixelDepth   = (short) dis.read();
    		short imageDescriptor = (short) dis.read();
    		
    		// Skip image ID.
    		if (idLength > 0) {
    			bis.skip(idLength);
    		}
    		
    		// Read Image Colors.
    		ByteBuffer data  = Util.createByteBuffer((width * height) * 4);
    		boolean hasAlpha = (pixelDepth == 32);
    		
    		System.out.println("Loading " + fileName + " (pixelDepth = " + pixelDepth + ")");
    		
    		for (int i = 0; i < width * height; i++) {
    			blue  = dis.readByte();
    			green = dis.readByte();
    			red   = dis.readByte();
    			
    			data.put(red); // Red
    			data.put(green); // Green
    			data.put(blue); // Blue
    			
    			if (hasAlpha)
    				data.put(dis.readByte()); // Alpha value
    			else	
    				data.put((byte) 0xFF); // Alpha of 1.0
    		}
    		data.flip();
    		
    		
    		fis.close();
    		return new Image(data, width, height);
		}
		catch (IOException e) {
			System.err.println("Error loading image " + fileName);
			System.exit(1);
		}
		return null;
	}
}

