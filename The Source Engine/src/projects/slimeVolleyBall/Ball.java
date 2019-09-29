package projects.slimeVolleyBall;

import java.awt.Color;
import projects.slimeVolleyBall.server.SlimeBallServer;
import projects.slimeVolleyBall.server.SlimeServerRunner;
import common.Collision;
import common.Draw;
import common.GMath;
import common.Vector;
import common.Collision.CollisionResult;
import common.shape.Circle;
import common.shape.Polygon;
import common.shape.Rectangle;


/**
 * Ball.
 * 
 * @author David Jordan
 */
public class Ball {
	private static final double BALL_GRAVITY = 0.22;
	private Vector position;
	private Vector velocity;
	private double radius;
	private double maxSpeed;
	private Color color;
	private Polygon net;
	
	// ================== CONSTRUCTORS ================== //
	
	public Ball(Rectangle net) {
		this.position = new Vector(Settings.VIEW_WIDTH / 2.0, Settings.FLOOR_Y - Settings.BALL_SERVE_HEIGHT);
		this.velocity = new Vector();
		this.radius   = Settings.BALL_RADIUS;
		this.maxSpeed = Settings.BALL_MAX_SPEED;
		this.color    = Settings.BALL_COLOR;
		this.net      = net.toPolygon();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Return the position of this ball. **/
	public Vector getPosition() {
		return position;
	}
	
	/** Return the velocity of this ball. **/
	public Vector getVelocity() {
		return velocity;
	}
	
	/** Return the radius of this ball. **/
	public double getRadius() {
		return radius;
	}

	/** Return a circle representing this ball's shape. **/
	private Circle getCircle() {
		return new Circle(position, radius);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Update the ball movement. **/
	public void update(SlimeBallServer control) {
		// Update movement:
		velocity.y += BALL_GRAVITY;
        position.add(velocity);
        
        // Check Collisions:
        for (Slime s : control.getPlayers())
        	collideWithSlime(s);
        collideWithWalls(control);
		collideWithNet();
		
		// Limit max speed:
		if (velocity.length() > maxSpeed)
			velocity.setLength(maxSpeed);
	}
	
	/** Check collisions with the net. **/
	private void collideWithNet() {
		CollisionResult result = Collision.getMinimumTranslationVector(getCircle(), net, velocity);
		
		if (result.willCollide) {
			position.add(result.minTranslationVector);
			velocity.set(velocity.projectionOn(result.collisionAxis).minus(velocity.rejectionOn(result.collisionAxis)));
		}
	}

	/** Check collisions with the map boundaries. **/
	private void collideWithWalls(SlimeBallServer control) {
		// Collide with walls:
		if (position.x <= control.getBoundaries().x1() + radius) {
			position.x = control.getBoundaries().x1() + radius;
			velocity.x = -velocity.x;
		}
		if (position.x >= control.getBoundaries().x2() - radius) {
			position.x = control.getBoundaries().x2() - radius;
			velocity.x = -velocity.x;
		}
		
		// Check if touching floor:
		if (position.y >= Settings.FLOOR_Y - radius) {
			position.y = Settings.FLOOR_Y - radius;
//			velocity.y = -velocity.y - Settings.BALL_GRAVITY;
			control.scorePoint(control.getOppositeSide(position));
			velocity.zero();
		}
	}

	/** Check collisions with a slime. **/
	private void collideWithSlime(Slime slime) {
		Vector slimePos = slime.getPosition();
        Vector slimeVel = slime.getVelocity();
		double dx       = position.x - slimePos.x;
		double dy       = position.y - slimePos.y;
		double dvx      = velocity.x - slimeVel.x;
		double dvy      = velocity.y - slimeVel.y;
		double distSqr  = (dx * dx) + (dy * dy);
		
		if (dy < 0 && distSqr <= GMath.sqr(radius + slime.getRadius()) && distSqr > 4) {
			double dist = Math.sqrt(distSqr);
			double proj = ((dx * dvx) + (dy * dvy)) / dist;
			position.x  = slimePos.x + (dx * (radius + slime.getRadius())) / dist;
			position.y  = slimePos.y + (dy * (radius + slime.getRadius())) / dist;
			
			if (proj <= 0) {
				velocity.x += slimeVel.x - (2.0 * dx * proj) / dist;
				
				if (velocity.x < -15)
					velocity.x = -15;
				if (velocity.x > 15)
					velocity.x = 15;
				
				velocity.y += slimeVel.y - (BALL_GRAVITY * 0.5) - (2.0 * dy * proj) / dist;
				
				if (velocity.y < -22)
					velocity.y = -22;
				if (velocity.y > 22)
					velocity.y = 22;
			}
		}
	}
	
	/** Draw the ball. **/
	public void draw() {
		Draw.setColor(color);
		Draw.fillCircle(position, radius);
	}
}
