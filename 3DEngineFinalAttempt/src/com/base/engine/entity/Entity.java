package com.base.engine.entity;

import com.base.engine.core.CoreEngine;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public abstract class Entity {
	protected CoreEngine engine;
	
	
	
	public void input(float delta) {}
	public void update(float delta) {}
	public void render(Shader shader, RenderingEngine renderingEngine) {}
	
	
	
	public CoreEngine getEngine() {
		return engine;
	}
	
	public void setEngine(CoreEngine engine) {
		this.engine = engine;
	}
}
