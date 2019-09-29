package critter;

import java.awt.Color;
import java.util.ArrayList;
import brain.Genome;
import brain.NeuralNet;
import brainOLD.Brain;
import common.Energy;
import common.GMath;
import common.Settings;
import common.Vector;
import environment.Food;
import environment.World;
import game.Keyboard;
import game.Main;

public class Critter {
	public static final double DEFAULT_RADIUS =   0.5;
	public static final double MAX_ENERGY     = 100.0;
	public static final double MAX_SPEED      =   0.1;
	
	public World world;
	public Vector position;
	public Vector velocity;
	public NeuralNet brain;
	public double radius;
	public Color color;
	public double turnSpeed;
	public double energy;
	public double speed;
	public double direction;
	public boolean mate;
	public boolean attack;
	public double greenness;
	
	public double strength;
	
	public double sightRed;
	public double sightGreen;
	public double sightBlue;
	
	public double fov;
	public double depth;
	
	public double age;
	public double energyTotal;
	
	public Vector closestPoint;
	public int fitness;

	public Critter(World world) {
		this(world, null);
	}
	
	public Critter(World world, Genome genome) {
		this.world				= world;
		if (genome != null)
			this.brain			= new NeuralNet(genome);
		else
			this.brain          = new NeuralNet();
		this.position			= new Vector(GMath.random(world.size.x), GMath.random(world.size.y));
		this.velocity			= new Vector();
		this.direction			= GMath.random(GMath.TWO_PI);
		this.speed				= 0;
		this.energy				= MAX_ENERGY * 0.5;
		this.sightRed			= 0.0;
		this.sightGreen			= 0.0;
		this.sightBlue			= 0.0;
		
		this.greenness			= 100.0 + GMath.random(155.0);
		
		this.fov				= 1.5;
		this.depth				= 10.0;
		this.mate				= false;
		this.attack				= false;
		
		this.strength			= 50.0;
		this.color				= new Color((int) GMath.random(256), (int) GMath.random(256), (int) GMath.random(256));
		this.radius				= DEFAULT_RADIUS;
		
		this.turnSpeed			= 0;
		this.age				= 0;
		this.energyTotal		= this.energy;
		this.closestPoint		= null;
		this.fitness            = 0;
	}
	
	public Critter(Critter c) {
		this(c.world);
//		this.brain = c.brain.getCopy(this);
		// TODO
	}
	
	public void createNewSpecies() {
//		brain.createRandomNeuralNetwork();
	}
	
	public Vector getClosestFood(Vector closest, double offsetX, double offsetY) {
		Vector closestFood = closest;
		double closestDist = (closest == null ? 0 : position.distanceTo(closest));
		Vector offset      = new Vector(offsetX, offsetY);
		
		for (Food food : world.foodList) {
			double dist = food.position.plus(offset).distanceTo(position);
			if (dist < closestDist || closestFood == null) {
				closestFood = food.position.plus(offset);
				closestDist = dist;
			}
		}
		return new Vector(closestFood);
	}
	
	public Vector getClosestFood() {
		Vector closest = null;
		closest = getClosestFood(closest, world.size.x, 0);
		closest = getClosestFood(closest, world.size.x, -world.size.y);
		closest = getClosestFood(closest, 0, -world.size.y);
		closest = getClosestFood(closest, -world.size.x, -world.size.y);
		closest = getClosestFood(closest, -world.size.x, 0);
		closest = getClosestFood(closest, -world.size.x, world.size.y);
		closest = getClosestFood(closest, 0, world.size.y);
		return closest;
	}
	
	public boolean update() {
		ArrayList<Double> inputs = new ArrayList<Double>();
		Vector vecClosestFood = getClosestFood().minus(position);
		inputs.add(vecClosestFood.x);
		inputs.add(vecClosestFood.y);
		
		// Update the brain and give feedback.
		ArrayList<Double> outputs = brain.update(inputs);
		
		// Make sure there were no errors in calculating the output
		if (outputs.size() < Settings.NUM_OUTPUTS) {
//			System.out.println("ASDASD");
			return false;
		}
		
		double trackL = outputs.get(0);
		double trackR = outputs.get(1);
//		speed = trackL + trackR;
		turnSpeed = trackL - trackR;
		turnSpeed = GMath.max(-0.3, GMath.min(0.4, turnSpeed));
		speed     = (trackL + trackR) / 10;
		speed     = GMath.min(0.2, speed);
		// Assign the outputs to the speed and turn speed;
//		speed     = outputs.get(0);
//		speed     = 0.15;
//		turnSpeed = 0.4 - outputs.get(0);
		
		age += 1.0 / Main.FPS;
		
//		if (Keyboard.moveRight.down())
//			direction -= 0.07;
//		if (Keyboard.moveLeft.down())
//			direction += 0.07;
//		if (Keyboard.moveUp.down())
//			speed += 0.01;
//		else
//			speed -= 0.02;
		
//        speed      = GMath.max(0, GMath.min(speed, 0.3));
        direction += turnSpeed;
        direction %= GMath.TWO_PI;
        velocity.setPolar(speed, direction);
		
        //System.out.println(inputs);
        velocity.setPolar(0.1, (trackL - trackR) * 10);
        
		// Eat food:
		for (int i = 0; i < world.foodList.size(); i++) {
			Food food = world.foodList.get(i);
			if (food.isAlive() && touching(food)) {
				giveEnergy(food.getEnergy());
				//food.dead = true;
				food.relocate();
			}
		}
		
		// Check for attacking situations:
		if (attack) {
        	for (int i = 0; i < world.critterList.size(); i++) {
        		Critter c = world.critterList.get(i);
        		if (c != this && !c.isDead() && touching(c)) {
        			attack(c);
        		}
        	}
		}
		// Check for mating situations:
		else if (mate) {
        	for (int i = 0; i < world.critterList.size(); i++) {
        		Critter c = world.critterList.get(i);
        		if (c != this && !c.isDead() && c.mate && touching(c)) {
        			reproduce(c);
        		}
        	}
		}
		
		
		
		
		// Cost energy to move:
		//costEnergy(velocity.length() * Energy.COST_MOVE);
		position.add(velocity);
		
		if (position.x < 0)
			position.x += world.size.x;
		if (position.y < 0)
			position.y += world.size.y;
		if (position.x > world.size.x)
			position.x -= world.size.x;
		if (position.y > world.size.y)
			position.y -= world.size.y;
		/*
		if (position.x < 0) {
			position.x = 0;
			limitDirection(0);
		}
		if (position.x > world.size.x - radius) {
			position.x = world.size.x - radius;
			limitDirection(0);
		}
		if (position.y < 0) {
			position.y = 0;
			limitDirection(GMath.HALF_PI);
		}
		if (position.y > world.size.y - radius) {
			position.y = world.size.y - radius;
			limitDirection(GMath.HALF_PI);
		}
		*/
		
		Color col  = getClosestColor();
		sightRed   = col.getRed();
		sightGreen = col.getGreen();
		sightBlue  = col.getBlue();

		// Cost energy to exist:
		//costEnergy(Energy.COST_EXIST * (1.0 / Main.FPS));
		
		return true;
	}
	
