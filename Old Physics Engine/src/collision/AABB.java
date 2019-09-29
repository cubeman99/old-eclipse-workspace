package collision;

import common.Vector;

public class AABB {
	public static final double DEFUALT_GROW_AMOUNT = 10;
	
	public Vector lower;
	public Vector upper;
	
	public AABB() {
		this(0, 0, 0, 0);
	}

	public AABB(double x1, double y1, double x2, double y2) {
		this.lower = new Vector(x1, y1);
		this.upper = new Vector(x2, y2);
	}
	
	public AABB(Vector lower, Vector upper) {
		this.lower = new Vector(lower);
		this.upper = new Vector(upper);
	}
	
	public AABB(AABB copy) {
		this.lower = new Vector(copy.lower);
		this.upper = new Vector(copy.upper);
	}
	
	public double x1() {
		return lower.x;
	}
	
	public double y1() {
		return lower.y;
	}
	
	public double x2() {
		return upper.x;
	}
	
	public double y2() {
		return upper.y;
	}
	
	public double width() {
		return (upper.x - lower.x);
	}
	
	public double height() {
		return (upper.y - lower.y);
	}
	
	/** Return the parimeter. **/
	public double getParimeter() {
		return (2.0 * (upper.x - lower.x + upper.y - lower.y));
	}

	public void grow() {
		grow(DEFUALT_GROW_AMOUNT);
	}
	
	public void grow(double amount) {
		lower.x -= amount;
		lower.y -= amount;
		upper.x += amount;
		upper.y += amount;
	}

	/** Return true if this contains another AABB. **/
	public boolean contains(AABB aabb) {
		return (lower.x > aabb.lower.x
				&& lower.y > aabb.lower.y
				&& aabb.upper.x > upper.x
				&& aabb.upper.y > upper.y);
	}

	/** Return true if this contains a point. **/
	public boolean contains(Vector v) {
		return (v.x >= lower.x && v.y >= lower.y && v.x <= upper.x && v.y <= upper.y);
	}
	
	/** Return the center point of this AABB **/
	public Vector getCenter() {
		Vector center = new Vector(lower);
		center.add(upper).scale(0.5);
		return center;
	}

	
	/** Return true if two AABBs are touching (overlapping). {@link}**/
	public static boolean testOverlap(AABB a, AABB b) {
		if (b.lower.x - a.upper.x > 0.0f || b.lower.y - a.upper.y > 0.0f)
			return false;
		if (a.lower.x - b.upper.x > 0.0f || a.lower.y - b.upper.y > 0.0f)
			return false;
		return true;
	}
}
