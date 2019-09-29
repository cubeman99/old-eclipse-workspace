package engine.core;

public abstract class AbstractGame {
	private CoreEngine engine;
	
	
	
	public abstract void initialize();
	
	public abstract void update();
	
	public abstract void render();
	
	
	public CoreEngine getEngine() {
		return engine;
	}
	
	public void setEngine(CoreEngine engine) {
		this.engine = engine;
	}
}
