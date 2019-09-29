package com.base.engine.core;

import com.base.engine.entity.World;
import com.base.engine.rendering.RenderingEngine;


public abstract class AbstractGame {
	protected CoreEngine engine;
	protected World world;
	
	public AbstractGame() {
		world = new World();
	}
	
	public void init() {}
	
	public void input(float delta) {
//		world.input(delta);
	}
	
	public void update(float delta) {
		world.update(delta);
	}
	
	public void render(RenderingEngine renderingEngine) {
		renderingEngine.render(world);
	}
	
	
	
	public World getWorld() {
		return world;
	}
	
	public CoreEngine getEngine() {
		return engine;
	}
	
	public void setEngine(CoreEngine engine) {
		this.engine = engine;
//		world.setEngine(engine);
	}
}
