package OLD;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import zelda.common.geometry.Point;


public class BufferedSpriteSheetOLD extends SpriteSheetOLD {
	private BufferedImage buffer;

	public BufferedSpriteSheetOLD(SpriteSheetOLD sheet) {
		this.image = sheet.image;
		this.size = new Point(sheet.size);
		this.spriteSize = new Point(sheet.spriteSize);
		this.spriteCount = new Point(sheet.spriteCount);
		this.seperation = sheet.seperation;
		this.sprites = new SpriteOLD[spriteCount.x][spriteCount.y];
		this.origin = new Point();

		this.buffer = new BufferedImage(size.x, size.y,
				BufferedImage.TYPE_INT_ARGB);
		this.buffer.getGraphics().drawImage(image, 0, 0, null);
	}

	/** Return the list of pixel RGB values. **/
	public int[] getPixels() {
		return ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
	}

	/** Replace all pixels of the given color with another color. **/
	public void replaceColor(Color oldColor, Color newColor) {
		int[] pixels = getPixels();

		for (int i = 0; i < pixels.length; i++) {
			if (pixels[i] == oldColor.getRGB())
				pixels[i] = newColor.getRGB();
		}
	}

	@Override
	public BufferedImage getImage() {
		return buffer;
	}

	@Override
	public SpriteOLD getSprite(int sx, int sy) {
		return new SpriteOLD(this, sx, sy, createBufferSpriteImage(sx, sy));
	}

	@Override
	public SpriteOLD getSprite(Point sp) {
		return new SpriteOLD(this, sp.x, sp.y, createBufferSpriteImage(sp.x,
				sp.y));
	}


	/** Create the image for the given sprite location. **/
	protected Image createBufferSpriteImage(int sx, int sy) {
		Image img = new BufferedImage(spriteSize.x, spriteSize.y,
				BufferedImage.TYPE_INT_ARGB);
		int dsx = sx * (spriteSize.x + seperation);
		int dsy = sy * (spriteSize.y + seperation);

		img.getGraphics().drawImage(buffer, 0, 0, spriteSize.x, spriteSize.y,
				dsx, dsy, dsx + spriteSize.x, dsy + spriteSize.y, null);

		return img;
	}
}
