package items;

import java.awt.Graphics2D;

import game.GameInstance;
import geometry.Vector;

/**
 * The base class for all items that go in the inventory.
 * @author	Robert Jordan
 */
public abstract class Ammo {

	// ======================= Members ========================
	
	/** The instance of the game. */
	public GameInstance game;
	/** The string id used to find the ammo. */
	protected String id;
	
	/** The ammo count for this type of ammunition. */
	protected int ammoCount;
	
	// ===================== Constructors =====================
	
	/** Constructs the default ammunition. */
	protected Ammo() {
		this.game		= null;
		
		this.id			= "";
		this.ammoCount	= 0;
	}
	/** Constructs an ammunition with the given information. */
	protected Ammo(String id, int ammoCount) {
		this.game		= null;
		
		this.id			= id;
		this.ammoCount	= ammoCount;
	}
	/** Initializes the ammunition and sets up the container variables. */
	public void initialize(GameInstance game) {
		this.game = game;
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
		
	}
	
	// ===================== Information ======================
	
	/** Get's the item's id. */
	public String getID() {
		return id;
	}
	/** Gets the name of the item. */
	public abstract String getName();
	/** Gets the description of the item. */
	public abstract String getDescription();
	/** Gets the maximum amount of ammo that the player can carry at a time. */
	public abstract int getCapacity();
	/** Gets the current amount of ammo. */
	public int getCount() {
		return ammoCount;
	}
	/** Sets the ammo count. */
	public void setCount(int amount) {
		ammoCount = amount;
		if (ammoCount < 0)
			ammoCount = 0;
		else if (ammoCount > getCapacity())
			ammoCount = getCapacity();
	}
	/** Adds to the ammo count. */
	public void addCount(int amount) {
		ammoCount += amount;
		if (ammoCount > getCapacity())
			ammoCount = getCapacity();
	}
	/** Removes from the ammo count. */
	public void removeCount(int amount) {
		ammoCount -= amount;
		if (ammoCount < 0)
			ammoCount = 0;
	}
	/** Returns true if the ammo is at capacity. */
	public boolean isAtCapacity() {
		return ammoCount >= getCapacity();
	}
}