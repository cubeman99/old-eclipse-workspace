package control;

import java.awt.Color;
import common.Draw;
import common.HUD;
import main.GamePanel;
import main.GameRunnerOLD;
import main.Main;

public class ControlRunner implements GameRunnerOLD {
	public Control control;
	private GamePanel panel;
	
	@Override
	public void initialize() {
		control = new Control();
		control.testRun();
		
		panel   = new GamePanel();
		Main.frame.add(panel.getPanel());
	}

	@Override
	public void update() {
		panel.update();
		control.update();
	}

	@Override
	public void draw() {
		panel.drawBackground(Color.BLACK);
		Draw.setGraphics(panel.getGraphics());
		HUD.setGraphics(panel.getGraphics());
		control.draw(panel.getGraphics());
		panel.repaint();
	}
}
