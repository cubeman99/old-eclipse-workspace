package projects.fractals;

import java.util.Random;

public class PerlinNoise {
	public static float[][][] generateWhiteNoise(int width, int height, int depth) {
		Random random = new Random();
		float[][][] noise = new float[width][height][depth];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < depth; z++) {
					noise[x][y][z] = (float) random.nextDouble() % 1;
				}
			}
		}

		return noise;
	}
	
	public static float[][][] generatePerlinNoise(float[][][] baseNoise, int octaveCount) {
		int width  = baseNoise.length;
		int height = baseNoise[0].length;
		int depth  = baseNoise[0][0].length;
		float persistance = 0.5f;

		float[][][] perlinNoise = new float[width][height][depth];
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;

		// Blend noise octaves together.
		for (int octave = octaveCount - 1; octave >= 0; octave--) {
			amplitude *= persistance;
			totalAmplitude += amplitude;

			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					for (int z = 0; z < depth; z++) {
    					perlinNoise[x][y][z] += createSmoothNoise(baseNoise, octave, x, y, z) * amplitude;
					}
				}
			}
		}

		// Normalize values.
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < depth; z++) {
					perlinNoise[x][y][z] /= totalAmplitude;
				}
			}
		}

		return perlinNoise;
	}

	private static float createSmoothNoise(float[][][] baseNoise, int octave, int x, int y, int z) {
		int width  = baseNoise.length;
		int height = baseNoise[0].length;
		int depth  = baseNoise[0][0].length;
		int samplePeriod      = 1 << octave; // calculates 2 ^ k
		float sampleFrequency = 1.0f / samplePeriod;

		// calculate the horizontal sampling indices
		int sampleX0 = floor(x, samplePeriod);
		int sampleX1 = (sampleX0 + samplePeriod) % width; // wrap around
		float blendX = (x - sampleX0) * sampleFrequency;
		
		// calculate the vertical sampling indices
		int sampleY0 = floor(y, samplePeriod);
		int sampleY1 = (sampleY0 + samplePeriod) % height; // wrap around
		float blendY = (y - sampleY0) * sampleFrequency;
		
		// calculate the depth sampling indices
		int sampleZ0 = floor(z, samplePeriod);
		int sampleZ1 = (sampleZ0 + samplePeriod) % depth; // wrap around
		float blendZ = (z - sampleZ0) * sampleFrequency;
		
		// Blend along x-axis (4 samples).
		float edgeX1 = interpolate(baseNoise[sampleX0][sampleY0][sampleZ0],
								   baseNoise[sampleX1][sampleY0][sampleZ0], blendX);
		float edgeX2 = interpolate(baseNoise[sampleX0][sampleY1][sampleZ0],
								   baseNoise[sampleX1][sampleY1][sampleZ0], blendX);
		float edgeX3 = interpolate(baseNoise[sampleX0][sampleY0][sampleZ1],
				   				   baseNoise[sampleX1][sampleY0][sampleZ1], blendX);
        float edgeX4 = interpolate(baseNoise[sampleX0][sampleY1][sampleZ1],
        				           baseNoise[sampleX1][sampleY1][sampleZ1], blendX);

        // Blend along y-axis (2 samples).
		float edgeY1 = interpolate(edgeX1, edgeX2, blendY);
		float edgeY2 = interpolate(edgeX3, edgeX4, blendY);

		// Blend along z-axis (1 sample).
		return interpolate(edgeY1, edgeY2, blendZ);
	}
	
	private static float interpolate2(float x0, float x1, float alpha) {
		return ((x0 * (1 - alpha)) + (alpha * x1));
	}
	
	private static float interpolate(float v1, float v2, float t) {
		// 3x^2 - 2x^3
		float tt = (3 * (t * t)) - (2 * (t * t * t));
		return ((v1 * (1 - t)) + (t * v2));
		
	}
	
	public static int floor(int value, int period) {
		return ((int) Math.floor(value / (float) period)) * period;
	}
}
