package items.weapons;

import game.GameInstance;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;
import graphics.Tileset;
import graphics.library.Library;

import items.Ammo;
import items.ammo.EmberSeeds;
import items.ammo.GaleSeeds;
import items.ammo.MysterySeeds;
import items.ammo.PegasusSeeds;
import items.ammo.ScentSeeds;

import java.awt.Graphics2D;

/**
 * A bag for using seeds.
 * @author	Robert Jordan
 */
public class Slingshot extends Weapon {

	// ====================== Constants =======================
	
	// ======================= Members ========================

	/** The current seed used by the slingshot. */
	private int currentSeed;
	
	// ===================== Constructors =====================
	
	/** Constructs the default slingshot. */
	public Slingshot() {
		super("slingshot", 1);
		
		this.currentSeed = 0;
	}
	/** Constructs a slingshot with the given information. */
	public Slingshot(int level, int currentSeed) {
		super("slingshot", level);
		
		this.currentSeed = currentSeed;
	}
	/** Initializes the item and sets up the container variables. */
	public void initialize(GameInstance game) {
		super.initialize(game);
		
		final String[] seedIDs = {"ember_seeds", "scent_seeds", "pegasus_seeds", "gale_seeds", "mystery_seeds"};
		final Ammo[] seeds = new Ammo[]{new EmberSeeds(), new ScentSeeds(), new PegasusSeeds(), new GaleSeeds(), new MysterySeeds()};
		if (!game.inventory.ammoExists(seedIDs[currentSeed]))
			game.inventory.addAmmo(seeds[currentSeed]);
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the item. */
	public void update() {
		
	}
	/** Called every step to draw the item in the menu or HUD. */
	public void draw(Graphics2D g, Vector point) {
		final Point[] levelTiles = {new Point(10, 0), new Point(11, 0)};
		final String[] seedIDs = {"ember_seeds", "scent_seeds", "pegasus_seeds", "gale_seeds", "mystery_seeds"};
		final Tileset tileset = (game.isInMenu() ?
					Library.tilesets.itemsSmallLight : Library.tilesets.itemsSmall);
			
		Draw.drawTile(g, tileset, levelTiles[level - 1].plus((getSlot() == 1 && level == 2) ? 1 : 0, 0), point);
		Draw.drawTile(g, tileset, new Point(currentSeed, 3), point.plus(8, 0));
		
		// Draw the level
		drawAmmoCount(g, point, game.inventory.getAmmoCount(seedIDs[currentSeed]));
	}
	
	// ===================== Information ======================

	/** Gets the name of the item. */
	public String getName() {
		final String[] names = {"Slingshot", "Hyper Slingshot"};
		return names[level - 1];
	}
	/** Gets the description of the item. */
	public String getDescription() {
		final String[] descriptions = {"Used to shoot seeds.", "Shoots in three directions."};
		return descriptions[level - 1];
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
		Ammo[] seeds = new Ammo[5];
		seeds[0] = game.inventory.getAmmo("ember_seeds");
		seeds[1] = game.inventory.getAmmo("scent_seeds");
		seeds[2] = game.inventory.getAmmo("pegasus_seeds");
		seeds[3] = game.inventory.getAmmo("gale_seeds");
		seeds[4] = game.inventory.getAmmo("mystery_seeds");
		
		int existingTotal = 0;
		
		for (int i = 0; i < 5; i++) {
			if (seeds[i] != null)
				existingTotal++;
		}
		
		if (existingTotal > 0) {
			Ammo[] ammo = new Ammo[existingTotal];
			for (int i = 0, index = 0; i < existingTotal; i++, index++) {
				for (; index < 5; index++) {
					if (seeds[index] != null)
						break;
				}
				ammo[i] = seeds[index];
			}
			return ammo;
		}
		return null;
	}
	/** Returns the current ammo being used. */
	public Ammo getCurrentAmmo() {
		final String[] seedIDs = {"ember_seeds", "scent_seeds", "pegasus_seeds", "gale_seeds", "mystery_seeds"};
		return game.inventory.getAmmo(seedIDs[currentSeed]);
	}
	/** Sets the current ammo to be used. */
	public void setCurrentAmmo(Ammo ammo) {
		final String[] seedIDs = {"ember_seeds", "scent_seeds", "pegasus_seeds", "gale_seeds", "mystery_seeds"};
		for (int i = 0; i < 5; i++) {
			if (ammo.getID().equals(seedIDs[i])) {
				currentSeed = i;
				break;
			}
		}
	}
	/** Returns true if the ammo with the given id is equipped. */
	public boolean isAmmoEquipped(String id) {
		final String[] seedIDs = {"ember_seeds", "scent_seeds", "pegasus_seeds", "gale_seeds", "mystery_seeds"};
		return id.equals(seedIDs[currentSeed]);
	}
}