package tile;

import game.CollisionType;
import geometry.Rectangle;
import geometry.Vector;

/**
 * A region used in a tile that has certain affects when collided with.
 * @author	Robert Jordan
 */
public class TileRegion {

	// ======================= Members ========================
	
	/** The bounds of the region within the tile. */
	public Rectangle bounds;
	/** The collision type of the region. */
	public int type;

	// ===================== Constructors =====================
	
	/** Constructs the default tile region. */
	public TileRegion() {
		this.bounds = new Rectangle();
		this.type	= CollisionType.land;
	}
	/** Constructs a tile region with the given box and type. */
	public TileRegion(double x, double y, double w, double h, int type) {
		this.bounds = new Rectangle(x, y, w, h);
		this.type	= type;
	}
	/** Constructs a tile region with the given box and type. */
	public TileRegion(Vector point, Vector size, int type) {
		this.bounds = new Rectangle(point, size);
		this.type	= type;
	}
	/** Constructs a tile region with the given box and type. */
	public TileRegion(Rectangle rect, int type) {
		this.bounds = new Rectangle(rect);
		this.type	= type;
	}
}