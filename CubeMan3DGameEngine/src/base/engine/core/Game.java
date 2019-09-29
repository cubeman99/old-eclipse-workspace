package base.engine.core;

import base.engine.rendering.RenderingEngine;


public abstract class Game {
	protected CoreEngine engine;
	protected World world;
	
	public Game() {
		world = new World(engine);
	}

	public void initialize() {}

	public void input(float frameTime) {
		world.input(frameTime);
//		getRootObject().inputAll(delta);
	}

	public void update(float frameTime) {
		world.update(frameTime);
	}

	public void render(RenderingEngine renderingEngine) {
		world.render(renderingEngine);
	}

	public void setEngine(CoreEngine engine) {
		this.engine = engine;
	}
}
