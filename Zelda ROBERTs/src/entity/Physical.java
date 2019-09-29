package entity;

import geometry.Rectangle;
import geometry.Vector;
import graphics.Sprite;
import graphics.library.Library;

import java.awt.Graphics2D;

import world.Room;

public class Physical extends Entity {

	// ======================= Members ========================
	
	// Containment
	/** The room that contains the entity. */
	public Room room;
	/** The string id used to find an entity in a room. */
	protected String id;
	
	// Movement
	/** The position of the entity in the room. */
	public Vector position;
	/** The velocity of the entity. */
	public Vector velocity;
	/** The z position of the entity in the room. */
	public double z;
	/** The z velocity of the entity. */
	public double zspeed;
	
	// Collision
	/** The collision box of the entity. */
	public Rectangle bounds;
	/** The depth of the entity used to decide when to draw the image. */
	public double depth;
	/** True if the entity should move to collide with other solids. */
	public boolean solid;
	/** How the player should interact when colliding with the entity. */
	public int collisionType;
	/** The type of tile the entity is standing on. */
	public int surfaceType;

	// Miscellaneous
	/** The last direction of the entity, used for when the current speed is zero. */
	private double lastDirection;
	/** True if the entity has been destroyed and is awaiting removal. */
	private boolean destroyed;
	/** The sprite of the entity's shadow. */
	protected Sprite shadowSprite;

	// ===================== Constructors =====================
	
	/** Constructs the default physical entity. */
	protected Physical() {
		super();
	}
	/** Constructs a physical entity with the given id. */
	protected Physical(String id) {
		super(id);
	}
	/** Initializes the entity and sets up the container variables. */
	public void initialize(Room room) {
		super.initialize(room);
	}
	
	
	// ======================= Updating =======================
	
	/** Called every step, before collision, to update the entity's state. */
	public void preupdate() {
		super.preupdate();
		shadowSprite.update();
	}
	/** Called every step, before movement, to update the entity's state. */
	public void update() {
		super.update();
		
	}
	/** Called every step, after movement, to update the entity's state. */
	public void postupdate() {
		super.postupdate();
		
	}
	/** Called every step, after drawing, to update the entity's state. */
	public void postdraw() {
		super.postdraw();
	}
	/** Called every step to check collisions. */
	public void updateCollissions() {
		
	}
	/** Called every step to move the entity. */
	public void updateMovement() {
		
	}
	
	// ======================= Drawing ========================
	
	/** Called every step to draw the entity in the room. */
	public void draw(Graphics2D g, Vector point) {
		super.draw(g, point);
	}
}