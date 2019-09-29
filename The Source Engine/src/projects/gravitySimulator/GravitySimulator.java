package projects.gravitySimulator;

import java.awt.Color;
import java.util.ArrayList;
import main.ImageLoader;
import main.Keyboard;
import main.Mouse;
import common.Draw;
import common.GMath;
import common.HUD;
import common.Vector;
import common.ViewControl;
import common.shape.Line;
import dynamics.PhysicsWorld;
import entity.Entity;



public class GravitySimulator {
	// Units:
	//  mass     = Earths
	//  distance = Earths
	//  time     = frames
	
	public static final double EARTH_RADIUS   = 1;
	public static final double EARTH_MASS     = 1;
	public static final double EARTH_DISTANCE = 23481.40009;
	public static final double EARTH_VELOCITY = (29.78 / 6378.1) / 60.0;
	
	public static final double SUN_RADIUS   = 109;
	public static final double SUN_MASS     = 333000;
	
	
	public static final double MOON_RADIUS   = 0.273;
	public static final double MOON_MASS     = 0.0123;
	public static final double MOON_DISTANCE = 60.2687;
	public static final double MOON_VELOCITY = (1.022 / 6378.1) / 60.0;
//	public static final double GRAVITATIONAL_CONSTANT = 0.00001;
//	public static final double GRAVITATIONAL_CONSTANT = 4.280708E-10 * 30000; // Calculated
//	public static final double GRAVITATIONAL_CONSTANT = 0.0004;
	public static final double SCALE = 100000; // 100000
//	public static final double GRAVITATIONAL_CONSTANT = (428.07080353 / (1000000.0 * 1000000.0)) * SCALE * SCALE;
	public static final double GRAVITATIONAL_CONSTANT = 0.00000000042807080353 * SCALE * SCALE;
	
//	public static final double EARTH_RADIUS = 1;
//	public static final double EARTH_MASS   = 1;
//	public static final double MOON_RADIUS   = 1737.1;
//	public static final double MOON_MASS     = 0.0123;
//	public static final double MOON_DISTANCE = 60.2687;
//	public static final double MOON_VELOCITY = (1.022 / 6378.1) / 60.0;
	
	private static final double VELOCITY_SCALE = 0.1;
	public ArrayList<Mass> entities;
	public boolean collide;
	public Vector dragPoint;
	public boolean dragging;
	public double radius;
	public ViewControl viewControl;
	public TestRunner runner;
	public double gridScale;
	public int planetViewIndex;
	
	public Mass sun;
	public Mass earth;
	public Mass moon;
	
	private double solarDistance;
	private long frameStartTime;
	private double currentFPS;
	private double elapsedTime;
	
	
	/** Create a new physics sandbox. **/
	public GravitySimulator(TestRunner runner) {
		this.runner = runner;
		entities    = new ArrayList<Mass>();
		viewControl = new ViewControl();
		dragging    = false;
		dragPoint   = new Vector();
		collide     = false;
		radius      = 9.4492;
		planetViewIndex = 5;
		gridScale   = 2;
		viewControl.setZoom(0.03);
		viewControl.setZoomMin(0.0001);
		viewControl.setZoomMax(-1);
		viewControl.zoomFollow(new Vector());
		ViewControl.viewSize = runner.getViewSize();
		frameStartTime = 0;
		elapsedTime    = 0;
		
//		viewControl.setZoom(10);
		
		
		PlanetData.createSolarSystem(this);
		
		System.out.println((2 * MOON_DISTANCE * GMath.PI) / (MOON_VELOCITY * 60 * 3600 * 24));
	}
	
	public Vector getMouseVector() {
		return viewControl.getGamePoint(Mouse.getVector());
	}
	
