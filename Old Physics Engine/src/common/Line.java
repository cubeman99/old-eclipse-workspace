package common;

import collision.AABB;
import collision.Circle;



public class Line {
	public Vector end1, end2;
	

	public Line() {
		this(0, 0, 0, 0);
	}
	
	public Line(double x1, double y1, double x2, double y2) {
		this.end1 = new Vector(x1, y1);
		this.end2 = new Vector(x2, y2);
	}
	
	public Line(Vector end1, Vector end2) {
		this.end1 = new Vector(end1);
		this.end2 = new Vector(end2);
	}
	
	public Line(Vector end2) {
		this(0, 0, end2.x, end2.y);
	}
	
	public Line(Line l) {
		this(l.end1, l.end2);
	}
	
	
	public double x1() {
		return end1.x;
	}
	
	public double y1() {
		return end1.y;
	}
	
	public double x2() {
		return end2.x;
	}
	
	public double y2() {
		return end2.y;
	}
	
	public double minX() {
		return GMath.min(x1(), x2());
	}
	
	public double minY() {
		return GMath.min(y1(), y2());
	}
	
	public double maxX() {
		return GMath.max(x1(), x2());
	}
	
	public double maxY() {
		return GMath.max(y1(), y2());
	}
	
	public double width() {
		return (end2.x - end1.x);
	}
	
	public double height() {
		return (end2.y - end1.y);
	}
	
	public Vector getVector() {
		return end2.minus(end1);
	}
	
	public double length() {
		return end1.distanceTo(end2);
	}
	
	public boolean isHorizontal() {
		return (end1.y == end2.y);
	}
	
	public boolean isVertical() {
		return (end1.x == end2.x);
	}
	
	public double direction() {
		return getVector().direction();
	}
	
	public Vector getClosestPoint(Vector point) {
		Vector lineVector       = getVector();
		Vector fromVector       = point.minus(end1);
		double scalarProjection = fromVector.scalarProjection(getVector());
		
		if (scalarProjection <= 0)
			return new Vector(end1);
		if (scalarProjection >= length())
			return new Vector(end2);
		
		Vector projection = fromVector.projectionOn(lineVector);
		
		return end1.plus(projection);
	}
	
	public Vector intersection(Line l) {
		return Line.intersection(this, l);
	}
	

	public Vector intersectionEndless(Line l) {
		double det = (width() * l.height()) - (height() * l.width());
		if (det == 0.0)
			return null;
		
		double xi = -(l.width() * ((x1() * y2()) - (y1() * x2())) - (width() * ((l.x1() * l.y2()) - (l.y1() * l.x2())))) / det;
		double yi = -(l.height() * ((x1() * y2()) - (y1() * x2())) - (height() * ((l.x1() * l.y2()) - (l.y1() * l.x2())))) / det;
		
		return new Vector(xi, yi);
	}

	public Line set(double x1, double y1, double x2, double y2) {
		end1 = new Vector(x1, y1);
		end2 = new Vector(x2, y2);
		return this;
	}
	
	public Line set(Vector end1, Vector end2) {
		end1 = new Vector(end1);
		end2 = new Vector(end2);
		return this;
	}
	
	public Line set(Line l) {
		end1 = new Vector(l.end1);
		end2 = new Vector(l.end2);
		return this;
	}
	
	public Line swapEnds() {
		Vector temp = new Vector(end1);
		end1.set(end2);
		end2.set(temp);
		return this;
	}
	
	
	
	
	public IntersectionSolutions intersectCircle(Circle c) {
    	Vector p1 = new Vector(x1() - c.x(), y1() - c.y());
    	Vector p2 = new Vector(x2() - c.x(), y2() - c.y());
    	
    	double dx = p2.x - p1.x;
    	double dy = p2.y - p1.y;
    	double dr = p1.distanceTo(p2);
    	dr = Math.sqrt((dx * dx) + (dy * dy));
    	double r  = c.radius;
    	double D  = (p1.x * p2.y) - (p2.x * p1.y);
    	
    	double discriminant = Math.sqrt(((r * r) * (dr * dr)) - (D * D));
    	
    	if (discriminant < 0) {
    		// NO real solutions
    		return new IntersectionSolutions(new Vector[] {});
    	}
    	if (discriminant == 0) {
    		// ONE real solution
    		double lx1 = ((D * dy) + (GMath.sign2(dy) * dx *  discriminant)) / (dr * dr);
    		double ly1 = ((-D * dx) + (GMath.abs(dy) * discriminant)) / (dr * dr);
    		
    		Vector v = new Vector(lx1 + c.x(), ly1 + c.y());
    		if (v.x < minX() || v.x > maxX() || v.y < minY() || v.y > maxY())
        		return new IntersectionSolutions(new Vector[] {v});
    		
    		return new IntersectionSolutions(new Vector[] {});
    	}
//    	return new IntersectionSolutions();
    	
    	// TWO real solutions
    	double lx1 = ((D * dy) + (GMath.sign2(dy) * dx *  discriminant)) / (dr * dr);
    	double lx2 = ((D * dy) - (GMath.sign2(dy) * dx * discriminant)) / (dr * dr);
    
    	double ly1 = ((-D * dx) + (Math.abs(dy) * discriminant)) / (dr * dr);
    	double ly2 = ((-D * dx) - (Math.abs(dy) * discriminant)) / (dr * dr);
    	
    	Vector v1 = new Vector(lx1 + c.x(), ly1 + c.y());
    	Vector v2 = new Vector(lx2 + c.x(), ly2 + c.y());
    	
		if (v1.x < minX() || v1.x > maxX() || v1.y < minY() || v1.y > maxY())
    		v1 = null;
		if (v2.x < minX() || v2.x > maxX() || v2.y < minY() || v2.y > maxY())
    		v2 = null;
    	
    	return new IntersectionSolutions(new Vector[] {v1, v2});
	}
	
