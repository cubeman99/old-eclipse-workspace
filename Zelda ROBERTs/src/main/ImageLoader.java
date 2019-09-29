package main;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A static class that handles loading and storing images from the 'images' folder.
 * 
 * @author	Robert Jordan
 * @author	David Jordan
 */
public class ImageLoader {

	// ================== Private Variables ===================
	
	/** The number of images loaded. This is used for console output. */
	private static int imagesLoaded = 0;
	/** The map containing all the loaded images. */
	private static HashMap<String, HashMap<String, ImageResource>> imageMap = new HashMap<String, HashMap<String, ImageResource>>();

	// =================== Program Loading ====================
	
	/** Preload any images that need to be stored before the program is initialized. */
	static void preloadImages() {
		// PRELOAD ALL IMAGES HERE:
		// example: loadImage("image1", "imagetest1.png");

		// Output console information:
		imagesLoaded = 0;
		System.out.println("Loading Images:");
		
		// Load the application icons:
		loadImage("icon16", "icon16.png", true, true, null);
		loadImage("icon32", "icon32.png", true, true, null);
		loadImage("icon48", "icon48.png", true, true, null);


		loadImage("test_tiles", "tiles/test_tiles.png", true, true, new Color(176, 48, 0));
		loadImage("landforms", "tiles/overworld/landforms.png", true, true, new Color(176, 48, 0));
		loadImage("special", "tiles/overworld/special.png", true, true, new Color(176, 48, 0));
		loadImage("water", "tiles/overworld/water.png", true, true, new Color(176, 48, 0));
		
		loadImage("font_small", "ui/font_small.png", true, true, new Color(176, 48, 0));
		loadImage("font_large", "ui/font_large.png", true, true, new Color(176, 48, 0));
		loadImage("hud_elements", "ui/hud_elements.png", true, true, new Color(176, 48, 0));
		loadImage("items", "ui/items.png", true, true, new Color(176, 48, 0));
		loadImage("weapon_menu", "ui/weapon_menu2.png", true, true, null);
		loadImage("key_item_menu", "ui/key_item_menu2.png", true, true, null);

		loadImage("menu_weapons_a", "ui/menu_weapons_a.png", true, true, null);
		loadImage("menu_weapons_b", "ui/menu_weapons_b.png", true, true, null);
		loadImage("menu_key_items_a", "ui/menu_key_items_a.png", true, true, null);
		loadImage("menu_key_items_b", "ui/menu_key_items_b.png", true, true, null);
		loadImage("menu_essences_a", "ui/menu_essences_a.png", true, true, null);
		loadImage("menu_essences_b", "ui/menu_essences_b.png", true, true, null);

		loadImage("items_small", "ui/items_small.png", true, true, new Color(176, 48, 0));
		loadImage("items_large", "ui/items_large.png", true, true, new Color(176, 48, 0));
		loadImage("menu_small", "ui/menu_small.png", true, true, new Color(176, 48, 0));
		loadImage("menu_large", "ui/menu_large.png", true, true, new Color(176, 48, 0));
		
		loadImage("items_small_light", "ui/items_small_light.png", true, true, new Color(176, 48, 0));
		loadImage("items_large_light", "ui/items_large_light.png", true, true, new Color(176, 48, 0));
		loadImage("menu_small_light", "ui/menu_small_light.png", true, true, new Color(176, 48, 0));
		loadImage("menu_large_light", "ui/menu_large_light.png", true, true, new Color(176, 48, 0));
		
		loadImage("link_normal", "sprites/link_normal.png", true, true, new Color(176, 48, 0));
		loadImage("link_normal_red", "sprites/link_normal_red.png", true, true, new Color(176, 48, 0));
		loadImage("link_normal_blue", "sprites/link_normal_blue.png", true, true, new Color(176, 48, 0));
		
		loadImage("weapons", "sprites/weapons.png", true, true, new Color(176, 48, 0));
		loadImage("effects", "sprites/effects.png", true, true, new Color(176, 48, 0));
		loadImage("special_effects", "sprites/special_effects.png", true, true, new Color(176, 48, 0));
		loadImage("physical_effects", "sprites/physical_effects.png", true, true, new Color(176, 48, 0));

		// Output console information:
		System.out.println("");
		System.out.println("Loaded " + String.valueOf(imagesLoaded) + " images");
		System.out.println("--------------------------------");
	}
	/** Load the rest of the images for the program. */
	static void loadImages() {
		// PRELOAD ALL IMAGES HERE:
		// example: loadImage("image1", "imagetest1.png");

		// Output console information:
		imagesLoaded = 0;
		System.out.println("Loading Images:");
		
		
		
		// Output console information:
		System.out.println("");
		System.out.println("Loaded " + String.valueOf(imagesLoaded) + " images");
		System.out.println("--------------------------------");
	}

	// =================== Loading Methods ====================

