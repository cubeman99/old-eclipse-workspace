package base.engine.rendering;

import base.engine.common.Vector3;

public class Material { 
	private Texture texture;
	private Texture normalMap;
	private Texture dispMap;
	private Vector3 color;
	private float specularIntensity;
	private float specularPower;
	private float dispMapScale;
	private float dispMapBias;
	
	
	// ================== CONSTRUCTORS ================== //

	public Material() {
		this(null, new Vector3(1, 1, 1));
	}
	
	public Material(Texture texture) {
		this(texture, new Vector3(1, 1, 1));
	}
	
	
	public Material(Texture texture, Vector3 color) {
		this(texture, color, 2, 32);
	}
	
	public Material(Texture texture, Vector3 color, float specularIntensity, float specularPower) {
		this.texture           = texture;
		this.color             = color;
		this.specularIntensity = specularIntensity;
		this.specularPower     = specularPower;
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
	
	public Texture getDispMap() {
		return dispMap;
	}
	
	public float getSpecularPower() {
		return specularPower;
	}
	
	public float getSpecularIntensity() {
		return specularIntensity;
	}
	
	public float getDispMapScale() {
		return dispMapScale;
	}
	
	public float getDispMapBias() {
		return dispMapBias;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public void setNormalMap(Texture normalMap) {
		this.normalMap = normalMap;
	}
	
	public void setDispMap(Texture dispMap) {
		this.dispMap = dispMap;
	}
	
	public void setColor(Vector3 color) {
		this.color = color;
	}
	
	public void setSpecularPower(float specularPower) {
		this.specularPower = specularPower;
	}
	
	public void setSpecularIntensity(float specularIntensity) {
		this.specularIntensity = specularIntensity;
	}
	
	public void setDispMapScale(float dispMapScale) {
		this.dispMapScale = dispMapScale;
	}
	
	public void setDispMapBias(float dispMapBias) {
		this.dispMapBias = dispMapBias;
	}
}
