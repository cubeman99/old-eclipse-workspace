package main;

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;


public class ImageLoader {
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();

	
	public static void loadAllImages() {
		// LOAD ALL IMAGES HERE:
		// example: loadImage("image1", "imagetest1.png");

		loadImage("gateNot",		"gates/gateNot.png");
		loadImage("gateAnd",		"gates/gateAnd.png");
		loadImage("gateNand",		"gates/gateNand.png");
		loadImage("gateOr",			"gates/gateOr.png");
		loadImage("gateNor",		"gates/gateNor.png");
		loadImage("gateXor",		"gates/gateXor.png");
		loadImage("gateXnor",		"gates/gateXNor.png");
		
		loadImage("gateAdd",		"gates/gateAdd.png");
		loadImage("gateSubtract",	"gates/gateSubtract.png");
		loadImage("gateMultiply",	"gates/gateMultiply.png");
		loadImage("gateDivide",		"gates/gateDivide.png");
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