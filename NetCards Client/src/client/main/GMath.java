package client.main;

import java.awt.Point;

public class GMath {
	public static double sin(double a) {
		return Math.sin(Math.toRadians(a));
	}

	public static double cos(double a) {
		return Math.cos(Math.toRadians(a));
	}

	public static double sqr(double a) {
		return (a * a);
	}

	public static int bool(boolean a) {
		if (a)
			return 1;
		return 0;
	}

	public static double dBool(boolean a) {
		if (a)
			return 1.0d;
		return 0.0d;
	}

	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(sqr(x2 - x1) + sqr(y2 - y1));
	}

	public static double distance(Point a, Point b) {
		return distance(a.x, a.y, b.x, b.y);
	}

	public static double dirSimp(double dir) {
		double newDir;
		newDir = dir;
		while (newDir >= 360.0d)
			newDir -= 360.0d;
		while (newDir < 0.0f)
			newDir += 360.0f;
		return newDir;
	}

	public static double direction(double x, double y) {
		double dir = 0.0d;
		if (x != 0.0d) {
			dir = -Math.toDegrees(Math.atan(-(y / x)));
			if (x < 0.0f)
				dir += 180.0d;
		}
		else if (y < 0.0d)
			dir = 270.0d;
		else if (y > 0.0d)
			dir = 90.0d;
		return dir;
	}

	public static double direction(Point a, Point b) {
		return direction(b.x - a.x, b.y - a.y);
	}
	
	public static Point lengthDirPoint(double length, double direction) {
		return new Point((int) (cos(direction) * length), (int) (sin(direction) * length));
	}
}
