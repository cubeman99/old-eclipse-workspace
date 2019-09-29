package game;

import java.awt.Graphics;
import cmg.graphics.Draw;
import cmg.main.GameRunner;

public class SlimeBallRunner extends GameRunner {
	public SlimeBall control;
	
	public SlimeBallRunner(SlimeBall control) {
		super(60, 750, 500, CanvasMode.MODE_STRETCH);
		this.control = control;
		this.control.setRunner(this);
	}
	
	public SlimeBall getGame() {
		return control;
	}
	
	@Override
	public void initialize() {
		
	}

	@Override
	public void update() {
		control.update();
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		control.draw();
	}
	
	public static void main(String[] args) {
//		new SlimeBallCleint();
	}
}
