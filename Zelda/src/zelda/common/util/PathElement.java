package zelda.common.util;

import zelda.common.geometry.Vector;

public class PathElement {
	public static final int TYPE_LENGTH = 0; // Move a certain length
	public static final int TYPE_HIT    = 1; // Move until hitting a wall, pausing slightly
	public static final int TYPE_BOUNCE = 2; // Move until hitting a wall, not pausing
	private int dir;
	private int length;
	private int type;
	private double currentLength;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public PathElement(int dir, int length, int type) {
		this.dir    = dir;
		this.length = length;
		this.type   = type;
		this.currentLength = 0;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public boolean isDone() {
		return (type == TYPE_LENGTH && currentLength >= length);
	}
	
	public Vector getMotion(double speed) {
		return Direction.lengthVector(speed, dir);
	}
	
	public int getDir() {
		return dir;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getType() {
		return type;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void begin() {
		currentLength = 0;
	}
	
	public Vector nextMotion(double speed) {
		currentLength += speed / 16.0;
		return getMotion(speed);
	}
}
