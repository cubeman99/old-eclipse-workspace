package projects.fractals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Keyboard;
import cmg.main.Mouse;
import cmg.math.GMath;
import cmg.math.geometry.Vector;

public class Control {
	private Branch tree;
	private GameRunner runner;
	private float[][] noiseMap;
	private float time;
	
	private ArrayList<Wave> waves;
	
	
	
	public Control(GameRunner runner) {
		this.runner = runner;
		tree = new Branch(8);
		time = 0;
		
		waves = new ArrayList<Wave>();
		waves.add(new Wave(0.7f, 64, 2, new Vector2f(1, 1)));
		waves.add(new Wave(0.2f, 32, 0.5f, new Vector2f(-1, 0.2f)));
		waves.add(new Wave(0.1f, 24, 1f, new Vector2f(0.1f, 1)));
		
		int x = 10;
		int y = 512;
		int z = 512;
//		noiseMap = PerlinNoise.generatePerlinNoise(PerlinNoise.generateWhiteNoise(x, y, z), 5)[x / 2];
		
		Random random = new Random();
		noiseMap = new float[400][400];
		
		float min = 100;
		float max = -100;
		
		for (int xx = 0; xx < noiseMap.length; xx++) {
			for (int yy = 0; yy < noiseMap[0].length; yy++) {
//				noiseMap[xx][yy] = NewPerlinNoise.getPerlinNoise((float) xx, (float) yy);
//				noiseMap[xx][yy] = NewPerlinNoise.gradient(xx, yy).y;
//				
//				random.setSeed(((long) xx * 73856093L) + (long) yy);random.nextFloat();random.nextFloat();random.nextFloat();random.nextFloat();
//				random.nextFloat();random.nextFloat();random.nextFloat();random.nextFloat();random.nextFloat();random.nextFloat();
//				noiseMap[xx][yy] = random.nextFloat();
				
				float noise = PerlinNoise3f.perlinNoise((xx - 256) / 70.0f, (yy - 256) / 70.0f, 3, 1.0f, 5);
//				noise = NewPerlin2.noise(xx, yy);
//				noise = random.nextFloat();
//				noise = NewPerlin2.noise1(xx * noiseMap.length + yy);
				if (noise < min)
					min = noise;
				if (noise > max)
					max = noise;
				
				noiseMap[xx][yy] = noise;
			}
		}

		for (int xx = 0; xx < noiseMap.length; xx++) {
			for (int yy = 0; yy < noiseMap[0].length; yy++) {
				noiseMap[xx][yy] = (noiseMap[xx][yy] - min) / (max - min);
			}
		}
		
		System.out.println(min);
		System.out.println(max);
//		System.exit(0);
	}
	
	public void update() {
		if (Keyboard.space.pressed()) {
			tree = new Branch(64);
			//tree.extend();
		}
		if (Keyboard.up.down()) {
			tree.update();
		}
	}
	
	public void drawBranch(Vector pos, double length, double angle, int iteration) {
		Vector next = pos.plus(Vector.polarVector(length, angle));
		Draw.drawLine(pos, next);
		
		if (iteration < 12) {
			double nextLength = length * 0.75;
			drawBranch(next, nextLength, angle - GMath.QUARTER_PI, iteration + 1);
			drawBranch(next, nextLength, angle + GMath.QUARTER_PI, iteration + 1);
		}
	}
	
	public void drawWaves() {
		float width = 400;
		float height = 400;
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				float disp = 0;
				for (Wave wave : waves)
					disp += wave.getState(x, y, time);
				
				
				disp = (disp * 0.5f) + 0.5f;
				int v = (int) (255 * disp);
				v = Math.max(0, Math.min(255, v));
				Draw.setColor(new Color(v, v, v));
				Draw.drawRect(x * 1, y * 1, 1, 1);
			}
		}
	}
	
	public void drawNoise() {
		float z = time / 300.0f;
		
		// Generate noise.
		float min = 100;
		float max = -100;
		for (int xx = 0; xx < noiseMap.length; xx++) {
			for (int yy = 0; yy < noiseMap[0].length; yy++) {
				float noise = PerlinNoise3f.perlinNoise((xx - 256) / 70.0f, (yy - 256) / 70.0f, z, 1.0f, 4);
				
				if (noise < min)
					min = noise;
				if (noise > max)
					max = noise;
				
				noiseMap[xx][yy] = noise;
			}
		}
		
		// Draw noise.
		for (int x = 0; x < noiseMap.length; x++) {
			for (int y = 0; y < noiseMap[0].length; y++) {
				noiseMap[x][y] = (noiseMap[x][y] - min) / (max - min);
				
				float noise = noiseMap[x][y];
				int v = (int) (255 * noise);
				Draw.setColor(new Color(v, v, v));
				Draw.drawRect(x * 1, y * 1, 1, 1);
			}
		}
	}
	
	public void draw() {
		time++;
		
//		drawNoise();
		
		drawWaves();
		
//		Draw.setColor(Color.BLACK);
//		tree.draw(new Vector(runner.getViewWidth() / 2.0, runner.getViewHeight() / 2.0), GMath.HALF_PI);
	}
}
