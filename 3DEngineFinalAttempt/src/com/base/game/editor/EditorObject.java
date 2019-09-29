package com.base.game.editor;

import com.base.engine.core.Transform;
import com.base.engine.entity.EntityObject;
import com.base.engine.entity.ObjectComponent;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class EditorObject {
	private ObjectComponent component;
	private Transform transform;

	
	
	// ================== CONSTRUCTORS ================== //

	public EditorObject(ObjectComponent component) {
		this.component = component;
		this.transform = new Transform();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Transform getTransform() {
		return transform;
	}
	
	public ObjectComponent getComponent() {
		return component;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setComponent(ObjectComponent component) {
		this.component = component;
	}
	
	public void render(Shader shader, RenderingEngine renderingEngine) {
		component.render(shader, renderingEngine);
	}
}
