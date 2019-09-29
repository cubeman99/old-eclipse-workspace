package dynamics;

import java.util.ArrayList;
import common.Line;
import common.Vector;
import collision.Shape;
import collision.Circle;
import simulation.World;

/**
 * A rigid rody that exists in a world.
 * 
 * @author David Jordan
 */
public class Body {
	public ArrayList<Vector> collisionPoints;
	public BodyType type;
	public World world;
	public Shape shape;
	public Vector motion;
	public Vector nextMotion;
	
	public Vector stepMovement;
	
	public double mass;
	public double restitution;
	public double friction;
	public double anglularVelocity = 0;
	public double angle = 0;
	
	public Vector collisionPoint = null;
	
	
	public Body(World world, Shape shape, BodyType type) {
		this.shape           = shape;
		this.world           = world;
		this.type            = type;
		this.motion          = new Vector();
		this.nextMotion      = new Vector();
		this.collisionPoints = new ArrayList<Vector>();
		this.stepMovement    = new Vector();
		
		this.restitution = 0.0;
		this.friction    = 0.0;
		
		this.shape.setBody(this);
	}
	
	private void applyGravity() {
		motion.add(world.gravity);
	}
	
	public void addMotion(Vector motionAdd) {
		motion.add(motionAdd);
	}
	
	public void limitDirection(double direction) {
		motion.set(motion.rejectionOn(new Vector(0, 1).setDirection(direction)));
	}
	
	public void update() {
		collisionPoints.clear();
		collisionPoint = null;

		shape.positionLast.set(shape.position);
		shape.position.add(motion);
		
		motion.add(world.gravity);
		
		for (Body body : world.bodies) {
			if (this != body) {
				shape.collide(body.shape);
			}
		}
		
		//shape.position.add(motion);
		for (Line line : world.staticLines) {
//			shape.collide(line);
			shape.collideSnap(line);
		}
		for (Circle c : world.staticCircles) {
			shape.collide(c);
		}
		

//		nextMotion.add(world.gravity);
		//motion.set(nextMotion);
		
		angle += anglularVelocity;
	}
	
	public void draw() {
		shape.draw(world.graphics);
	}
}
