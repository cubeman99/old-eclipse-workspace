package base.engine.entity.light;

import base.engine.common.Vector3;
import base.engine.entity.EntityComponent;
import base.engine.rendering.Shader;

public class BaseLight extends EntityComponent {
	private Vector3 color;
	private float intensity;
	private Shader shader;

	
	
	// ================== CONSTRUCTORS ================== //

	public BaseLight(Vector3 color, float intensity) {
		super();
		this.color = color;
		this.intensity = intensity;
	}

	
	
	// =================== ACCESSORS =================== //

	public Vector3 getColor() {
		return color;
	}
	
	public float getIntensity() {
		return intensity;
	}
	
	public Shader getShader() {
		return shader;
	}

	
	
	// ==================== MUTATORS ==================== //

	public void setColor(Vector3 color) {
		this.color = color;
	}
	
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void onCreate() {
		getParent().getWorld().getEngine().getRenderingEngine().addLight(this);
	}
}
