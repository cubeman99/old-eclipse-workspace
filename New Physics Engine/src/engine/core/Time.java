package engine.core;

public class Time {
	private static final long SECOND = 1000000000L;
	static float delta;
	
	public static double getTime() {
		return (double) System.nanoTime() / (double) SECOND;
	}
	
	public static float getDelta() {
		return delta;
	}
}
