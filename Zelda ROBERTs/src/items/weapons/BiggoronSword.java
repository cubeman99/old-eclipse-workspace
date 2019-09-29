package items.weapons;

import game.GameInstance;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;
import graphics.library.Library;

import items.Ammo;

import java.awt.Graphics2D;

/**
 * A large and very powerful sword.
 * @author	Robert Jordan
 */
public class BiggoronSword extends Weapon {

	// ====================== Constants =======================
	
	// ======================= Members ========================
	
	// ===================== Constructors =====================
	
	/** Constructs the default biggoron sword. */
	public BiggoronSword() {
		super("biggoron_sword", 1);
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
		if (getSlot() == 0) {
			// Draw in HUD
			if (game.isInMenu()) {
				Draw.drawTile(g, Library.tilesets.itemsLargeLight, new Point(9, 0), point);
				Draw.drawTile(g, Library.tilesets.itemsLargeLight, new Point(10, 0), point.plus(16, 0));
			}
			else {
				Draw.drawTile(g, Library.tilesets.itemsLargeLight, new Point(9, 0), point);
				Draw.drawTile(g, Library.tilesets.itemsLargeLight, new Point(10, 0), point.plus(16, 0));
			}
		}
		else {
			// Draw in menu
			if (game.isInMenu())
				Draw.drawTile(g, Library.tilesets.itemsLargeLight, new Point(8, 0), point);
			else
				Draw.drawTile(g, Library.tilesets.itemsLarge, new Point(8, 0), point);
		}
	}
	
	// ===================== Information ======================

	/** Gets the name of the item. */
	public String getName() {
		return "Biggoron's Sword";
	}
	/** Gets the description of the item. */
	public String getDescription() {
		return "A powerful, two- handed sword.";
	}
	/** Gets the maximum level of the weapon. */
	public int getMaxLevel() {
		return 1;
	}
	/** Returns whether the weapon takes up both indexes in the equipped slot. */
	public boolean isTwoHanded() {
		return true;
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