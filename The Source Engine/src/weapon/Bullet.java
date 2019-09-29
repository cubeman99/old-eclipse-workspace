package weapon;

import java.awt.Color;

import common.Draw;
import common.GMath;
import common.Settings;
import common.Vector;
import common.shape.Line;
import common.shape.Polygon;
import control.Team;
import entity.Entity;
import entity.unit.Unit;


/**
 * Bullet.
 * 
 * @author David Jordan
 */
public class Bullet extends Entity {
	public Unit owner;
	public Vector position;
	public Vector positionLast;
	public Vector velocity;
	public double damage;
	public boolean dead;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Bullet(Unit owner, Vector position, Vector velocity, double damage) {
		super(Settings.DEPTH_BULLET);
		this.owner        = owner;
		this.position     = new Vector(position);
		this.positionLast = new Vector(position);
		this.velocity     = new Vector(velocity);
		this.damage       = damage;
		this.dead         = false;
	}
	
	
	// =================== ACCESSORS =================== //
	// ~ none yet...
	
	
	
	// ==================== MUTATORS ==================== //
	// ~ none yet...
	
	
	
	// =============== INHERITED METHODS =============== //

	@Override
	/** Update the bullet. **/
	public void update() {
		
		if (velocity.length() > GMath.EPSILON) {
    		positionLast.set(position);
    		position.add(velocity);
		}
		
		if (dead || (!owner.control.map.inBounds(position) && !owner.control.map.inBounds(positionLast))) {
			destroy();
			return;
		}
		
		Line moveLine = new Line(positionLast, position);
		
		double snapDistance = 0;
		Vector snapPoint    = null;
		Unit hurtUnit       = null;
		
		// Collide with walls:
		for (Polygon p : owner.control.map.walls) {
			for (int i = 0; i < p.vertexCount(); i++) {
				Line l = p.getEdge(i);
				Vector intersect = Line.intersection(l, moveLine);
				
				if (intersect != null) {
					dead = true;
					double d = positionLast.distanceTo(intersect);
					if (d < snapDistance || snapPoint == null) {
						snapDistance = d;
						snapPoint = intersect;
					}
				}
			}
		}
		// Collide with units:
		for (Team t : owner.control.teams) {
			if (t != owner.team) {
				for (Unit u : t.getAliveUnits()) {
					if (moveLine.touching(u.getCircle())) {
						Vector[] intersections = moveLine.intersectWithCircle(u.getCircle());
						
						for (int i = 0; i < intersections.length; i++) {
							double tempDist = intersections[i].distanceTo(positionLast);
							if (tempDist < snapDistance || snapPoint == null) {
								snapDistance = tempDist;
								snapPoint    = intersections[i];
								hurtUnit     = u;
							}
						}
					}
				}
				if (isDestroyed())
					break;
			}
		}
		
		if (snapPoint != null) {
			position.set(snapPoint);
			dead = true;
			if (hurtUnit != null)
				hurtUnit.damage(damage);
		}
	}
	
	@Override
	/** Draw the bullet. **/
	public void draw() {
		Draw.setColor(Color.WHITE);
		Draw.drawLine(positionLast, position);
	}
}
