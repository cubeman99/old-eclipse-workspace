package game;

import java.util.ArrayList;
import main.ImageLoader;
import common.Point;
import common.Settings;
import common.graphics.Sprite;
import game.tile.Tile;


public class World {
	public Point size;
	public Point tileSize;
	public Tile[][] tiles;
	public ArrayList<Entity> entities;
	
	
	// ================== CONSTRUCTORS ================== //
	
	/** Create a new world with the given dimensions. **/
	public World(int width, int height) {
		size     = new Point(width, height);
		tiles    = new Tile[size.x][size.y];
		tileSize = new Point(Settings.TILE_SIZE, Settings.TILE_SIZE);
		entities = new ArrayList<Entity>();
		clearTiles();
		
		for (int i = 0; i < 30; i++) {
			place(10 + i, 20, ImageLoader.getSprite("testSheet", 0, 0), true);
		}
		
		entities.add(new Box(25, 19));
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Clear the world of all tiles. **/
	public void clearTiles() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				tiles[x][y] = null;
			}
		}
	}
	
	/** Place a tile. **/
	public void place(int x, int y, Sprite spr, boolean solid) {
		tiles[x][y] = new Tile(x, y, spr, true);
	}
	
	/** Remove a tile. **/
	public void remove(int x, int y) {
		tiles[x][y] = null;
	}
	
	/** Update the world. **/
	public void update() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tiles[x][y] != null)
					tiles[x][y].update();
			}
		}
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update();
	}
	
	/** Draw the world. **/
	public void draw() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				if (tiles[x][y] != null)
					tiles[x][y].draw();
			}
		}
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).draw();
	}
}
