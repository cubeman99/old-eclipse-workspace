package game.tile;

import game.World;
import common.Point;
import common.graphics.Draw;
import common.graphics.Sprite;

public class Tile {
	public Point position;
	public Sprite sprite;
	protected boolean solid;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Tile(int x, int y) {
		this.position = new Point(x, y);
		this.sprite   = null;
		this.solid    = false;
	}

	public Tile(int x, int y, boolean solid) {
		this.position = new Point(x, y);
		this.sprite   = null;
		this.solid    = solid;
	}
	
	public Tile(int x, int y, Sprite spr) {
		this.position = new Point(x, y);
		this.sprite   = spr;
		this.solid    = false;
	}
	
	public Tile(int x, int y, Sprite spr, boolean solid) {
		this.position = new Point(x, y);
		this.sprite   = spr;
		this.solid    = solid;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean isSolid() {
		return solid;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void update() {}
	
	public void draw() {
		if (sprite != null)
			Draw.drawSprite(sprite, position.x, position.y);
	}
}
