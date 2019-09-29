package projects.neuralNetwork;

import java.awt.Color;
import common.Draw;
import common.Vector;
import main.GameRunner;

public class NetworkRunner {
	private GameRunner runner;
	
	public NetworkRunner(GameRunner runner) {
		this.runner = runner;
	}
	
	public void update() {
		
	}
	
	public void draw() {
		System.out.println("AS");
		Draw.setColor(Color.WHITE);
		Draw.fillRect(0, 0, runner.getViewWidth(), runner.getViewHeight());
		
		Draw.setView(new Vector(), 1);
		
		Draw.setColor(Color.BLACK);
		Draw.drawLine(10, 10, 50, 50);
		Draw.drawStringCentered("Hello, World!", new Vector(120, 10));
	}
}
