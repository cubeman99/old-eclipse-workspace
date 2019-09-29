package simulation;

import java.awt.Color;
import java.awt.Graphics;
import common.Draw;
import common.Vector;
import common.ViewControl;
import critter.Critter;
import environment.Food;
import environment.World;
import game.Game;
import game.Keyboard;
import game.Mouse;

public class Simulator {
	
	public World world;
	public ViewControl viewControl;
	public int generation;
	
	public Critter followCritter;
	
	public Simulator() {
		world       = new World(30, 30);
		viewControl = new ViewControl(world);
		generation  = 1;
		
		
		for (int i = 0; i < 0; i++) {
			Critter c = new Critter(world);
			c.createNewSpecies();
			followCritter = c;
			world.critterList.add(c);
		}
		for (int i = 0; i < 40; i++)
			world.foodList.add(new Food(world));
	}
	
	public void update() {
		Vector ms = Mouse.getVector();
		viewControl.updateControls();

		//viewControl.zoomFollow(followCritter.position);
		
		
		if (Mouse.right.down()) {
			viewControl.pan.add(Mouse.getVectorPrevious().minus(Mouse.getVector()).scale(1.0 / viewControl.zoom));
		}
		
		if (Keyboard.newCritter.pressed()) {
			Vector pos = new Vector(viewControl.pan.x + (ms.x / viewControl.zoom), viewControl.pan.y + (ms.y / viewControl.zoom));
			
			Critter c = new Critter(world);
			c.createNewSpecies();
			c.setPosition(pos);
			world.critterList.add(c);
		}
		
		if (Keyboard.enter.pressed()) {
			// Next Generation:
			generation++;
			Critter fittest = world.getFittestCritter();
			world.critterList.clear();
			if (fittest != null) {
    			for (int i = 0; i < 50; i++) {
    				world.critterList.add(fittest.getClone());
    			}
			}
		}
		
		world.update();
	}
	
	public void draw(Graphics g) {
		Vector ms = Mouse.getVector();
		world.draw(g, viewControl.pan, viewControl.zoom);
		
		g.setColor(Color.WHITE);
		g.drawString("Generation " + world.generation, 16, 32);
	}
}
