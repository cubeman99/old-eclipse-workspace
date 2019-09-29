


public class Collision {
	
	public static boolean lineRectangle(Line l, Rect r) {
		if( l.isVertical() ) {
			return( l.x1 >= r.getX1() && l.x1 < r.x + r.getX2() );
		}
		else {
			if( l.getY(r.getX1()) >= r.getY1() && l.getY(r.getX1()) < r.getY2() )
				return true;
			if( l.getY(r.getX2()) >= r.getY1() && l.getY(r.getX2()) < r.getY2() )
				return true;
		}
		return false;
	}
	
	public static boolean rectangleRectangle(Rect r1, Rect r2) {
		return( r1.getX2() > r2.getX1() &&
			    r1.getY2() > r2.getY1() &&
				r1.getX1() < r2.getX2() &&
				r1.getY1() < r2.getY2()
				);
	}
	
	
	public static boolean circleCircle(Circle c1, Circle c2) {
		return( MyMath.distance(c1.x, c1.y, c2.x, c2.y) < c1.radius + c2.radius );
	}
}
