package tp.planner;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import tp.common.Point;
import tp.common.Rectangle;
import tp.common.graphics.GridCanvas;
import tp.planner.tile.Tile;

public class Grid {
	private static final int BLOCK_CHECK_DISTANCE = 8;
	private World world;
	private Tile[][] tiles;
//	private GridCanvas canvas;
	private int index;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Grid(World world, int gridIndex) {
		this.world  = world;
		this.tiles  = new Tile[world.getWidth()][world.getHeight()];
//		this.canvas = new GridCanvas(world.getSize());
		this.index  = gridIndex;
	}
	
	
	
	// =================== ACCESSORS =================== //

	public Point getSize() {
		return world.getSize();
	}
	
	public int getWidth() {
		return world.getWidth();
	}

	public int getHeight() {
		return world.getHeight();
	}
	
	/** Returns the tile at the given position **/
	public Tile get(int x, int y) {
		if (!isValid(x, y))
			return null;
		return tiles[x][y];
	}

	/** Returns the tile at the given position **/
	public Tile get(Point p) {
		return get(p.x, p.y);
	}
	
	/** Returns the item at the given position **/
	public Item getItem(int x, int y) {
		if (tiles[x][y] != null)
			return tiles[x][y].getItem();
		return null;
	}

	/** Returns the item at the given position **/
	public Item getItem(Point p) {
		return getItem(p.x, p.y);
	}

	/** Returns whether the given position is inside the grid. **/
	public boolean isValid(int x, int y) {
		return (x >= 0 && y >= 0 && x < world.getWidth() && y < world.getHeight());
	}
	
	public World getWorld() {
		return world;
	}
	
	/** Return whether the given item placed at the given location would be blocked. **/
	public boolean isBlocked(Item item, int x, int y) {
		for (int xx = Math.max(0, x); xx < Math.min(world.getWidth(), x + item.getWidth()); xx++) {
			for (int yy = Math.max(0, y); yy < Math.min(world.getHeight(), y + item.getHeight()); yy++) {
				Tile t = get(xx, yy);
				if (t != null) {
					if (isTileBlocking(t, item, x, y))
						return true;
				}
			}
		}
		return false;
	}
	
	/** Return whether the given tile is blocking placing an item at the given location. **/
	private boolean isTileBlocking(Tile t, Item item, int x, int y) {
		return (x + item.getWidth() > t.getX() && y + item.getHeight() > t.getY() && t.getX() + t.getWidth() > x && t.getY() + t.getHeight() > y);
	}
	
