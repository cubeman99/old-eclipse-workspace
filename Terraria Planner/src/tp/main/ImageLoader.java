package tp.main;

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
		
		loadImage("backgroundSky", "backgroundSky.png");
		loadImage("backgroundDirt", "backgroundDirt.png");
		loadImage("itemIcons", "Item Icons.png");
		loadImage("itemGroupIcons", "Item Group Icons.png");
		loadImage("slotBackingBlue", "slotBackingBlue.png");
		loadImage("slotBackingSelect", "slotBackingSelect.png");
		loadImage("buttonOverlay", "buttonOverlay.png");
		loadImage("hudButtonPanel", "hudButtonPanel.png");
		loadImage("hudToolPanelMid", "hudToolPanelMid.png");
		loadImage("hudButtonIcons", "hudButtonIcons.png");
		loadImage("hudItemPanelSide", "hudItemPanelSide.png");
		loadImage("hudItemPanelGridBorder", "hudItemPanelGridBorder.png");
		loadImage("hudItemPanelGroupBottom", "hudItemPanelGroupBottom.png");
	}
	
	
	/** Find the image with the given name. **/
	public static Image getImage(String imageName) {
		return imageMap.get(imageName);
	}
	
	/** Load a single image **/
	public static Image loadImage(String imagePath) {
		try {
			return new ImageIcon(ImageLoader.class.getResource("/tp/images/" + imagePath)).getImage();
		}
		catch (NullPointerException e) {
			System.out.println("Unable to load image \"" + imagePath + "\"");
			return null;
		}
	}
	
	/** Load an image and store it under a given name in a hash map. **/
	private static Image loadImage(String imageName, String imagePath) {
		Image img = new ImageIcon(ImageLoader.class.getResource("/tp/images/" + imagePath)).getImage();
		imageMap.put(imageName, img);
		return img;
	}
}