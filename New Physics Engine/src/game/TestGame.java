package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import engine.core.AbstractGame;
import engine.core.CoreEngine;
import engine.core.Mouse;
import engine.core.Window;
import engine.math.Circle;
import engine.math.Shape;
import engine.math.Vector2f;
import engine.physics.Body;

public class TestGame extends AbstractGame {
	private Body placeBody;
	
	
	@Override
	public void initialize() {
		placeBody = null;
	}

	@Override
	public void update() {
		if (placeBody != null) {
			if (!Mouse.left.down()) {
				placeBody.setVelocity(Mouse.getVector().minus(placeBody.getPosition()).times(4));
				placeBody = null;
			}
		}
		else if (Mouse.left.pressed()) {
			placeBody = new Body();
			placeBody.setShape(new Circle(32));
			placeBody.setPosition(Mouse.getVector());
			getEngine().getPhysicsEngine().addBody(placeBody);
		}
		else if (Mouse.right.pressed()) {
			Body body = new Body();
			body.setShape(new Circle(32));
			body.setPosition(Mouse.getVector());
			body.setStatic();
			getEngine().getPhysicsEngine().addBody(body);
		}
	}

	@Override
	public void render() {
		Window.clear(Color.BLACK);
		Graphics g = Window.getGraphics();
		g.setColor(Color.WHITE);
		
		ArrayList<Body> bodies = getEngine().getPhysicsEngine().getBodies();
		
		for (int i = 0; i < bodies.size(); i++) {
			Body body = bodies.get(i);
			Shape shape = body.getShape();
			
			g.setColor(body.isDynamic() ? Color.WHITE : Color.YELLOW);
			
			if (shape instanceof Circle) {
				Circle c = (Circle) shape;
				Vector2f pos = c.getPosition().plus(body.getPosition());
				g.drawOval((int) (pos.x - c.getRadius()),
						   (int) (pos.y - c.getRadius()),
						   (int) c.getDiameter(),
						   (int) c.getDiameter());
			}
		}
	}

    
    public static void main(String[] args) {
    	CoreEngine engine = new CoreEngine(60, new TestGame());
    	engine.createWindow(640, 480, "Impulse Physics Engine");
    	engine.start();
    }
}