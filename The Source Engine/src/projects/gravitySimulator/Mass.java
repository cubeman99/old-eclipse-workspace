package projects.gravitySimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import main.ImageLoader;
import main.Mouse;
import common.Draw;
import common.GMath;
import common.HUD;
import common.Vector;
import common.shape.Circle;
import entity.Entity;



/**
 * A body of mass.
 * 
 * @author David Jordan
 */
public class Mass extends Entity {
	public Circle shape;
	public Vector velocity;
	public double elasticity;
	public double mass;
	public Color color;
	public boolean dragging;
	public boolean draggingVelocity;
	public boolean collided;
	public Mass collisionOther;
	public boolean dynamic;
	public TestCollision collision;
	public GravitySimulator control;
	private Vector[] trace;
	private int maxTraceSize;
	private int traceIndex;
	public Image image;
	public Vector imageSize;
	public boolean rotateFromSun;
	public int offset;
	private boolean drawTrail;
	private String name;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Mass(GravitySimulator control) {
		this(control, new Vector(32, 32), 16, new Vector());
	}
	
	public Mass(GravitySimulator control, Vector position, double radius, Vector velocity) {
		super(0);
		this.shape            = new Circle(position, radius);
		this.velocity         = new Vector(velocity);
		this.elasticity       = 0.5;
		this.mass             = GMath.PI * radius * radius;
		this.color            = Color.RED;
		
		this.dragging         = false;
		this.draggingVelocity = false;
		this.collision        = null;
		this.collided         = false;
		this.collisionOther   = null;
		this.dynamic          = true;
		this.control          = control;
		
		this.traceIndex       = 0;
		this.maxTraceSize     = 160;
		this.trace            = new Vector[maxTraceSize];
		
		this.image            = null;
		this.imageSize        = new Vector(32, 32);
		this.rotateFromSun    = false;
		this.offset           = 0;
		this.drawTrail        = true;
		this.name             = "?";
		
		this.color            = new Color(
			155 + GMath.random.nextInt(100),
			155 + GMath.random.nextInt(100),
			155 + GMath.random.nextInt(100)
		);
		
		for (int i = 0; i < trace.length; i++)
			trace[i] = new Vector(position);
	}
	
	
	// =================== ACCESSORS =================== //

	/** Return the position of this mass's center. **/
	public Vector getPosition() {
		return shape.position;
	}
	
	/** Return the velocity of this mass. **/
	public Vector getVelocity() {
		return velocity;
	}
	
	/** Return the radius of this mass in Earths. **/
	public double getRadius() {
		return shape.radius;
	}
	
	/** Return if this mass has a collision found. **/
	public boolean isColliding() {
		return (collision != null);
	}
	
	public String getName() {
		return name;
	}
	
	public Vector getTrace(int index) {
		return trace[index];
	}
	
	public double getMass() {
		return mass;
	}
	
	public double getMassKG() {
		return (mass * PlanetData.MASS_OF_EARTH);
	}
	
	/** Return the radius of this mass in kilometers. **/
	public int getRadiusKM() {
		return (int) (getRadius() * PlanetData.RADIUS_OF_EARTH + 0.5);
	}
	
	

	// ==================== MUTATORS ==================== //
	
	/** Set the position of this mass's center. **/
	public void setPosition(Vector position) {
		shape.position.set(position);
	}
	
	/** Set the velocity of this mass. **/
	public void setVelocity(Vector velocity) {
		this.velocity.set(velocity);
	}
	
	/** Combine another mass into this mass. **/
	public void addMass(Mass c) {
		mass += c.mass;
		shape.radius = GMath.sqrt((shape.computeArea() + c.shape.computeArea()) / GMath.PI);
	}

	/** Add gravity between all other masses and this mass. **/
	public void applyGravity() {
		for (int i = 0; i < control.entities.size(); i++) {
			Mass c   = control.entities.get(i);
			Vector gravity = new Vector(getPosition(), c.getPosition());
			
			if (dynamic && gravity.lengthSquared() > 0) {
				double fgrav = (GravitySimulator.GRAVITATIONAL_CONSTANT * mass * c.mass) / (gravity.lengthSquared());
				gravity.setLength(fgrav / mass);

				velocity.add(gravity);
			}
		}
	}
	
