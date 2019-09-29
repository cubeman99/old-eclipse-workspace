package entity.weapons;

import entity.Entity;
import geometry.Vector;

import java.awt.Graphics2D;

import world.Room;

/**
 * A fiery seed that can be dropped or shot.
 * @author	Robert Jordan
 */
public class Boomerang extends Entity {

	// ======================= Members ========================

	// ===================== Constructors =====================
	
	/** Constructs the default boomerang. */
	public Boomerang() {
		super("boomerang");
	}
	/** Constructs a boomerang. */
	public Boomerang(int direction, Vector point) {
		super("boomerang");
	}
	/** Initializes the entity and sets up the container variables. */
	public void initialize(Room room) {
		super.initialize(room);
	}

	// ======================= Updating =======================
	
	/** Called every step, before movement, to update the entity's state. */
	public void preupdate() {
		
	}
	/** Called every step, before collision, to update the entity's state. */
	public void update() {
		
	}
	/** Called every step, after collision, to update the entity's state. */
	public void postupdate() {
		
	}
	/** Called every step to move the entity. */
	public void updateMovement() {
		super.updateMovement();
	}
	/** Called every step to check collisions. */
	public void updateCollissions() {
		
	}
	/** Called every step to draw the entity in the room. */
	public void draw(Graphics2D g, Vector point) {
		
	}
	
	// ================== Boomerang Related ===================
	
	
}