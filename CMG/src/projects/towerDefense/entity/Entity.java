package projects.towerDefense.entity;

import projects.towerDefense.Level;

public abstract class Entity {
	private boolean destroyed = false;
	protected Level level;
	
	public void destroy() {
		destroyed = true;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void initialize(Level level) {
		this.level = level;
		initialize();
	}

	public void initialize() {}
	
	public abstract void update();

	public void preDraw() {}
	public abstract void draw();
	public void postDraw() {}
}
