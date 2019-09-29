package projects.slimeVolleyBall;

import java.awt.Graphics;
import main.GameRunner;
import common.Draw;
import common.HUD;


public class SlimeTestRunner extends GameRunner {
	public SlimeBallRunner control;
	
	public SlimeTestRunner() {
		super();
	}
	
	@Override
	public void initialize() {
		control = new SlimeBallRunner(this);
	}

	@Override
	public void update() {
		control.update();
	}

	@Override
	public void draw(Graphics g) {
		Draw.setGraphics(g);
		HUD.setGraphics(g);
		control.draw();
	}
}
