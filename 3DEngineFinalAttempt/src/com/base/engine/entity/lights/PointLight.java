package com.base.engine.entity.lights;

import com.base.engine.common.Vector3f;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.Shader;


public class PointLight extends BaseLight {
	public static final Shader forwardPoint = new Shader("forward-point");
	private static final int COLOR_DEPTH = 256;
	
	private Attenuation atten;
	private float range;
	
	public PointLight(Vector3f color, float intensity, Attenuation atten) {
		super(color, intensity);
		this.atten = atten;
		
		// Calculate the range of the light based on the attenuation equation.
		float a = atten.getExponent();
		float b = atten.getLinear();
		float c = atten.getConstant() - (COLOR_DEPTH * getIntensity() * getColor().max());
		this.range = (float) ((-b + Math.sqrt((b * b) - (4 * a * c))) / (2 * a));
		
		setShader(forwardPoint);
	}

	public Attenuation getAtten() {
		return atten;
	}

	public float getRange() {
		return range;
	}

	public Vector3f getPosition() {
		return getTransform().getPosition();
	}

	public void setAtten(Attenuation atten) {
		this.atten = atten;
	}

	public void setPosition(Vector3f position) {
		getTransform().setPosition(position);
	}

	public void setRange(float range) {
		this.range = range;
	}
}
