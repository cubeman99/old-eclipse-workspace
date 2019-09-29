package tile;

import entity.tile.PushingBlock;
import game.CollisionType;
import geometry.Point;
import geometry.Rectangle;
import geometry.Vector;
import graphics.Draw;
import graphics.Tileset;
import graphics.library.Library;

import java.awt.Graphics2D;

import world.Room;

/**
 * 
 * @author	Robert Jordan
 */
public class PushableBlock extends Tile {
	
	// ======================= Members ========================

	/** The tileset of the tile. */
	private Tileset tileset;
	/** The index of the tiles. */
	private Point tile;
	
	/** The tile the block is on top of. */
	public Tile coveredTile;
	
	// ===================== Constructors =====================
	
	/** Constructs the default basic tile. */
	public PushableBlock() {
		super();
		
		this.regions			= new TileRegion[1];
		this.regions[0]			= new TileRegion();
		this.regions[0].bounds	= new Rectangle(0, 0, 16, 16);
		this.regions[0].type	= CollisionType.solid;
		
		this.tileset	= Library.tilesets.specialTiles;
		this.tile		= new Point(0, 3);
		this.coveredTile = new BasicTile();
	}
	/** Constructs a basic tile with the given tile information. */
	public PushableBlock(Tileset tileset, Point tile) {
		super();
		
		this.regions			= new TileRegion[1];
		this.regions[0]			= new TileRegion();
		this.regions[0].bounds	= new Rectangle(0, 0, 16, 16);
		this.regions[0].type	= CollisionType.solid;
		
		this.tileset	= tileset;
		this.tile		= new Point(tile);
		this.coveredTile = new BasicTile();
	}
	/** Constructs a basic tile with the given tile information. */
	public PushableBlock(Tileset tileset, int tileX, int tileY) {
		super();
		
		this.regions			= new TileRegion[1];
		this.regions[0]			= new TileRegion();
		this.regions[0].bounds	= new Rectangle(0, 0, 16, 16);
		this.regions[0].type	= CollisionType.solid;
		
		this.tileset	= tileset;
		this.tile		= new Point(tileX, tileY);
		this.coveredTile = new BasicTile();
	}
	/** Constructs a basic tile from the specified basic tile. */
	public PushableBlock(PushableBlock tile) {
		super();
		
		this.regions			= new TileRegion[1];
		this.regions[0]			= new TileRegion();
		this.regions[0].bounds	= new Rectangle(0, 0, 16, 16);
		this.regions[0].type	= CollisionType.solid;
		
		this.tileset	= tile.tileset;
		this.tile		= new Point(tile.tile);
		this.coveredTile = tile.coveredTile.createNewTile();
	}
	/** Initializes the tile and sets up the container variables. */
	public void initialize(Room room) {
		super.initialize(room);
	}
	/** Duplicates the tile and returns the newly created one. */
	public Tile createNewTile() {
		return new PushableBlock(this);
	}

	// ======================= Drawing ========================
	
	/** Called every step to draw the tile in the room. */
	public void draw(Graphics2D g, Vector point) {
		
		Draw.drawTile(g, tileset, tile, point);
	}

	// ===================== Interaction ======================

	/** Called if the tile is used by the player. */
	public void use() {
		
	}
	/** Called if the tile is stepped on by the player. */
	public void step() {
		
	}
	/** Called if the tile is hit by a damaging attack. */
	public void attack(int damageType) {
		
	}
	/** Called if the tile is pushed by the player. */
	public void push(int direction) {
		
		Point offset = new Point();
		
		switch (direction) {
		case 0:	offset.set(1, 0);	break;
		case 1:	offset.set(0, 1);	break;
		case 2:	offset.set(-1, 0);	break;
		case 3:	offset.set(0, -1);	break;
		}
		
		if (room.isValidTileCoordinates(getCoordinates().plus(offset))) {
			if (room.isTileOfType(getCoordinates().plus(offset), CollisionType.allCoverable)) {
				PushingBlock block = new PushingBlock(direction, getPosition(), this);
				room.addEntity(block);
				room.setTile(getCoordinates(), coveredTile);
				coveredTile.uncover();
			}
		}
	}
	/** Called if the tile is pulled by the player. */
	public void pull(int direction) {
		
	}
	/** Called to interact with a tile and get a response. */
	public boolean interact(int message) {
		return false;
	}

	/** Called to place the block back in the world after being pushed. */
	public void place(Point point) {
		coveredTile = room.getTile(point);
		coveredTile.cover();
		room.setTile(point, this);
	}
	/** Called to place the block back in the world after being pushed. */
	public void place(Vector point) {
		coveredTile = room.getTileAtPosition(point);
		coveredTile.cover();
		room.setTileAtPosition(point, this);
	}
}
