package projects.neuralNetworks;

import java.awt.Color;
import cmg.graphics.Draw;

public class Food extends Entity {

	@Override
	public void update() {
		radius = 10;
	}

	@Override
	public void draw() {
		Draw.setColor(new Color(80, 255, 80));
		Draw.fillCircle(position, radius);
		Draw.setColor(Color.BLACK);
		Draw.drawCircle(position, radius);
	}
}
