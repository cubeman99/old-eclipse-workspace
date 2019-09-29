

public class Collisions {

	// Line Segment to Line Segment Collision
	/*
	public static boolean segments(Line l1, Line l2) {
		return true;
	}
	*/
	
	public static boolean segmentRectangle(Line l, Rect r) {
		// First check if the Rectangle contains any of the Line's 2 end points
		if (r.containsPoint(l.getEnd1()))
			return true;
		if (r.containsPoint(l.getEnd2()))
			return true;
		

		// Then check if the line is Vertical:
		if (l.x1 == l.x2 && l.y1 <= r.getY1() && l.y2 >= r.getY2())
			return (l.x1 >= r.getX1() && l.x1 < r.getX2());
		
		// Then check if the line is Horizontal:
		if (l.y1 == l.y2 && l.x1 <= r.getX1() && l.x2 >= r.getX2())
			return (l.y1 >= r.getY1() && l.y1 < r.getY2());
		
		// Finally check if the closest point on the Line from each of the
		// Rectangle's corners is inside the Rectangle.
		if (r.containsPoint(l.pointClosest(r.getX1(), r.getY1())))
			return true;
		if (r.containsPoint(l.pointClosest(r.getX2(), r.getY1())))
			return true;
		if (r.containsPoint(l.pointClosest(r.getX1(), r.getY2())))
			return true;
		if (r.containsPoint(l.pointClosest(r.getX2(), r.getY2())))
			return true;
		
		// No collisions
		return false;
	}
	
	public static boolean circleRectangle(Circle c, Rect r) {
		Vector closest = rectPointClosest(c.getCenter(), r);
		return (closest.distance(c.getCenter()) < c.radius);
	}
	
	public static Vector rectPointClosest(Vector point, Rect r) {
		return new Vector(GMath.clamp(point.x, r.getX1(), r.getX2()), GMath.clamp(point.y, r.getY1(), r.getY2()));
	}
	
	public static boolean circleCircle(Circle c1, Circle c2) {
		return (GMath.distance(c1.getCenter(), c2.getCenter()) < c1.radius + c2.radius);
	}
}
