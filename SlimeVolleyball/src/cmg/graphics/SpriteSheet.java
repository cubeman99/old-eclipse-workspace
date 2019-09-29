package cmg.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import cmg.main.ImageLoader;
import cmg.math.geometry.Point;


/**
 * A sheet of same-sized images that are arranged in a grid layout.
 * 
 * @author David Jordan
 */
public class SpriteSheet {
	private static final Point DEFAULT_OFFSET = new Point(0, 0);
	private static final Point DEFAULT_SEPERATION = new Point(0, 0);

	protected BufferedImage buffer;
	protected Point bufferSize;
	protected Point imageSize;
	protected Point size;
	protected Point offset;
	protected Point seperation;
	


	// ================== CONSTRUCTORS ================== //

	public SpriteSheet(String imageName, int imageWidth, int imageHeight) {
		this(imageName, new Point(imageWidth, imageHeight), DEFAULT_SEPERATION,
				DEFAULT_OFFSET);
	}

	public SpriteSheet(String imageName, Point imageSize) {
		this(imageName, imageSize, DEFAULT_SEPERATION, DEFAULT_OFFSET);
	}

	public SpriteSheet(String imageName, int imageWidth, int imageHeight,
			int seperation) {
		this(imageName, new Point(imageWidth, imageHeight), new Point(
				seperation, seperation), DEFAULT_OFFSET);
	}

	public SpriteSheet(String imageName, Point imageSize, Point seperation) {
		this(imageName, imageSize, seperation, DEFAULT_OFFSET);
	}

	public SpriteSheet(String imageName, Point imageSize, Point seperation,
			Point offset) {
		Image image = ImageLoader.loadImage(imageName);

		this.bufferSize = new Point(image.getWidth(null), image.getHeight(null));
		this.imageSize  = new Point(imageSize);
		this.offset     = new Point(offset);
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
	
	public Point getOffset() {
		return offset;
	}

	public Point getSeperation() {
		return seperation;
	}
	
	public Graphics getGraphics() {
		return buffer.getGraphics();
	}
}
