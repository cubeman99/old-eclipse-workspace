package common.graphics;

import java.awt.Image;
import java.awt.image.BufferedImage;
import common.Point;


public class SpriteSheet {
	private Image image;
	private Sprite[][] sprites;
	private Point spriteSize;
	private Point spriteCount;
	private int seperation;
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Create a new sprite sheet from the given image and sprite size. **/
	public SpriteSheet(Image image, int spriteWidth, int spriteHeight, int seperation) {
		this.image        = image;
		this.spriteSize   = new Point(spriteWidth, spriteHeight);
		this.spriteCount  = new Point(image.getWidth(null) / spriteWidth, image.getHeight(null) / spriteHeight);
		this.seperation   = seperation;
		this.sprites      = new Sprite[spriteCount.x][spriteCount.y];
		createSprites();
	}

	/** Create a new sprite sheet from the given image and sprite size. **/
	public SpriteSheet(Image image, int spriteWidth, int spriteHeight) {
		this(image, spriteWidth, spriteHeight, 0);
	}
	
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Create all the sprites for this sprite sheet. **/
	private void createSprites() {
		for (int x = 0; x < spriteCount.x; x++) {
			for (int y = 0; y < spriteCount.y; y++) {
				sprites[x][y] = new Sprite(this, x, y, createSpriteImage(x, y));
			}
		}
	}
	
	/** Create the image for the given sprite location. **/
	private Image createSpriteImage(int sx, int sy) {
		Image img = new BufferedImage(spriteSize.x, spriteSize.y, BufferedImage.TYPE_INT_ARGB);
		int dsx   = sx * (spriteSize.x + seperation);
		int dsy   = sy * (spriteSize.y + seperation);
		img.getGraphics().drawImage(image,
				0, 0,
				spriteSize.x, spriteSize.y,
				dsx, dsy,
				dsx + spriteSize.x, dsy + spriteSize.y,
				null);
		
		return img;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return the image of the sprite sheet. **/
	public Image getImage() {
		return image;
	}

	/** Return the sprite at the given location on the sheet. **/
	public Sprite getSprite(Point sp) {
		return sprites[sp.x][sp.y];
	}

	/** Return the sprite at the given location on the sheet. **/
	public Sprite getSprite(int sx, int sy) {
		return sprites[sx][sy];
	}
	
	/** Return the size of an individual sprite. **/
	public Point getSpriteSize() {
		return spriteSize;
	}
}
