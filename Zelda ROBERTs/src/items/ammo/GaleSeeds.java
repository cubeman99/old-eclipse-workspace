package items.ammo;

import items.Ammo;
import items.weapons.Weapon;

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
 * Seed satchel ammo.
 * @author	Robert Jordan
 */
public class GaleSeeds extends Ammo {

	// ======================= Members ========================
	
	// ===================== Constructors =====================
	
	/** Constructs the default gale seed ammunition. */
	public GaleSeeds() {
		super("gale_seeds", 20);
	}
	/** Initializes the ammunition and sets up the container variables. */
	public void initialize(GameInstance game) {
		super.initialize(game);
	}
	
	// ======================= Updating =======================
	
	/** Called every step to update the ammunition. */
	public void update() {
		
	}
	/** Called every step to draw the ammunition in the menu or HUD. */
	public void draw(Graphics2D g, double x, double y) {
		draw(g, new Vector(x, y));
	}
	/** Called every step to draw the ammunition in the menu or HUD. */
	public void draw(Graphics2D g, Vector point) {
		Draw.drawTile(g, GamePanel.hudTileset, new Point(4, 2), point.plus(4, 4));
		
		g.setColor(new Color(248, 248, 248));
		DecimalFormat ammoFormat = new DecimalFormat("00");
		Draw.drawRasterString(g, Library.fonts.fontSmall,
				ammoFormat.format(ammoCount), point.plus(0, 16));
	}
	
	// ===================== Information ======================
	
	/** Gets the name of the item. */
	public String getName() {
		return "Gale Seeds";
	}
	/** Gets the description of the item. */
	public String getDescription() {
		return "A windy trip.";
	}
	/** Gets the maximum amount of ammo that the player can carry at a time. */
	public int getCapacity() {
		final int[] capacities = {20, 50};
		if (game.inventory.itemExists("seed_satchel")) {
			return capacities[((Weapon)game.inventory.getItem("seed_satchel")).getLevel() - 1];
		}
		return 0;
		
	}
}