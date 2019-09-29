import java.awt.Color;
import java.awt.Graphics;


public class Line {
	public float x1;
	public float y1;
	public float x2;
	public float y2;
	
	public Line(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	public Line(Line l) {
		this.x1 = l.x1;
		this.y1 = l.y1;
		this.x2 = l.x2;
		this.y2 = l.y2;
	}
	
	public boolean isVertical() {
		return (x2 - x1 == 0);
	}
	
	public float getSlope() {
		return (y2 - y1) / (x2 - x1);
	}
	
	public float getM() {
		return getSlope();
	}
	
	public float getIntercept() {
		return y1 - (x1 * getSlope());
	}
	
	public float getB() {
		return getIntercept();
	}
	
	public float getY(float X) {
		// y = mx + b
		return( (getM() * X) + getB() );
	}
	
	
	public IntersectionSolutions circleIntersections(Circle c) {
		Vector p1 = new Vector(x1 - c.x, y1 - c.y);
		Vector p2 = new Vector(x2 - c.x, y2 - c.y);
		
		float dx = p2.x - p1.x;
		float dy = p2.y - p1.y;
		float dr = (float)Math.sqrt((float)(dx * dx) + (float)(dy * dy));
		float r  = c.radius;
		float D  = (p1.x * p2.y) - (p2.x * p1.y);
		
		float discriminant = (float) Math.sqrt(((r * r) * (dr * dr)) - (D * D));
		
		if( discriminant < 0 ) {
			// NO real solutions
			
			return new IntersectionSolutions();
		}
		if( discriminant == 0 ) {
			// ONE real solution
			float lx1 = ((D * dy) + (MyMath.sign2(dy) * dx *  discriminant)) / (dr * dr);
			float ly1 = ((-D * dx) + (Math.abs(dy) * discriminant)) / (dr * dr);
			
			return new IntersectionSolutions(new Vector(lx1 + c.x, ly1 + c.y));
		}
		
		// TWO real solutions
		float lx1 = ((D * dy) + (MyMath.sign2(dy) * dx *  discriminant)) / (dr * dr);
		float lx2 = ((D * dy) - (MyMath.sign2(dy) * dx * discriminant)) / (dr * dr);

		float ly1 = ((-D * dx) + (Math.abs(dy) * discriminant)) / (dr * dr);
		float ly2 = ((-D * dx) - (Math.abs(dy) * discriminant)) / (dr * dr);

		return new IntersectionSolutions(new Vector(lx1 + c.x, ly1 + c.y), new Vector(lx2 + c.x, ly2 + c.y));
	}
	
	public float distance() {
		return MyMath.distance(x1, y1, x2, y2);
	}
	
	public void draw(Graphics g, Color col) {
		g.setColor(col);
		g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
	}
}
