package com.base.engine.common;




/**
 * Extension of Shape:
 * A class to represent a line segment with two
 * Vector2fs for end points.
 * 
 * @author David Jordan
 */
public class Line2f {
	public boolean horizontal;
	public boolean vertical;
	public Vector2f end1, end2;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Line2f() {
		this.end1 = new Vector2f();
		this.end2 = new Vector2f();
		this.horizontal = false;
		this.vertical   = false;
	}
	
	public Line2f(float x1, float y1, float x2, float y2) {
		this.end1 = new Vector2f(x1, y1);
		this.end2 = new Vector2f(x2, y2);
		this.horizontal = false;
		this.vertical   = false;
	}
	
	public Line2f(Vector2f end1, Vector2f end2) {
		this.end1 = new Vector2f(end1);
		this.end2 = new Vector2f(end2);
		this.horizontal = false;
		this.vertical   = false;
	}
	
	public Line2f(Vector2f end2) {
		this.end1 = new Vector2f();
		this.end2 = new Vector2f(end2);
		this.horizontal = false;
		this.vertical   = false;
	}
	
	public Line2f(Line2f l) {
		this.end1 = new Vector2f(l.end1);
		this.end2 = new Vector2f(l.end2);
		this.horizontal = false;
		this.vertical   = false;
	}

	
	
	// =================== ACCESSORS =================== //
	
	/** Return the x component of the first end point. **/
	public float x1() {
		return end1.x;
	}

	/** Return the y component of the first end point. **/
	public float y1() {
		return end1.y;
	}

	/** Return the x component of the second end point. **/
	public float x2() {
		return end2.x;
	}

	/** Return the y component of the second end point. **/
	public float y2() {
		return end2.y;
	}

	/** Return the minimum x value of the two end points. **/
	public float minX() {
		return Math.min(x1(), x2());
	}

	/** Return the minimum y value of the two end points. **/
	public float minY() {
		return Math.min(y1(), y2());
	}

	/** Return the maximum x value of the two end points. **/
	public float maxX() {
		return Math.max(x1(), x2());
	}

	/** Return the maximum y value of the two end points. **/
	public float maxY() {
		return Math.max(y1(), y2());
	}
	
	public Vector2f getEnd(int side) {
		if (side <= 0)
			return end1;
		else
			return end2;
	}

	/** Return width of the line segment (Yes this can be negative). **/
	public float width() {
		return (end2.x - end1.x);
	}

	/** Return height of the line segment (Yes this can be negative). **/
	public float height() {
		return (end2.y - end1.y);
	}

	/** Return a vector representing the displacement between end points. **/
	public Vector2f getVector2f() {
		return end2.minus(end1);
	}

	/** Return whether the line segment is horizontal. **/
	public boolean isHorizontal() {
		return ((float) Math.abs(end1.y - end2.y) < GMath.EPSILON);
	}

	/** Return whether the line segment is vertical. **/
	public boolean isVertical() {
		return ((float) Math.abs(end1.x - end2.x) < GMath.EPSILON);
	}
	
	/** Return the center point of the line.  **/
	public Vector2f getCenter() {
		return end1.plus(end2).times(0.5f);
	}
	
	/** Return the length of the line. **/
	public float length() {
		return end1.distanceTo(end2);
	}
	
	public Line2f plus(Vector2f v) {
		return new Line2f(this).add(v);
	}
	
	public Line2f minus(Vector2f v) {
		return new Line2f(this).sub(v);
	}

	/** Return the direction from end1 to end2. **/
	public float direction() {
		return GMath.getDirection(end2.x - end1.x, end2.y - end1.y);
	}
	
	// TODO
//	public Vector2f getPoint(float ratio) {
//		return end1.plus(getVector2f().setLength(length() * ratio));
//	}

	// TODO
	/** Get the closest point on the line to a given point. **/
//	public Vector2f getClosestPoint(Vector2f point) {
//		Vector2f lineVector2f       = getVector2f();
//		Vector2f fromVector2f       = point.minus(end1);
//		float scalarProjection = fromVector2f.scalarProjection(lineVector2f);
//		
//		if (scalarProjection <= 0)
//			return new Vector2f(end1);
//		if (scalarProjection >= length())
//			return new Vector2f(end2);
//		
//		return end1.plus(fromVector2f.projectionOn(lineVector2f));
//	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Set the x and y components for each endpoint of the line. **/
	public Line2f set(float x1, float y1, float x2, float y2) {
		end1 = new Vector2f(x1, y1);
		end2 = new Vector2f(x2, y2);
		return this;
	}

	/** Set the ends of this line with two vectors. **/
	public Line2f set(Vector2f end1, Vector2f end2) {
		this.end1 = new Vector2f(end1);
		this.end2 = new Vector2f(end2);
		return this;
	}
	
	/** Set this line as a copy of another line. **/
	public Line2f set(Line2f l) {
		end1 = new Vector2f(l.end1);
		end2 = new Vector2f(l.end2);
		return this;
	}
	
	public Line2f add(Vector2f v) {
		end1.add(v);
		end2.add(v);
		return this;
	}
	
	public Line2f sub(Vector2f v) {
		end1.sub(v);
		end2.sub(v);
		return this;
	}
	
	/** Scale the line as if it were a vector. **/
	public Line2f scale(float amount) {
		if (amount < GMath.EPSILON) {
			end2.set(end1);
			return this;
		}
		Vector2f v = getVector2f().multiply(amount);
		end2.set(end1.plus(v));
		return this;
	}
	
