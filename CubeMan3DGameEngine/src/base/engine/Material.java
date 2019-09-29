package base.engine;

import base.engine.Texture;
import base.engine.common.Vector3;

public class Material {
	private Texture texture;
	private Texture normalMap;
	private Vector3 color;
	private float specularIntensity;
	private float specularExponent;
	
	

	// ================== CONSTRUCTORS ================== //

	public Material(Texture texture) {
		this(texture, new Vector3(1, 1, 1));
	}
	
	
	public Material(Texture texture, Vector3 color) {
		this(texture, color, 2, 32);
	}
	
	public Material(Texture texture, Vector3 color, float specularIntensity, float specularExponent) {
		this.texture           = texture;
		this.color             = color;
		this.specularIntensity = specularIntensity;
		this.specularExponent  = specularExponent;
	}
	

	// =================== ACCESSORS =================== //
	
	public Vector3 getColor() {
		return color;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Texture getNormalMap() {
		return normalMap;
	}
	
	public float getSpecularExponent() {
		return specularExponent;
	}
	
	public float getSpecularIntensity() {
		return specularIntensity;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
	}
	
	public void setColor(Vector3 color) {
		this.color = color;
	}
	
	public void setSpecularExponent(float specularExponent) {
		this.specularExponent = specularExponent;
	}
	
	public void setSpecularIntensity(float specularIntensity) {
		this.specularIntensity = specularIntensity;
	}
}
