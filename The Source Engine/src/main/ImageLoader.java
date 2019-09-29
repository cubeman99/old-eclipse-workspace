package main;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import javax.swing.ImageIcon;
import sun.tools.jar.resources.jar;


/**
 * A static class that handles loading and
 * storing images from the 'images' folder.
 * 
 * @author David
 */
public class ImageLoader {
	private static HashMap<String, Image> imageMap = new HashMap<String, Image>();
	
	
	/** Preload all necessary images; storing them in the hash map. **/
	public static void loadAllImages() {
		// LOAD ALL IMAGES HERE:
		MediaTracker tracker = new MediaTracker(Main.frame);
		
		loadImage("crosshair", "crosshair.png");
		
		loadImage("select", "select.png");
		loadImage("vertex", "vertex.png");
		loadImage("line", "line.png");
		loadImage("rectangle", "rectangle.png");
		loadImage("polygon", "polygon.png");
		
		loadImage("handleRotate", "handleRotate.png");
		
		loadImage("buttonBarBackground", "buttonBarBackground.png");
//		loadImage("iconSave", "iconSave.png");

		loadImage("sun", "sun.png");
		loadImage("moon", "moon.png");
		
		loadImage("mercury", "mercury.png");
		loadImage("venus", "venus.png");
		loadImage("earth", "earth.png");
		loadImage("mars", "mars.png");
		loadImage("jupiter", "jupiter.png");
		loadImage("saturn", "saturn.png");
		loadImage("uranus", "uranus.png");
		loadImage("neptune", "neptune.png");
		loadImage("pluto", "pluto.png");
		
		// Wait for all images to load:
		try {
			tracker.waitForAll();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** Find the image with the given name. **/
	public static Image getImage(String imageName) {
		return imageMap.get(imageName);
	}
	
	public static Cursor makeCursor(String imageName, Point hotspot) {
		return Toolkit.getDefaultToolkit().createCustomCursor(ImageLoader.getImage(imageName), hotspot, imageName);
	}
	
	public static Cursor makeCursor(String imageName, int hotspotX, int hotspotY) {
		return makeCursor(imageName, new Point(hotspotX, hotspotY));
	}
	
	public static Image loadNewImage(String imagePath) {
//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		URL imageURL   = classLoader.getResource("/images/" + imagePath);
//		Image newImage = Toolkit.getDefaultToolkit().getImage(imageURL);
//		return newImage;
		return new ImageIcon(ImageLoader.class.getResource("/images/" + imagePath)).getImage();
	}
	
	/** Load an image and store it under a given name in the hash map. **/
	private static void loadImage(String imageName, String imagePath) {
		Image img = loadNewImage(imagePath);
		imageMap.put(imageName, img);
	}
}