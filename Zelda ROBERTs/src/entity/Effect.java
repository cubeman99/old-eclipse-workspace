package entity;

import java.awt.Graphics2D;

import world.Room;
import geometry.Vector;
import graphics.Animation;
import graphics.Sprite;
import graphics.Tileset;

public class Effect extends Entity {

	// ======================= Members ========================
	
	/** The sprite of the effect animation. */
	public Sprite sprite;

	// ===================== Constructors =====================
	
	/** Constructs the default effect. */
	public Effect() {
		super();
		
		this.sprite = null;
	}
	/** Constructs an effect with the given animation. */
	public Effect(Animation animation, Tileset tileset, Vector point) {
		super();
		
		this.position = new Vector(point);
		this.sprite = new Sprite(animation, tileset);
		this.sprite.setFramePosition(-1);
	}
	/** Initializes the entity and sets up the container variables. */
	public void initialize(Room room) {
		super.initialize(room);
	}

	// ======================= Updating =======================
	
	/** Called every step, before movement, to update the entity's state. */
	public void preupdate() {
		sprite.update();
		if (sprite.isAnimationFinished()) {
			destroy();
		}
	}
	/** Called every step, before collision, to update the entity's state. */
	public void update() {
	}
	/** Called every step, after collision, to update the entity's state. */
	public void postupdate() {
		
	}
	/** Called every step to move the entity. */
	public void updateMovement() {
		position.add(velocity);
	}
	/** Called every step to check collisions. */
	public void updateCollissions() {
		
	}
	/** Called every step to draw the entity in the room. */
	public void draw(Graphics2D g, Vector point) {
		sprite.draw(g, point.plus(position));
	}
}