package com.base.engine.common;

import java.util.Random;

public class GMath {
	public static final float PI              = (float) Math.PI;
	public static final float TWO_PI          = 2.0f * PI;
	public static final float INV_PI          = 1.0f / PI;
	public static final float HALF_PI         = PI / 2.0f;
	public static final float QUARTER_PI      = PI / 4.0f;
	public static final float THREE_HALVES_PI = TWO_PI - HALF_PI;
	public static final float EPSILON         = (float) 1.1920928955078125E-7;

	public static Random random = new Random();

	
	
	public static float toRadians(float theta) {
		return (float) Math.toRadians(theta);
	}

	public static float toDegrees(float theta) {
		return (float) Math.toDegrees(theta);
	}
	
	public static float sin(float x) {
		return (float) Math.sin(x);
	}

	public static float asin(float x) {
		return (float) Math.asin(x);
	}

	public static float cos(float x) {
		return (float) Math.cos(x);
	}

	public static float acos(float x) {
		return (float) Math.acos(x);
	}
	
	public static float tan(float x) {
		return (float) Math.tan(x);
	}

	public static float atan(float x) {
		return (float) Math.atan(x);
	}
	
	public static int mod(int x, int n) {
		return (x >= 0 ? x % n : (n - ((-x) % n)) % n);
	}
	
	public static float mod(float x, float n) {
		return (x >= 0.0f ? x % n : (n - ((-x) % n)) % n);
	}
	
	public static float distance(float dx, float dy) {
		return ((dx * dx) + (dy * dy));
	}
	
	public static float angleBetween(float angle1, float angle2) {
		if (angle2 > angle1)
			return (angle2 - angle1);
		return (GMath.TWO_PI - angle1 + angle2);
	}

	/** Return the smallest angle between two directions **/
	public static float smallestAngleBetween(float dir1, float dir2) {
		float a = (float) Math.abs((dir1 % TWO_PI) - (dir2 % TWO_PI));
		if (a > GMath.PI)
			return (float) Math.abs(TWO_PI - a);
		return a;
	}
	
	public static float getDirection(float x, float y) {
		float dir = 0.0f;
		if (x != 0.0f) {
			dir = ((float) Math.atan(y / x));
			if (x < 0.0f)
				dir += (float) Math.PI;
		}
		else if (y < 0)
			dir = (3.0f / 2.0f) * (float) Math.PI;
		else if (y > 0)
			dir = (float) Math.PI * 0.5f;
		if (dir < 0)
			dir =  ((float) Math.PI * 2.0f) + dir;
		return dir;
	}
}
