

import java.awt.Color;
import java.awt.Graphics;


public class Line {
	public double x1;
	public double y1;
	public double x2;
	public double y2;
	
	public Line(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public Line(Node n1, Node n2) {
		this.x1 = n1.x;
		this.y1 = n1.y;
		this.x2 = n2.x;
		this.y2 = n2.y;
	}

	public Line(Unit n1, Unit n2) {
		this.x1 = n1.x;
		this.y1 = n1.y;
		this.x2 = n2.x;
		this.y2 = n2.y;
	}
	
	public Line(Vector v1, Vector v2) {
		this.x1 = v1.x;
		this.y1 = v1.y;
		this.x2 = v2.x;
		this.y2 = v2.y;
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
	
	public double getSlope() {
		if (isVertical())
			return 0;
		return ((y2 - y1) / (x2 - x1));
	}
	
	public double getM() {
		return getSlope();
	}
	
	public double getIntercept() {
		return (y1 - (x1 * getSlope()));
	}
	
	public double getB() {
		return getIntercept();
	}
	
	public double getY(double X) {
		// y = mx + b
		return ((getM() * X) + getB());
	}

	public Vector getEnd1() {
		return new Vector(x1, y1);
	}

	public Vector getEnd2() {
		return new Vector(x2, y2);
	}
	
	public double length() {
		return GMath.distance(this);
	}
	
	public double pointDistance(double x, double y) {
		Vector P = new Vector(x, y);
		Vector v = getEnd2().subtract(getEnd1());
	    Vector w = P.subtract(getEnd1());

	    double c1 = w.dotProduct(v);
	    if (c1 <= 0)
	        return GMath.distance(new Vector(x, y), getEnd1());

	    double c2 = length();
	    if (c2 <= c1)
	        return GMath.distance(new Vector(x, y), getEnd2());

	    double b = c1 / c2;
	    Vector Pb = getEnd1().add(v.scale(b));
	    return GMath.distance(new Vector(x, y), Pb);
	}
	
	public Vector pointClosest(double x, double y) {
		Vector P = new Vector(x, y);
		Vector v = getEnd2().subtract(getEnd1());
	    Vector w = P.subtract(getEnd1());

	    double c1 = w.dotProduct(v);
	    if (c1 <= 0)
	        return getEnd1();

	    double c2 = v.length();
	    if (c2 <= c1)
	        return getEnd2();

	    double b = c1 / c2;
	    Vector Pb = getEnd1().add(v.scale(b));
	    return Pb;
	}

	public double distance() {
		return GMath.distance(x1, y1, x2, y2);
	}
	
	public void draw(Graphics g) {
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
	}
	
	public void draw(Graphics g, Color col) {
		g.setColor(col);
		draw(g);
	}
}
