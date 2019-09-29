package com.base.engine.core;

import com.base.engine.common.Matrix4f;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector3f;


public class Transform {
	private Transform parent;
	private Matrix4f parentMatrix;
	
	private Vector3f position;
	private Quaternion rotation;
	private Vector3f scale;
	
	private Vector3f prevPosition;
	private Quaternion prevRotation;
	private Vector3f prevScale;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Transform() {
		position     = new Vector3f(0, 0, 0);
		rotation     = new Quaternion(0, 0, 0, 1);
		scale        = new Vector3f(1, 1, 1);
		prevPosition = new Vector3f(0, 0, 0);
		prevRotation = new Quaternion(0, 0, 0, 1);
		prevScale    = new Vector3f(1, 1, 1);
		parentMatrix = new Matrix4f().initIdentity();
		parent       = null;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Matrix4f getTransformation() {
		Matrix4f positionMatrix = Matrix4f.createTranslation(position.x, position.y, position.z);
		Matrix4f rotationMatrix = rotation.toRotationMatrix();
		Matrix4f scaleMatrix    = Matrix4f.createScale(scale.x, scale.y, scale.z);
		return getParentMatrix().times(positionMatrix.times(rotationMatrix.times(scaleMatrix)));
	}

	private Matrix4f getParentMatrix() {
		if (parent != null && parent.hasChanged())
			parentMatrix = parent.getTransformation();
		return parentMatrix;
	}
	
	public Transform getParent() {
		return parent;
	}
	
	public Vector3f getPosition() {
		return position;
	}

	public Quaternion getRotation() {
		return rotation;
	}

	public Vector3f getTransformedPosition() {
		return getParentMatrix().transform(position);
	}

	public Quaternion getTransformedRotation() {
		Quaternion parentRotation = new Quaternion(0,0,0,1);

		if(parent != null)
			parentRotation = parent.getTransformedRotation();

		return parentRotation.times(rotation);
	}

	public Vector3f getScale() {
		return scale;
	}

	public boolean hasChanged() {
		if(parent != null && parent.hasChanged())
			return true;
		if (!position.equals(prevPosition))
			return true;
		if (!rotation.equals(prevRotation))
			return true;
		if (!scale.equals(prevScale))
			return true;
		return false;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void update() {
		prevPosition.set(position.plus(1.0f));
		prevRotation.set(rotation.times(0.5f));
		prevScale.set(scale.plus(1.0f));
	}

	public void setParent(Transform parent) {
		this.parent = parent;
	}
	
	public void set(Transform t) {
		position.set(t.getPosition());
		rotation.set(t.getRotation());
		scale.set(t.getScale());
	}
	
	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	public void setPosition(float x, float y, float z) {
		this.position.set(x, y, z);
	}

	public void setDirection(Vector3f dir) {
		this.rotation.setDirection(dir);
	}

	public void setRotation(Quaternion rotation) {
		this.rotation.set(rotation);
	}

	public void setRotation(float x, float y, float z, float w) {
		this.rotation.set(x, y, z, w);
	}

	public void setScale(Vector3f scale) {
		this.scale.set(scale);
	}

	public void setScale(float x, float y, float z) {
		this.scale.set(x, y, z);
	}

	public void setScale(float scale) {
		this.scale.set(scale, scale, scale);
	}

	public void rotate(Vector3f axis, float angle) {
		rotation = new Quaternion(axis, angle).times(rotation).normalized();
	}
}
