package items.weapons;

import game.GameInstance;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;
import graphics.Tileset;
import graphics.library.Library;

import items.Ammo;
import items.ammo.Bombs;

import java.awt.Graphics2D;

/**
 * A bag containing bombs.
 * @author	Robert Jordan
 */
public class BombBag extends Weapon {

	// ====================== Constants =======================
	
	// ======================= Members ========================
	
	// ===================== Constructors =====================
	
	/** Constructs the default bomb bag. */
	public BombBag() {
		super("bomb_bag", 1);
	}
	/** Constructs a bomb bag with the given information. */
	public BombBag(int level) {
		super("bomb_bag", level);
	}
	/** Initializes the item and sets up the container variables. */
	public void initialize(GameInstance game) {
		super.initialize(game);
		
		game.inventory.addAmmo(new Bombs());
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the item. */
	public void update() {
		
	}
	/** Called every step to draw the item in the menu or HUD. */
	public void draw(Graphics2D g, Vector point) {
		final Tileset tileset = (game.isInMenu() ?
					Library.tilesets.itemsSmallLight : Library.tilesets.itemsSmall);
			
		Draw.drawTile(g, tileset, new Point(13, 0), point);

		// Draw the level
		drawAmmoCount(g, point, game.inventory.getAmmoCount("bombs"));
	}
	
	// ===================== Information ======================

	/** Gets the name of the item. */
	public String getName() {
		return "Bomb Bag";
	}
	/** Gets the description of the item. */
	public String getDescription() {
		return "Very explosive.";
	}
	/** Gets the maximum level of the weapon. */
	public int getMaxLevel() {
		return 2;
	}
	/** Returns whether the weapon takes up both indexes in the equipped slot. */
	public boolean isTwoHanded() {
		return false;
	}
	/** Returns true if the weapon uses ammo. */
	public boolean isConsumable() {
		return true;
	}
	/** Returns the ammo types used. */
	public Ammo[] getAmmoTypes() {
		if (game.inventory.ammoExists("bombs")) {
			return new Ammo[]{game.inventory.getAmmo("bombs")};
		}
		return null;
	}
	/** Returns the current ammo being used. */
	public Ammo getCurrentAmmo() {
		return game.inventory.getAmmo("bombs");
	}
	/** Sets the current ammo to be used. */
	public void setCurrentAmmo(Ammo ammo) {
		
	}
	/** Returns true if the ammo with the given id is equipped. */
	public boolean isAmmoEquipped(String id) {
		return id.equals("bombs");
	}
}