	public int getGridIndex() {
		return index;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Put an item tile in the grid at the given position. **/
	public boolean put(int x, int y, Item item, boolean replaceMode) {
		if (replaceMode)
			deleteBlockers(item, x, y);
		else if (isBlocked(item, x, y))
			return false;
		
		Tile t = new Tile(this, new Point(x, y), item);
		
		for (int xx = 0; xx < t.getWidth(); xx++) {
			for (int yy = 0; yy < t.getHeight(); yy++) {
				if (isValid(x + xx, y + yy))
					tiles[x + xx][y + yy] = t;
			}
		}

		refreshArea(t.getX() - 2, t.getY() - 2, t.getWidth() + 4, t.getHeight() + 4);
		return true;
	}

	/** Put an item tile in the grid at the given position. **/
	public boolean put(Point p, Item item, boolean replaceMode) {
		return put(p.x, p.y, item, replaceMode);
	}
	
	public void putRaw(Tile t) {
		for (int x = t.getX(); x < t.getX() + t.getWidth(); x++) {
			for (int y = t.getY(); y < t.getY() + t.getHeight(); y++) {
				if (isValid(x, y))
					tiles[x][y] = t;
			}
		}
	}

	/** Clear the tile data at the given position. **/
	public void delete(int x, int y) {
		if (isValid(x, y))
			tiles[x][y] = null;
//		canvas.clearPoint(x, y);
	}
	

	/** Clear the tile data at the given position. **/
	public void delete(Point p) {
		delete(p.x, p.y);
	}
	
	public void clear() {
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				tiles[x][y] = null;
			}
		}
	}
	
	public void resize(int sizeX, int sizeY) {
		tiles = new Tile[sizeX][sizeY];
	}
	
	/** Delete all tiles that are blocking placing the given item. **/
	public void deleteBlockers(Item item, int x, int y) {
		for (int xx = Math.max(0, x); xx < Math.min(world.getWidth(), x + item.getWidth()); xx++) {
			for (int yy = Math.max(0, y); yy < Math.min(world.getHeight(), y + item.getHeight()); yy++) {
				Tile t = get(xx, yy);
				if (t != null) {
					if (isTileBlocking(t, item, x, y))
						t.removeSelfFromGrid();
				}
			}
		}
	}
	
	public void refreshArea(int x, int y) {
		refreshArea(x - 2, y - 2, 5, 5);
	}
	
	public void refreshArea(int x, int y, int width, int height) {
		connectArea(x, y, width, height);
		connectArea(x, y, width, height);
		world.bufferArea(x, y, width, height);
	}
	
	public void connectTile(Tile t) {
		ConnectionScheme.setConnectSubimage(t);
		ConnectionScheme.setConnectIndex(t);
	}
	
	public void connectArea(int x, int y) {
		connectArea(x - 2, y - 2, 5, 5);
	}
	
	public void connectArea(int x, int y, int width, int height) {
		for (int xx = Math.max(0, x); xx < Math.min(world.getWidth(), x + width + 1); xx++) {
			for (int yy = Math.max(0, y); yy < Math.min(world.getHeight(), y + height + 1); yy++) {
				if (tiles[xx][yy] != null)
					connectTile(tiles[xx][yy]);
			}
		}
	}
	/* TODO delete this?
	public void bufferArea(int x, int y) {
		bufferArea(x - 2, y - 2, 5, 5);
	}
	
	public void bufferArea(int x, int y, int width, int height) {
		for (int yy = Math.max(0, y); yy < Math.min(world.getHeight(), y + height + 1); yy++) {
			for (int xx = Math.max(0, x); xx < Math.min(world.getWidth(), x + width + 1); xx++) {
				bufferSpace(xx, yy);
//				if (tiles[xx][yy] != null)
//					tiles[xx][yy].buffer();
//				else
//					canvas.clearPoint(xx, yy);
			}
		}
	}
	*/
	public void bufferSpace(int x, int y) {
		if (index == Item.TYPE_OBJECT)
			bufferSpaceObject(x, y);
		else if (index == Item.TYPE_WALL)
			bufferSpaceWall(x, y);
		else if (index == Item.TYPE_WIRE)
			bufferSpaceWire(x, y);
		else if (index == Item.TYPE_LIQUID)
			bufferSpaceLiquid(x, y);
	}
	
	public void bufferSpaceObject(int x, int y) {
		// Look for extended objects:
		int a = 5;
		for (int yy = Math.max(0, y - a); yy < Math.min(world.getHeight(), y + a + 1); yy++) {
			for (int xx = Math.max(0, x - a); xx < Math.min(world.getWidth(), x + a + 1); xx++) {
				Tile t = get(xx, yy);

				if (t != null && !t.getPosition().equals(x, y)
						&& t.getAbsoluteRect().contains(x, y))
				{
					world.getCanvas().drawObjectTile(t, x - t.getX(), y - t.getY());
				}
			}
		}
		
		// Draw the tile located at the given position:
		Tile t = get(x, y);
		if (t != null)
			world.getCanvas().drawObjectTile(t, x - t.getX(), y - t.getY());
	}
	
	public void bufferSpaceWall(int x, int y) {
		for (int yy = -1; yy <= 1; yy++) {
			for (int xx = -1; xx <= 1; xx++) {
				if (isValid(x + xx, y + yy)) {
					Tile t = get(x + xx, y + yy);
					
					if (t != null)
						world.getCanvas().drawWallTile(t, -xx, -yy);
				}
			}
		}
	}
	
	public void bufferSpaceWire(int x, int y) {
		Tile t = get(x, y);
		if (t != null)
			world.getCanvas().drawWireTile(t);
	}
	
	public void bufferSpaceLiquid(int x, int y) {
		Tile t = get(x, y);
		if (t != null)
			world.getCanvas().drawLiquidTile(t);
	}
	
	
	/** Draw the grid canvas on the given graphics. **/
//	public void draw(Graphics g) {
//		g.drawImage(canvas.getImage(), -world.control.getViewPosition().x, -world.control.getViewPosition().y, null);
//	}
}
