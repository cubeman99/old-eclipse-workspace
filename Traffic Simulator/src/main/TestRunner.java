package main;

import game.Control;
import java.awt.Graphics;
import common.graphics.Draw;
import main.GameRunner;


public class TestRunner extends GameRunner {
	public Control control;
	
	public TestRunner() {
		super(60);
	}
	
	@Override
	public void initialize() {
		control = new Control(this);
	}

	@Override
	public void update() {
		control.update();
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		control.draw(g);
	}
	
	public static void main(String[] args) {
		new TestRunner();
	}
}
