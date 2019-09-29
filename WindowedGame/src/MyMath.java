import java.util.Random;


public class MyMath {
	public static final double PI = Math.PI;
	public static Random random = new Random();
	
	public static float radians(float degrees) {
		return(degrees * ((float)PI / 180.0f));
	}
	public static float degrees(float radians) {
		return(radians * (180.0f / (float)PI));
	}
	
	public static float distance(float x1, float y1, float x2, float y2) {
		return( (float) Math.sqrt((float) (sqr(x2 - x1) + sqr(y2 - y1))) );
	}
	
	public static float sin(float degrees) {
		return (float) Math.sin(radians(degrees));
	}
	
	public static float cos(float degrees) {
		return (float) Math.cos(radians(degrees));
	}
	
	public static float sign(float a) {
		if( a > 0 )
			return 1.0f;
		else if( a < 0 )
			return -1.0f;
		else
			return 0.0f;
	}
	
	public static float sign2(float a) {
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
	
	public static float sqr(float a) {
		return a * a;
	}
	
	public static float random(float a) {
		return random.nextFloat() * a;
	}
	
	public static float random(float a, float b) {
		return a + (random.nextFloat() * (b - a));
	}
	
	public static int random(int a) {
		return Math.abs(random.nextInt(a));
	}
	
	public static int random(int a, int b) {
		return a + (Math.abs(random.nextInt(b - a)));
	}
	
	public static boolean chance(int possibilities) {
		return( random((float)possibilities) > (float)(possibilities - 1.0f) );
	}
}
