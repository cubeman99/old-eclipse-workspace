package tp.main;

import tp.planner.Control;
import java.awt.Graphics;
import tp.main.GameRunner;


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
		control.draw(g);
	}
	
	public static void main(String[] args) {
		new TestRunner();
	}
}
