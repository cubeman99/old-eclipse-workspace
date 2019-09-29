package common;


public class GMath {
	
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
	
	public static double distance(double dx, double dy) {
		return sqrt(sqr(dx) + sqr(dy));
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return distance(x2 - x1, y2 - y1);
	}
	
	public static double distance(Point p1, Point p2) {
		return distance(p2.x - p1.x, p2.y - p1.y);
	}
}
