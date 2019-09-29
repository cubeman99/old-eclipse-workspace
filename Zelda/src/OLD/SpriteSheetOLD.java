package OLD;

import java.awt.Image;
import java.awt.image.BufferedImage;
import zelda.common.geometry.Point;
import zelda.main.ImageLoader;


public class SpriteSheetOLD {
	protected Image image;
	protected SpriteOLD[][] sprites;
	protected Point spriteSize;
	protected Point spriteCount;
	protected int seperation;
	protected Point size;
	protected Point origin;



	// ================== CONSTRUCTORS ================== //

	public SpriteSheetOLD() {

	}

	/** Create a new sprite sheet from the given image and sprite size. **/
	public SpriteSheetOLD(Image image, int spriteWidth, int spriteHeight,
			int seperation) {
		this.image = image;
		this.spriteSize = new Point(spriteWidth, spriteHeight);
		this.size = new Point(image.getWidth(null), image.getHeight(null));
		this.spriteCount = new Point(size.x / spriteWidth, size.y
				/ spriteHeight);
		this.seperation = seperation;
		this.sprites = new SpriteOLD[spriteCount.x][spriteCount.y];
		this.origin = new Point();
		createSprites();
	}

	/** Create a new sprite sheet from the given image and sprite size. **/
	public SpriteSheetOLD(Image image, int spriteWidth, int spriteHeight) {
		this(image, spriteWidth, spriteHeight, 0);
	}

	public SpriteSheetOLD(String imageName, int spriteWidth, int spriteHeight,
			int seperation) {
		this(ImageLoader.loadImage(imageName), spriteWidth, spriteHeight,
				seperation);
	}

	public SpriteSheetOLD(String imageName, int spriteWidth, int spriteHeight) {
		this(ImageLoader.loadImage(imageName), spriteWidth, spriteHeight, 0);
	}



	// ==================== MUTATORS ==================== //

	/** Create all the sprites for this sprite sheet. **/
	protected void createSprites() {
		for (int x = 0; x < spriteCount.x; x++) {
			for (int y = 0; y < spriteCount.y; y++) {
				sprites[x][y] = new SpriteOLD(this, x, y, createSpriteImage(x,
						y));
			}
		}
	}

	/** Create the image for the given sprite location. **/
	protected Image createSpriteImage(int sx, int sy) {
		Image img = new BufferedImage(spriteSize.x, spriteSize.y,
				BufferedImage.TYPE_INT_ARGB);
		int dsx = sx * (spriteSize.x + seperation);
		int dsy = sy * (spriteSize.y + seperation);
		img.getGraphics().drawImage(image, 0, 0, spriteSize.x, spriteSize.y,
				dsx, dsy, dsx + spriteSize.x, dsy + spriteSize.y, null);

		return img;
	}

	public SpriteSheetOLD centerOrigin() {
		setOrigin(new Point(spriteSize.x / 2, spriteSize.y / 2));
		return this;
	}

	public SpriteSheetOLD setOrigin(int originX, int originY) {
		return setOrigin(new Point(originX, originY));
	}

	public SpriteSheetOLD setOrigin(Point origin) {
		this.origin = new Point(origin);

		for (int x = 0; x < spriteCount.x; x++) {
			for (int y = 0; y < spriteCount.y; y++) {
				sprites[x][y].setOrigin(origin);
			}
		}

		return this;
	}



	// =================== ACCESSORS =================== //

	/** Return the image of the sprite sheet. **/
	public Image getImage() {
		return image;
	}

	/** Return the sprite at the given location on the sheet. **/
	public SpriteOLD getSprite(Point sp) {
		return sprites[sp.x][sp.y];
	}

	/** Return the sprite at the given location on the sheet. **/
	public SpriteOLD getSprite(int sx, int sy) {
		return sprites[sx][sy];
	}

	/** Return the size of an individual sprite. **/
	public Point getSpriteSize() {
		return spriteSize;
	}

	/** Return the width in sprites of the sheet. **/
	public int getWidth() {
		return spriteCount.x;
	}

	/** Return the height in sprites of the sheet. **/
	public int getHeight() {
		return spriteCount.x;
	}

	/** Return the dimensions in sprites of the sheet. **/
	public Point getSize() {
		return spriteCount;
	}
}
