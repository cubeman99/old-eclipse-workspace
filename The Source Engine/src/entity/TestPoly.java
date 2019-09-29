package entity;

import java.awt.Color;

import main.Keyboard;
import main.Mouse;
import common.Collision;
import common.Draw;
import common.Vector;
import common.shape.Line;
import common.shape.Polygon;
import control.Control;


public class TestPoly extends Entity {
	public Polygon shape;
	public Control control;
	public Vector velocity;
	public Line debugEdge;
	
	public TestPoly(Control control) {
		super(-2);
		this.control = control;
		debugEdge = new Line();
		
		velocity = new Vector();
		
		// Right Triangle:
		shape = new Polygon(
			new Vector(0, 0),
			new Vector(0, 1),
			new Vector(1, 1)
		);
		
		// Weird Concave Polygon:
		shape = new Polygon(
			new Vector(-0.4, -0.3),
			new Vector(-0.5, -0.5),
			new Vector(-0.34, -0.55),
			new Vector(0.2, -0.3),
			new Vector(0.4, 0.0),
			new Vector(0.5, 0.3),
			new Vector(0.2, 0.2),
			new Vector(0.3, 0.5),
			new Vector(-0.3, 0.6)
		);
		
		
		shape.translate(new Vector(1, 1));
	}

	@Override
	public void update() {
		
		Vector move = new Vector();
		if (Keyboard.up.down())
			move.y -= 1;
		if (Keyboard.down.down())
			move.y += 1;
		if (Keyboard.left.down())
			move.x -= 1;
		if (Keyboard.right.down())
			move.x += 1;
		
		move.setLength(0.002);
		
//		if (!Keyboard.space.pressed() && !Keyboard.enter.down())
//			return;
		
		velocity.add(move);
		
		Collision.applyCollisions(control.map, shape, velocity);
		
		/*
		debugEdge = new Line();
		for (Polygon p : control.map.walls) {
			PolygonCollisionResult result = Polygon.polygonCollision(shape, p, velocity);
			System.out.println();
			System.out.println(result);
			
			if (result.willIntersect || result.intersect) {
				shape.translate(result.minimumTranslationVector);
				debugEdge = result.edge;
//				velocity.set(velocity.rejectionOn(result.edge.getVector().getPerpendicular()));
//				velocity.zero();
			}
		}
		*/
		
//		shape.translate(velocity);
	}
	
	public void debugCollisions() {
		Vector ms = control.viewControl.getGamePoint(Mouse.getVector());
		
		
		Vector testVelocity = new Vector(shape.getCenter(), ms);
		
		for (Polygon p : control.map.walls) {
			for (int i = 0; i < p.edgeCount(); i++) {
				Line edge = p.getEdge(i);
				
				testVelocity = Collision.getMinTranslationVector(shape, edge, testVelocity, new Vector());
			}
		}
		
		Draw.setColor(Color.GREEN);
		Draw.drawPolygon((Polygon) shape.getTranslated(testVelocity));
	}
	
	@Override
	public void draw() {
		Draw.setColor(Color.YELLOW);
		Draw.fillPolygon(shape);
		Draw.setColor(Color.RED);
		Draw.drawPolygon(shape);
//		Draw.setColor(Color.RED);
//		Draw.drawLine(debugEdge);
//		debugCollisions();
	}

}
