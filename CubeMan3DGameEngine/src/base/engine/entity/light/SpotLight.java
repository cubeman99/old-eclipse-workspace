package base.engine.entity.light;

import base.engine.common.Vector3;
import base.engine.rendering.Attenuation;
import base.engine.rendering.Shader;

public class SpotLight extends PointLight {
	private float cutoff;

	
	
	// ================== CONSTRUCTORS ================== //

	public SpotLight(Vector3 color, float intensity, Attenuation atten, float range, float cutoff) {
		super(color, intensity, atten, range);
		this.cutoff = cutoff;
		
		setShader(new Shader("forward-spot"));
	}
	
	

	// =================== ACCESSORS =================== //
	
	public float getCutoff() {
		return cutoff;
	}
	
	public Vector3 getDirection() {
		return getTransform().getRotation().getForward();
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setCutoff(float cutoff) {
		this.cutoff = cutoff;
	}
}
