package OLD;

import java.awt.Image;
import zelda.common.geometry.Point;
import zelda.main.ImageLoader;


public class SpriteOLD {
	private Image image;
	private SpriteSheetOLD sheet;
	private Point size;
	private Point origin;
	private Point sourceLocation;



	// ================== CONSTRUCTORS ================== //

	/** Construct a sprite from the sheet with the given location and image. **/
	public SpriteOLD(SpriteSheetOLD sheet, int sx, int sy, Image image) {
		this.sheet = sheet;
		this.sourceLocation = new Point(sx, sy);
		this.image = image;
		this.size = sheet.getSpriteSize();
		this.origin = new Point();
	}

	/** Create a copy of a sprite from the given sheet at the given location. **/
	public SpriteOLD(SpriteSheetOLD sheet, int sx, int sy) {
		this(sheet, sx, sy, sheet.getSprite(sx, sy).getImage());
	}

	/** Create a single sprite from an image. **/
	public SpriteOLD(String imageName) {
		this.image = ImageLoader.loadImage(imageName);
		this.sheet = null;
		this.size = new Point(image.getWidth(null), image.getHeight(null));
		this.origin = new Point();
		this.sourceLocation = new Point(size.x / 2, size.y / 2);
	}

	public SpriteOLD(SpriteOLD other) {
		this.image = other.image;
		this.sheet = other.sheet;
		this.size = new Point(other.size);
		this.origin = new Point(other.origin);
		this.sourceLocation = new Point(other.sourceLocation);
	}



	// =================== ACCESSORS =================== //

	/** Get the image that is the graphics of this sprite. **/
	public Image getImage() {
		return image;
	}

	/** Return the size of the sprite. **/
	public Point getSize() {
		return size;
	}

	/** Return the width of the sprite. **/
	public int getWidth() {
		return size.x;
	}

	/** Return the height of the sprite. **/
	public int getHeight() {
		return size.y;
	}

	/** Return the sprite sheet this sprite is from. **/
	public SpriteSheetOLD getSpriteSheet() {
		return sheet;
	}

	/** Return the origin of the sprite that it is drawn from. **/
	public Point getOrigin() {
		return origin;
	}

	/** Return the location on the sheet at which this sprite was. **/
	public Point getSourceLocation() {
		return sourceLocation;
	}



	// ==================== MUTATORS ==================== //

	/** Move the origin into the center of the image. **/
	public SpriteOLD centerOrigin() {
		origin.set(size.x / 2, size.y / 2);
		return this;
	}

	public SpriteOLD setOrigin(int originX, int originY) {
		this.origin = new Point(originX, originY);
		return this;
	}

	public SpriteOLD setOrigin(Point origin) {
		this.origin = new Point(origin);
		return this;
	}
}