	/** Swap end1 with end2 and vice versa. **/
	public Line2f swapEnds() {
		Vector2f temp = new Vector2f(end1);
		end1.set(end2);
		end2.set(temp);
		return this;
	}
	

	
	// ================ STATIC METHODS ================ //
	
	/** Returns the intersection between two lines (if none, this returns null). **/
	public static Vector2f intersection(Line2f l1, Line2f l2) {
		if (l1.isHorizontal()) {
			if (l2.isHorizontal())
				return null;
			if (l2.isVertical()) {
				if (l2.minX() > l1.maxX() || l2.maxX() < l1.minX())
					return null;
				if (l1.minY() > l2.maxY() || l1.maxY() < l2.minY())
					return null;
				return new Vector2f(l2.end1.x, l1.end1.y);
			}

			float xi = l2.end2.x - ((l2.end2.y - l1.end2.y) * ((l2.end2.x - l2.end1.x) / (l2.end2.y - l2.end1.y)));
			if (xi > l1.maxX() || xi < l1.minX())
				return null;
			if (xi > l2.maxX() || xi < l2.minX())
				return null;
			return new Vector2f(xi, l1.end1.y);
		}
		else if (l1.isVertical()) {
			if (l2.isVertical())
				return null;
			if (l2.isHorizontal()) {
				if (l1.minX() > l2.maxX() || l1.maxX() < l2.minX())
					return null;
				if (l2.minY() > l1.maxY() || l2.maxY() < l1.minY())
					return null;
				return new Vector2f(l1.end1.x, l2.end1.y);
			}

			float yi = l2.end2.y - ((l2.end2.x - l1.end2.x) * ((l2.end2.y - l2.end1.y) / (l2.end2.x - l2.end1.x)));
			if (yi > l1.maxY() || yi < l1.minY())
				return null;
			if (yi > l2.maxY() || yi < l2.minY())
				return null;
			return new Vector2f(l1.end1.x, yi);
		}
		else if (l2.isHorizontal() || l2.isVertical())
			return Line2f.intersection(l2, l1);


		float det = (l1.width() * l2.height()) - (l1.height() * l2.width());
		if (det == 0.0)
			return null;

		float xi = -(l2.width() * ((l1.x1() * l1.y2()) - (l1.y1() * l1.x2())) - (l1.width() * ((l2.x1() * l2.y2()) - (l2.y1() * l2.x2())))) / det;
		float yi = -(l2.height() * ((l1.x1() * l1.y2()) - (l1.y1() * l1.x2())) - (l1.height() * ((l2.x1() * l2.y2()) - (l2.y1() * l2.x2())))) / det;

		if (xi < l1.minX() || xi > l1.maxX())
			return null;
		if (xi < l2.minX() || xi > l2.maxX())
			return null;
		if (yi < l1.minY() || yi > l1.maxY())
			return null;
		if (yi < l2.minY() || yi > l2.maxY())
			return null;

		return new Vector2f(xi, yi);
	}
	
	
	
	public static Vector2f intersectionEndless(Line2f l1, Line2f l2) {
		if (l1.isHorizontal()) {
			if (l2.isHorizontal())
				return null; // parallel lines
			if (l2.isVertical())
				return new Vector2f(l2.end1.x, l1.end1.y);
			
			float xi = l2.end2.x - ((l2.end2.y - l1.end2.y) * ((l2.end2.x - l2.end1.x) / (l2.end2.y - l2.end1.y)));
			return new Vector2f(xi, l1.end1.y);
		}
		else if (l1.isVertical()) {
			if (l2.isVertical())
				return null; // parallel lines
			if (l2.isHorizontal())
				return new Vector2f(l1.end1.x, l2.end1.y);
			
			float yi = l2.end2.y - ((l2.end2.x - l1.end2.x) * ((l2.end2.y - l2.end1.y) / (l2.end2.x - l2.end1.x)));
			return new Vector2f(l1.end1.x, yi);
		}
		else if (l2.isHorizontal() || l2.isVertical())
				return Line2f.intersectionEndless(l2, l1);
		
		
		float det = (l1.width() * l2.height()) - (l1.height() * l2.width());
		if (det == 0.0)
			return null;
		
		float xi = -(l2.width() * ((l1.x1() * l1.y2()) - (l1.y1() * l1.x2())) - (l1.width() * ((l2.x1() * l2.y2()) - (l2.y1() * l2.x2())))) / det;
		float yi = -(l2.height() * ((l1.x1() * l1.y2()) - (l1.y1() * l1.x2())) - (l1.height() * ((l2.x1() * l2.y2()) - (l2.y1() * l2.x2())))) / det;
		
		return new Vector2f(xi, yi);
	}
	
	// TODO
	/** Return the shortest distance between line segments. **/
//	public static float shortestDistance(Line l1, Line l2) {
//		if (intersection(l1, l2) != null)
//			return 0;
//		
//		return GMath.min(
//				l1.end1.distanceToSegment(l2),
//				l1.end2.distanceToSegment(l2),
//				l2.end1.distanceToSegment(l1),
//				l2.end2.distanceToSegment(l1),
//				l1.end1.distanceTo(l2.end1),
//				l1.end2.distanceTo(l2.end2),
//				l1.end1.distanceTo(l2.end2),
//				l1.end2.distanceTo(l2.end1)
//		);
//	}
}
