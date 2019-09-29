package com.base.game.rubiksCube;

public class Move {
	private int axis;
	private int layer;
	private boolean clockwise;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Move() {
	}
	
	public Move(Move copy) {
		this(copy.axis, copy.layer, copy.clockwise);
	}
	
	public Move(int axis, int layer, boolean clockwise) {
		this.axis      = axis;
		this.layer     = layer;
		this.clockwise = clockwise;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public int getAxis() {
		return axis;
	}
	
	public int getLayer() {
		return layer;
	}
	
	public boolean isClockwise() {
		return clockwise;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setAxis(int axis) {
		this.axis = axis;
	}
	
	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public void setClockwise(boolean clockwise) {
		this.clockwise = clockwise;
	}
}
