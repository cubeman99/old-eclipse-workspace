package projects.fractals;

import java.util.Random;

public class NewPerlin2 {
	private static Random random = new Random();
	
	public static float noise1(int x) {
		 x = (x << 13) ^ x;
		 return (1.0f - ((x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff)) / 1073741824.0f;
	}
	
	private static float interpolate(float v1, float v2, float t) {
//		t = (3 * (t * t)) - (2 * (t * t * t)); // Cubic Interpolation
		return ((v1 * (1 - t)) + (t * v2));
	}
	
	public static float noisef(float x, float y) {
		return noise((int) Math.floor(x), (int) Math.floor(y));
	}
	
	public static float noise(int x, int y) {
		return noise1(x * 200 + y);
//		int n = x + (y * 57);
//		n = (n << 13) ^ n;
//		random.setSeed(n);
//		return random.nextFloat();
//		return (1.0f - ((n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0f);
	}
	
	private static float smoothNoise(float x, float y) {
		float corners = (noisef(x - 1, y - 1) + noisef(x + 1, y - 1)
				+ noisef(x - 1, y + 1) + noisef(x + 1, y + 1)) / 16;
		float sides = (noisef(x - 1, y) + noisef(x + 1, y)
				+ noisef(x, y - 1) + noisef(x, y + 1)) / 8;
		float center = noisef(x, y) / 4;
		sides = 0;
		corners = 0;
		return corners + sides + center;
	}
	
	private static float interpolatedNoise(float x, float y) {
		int intX    = (int) Math.floor(x);
		float fracX = (float) Math.abs(x - intX);
		int intY    = (int) Math.floor(y);
		float fracY = (float) Math.abs(y - intY);
		
		float v1 = smoothNoise(intX + 0, intY + 0);
		float v2 = smoothNoise(intX + 1, intY + 0);
		float v3 = smoothNoise(intX + 0, intY + 1);
		float v4 = smoothNoise(intX + 1, intY + 1);
		
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		
		return interpolate(i1, i2, fracY);
	}
	
	public static float perlinNoise(float x, float y, float persistance, int numOctaves) {
		float total     = 0;
		float frequency = 1;
		float amplitude = persistance;
		
		for (int i = 0; i < numOctaves; i++) {
			total += interpolatedNoise(x * frequency, y * frequency) * amplitude;
			frequency *= 2;
			amplitude *= 2;
		}
		
		return total;
	}
}