	/**
	 * Finds the image with the given name.
	 * 
	 * @param	name - The name of the image to look for.
	 * @return	Returns the image resource or null if the image is not loaded.
	 */
	public static Image getImage(String name) {
		return getImage(name, "");
	}
	/**
	 * Finds the image with the given name.
	 * 
	 * @param	name - The name of the image to look for.
	 * @param	group - The resource group of the image.
	 * @return	Returns the image resource or null if the image is not loaded.
	 */
	public static Image getImage(String name, String group) {
		if (imageMap.containsKey(group)) {
			if (imageMap.get(group).containsKey(name)) {
				return imageMap.get(group).get(name).getResource();
			}
		}
		return null;
	}
	/**
	 * Tests whether the image with the specified name has been loaded.
	 * 
	 * @param	name - The name of the image to look for.
	 * @return	Returns true if the image has been loaded.
	 */
	public static boolean isImageLoaded(String name) {
		return isImageLoaded(name, "");
	}
	/**
	 * Tests whether the image with the specified name has been loaded.
	 * 
	 * @param	name - The name of the image to look for.
	 * @param	group - The resource group of the image.
	 * @return	Returns true if the image has been loaded.
	 */
	public static boolean isImageLoaded(String name, String group) {
		if (imageMap.containsKey(group)) {
			if (imageMap.get(group).containsKey(name)) {
				return imageMap.get(group).get(name).isLoaded();
			}
		}
		return false;
	}
	/**
	 * Loads the image with the specified name.
	 * 
	 * @param	name - The name of the image to load.
	 */
	public static void loadImage(String name) {
		loadImage(name, "");
	}
	/**
	 * Loads the image with the specified name.
	 * 
	 * @param	name - The name of the image to load.
	 * @param	group - The resource group of the image.
	 */
	public static void loadImage(String name, String group) {
		if (imageMap.containsKey(group)) {
			if (imageMap.get(group).containsKey(name)) {
				imageMap.get(group).get(name).load();
			}
		}
	}
	/**
	 * Loads all the image in specified group.
	 * 
	 * @param	group - The resource group to load.
	 */
	public static void loadGroup(String group) {
		if (imageMap.containsKey(group)) {
			for (ImageResource resource : imageMap.get(group).values()) {
				resource.load();
			}
		}
	}
	/**
	 * Unloads the image with the specified name.
	 * 
	 * @param	name - The name of the image to unload.
	 */
	public static void unloadImage(String name) {
		unloadImage(name, "");
	}
	/**
	 * Unloads the image with the specified name.
	 * 
	 * @param	name - The name of the image to unload.
	 * @param	group - The resource group of the image.
	 */
	public static void unloadImage(String name, String group) {
		if (imageMap.containsKey(group)) {
			if (imageMap.get(group).containsKey(name)) {
				imageMap.get(group).get(name).unload();
			}
		}
	}
	/**
	 * Unloads all the image in specified group.
	 * 
	 * @param	group - The resource group to unload.
	 */
	public static void unloadGroup(String group) {
		if (imageMap.containsKey(group)) {
			for (ImageResource resource : imageMap.get(group).values()) {
				resource.unload();
			}
		}
	}

	// =================== Private Methods ====================
	
	/**
	 * Load an image and stores it under a given name in the hash map.
	 * 
	 * @param	name - The name to store the image with.
	 * @param	path - The path to the image.
	 * @param	isResource - True if the image should be loaded as a resource.
	 * @param	load - True if the image should be loaded now.
	 */
	private static void loadImage(String name, String path, boolean isResource, boolean load, Color filter) {
		loadImage(name, "", path, isResource, load, filter);
	}
	/**
	 * Load an image and stores it under a given name in the hash map.
	 * 
	 * @param	name - The name to store the image with.
	 * @param	group - The group to store the image with.
	 * @param	path - The path to the image.
	 * @param	isResource - True if the image should be loaded as a resource.
	 * @param	load - True if the image should be loaded now.
	 */
	private static void loadImage(String name, String group, String path, boolean isResource, boolean load, Color filter) {
		ImageResource resource = new ImageResource(path, isResource, filter);
		if (load)
			resource.load();
		
		// Add the image to the hash map:
		// Create a new hash map group if one doesn't exist
		if (!imageMap.containsKey(group)) {
			imageMap.put(group, new HashMap<String, ImageResource>());
		}
		imageMap.get(group).put(name, resource);
		
		// Output the loaded image path to the console
		System.out.println("- " + path);
		imagesLoaded++;
	}
	/**
	 * Load all the images with a given path and stores them under a given
	 * name in the hash map.
	 * 
	 * @param	name - The name to store the images with.
	 * @param	path - The path to the images.
	 * @param	excludePath - The continued path to exclude from the search.
	 * @param	isResource - True if the image should be loaded as a resource.
	 * @param	load - True if the image should be loaded now.
	 */
	private static void loadAllImages(String name, String path, String excludePath, boolean isResource, boolean load, Color filter) {
		loadAllImages(name, "", path, excludePath, isResource, load, filter);
	}
	/**
	 * Load all the images with a given path and stores them under a given
	 * name in the hash map.
	 * 
	 * @param	name - The name to store the images with.
	 * @param	group - The group to store the images with.
	 * @param	path - The path to the images.
	 * @param	excludePath - The continued path to exclude from the search.
	 * @param	isResource - True if the image should be loaded as a resource.
	 * @param	load - True if the image should be loaded now.
	 */
	private static void loadAllImages(String name, String group, String path, String excludePath, boolean isResource, boolean load, Color filter) {
		
		String imagePath = "resources/images/" + path;
		ArrayList<String> images = new ArrayList<String>();
		
		// Load all png's and jpg's:
		images.addAll(ResourceLoader.getResources("resources/images/" + path, excludePath, ".jpg", true));
		images.addAll(ResourceLoader.getResources("resources/images/" + path, excludePath, ".png", true));
		
		// For each image resource found, load the image:
		for (String fullPath : images) {
			// Get the index of the start of the path
			int index = fullPath.lastIndexOf("resources/images/" + path);
			
			String finalPath = fullPath.substring(index + imagePath.length(), fullPath.length());
			String finalName = name + finalPath.substring(0, finalPath.length() - 4);
			
			loadImage(finalName, group, path + finalPath, isResource, load, filter);
		}
	}
}