package items.ammo;

import items.Ammo;
import items.weapons.Weapon;

import java.awt.Graphics2D;

import main.GamePanel;

import game.GameInstance;
import geometry.Point;
import geometry.Vector;
import graphics.Draw;

/**
 * Bomb bag ammo.
 * @author	Robert Jordan
 */
public class Bombs extends Ammo {

	// ======================= Members ========================
	
	// ===================== Constructors =====================
	
	/** Constructs the default bomb ammunition. */
	public Bombs() {
		super("bombs", 10);
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
		Draw.drawTile(g, GamePanel.itemTileset, new Point(10, 0), point);
	}
	
	// ===================== Information ======================
	
	/** Gets the name of the item. */
	public String getName() {
		return "Bombs";
	}
	/** Gets the description of the item. */
	public String getDescription() {
		return "Very explosive.";
	}
	/** Gets the maximum amount of ammo that the player can carry at a time. */
	public int getCapacity() {
		final int[] capacities = {10, 30};
		if (game.inventory.itemExists("bomb_bag")) {
			return capacities[((Weapon)game.inventory.getItem("bomb_bag")).getLevel() - 1];
		}
		return 0;
		
	}
}