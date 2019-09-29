package com.base.engine.core;

import java.util.Random;

public class PerlinNoise3f {
	
	public static float noise1(int x) {
		 x = (x << 13) ^ x;
		 return (1.0f - ((x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff)) / 1073741824.0f + 1.0f;
	}
	
	private static float interpolate(float v1, float v2, float t) {
//		t = (3 * (t * t)) - (2 * (t * t * t)); // Cubic Interpolation
		return ((v1 * (1 - t)) + (t * v2));
	}
	
	
	public static float noise(int x, int y, int z) {
		return noise1((x * 200 * 200) + (y * 200) + z);
	}
	
	private static float interpolatedNoise(float x, float y, float z) {
		int intX    = (int) Math.floor(x);
		float fracX = (float) Math.abs(x - intX);
		int intY    = (int) Math.floor(y);
		float fracY = (float) Math.abs(y - intY);
		int intZ    = (int) Math.floor(z);
		float fracZ = (float) Math.abs(z - intZ);
		
		float xlerp1, xlerp2, ylerp1, ylerp2, zlerp1, zlerp2;
		
		xlerp1 = noise(intX + 0, intY + 0, intZ + 0);
		xlerp2 = noise(intX + 1, intY + 0, intZ + 0);
		ylerp1 = interpolate(xlerp1, xlerp2, fracX);
		
		xlerp1 = noise(intX + 0, intY + 1, intZ + 0);
		xlerp2 = noise(intX + 1, intY + 1, intZ + 0);
		ylerp2 = interpolate(xlerp1, xlerp2, fracX);
		
		zlerp1 = interpolate(ylerp1, ylerp2, fracY);
		
		xlerp1 = noise(intX + 0, intY + 0, intZ + 1);
		xlerp2 = noise(intX + 1, intY + 0, intZ + 1);
		ylerp1 = interpolate(xlerp1, xlerp2, fracX);
		
		xlerp1 = noise(intX + 0, intY + 1, intZ + 1);
		xlerp2 = noise(intX + 1, intY + 1, intZ + 1);
		ylerp2 = interpolate(xlerp1, xlerp2, fracX);
		
		zlerp2 = interpolate(ylerp1, ylerp2, fracY);
		
		return interpolate(zlerp1, zlerp2, fracZ);
	}
	
	public static float perlinNoise(float x, float y, float z, float persistance, int numOctaves) {
		float total     = 0;
		float frequency = 1;
		float amplitude = persistance;
		float totalAmplitude = 0;
		
		for (int i = 0; i < numOctaves; i++) {
			float noise = interpolatedNoise(x * frequency, y * frequency, z * frequency);
			total += noise * amplitude;
			totalAmplitude += amplitude;
			frequency *= 2;
			amplitude *= 2;
		}
//		System.out.println(total + "\t -- \t" + totalAmplitude);
		
		return total / totalAmplitude;
	}
}
