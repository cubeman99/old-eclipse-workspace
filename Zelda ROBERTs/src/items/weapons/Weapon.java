package items.weapons;

import items.Ammo;
import items.Item;

import java.awt.Color;
import java.awt.Graphics2D;
import java.text.DecimalFormat;

import main.GamePanel;

import game.GameInstance;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;
import graphics.library.Library;

/**
 * The base class for all weapons.
 * @author	Robert Jordan
 */
public abstract class Weapon extends Item {

	// ======================= Members ========================
	
	/** The weapon's level. */
	protected int level;

	// ===================== Constructors =====================
	
	/** Constructs the default item. */
	protected Weapon() {
		super();

		this.level = 1;
	}
	/** Constructs an item with the given information. */
	protected Weapon(String id, int level) {
		super(id);
		
		this.level = level;
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
		
	}
	/** Draw's the item's level. */
	protected void drawLevel(Graphics2D g, Vector point) {
		if (game.isInMenu()) {
			g.setColor(new Color(16, 16, 16));
			Draw.drawTile(g, Library.tilesets.menuSmallLight, new Point(2, 1), point.plus(8, 8));
		}
		else {
			g.setColor(new Color(0, 0, 0));
			Draw.drawTile(g, Library.tilesets.menuSmall, new Point(2, 1), point.plus(8, 8));
		}
		Draw.drawRasterString(g, Library.fonts.fontSmall, String.valueOf(level), point.plus(16, 8));
	}
	/** Draw's the item's ammo count. */
	protected void drawAmmoCount(Graphics2D g, Vector point, int ammo) {
		if (game.isInMenu()) {
			g.setColor(new Color(16, 16, 16));
		}
		else {
			g.setColor(new Color(0, 0, 0));
		}
		DecimalFormat ammoFormat = new DecimalFormat("00");
		Draw.drawRasterString(g, GamePanel.fontSmall, ammoFormat.format(ammo), point.plus(8, 8));
	}
	
	// ===================== Information ======================

	/** Sets the weapon's level. */
	public void setLevel(int level) {
		this.level = level;
	}
	/** Gets the weapon's level. */
	public int getLevel() {
		return level;
	}
	/** Gets the maximum level of the weapon. */
	public abstract int getMaxLevel();
	/** Returns whether the weapon takes up both indexes in the equipped slot. */
	public abstract boolean isTwoHanded();
	/** Returns true if the weapon uses ammo. */
	public abstract boolean isConsumable();
	/** Returns the ammo types used. */
	public abstract Ammo[] getAmmoTypes();
	/** Returns the current ammo being used. */
	public abstract Ammo getCurrentAmmo();
	/** Sets the current ammo to be used. */
	public abstract void setCurrentAmmo(Ammo ammo);
	/** Returns true if the ammo with the given id is equipped. */
	public abstract boolean isAmmoEquipped(String id);
}