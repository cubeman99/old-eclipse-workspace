package com.base.engine.rendering.resourceManagement;

import java.util.HashMap;
import com.base.engine.common.Vector3f;
import com.base.engine.rendering.Texture;

public class MappedValues {
	private HashMap<String, Texture> mapTexture;
	private HashMap<String, Vector3f> mapVector3f;
	private HashMap<String, Float> mapFloat;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public MappedValues() {
		mapTexture  = new HashMap<String, Texture>();
		mapVector3f = new HashMap<String, Vector3f>();
		mapFloat    = new HashMap<String, Float>();
	}
	
	

	// =================== ACCESSORS =================== //

	public Texture getTexture(String name) {
		Texture result = mapTexture.get(name);
		if (result != null)
			return result;
		return Texture.defaultTexture;
	}
	
	public Vector3f getVector3f(String name) {
		Vector3f result = mapVector3f.get(name);
		if (result != null)
			return result;
		return new Vector3f(0, 0, 0);
	}
	
	public float getFloat(String name) {
		Float result = mapFloat.get(name);
		if (result != null)
			return result;
		return 0.0f;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setTexture(String name, Texture value) {
		mapTexture.put(name, value);
	}

	public void setVector3f(String name, Vector3f value) {
		mapVector3f.put(name, value);
	}

	public void setFloat(String name, float value) {
		mapFloat.put(name, value);
	}
}
