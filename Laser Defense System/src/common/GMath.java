package common;

import java.util.Random;


/**
 * This is the main Math class that contains many
 * math related functions including trigonometry,
 * and other various functions.
 * 
 * Note: All trigonometric functions work in radians.
 * Note: Direction starts from the right and goes counter-clockwise.
 * 
 * @author David Jordan
 */
public class GMath {
	public static final double PI              = Math.PI;
	public static final double TWO_PI          = 2.0 * PI;
	public static final double INV_PI          = 1.0 / PI;
	public static final double HALF_PI         = PI / 2.0;
	public static final double QUARTER_PI      = PI / 4.0;
	public static final double THREE_HALVES_PI = TWO_PI - HALF_PI;
	public static final double EPSILON         = 1.1920928955078125E-7;
	
	public static Random random = new Random();
	
	
	// ================= STATIC METHODS ================= //
	
	/** Return the degrees in a radian value. **/
	public static double toDegrees(double x) {
		return Math.toDegrees(x);
	}

	/** Return the radians in a degree value. **/
	public static double toRadians(double x) {
		return Math.toRadians(x);
	}

	/** Return the sine of a value. **/
	public static double sin(double x) {
		return Math.sin(x);
	}

	/** Return the cosine of a value. **/
	public static double cos(double x) {
		return Math.cos(x);
	}

	/** Return the tangent of a value. **/
	public static double tan(double x) {
		return Math.tan(x);
	}

	/** Return the inverse sine of a value. **/
	public static double asin(double x) {
		return Math.asin(x);
	}

	/** Return the inverse cosine of a value. **/
	public static double acos(double x) {
		return Math.acos(x);
	}
	
	/** Return a random number from zero to two pi. **/
	public static double getRandomAngle() {
		return (random.nextDouble() * TWO_PI);
	}

	/** Return the square of a value. **/
	public static double sqr(double x) {
		return (x * x);
	}

	/** Return the square root of a value. **/
	public static double sqrt(double x) {
		return Math.sqrt(x);
	}

	/** Return the sign of a value (-1, 0, or +1). **/
	public static double sign(double x) {
		return (x == 0 ? 0 : (x > 0 ? 1 : -1));
	}

	/** Return the sign of a value (-1 or +1). **/
	public static double sign2(double x) {
		return (x >= 0 ? 1 : -1);
	}

	/** Return the absolute value; the positive of a value. **/
	public static double abs(double x) {
		return (x >= 0 ? x : -x);
	}

	/** Return the maximum value out of a set of values. **/
	public static double max(double... args) {
		double max = args[0];
		for (int i = 1; i < args.length; i++) {
			if (args[i] > max)
				max = args[i];
		}
		return max;
	}

	/** Return the minimum value out of a set of values. **/
	public static double min(double... args) {
		double min = args[0];
		for (int i = 1; i < args.length; i++) {
			if (args[i] < min)
				min = args[i];
		}
		return min;
	}

	/** Return the length of a vector with components dx and dy. **/
	public static double distance(double dx, double dy) {
		return sqrt(sqr(dx) + sqr(dy));
	}

	/** Return the length of a vector. **/
	public static double distance(Vector v) {
		return distance(v.x, v.y);
	}

	/** Return the distance between to points. **/
	public static double distance(double x1, double y1, double x2, double y2) {
		return distance(x2 - x1, y2 - y1);
	}

	/** Return the distance between to vectors. **/
	public static double distance(Vector v1, Vector v2) {
		return distance(v2.x - v1.x, v2.y - v1.y);
	}
	
	/** Return a direction simplified to be within zero and two pi. **/
	public static double directionSimplify(double dir) {
		return getWrappedValue(dir, TWO_PI);
	}

	/** Return the direction of a vector with components dx and dy. **/
	public static double direction(double dx, double dy) {
		double dir = 0.0;
		if (dx != 0.0) {
		    dir = -((double) Math.atan(dy / dx));
		    if (dx < 0.0)
		        dir += PI;
		}
		else if (dy > 0)
		    dir = THREE_HALVES_PI;
		else if (dy < 0)
		    dir = HALF_PI;
		
		return dir;
	}

	/** Return the direction of a vector. **/
	public static double direction(Vector v) {
		return direction(v.x, v.y);
	}

	/** Return the direction from one point to another. **/
	public static double direction(double x1, double y1, double x2, double y2) {
		return direction(x2 - x1, y2 - y1);
	}

	/** Return the direction from one vector to another. **/
	public static double direction(Vector v1, Vector v2) {
		return direction(v2.x - v1.x, v2.y - v1.y);
	}

	/** Return the smallest angle between two directions **/
	public static double angleBetween(double dir1, double dir2) {
		double a = abs((dir1 % GMath.TWO_PI) - (dir2 % GMath.TWO_PI));
		if (a > GMath.PI)
			return abs(GMath.TWO_PI - a);
		return a;
	}
	
	/** Return a value wrapped around a certain boundary. **/
	public static int getWrappedValue(int value, int size) {
		return (value < 0 ? (size + (value % size)) % size : value % size);
	}
	
	/** Return a value wrapped around a certain boundary. **/
	public static double getWrappedValue(double value, double size) {
		return (value < 0 ? (size + (value % size)) % size : value % size);
	}
	
	/** Snap a point to a grid. **/
	public static Vector snapToGrid(Vector point, Vector gridOffset, Vector gridScale) {
		return point.set(getSnappedToGrid(point, gridOffset, gridScale));
	}

	/** Snap a point to a grid. **/
	public static Vector snapToGrid(Vector point, Vector gridOffset, double gridScale) {
		return snapToGrid(point, gridOffset, new Vector(gridScale, gridScale));
	}
	
	/** Return a snapped version of a point on a grid. **/
	public static Vector getSnappedToGrid(Vector point, Vector gridOffset, Vector gridScale) {
		return new Vector(
			((int) (((point.x - gridOffset.x) / gridScale.x) + 0.5) * gridScale.x) + gridOffset.x,
			((int) (((point.y - gridOffset.y) / gridScale.y) + 0.5) * gridScale.y) + gridOffset.y
		);
	}

	/** Snap a point to a grid. **/
	public static Vector getSnappedToGrid(Vector point, Vector gridOffset, double gridScale) {
		return getSnappedToGrid(point, gridOffset, new Vector(gridScale, gridScale));
	}
	
	/** Return on a random chance of one in the given total chances. **/
	public static boolean onChance(int chances) {
		return (random.nextInt(chances) == 0);
	}
}
