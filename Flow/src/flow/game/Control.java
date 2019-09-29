package flow.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import flow.common.GMath;
import flow.common.Vector;
import flow.main.GameRunner;

public class Control {
	public GameRunner runner;
	public ArrayList<Dot> dots;
	public Point size;
	
	// ================== CONSTRUCTORS ================== //
	
	/** Create a new game controller. **/
	public Control(GameRunner runner) {
		this.size = new Point(600, 800);
		this.runner = runner;
		this.dots = new ArrayList<Dot>();
		
		
		for (int i = 0; i < 100; i++) {
			dots.add(new Dot(this, new Vector(GMath.random.nextDouble() * (double) size.x, GMath.random.nextDouble() * (double) size.y)));
		}
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Update the game. **/
	public void update() {
		
		for (int i = 0; i < dots.size(); i++) {
			for (int j = 0; j < dots.size(); j++) {
				if (i != j)
					dots.get(i).applyGravity(dots.get(j).position);
			}
		}
		
		
		for (Dot dot : dots) {
			dot.update();
		}
	}
	
	/** Draw the game. **/
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, size.x, size.y);
		g.setColor(Color.BLACK);
		
		for (Dot dot : dots) {
			g.fillOval((int) dot.position.x - 2, (int) dot.position.y - 2, 4, 4);
		}
	}
}
