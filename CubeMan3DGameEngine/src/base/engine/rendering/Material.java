package base.engine.rendering;

import java.util.HashMap;
import base.engine.common.ResourceLoader;
import base.engine.common.Vector3;
import base.engine.rendering.resourceManagement.MappedValues;

public class Material extends MappedValues {
//	private Texture texture;
//	private Texture normalMap;
//	private Texture dispMap;
//	private Vector3 color;
//	private float specularIntensity;
//	private float specularExponent;
//	private float dispMapScale;
//	private float dispMapBias;
	
	
	

	private HashMap<String, Texture> textureHashMap;

	// ================== CONSTRUCTORS ================== //
	
	public Material()
	{
		super();
		textureHashMap = new HashMap<String, Texture>();
		textureHashMap.put("normalMap", new Texture("default_normal.jpg"));
		textureHashMap.put("dispMap", new Texture("default_disp.png"));
	}

	public void addTexture(String name, Texture texture) {
		textureHashMap.put(name, texture);
	}

	public Texture getTexture(String name)
	{
		Texture result = textureHashMap.get(name);
		if(result != null)
			return result;
		return new Texture("unknown_texture.jpg");
	}
	
	

//
//	public Material(Texture texture) {
//		this(texture, new Vector3(1, 1, 1));
//	}
//	
//	
//	public Material(Texture texture, Vector3 color) {
//		this(texture, color, 2, 32);
//	}
//	
//	public Material(Texture texture, Vector3 color, float specularIntensity, float specularExponent) {
//		this(texture, new Texture("default_normal.jpg"), color, specularIntensity, specularExponent);
//	}
//	
//	public Material(Texture texture, Texture normalMap, Vector3 color, float specularIntensity, float specularExponent) {
//		this(texture, normalMap, new Texture("default_disp.jpg"), color, specularIntensity, specularExponent, 0.0f, 0);
//	}
//	
//	public Material(Texture texture, Texture normalMap, Texture dispMap, Vector3 color,
//			float specularIntensity, float specularExponent, float dispMapScale, float dispMapOffset)
//	{
//		this.texture           = texture;
//		this.normalMap         = normalMap;
//		this.dispMap           = dispMap;
//		this.color             = color;
//		this.specularIntensity = specularIntensity;
//		this.specularExponent  = specularExponent;
//		this.dispMapScale      = dispMapScale;
//		
//		float baseBias   = (dispMapScale / 2.0f);
//		this.dispMapBias = -baseBias + (baseBias * dispMapOffset);
//	}
//	

	// =================== ACCESSORS =================== //
	
	public Vector3 getColor() {
		return getVector3("color");
	}
	
	public Texture getTexture() {
		return getTexture("diffuse");
	}
	
	public Texture getNormalMap() {
		return getTexture("normalMap");
	}
	
	public Texture getDispMap() {
		return getTexture("dispMap");
	}
	
	public float getSpecularIntensity() {
		return getFloat("specularIntensity");
	}
	
	public float getSpecularPower() {
		return getFloat("specularPower");
	}
	
	public float getDispMapBias() {
//		return dispMapBias;
		return 0;
	}
	
	public float getDispMapOffset() {
//		if (dispMapScale == 0)
//			return 0;
//		float baseBias   = (dispMapScale / 2.0f);
//		return (dispMapBias + baseBias) / baseBias;
		return 0;
	}
	
	public float getDispMapScale() {
//		return dispMapScale;
		return 0;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void setTexture(Texture texture) {
		addTexture("diffuse", texture);
	}
	
	public void setNormalMap(Texture normalMap) {
		addTexture("normalMap", normalMap);
	}
	
	public void setDispMap(Texture dispMap) {
		addTexture("dispMap", dispMap);
	}
	
	public void setColor(Vector3 color) {
		addVector3("color", color);
	}
	
	public void setSpecularIntensity(float specularIntensity) {
		addFloat("specularIntensity", specularIntensity);
	}
	
	public void setSpecularPower(float specularPower) {
		addFloat("specularPower", specularPower);
	}
	
	public void setDispMapOffset(float dispMapOffset) {
//		float baseBias   = (dispMapScale / 2.0f);
//		this.dispMapBias = -baseBias + (baseBias * dispMapOffset);
	}
	
	public void setDispMapBias(float dispMapBias) {
//		this.dispMapBias = dispMapBias;
	}
	
	public void setDispMapScale(float dispMapScale) {
//		addFloat("dispMapScale", dispMapScale);
	}
}
