package simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import collision.AABB;
import collision.Circle;
import collision.Polygon;
import main.Game;
import main.Keyboard;
import main.Mouse;
import common.GMath;
import common.IntersectionSolutions;
import common.Line;
import common.Ray;
import common.Vector;
import dynamics.Body;
import dynamics.BodyType;

public class World {
	public Graphics graphics;
	public Vector gravity = new Vector(0.0, 0.0);
	
	public ArrayList<Body> bodies;
	public ArrayList<Line> staticLines;
	public ArrayList<Circle> staticCircles;
	
	public Vector dragPoint = null;
	
	public Vector debugPoint = null;
	
	
	public Line testLine1   = new Line(64, 64, 500, 350);
	public Line testLine2   = new Line(48, 300, 500, 200);
	
	public World(Graphics graphics) {
		this.graphics = graphics;
		
		staticLines   = new ArrayList<Line>();
		staticCircles = new ArrayList<Circle>();
		bodies        = new ArrayList<Body>(); //new Body(this, new ShapeCircle(new Vector(402, 48), 32), BodyType.DYNAMIC);
		
		createSineWave();
	}
	
	public void createSineWave() {
		double yp = 0;
		int interval = 24;
		for (int x = 0; x < Game.VIEW_WIDTH + interval; x += interval) {
			double y = 550.0 - (150.0 * GMath.cos(GMath.TWO_PI * ((double) x / (double) Game.VIEW_WIDTH)));
			if (x > 0)
				staticLines.add(new Line(x - interval, yp, x, y));
			yp = y;
		}
	}
	
	public void update() {
		Vector ms = Mouse.getVector();
		
		if (Keyboard.restart.pressed()) {
			staticLines.clear();
			staticCircles.clear();
			bodies.clear();
		}
		
		
		boolean freeHand = !Keyboard.shift.down();
		if (dragPoint != null) {
			if (freeHand && dragPoint.distanceTo(ms) > 16) {
				staticLines.add(new Line(dragPoint, ms));
				dragPoint = new Vector(ms);
			}
			if (!Mouse.left()) {
				if (!freeHand)
					staticLines.add(new Line(dragPoint, ms));
				dragPoint = null;
			}
		}
		else if (Mouse.leftPressed()) {
			dragPoint = new Vector(ms);
		}
		if (Mouse.rightPressed()) {
			staticCircles.add(new Circle(ms, 32));
		}
		
		if (Keyboard.space.pressed()) {
			Polygon poly = new Polygon(ms);
			poly.addVertex(0, 0);
			poly.addVertex(64, 0);
			poly.addVertex(64, 64);
			poly.addVertex(0, 64);
//			poly.addVertex(0, 16);
//			poly.addVertex(64, 0);
//			poly.addVertex(80, 64);
//			poly.addVertex(16, 64);
			if (Keyboard.control.down())
				bodies.add(new Body(this, poly, BodyType.DYNAMIC));
			else
				bodies.add(new Body(this, new Circle(ms, 32), BodyType.DYNAMIC));
		}
		
		/** Update the world physics. **/
		double amount = 0.1;
		
		for (Body body : bodies) {
			if (Keyboard.right.down())
				body.addMotion(Vector.vectorFromPolar(amount, 0));
			if (Keyboard.up.down())
				body.addMotion(Vector.vectorFromPolar(amount, GMath.HALF_PI));
			if (Keyboard.left.down())
				body.addMotion(Vector.vectorFromPolar(amount, GMath.PI));
			if (Keyboard.down.down())
				body.addMotion(Vector.vectorFromPolar(amount, GMath.THREE_HALVES_PI));
			
			body.update();
		}
	}
	
	
	public void drawVector(Vector v, double x, double y) {
		graphics.drawLine((int) x, (int) y, (int) (x + v.x), (int) (y + v.y));
	}
	
	public void drawVectorPoint(Vector v) {
		int r = 4;
		graphics.drawOval((int) v.x - r, (int) v.y - r, r * 2, r * 2);
	}
	
	public void drawLine(Line line) {
		graphics.drawLine((int) line.x1(), (int) line.y1(), (int) line.x2(), (int) line.y2());
	}
	
	public void drawCircle(Circle circle) {
		graphics.drawOval((int) circle.x1(), (int) circle.y1(), (int) circle.diameter(), (int) circle.diameter());
	}

