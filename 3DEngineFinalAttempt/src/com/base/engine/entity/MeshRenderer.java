package com.base.engine.entity;

import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class MeshRenderer extends SceneObject {
	private Mesh mesh;
	private Material material;

	
	
	// ================== CONSTRUCTORS ================== //
	
	public MeshRenderer() {
		mesh     = null;
		material = null;
	}
	
	public MeshRenderer(Mesh mesh, Material material) {
		this.mesh     = mesh;
		this.material = material;
	}
	
	
	
	// =================== ACCESSORS =================== //

	public Mesh getMesh() {
		return mesh;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		shader.bind();
		shader.updateUniforms(getTransform(), renderingEngine, material);
		mesh.draw();
	}
}
