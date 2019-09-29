package simulator;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import main.Game;
import main.ImageLoader;
import main.Mouse;

public class GridObject {
	public int x;
	public int y;
	public int width;
	public int height;
	public boolean tickCheck;
	public Image image;
	
	public GridObject(int x, int y) {
		this.x      = x;
		this.y      = y;
		this.width  = 1;
		this.height = 1;
		this.image  = null;
		tickCheck   = Game.wireSimulator.grid.tickCheck;
	}
	
	public void setImage (Image newImage) {
		this.image = newImage;
	}
	
	public void setImage(String imageName) {
		this.image = ImageLoader.getImage(imageName);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(x * Grid.TILE_SIZE, y * Grid.TILE_SIZE, width * Grid.TILE_SIZE, height * Grid.TILE_SIZE);
	}

	// IMPLEMENTABLE: (Needs super. call)
	public void update() {
		if (Mouse.inArea(getRectangle()) && Mouse.leftPressed()) {
			onPressed();
		}
	}
	
	// IMPLEMENTABLE:
	public void onPressed() {}
	
	
	// IMPLEMENTABLE:
	public void draw(Graphics g) {
		if (image != null) {
			g.drawImage(image, x * Grid.TILE_SIZE, y * Grid.TILE_SIZE, null);
		}
	}
}
