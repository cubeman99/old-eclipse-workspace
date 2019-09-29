package simulator;

import java.awt.Graphics;

public class WireSimulator {
	public Grid grid;
	
	public WireSimulator() {
		grid = new Grid(32, 32);
	}
	
	public void update() {
		grid.update();
	}
	
	public void draw(Graphics g) {
		grid.draw(g);
	}
}
