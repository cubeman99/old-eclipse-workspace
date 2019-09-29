package world;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import main.Keyboard;
import main.Main;
import main.Mouse;

import entity.Entity;
import tile.BasicTile;
import tile.PushableBlock;
import tile.Tile;
import tile.TileRegion;
import transition.Screen;
import game.CollisionType;
import game.GameInstance;
import geometry.Point;
import geometry.Rectangle;
import geometry.Vector;
import graphics.Draw;
import graphics.Tileset;
import graphics.library.Library;

/**
 * A class that contains a screen-full of tiles and entities.
 * @author	Robert Jordan
 */
public class Room extends Screen {

	// ======================= Members ========================
	
	/** The size of the room. */
	public Point size;
	/** The size of the room in tiles. */
	public Point tileSize;
	
	/** The fixed-size collection of tiles in the room. */
	public Tile[][] tiles;
	/** The collection of entities in the room. */
	public ArrayList<Entity> entities;
	

	/** The fixed-size collection of tiles in the room. */
	public int[][] checkedTiles;
	
	public static Tile leftTile;
	public static Tile middleTile;
	public static Tile rightTile;

	// ===================== Constructors =====================
	
	/** Constructs the default room. */
	public Room() {
		super();
		
		leftTile = new BasicTile(CollisionType.solid, Library.tilesets.landformTiles, 7, 29);
		middleTile = new PushableBlock(Library.tilesets.specialTiles, 0, 3);
		rightTile = new BasicTile(CollisionType.land, Library.tilesets.landformTiles, 4, 22);
		
		this.tileSize	= new Point(10, 8);
		this.size		= new Point(tileSize.x * 16, tileSize.y * 16);
		
		this.tiles		= new Tile[tileSize.x][tileSize.y];
		this.checkedTiles	= new int[tileSize.x][tileSize.y];
		this.entities	= new ArrayList<Entity>();
		
		for (int i = 0; i < tileSize.x; i++) {
			for (int j = 0; j < tileSize.y; j++) {
				tiles[i][j] = new BasicTile();
				checkedTiles[i][j] = -1;
			}
		}
	}
	/** Constructs a room of the specified size. */
	public Room(Point tileSize) {
		super();
		
		this.tileSize	= new Point(tileSize);
		this.size		= new Point(tileSize.x * 16, tileSize.y * 16);
		
		this.tiles		= new Tile[tileSize.x][tileSize.y];
		this.checkedTiles	= new int[tileSize.x][tileSize.y];
		this.entities	= new ArrayList<Entity>();
		
		for (int i = 0; i < tileSize.x; i++) {
			for (int j = 0; j < tileSize.y; j++) {
				tiles[i][j] = new BasicTile();
				checkedTiles[i][j] = -1;
			}
		}
	}
	/** Initializes the room and sets up the container variables. */
	public void initialize(GameInstance game) {
		super.initialize(game);
		
		for (Entity e : entities) {
			e.initialize(this);
		}
		
		for (int i = 0; i < tileSize.x; i++) {
			for (int j = 0; j < tileSize.y; j++) {
				tiles[i][j].initialize(this);
			}
		}
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the rooms's tiles and entities state. */
	public void update() {
		
		if (Keyboard.getKey(KeyEvent.VK_1).pressed() ||
			Keyboard.getKey(KeyEvent.VK_2).pressed() ||
			Keyboard.getKey(KeyEvent.VK_3).pressed()) {
			
			String pushableStr = JOptionPane.showInputDialog(null, "Is pushable block?", Integer.toString(0), JOptionPane.OK_CANCEL_OPTION);
			String collisionTypeStr = "";
			int collisionType = 1;
			String tilesetIndexStr = "";
			int tilesetIndex = 0;
			String numFramesStr = "";
			int numFrames = 1;
			Point[] tiles = null;
			Tileset tileset = null;
			Tile tile = null;
			if (pushableStr.equals("0")) {
				collisionTypeStr = JOptionPane.showInputDialog(null, "Collision Type", Integer.toString(1), JOptionPane.OK_CANCEL_OPTION);
				collisionType = Integer.parseInt(collisionTypeStr);
			}
			tilesetIndexStr = JOptionPane.showInputDialog(null, "Tileset: 0) Land 1) Special 2) Water", Integer.toString(0), JOptionPane.OK_CANCEL_OPTION);
			tilesetIndex = Integer.parseInt(tilesetIndexStr);

			if (pushableStr.equals("0")) {
				numFramesStr = JOptionPane.showInputDialog(null, "Number of frames", Integer.toString(1), JOptionPane.OK_CANCEL_OPTION);
				numFrames = Integer.parseInt(numFramesStr);
			}
			tiles = new Point[numFrames];
			for (int i = 0; i < numFrames; i++) {
				String xStr = JOptionPane.showInputDialog(null, "Frame: " + Integer.toString(i + 1) + ", Tile X", Integer.toString(0), JOptionPane.OK_CANCEL_OPTION);
				int x = Integer.parseInt(xStr);
				String yStr = JOptionPane.showInputDialog(null, "Frame: " + Integer.toString(i + 1) + ", Tile Y", Integer.toString(0), JOptionPane.OK_CANCEL_OPTION);
				int y = Integer.parseInt(yStr);
				
				tiles[i] = new Point(x, y);
			}
			
			if (tilesetIndex == 1)
				tileset = Library.tilesets.specialTiles;
			else if (tilesetIndex == 2)
				tileset = Library.tilesets.waterTiles;
			else
				tileset = Library.tilesets.landformTiles;
			
			if (pushableStr.equals("0")) {
				tile = new BasicTile(collisionType, tileset, tiles);
			}
			else {
				tile = new PushableBlock(tileset, tiles[0]);
			}
			
			if (Keyboard.getKey(KeyEvent.VK_1).pressed())
				leftTile = tile;
			else if (Keyboard.getKey(KeyEvent.VK_2).pressed())
				middleTile = tile;
			else
				rightTile = tile;
			Keyboard.reset();
			Mouse.reset();
			
		}
		
		for (int i = 0; i < tileSize.x; i++) {
			for (int j = 0; j < tileSize.y; j++) {
				checkedTiles[i][j] = -1;
			}
		}
		
		// Update all the entities with each update cycle
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isDestroyed())
				entities.get(i).preupdate();
		}
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isDestroyed())
				entities.get(i).updateCollissions();
		}
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isDestroyed())
				entities.get(i).update();
		}
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isDestroyed())
				entities.get(i).updateMovement();
		}
		for (int i = 0; i < entities.size(); i++) {
			if (!entities.get(i).isDestroyed())
				entities.get(i).postupdate();
		}
		
		// Remove any destroyed entities
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).isDestroyed()) {
				entities.remove(i);
				i--;
			}
		}
		
		if (Keyboard.start.pressed()) {
			game.openMenu("menu_weapons");
		}
		
		if (isValidTileCoordinates(getTileCoordinates(Mouse.getVector().minus(0, 16)).getPoint()) &&
			Mouse.isMouseInsideWindow()) {
			Vector tile = getTileCoordinates(Mouse.getVector().minus(0, 16));
			if (Mouse.left.down())
				setTile((int)tile.x, (int)tile.y, leftTile.createNewTile());
				//setTile((int)tile.x, (int)tile.y, new BasicTile(CollisionType.solid, Library.tilesets.landformTiles, 7, 29));
			else if (Mouse.right.down())
				setTile((int)tile.x, (int)tile.y, rightTile.createNewTile());
				//setTile((int)tile.x, (int)tile.y, new BasicTile(CollisionType.land, Library.tilesets.landformTiles, 4, 25));
			else if (Mouse.middle.pressed()) {
				setTile((int)tile.x, (int)tile.y, middleTile.createNewTile());
				//setTile((int)tile.x, (int)tile.y, new PushableBlock(Library.tilesets.specialTiles, 0, 3));
			}
		}
	}
	/** Called every step to draw the room's tiles and entities in the room. */
	public void draw(Graphics2D g, Vector point) {
		// Draw the tiles first:
		for (int i = 0; i < tileSize.x; i++) {
			for (int j = 0; j < tileSize.y; j++) {
				tiles[i][j].draw(g, point.plus(i*16, j*16));
				if (Main.debugMode && checkedTiles[i][j] != -1) {
					if (checkedTiles[i][j] == 0) {
						g.setColor(Color.WHITE);
						Draw.fillRect(g, point.plus(i*16, j*16), new Vector(8, 16));
					}
					else if (checkedTiles[i][j] == 1) {
						g.setColor(Color.RED);
						Draw.fillRect(g, point.plus(i*16, j*16), new Vector(8, 16));
					}
				}
			}
		}
		
		// List the entities to draw in a specific order:
		int[] indexes = new int[entities.size()];
		double[] depths = new double[entities.size()];
		for (int i = 0; i < entities.size(); i++) {
			indexes[i] = i;
			depths[i] = entities.get(i).depth;
		}
		
		// Sort the indexes using insertion sort:
		for (int i = 1; i < entities.size(); i++) {
			int i2 = i;
			int index = indexes[i];
			double depth = depths[i];
			
			while (i2 > 0 && depth < depths[i2 - 1]) {
				indexes[i2] = indexes[i2 - 1];
				depths[i2] = depths[i2 - 1];
				i2--;
			}
			
			depths[i2] = depth;
			indexes[i2] = index;
		}
		
		// Draw each of the entities:
		for (int i = 0; i < entities.size(); i++) {
			entities.get(indexes[i]).draw(g, point);
			if (Main.debugMode) {
				g.setColor(Color.BLUE);
				Draw.fillRect(g, entities.get(indexes[i]).getBounds().translatedBy(point.plus(entities.get(indexes[i]).position)));
			}
		}
		
		if (isValidTileCoordinates(Mouse.getPoint().minus(0, 16).scaledBy(1.0/16.0)) &&
			Mouse.isMouseInsideWindow()) {
			g.setColor(Color.RED);
			Vector tile = getTileCoordinates(Mouse.getVector().minus(0, 16)).scaledBy(16);
			Draw.drawRect(g, point.plus(tile), new Vector(15, 15));
		}
		
		
	}
	
	// ===================== Information ======================
	
	/** Gets the size of the room in pixels. */
	public Point getSize() {
		return new Point(size);
	}
	/** Gets the bounds of the room. */
	public Rectangle getBounds() {
		return new Rectangle(new Vector(0, 16), size.getVector());
	}
	/** Gets the size of the room in tiles. */
	public Point getTileSize() {
		return new Point(tileSize);
	}
	/** Returns true if the object bounding box is outside the room. */
	public boolean isOutsideRoom(Vector position, Rectangle bounds) {
		return !getBounds().colliding(bounds.translatedBy(position));
	}
	
	// ==================== Entity Related ====================
	
	/** Adds an entity to the room. */
	public void addEntity(Entity e) {
		entities.add(e);
		if (game != null)
			e.initialize(this);
	}
	/** Removes an entity from the room. */
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
	/** Tests whether the entity exists in the room. */
	public boolean entityExists(Entity e) {
		return (entities.indexOf(e) != -1 && !e.isDestroyed());
	}
	/** Tests whether the entity with the given id exists in the room. */
	public boolean entityExists(String id) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).getID().equals(id) && !entities.get(i).isDestroyed()) {
				return true;
			}
		}
		return false;
	}
	/** Returns the entity in the room with the given id. */
	public Entity getEntity(String id) {
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).getID().equals(id) && !entities.get(i).isDestroyed()) {
				return entities.get(i);
			}
		}
		return null;
	}
	/** Returns the number of entities in the room. */
	public int getNumEntities() {
		int total = entities.size();
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).isDestroyed()) {
				total--;
			}
		}
		return total;
	}

	// ===================== Tile Related =====================
	
	/** Gets the tile at the current position. */
	public Tile getTile(int x, int y) {
		if (isValidTileCoordinates(x, y)) {
			return tiles[x][y];
		}
		return null;
	}
	/** Gets the tile at the current position. */
	public Tile getTile(Point point) {
		if (isValidTileCoordinates(point.x, point.y)) {
			return tiles[point.x][point.y];
		}
		return null;
	}
	/** Gets the tile at the current position. */
	public Tile getTileAtPosition(double x, double y) {
		if (isValidTileCoordinates((int)x / 16, (int)y / 16)) {
			return tiles[(int)x / 16][(int)y / 16];
		}
		return null;
	}
	/** Gets the tile at the current position. */
	public Tile getTileAtPosition(Vector point) {
		if (isValidTileCoordinates((int)point.x / 16, (int)point.y / 16)) {
			return tiles[(int)point.x / 16][(int)point.y / 16];
		}
		return null;
	}
	
	/** Sets the tile at the current position. */
	public void setTile(int x, int y, Tile tile) {
		if (isValidTileCoordinates(x, y)) {
			tiles[x][y] = tile;
			if (game != null)
				tile.initialize(this);
		}
	}
	/** Sets the tile at the current position. */
	public void setTile(Point point, Tile tile) {
		if (isValidTileCoordinates(point.x, point.y)) {
			tiles[point.x][point.y] = tile;
			if (game != null)
				tile.initialize(this);
		}
	}
	/** Sets the tile at the current position. */
	public void setTileAtPosition(double x, double y, Tile tile) {
		if (isValidTileCoordinates((int)x / 16, (int)y / 16)) {
			tiles[(int)x / 16][(int)y / 16] = tile;
			if (game != null)
				tile.initialize(this);
		}
	}
	/** Sets the tile at the current position. */
	public void setTileAtPosition(Vector point, Tile tile) {
		if (isValidTileCoordinates((int)point.x / 16, (int)point.y / 16)) {
			tiles[(int)point.x / 16][(int)point.y / 16] = tile;
			if (game != null)
				tile.initialize(this);
		}
	}
	
	/** Tests whether the given tile coordinates lie in the room. */
	public boolean isValidTileCoordinates(int x, int y) {
		return	(x >= 0 && x < tileSize.x) &&
				(y >= 0 && y < tileSize.y);
	}
	/** Tests whether the given tile coordinates lie in the room. */
	public boolean isValidTileCoordinates(Point point) {
		return	(point.x >= 0 && point.x < tileSize.x) &&
				(point.y >= 0 && point.y < tileSize.y);
	}
	/** Tests whether the given tile coordinates lie in the room. */
	public boolean isValidTilePosition(double x, double y) {
		return	((int)(x/16) >= 0 && (int)(x/16) < tileSize.x) &&
				((int)(y/16) >= 0 && (int)(y/16) < tileSize.y);
	}
	/** Tests whether the given tile coordinates lie in the room. */
	public boolean isValidTilePosition(Vector point) {
		return	((int)(point.x/16) >= 0 && (int)(point.x/16) < tileSize.x) &&
				((int)(point.y/16) >= 0 && (int)(point.y/16) < tileSize.y);
	}
	
	/** Gets the tile coordinates of the room coordinates. */
	public Vector getTileCoordinates(double x, double y) {
		return new Vector((int)(x / 16), (int)(y / 16));
	}
	/** Gets the tile coordinates of the room coordinates. */
	public Vector getTileCoordinates(Vector point) {
		return new Vector((int)(point.x / 16), (int)(point.y / 16));
	}
	
	/** Gets the coordinates of the tile. */
	public Point getTileCoordinates(Tile tile) {
		for (int i = 0; i < tileSize.x; i++) {
			for (int j = 0; j < tileSize.y; j++) {
				if (tiles[i][j] == tile) {
					return new Point(i, j);
				}
			}
		}
		return new Point(-1, -1);
	}
	/** Gets the position of the tile. */
	public Vector getTilePosition(Tile tile) {
		for (int i = 0; i < tileSize.x; i++) {
			for (int j = 0; j < tileSize.y; j++) {
				if (tiles[i][j] == tile) {
					return new Vector(i*16, j*16);
				}
			}
		}
		return new Vector(-1, -1);
	}
	
	/** Returns true if the tile is of the given type. */
	public boolean isTileOfType(int x, int y, int collisionType) {
		return isTileOfType(new Point(x, y), collisionType);
	}
	/** Returns true if the tile is of the given type. */
	public boolean isTileOfType(Point point, int collisionType) {
		if (isValidTileCoordinates(point)) {
			for (int i = 0; i < tiles[point.x][point.y].regions.length; i++) {
				if (CollisionType.matchesType(collisionType, tiles[point.x][point.y].regions[i].type))
					return true;
			}
		}
		return false;
	}
	/** Returns true if the tile is of the given type. */
	public boolean isTileTypeAtPosition(double x, double y, int collisionType) {
		return isTileOfType(new Point((int)(x/16), (int)(y/16)), collisionType);
	}
	/** Returns true if the tile is of the given type. */
	public boolean isTileTypeAtPosition(Vector point, int collisionType) {
		return isTileOfType(new Point((int)(point.x/16), (int)(point.y/16)), collisionType);
	}
	
	/** Returns true if the tile is of the given type. */
	public int getTileTypeAtPosition(double x, double y) {
		return getTileTypeAtPosition(new Vector(x, y));
	}
	/** Returns true if the tile is of the given type. */
	public int getTileTypeAtPosition(Vector point) {
		Vector newPoint = new Vector(point);
		newPoint.x = (int)newPoint.x;
		newPoint.y = (int)newPoint.y;
		
		if (isValidTilePosition(newPoint)) {
			Tile tile = getTileAtPosition(newPoint);
			for (int i = 0; i < tile.regions.length; i++) {
				if (tile.regions[i].bounds.translatedBy(tile.getPosition()).contains(newPoint))
					return tile.regions[i].type;
			}
		}
		return CollisionType.boundary;
	}

	// ====================== Collision =======================

	/** Checks for collision with a tile of a specific type. */
	public boolean isCollidingWithEntity(Vector point, Rectangle bounds, Entity entity) {
		Rectangle newBounds = new Rectangle(bounds).translate(point);
		newBounds.point.x = (int)newBounds.point.x;
		newBounds.point.y = (int)newBounds.point.y;
		
		Rectangle otherBounds = new Rectangle(entity.getBounds());
		otherBounds.translate(entity.position);
		otherBounds.translate(entity.velocity);
		otherBounds.point.x = (int)otherBounds.point.x;
		otherBounds.point.y = (int)otherBounds.point.y;
		
		if (otherBounds.colliding(newBounds)) {
			return true;
		}
		return false;
	}
	/** Checks for collision with a tile of a specific type. */
	public boolean isCollidingWithEntity(Vector point, Rectangle bounds, int collisionType) {
		Rectangle newBounds = new Rectangle(bounds).translate(point);
		newBounds.point.x = (int)newBounds.point.x;
		newBounds.point.y = (int)newBounds.point.y;
		
		for (int i = 0; i < entities.size(); i++) {
			if (CollisionType.matchesType(collisionType, entities.get(i).getCollisionType())) {
				Rectangle otherBounds = new Rectangle(entities.get(i).getBounds());
				otherBounds.translate(entities.get(i).position);
				otherBounds.translate(entities.get(i).velocity);
				otherBounds.point.x = (int)otherBounds.point.x;
				otherBounds.point.y = (int)otherBounds.point.y;
				if (otherBounds.colliding(newBounds)) {
					return true;
				}
			}
		}
		return false;
	}
	/** Checks for collision with a tile of a specific type. */
	public Entity[] getEntityCollisions(Vector point, Rectangle bounds, int collisionType) {
		Rectangle newBounds = new Rectangle(bounds).translate(point);
		newBounds.point.x = (int)newBounds.point.x;
		newBounds.point.y = (int)newBounds.point.y;
		
		Entity[] entityList = new Entity[0];
		
		for (int i = 0; i < entities.size(); i++) {
			if (CollisionType.matchesType(collisionType, entities.get(i).getCollisionType())) {
				Rectangle otherBounds = new Rectangle(entities.get(i).getBounds());
				otherBounds.translate(entities.get(i).position);
				otherBounds.translate(entities.get(i).velocity);
				otherBounds.point.x = (int)otherBounds.point.x;
				otherBounds.point.y = (int)otherBounds.point.y;
				if (otherBounds.colliding(newBounds)) {
					Entity[] newEntityList = new Entity[entityList.length + 1];
					for (int j = 0; j < entityList.length; j++) {
						newEntityList[j] = entityList[j];
					}
					newEntityList[entityList.length] = entities.get(i);
					entityList = newEntityList;
				}
			}
		}
		return entityList;
	}
	/** Checks for collision with a tile of a specific type. */
	public boolean isCollidingWithTile(Vector point, Rectangle bounds, Tile tile) {
		Rectangle newBounds = new Rectangle(bounds).translate(point);
		newBounds.point.x = (int)newBounds.point.x;
		newBounds.point.y = (int)newBounds.point.y;
		
		// For each of the tile regions within the tile
		for (int i = 0; i < tile.regions.length; i++) {
			if (tile.regions[i].bounds.translatedBy(getTilePosition(tile)).colliding(newBounds)) {
				return true;
			}
		}
		return false;
	}
	/** Checks for collision with a tile of a specific type. */
	public boolean isCollidingWithTile(Vector point, Rectangle bounds, int collisionType) {
		Rectangle newBounds = new Rectangle(bounds).translate(point);
		newBounds.point.x = (int)newBounds.point.x;
		newBounds.point.y = (int)newBounds.point.y;
		Vector min = getTileCoordinates(newBounds.point);
		Vector max = getTileCoordinates(newBounds.point.plus(newBounds.size));
		
		// For each of the tiles in range of the bounding box
		for (int x = (int)min.x; x <= (int)max.x; x++) {
			for (int y = (int)min.y; y <= (int)max.y; y++) {
				
				// For each of the tile regions within the tile
				if (isValidTileCoordinates(x, y)) {
					for (int i = 0; i < tiles[x][y].regions.length; i++) {
						if (CollisionType.matchesType(collisionType, tiles[x][y].regions[i].type) &&
							tiles[x][y].regions[i].bounds.translatedBy(x*16, y*16).colliding(newBounds)) {
							checkedTiles[x][y] = collisionType;
							return true;
						}
					}
				}
				else if (collisionType == CollisionType.boundary) {
					return true;
				}
			}
		}
		return false;
	}
	/** Checks for collision with a tile of a specific type. */
	public Tile[] getTileCollisions(Vector point, Rectangle bounds, int collisionType) {
		Rectangle newBounds = new Rectangle(bounds).translate(point);
		newBounds.point.x = (int)newBounds.point.x;
		newBounds.point.y = (int)newBounds.point.y;
		Vector min = getTileCoordinates(newBounds.point);
		Vector max = getTileCoordinates(newBounds.point.plus(newBounds.size));
		

		Tile[] tileList = new Tile[0];
		if (collisionType == CollisionType.boundary) {
			return tileList;
		}
		
		// For each of the tiles in range of the bounding box
		for (int x = (int)min.x; x <= (int)max.x; x++) {
			for (int y = (int)min.y; y <= (int)max.y; y++) {
				
				// For each of the tile regions within the tile
				if (isValidTileCoordinates(x, y)) {
					for (int i = 0; i < tiles[x][y].regions.length; i++) {
						if (CollisionType.matchesType(collisionType, tiles[x][y].regions[i].type) &&
							tiles[x][y].regions[i].bounds.translatedBy(x*16, y*16).colliding(newBounds)) {
							Tile[] newTileList = new Tile[tileList.length + 1];
							for (int j = 0; j < tileList.length; j++) {
								newTileList[j] = tileList[j];
							}
							newTileList[tileList.length] = tiles[x][y];
							tileList = newTileList;
							break;
						}
					}
				}
			}
		}
		return tileList;
	}
}