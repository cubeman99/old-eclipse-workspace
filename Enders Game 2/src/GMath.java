import java.util.Random;

public class GMath {
	public static final double PI = Math.PI;
	public static Random random = new Random();
	
	public static double radians(double degrees) {
		return(degrees * ((double)PI / 180.0f));
	}
	public static double degrees(double radians) {
		return(radians * (180.0f / (double)PI));
	}
	
	public static double distance(double x1, double y1, double x2, double y2) {
		return( (double) Math.sqrt((double) (sqr(x2 - x1) + sqr(y2 - y1))) );
	}
	
	public static double sin(double degrees) {
		return (double) Math.sin(radians(degrees));
	}
	
	public static double cos(double degrees) {
		return (double) Math.cos(radians(degrees));
	}
	
	public static double sign(double a) {
		if( a > 0 )
			return 1.0f;
		else if( a < 0 )
			return -1.0f;
		else
			return 0.0f;
	}
	
	public static double sign2(double a) {
		if( a < 0 )
			return -1.0f;
		else
			return 1.0f;
	}
	
	public static int sign(int a) {
		if( a > 0 )
			return 1;
		else if( a < 0 )
			return -1;
		else
			return 0;
	}
	
	public static int sqr(int a) {
		return a * a;
	}
	
	public static double sqr(double a) {
		return a * a;
	}
	
	public static double distance(double x, double y) {
		return Math.sqrt(sqr(x) + sqr(y));
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
	
	public static double random(double a) {
		return random.nextFloat() * a;
	}
	
	public static double random(double a, double b) {
		return a + (random.nextFloat() * (b - a));
	}
	
	public static int random(int a) {
		return Math.abs(random.nextInt(a));
	}
	
	public static int random(int a, int b) {
		return a + (Math.abs(random.nextInt(b - a)));
	}
	
	public static boolean chance(int possibilities) {
		return( random((double)possibilities) > (double)(possibilities - 1.0f) );
	}
}
