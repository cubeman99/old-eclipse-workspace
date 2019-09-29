package projects.neuralNetworks;

import java.awt.Color;
import java.util.ArrayList;
import cmg.graphics.Draw;
import cmg.main.GameRunner;
import cmg.main.Keyboard;
import cmg.math.GMath;
import cmg.math.geometry.Vector;

public class World {
	private ArrayList<Entity> entities;
	private Vector size;
	private GameRunner runner;
	private int generation;
	private int timer;
	private int generationTime;
	private Critter bestCritter;
	private Critter currentCritter;
	
	public World(GameRunner runner) {
		this.runner = runner;
		entities = new ArrayList<Entity>(); 
		size = new Vector(500, 500);
		
		timer = 0;
		generationTime = 60 * 20;
		
		setup();
	}
	
	private void setup() {
		entities.clear();
		
		GMath.random.setSeed(11);
		for (int i = 0; i < 30; i++) {
			Food e = new Food();
			e.getPosition().set(getRandomPosition());
			addEntity(e);
		}

		GMath.random.setSeed(System.currentTimeMillis());
		if (bestCritter == null) {
			currentCritter = new Critter();
			bestCritter    = currentCritter;
		}
		else {
			//currentCritter = new Critter(bestCritter);
			if (!Keyboard.control.down()) {
				currentCritter = new Critter();
				//currentCritter.mutate(0.1);
			}
			else {
				currentCritter = new Critter(bestCritter);
			}
		}
		
		GMath.random.setSeed(11);
		currentCritter.getPosition().set(getRandomPosition());
		currentCritter.begin(this);
	}
	
	public Vector getSize() {
		return size;
	}
	
	public Vector getRandomPosition() {
		return new Vector(
    		GMath.random.nextDouble() * size.x,
    		GMath.random.nextDouble() * size.y
    	);
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
		e.begin(this);
	}
	
	public int getNumEntities() {
		return entities.size();
	}
	
	public Entity getEntity(int index) {
		return entities.get(index);
	}
	
	public void update() {
		boolean skip = Keyboard.enter.down() || Keyboard.space.pressed();
		int numUpdates = (skip ? generationTime - timer : 1);
		
		
		for (int index = 0; index < numUpdates; index++) {
    		timer++;
			
    		if (timer >= generationTime) {
    			generation++;
    			timer = 0;
    			
    			/*
    			double energy = 0;
    			int count = 0;
    			Critter best = null;
    			for (int i = 0; i < entities.size(); i++) {
        			if (entities.get(i) instanceof Critter) {
        				Critter c = (Critter) entities.get(i);
        				energy += c.getEnergy();
        				count++;
        				if (best == null || c.getEnergy() > best.getEnergy())
        					best = c;
        			}
        		}
    			if (count > 0)
    				energy /= count;
    			String str = String.format("Generation %d: Average Energy = %.3f", generation, energy);
    			System.out.print(str);
    			
    			if (bestCritter)
    			*/
    			
    			double energy = currentCritter.getEnergy();
    			
    			if (energy >= bestCritter.getEnergy())
    				bestCritter = currentCritter;


    			String str = String.format("Generation %d: Energy = %.3f", generation, energy);
    			System.out.print(str);
    			for (int i = 0; i <= (int) (energy * 0.5); i++)
    				System.out.print(".");
    			System.out.println("#");
    			
    			
    			setup();
    		}
    		
    		currentCritter.update();
    		
			for (int i = 0; i < entities.size(); i++) {
    			entities.get(i).update();
    		}
		}
	}
	
	public void draw() {
		Draw.setColor(Color.LIGHT_GRAY);
		Draw.fillRect(0, 0, runner.getViewWidth(), runner.getViewHeight());
		Draw.setColor(Color.WHITE);
		Draw.fillRect(0, 0, size.x, size.y);
		Draw.setColor(Color.BLACK);
		Draw.drawRect(0, 0, size.x, size.y);
		
		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).draw();
		}
		
		currentCritter.draw();
	}
}
