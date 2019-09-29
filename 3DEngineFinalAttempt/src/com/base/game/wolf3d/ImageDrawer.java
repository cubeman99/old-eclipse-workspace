package com.base.game.wolf3d;

import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Transform;
import com.base.engine.core.Vertex;
import com.base.engine.entity.MeshRenderer;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Texture;

public class ImageDrawer extends MeshRenderer {
	private Texture texture;
	private SceneObject target;
	private Vector2f size;
	private Transform finalTransform;
	

	// ================== CONSTRUCTORS ================== //

	public ImageDrawer(SceneObject target, float width, float height, Texture texture) {
		this.target  = target;
		this.size    = new Vector2f(width, height);
		this.texture = texture;
		
		Vertex[] vertices = new Vertex[4];
		vertices[0] = new Vertex(-width / 2, height, 0, 0, 1);
		vertices[1] = new Vertex(width / 2, height, 0, 1, 1);
		vertices[2] = new Vertex(width / 2, 0, 0, 1, 0);
		vertices[3] = new Vertex(-width / 2, 0, 0, 0, 0);
		int[] indices = new int[] {0, 1, 2, 0, 2, 3};
		
		finalTransform = new Transform();
		
		setMesh(new Mesh(vertices, indices, true, true));
		setMaterial(new Material());
		getMaterial().setTexture(texture);
	}


	
	// =================== ACCESSORS =================== //
	
	public SceneObject getTarget() {
		return target;
	}
	
	public Vector2f getSize() {
		return size;
	}
	
	public float getWidth() {
		return size.x;
	}
	
	public float getHeight() {
		return size.y;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setTarget(SceneObject target) {
		this.target = target;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
		getMaterial().setTexture(texture);
	}
	
	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
//		finalTransform.set(getTransform());
//		finalTransform.setPosition(getTransform().getTransformedPosition());
//		Vector2f dir = target.getTransform().getRotation().getForward().getXZ().normalize();
//		finalTransform.setDirection(new Vector3f(dir.x, 0, dir.y));
	}
	
	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		finalTransform.set(getTransform());
		finalTransform.setPosition(getTransform().getTransformedPosition());
		Vector2f dir = target.getTransform().getRotation().getForward().getXZ().normalize();
		finalTransform.setDirection(new Vector3f(dir.x, 0, dir.y));
		
		shader.bind();
		shader.updateUniforms(finalTransform, renderingEngine, getMaterial());
		getMesh().draw();
	}
}