	/** Update the simulation. **/
	public void update() {
		double diff    = System.currentTimeMillis() - frameStartTime;
		if (diff > 0)
			currentFPS = 1000.0 / diff;
		frameStartTime = System.currentTimeMillis();
		boolean updateTrace = (frameStartTime % 5 == 0);
		
		elapsedTime += SCALE / (365.256363004 * 24.0 * 3600.0 * 60.0);
		
		viewControl.updateViewControls();
		
		if (Keyboard.left.pressed()) {
			planetViewIndex--;
			if (planetViewIndex < -1)
				planetViewIndex = PlanetData.planets.size() - 1;
		}
		else if (Keyboard.right.pressed()) {
				planetViewIndex++;
				if (planetViewIndex >= PlanetData.planets.size())
					planetViewIndex = -1;
			}
		
		if (Keyboard.backspace.pressed())
			entities.clear();
		
		if (Keyboard.up.down())
			radius += 3 / viewControl.zoom;
		if (Keyboard.down.down())
			radius = Math.max(0.001, radius - (3 / viewControl.zoom));
		
		if (dragging) {
			if (!Mouse.left.down()) {
				dragging = false;
				Vector motion = new Vector(dragPoint, getMouseVector()).scale(VELOCITY_SCALE);
				addMass(dragPoint, radius, motion);
			}
		}
		else if (Mouse.left.pressed()) {
			dragging = true;
			dragPoint = getMouseVector();
		}
		
		/*
		for (int i = 0; i < entities.size(); i++) {
			Mass c1 = entities.get(i);

			for (int j = 0; j < entities.size(); j++) {
				if (i != j) {
    				Mass c2 = entities.get(j);
    				
    				Vector gravity = new Vector(c1.getPosition(), c2.getPosition());
    				
    				if (c1.dynamic && gravity.lengthSquared() > 0) {
    					double fgrav = (GRAVITATIONAL_CONSTANT * c1.mass * c2.mass) / (gravity.lengthSquared());
    					
    						c1.velocity.add(gravity.scale(fgrav / c1.mass));
    					
    				}
				}
			}
		}
		*/
		/*
		 * 
		for (int i = 0; i < entities.size(); i++) {
			TestCircle c1 = entities.get(i);
			
			for (int j = 0; j < entities.size(); j++) {
				if (i != j) {
					TestCircle c2 = entities.get(j);
					
					if (c1.getPosition().distanceTo(c2.getPosition()) < c1.getRadius() + c2.getRadius()) {
						c1.detectCollision(c2);
					}
				}
			}
		}*/
		
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).applyGravity();
		}
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).update();
			if (updateTrace)
				entities.get(i).updateTrace();
			if (entities.get(i).isDestroyed())
				entities.remove(i--);
		}
		
		if (planetViewIndex >= 0)
			viewControl.zoomFollow(PlanetData.planets.get(planetViewIndex).getPosition());
	}
	
	
	/** Add an entity to the simulation. **/
	public Mass addMass(Vector position, double radius, Vector velocity) {
		return addEntity(new Mass(this, position, radius, velocity));
	}
	
	
	/** Add an entity to the simulation. **/
	public Mass addEntity(Mass e) {
		entities.add(e);
		return e;
	}
	
	private Vector getGridPoint(Vector point) {
		return new Vector(
			(int) (point.x / gridScale) * gridScale,
			(int) (point.y / gridScale) * gridScale
		);
	}
	
	private void drawGrid() {
		Vector start = getGridPoint(viewControl.pan);
		Vector end   = viewControl.getGamePoint(new Vector(runner.getViewWidth(), runner.getViewHeight()));
		
		for (double x = start.x; x < end.x; x += gridScale)
			Draw.drawLine(x, start.y, x, end.y);
		for (double y = start.y; y < end.y; y += gridScale)
			Draw.drawLine(start.x, y, end.x, y);
	}
	
	/** Draw all entities. **/
	public void draw() {
		HUD.getGraphics().setColor(Color.WHITE);
		HUD.getGraphics().drawString("fps = " + currentFPS, 10, 30);
		HUD.getGraphics().drawString("time = " + elapsedTime + " years", 10, 60);
		HUD.getGraphics().drawString("e = " + entities.size(), 10, 90);
		Draw.setView(viewControl);

		/*
		double dist = earth.getPosition().distanceTo(sun.getPosition());
		double diff = dist - solarDistance;
		
		Draw.setColor(dist > solarDistance ? Color.red : Color.YELLOW);
		if (GMath.abs(diff) < 1)
			Draw.setColor(Color.GREEN);
		HUD.getGraphics().drawString("dist = " + dist, 10, 60);
		HUD.getGraphics().drawString("diff = " + diff, 10, 100);
		*/
		
		
		if (gridScale * viewControl.zoom > 2) {
//    		Draw.setColor(new Color(40, 40, 40));
//    		drawGrid();
		}
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).draw();
		}
		
		if (dragging) {
			Line motion = new Line(dragPoint, getMouseVector());
			
			Draw.setColor(Color.GREEN);
			Draw.drawLine(motion);
			Draw.setColor(Color.GRAY);
			Draw.drawCircle(dragPoint, radius);

			Draw.setColor(Color.GREEN);
			
			
			Mass c = new Mass(this, dragPoint, radius, motion.getVector().scale(VELOCITY_SCALE));
			Vector v = new Vector(c.getPosition());
			
			for (int i = 0; i < 2000; i++) {
				c.applyGravity();
				c.move();
				Draw.drawLine(v, c.getPosition());
				v.set(c.getPosition());
			}
		}
		else {
			Draw.setColor(Color.GRAY);
			Draw.drawCircle(getMouseVector(), radius);
		}
		
		
		if (planetViewIndex >= 0)
			PlanetData.planets.get(planetViewIndex).drawInfo(16, 200);
	}
	
	public Vector getTrailOffset(int index) {
		if (planetViewIndex >= 0)
			return PlanetData.planets.get(planetViewIndex).getTrace(index);
		return new Vector();
	}
	
	public Vector getViewPoint() {
		if (planetViewIndex >= 0)
			return PlanetData.planets.get(planetViewIndex).getPosition();
		return new Vector();
	}
	
	public double detect(Mass c1, Mass c2) {
		
		Vector cDiff = c2.getPosition().minus(c1.getPosition());
		double c = cDiff.dot(cDiff) - GMath.sqr(c1.getRadius() + c2.getRadius());
		
		if (c < -GMath.EPSILON) {
			// All ready colliding at initial positions:
			return 0;
		}

		// Quadratic equation time:
		Vector vDiff = c2.getVelocity().minus(c1.getVelocity());
		
		double a = vDiff.dot(vDiff);
		if (a < GMath.EPSILON) {
			// Circles not moving relative each other
			return -1;
		}

		double b = vDiff.dot(cDiff);
		if (b >= 0) {
			// Circles moving apart
			return -1;
		}

		double d = (b * b) - (a * c);
		if (d < 0) {
			// Circles don't intersect
			return -1;
		}

		// Evaluate the time of collision
		double t = ((-b - GMath.sqrt( d )) / a);
		return (t <= 1 ? t : -1);
	}
	
	public void react(Mass g1, Mass g2, Mass c1, Mass c2, double t) {
		
		//assign the position at percentage t along each velocity vector to each ghost
		g1.setPosition(c1.getPosition().plus(c1.velocity.scaledBy(t)));
		g2.setPosition(c2.getPosition().plus(c2.velocity.scaledBy(t)));
		
		//difference vector between circles at time of collision
		Vector cDiff = g2.getPosition().minus(g1.getPosition());
		//normalized
		Vector collisionNormal = cDiff.normalized();
		
		//relative velocities of circles
		Vector relativeVelocity = c1.velocity.minus(c2.velocity);
		//coefficient of restitution range of 0 - 1.0
		//0 is completely inelastic (lump of clay) and 1 is purely elastic (superball)
		double restitution = Math.sqrt(c1.elasticity * c2.elasticity);
		
		//the impulse created at collision- 
		//this is given by the following equation (j is used to represent impulse)
		//j = -( 1 + e )vAB . n / n . n( 1 / mA + 1 / mB )
		//where e is the coefficient of restitution, vAB is the relative velocities
		//n is the collision normal and mA/mB are the respective masses of the different circles
		// "."s represent the vector dot product
		double impulse = collisionNormal.dot(relativeVelocity.scaledBy(-(1 + restitution)));
		impulse /= collisionNormal.dot(collisionNormal.scaledBy(1 / c1.mass + 1 / c2.mass));
		
		//scale each impulse along the collision normal by the circles' respective masses
		Vector reactionA = collisionNormal.scaledBy(impulse / c1.mass);
		Vector reactionB = collisionNormal.scaledBy(-impulse / c2.mass);
		
		//Multiply this by the the remainder of the interval (1 - t)
		g1.velocity = c1.velocity.plus(reactionA).scaledBy(1 - t);
		g2.velocity = c2.velocity.plus(reactionB).scaledBy(1 - t);
	}
}
