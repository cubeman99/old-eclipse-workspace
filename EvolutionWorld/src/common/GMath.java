package common;

import java.util.Random;


public class GMath {
	public static Random random = new Random();
	
	public static final double PI              = Math.PI;
	public static final double TWO_PI          = 2.0 * PI;
	public static final double INV_PI          = 1.0 / PI;
	public static final double HALF_PI         = PI / 2.0;
	public static final double QUARTER_PI      = PI / 4.0;
	public static final double THREE_HALVES_PI = TWO_PI - HALF_PI;
	public static final double PI_OVER_180     = PI / 180.0;
	public static final double PI_UNDER_180    = 180.0 / PI;
	public static final double EPSILON         = 1.1920928955078125E-7;
	
	
	
	public static double toRadians(double x) {
		return (x * PI_OVER_180);
	}
	
	public static double toDegrees(double x) {
		return (x * PI_UNDER_180);
	}
	
	public static double sin(double x) {
		return Math.sin(x);
	}
	
	public static double cos(double x) {
		return Math.cos(x);
	}
	
	public static double acos(double x) {
		return Math.acos(x);
	}
	
	public static double sqr(double x) {
		return (x * x);
	}

	public static double sqrt(double x) {
		return Math.sqrt(x);
	}

	public static double sign(double x) {
		return (x == 0 ? 0 : (x > 0 ? 1 : -1));
	}

	public static double sign2(double x) {
		return (x >= 0 ? 1 : -1);
	}
	
	public static double abs(double x) {
		return (x >= 0 ? x : -x);
	}
	
	public static double max(double... args) {
		double max = args[0];
		for (int i = 1; i < args.length; i++) {
			if (args[i] > max)
				max = args[i];
		}
		return max;
	}
	
	public static double min(double... args) {
		double min = args[0];
		for (int i = 1; i < args.length; i++) {
			if (args[i] < min)
				min = args[i];
		}
		return min;
	}
	
	public static double randomClamped() {
		return (random() - random());
	}
	
	public static double random() {
		return random.nextDouble();
	}
	
	public static double random(double x) {
		return (random.nextDouble() * x);
	}
	
	public static int randomInt(int x) {
		if (x == 0)
			return 0;
		return random.nextInt(x);
	}
	
	public static boolean randomBoolean() {
		return random.nextBoolean();
	}
	
	public static int choose(int...options) {
		if (options.length == 0)
			return 0;
		return options[random.nextInt(options.length)];
	}
	
	public static double distance(double dx, double dy) {
		return sqrt(sqr(dx) + sqr(dy));
	}
	
	public static double distance(Vector v) {
		return distance(v.x, v.y);
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return distance(x2 - x1, y2 - y1);
	}
	
	public static double distance(Vector v1, Vector v2) {
		return distance(v2.x - v1.x, v2.y - v1.y);
	}
	
	public static double direction(double x, double y) {
		double dir = 0.0;
		if (x != 0.0) {
		    dir = -((double) Math.atan(y / x));
		    if (x < 0.0)
		        dir += PI;
		}
		else if (y > 0)
		    dir = THREE_HALVES_PI;
		else if (y < 0)
		    dir = HALF_PI;
		
		return dir;
	}
	
	public static double direction(Vector v) {
		return direction(v.x, v.y);
	}
	
	public static double direction(double x1, double y1, double x2, double y2) {
		return direction(x2 - x1, y2 - y1);
	}
	
	public static double direction(Vector v1, Vector v2) {
		return direction(v2.x - v1.x, v2.y - v1.y);
	}
	
	public static double angleBetween(double dir1, double dir2) {
		double a = abs((dir1 % GMath.TWO_PI) - (dir2 % GMath.TWO_PI));
		if (a > GMath.PI)
			return abs(GMath.TWO_PI - a);
		return a;
	}
}
