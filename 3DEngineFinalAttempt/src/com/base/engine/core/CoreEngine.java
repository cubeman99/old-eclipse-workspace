package com.base.engine.core;

import com.base.engine.audio.AudioEngine;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Window;


public class CoreEngine {
	private static float globalTime = 0;
	
	private boolean isRunning;
	private AbstractGame game;
	private int width;
	private int height;
	private double frameTime;
	private RenderingEngine renderingEngine;
	
	

	// ================== CONSTRUCTORS ================== //

	public CoreEngine(int width, int height, double framerate, AbstractGame game) {
		this.isRunning = false;
		this.game      = game;
		this.width     = width;
		this.height    = height;
		this.frameTime = 1.0 / framerate;
		game.setEngine(this);
	}
	
	

	// =================== ACCESSORS =================== //

	public RenderingEngine getRenderingEngine() {
		return renderingEngine;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void createWindow(String title) {
		Window.createWindow(width, height, title);
		this.renderingEngine = new RenderingEngine();
		AudioEngine.initialize();
	}

	public void start() {
		if (isRunning)
			return;

		run();
	}

	public void stop() {
		if (!isRunning)
			return;

		isRunning = false;
	}

	private void run() {
		isRunning = true;
		
		int frames = 0;
		double frameCounter = 0;

		game.init();

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
				Keyboard.update();
				Mouse.update();
				AudioEngine.update((float) frameTime);
				
				game.update((float) frameTime);
				
				globalTime += frameTime;

				if (frameCounter >= 1.0) {
//					System.out.println(frames);
					frames = 0;
					frameCounter = 0;
				}
			}
			if (render) {
				game.render(renderingEngine);
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
		AudioEngine.dispose();
	}
	
	public static float getGlobalTime() {
		return globalTime;
	}
}
