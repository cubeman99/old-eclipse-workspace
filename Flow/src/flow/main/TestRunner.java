package flow.main;

import java.awt.Graphics;
import flow.game.Control;


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
//		Draw.setGraphics(g);
		control.draw(g);
	}
	
	public static void main(String[] args) {
		new TestRunner();
	}
}
