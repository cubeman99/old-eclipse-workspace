package environment;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import brain.GeneticAlgorithm;
import brain.Genome;
import common.Draw;
import common.Settings;
import common.Vector;
import critter.Critter;

public class World {
	public static final int MAX_FOOD_COUNT = 10000;
	public static final int GRID_SIZE = 1;
	public Vector size;
	
	public GeneticAlgorithm geneticAlgorithm;
	public ArrayList<Food> foodList;
	public ArrayList<Critter> critterList;
	public ArrayList<Genome> population;
	public int generation;
	
	public int tickTimer;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public World(double width, double height) {
		size = new Vector(width, height);
		
		foodList    = new ArrayList<Food>();
		critterList = new ArrayList<Critter>();
		tickTimer   = 0;
		generation  = 0;
		
		for (int i = 0; i < 30; i++) {
			critterList.add(new Critter(this));
		}
		
		int numWeights   = critterList.get(0).brain.getNumberOfWeights();
		geneticAlgorithm = new GeneticAlgorithm(critterList.size(),
				Settings.MUTATION_RATE, Settings.CROSSOVER_RATE, numWeights);
		population       = geneticAlgorithm.getPopulation();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Critter getFittestCritter() {
		Critter fittest = null;
		for (int i = 0; i < critterList.size(); i++) {
			Critter c = critterList.get(i);
			if (fittest == null) {
				fittest = c;
			}
			else if (fittest.energyTotal > c.energyTotal) {
				fittest = c;
			}
		}
		return fittest;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void update() {
		tickTimer++;
		
		if (tickTimer > 30 * 60) {
			tickTimer = 0;
			nextGeneration();
		}
		for (int i = 0; i < critterList.size(); i++) {
			Critter c = critterList.get(i);
			c.update();
			if (c.energy <= 0) {
				Food corpse   = new Food(this, c.position);
				corpse.energy = 80;
				corpse.size   = 0.6;
				foodList.add(corpse);
				critterList.remove(i);
				i--;
			}
		}
		for (int i = 0; i < foodList.size(); i++) {
			Food food = foodList.get(i);
			food.update();
			if (food.dead && food.fade <= 0) {
				foodList.remove(i);
				i--;
			}
		}
	}
	
	public void nextGeneration() {
		generation++;
//		for (int i = 0; i < population.size(); i++) {
//			if (population.get(i).weights.size() == 0)
//				System.out.println("!!!Zero at i=" + i);
//		}
		
		for (int i = 0; i < critterList.size(); i++)
			population.get(i).fitness = critterList.get(i).getFitness();
		
		critterList.clear();
		
		population = geneticAlgorithm.epoch(population);
		
		for (int i = 0; i < population.size(); i++) {
//			if (population.get(i).weights.size() == 0)
//				System.out.println("Zero at i=" + i);
			Critter c = new Critter(this, population.get(i));
			critterList.add(c);
		}
	}

	public void kill(Critter c) {
		//critterList.remove(c);
	}
	
	public void kill(Food food) {
		foodList.remove(food);
	}
	
	public void draw(Graphics g, Vector pan, double zoom) {
		Draw.setGraphics(g);
		Draw.setView(pan, zoom);
		
		// Draw grid:
		Draw.setColor(new Color(32, 32, 32));
		
		for (int x = 0; x < size.x; x += GRID_SIZE)
			Draw.drawLine(x, 0, x, size.y);
		for (int y = 0; y < size.y; y += GRID_SIZE)
			Draw.drawLine(0, y, size.x, y);
		Draw.drawRect(Vector.ORIGIN, size);
		
		
		for (int i = 0; i < foodList.size(); i++) {
			Food food = foodList.get(i);
			Draw.setColor(food.getColor());
			Draw.fillRect(food.position.x - (food.size / 2.0), food.position.y - (food.size / 2.0), food.size, food.size);
			//Draw.drawRect(food.position.minus(new Vector(food.size / 2.0, food.size / 2.0)), new Vector(food.size, food.size));
		}
		
		for (int i = 0; i < critterList.size(); i++) {
			Critter c = critterList.get(i);
			
			// Draw field of view:
			
//			Draw.setColor(Color.DARK_GRAY);
//			Draw.drawLine(c.position, c.position.plus(Vector.polarVector(c.depth, c.getDirection() - (c.fov * 0.5))));
//			Draw.drawLine(c.position, c.position.plus(Vector.polarVector(c.depth, c.getDirection() + (c.fov * 0.5))));
//			Draw.drawArc(c.position, c.depth, c.direction - (c.fov * 0.5), c.fov);
			
			// Draw body:
			Draw.setColor(c.getColor());
			Draw.drawCircle(c.position, c.radius);
			Draw.drawLine(c.position, c.position.plus(Vector.polarVector(c.radius, c.getDirection())));
			
			// Draw vision color:
			Color col = c.getClosestColor();
			if (col != null) {
				Draw.setColor(col);
				Draw.fillCircle(c.position, c.radius * 0.2);
			}
		}
	}
}