	/** Apply the velocity to this mass's position. **/
	public void move() {
		shape.position.add(velocity);
	}
	
	/** Check and react to any collisions. **/
	public void detectCollision(Mass c) {
		if (c.collisionOther != this)
			calculateNewVelocities(c);
	}
	
	/** Set an image for this to be drawn as **/
	public void setImage(Image img) {
		if (img == null)
			image = ImageLoader.getImage("moon");
		if (img != null) {
    		image     = img;
    		imageSize = new Vector(image.getWidth(null), image.getHeight(null));
		}
	}
	
	/** Set an image for this to be drawn as **/
	public void setImage(String imageName, boolean rotate, int offset) {
		image = ImageLoader.getImage(imageName.toLowerCase());
		if (image != null) {
			this.imageSize     = new Vector(image.getWidth(null), image.getHeight(null));
    		this.rotateFromSun = rotate;
    		this.offset        = offset;
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/** Set the reaction velocities of a collision pair. **/
	private void calculateNewVelocities(Mass c) {
		collisionOther = c;
		
    	double newVelX1 = (  velocity.x * (  mass - c.mass) + (2 * c.mass * c.velocity.x)) / (mass + c.mass);
    	double newVelX2 = (c.velocity.x * (c.mass -   mass) + (2 *   mass *   velocity.x)) / (mass + c.mass);
    	double newVelY1 = (  velocity.y * (  mass - c.mass) + (2 * c.mass * c.velocity.y)) / (mass + c.mass);
    	double newVelY2 = (c.velocity.y * (c.mass -   mass) + (2 *   mass *   velocity.y)) / (mass + c.mass);
    
    	if (dynamic) {
        	velocity.x   = newVelX1 * c.elasticity;
        	velocity.y   = newVelY1 * c.elasticity;
        	setPosition(new Vector(
        		getPosition().x + newVelX1,
        		getPosition().y + newVelY1
        	));
    	}
    	
    	if (c.dynamic) {
        	c.velocity.x = newVelX2 * elasticity;
        	c.velocity.y = newVelY2 * elasticity;
        	c.setPosition(new Vector(
        		c.getPosition().x + newVelX2,
        		c.getPosition().y + newVelY2
        	));
    	}
	}
	
	
	public void updateTrace() {
//		if (drawTrail) {
			trace[traceIndex++] = new Vector(getPosition());
    		if (traceIndex >= maxTraceSize)
    			traceIndex = 0;
//		}
	}
	
	public void drawInfo(int x, int y) {
		Graphics g = HUD.getGraphics();
		
		g.setColor(Color.WHITE);
		g.drawString(name, x, y);
		g.drawString("Mass: " + getMassKG() + " kg (" + mass + " Earths)", x, y + 30);
		g.drawString("Radius: " + getRadiusKM() + " km (" + getRadius() + " Earths)", x, y + 60);
	}
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	/** Update this mass. **/
	public void update() {
		collisionOther = null;
		move();
	}

	@Override
	/** Draw the mass. **/
	public void draw() {
		if (drawTrail) {
			Vector v  = null;
			
    		for (int i = 0; i < trace.length; i++) {
    			int index = (traceIndex + i) % trace.length;
    			double a  = ((double) i / (double) maxTraceSize);
    			Draw.setColor(new Color(
    				(int) (a * color.getRed()),
    				(int) (a * color.getGreen()),
    				(int) (a * color.getBlue())
    			));
    			if (v != null)
    				Draw.drawLine(v, trace[index]);
    			v = trace[index];
    		}
    		
			Draw.drawLine(getPosition(), trace[(traceIndex + trace.length - 1) % trace.length]);
		}
		
		if (draggingVelocity) {
			Draw.setColor(Color.GREEN);
			Draw.drawLine(getPosition(), Mouse.getVector());
		}
		
		if (image == null) {
			Draw.setColor(color);
			Draw.drawCircle(shape);
		}
		else {
			double scale = (getRadius() * 2) / (imageSize.x - (offset * 2.0));
			Draw.drawImage(image, getPosition().minus(new Vector(getRadius() + (offset * scale), getRadius() + (offset * scale))), 1.0 / scale);
		}
	}
}
