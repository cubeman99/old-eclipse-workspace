package main;

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;


public class ImageLoader {
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();

	
	public static void loadAllImages() {
		// LOAD ALL IMAGES HERE:
		loadImage("wire",			"wires.png");
		loadImage("sourceTrue",		"source_true.png");
		loadImage("sourceFalse",	"source_false.png");
		loadImage("sourceExtender",	"source_extender.png");
		loadImage("gateNot",		"gateNot.png");
		loadImage("gateAnd",		"gateAnd.png");
		loadImage("gateOr",			"gateOr.png");
		loadImage("sourceLever",	"sourceLever.png");
		
		
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