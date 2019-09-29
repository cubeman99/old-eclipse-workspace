package game;

import main.ImageLoader;
import common.Vector;
import common.graphics.Draw;
import common.graphics.Sprite;

public class Box extends Entity {
	private Vector position;
	private Sprite sprite;
	
	public Box(double x, double y) {
		position = new Vector(x, y);
		sprite   = ImageLoader.getSprite("testSheet", 0, 1);
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void draw() {
		Draw.drawSprite(sprite, position.x, position.y);
	}
}
