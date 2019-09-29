package tp.common.graphics;

import java.awt.Graphics;
import java.awt.Image;
import tp.common.Point;
import tp.main.ImageLoader;

public class Sheet {
	private Image image;
	private Point spriteSize;
	private Point imageSize;
	private int spriteSep;
	

	// ================== CONSTRUCTORS ================== //
	
	public Sheet(String imageName, int spriteWidth, int spriteHeight, int spriteSep) {
		this.image = ImageLoader.getImage(imageName);
		if (this.image == null)
			this.image = ImageLoader.loadImage(imageName + ".png");
		this.spriteSize = new Point(spriteWidth, spriteHeight);
		this.imageSize  = new Point(image.getWidth(null), image.getHeight(null));
		this.spriteSep  = spriteSep;
	}
	
	public Sheet(String imageName, int spriteSize) {
		this(imageName, spriteSize, spriteSize, 0);
	}
	
	public Sheet(String imageName) {
		this(imageName, 0, 0, 0);
		this.spriteSize.set(imageSize);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Image getImage() {
		return image;
	}
	
	public Point getSpriteSize() {
		return spriteSize;
	}
	
	public int getSpriteWidth() {
		return spriteSize.x;
	}
	
	public int getSpriteHeight() {
		return spriteSize.y;
	}
	
	public Point getImageSize() {
		return imageSize;
	}
	
	public int getImageWidth() {
		return imageSize.x;
	}
	
	public int getImageHeight() {
		return imageSize.y;
	}

	public int getSpriteSep() {
		return spriteSep;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void draw(Graphics g, int dx, int dy) {
		g.drawImage(image, dx, dy, null);
	}
	
	public void drawSprite(Graphics g, int dx, int dy, int spriteX, int spriteY) {
		g.drawImage(image, dx, dy, dx + spriteSize.x, dy + spriteSize.y,
				spriteX * (spriteSize.x + spriteSep), spriteY
						* (spriteSize.y + spriteSep),
				(spriteX * (spriteSize.x + spriteSep)) + spriteSize.x,
				(spriteY * (spriteSize.y + spriteSep)) + spriteSize.y, null);
	}
	
	public void drawSprite(Graphics g, int dx, int dy, int spriteX, int spriteY, int xScale, int yScale) {
		g.drawImage(image, dx, dy, dx + (spriteSize.x * xScale), dy + (spriteSize.y * yScale),
				spriteX * (spriteSize.x + spriteSep), spriteY
						* (spriteSize.y + spriteSep),
				(spriteX * (spriteSize.x + spriteSep)) + spriteSize.x,
				(spriteY * (spriteSize.y + spriteSep)) + spriteSize.y, null);
	}
}
