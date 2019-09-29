


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
	
	public static int modulus(int a, int b) {
		double aa = (double) a;
		double bb = (double) b;
		double digits = (aa / bb) - ((double) ((int) (aa / bb)));
		return (int) (digits * bb);
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
	
	public static double clamp(double x, double min, double max) {
		return Math.max(min, Math.min(x, max));
	}
	
	public static double distance(double x, double y) {
		return Math.sqrt(sqr(x) + sqr(y));
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt(sqr(x2 - x1) + sqr(y2 - y1));
	}
	
	public static double distance(Vector v1, Vector v2) {
		return distance(v1.x, v1.y, v2.x, v2.y);
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

	public static double direction(Vector v1, Vector v2) {
		return direction(v2.x - v1.x, v2.y - v1.y);
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
