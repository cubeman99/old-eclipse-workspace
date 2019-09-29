package base.engine.entity.light;

import base.engine.common.Vector3;
import base.engine.rendering.Attenuation;
import base.engine.rendering.Shader;

public class PointLight extends BaseLight {
	private Attenuation attenuation;
	private float range;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public PointLight(Vector3 color, float intensity, Attenuation atten, float range) {
		super(color, intensity);
		this.attenuation = atten;
		this.range       = range;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Attenuation getAttenuation() {
		return attenuation;
	}
	
	public float getRange() {
		return range;
	}


	
	// ==================== MUTATORS ==================== //
	
	
	public void setAttenuation(Attenuation atten) {
		this.attenuation = atten;
	}
	
	public void setRange(float range) {
		this.range = range;
	}
}
