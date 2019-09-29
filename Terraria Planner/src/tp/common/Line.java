package tp.common;


/**
 * Extension of Shape:
 * A class to represent a line segment with two
 * Vectors for end points.
 * 
 * @author David Jordan
 */
public class Line {
	public boolean horizontal;
	public boolean vertical;
	public Vector end1, end2;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Line() {
		this.end1 = new Vector();
		this.end2 = new Vector();
		this.horizontal = false;
		this.vertical   = false;
	}
	
	public Line(double x1, double y1, double x2, double y2) {
		this.end1 = new Vector(x1, y1);
		this.end2 = new Vector(x2, y2);
		this.horizontal = false;
		this.vertical   = false;
	}
	
	public Line(Vector end1, Vector end2) {
		this.end1 = new Vector(end1);
		this.end2 = new Vector(end2);
		this.horizontal = false;
		this.vertical   = false;
	}
	
	public Line(Vector end2) {
		this.end1 = new Vector();
		this.end2 = new Vector(end2);
		this.horizontal = false;
		this.vertical   = false;
	}
	
	public Line(Line l) {
		this.end1 = new Vector(l.end1);
		this.end2 = new Vector(l.end2);
		this.horizontal = false;
		this.vertical   = false;
	}

	
	
	// =================== ACCESSORS =================== //
	
	/** Return the x component of the first end point. **/
	public double x1() {
		return end1.x;
	}

	/** Return the y component of the first end point. **/
	public double y1() {
		return end1.y;
	}

	/** Return the x component of the second end point. **/
	public double x2() {
		return end2.x;
	}

	/** Return the y component of the second end point. **/
	public double y2() {
		return end2.y;
	}

	/** Return the minimum x value of the two end points. **/
	public double minX() {
		return GMath.min(x1(), x2());
	}

	/** Return the minimum y value of the two end points. **/
	public double minY() {
		return GMath.min(y1(), y2());
	}

	/** Return the maximum x value of the two end points. **/
	public double maxX() {
		return GMath.max(x1(), x2());
	}

	/** Return the maximum y value of the two end points. **/
	public double maxY() {
		return GMath.max(y1(), y2());
	}

	/** Return width of the line segment (Yes this can be negative). **/
	public double width() {
		return (end2.x - end1.x);
	}

	/** Return height of the line segment (Yes this can be negative). **/
	public double height() {
		return (end2.y - end1.y);
	}

	/** Return a vector representing the displacement between end points. **/
	public Vector getVector() {
		return end2.minus(end1);
	}

	/** Return whether the line segment is horizontal. **/
	public boolean isHorizontal() {
		return (GMath.abs(end1.y - end2.y) < GMath.EPSILON);
	}

	/** Return whether the line segment is vertical. **/
	public boolean isVertical() {
		return (GMath.abs(end1.x - end2.x) < GMath.EPSILON);
	}

	/** Return the length of the line. **/
	public double length() {
		return end1.distanceTo(end2);
	}

	/** Return the direction from end1 to end2. **/
	public double direction() {
		return getVector().direction();
	}

	/** Get the closest point on the line to a given point. **/
	public Vector getClosestPoint(Vector point) {
		Vector lineVector       = getVector();
		Vector fromVector       = point.minus(end1);
		double scalarProjection = fromVector.scalarProjection(lineVector);
		
		if (scalarProjection <= 0)
			return new Vector(end1);
		if (scalarProjection >= length())
			return new Vector(end2);
		
		Vector projection = fromVector.projectionOn(lineVector);
		
		return end1.plus(projection);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Set the x and y components for each endpoint of the line. **/
	public Line set(double x1, double y1, double x2, double y2) {
		end1 = new Vector(x1, y1);
		end2 = new Vector(x2, y2);
		return this;
	}

	/** Set the ends of this line with two vectors. **/
	public Line set(Vector end1, Vector end2) {
		end1 = new Vector(end1);
		end2 = new Vector(end2);
		return this;
	}
	
	/** Set this line as a copy of another line. **/
	public Line set(Line l) {
		end1 = new Vector(l.end1);
		end2 = new Vector(l.end2);
		return this;
	}
	
	/** Scale the line as if it were a vector. **/
	public Line scale(double amount) {
		if (amount < GMath.EPSILON) {
			end2.set(end1);
			return this;
		}
		Vector v = getVector().scale(amount);
		end2.set(end1.plus(v));
		return this;
	}
	
	/** Swap end1 with end2 and vice versa. **/
	public Line swapEnds() {
		Vector temp = new Vector(end1);
		end1.set(end2);
		end2.set(temp);
		return this;
	}
	

	
	// ================ STATIC METHODS ================ //
	
	/** Returns the intersection between two lines (if none, this returns null). **/
	public static Vector intersection(Line l1, Line l2) {
		if (l1.isHorizontal()) {
			if (l2.isHorizontal())
				return null;
			if (l2.isVertical()) {
				if (l2.minX() > l1.maxX() || l2.maxX() < l1.minX())
					return null;
				if (l1.minY() > l2.maxY() || l1.maxY() < l2.minY())
					return null;
				return new Vector(l2.end1.x, l1.end1.y);
			}

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
			if (l2.isHorizontal()) {
				if (l1.minX() > l2.maxX() || l1.maxX() < l2.minX())
					return null;
				if (l2.minY() > l1.maxY() || l2.maxY() < l1.minY())
					return null;
				return new Vector(l1.end1.x, l2.end1.y);
			}

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
