package com.base.engine.rendering;

import java.util.HashMap;
import com.base.engine.common.Vector3f;
import com.base.engine.rendering.resourceManagement.MappedValues;


public class Material extends MappedValues {
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Material() {
		super();
		
//		setTexture("sampler",   null);
		setTexture("normalMap",   new Texture("default_normal.jpg"));
		setTexture("dispMap",     new Texture("default_disp.png"));
		setTexture("specularMap", new Texture("default_spec.png"));
//		
		setVector3f("color", new Vector3f(1, 1, 1));
//		setFloat("specularIntensity", 2);
//		setFloat("specularPower", 8);
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Texture getTexture() {
		return getTexture("diffuse");
	}
	
	public Texture getNormalMap() {
		return getTexture("normalMap");
	}
	
	public Texture getDispMap() {
		return getTexture("dispMap");
	}

	public Vector3f getColor() {
		return getVector3f("color");
	}

	public float getSpecularIntensity() {
		return getFloat("specularIntensity");
	}
	
	public float getSpecularPower() {
		return getFloat("specularPower");
	}
	
	public float getReflectivity() {
		return getFloat("reflectivity");
	}
	
	
	public float getDispMapScale() {
		return getFloat("dispMapScale");
	}
	
	public float getDispMapBias() {
		return getFloat("dispMapBias");
	}



	// ==================== MUTATORS ==================== //
	
	public Material setTexture(Texture texture) {
		setTexture("diffuse", texture);
		return this;
	}
	
	public Material setNormalMap(Texture normalMap) {
		setTexture("normalMap", normalMap);
		return this;
	}
	
	public Material setDispMap(Texture dispMap) {
		setTexture("dispMap", dispMap);
		return this;
	}
	
	public Material setDispMap(Texture dispMap, float dispMapScale, float dispMapOffset) {
		setTexture("dispMap", dispMap);
		setDisplacement(dispMapScale, dispMapOffset);
		return this;
	}

	public Material setColor(Vector3f color) {
		setVector3f("color", color);
		return this;
	}
	
	public Material setSpecularMap(Texture specMap, float specularIntensity, float specularPower) {
		setTexture("specularMap", specMap);
		setFloat("specularIntensity", specularIntensity);
		setFloat("specularPower", specularPower);
		return this;
	}
	
	public Material setSpecular(float specularIntensity, float specularPower) {
		setFloat("specularIntensity", specularIntensity);
		setFloat("specularPower", specularPower);
		return this;
	}
	
	public Material setSpecularIntensity(float specularIntensity) {
		setFloat("specularIntensity", specularIntensity);
		return this;
	}

	public Material setSpecularPower(float specularPower) {
		setFloat("specularPower", specularPower);
		return this;
	}
	
	public Material setReflectivity(float reflectivity) {
		setFloat("reflectivity", Math.max(0, Math.min(1, reflectivity)));
		return this;
	}
	
	public Material setDisplacement(float dispMapScale, float dispMapOffset) {
		float baseBias = dispMapScale / 2.0f;
		setFloat("dispMapScale", dispMapScale);
		setFloat("dispMapBias", -baseBias + (baseBias * dispMapOffset));
		return this;
	}
	
	public Material setDispMapScale(float dispMapScale) {
		setFloat("dispMapScale", dispMapScale);
		return this;
	}
	
	public Material setDispMapOffset(float dispMapOffset) {
		float baseBias = getDispMapScale() / 2.0f;
		setFloat("dispMapBias", -baseBias + (baseBias * dispMapOffset));
		return this;
	}
}
