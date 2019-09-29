package common.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class SpriteSheet {
	private Image image;
	private Dimension imageSize;
	private Sprite[] sprites;
	private int spriteCount;
	
	public SpriteSheet(Image image, int spriteWidth, int spriteHeight) {
		this.image       = image;
		this.imageSize   = new Dimension(spriteWidth, spriteHeight);
		this.spriteCount = image.getWidth(null) / spriteWidth;
		this.sprites     = new Sprite[spriteCount];
		
		for (int i = 0; i < spriteCount; i++) {
			sprites[i] = new Sprite(this, i, createSpriteImage(i));
		}
	}
	
	public Sprite getSprite(int subImage) {
		return sprites[subImage];
	}
	
	public Image createSpriteImage(int subImage) {
		Image img = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
		int sx    = subImage * imageSize.width;
		img.getGraphics().drawImage(image,
				0, 0,
				imageSize.width, imageSize.height,
				sx, 0,
				sx + imageSize.width, imageSize.height,
				null);
		
		return img;
	}
	
	public Dimension getSpriteSize() {
		return imageSize;
	}
}
