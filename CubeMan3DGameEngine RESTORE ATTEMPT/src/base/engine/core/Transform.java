package base.engine.core;

import base.engine.common.Matrix4f;
import base.engine.common.Quaternion;
import base.engine.common.Vector3;

public class Transform {
	private static float depthMin;
	private static float depthMax;
	private static float width;
	private static float height;
	private static float fov;
	
	private Vector3 translation;
	private Quaternion rotation;
	private Vector3 scale;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Transform() {
		translation = new Vector3(0, 0, 0);
		rotation    = new Quaternion(0, 0, 0, 1);
		scale       = new Vector3(1, 1, 1);
		
		depthMin = 0;
		depthMax = 0;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Matrix4f getProjectedTransformation() {
		Matrix4f transformationMatrix = getTransformation();
		Matrix4f projectionMatrix = new Matrix4f().initPerspective(fov, width / height, depthMin, depthMax);
		
		return projectionMatrix.times(transformationMatrix);
	}
	
	public Matrix4f getTransformation() {
		Matrix4f translationMatrix = Matrix4f.createTranslation(translation.x, translation.y, translation.z);
		Matrix4f rotationMatrix    = rotation.toRotationMatrix();
		Matrix4f scaleMatrix       = Matrix4f.createScale(scale.x , scale.y, scale.z);
		
		return translationMatrix.times(rotationMatrix.times(scaleMatrix));
	}
	/*
	public Vector3 getTransformedPos()
	{
		return getParentMatrix().transform(pos);
	}

	public Quaternion getTransformedRot()
	{
		Quaternion parentRotation = new Quaternion(0,0,0,1);

		if(parent != null)
			parentRotation = parent.getTransformedRot();

		return parentRotation.mul(rot);
	}
	*/
	public Vector3 getTranslation() {
		return translation;
	}
	
	public Quaternion getRotation() {
		return rotation;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void rotate(Vector3 axis, float angle) {
		rotation = new Quaternion(axis, angle).times(rotation).normalized();
	}
	
	public static void setProjection(float fov, float width, float height, float depthMin, float depthMax) {
		Transform.fov      = fov;
		Transform.width    = width;
		Transform.height   = height;
		Transform.depthMin = depthMin;
		Transform.depthMax = depthMax;
	}
	
	public void setTranslation(Vector3 translation) {
		this.translation = translation;
	}
	
	public void setTranslation(float x, float y, float z) {
		this.translation = new Vector3(x, y, z);
	}
	
	public void setRotation(Quaternion rotation) {
		this.rotation = rotation;
	}
	
	public void setScalation(Vector3 scale) {
		this.scale = scale;
	}
	
	public void setScalation(float x, float y, float z) {
		this.scale = new Vector3(x, y, z);
	}
}