	public AABB getAABB() {
		return new AABB(minX(), minY(), maxX(), maxY());
	}
	
	public boolean aabbContains(Vector v) {
		return getAABB().contains(v);
	}
	
	public boolean pointInAABB(Vector v) {
		return getAABB().contains(v);
	}
	
	
	
	public static Vector intersection(Line l1, Line l2) {
		
		if (l1.isHorizontal()) {
			if (l2.isHorizontal())
				return null;
			if (l2.isVertical())
				return new Vector(l2.end1.x, l1.end1.y);
			
			double xi = l2.end2.x - ((l2.end2.y - l1.end2.y) * ((l2.end2.x - l2.end1.x) / (l2.end2.y - l2.end1.y)));
			if (xi > l1.maxX() || xi < l1.minX())
				return null;
			if (xi > l2.maxX() || xi < l2.minX())
				return null;
			return new Vector(xi, l1.end1.y);
		}
		else if (l1.isVertical()) {
			if (l2.isVertical())
				return null;
			if (l2.isHorizontal())
				return new Vector(l1.end1.x, l2.end1.y);
			
			double yi = l2.end2.y - ((l2.end2.x - l1.end2.x) * ((l2.end2.y - l2.end1.y) / (l2.end2.x - l2.end1.x)));
			if (yi > l1.maxY() || yi < l1.minY())
				return null;
			if (yi > l2.maxY() || yi < l2.minY())
				return null;
			return new Vector(l1.end1.x, yi);
		}
		else if (l2.isHorizontal() || l2.isVertical())
				return Line.intersection(l2, l1);
		
		
		double det = (l1.width() * l2.height()) - (l1.height() * l2.width());
		if (det == 0.0)
			return null;
		
		double xi = -(l2.width() * ((l1.x1() * l1.y2()) - (l1.y1() * l1.x2())) - (l1.width() * ((l2.x1() * l2.y2()) - (l2.y1() * l2.x2())))) / det;
		double yi = -(l2.height() * ((l1.x1() * l1.y2()) - (l1.y1() * l1.x2())) - (l1.height() * ((l2.x1() * l2.y2()) - (l2.y1() * l2.x2())))) / det;
		
		if (xi < l1.minX() || xi > l1.maxX())
			return null;
		if (xi < l2.minX() || xi > l2.maxX())
			return null;
		if (yi < l1.minY() || yi > l1.maxY())
			return null;
		if (yi < l2.minY() || yi > l2.maxY())
			return null;
		
		return new Vector(xi, yi);
	}
	
	public static Vector intersectionEndless(Line l1, Line l2) {
		if (l1.isHorizontal()) {
			if (l2.isHorizontal())
				return null; // parallel lines
			if (l2.isVertical())
				return new Vector(l2.end1.x, l1.end1.y);
			
			double xi = l2.end2.x - ((l2.end2.y - l1.end2.y) * ((l2.end2.x - l2.end1.x) / (l2.end2.y - l2.end1.y)));
			return new Vector(xi, l1.end1.y);
		}
		else if (l1.isVertical()) {
			if (l2.isVertical())
				return null; // parallel lines
			if (l2.isHorizontal())
				return new Vector(l1.end1.x, l2.end1.y);
			
			double yi = l2.end2.y - ((l2.end2.x - l1.end2.x) * ((l2.end2.y - l2.end1.y) / (l2.end2.x - l2.end1.x)));
			return new Vector(l1.end1.x, yi);
		}
		else if (l2.isHorizontal() || l2.isVertical())
				return Line.intersectionEndless(l2, l1);
		
		
		double det = (l1.width() * l2.height()) - (l1.height() * l2.width());
		if (det == 0.0)
			return null;
		
		double xi = -(l2.width() * ((l1.x1() * l1.y2()) - (l1.y1() * l1.x2())) - (l1.width() * ((l2.x1() * l2.y2()) - (l2.y1() * l2.x2())))) / det;
		double yi = -(l2.height() * ((l1.x1() * l1.y2()) - (l1.y1() * l1.x2())) - (l1.height() * ((l2.x1() * l2.y2()) - (l2.y1() * l2.x2())))) / det;
		
		return new Vector(xi, yi);
	}
	
	/** Return the shortest distance between line segments. **/
	public static double shortestDistance(Line l1, Line l2) {
		if (intersection(l1, l2) != null)
			return 0;
		
		return GMath.min(
				l1.end1.distanceToSegment(l2),
				l1.end2.distanceToSegment(l2),
				l2.end1.distanceToSegment(l1),
				l2.end2.distanceToSegment(l1),
				l1.end1.distanceTo(l2.end1),
				l1.end2.distanceTo(l2.end2),
				l1.end1.distanceTo(l2.end2),
				l1.end2.distanceTo(l2.end1)
		);
	}
}
