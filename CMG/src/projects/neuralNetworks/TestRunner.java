package projects.neuralNetworks;

import java.awt.Graphics;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Keyboard;

public class TestRunner extends GameRunner {
	public World control;
	
	public TestRunner() {
		super(60, 640, 640, CanvasMode.MODE_RESIZE);
	}
	
	@Override
	public void initialize() {
		control = new World(this);
	}

	@Override
	public void update() {
		if (Keyboard.restart.pressed())
			control = new World(this);
		control.update();
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		control.draw();
	}
	
	public static void main(String[] args) {
		new TestRunner();
	}
}
