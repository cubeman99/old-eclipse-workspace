package com.base.engine.rendering.resourceManagement;

import static org.lwjgl.opengl.GL15.*;

public class MeshResource extends BasicResource {
	private int vbo; // Vertex Buffer Object
	private int ibo; // Index Buffer Object
	private int size;
	
	
	// ================== CONSTRUCTORS ================== //

	public MeshResource(int size) {
		super(true);
		this.vbo           = glGenBuffers();
		this.ibo           = glGenBuffers();
		this.size          = size;
	}
	


	// =================== ACCESSORS =================== //

	public int getVbo() {
		return vbo;
	}
	
	public int getIbo() {
		return ibo;
	}
	
	public int getSize() {
		return size;
	}
	
	// ==================== MUTATORS ==================== //
	
	public void setSize(int size) {
		this.size = size;
	}
	
	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	protected void finalize() {
		glDeleteBuffers(vbo);
		glDeleteBuffers(ibo);
	}
}
