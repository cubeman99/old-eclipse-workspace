package items;

import java.awt.Graphics2D;

import game.GameInstance;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;
import graphics.library.Library;

/**
 * The class for defining simple itemsy.
 * @author	Robert Jordan
 */
public class BasicItem extends Item {

	// ======================= Members ========================
	
	/** The name of the item. */
	protected String name;
	/** The item's description. */
	protected String description;
	/** The index of the tile. */
	protected Point tile;
	/** True if the tile lies in the large tileset. */
	protected boolean largeTile;

	// ===================== Constructors =====================
	
	/** Constructs the default basic item. */
	public BasicItem() {
		super();
		
		this.name			= "";
		this.description	= "";
		this.tile			= new Point();
		this.largeTile		= false;
	}
	/** Constructs a basic item with the given information. */
	public BasicItem(String id, String name, String description, int tileX, int tileY, boolean largeTile) {
		super(id);
		
		this.name			= name;
		this.description	= description;
		this.tile			= new Point(tileX, tileY);
		this.largeTile		= largeTile;
	}
	/** Constructs a basic item with the given information. */
	public BasicItem(String id, String name, String description, Point tile, boolean largeTile) {
		super(id);
		
		this.name			= name;
		this.description	= description;
		this.tile			= new Point(tile);
		this.largeTile		= largeTile;
	}
	/** Initializes the item and sets up the container variables. */
	public void initialize(GameInstance game) {
		super.initialize(game);
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the item. */
	public void update() {
		
	}
	/** Called every step to draw the item in the menu or HUD. */
	public void draw(Graphics2D g, double x, double y) {
		draw(g, new Vector(x, y));
	}
	/** Called every step to draw the item in the menu or HUD. */
	public void draw(Graphics2D g, Vector point) {
		if (largeTile && game.isInMenu())
			Draw.drawTile(g, Library.tilesets.itemsLargeLight, tile, point);
		else if (game.isInMenu())
			Draw.drawTile(g, Library.tilesets.itemsSmallLight, tile, point);
		else if (largeTile)
			Draw.drawTile(g, Library.tilesets.itemsLarge, tile, point);
		else
			Draw.drawTile(g, Library.tilesets.itemsSmall, tile, point);
	}
	
	// ===================== Information ======================
	
	/** Gets the name of the item. */
	public String getName() {
		return name;
	}
	/** Gets the description of the item. */
	public String getDescription() {
		return description;
	}
}