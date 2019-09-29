package projects.neuralNetwork;

import java.awt.Color;
import java.awt.Graphics;
import main.GameRunner;
import common.Draw;
import common.HUD;


public class TestRunner extends GameRunner {
	public NetworkRunner control;
	
	public TestRunner() {
		super(60);
	}
	
	@Override
	public void initialize() {
		control = new NetworkRunner(this);
	}

	@Override
	public void update() {
		control.update();
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		HUD.setGraphics(g);
		control.draw();
	}
	
	public static void main(String[] args) {
		new TestRunner();
	}
}
