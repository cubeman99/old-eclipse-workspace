package projects.towerDefense.entity;

import java.awt.Color;
import projects.towerDefense.tile.Path;
import projects.towerDefense.tile.Tile;
import cmg.graphics.Draw;
import cmg.math.GMath;
import cmg.math.geometry.Circle;
import cmg.math.geometry.Point;
import cmg.math.geometry.Vector;

public class Creep extends Entity {
	private int pathIndex;
	private Vector position;
	private Vector velocity;
	private double speed;
	private double health;
	private double targetHealth;
	private double radius;
	
	

	// ================== CONSTRUCTORS ================== //
	
	public Creep() {
		pathIndex    = 0;
		position     = new Vector();
		velocity     = new Vector();
		speed        = 2;
		health       = 4;
		targetHealth = health;
		radius       = 0.25;
	}

	
	
	// =================== ACCESSORS =================== //
	
	public boolean isDead() {
		return (health <= 0);
	}
	
	public boolean isTargetDead() {
		return (targetHealth <= 0);
	}
	
	public double getTargetHealth() {
		return targetHealth;
	}
	
	public double getHealth() {
		return health;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public Point getLocation() {
		return new Point(GMath.floor(position.x),
				GMath.floor(position.y));
	}
	
	public Vector getPosition() {
		return position;
	}
	
	public Vector getVelocity() {
		return velocity;
	}
	
	public Path getCurrentStep() {
		if (pathIndex >= level.getPath().numSteps())
			return null;
		return level.getPath().getStep(pathIndex);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void applyTargetDamage(double amount) {
		targetHealth -= amount;
	}
	
	public void damage(double amount) {
		health -= amount;
		if (isDead())
			kill();
	}
	
	public void kill() {
		health = 0;
		destroy();
	}
	
	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void initialize() {
		Path step = getCurrentStep();
		position.set(step.getLocation());
		position.add(0.5, 0.5);
	}
	
	@Override
	public void update() {
		Path step = getCurrentStep();
		
		if (step == null)
			destroy();
		else {
			double movespeed = speed / 60;
			Vector goal = new Vector(step.getLocation()).plus(0.5, 0.5); 
			velocity.set(goal).sub(position).setLength(movespeed);
			
			if (position.distanceTo(goal) < movespeed) {
				pathIndex++;
			}
		}
		
		position.add(velocity);
	}

	@Override
	public void draw() {
		Vector v = position.scaledBy(Tile.SIZE);
		Circle c = new Circle(v, radius * Tile.SIZE);
		Draw.setColor(Color.WHITE);
		Draw.fill(c);
		Draw.setColor(Color.BLACK);
		Draw.draw(c);
		Draw.drawLine(v, v.plus(velocity.lengthVector(c.radius)));
	}
}
