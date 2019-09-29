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
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();

	
	public static void loadAllImages() {
		// LOAD ALL IMAGES HERE:
		// example: loadImage("image1", "imagetest1.png");
		
	}
	
	
	/** Find the image with the given name. **/
	public static Image getImage(String imageName) {
		return imageMap.get(imageName);
	}

	/** Load an image and store it under a given name in the hash map. **/
	private static void loadImage(String imageName, String imagePath) {
		Image img = new ImageIcon(ImageLoader.class.getResource("/images/" + imagePath)).getImage();
		imageMap.put(imageName, img);
	}
}