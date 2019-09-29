package projects.neuralNetworks;

import java.awt.Color;
import cmg.graphics.Draw;
import cmg.math.GMath;
import cmg.math.geometry.Vector;

public class Critter extends Entity {
	private NeuralNetwork brain;
	private Vector velocity;
	private double direction;
	private double energy;
	
	public Critter() {
		super();
		radius = 16;
		
		// Neural Network:
		//  Inputs:
		//   1. direction
		//   2. Random
		//   3. x distance to nearest food
		//   4. y distance to nearest food
		//  One Hidden Layer with 3 nodes.
		//  Outputs:
		//   1. directionSpeed
		
		brain = new NeuralNetwork(4, 1);
		brain.addHiddenLayer(4);
		brain.addHiddenLayer(4);
		brain.initialize();
		
		velocity  = new Vector();
		direction = 0;
		energy    = 0;
	}

	public Critter(Critter c) {
		this();
		brain.set(c.brain);
	}
	
	private Food getNearestFood() {
		Food nearestFood = null;
		double nearestDist = 0;
		
		for (int i = 0; i < world.getNumEntities(); i++) {
			if (world.getEntity(i) instanceof Food) {
				Food food = (Food) world.getEntity(i);
				double dist = position.distanceTo(food.getPosition());
				if (nearestFood == null || dist < nearestDist) {
					nearestFood = food;
					nearestDist = dist;
				}
			}
		}
		
		return nearestFood;
	}
	
	public NeuralNetwork getBrain() {
		return brain;
	}
	
	public double getEnergy() {
		return energy;
	}
	
	public void mutate(double amount) {
		brain.mutate(amount);
	}
	
	public void giveEnergy(double amount) {
		 energy += amount;
	}
	
	@Override
	public void update() {
		Food nearestFood = getNearestFood();
		
		if (nearestFood != null) {
			Vector v = nearestFood.getPosition().minus(position);
    		double[] inputs = {direction, GMath.random.nextDouble(), v.x, v.y};
    		double[] outputs = brain.calculate(inputs);
    		direction += outputs[0] / 10;
    		if (v.length() < radius + nearestFood.getRadius()) {
    			energy += 1;
    			for (int i = 0; i < 10; i++) {
    				nearestFood.getPosition().set(world.getRandomPosition());
    				if (nearestFood.getPosition().distanceTo(position) > 45)
    					break;
    			}
    		}
		}
		
		velocity.setPolar(1.5, direction);
		position.add(velocity);
		
		if (position.x < 0)
			position.x = 0;
		if (position.y < 0)
			position.y = 0;
		if (position.x > world.getSize().x)
			position.x = world.getSize().x;
		if (position.y > world.getSize().y)
			position.y = world.getSize().y;
	}

	@Override
	public void draw() {
		Draw.setColor(new Color(255, 255, 220));
		Draw.fillCircle(position, radius);
		Draw.setColor(Color.BLACK);
		Draw.drawCircle(position, radius);
		Draw.drawLine(position, position.plus(
				Vector.polarVector(radius, direction)));
	}
	
}
