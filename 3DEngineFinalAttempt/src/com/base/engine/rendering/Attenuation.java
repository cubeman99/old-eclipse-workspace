package com.base.engine.rendering;

public class Attenuation {
	private float constant;
	private float linear;
	private float exponent;
	
	
	// ================== CONSTRUCTORS ================== //

	public Attenuation(float constant, float linear, float exponent) {
		this.constant = constant;
		this.linear   = linear;
		this.exponent = exponent;
	}
	
	

	// =================== ACCESSORS =================== //

	public float getConstant() {
		return constant;
	}

	public float getLinear() {
		return linear;
	}

	public float getExponent() {
		return exponent;
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void setConstant(float constant) {
		this.constant = constant;
	}

	public void setLinear(float linear) {
		this.linear = linear;
	}

	public void setExponent(float exponent) {
		this.exponent = exponent;
	}
}
