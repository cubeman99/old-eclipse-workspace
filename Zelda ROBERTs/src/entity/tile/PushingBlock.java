package entity.tile;

import entity.Entity;
import game.CollisionType;
import geometry.Point;
import geometry.Rectangle;
import geometry.Vector;
import graphics.Draw;
import graphics.Sprite;
import graphics.Tileset;
import graphics.library.Library;

import java.awt.Graphics2D;

import tile.PushableBlock;
import world.Room;

/**
 * A base seed that can be dropped or shot.
 * @author	Robert Jordan
 */
public class PushingBlock extends Entity {

	// ======================= Members ========================
	
	public PushableBlock block;
	
	public Vector startPosition;

	// ===================== Constructors =====================
	
	/** Constructs the default seed. */
	public PushingBlock() {
		super();
	}
	/** Constructs a seed with the given direction and position. */
	public PushingBlock(int direction, Vector point, PushableBlock block) {
		super();

		this.bounds = new Rectangle(0, 0, 16, 16);
		this.collisionType = CollisionType.solid;
		this.position = new Vector(point);
		this.startPosition = new Vector(point);
		this.block = block;
		
		switch (direction) {
		case 0: velocity.set(0.5, 0); break;
		case 1: velocity.set(0, 0.5); break;
		case 2: velocity.set(-0.5, 0); break;
		case 3: velocity.set(0, -0.5); break;
		}
		
	}
	/** Initializes the entity and sets up the container variables. */
	public void initialize(Room room) {
		super.initialize(room);
	}

	// ======================= Updating =======================
	
	/** Called every step, before movement, to update the entity's state. */
	public void preupdate() {
		super.preupdate();
		
		if (position.x >= startPosition.x + 16 ||
			position.x <= startPosition.x - 16 ||
			position.y >= startPosition.y + 16 ||
			position.y <= startPosition.y - 16) {
			
			block.place(position);
			destroy();
		}
	}
	/** Called every step, before collision, to update the entity's state. */
	public void update() {
		super.update();
	}
	/** Called every step, after collision, to update the entity's state. */
	public void postupdate() {
		super.postupdate();
	}
	/** Called every step, after drawing, to update the entity's state. */
	public void postdraw() {
		super.postdraw();
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
		
		block.draw(g, point.plus(position));
	}
}