package common.graphics;

import java.awt.Image;
import common.Point;


public class Sprite {
	private Image image;
	private SpriteSheet sheet;
	private Point size;
	private Point origin;
	private Point sourceLocation;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Construct a sprite from the sheet with the given location and image. **/
	public Sprite(SpriteSheet sheet, int sx, int sy, Image image) {
		this.sheet          = sheet;
		this.sourceLocation = new Point(sx, sy);
		this.image          = image;
		this.size           = sheet.getSpriteSize();
		this.origin         = new Point(size.x / 2, size.y / 2);
	}
	
	/** Create a copy of a sprite from the given sheet at the given location. **/
	public Sprite(SpriteSheet sheet, int sx, int sy) {
		this(sheet, sx, sy, sheet.getSprite(sx, sy).getImage());
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
	
	/** Return the sprite sheet this sprite is from. **/
	public SpriteSheet getSpriteSheet() {
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
}
