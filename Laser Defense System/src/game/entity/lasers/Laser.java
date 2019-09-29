package game.entity.lasers;

import java.util.ArrayList;
import common.GMath;
import common.Line;
import common.Settings;
import common.Vector;
import common.graphics.Draw;
import common.graphics.Sprite;
import main.ImageLoader;
import game.Control;
import game.entity.Entity;
import game.nodes.Enemy;


/**
 * Laser.
 * 
 * @author David Jordan
 */
public abstract class Laser extends Entity {
	public Control control;
	protected Sprite sprite;
	protected double radius;
	protected double angle;
	protected double damage;
	

	// ================== CONSTRUCTORS ================== //
	
	public Laser(Control control, double radius, double angle) {
		super(0);
		this.control = control;
		this.radius  = radius;
		this.angle   = angle;
		this.damage  = Settings.LASER_DAMAGE;
		this.sprite  = null;
	}
	
	public Laser(int subImage) {
		super(0);
		this.control = null;
		this.radius  = 0;
		this.angle   = 0;
		this.damage  = Settings.LASER_DAMAGE;
		this.sprite  = ImageLoader.getSprite("lasers", subImage);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return the position of this object in rectangle form. **/
	public Vector getPosition() {
		return Vector.polarVector(radius, angle);
	}
	
	/** Return the radius component of this laser's position. **/
	public double getRadius() {
		return radius;
	}
	
	/** Return the angle component of this laser's position. **/
	public double getAngle() {
		return angle;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Configure this laser. **/
	public void configure(Control control, double radius, double angle) {
		this.control = control;
		this.radius  = radius;
		this.angle   = angle;
	}
	
	/** Set the polar position based on a rectangular coordinate. **/
	protected void setPosition(Vector rectPosition) {
		radius = rectPosition.length();
		angle  = rectPosition.direction();
	}
	
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	public void update() {
		Line motion = new Line(Vector.polarVector(radius, angle), Vector.polarVector(radius - 1, angle));
		ArrayList<Enemy> enemies = control.getEnemyList();
		
		// Check for enemy hits:
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			
			if (GMath.abs(radius - enemy.getRadius()) <= enemy.getMaskRadius()) {	
				double dist = enemy.getPosition().distanceToSegment(motion);
				
				if (dist < enemy.getMaskRadius()) {
					enemy.damage(damage);
					super.destroy();
				}
			}
		}
		
		// Check if out of bounds:
		if (radius > Settings.RING_SPAWN_RADIUS)
			destroy();
	}

	@Override
	public void draw() {
		Draw.draw(sprite, radius, angle);
	}
}