	/** Draw the world. **/
	public void draw(Graphics g) {
		Vector ms = Mouse.getVector();
		graphics.setColor(Color.BLACK);
		
		if (dragPoint != null) {
			drawLine(new Line(dragPoint, ms));
		}
		
		for (Line line : staticLines) {
			drawLine(line);
		}
		for (Circle c : staticCircles) {
			drawCircle(c);
		}
		for (Body body : bodies) {
			body.draw();
		}
		
		
		/*
		graphics.setColor(Color.BLACK);
		Circle c1 = new Circle(testLine1.end1, 32);
		Circle c2 = new Circle(testLine2.end1, 80);
		drawLine(testLine1);
		drawLine(testLine2);
		drawCircle(c1);
		drawCircle(c2);
		double theta = GMath.angleBetween(testLine1.direction(), testLine2.direction());
		double a  = GMath.sin(theta) / (c1.radius + c2.radius);
		double d1 = c1.radius / GMath.tan(theta * 0.5);
		double d2 = GMath.sin(GMath.PI - theta - GMath.asin(d1 * a)) / a;
		Vector vI = Line.intersectionEndless(testLine1, testLine2);
		
		d1 = ((c1.radius + c2.radius) * 0.5) / GMath.sin(theta * 0.5);
		d2 = d1;
		
		if (Keyboard.left.down())
			testLine1.end1.set(ms);
		if (Keyboard.right.down())
			testLine2.end2.set(ms);
		if (Keyboard.up.down())
			testLine1.end2.set(ms);
		if (Keyboard.down.down())
			testLine2.end1.set(ms);
		

		double minDist = Line.shortestDistance(testLine1, testLine2);
		
		
		graphics.drawString("theta = " + theta, 10, 600);
		graphics.drawString("a = " + a, 10, 620);
		graphics.drawString("d1 = " + d1, 10, 640);
		graphics.drawString("d2 = " + d2, 10, 660);
		graphics.drawString("minDist = " + minDist, 10, 690);
		
		Vector snapPos = null;
		
		if (minDist < c1.radius) {
			Vector intersection = vI;
			boolean method2 = true;
			Vector newClose = new Vector();
			
			if (intersection != null) {
        		double dd = c1.radius / GMath.sin(theta);
        		graphics.drawString("d = " + dd, 10, 720);
        		
        		snapPos = intersection.minus(testLine1.getVector().setLength(dd));
        		
        		double xx = GMath.sqrt((dd * dd) - (c1.radius * c1.radius));
        		Vector displacement = testLine2.getVector().setLength(xx);
        		if (theta > GMath.HALF_PI)
        			displacement.negate();
        		newClose = intersection.minus(displacement);
        		method2  = !testLine2.aabbContains(newClose);
			}
    		
    		
			if (method2) {
				// Method 2:
				Vector anchor;
				graphics.setColor(Color.GREEN);
				if (snapPos == null) {
					anchor = testLine2.getClosestPoint(testLine1.end1);
					graphics.setColor(Color.MAGENTA);
				}
				else
					anchor = testLine2.getClosestPoint(snapPos);
				
				double dist   = anchor.distanceToLine(testLine1);
				double dd     = GMath.sqrt((c1.radius * c1.radius) - (dist * dist));
				snapPos       = anchor.projectionOn(testLine1).minus(testLine1.getVector().setLength(dd));
				
				drawVectorPoint(testLine1.getClosestPoint(anchor));
				
				graphics.setColor(Color.BLUE);
				drawVectorPoint(anchor);
			}
			else {
				graphics.setColor(Color.BLUE);
				drawVectorPoint(newClose);
			}
			
			graphics.setColor(Color.RED);
			drawVectorPoint(snapPos);
			
    		graphics.setColor(Color.LIGHT_GRAY);
    		drawCircle(new Circle(snapPos, c1.radius));
    		
		}
		else {
    		graphics.setColor(Color.LIGHT_GRAY);
    		drawCircle(new Circle(testLine1.end2, c1.radius));
		}
		
		if (snapPos != null) {
    		graphics.setColor(Color.GREEN);
    		drawCircle(new Circle(snapPos, c1.radius));
		}
		*/
		
		
//		else {
//    		graphics.setColor(Color.LIGHT_GRAY);
//    		drawCircle(new Circle(testLine1.end2, c1.radius));
//		}
		
		graphics.setColor(Color.LIGHT_GRAY);
		//drawCircle(new Circle(v1, c1.radius));
		//drawCircle(new Circle(v2, c2.radius));
		
		
		
		for (Body b1 : bodies) {
			AABB box = b1.shape.getAABB();
			graphics.setColor(Color.RED);
			//graphics.drawRect((int) box.x1(), (int) box.y1(), (int) box.width(), (int) box.height());
			
			if (b1.collisionPoint != null)
				this.drawVectorPoint(b1.collisionPoint);
			
			graphics.setColor(Color.CYAN);
			for (Body b2 : bodies) {
				if (b1 != b2) {
					if (AABB.testOverlap(b1.shape.getAABB(), b2.shape.getAABB())) {
						graphics.drawLine((int) b1.shape.centroid.plus(b1.shape.position).x,
										  (int) b1.shape.centroid.plus(b1.shape.position).y,
										  (int) b2.shape.centroid.plus(b2.shape.position).x,
										  (int) b2.shape.centroid.plus(b2.shape.position).y);
					}
				}
			}
		}
		
		if (debugPoint != null) {
			graphics.setColor(Color.RED);
			drawVectorPoint(debugPoint);
			debugPoint = null;
		}
		
		/*
		graphics.setColor(Color.BLACK);
		int Y = 24;
		for (Body body : bodies) {
			g.drawString("" + body.motion.length(), 20, Y);
			Y += 24;
		}
		*/
	}
}
