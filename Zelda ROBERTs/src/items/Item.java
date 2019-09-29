package items;

import java.awt.Graphics2D;

import game.GameInstance;
import geometry.Vector;

/**
 * The base class for all items that go in the inventory.
 * @author	Robert Jordan
 */
public abstract class Item {

	// ======================= Members ========================
	
	/** The instance of the game. */
	public GameInstance game;
	/** The string id used to find the item. */
	protected String id;

	// ===================== Constructors =====================
	
	/** Constructs the default item. */
	protected Item() {
		this.game			= null;
		
		this.id				= "";
	}
	/** Constructs an item with the given information. */
	protected Item(String id) {
		this.game			= null;
		
		this.id				= id;
	}
	/** Initializes the item and sets up the container variables. */
	public void initialize(GameInstance game) {
		this.game = game;
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
	
	// ===================== Information ======================
	
	/** Get's the item's id. */
	public String getID() {
		return id;
	}
	/** Gets the slot the item is in. */
	public int getSlot() {
		return game.inventory.getItemSlot(this);
	}
	/** Gets the index of the item. */
	public int getIndex() {
		return game.inventory.getItemIndex(this);
	}
	/** Gets the name of the item. */
	public abstract String getName();
	/** Gets the description of the item. */
	public abstract String getDescription();
}