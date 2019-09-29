package items.weapons;

import game.GameInstance;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;
import graphics.Tileset;
import graphics.library.Library;

import items.Ammo;

import java.awt.Graphics2D;

/**
 * Link's shield.
 * @author	Robert Jordan
 */
public class Shield extends Weapon {

	// ====================== Constants =======================
	
	// ======================= Members ========================

	// ===================== Constructors =====================
	
	/** Constructs the default shield. */
	public Shield() {
		super("shield", 1);
	}
	/** Constructs a shield with the given information. */
	public Shield(int level) {
		super("shield", level);
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
	public void draw(Graphics2D g, Vector point) {
		final Point[] levelTiles = {new Point(3, 0), new Point(4, 0), new Point(5, 0)};
		final Tileset tileset = (game.isInMenu() ?
				Library.tilesets.itemsSmallLight : Library.tilesets.itemsSmall);
		
		Draw.drawTile(g, tileset, levelTiles[level - 1], point);

		// Draw the level
		drawLevel(g, point);
	}
	
	// ===================== Information ======================

	/** Gets the name of the item. */
	public String getName() {
		final String[] names = {"Wooden Shield", "Iron Shield", "Mirror Shield"};
		return names[level - 1];
	}
	/** Gets the description of the item. */
	public String getDescription() {
		final String[] descriptions = {"A small shield.", "A large shield.", "A reflective shield."};
		return descriptions[level - 1];
	}
	/** Gets the maximum level of the weapon. */
	public int getMaxLevel() {
		return 3;
	}
	/** Returns whether the weapon takes up both indexes in the equipped slot. */
	public boolean isTwoHanded() {
		return false;
	}
	/** Returns true if the weapon uses ammo. */
	public boolean isConsumable() {
		return false;
	}
	/** Returns the ammo types used. */
	public Ammo[] getAmmoTypes() {
		return null;
	}
	/** Returns the current ammo being used. */
	public Ammo getCurrentAmmo() {
		return null;
	}
	/** Sets the current ammo to be used. */
	public void setCurrentAmmo(Ammo ammo) {
		
	}
	/** Returns true if the ammo with the given id is equipped. */
	public boolean isAmmoEquipped(String id) {
		return false;
	}
}