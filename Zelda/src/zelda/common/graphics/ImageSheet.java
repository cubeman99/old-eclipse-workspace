package zelda.common.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import zelda.common.geometry.Point;
import zelda.main.ImageLoader;


/**
 * A sheet of same-sized images that are arranged in a grid layout.
 * 
 * @author David Jordan
 */
public class ImageSheet {
	private static final Point DEFAULT_OFFSET = new Point(0, 0);
	private static final Point DEFAULT_SEPERATION = new Point(0, 0);

	protected BufferedImage buffer;
	protected Point bufferSize;
	protected Point imageSize;
	protected Point size;
	protected Point offset;
	protected Point seperation;
	protected boolean hasSubSheet;
	protected Point subSheetLocation;
	protected Point subSheetImageSize;


	// ================== CONSTRUCTORS ================== //

	public ImageSheet(String imageName, int imageWidth, int imageHeight) {
		this(imageName, new Point(imageWidth, imageHeight), DEFAULT_SEPERATION,
				DEFAULT_OFFSET);
	}

	public ImageSheet(String imageName, Point imageSize) {
		this(imageName, imageSize, DEFAULT_SEPERATION, DEFAULT_OFFSET);
	}

	public ImageSheet(String imageName, int imageWidth, int imageHeight,
			int seperation) {
		this(imageName, new Point(imageWidth, imageHeight), new Point(
				seperation, seperation), DEFAULT_OFFSET);
	}

	public ImageSheet(String imageName, Point imageSize, Point seperation) {
		this(imageName, imageSize, seperation, DEFAULT_OFFSET);
	}

	public ImageSheet(String imageName, Point imageSize, Point seperation,
			Point offset) {
		Image image = ImageLoader.loadImage(imageName);

		this.bufferSize = new Point(image.getWidth(null), image.getHeight(null));
		this.imageSize = new Point(imageSize);
		this.offset = new Point(offset);
		this.seperation = new Point(seperation);
		
		this.buffer = new BufferedImage(bufferSize.x, bufferSize.y,
				BufferedImage.TYPE_INT_ARGB);
		this.buffer.getGraphics().drawImage(image, 0, 0, null);

		this.size = bufferSize.plus(1).minus(offset)
				.dividedBy(imageSize.plus(seperation));
	}



	// =================== ACCESSORS =================== //

	public BufferedImage getBuffer() {
		return buffer;
	}

	public Point getBufferSize() {
		return bufferSize;
	}

	public Point getSize() {
		return size;
	}

	public Point getImageSize() {
		return imageSize;
	}

	public Point getSubSheetImageSize() {
		return subSheetImageSize;
	}
	
	public Point getSubSheetLocation() {
		return subSheetLocation;
	}

	public Point getOffset() {
		return offset;
	}

	public Point getSeperation() {
		return seperation;
	}
	
	public boolean isOfSubSheet(Point sourcePos) {
		return (hasSubSheet && sourcePos.x >= subSheetLocation.x
				&& sourcePos.y >= subSheetLocation.y);
	}
	
	public Graphics2D getGraphics() {
		return (Graphics2D) buffer.getGraphics();
	}
	
	/** Return the list of pixel RGB values. **/
	public int[] getPixels() {
		return ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
	}



	// ==================== MUTATORS ==================== //
	
	public ImageSheet createSubSheet(int locX, int locY, int sizeX, int sizeY) {
		hasSubSheet       = true;
		subSheetLocation  = new Point(locX, locY);
		subSheetImageSize = new Point(sizeX, sizeY);
		return this;
	}
	
	/** Replace all pixels of the given color with another color. **/
	public void replaceColor(Color oldColor, Color newColor) {
		int[] pixels = getPixels();

		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == oldColor.getRGB())
				pixels[i] = newColor.getRGB();
		}
	}
}
