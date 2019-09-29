package base.engine.entity.light;

import base.engine.common.Vector3;
import base.engine.rendering.Shader;

public class DirectionalLight extends BaseLight {
	
	public DirectionalLight(Vector3 color, float intensity) {
		super(color, intensity);
	}
	
	public Vector3 getDirection() {
		return getTransform().getRotation().getForward();
	}
}
