package hstone;

import java.awt.Graphics;
import javax.swing.JFrame;
import cmg.graphics.Draw;
import cmg.main.GameRunner;


public class HearthStoneRunner extends GameRunner {
	public Control game;
	
	public HearthStoneRunner(JFrame frame) {
		super(60, 900, 700, frame, CanvasMode.MODE_RESIZE);
	}
	
	@Override
	public void initialize() {
		game = new Control(this);
	}

	@Override
	public void update() {
		game.update();
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		game.draw();
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Hearthstone Match Logger");
		new HearthStoneRunner(frame);
	}
}