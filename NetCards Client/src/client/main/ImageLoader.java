package client.main;

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;


public class ImageLoader {
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();

	
	public static void loadAllImages() {
		// LOAD ALL IMAGES HERE:
		
		loadImage("cardWhite",			"cardWhite.png");
		loadImage("cardBlack",			"cardBlack.png");
		loadImage("cardSlot",			"cardSlot.png");
		loadImage("playerHolderSlot",	"playerHolderSlot.png");
	}
	

	public static Image getImage(String imageName) {
		return imageMap.get(imageName);
	}
	
	public static void loadImage(String imagePath) {
		loadImage(imagePath, imagePath);
	}
	
	public static void loadImage(String imageName, String imagePath) {
		Image img = new ImageIcon(ImageLoader.class.getResource("/images/" + imagePath)).getImage();
		imageMap.put(imageName, img);
	}
}