package main;

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
	
	public static int iBool(boolean b) {
		if (b) return 1;
		return 0;
	}
	
	public static double dBool(boolean b) {
		if (b) return 1.0d;
		return 0.0d;
	}
	
	public static Point add(Point p1, Point p2) {
		return new Point(p1.x + p2.x, p1.y + p2.y);
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(sqr(x2 - x1) + sqr(y2 - y1));
	}
	
	public static double distance(Point p1, Point p2) {
		return distance(p1.x, p1.y, p2.x, p2.y);
	}
	
	public static double dirSimp(double dir) {
		double d;
		d = dir;
		
		while( d >= 360.0d )
			d -= 360.0d;
		while( d < 0.0f )
			d += 360.0f;
		
		return d;
	}
	
	public static Point dirGet(int dir) {
		if (dir == 0) return new Point(1, 0);
		if (dir == 1) return new Point(0, -1);
		if (dir == 2) return new Point(-1, 0);
		return new Point(0, 1);
	}
	
	public static double direction(double x, double y) {
		double dir = 0.0d;
		if( x != 0.0d ) {
		    dir = -Math.toDegrees(Math.atan(-(y / x)));
		    if( x < 0.0f )
		        dir += 180.0d;
		}
		else if( y < 0.0d )
		    dir = 270.0d;
		else if( y > 0.0d )
		    dir = 90.0d;
		
		return dir;
	}
	
	public static double direction(Point p1, Point p2) {
		return direction(p2.x - p1.x, p2.y - p1.y);
	}
	
	public static double direction(double x1, double y1, double x2, double y2) {
		return direction(x2 - x1, y2 - y1);
	}
	

	public static double lenDirX(double len, double dir) {
		return (cos(dir) * len);
	}
	
	public static double lenDirY(double len, double dir) {
		return (sin(dir) * len);
	}
}
