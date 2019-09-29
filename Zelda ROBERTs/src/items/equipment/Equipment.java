package items.equipment;

import items.BasicItem;

import java.awt.Graphics2D;

import game.GameInstance;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;
import graphics.library.Library;

/**
 * The base class for all weapons.
 * @author	Robert Jordan
 */
public class Equipment extends BasicItem {

	// ======================= Members ========================

	// ===================== Constructors =====================
	
	/** Constructs the default equipment item. */
	public Equipment() {
		super();
	}
	/** Constructs an equipment item with the given information. */
	public Equipment(String id, String name, String description, int tileX, int tileY, boolean largeTile) {
		super(id, name, description, tileX, tileY, largeTile);
	}
	/** Constructs an equipment item with the given information. */
	public Equipment(String id, String name, String description, Point tile, boolean largeTile) {
		super(id, name, description, tile, largeTile);
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
		super.draw(g, point);
		
		if (isEquipped()) {
			// Draw the equipped icon
			Draw.drawTile(g, Library.tilesets.menuSmallLight, new Point(2, 2), point.plus(8, 0));
			Draw.drawTile(g, Library.tilesets.menuSmallLight, new Point(2, 3), point.plus(8, 8));
		}
	}
	
	// ===================== Information ======================

	/** Returns true if the item is equipped. */
	public boolean isEquipped() {
		return game.inventory.isEquipmentEquipped(id);
	}
}