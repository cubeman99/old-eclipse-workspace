package com.base.engine.rendering;

import com.base.engine.common.Vector2f;

public class Wave {
	private float amplitude;
	private float frequency;
	private float phaseTime;
	private Vector2f direction;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Wave(float amplitude, float waveLength, float speed, Vector2f direction) {
		this.amplitude = amplitude;
		this.frequency = 2 * ((float) Math.PI / waveLength);
		this.phaseTime = speed * frequency;
		this.direction = direction.normalized();
	}
	
	

	// =================== ACCESSORS =================== //
	
	public float getAmplitude() {
		return amplitude;
	}
	
	public Vector2f getDirection() {
		return direction;
	}
	
	public float getFrequency() {
		return frequency;
	}
	
	public float getPhaseTime() {
		return phaseTime;
	}
	
	public float getWaveLength() {
		return (frequency * 2 * (float) Math.PI);
	}
	
	public float getSpeed() {
		return phaseTime / frequency;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}
	
	public void setDirection(Vector2f direction) {
		this.direction = direction;
	}
	
	public void setFrequency(float frequency) {
		this.frequency = frequency;
	}
	
	public void setPhaseTime(float phaseTime) {
		this.phaseTime = phaseTime;
	}
	
	public void setWaveLength(float waveLength) {
		this.frequency = 2 * ((float) Math.PI / waveLength);
	}
	
	public void setSpeed(float speed) {
		this.phaseTime = speed * frequency;
	}
}
