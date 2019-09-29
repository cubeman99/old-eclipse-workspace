package com.base.engine.rendering;

import com.base.engine.common.Matrix4f;
import com.base.engine.common.Vector3f;
import com.base.engine.entity.SceneObject;


public class Camera extends SceneObject {
	private float fov;
	private float depthMin;
	private float depthMax;
	private float aspectRatio;
	private Matrix4f projection;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Camera() {
		this.projection = Matrix4f.createIdentity();
	}
	
	public Camera(Matrix4f projection) {
		this.projection = projection;
	}
	
	public Camera(float fov, float aspectRatio, float depthMin, float depthMax) {
		this.fov         = fov;
		this.depthMin    = depthMin;
		this.depthMax    = depthMax;
		this.aspectRatio = aspectRatio;
		
		projection = new Matrix4f();
		calculateProjection();
	}

	
	
	// =================== ACCESSORS =================== //
	
	public Matrix4f getViewProjection() {
		Vector3f forward     = getTransform().getTransformedRotation().getForward();
		Vector3f up          = getTransform().getTransformedRotation().getUp();
		Matrix4f rotation    = Matrix4f.createRotation(forward, up);
		Matrix4f translation = Matrix4f.createTranslation(getTransform().getTransformedPosition().inverse());
		return projection.times(rotation.times(translation));
	}
	
	public Matrix4f getProjection() {
		return projection;
	}
	
	public float getFov() {
		return fov;
	}
	
	public float getAspectRatio() {
		return aspectRatio;
	}
	
	public float getDepthMin() {
		return depthMin;
	}
	
	public float getDepthMax() {
		return depthMax;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setProjection(Matrix4f projection) {
		this.projection = projection;
	}
	
	public void setFov(float fov) {
		this.fov = fov;
		calculateProjection();
	}
	
	public void setAspectRatio(float aspectRatio) {
		this.aspectRatio = aspectRatio;
		calculateProjection();
	}
	
	public void setDepthMin(float depthMin) {
		this.depthMin = depthMin;
		calculateProjection();
	}
	
	public void setDepthMax(float depthMax) {
		this.depthMax = depthMax;
		calculateProjection();
	}
	
	private void calculateProjection() {
		projection.initPerspective(fov, aspectRatio, depthMin, depthMax);
	}
}
