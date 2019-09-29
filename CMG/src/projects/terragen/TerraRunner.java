package projects.terragen;

import java.awt.Graphics;
import cmg.graphics.Draw;
import cmg.main.GameRunner;


public class TerraRunner extends GameRunner {
	public Control control;
	
	public TerraRunner() {
		super(60, 640, 640, CanvasMode.MODE_RESIZE);
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
		control.draw();
	}
	
	public static void main(String[] args) {
		new TerraRunner();
	}
}