package main;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * A basic image resource that can be stored and loaded or unloaded in
 * order to manage memory. Each resource needs to have a name, path, and
 * boolean of whether it's loaded as a resource, or file.
 * @author	Robert Jordan
 * @see
 * {@linkplain Resource}
 */
public class ImageResource extends Resource {

	// ======================= Members ========================

	/** The image data of the resource. */
	protected Image		image;
	/** The color to filter to empty when loading. */
	protected Color		filter;
	
	// ===================== Constructors =====================

	/**
	 * The default constructor, constructs an unloaded image.
	 * 
	 * @return	Returns the default image.
	 */
	public ImageResource() {
		super();
		this.image	= null;
		this.filter	= null;
	}
	/**
	 * Constructs an image with the given name, path, and resource state.
	 * 
	 * @param	path - The path to the image.
	 * @param	isResource - True if the image should be loaded as a
	 * resource instead of as a file.
	 * @return	Returns an unloaded image with the given file details.
	 */
	public ImageResource(String path, boolean isResource) {
		super(path, isResource);
		this.image	= null;
		this.filter	= null;
	}
	/**
	 * Constructs an image with the given name, path, and resource state.
	 * 
	 * @param	path - The path to the image.
	 * @param	isResource - True if the image should be loaded as a
	 * resource instead of as a file.
	 * @param	filter - The color to filter to empty when loading.
	 * @return	Returns an unloaded image with the given file details.
	 */
	public ImageResource(String path, boolean isResource, Color filter) {
		super(path, isResource);
		this.image	= null;
		this.filter	= filter;
	}
	
	// ======================= Methods ========================

	/**
	 * Tests whether the image has been loaded.
	 * 
	 * @return	Returns true if the image is currently loaded.
	 */
	public boolean isLoaded() {
		return image != null;
	}
	/**
	 * Loads the image from file.
	 */
	public void load() {
		try {
			// Load the image only if it is not loaded
			if (image == null) {
				if (isResource) {
					// Load the image as a resource
					image = new ImageIcon(ImageResource.class.getResource("/resources/images/" + resourcePath)).getImage();
				}
				else {
					// Load the image as a file
					image = new ImageIcon(new URL(resourcePath)).getImage();
				}
				
				if (filter != null) {
					filterImage();
				}
			}
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Unloads the image and removes all attached data to save memory.
	 */
	public void unload() {
		if (image != null)
			image.flush();
		image = null;
	}
	/**
	 * Gets the image with the given name.
	 * 
	 * @return	Returns the image or null if it's not loaded.
	 */
	public Image getResource() {
		return image;
	}
	/**
	 * Filters out the image after loading.
	 */
	private void filterImage() {
		
		BufferedImage buffer = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		buffer.getGraphics().drawImage(image, 0, 0, null);
		
		int[] pixels = ((DataBufferInt)buffer.getRaster().getDataBuffer()).getData();
		
		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == filter.getRGB()) {
				pixels[i] = 0x00000000;
			}
		}
		
		image.flush();
		image = Toolkit.getDefaultToolkit().createImage(buffer.getSource());
		buffer.flush();
		buffer = null;
	}
}
