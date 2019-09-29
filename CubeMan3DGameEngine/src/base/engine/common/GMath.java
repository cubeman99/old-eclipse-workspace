package base.engine.common;

import java.util.Random;


/**
 * This is the main Math class that contains many math related functions
 * including trigonometry, and other various functions.
 * 
 * Note: All trigonometric functions work in radians. Note: Direction starts
 * from the right and goes counter-clockwise.
 * 
 * @author David Jordan
 */
public class GMath {
	public static final float PI = (float) Math.PI;
	public static final float TWO_PI = 2 * PI;
	public static final float INV_PI = 1 / PI;
	public static final float HALF_PI = PI / 2;
	public static final float QUARTER_PI = PI / 4;
	public static final float THREE_HALVES_PI = TWO_PI - HALF_PI;
	public static final float EPSILON = (float) (1.1920928955078125E-7);

	public static Random random = new Random();


	// ================= STATIC METHODS ================= //

	public static int bool(boolean b) {
		return (b ? 1 : 0);
	}
	
	public static boolean toBool(int x) {
		return (x >= 1);
	}
	
	/** Return the degrees in a radian value. **/
	public static float toDegrees(float x) {
		return (float) Math.toDegrees(x);
	}

	/** Return the radians in a degree value. **/
	public static float toRadians(float x) {
		return (float) Math.toRadians(x);
	}

	/** Return the sine of a value. **/
	public static float sin(float x) {
		return (float) Math.sin(x);
	}

	/** Return the cosine of a value. **/
	public static float cos(float x) {
		return (float) Math.cos(x);
	}

	/** Return the tangent of a value. **/
	public static float tan(float x) {
		return (float) Math.tan(x);
	}

	/** Return the inverse sine of a value. **/
	public static float asin(float x) {
		return (float) Math.asin(x);
	}

	/** Return the inverse cosine of a value. **/
	public static float acos(float x) {
		return (float) Math.acos(x);
	}

	/** Return the square of a value. **/
	public static float sqr(float x) {
		return (x * x);
	}

	/** Return the square root of a value. **/
	public static float sqrt(float x) {
		return (float) Math.sqrt(x);
	}

	/** Return the sign of a value (-1, 0, or +1). **/
	public static float sign(float x) {
		return (x == 0 ? 0 : (x > 0 ? 1 : -1));
	}

	/** Return the sign of a value (-1, 0, or +1). **/
	public static int sign(int x) {
		return (x == 0 ? 0 : (x > 0 ? 1 : -1));
	}

	/** Return the sign of a value (-1 or +1). **/
	public static float sign2(float x) {
		return (x >= 0 ? 1 : -1);
	}
	
	/** Return the integer floor of a value. **/
	public static int floor(float x) {
		return ((int) Math.floor(x));
	}
	
	/** Return the absolute value; the positive of a value. **/
	public static float abs(float x) {
		return (x >= 0 ? x : -x);
	}

	/** Return the maximum value out of a set of values. **/
	public static float max(float... args) {
		float max = args[0];
		for (int i = 1; i < args.length; i++) {
			if (args[i] > max)
				max = args[i];
		}
		return max;
	}

	/** Return the minimum value out of a set of values. **/
	public static float min(float... args) {
		float min = args[0];
		for (int i = 1; i < args.length; i++) {
			if (args[i] < min)
				min = args[i];
		}
		return min;
	}

	/** Return the length of a vector with components dx and dy. **/
	public static float distance(float dx, float dy) {
		return sqrt(sqr(dx) + sqr(dy));
	}

	/** Return the length of a vector. **/
	public static float distance(Vector v) {
		return distance(v.x, v.y);
	}

	/** Return the distance between to points. **/
	public static float distance(float x1, float y1, float x2, float y2) {
		return distance(x2 - x1, y2 - y1);
	}

	/** Return the distance between to vectors. **/
	public static float distance(Vector v1, Vector v2) {
		return distance(v2.x - v1.x, v2.y - v1.y);
	}

	/** Return the direction of a vector with components dx and dy. **/
	public static float direction(float dx, float dy) {
		float dir = 0.0f;
		if (dx != 0.0f) {
			dir = -((float) Math.atan(dy / dx));
			if (dx < 0.0f)
				dir += PI;
		}
		else if (dy > 0)
			dir = THREE_HALVES_PI;
		else if (dy < 0)
			dir = HALF_PI;
		if (dir < 0)
			dir = GMath.TWO_PI + dir;
		return dir;
	}

	/** Return the direction of a vector. **/
	public static float direction(Vector v) {
		return direction(v.x, v.y);
	}

	/** Return the direction from one point to another. **/
	public static float direction(float x1, float y1, float x2, float y2) {
		return direction(x2 - x1, y2 - y1);
	}

	/** Return the direction from one vector to another. **/
	public static float direction(Vector v1, Vector v2) {
		return direction(v2.x - v1.x, v2.y - v1.y);
	}

	/** Return the smallest angle between two directions **/
	public static float angleBetween(float dir1, float dir2) {
		float a = abs((dir1 % GMath.TWO_PI) - (dir2 % GMath.TWO_PI));
		if (a > GMath.PI)
			return abs(GMath.TWO_PI - a);
		return a;
	}

	/** Return a value wrapped around a certain boundary. **/
	public static int getWrappedValue(int value, int size) {
		return (value < 0 ? (size + (value % size)) % size : value % size);
	}

	/** Return a value wrapped around a certain boundary. **/
	public static float getWrappedValue(float value, float size) {
		return (value < 0 ? (size + (value % size)) % size : value % size);
	}

	/** Snap a point to a grid. **/
	public static Vector snapToGrid(Vector point, Vector gridOffset,
			Vector gridScale) {
		return point.set(getSnappedToGrid(point, gridOffset, gridScale));
	}

	/** Snap a point to a grid. **/
	public static Vector snapToGrid(Vector point, Vector gridOffset,
			float gridScale) {
		return snapToGrid(point, gridOffset, new Vector(gridScale, gridScale));
	}

	/** Return a snapped version of a point on a grid. **/
	public static Vector getSnappedToGrid(Vector point, Vector gridOffset,
			Vector gridScale) {
		return new Vector(
				((int) (((point.x - gridOffset.x) / gridScale.x) + 0.5) * gridScale.x)
						+ gridOffset.x,
				((int) (((point.y - gridOffset.y) / gridScale.y) + 0.5) * gridScale.y)
						+ gridOffset.y);
	}

	/** Snap a point to a grid. **/
	public static Vector getSnappedToGrid(Vector point, Vector gridOffset,
			float gridScale) {
		return getSnappedToGrid(point, gridOffset, new Vector(gridScale,
				gridScale));
	}

	/** Return the number of frames equivalent to the given number of seconds. **/
	public static int seconds(int sec) {
		return (sec * 60);
	}
}