package projects.fractals;

import java.util.Random;

public class NewPerlinNoise {
	private static Random random = new Random();
	
	public static Vector2f gradient(int gx, int gy) {
//		random.setSeed((((long) gx) << (8 * 4)) | ((long) gy));
		random.setSeed(((long) gx * 73856093L) + (long) gy);
//		System.out.println((((long) gx) << (8 * 4)) | ((long) gy));
//		System.out.println((((long) gx) << (8 * 4)));
		return new Vector2f(random.nextFloat(), random.nextFloat());//.normalize();
	}
	
	public static float getPerlinNoise(float x, float y) {
		float c1 = getInfluence(x, y, 0, 0);
		float c2 = getInfluence(x, y, 1, 0);
		float c3 = getInfluence(x, y, 0, 1);
		float c4 = getInfluence(x, y, 1, 1);
		
//		return (float) Math.abs(c1);
		
		return (float) Math.abs((((c1 + c2) / 2) + ((c3 + c4) / 2)) / 2);
	}
	
	private static float getInfluence(float x, float y, float offsetX, float offsetY) {
		int gx = floor(x + offsetX, 1);
		int gy = floor(y + offsetY, 1);
		return gradient(gx, gy).dot(new Vector2f(x - gx, y - gy));
	}
	
	private static float interpolate(float v1, float v2, float t) {
		float tt = (3 * (t * t)) - (2 * (t * t * t));
		return ((v1 * (1 - t)) + (t * v2));
		
	}
	
	public static int floor(float value, int period) {
		return ((int) Math.floor(value / (float) period)) * period;
	}
}
