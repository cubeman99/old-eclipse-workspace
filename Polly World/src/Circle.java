import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;


public class Circle {
	public float radius;
	public float x;
	public float y;
	
	public Circle(float x, float y, float radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	
	public boolean colliding(Circle c) {
		return Collision.circleCircle(this, c);
	}
	
	public IntersectionSolutions circleIntersection(Circle c) {
		boolean print = PollyWorld.keyPressed[KeyEvent.VK_SPACE];
		
		Vector c1 = new Vector(x, y);
		Vector c2 = new Vector(c.x, c.y);
		float d = c1.distance(c2);
		
		if( d > radius + c.radius || d < Math.abs(radius - c.radius) ) {
			// NO solutions
			return new IntersectionSolutions();
		}
		else if( d == radius + c.radius ) {
			// ONE solution
			Vector v = c1.add(c2.sub(c1).normalise().scale(radius));
			return new IntersectionSolutions(v);
		}
		
		// Else, TWO solutions
		// find equation of intersection line
		
		float mx = -(2.0f * c1.x) + (2.0f * c2.x);
		float bx = (c1.x * c1.x) - (c2.x * c2.x);
		float my = -(2.0f * c1.y) + (2.0f * c2.y);
		float by = (c1.y * c1.y) - (c2.y * c2.y);
		float a  = (radius * radius) - (c.radius * c.radius);
		
		float m  = -(mx / my);
		float b  = (a - bx - by) / my;
		
		float x1 = 0;
		float x2 = 500;
		
		Line l = new Line(x1, (m * x1) + b, x2, (m * x2) + b);
		
		/*
		Graphics g = PollyWorld.buffer.getGraphics();
		g.setColor(Color.red);
		for( int xx = 0; xx < 500; xx++ ) {
			g.drawRect((int)xx, (int)((m * xx) + b), 0, 0);
		}
		*/
		
		if( print ) {
			System.out.println("m = " + String.valueOf(m));
			System.out.println("b = " + String.valueOf(b));
		}
		
		return l.circleIntersections(this);
		
		/*
		float a = ((radius * radius) - (c.radius + c.radius) + (d * d)) / (2.0f * d);
		float h = (float) Math.sqrt((radius * radius) - (a * a));
		
		Vector P = new Vector(c1);
		P.add(new Vector(c2).sub(c1).normalise().scale(a));
		
		float nx1 = P.x + (h * ((c2.y - c1.y) / d));
		float nx2 = P.x - (h * ((c2.y - c1.y) / d));
		float ny1 = P.y	+ (h * ((c2.x - c1.x) / d));
		float ny2 = P.y - (h * ((c2.x - c1.x) / d));
		
		if( print ) {
			System.out.println("a = " + String.valueOf(a) + "(50)");
			System.out.println("h = " + String.valueOf(h));
		}
		
		
		return new IntersectionSolutions(P);
		//return new IntersectionSolutions(new Vector(nx1, ny1), new Vector(nx2, ny2));
		*/
	}
	
	public void draw(Graphics g, Color col) {
		g.setColor(col);
		g.drawOval((int)(x - radius), (int)(y - radius), (int)(radius * 2), (int)(radius * 2));
	}
}
