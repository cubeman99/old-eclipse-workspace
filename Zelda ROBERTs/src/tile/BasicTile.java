package tile;

import game.CollisionType;
import geometry.Point;
import geometry.Rectangle;
import geometry.Vector;
import graphics.Draw;
import graphics.Tileset;
import graphics.library.Library;

import java.awt.Graphics2D;

import main.Main;

import world.Room;

/**
 * 
 * @author	Robert Jordan
 */
public class BasicTile extends Tile {
	
	// ======================= Members ========================

	/** The tileset of the tile. */
	private Tileset tileset;
	/** The index of the tiles. */
	private Point[] tiles;
	
	// ===================== Constructors =====================
	
	/** Constructs the default basic tile. */
	public BasicTile() {
		super();
		
		this.regions			= new TileRegion[1];
		this.regions[0]			= new TileRegion();
		this.regions[0].bounds	= new Rectangle(0, 0, 16, 16);
		this.regions[0].type	= CollisionType.land;
		
		this.tileset	= Library.tilesets.landformTiles;
		this.tiles		= new Point[1];
		this.tiles[0]	= new Point(4, 22);
	}
	/** Constructs a basic tile with the given tile information. */
	public BasicTile(int type, Tileset tileset, Point... tiles) {
		super();
		
		this.regions			= new TileRegion[1];
		this.regions[0]			= new TileRegion();
		this.regions[0].bounds	= new Rectangle(0, 0, 16, 16);
		this.regions[0].type	= type;
		
		this.tileset	= tileset;
		this.tiles		= new Point[tiles.length];
		for (int i = 0; i < tiles.length; i++)
			this.tiles[i] = new Point(tiles[i]);
	}
	/** Constructs a basic tile with the given tile information. */
	public BasicTile(int type, Tileset tileset, int... tileXY) {
		super();
		
		this.regions			= new TileRegion[1];
		this.regions[0]			= new TileRegion();
		this.regions[0].bounds	= new Rectangle(0, 0, 16, 16);
		this.regions[0].type	= type;
		
		this.tileset	= tileset;
		this.tiles		= new Point[tileXY.length/2];
		for (int i = 0; i < tileXY.length/2; i++) {
			this.tiles[i] = new Point(tileXY[i*2], tileXY[i*2+1]);
		}
	}
	/** Constructs a basic tile from the specified basic tile. */
	public BasicTile(BasicTile tile) {
		super();
		
		this.regions			= new TileRegion[1];
		this.regions[0]			= new TileRegion();
		this.regions[0].bounds	= new Rectangle(0, 0, 16, 16);
		this.regions[0].type	= tile.regions[0].type;
		
		this.tileset	= tile.tileset;
		this.tiles		= new Point[tile.tiles.length];
		for (int i = 0; i < tile.tiles.length; i++)
			this.tiles[i] = new Point(tile.tiles[i]);
	}
	/** Initializes the tile and sets up the container variables. */
	public void initialize(Room room) {
		super.initialize(room);
	}
	/** Duplicates the tile and returns the newly created one. */
	public Tile createNewTile() {
		return new BasicTile(this);
	}

	// ======================= Drawing ========================
	
	/** Called every step to draw the tile in the room. */
	public void draw(Graphics2D g, Vector point) {
		
		int tileIndex = 0;
		if (tiles.length > 1) {
			tileIndex = (int)(Main.getStepTime() % (16 * tiles.length)) / 16;
		}
		
		Draw.drawTile(g, tileset, tiles[tileIndex], point);
	}

	// ===================== Interaction ======================

	/** Called if the tile is used by the player. */
	public void use(int direction) {
		
	}
	/** Called if the tile is stepped on by the player. */
	public void step() {
		
	}
	/** Called if the tile is hit by a damaging attack. */
	public void attack(int damageType) {
		
	}
	/** Called if the tile is pushed by the player. */
	public void push(int direction) {
		
	}
	/** Called if the tile is pulled by the player. */
	public void pull(int direction) {
		
	}
	/** Called to interact with a tile and get a response. */
	public boolean interact(int message) {
		return false;
	}
	
	// ===================== Information ======================

	/** Returns whether the tile is of a single type. */
	public boolean isSingleTile() {
		return true;
	}
}
