package graphics;

import java.awt.Image;

import geometry.Point;

/**
 * An image that represents smaller images grouped together.
 * @author	Robert Jordan
 */
public class Tileset {

	// ====================== Constants =======================
	
	/** The palette used for sprites. */
	public static final int TILETYPE_SPRITE	= 0;
	/** The palette used for world tiles. */
	public static final int TILETYPE_WORLD	= 1;
	
	// ======================= Members ========================
	
	/** The number of tiles in each direction. */
	protected Point numTiles;
	/** The size of each tile. */
	protected Point tileSize;
	/** The spacing between each tile. */
	protected Point tileSpacing;
	/** The offset before the first tiles. */
	protected Point tileOffset;
	
	/** The image that contains all the tiles. */
	protected LinkedImage tileSheet;

	// ===================== Constructors =====================
	
	/** Constructs the default tile set with no data. */
	public Tileset() {
		this.numTiles		= new Point();
		this.tileSize		= new Point();
		this.tileSpacing	= new Point();
		this.tileOffset		= new Point();

		this.tileSheet		= null;
	}
	/** Constructs a tile set with the given image and dimensions. */
	public Tileset(String imageName, int tileWidth, int tileHeight,
			int tileSpacingX, int tileSpacingY) {
		this.numTiles		= new Point();
		this.tileSize		= new Point(tileWidth, tileHeight);
		this.tileSpacing	= new Point(tileSpacingX, tileSpacingY);
		this.tileOffset		= new Point();
		
		this.tileSheet		= new LinkedImage(imageName);
	}
	/** Constructs a tile set with the given image and dimensions. */
	public Tileset(String imageName, Point tileSize, Point tileSpacing) {
		this.numTiles		= new Point();
		this.tileSize		= new Point(tileSize);
		this.tileSpacing	= new Point(tileSpacing);
		this.tileOffset		= new Point();

		this.tileSheet		= new LinkedImage(imageName);
	}
	/** Constructs a tile set with the given image and dimensions. */
	public Tileset(String imageName, int tileWidth, int tileHeight, int tileSpacingX, int tileSpacingY,
			int tileOffsetX, int tileOffsetY) {
		this.numTiles		= new Point();
		this.tileSize		= new Point(tileWidth, tileHeight);
		this.tileSpacing	= new Point(tileSpacingX, tileSpacingY);
		this.tileOffset		= new Point(tileOffsetX, tileOffsetY);

		this.tileSheet		= new LinkedImage(imageName);
	}
	/** Constructs a tile set with the given image and dimensions. */
	public Tileset(String imageName, Point tileSize, Point tileSpacing, Point tileOffset) {
		this.numTiles		= new Point();
		this.tileSize		= new Point(tileSize);
		this.tileSpacing	= new Point(tileSpacing);
		this.tileOffset		= new Point(tileOffset);

		this.tileSheet		= new LinkedImage(imageName);
	}

	// ======================== Tiles =========================
	
	/** Gets the tile set image. */
	public Image getImage() {
		return tileSheet.getImage();
	}
	/** Gets the coordinates of the given tile. */
	public Point getTilePoint(int indexX, int indexY) {
		return getTilePoint(new Point(indexX, indexY));
	}
	/** Gets the coordinates of the given tile. */
	public Point getTilePoint(Point index) {
		Point index2 = new Point(index);
		index2.x *= tileSize.x + tileSpacing.x;
		index2.y *= tileSize.y + tileSpacing.y;
		index2.add(tileSpacing.plus(tileOffset));
		
		return index2;
	}
	/** Gets the size of the tiles. */
	public Point getTileSize() {
		return new Point(tileSize);
	}
	/** Gets the spacing of the tiles. */
	public Point getTileSpacing() {
		return new Point(tileSpacing);
	}
	/** Gets the number of tiles in each direction. */
	public Point getNumTiles() {
		return new Point(numTiles);
	}
}