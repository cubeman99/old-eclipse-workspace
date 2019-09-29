package life;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import main.Keyboard;
import main.Mouse;

public class LifeRunner {
	private boolean running;
	private LifeInfinite life;
	private ArrayList<Integer> populationData;
	
	
	public LifeRunner() {
		running = false;
		life    = new LifeInfinite();
		populationData = new ArrayList<Integer>();
	}
	
	public void update() {
		Point ms  = Mouse.position();
		Point msc = life.getGridPoint(ms);
		
		if (Mouse.left.down()) {
			life.setCell(msc.x, msc.y, true);
			populationData.clear();
		}
		if (Mouse.right.down()) {
			life.setCell(msc.x, msc.y, false);
			populationData.clear();
		}
		
		if (running || Keyboard.space.pressed()) {
			life.tick();
			populationData.add(life.getPopulation());
		}
		if (Keyboard.enter.pressed())
			running = !running;
		if (Keyboard.backspace.pressed()) {
			life.clear();
			populationData.clear();
		}
	}
	
	public void drawPopulationGraph(Graphics g, Rectangle r) {
		int max = 10;
		for (int i = 0; i < populationData.size(); i++) {
			if (populationData.get(i) > max)
				max = populationData.get(i);
		}
		
		double xscale = (double) r.width / (double) populationData.size();
		double yscale = (double) r.height / (double) max;
		int xInterval = (int) Math.ceil(1.0 / xscale);

		g.setColor(Color.BLACK);
		g.fillRect(r.x, r.y, r.width, r.height);
		
		Point prev = null;
		g.setColor(Color.GREEN);
		for (int i = 0; i < populationData.size(); i += xInterval) {
			double data = populationData.get(i);
			Point p = new Point(r.x + (int) ((double) i * xscale), r.y + r.height - (int) (data * yscale));
			if (prev != null)
				g.drawLine(prev.x, prev.y, p.x, p.y);
			prev = p;
		}
	}
	
	public void draw(Graphics g) {
		life.draw(g);
		
		Rectangle r = new Rectangle(0, 400, Math.min(populationData.size(), 400), 128);
		drawPopulationGraph(g, r);
	}
}
