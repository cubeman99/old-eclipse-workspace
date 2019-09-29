package base.engine.entity;

import base.engine.common.Matrix4f;
import base.engine.common.Vector3;
import base.engine.core.Transform;


public class Camera {
	private Matrix4f projection;
	private Transform transform;
	
	

	// ================== CONSTRUCTORS ================== //
	
	public Camera(float fov, float aspectRatio, float depthMin, float depthMax) {
		this.transform  = new Transform();
		this.projection = new Matrix4f().initPerspective(fov, aspectRatio, depthMin, depthMax);
	}
	
	
	
	// =================== ACCESSORS =================== //

	public Matrix4f getViewProjection() {
		Matrix4f cameraRotation    = transform.getRotation().conjugate().toRotationMatrix();
		Vector3 cameraPos          = transform.getTranslation().inverse();
		Matrix4f cameraTranslation = Matrix4f.createTranslation(cameraPos.x, cameraPos.y, cameraPos.z);
		
		return projection.times(cameraRotation.times(cameraTranslation));
	}
	
	public Transform getTransform() {
		return transform;
	}
}
