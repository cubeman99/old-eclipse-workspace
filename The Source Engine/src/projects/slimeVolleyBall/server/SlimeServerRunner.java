package projects.slimeVolleyBall.server;

import java.awt.Graphics;
import main.GameRunner;
import common.Draw;
import common.HUD;


public class SlimeServerRunner extends GameRunner {
	public SlimeBallServer control;
	public boolean host;
	
	@Override
	public void initialize() {
		control = new SlimeBallServer(this);
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
