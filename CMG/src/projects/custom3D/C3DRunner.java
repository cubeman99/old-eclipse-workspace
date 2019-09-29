package projects.custom3D;

import java.awt.Graphics;
import cmg.graphics.Draw;
import cmg.main.GameRunner;


public class C3DRunner extends GameRunner {
	public Control control;
	
	public C3DRunner() {
		super(60, 640, 640, CanvasMode.MODE_RESIZE);
		RenderUtil.initGraphics();
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
		RenderUtil.clearScreen();
		Draw.setGraphics(g);
		control.draw();
	}
	
	public static void main(String[] args) {
		new C3DRunner();
	}
}