package base.engine.entity;

import base.engine.rendering.Material;
import base.engine.rendering.Mesh;
import base.engine.rendering.RenderingEngine;
import base.engine.rendering.Shader;

public class Model extends EntityComponent {
	private Mesh mesh;
	private Material material;

	
	
	// ================== CONSTRUCTORS ================== //

	public Model(Mesh mesh, Material material) {
		this.mesh     = mesh;
		this.material = material;
	}
	
	
	
	// =================== ACCESSORS =================== //

	public Material getMaterial() {
		return material;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void setMaterial(Material material) {
		this.material = material;
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		shader.bind();
		shader.updateUniforms(getTransform(), material, renderingEngine);
		mesh.draw();
	}
}
