package com.base.engine.entity;

import com.base.engine.common.Rect3f;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.resourceManagement.OBJModel;

public class Model extends SceneObject {
	private Mesh[] meshes;
	private Material[] materials;
	private int numParts;
	private Rect3f boundingBox;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Model(String fileName) {
		this(new OBJModel(fileName));
	}
	
	public Model(OBJModel obj) {
		meshes       = obj.createMeshes();
		materials    = obj.createMaterials();
		numParts     = materials.length;
		boundingBox  = obj.getBoundingBox();
	}

	public Model(Mesh mesh, Material material) {
		this.meshes    = new Mesh[] {mesh};
		this.materials = new Material[] {material};
		this.numParts  = 1;
	}
	
	public Model(Mesh[] meshes, Material[] materials, int numParts) {
		this.meshes    = meshes;
		this.materials = materials;
		this.numParts  = numParts;
	}
	
	private void swap(int index1, int index2) {
		Mesh tempMesh     = meshes[index1];
		Material tempMat  = materials[index1];
		meshes[index1]    = meshes[index2];
		materials[index1] = materials[index2];
		meshes[index2]    = tempMesh;
		materials[index2] = tempMat;
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Rect3f getBoundingBox() {
		return boundingBox;
	}
	
	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		super.render(shader, renderingEngine);
		shader.bind();
		for (int i = 0; i < numParts; i++) {
    		shader.updateUniforms(getTransform(), renderingEngine, materials[i]);
    		meshes[i].draw();
		}
	}
}
