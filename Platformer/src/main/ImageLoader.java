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
	private static final int DEFAULT_SPRITE_WIDTH        = 16;
	private static final int DEFAULT_SPRITE_HEIGHT       = 16;
	private static HashMap<String, Image> imageMap       = new HashMap<String, Image>();
	private static HashMap<String, SpriteSheet> sheetMap = new HashMap<String, SpriteSheet>();

	
	public static void loadAllImages() {
		// LOAD ALL IMAGES HERE:
		
		loadSheet("testSheet", "tileset1.png", 1);
		loadSheet("player", "player.png", 32, 32);
		loadSheet("effectJetPackFlame", "effectJetPackFlame.png", 8, 8);
	}
	
	
	/** Find the image with the given name. **/
	public static Image getImage(String imageName) {
		return imageMap.get(imageName);
	}
	
	/** Get the sprite of given sub-image on the named sprite sheet. **/
	public static Sprite getSprite(String sheetName, int sx, int sy) {
		return sheetMap.get(sheetName).getSprite(sx, sy);
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
	private static SpriteSheet loadSheet(String sheetName, String imagePath, int spriteWidth, int spriteHeight, int seperation) {
		SpriteSheet sheet = new SpriteSheet(loadImage(sheetName, imagePath), spriteWidth, spriteHeight, seperation);
		sheetMap.put(sheetName, sheet);
		return sheet;
	}

	/** Load an sprite sheet and store it under a given name in a hash map. **/
	private static SpriteSheet loadSheet(String sheetName, String imagePath, int spriteWidth, int spriteHeight) {
		SpriteSheet sheet = new SpriteSheet(loadImage(sheetName, imagePath), spriteWidth, spriteHeight, 0);
		sheetMap.put(sheetName, sheet);
		return sheet;
	}
	
	/** Load an sprite sheet and store it under a given name in a hash map. **/
	private static SpriteSheet loadSheet(String sheetName, String imagePath) {
		return loadSheet(sheetName, imagePath, DEFAULT_SPRITE_WIDTH, DEFAULT_SPRITE_HEIGHT, 0);
	}
	
	/** Load an sprite sheet and store it under a given name in a hash map. **/
	private static SpriteSheet loadSheet(String sheetName, String imagePath, int seperation) {
		return loadSheet(sheetName, imagePath, DEFAULT_SPRITE_WIDTH, DEFAULT_SPRITE_HEIGHT, seperation);
	}
}