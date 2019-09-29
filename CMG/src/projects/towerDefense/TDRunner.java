package projects.towerDefense;

import java.awt.Graphics;
import cmg.graphics.Draw;
import cmg.main.GameRunner;


public class TDRunner extends GameRunner {
	public GameInstance game;
	
	public TDRunner() {
		super(60, 900, 700, CanvasMode.MODE_RESIZE);
	}
	
	@Override
	public void initialize() {
		game = new GameInstance(this);
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
		new TDRunner();
	}
}