package common.graphics;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;

public class Sprite {
	private Image image;
	private SpriteSheet sheet;
	private Dimension size;
	private Point origin;
	private int subImage;
	
	public Sprite(SpriteSheet sheet, int subImage, Image image) {
		this.sheet    = sheet;
		this.subImage = subImage;
		this.image    = image;
		this.size     = sheet.getSpriteSize();
		this.origin   = new Point(size.width / 2, size.height / 2);
	}
	
	public Sprite(SpriteSheet sheet, int subImage) {
		this(sheet, subImage, sheet.getSprite(subImage).getImage());
	}
	
	public Image getImage() {
		return image;
	}
	
	public Point getOrigin() {
		return origin;
	}
}
