package main;

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;


/**
 * A static class that handles loading and
 * storing images from the 'images' folder.
 * 
 * @author David
 */
public class ImageLoader {
	private static final int DEFAULT_SPRITE_WIDTH        = 16;
	private static final int DEFAULT_SPRITE_HEIGHT       = 16;
	private static HashMap<String, Image> imageMap       = new HashMap<String, Image>();

	
	public static void loadAllImages() {
		// LOAD ALL IMAGES HERE:
		
	}
	
	
	/** Find the image with the given name. **/
	public static Image getImage(String imageName) {
		return imageMap.get(imageName);
	}
	
	
	/** Load an image and store it under a given name in a hash map. **/
	private static Image loadImage(String imageName, String imagePath) {
		Image img = new ImageIcon(ImageLoader.class.getResource("/images/" + imagePath)).getImage();
		imageMap.put(imageName, img);
		return img;
	}

}