package entity.unit;

import java.awt.Color;
import weapon.Weapon;
import common.Draw;
import common.GMath;
import common.Settings;
import common.Timer;
import common.Vector;
import common.shape.Circle;
import common.shape.Line;
import common.shape.Polygon;
import control.Control;
import control.Team;
import dynamics.Body;
import entity.Entity;


/**
 * Unit.
 * 
 * @author David Jordan
 */
public abstract class Unit extends Entity {
	public static final double UNIT_RADIUS = 0.49;
	public Control control;
	public Team team;
	
	public Body body;
	public Circle shape;
	
	public Vector moveVector;
	public double radius;
	public double direction;
	
	public double speed;
	public double maxSpeed;
	
	public double health;
	public double maxHealth;
	
	public Timer respawnTimer;
	public Weapon weapon;
	
	// ================== CONSTRUCTORS ================== //
	
	public Unit(Team team, Vector position, double radius) {
		super(Settings.DEPTH_UNIT);
		this.team       = team;
		this.control    = team.control;
		
		this.radius     = radius;
		this.moveVector = new Vector();
		this.direction  = 0;
		this.speed      = 0;
		
		this.maxSpeed   = 0.1;
		this.health     = 0;
		this.maxHealth  = 0;
		
		this.respawnTimer = new Timer(1000);
		
		this.shape = new Circle(position, radius);
		this.body  = Body.newDynamicBody(shape);
		control.physics.addBody(body);
	}
	
	public void respawn(Vector location) {
		shape.position.set(location);
		health    = maxHealth;
		speed     = 0;
		destroyed = false;
		respawnTimer.stop();
		moveVector.zero();
		if (weapon != null)
			weapon.renew();
	}
	
	
	// =================== ACCESSORS =================== //
	
	/** Return a the position of the unit. **/
	public final Vector getPosition() {
		return shape.position;
	}
	
	/** Return a circle representing the unit's shape and position. **/
	public final Circle getCircle() {
		return shape;
	}
	
	/** Return if this and another unit are on the same team. **/
	public boolean isOnSameTeam(Unit u) {
		return (this.team == u.team);
	}
	
	
	// ==================== MUTATORS ==================== //

	/** Give the unit health. Return if the unit was healed completely. **/
	public boolean giveHealth(double amount) {
		health = GMath.min(maxHealth, health + amount);
		return (health > maxHealth - GMath.EPSILON);
	}
	
	/** Subtract from the unit's health. Return if the unit is now dead. **/
	public boolean damage(double amount) {
		health = GMath.max(0, health - amount);
		if (health < GMath.EPSILON)
			destroy();
		return (health < GMath.EPSILON);
	}

	/** Set the max health and set health to that value. **/
	public void setMaxHealth(double maxHealth) {
		this.maxHealth = maxHealth;
		this.health    = maxHealth;
	}
	
	
//	/** Collide the unit's circular shape with the map. **/
//	public boolean checkCollisions() {
//		Circle c = getCircle();
//		boolean colliding = Collision.applyCollisions(control.map, c, body.velocity);
//		getPosition().set(c.position);
//		for (Polygon p : control.map.walls)
//			colliding = collideSnap(p) || colliding;
//		return colliding;
//	}
//	
//	
//	/** Snap outside of a polygon if colliding with it. **/
//	public boolean collideSnap(Polygon p) {
//		boolean colliding = false;
//		for (int i = 0; i < p.vertexCount(); i++) {
//			colliding = collideSnap(p.getEdge(i)) || colliding;
//		}
//		return colliding;
//	}
//
//	/** Snap outside of a line if colliding with it. **/
//	public boolean collideSnap(Line l) {
//		Circle c = getCircle();
//		if (!c.touching(l))
//			return false;
//		
//		Vector closest = l.getClosestPoint(getPosition());
//		Vector moveVec = new Vector(closest, getPosition());
//		moveVec.setLength(radius);
//		
//		getPosition().set(closest.plus(moveVec));
//		//limitDirection(moveVec);
//		
//		return true;
//	}
	
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	public void destroy() {
		super.destroy();
//		control.physics.removeBody(this.body);
	}
	
	@Override
	public void update() {
		
	}
	
	@Override
	public void draw() {
		// Draw a circle with direction line:
		Draw.setColor(team.color);
		shape.draw();
		Line l = new Line(getPosition(), getPosition().plus(Vector.polarVector(radius, direction)));
		Draw.drawLine(l);
		
		// Draw a health bar:
		if (maxHealth > 0) {
    		double hr = radius;
    		double ht = radius + 0.25;
    		Line h1   = new Line(getPosition().minus(hr, ht), getPosition().minus(-hr, ht));
    		Draw.setColor(Color.GRAY);
    		Draw.drawLine(h1);
    		h1.scale(health / maxHealth);
    		Draw.setColor(Color.GREEN);
    		Draw.drawLine(h1);
		}
	}
}
