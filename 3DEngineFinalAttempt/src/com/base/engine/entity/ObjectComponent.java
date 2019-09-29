package com.base.engine.entity;

import com.base.engine.core.CoreEngine;
import com.base.engine.core.Transform;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public abstract class ObjectComponent {
	private EntityObject parent;
	
	
	
	public void input(float delta) {}
	public void update(float delta) {}
	public void render(Shader shader, RenderingEngine renderingEngine) {}
	
	public void addToEngine(CoreEngine engine) {}
	
	
	
	public EntityObject getParent() {
		return parent;
	}

	public Transform getTransform() {
		return parent.getTransform();
	}

	public void setParent(EntityObject parent) {
		this.parent = parent;
	}
}
