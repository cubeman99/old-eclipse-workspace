package projects.gravitySimulator;

import java.awt.Color;
import java.awt.Graphics;
import main.GameRunner;
import common.Draw;
import common.HUD;


public class TestRunner extends GameRunner {
	public GravitySimulator control;
	
	public TestRunner() {
		super(60);
	}
	
	@Override
	public void initialize() {
		control = new GravitySimulator(this);
	}

	@Override
	public void update() {
		control.update();
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		HUD.setGraphics(g);
		
		//g.setColor(Color.BLACK);
		//g.drawRect(0, 0, 1000, 1000);
		control.draw();
	}
	
	public static void main(String[] args) {
		new TestRunner();
	}
}
