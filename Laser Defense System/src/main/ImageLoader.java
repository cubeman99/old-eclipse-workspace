package main;

import java.awt.Image;
import java.util.HashMap;
import javax.swing.ImageIcon;
import common.graphics.Sprite;
import common.graphics.SpriteSheet;


/**
 * A static class that handles loading and
 * storing images from the 'images' folder.
 * 
 * @author David
 */
public class ImageLoader {
	private static HashMap<String, Image> imageMap       = new HashMap<String, Image>();
	private static HashMap<String, SpriteSheet> sheetMap = new HashMap<String, SpriteSheet>();

	
	public static void loadAllImages() {
		// LOAD ALL IMAGES HERE:
		loadImage("playerShip", "playerShip.png");
		loadImage("planet", "planet.png");
		loadImage("hudPanelHurt", "hudPanelHurt.png");
		
		loadSpriteSheet("nodes", "nodes.png", 64, 64);
		loadSpriteSheet("lasers", "lasers.png", 32, 32);
		loadSpriteSheet("miniShip", "miniShip.png", 64, 64);
		loadSpriteSheet("nodeDeployer", "nodeDeployer.png", 128, 128);
		loadSpriteSheet("nodeMover", "nodeMover.png", 64, 64);
		loadSpriteSheet("nodeMoverLeft", "nodeMoverLeft.png", 64, 64);
		loadSpriteSheet("nodeMoverRight", "nodeMoverRight.png", 64, 64);
		loadSpriteSheet("powerUpIcons", "powerUpIcons.png", 13, 13);
		loadSpriteSheet("nodeJunk", "nodeJunk.png", 48, 48);
		loadSpriteSheet("miniShipJunk", "miniShipJunk.png", 64, 64);
		loadSpriteSheet("missile", "missile.png", 64, 64);
		loadSpriteSheet("chargeBlastEffect", "chargeBlastEffect.png", 48, 48);
		loadSpriteSheet("hudPanel", "hudPanel.png", 230, 110);
		loadSpriteSheet("explosionCloud", "explosionCloud.png", 32, 32);
	}
	
	
	/** Find the image with the given name. **/
	public static Image getImage(String imageName) {
		return imageMap.get(imageName);
	}
	
	/** Get the sprite of given sub-image on the named sprite sheet. **/
	public static Sprite getSprite(String sheetName, int subImage) {
		return sheetMap.get(sheetName).getSprite(subImage);
	}
	
	public static SpriteSheet getSpriteSheet(String sheetName) {
		return sheetMap.get(sheetName);
	}
	
	/** Load an image and store it under a given name in a hash map. **/
	private static Image loadImage(String imageName, String imagePath) {
		Image img = new ImageIcon(ImageLoader.class.getResource("/images/" + imagePath)).getImage();
		imageMap.put(imageName, img);
		return img;
	}

	/** Load an sprite sheet and store it under a given name in a hash map. **/
	private static SpriteSheet loadSpriteSheet(String sheetName, String imagePath, int spriteWidth, int spriteHeight) {
		SpriteSheet sheet = new SpriteSheet(loadImage(sheetName, imagePath), spriteWidth, spriteHeight);
		sheetMap.put(sheetName, sheet);
		return sheet;
	}
}