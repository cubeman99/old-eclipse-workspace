package base.engine.rendering.resourceManagement;

import java.util.HashMap;
import base.engine.common.Vector3;

public class MappedValues {
	private HashMap<String, Vector3> vector3HashMap;
	private HashMap<String, Float> floatHashMap;

	
	
	// ================== CONSTRUCTORS ================== //

	public MappedValues() {
		vector3HashMap = new HashMap<String, Vector3>();
		floatHashMap   = new HashMap<String, Float>();
	}


	
	// =================== ACCESSORS =================== //

	public Vector3 getVector3(String name) {
		Vector3 result = vector3HashMap.get(name);
		if(result != null)
			return result;
		return new Vector3(0, 0, 0);
	}

	public float getFloat(String name) {
		Float result = floatHashMap.get(name);
		if(result != null)
			return result;
		return 0.0f;
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void addVector3(String name, Vector3 v) {
		vector3HashMap.put(name, v);
	}
	
	public void addFloat(String name, float value) {
		floatHashMap.put(name, value);
	}
}
