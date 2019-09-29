package com.base.engine.entity.lights;

import com.base.engine.common.Matrix4f;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector3f;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.ShadowInfo;


public class SpotLight extends PointLight {
	public static final Shader forwardSpot = new Shader("forward-spot");
	private float cutoff;
	
	
	public SpotLight(Vector3f color, float intensity, Attenuation atten, float viewAngle, int shadowMapSizeIndex) {
		super(color, intensity, atten);
		this.cutoff = (float) Math.cos(viewAngle / 2);
		
		setShader(forwardSpot);
		
		if (shadowMapSizeIndex > 0 && shadowMapSizeIndex <= 10) {
    		setShadowInfo(new ShadowInfo(Matrix4f.createPerspective(viewAngle, 1.0f, 0.1f, getRange()), 
    				false, shadowMapSizeIndex, 1.0f, 0.2f, 0.00002f));
		}
	}
	

	public Quaternion getDirection() {
		return getTransform().getRotation();
	}

	public float getCutoff() {
		return cutoff;
	}

	public void setDirection(Vector3f direction) {
		getTransform().setDirection(direction);
	}

	public void setViewAngle(float viewAngle) {
		this.cutoff = (float) Math.cos(viewAngle / 2);
		getShadowInfo().setProjection(Matrix4f.createPerspective(viewAngle, 1.0f, 0.1f, getRange()));
	}

	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}
}
