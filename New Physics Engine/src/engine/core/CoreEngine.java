package engine.core;

import engine.physics.PhysicsEngine;
import engine.rendering.RenderingEngine;


public class CoreEngine {
	private static float globalTime = 0;
	
	private RenderingEngine renderingEngine;
	private PhysicsEngine physicsEngine;
	private GameThread gameThread;
	private AbstractGame game;
	
	

	// ================== CONSTRUCTORS ================== //

	public CoreEngine(double framerate, AbstractGame game) {
		this.game  = game;
		gameThread = new GameThread(framerate);
		game.setEngine(this);
	}
	
	

	// =================== ACCESSORS =================== //

	public RenderingEngine getRenderingEngine() {
		return renderingEngine;
	}
	
	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void createWindow(int width, int height, String title) {
		Window.createWindow(width, height, title);
		renderingEngine = new RenderingEngine();
		physicsEngine   = new PhysicsEngine();
	}

	public void start() {
		if (!gameThread.isRunning)
			gameThread.start();
	}

	public void stop() {
		if (gameThread.isRunning)
			gameThread.isRunning = false;
	}
	
	
	
	
	private class GameThread extends Thread {
		private boolean isRunning;
		private double frameTime;
		
		
		
		public GameThread(double framerate) {
			this.isRunning = false;
			this.frameTime = 1.0 / framerate;
		}
		
		@Override
		public void run() {
			int frames             = 0;
			double frameCounter    = 0;
			double lastTime        = Time.getTime();
			double unprocessedTime = 0;
			isRunning = true;

			game.initialize();
			
			
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
					
					Time.delta = (float) frameTime;
					
					Window.update();
					Keyboard.update();
					Mouse.update();
					game.update();
					physicsEngine.simulate((float) frameTime);
					
					globalTime += frameTime;

					if (frameCounter >= 1.0) {
//						System.out.println(frames);
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
		}
	}
}
