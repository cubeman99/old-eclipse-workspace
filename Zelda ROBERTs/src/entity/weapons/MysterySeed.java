package entity.weapons;

import geometry.Point;
import geometry.Vector;
import graphics.library.Library;

import java.awt.Graphics2D;

import world.Room;

/**
 * A strange seed that can be dropped or shot.
 * @author	Robert Jordan
 */
public class MysterySeed extends Seed {

	// ======================= Members ========================

	// ===================== Constructors =====================
	
	/** Constructs the default mystery seed. */
	public MysterySeed() {
		super();
	}
	/** Constructs a mystery seed with the given direction and position. */
	public MysterySeed(int direction, int seedState, Vector point, double z) {
		super("mystery_seeds", direction, seedState, new Point(4, 15), point, z);
	}
	/** Initializes the entity and sets up the container variables. */
	public void initialize(Room room) {
		super.initialize(room);
	}

	// ======================= Updating =======================
	
	/** Called every step, before movement, to update the entity's state. */
	public void preupdate() {
		super.preupdate();
	}
	/** Called every step, before collision, to update the entity's state. */
	public void update() {
		super.update();
	}
	/** Called every step, after collision, to update the entity's state. */
	public void postupdate() {
		super.postupdate();
	}
	/** Called every step to move the entity. */
	public void updateMovement() {
		super.updateMovement();
	}
	/** Called every step to check collisions. */
	public void updateCollissions() {
		super.updateCollissions();
	}
	/** Called every step to draw the entity in the room. */
	public void draw(Graphics2D g, Vector point) {
		super.draw(g, point);
	}
	
	// ===================== Seed Related =====================

	/** Called to create the effect and perform actions. */
	public void startEffect() {
		effectSprite.setAnimation(Library.animations.mysterySeedEffect);
		effectSprite.setTileset(Library.tilesets.specialEffects);
	}
}