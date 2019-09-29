package zelda.editor;

import java.awt.Graphics;
import zelda.common.graphics.Draw;
import zelda.main.GameRunner;


public class EditorRunner extends GameRunner {
	public Editor control;

	public EditorRunner() {
		super(60, 1200, 900, CanvasMode.MODE_RESIZE);
	}

	@Override
	public void initialize() {
		control = new Editor(this);
		Draw.setRunner(this);
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
		new EditorRunner();
	}
}
