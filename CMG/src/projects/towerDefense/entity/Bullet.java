package projects.towerDefense.entity;

import java.awt.Color;
import projects.towerDefense.tile.Tile;
import cmg.graphics.Draw;
import cmg.math.geometry.Vector;

public class Bullet extends Entity {
	private Creep target;
	private Vector goalPosition;
	private Vector position;
	private Vector velocity;
	private Vector positionLast;
	private double speed;
	private double damage;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Bullet(Vector pos, Creep target, double damage) {
		this.position     = new Vector(pos);
		this.velocity     = new Vector();
		this.positionLast = new Vector(pos);
		this.goalPosition = new Vector(pos);
		this.target       = target;
		this.speed        = 0.3;
		this.damage       = damage;
	}

	
	
	// =================== ACCESSORS =================== //
	
	
	
	// ==================== MUTATORS ==================== //


	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void update() {
		if (target != null) {
			if (target.isDestroyed())
				target = null;
			else
				goalPosition = target.getPosition();
		}
		
		velocity.set(goalPosition);
		velocity.sub(position);
		velocity.setLength(speed);
		
		double radius = (target == null ? speed : target.getRadius());
		if (position.distanceTo(goalPosition) < radius) {
			if (target != null)
				target.damage(damage);
			destroy();
		}
		
		positionLast.set(position);
		position.add(velocity);
	}
	
	@Override
	public void draw() {
		Draw.setColor(Color.BLACK);
		Draw.drawLine(positionLast.scaledBy(Tile.SIZE), position.scaledBy(Tile.SIZE));
	}
}
