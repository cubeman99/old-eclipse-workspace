package base.engine.core;

import base.engine.common.Time;
import base.engine.rendering.RenderUtil;
import base.engine.rendering.Window;


public class CoreEngine {
	private boolean isRunning;
	private Game game;
//	private RenderingEngine renderingEngine;
	private int width;
	private int height;
	private double frameTime;

	
	
	// ================== CONSTRUCTORS ================== //

	public CoreEngine(int width, int height, double framerate, Game game) {
		this.isRunning = false;
		this.game      = game;
		this.width     = width;
		this.height    = height;
		this.frameTime = 1.0 / framerate;
		game.setEngine(this);
	}

	
	
	// =================== ACCESSORS =================== //

//	public RenderingEngine getRenderingEngine() {
//		return renderingEngine;
//	}

	
	
	// ==================== MUTATORS ==================== //
	
	public void createWindow(String title) {
		Window.createWindow(width, height, title);
		RenderUtil.initGraphics();
//		this.renderingEngine = new RenderingEngine();
	}

	public void start() {
		if(!isRunning)
			run();
	}
	
	public void stop() {
		if(isRunning)
			isRunning = false;
	}
	
	private void run() {
		isRunning = true;
		
		int frames = 0;
		double frameCounter = 0;

		game.initialize();

		double lastTime = Time.getTime();
		double unprocessedTime = 0;
		
		while (isRunning) {
			boolean render = false;

			double startTime = Time.getTime();
			double passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocessedTime += passedTime;
			frameCounter += passedTime;

			while (unprocessedTime > frameTime) {
				render = true;
				
				unprocessedTime -= frameTime;
				
				if(Window.isCloseRequested())
					stop();

				game.input((float) frameTime);
				Input.update();
				
				game.update((float) frameTime);
				
				if (frameCounter >= 1.0) {
//					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				game.render();
				Window.render();
				frames++;
			}
			else {
				try {
					Thread.sleep(1);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		cleanUp();
	}

	private void cleanUp() {
		Window.dispose();
	}
}
