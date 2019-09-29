package main;

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;


/**
 * A static class that handles loading and
 * storing images from the 'resources/images' folder.
 * 
 * @author David
 */
public class ImageLoader {
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();
	
	
	/** Preload all necessary images; storing them in the hash map. **/
	public static void loadAllImages() {
		// LOAD ALL IMAGES HERE:
		loadImage("image1", "error1.bmp");
		
	}
	
	/** Find the image with the given name. **/
	public static Image getImage(String imageName) {
		return imageMap.get(imageName);
	}

	/** Load an image and store it under a given name in the hash map. **/
	private static void loadImage(String imageName, String imagePath) {
		Image img = new ImageIcon(ImageLoader.class.getResource("/resources/images/" + imagePath)).getImage();
		imageMap.put(imageName, img);
	}
}