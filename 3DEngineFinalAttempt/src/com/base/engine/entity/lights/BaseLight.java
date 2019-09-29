package com.base.engine.entity.lights;

import com.base.engine.common.Vector3f;
import com.base.engine.core.Transform;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.ShadowInfo;


public abstract class BaseLight extends SceneObject {
	private Vector3f color;
	private float intensity;
	private Shader shader;
	private ShadowInfo shadowInfo;
	
	

	// ================== CONSTRUCTORS ================== //

	public BaseLight(Vector3f color, float intensity) {
		this.color      = color;
		this.intensity  = intensity;
		this.shader     = null;
		this.shadowInfo = null;
	}
	
	
	
	// =================== ACCESSORS =================== //

	public Transform getShadowCameraTransform(Transform mainCameraTransform) {
		return getTransform();
	}

	public Vector3f getColor() {
		return color;
	}

	public float getIntensity() {
		return intensity;
	}

	public Shader getShader() {
		return shader;
	}
	
	public ShadowInfo getShadowInfo() {
		return shadowInfo;
	}
	
	

	// ==================== MUTATORS ==================== //

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

    public void setShader(Shader shader) {
    	this.shader = shader;
    }
    
    public void setShadowInfo(ShadowInfo shadowInfo) {
		this.shadowInfo = shadowInfo;
	}
}
