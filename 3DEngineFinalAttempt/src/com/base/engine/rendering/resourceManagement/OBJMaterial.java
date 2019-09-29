package com.base.engine.rendering.resourceManagement;

public class OBJMaterial {
	private String name;
	private String mapDiffuse; // fileName for diffuse map.
	
	
	
	// ================== CONSTRUCTORS ================== //

	public OBJMaterial(String name) {
		this.name       = name;
		this.mapDiffuse = "";
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public String getName() {
		return name;
	}
	
	public String getMapDiffuse() {
		return mapDiffuse;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setMapDiffuse(String mapDiffuse) {
		this.mapDiffuse = mapDiffuse;
	}
}