	public Color getClosestColor() {
		double closestDistance = 0;
		Color closestColor     = null;
		closestPoint           = null;
		
    	for (int i = 0; i < world.critterList.size(); i++) {
    		Critter c   = world.critterList.get(i);
    		double dist = c.position.distanceTo(position);
    		if (c != this && !c.isDead() && (dist < closestDistance || closestColor == null) && pointInFov(c.position)) {
    			closestDistance = dist;
    			closestColor    = c.getColor();
    			closestPoint    = c.position;
    		}
    	}
		for (int i = 0; i < world.foodList.size(); i++) {
			Food food = world.foodList.get(i);
    		double dist = food.position.distanceTo(position);
    		if (food.isAlive() && (dist < closestDistance || closestColor == null) && pointInFov(food.position)) {
    			closestDistance = dist;
    			closestColor    = food.getColor();
    			closestPoint    = food.position;
    		}
		}
		if (closestColor == null)
			closestColor = Color.BLACK;
		return closestColor;
	}
	
	public double fovMinDirection() {
		return ((direction - (fov * 0.5)) % GMath.TWO_PI);
	}
	
	public double fovMaxDirection() {
		return ((direction + (fov * 0.5)) % GMath.TWO_PI);
	}
	
	public boolean pointInFov(Vector pos) {
		if (pos.distanceTo(position) > depth)
			return false;
		if (directionInFov(GMath.direction(position, pos)))
			return true;
		return false;
	}
	
	public boolean directionInFov(double dir) {
		return (GMath.angleBetween(dir, direction) <= fov * 0.5);
	}
	
	public void attack(Critter prey) {
		if (GMath.angleBetween(GMath.direction(position, prey.position), direction) > GMath.HALF_PI * 0.7)
			return;
		
		double factorSpeed  = 0.5 + (0.5 * (velocity.length() / MAX_SPEED));
		double factorEnergy = 0.5 + (0.5 * (energy / MAX_ENERGY));
		costEnergy(Energy.COST_ATTACK * factorSpeed * factorEnergy);
		prey.costEnergy(strength);
	}
	
	public void reproduce(Critter partner) {
		if (isDead() || partner.isDead())
			return;
//		if (energy <= Energy.COST_REPRODUCE)
//			return;
//		if (partner.energy <= Energy.COST_REPRODUCE)
//			return;
		
		costEnergy(Energy.COST_REPRODUCE);
		partner.costEnergy(Energy.COST_REPRODUCE);
		
		Critter c = new Critter(this);
		//c.brain.neuralNet.mutate();
		c.position.set(this.position.plus(partner.position).scale(0.5));
		
		world.critterList.add(c);
	}
	
	public Critter getClone() {
		Critter c = new Critter(this);
		return c;
	}
	
	public boolean isDead() {
		return (energy <= 0);
	}
	
	public void setPosition(Vector newPosition) {
		position.set(newPosition);
	}
	
	private void limitDirection(double direction) {
		velocity.set(velocity.rejectionOn(Vector.polarVector(1, direction)));
	}
	
	public boolean touching(Food food) {
		return (position.distanceTo(food.position) < radius + (food.size * 0.5));
	}
	
	public boolean touching(Critter c) {
		return (position.distanceTo(c.position) < radius + c.radius);
	}
	
	public void giveEnergy(double amount) {
		energy = GMath.min(energy + amount, MAX_ENERGY);
		energyTotal -= amount;
		if (amount > 0)
			fitness += amount;
	}
	
	public void costEnergy(double amount) {
		energy -= amount;
		energyTotal -= amount;
		if (energy <= 0) {
			// Die!
			world.kill(this);
		}
	}
	
	public int getFitness() {
		return fitness;
	}
	
	public Color getColor() {
//		int r = attack ? 255 : 0;
//		int g = (int) greenness;
//		int b = (mate && !attack) ? 255 : 0;
		int r = attack ? 255 : 0;
		int g = 0;
		int b = (int) greenness;
		
//		return new Color(r, g, b);
		return Color.WHITE;
	}
	
	public double getDirection() {
		return velocity.direction();
//		return direction;
	}
}
