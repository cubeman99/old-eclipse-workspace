package tile;

import geometry.Point;
import geometry.Vector;

import java.awt.Graphics2D;

import world.Room;

/**
 * A 16x16 square in a room that contains information about how
 * the player should interact with it, as well as graphical information.
 * @author	Robert Jordan
 */
public abstract class Tile {

	// ======================= Members ========================
	
	/** The room that contains the tile. */
	public Room room;
	
	/** The collection of collidable parts of the tile. */
	public TileRegion[] regions;
	
	// ===================== Constructors =====================
	
	/** Constructs the default tile. */
	protected Tile() {
		this.room		= null;
		
		this.regions	= new TileRegion[0];
	}
	/** Initializes the tile and sets up the container variables. */
	public void initialize(Room room) {
		this.room = room;
	}
	/** Duplicates the tile and returns the newly created one. */
	public abstract Tile createNewTile();

	// ======================= Updating =======================
	
	/** Called every step to update the tile. */
	public void update() {
		
	}
	
	// ======================= Drawing ========================
	
	/** Called every step to draw the tile in the room. */
	public void draw(Graphics2D g, double x, double y) {
		draw(g, new Vector(x, y));
	}
	/** Called every step to draw the tile in the room. */
	public void draw(Graphics2D g, Vector point) {
		
	}

	// ===================== Interaction ======================

	/** Called if the tile is used by the player. */
	public void use(int direction) {
		
	}
	/** Called if the tile is stepped on by the player. */
	public void step() {
		
	}
	/** Called if the tile is hit by a damaging attack. */
	public void attack(int damageType, int direction) {
		
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
	/** Called when the tile has been covered by another block. */
	public void cover() {
		
	}
	/** Called when the tile has been uncovered from under another block. */
	public void uncover() {
		
	}
	
	// ===================== Information ======================

	/** Gets the coordinates of the tile. */
	public Point getCoordinates() {
		return room.getTileCoordinates(this);
	}
	/** Gets the position of the tile. */
	public Vector getPosition() {
		return room.getTilePosition(this);
	}
	/** Returns whether the tile is of a single type. */
	public boolean isSingleTile() {
		return regions.length == 1;
	}
	/** Gets the center of the tile. */
	public Vector getCenter() {
		return getPosition().plus(8, 8);
	}
}
