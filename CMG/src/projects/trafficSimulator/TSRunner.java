package projects.trafficSimulator;

import java.awt.Graphics;
import cmg.graphics.Draw;
import cmg.main.GameRunner;


public class TSRunner extends GameRunner {
	public MapEditor map;
	
	public TSRunner() {
		super(60, 640, 640, CanvasMode.MODE_RESIZE);
	}
	
	@Override
	public void initialize() {
		map = new MapEditor(this);
	}

	@Override
	public void update() {
		map.update();
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		map.draw();
	}
	
	public static void main(String[] args) {
		new TSRunner();
	}
}