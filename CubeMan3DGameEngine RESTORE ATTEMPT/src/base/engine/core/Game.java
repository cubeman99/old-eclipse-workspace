package base.engine.core;


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

	public void render() {
		world.render();
	}

	public void setEngine(CoreEngine engine) {
		this.engine = engine;
	}
}
