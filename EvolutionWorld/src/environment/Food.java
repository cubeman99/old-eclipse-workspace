package environment;

import java.awt.Color;
import game.Main;
import common.GMath;
import common.Vector;

public class Food {
	public World world;
	public double size;
	public double energy;
	public Vector position;
	public double age;
	public double lifeSpan;
	public boolean dead;
	public double fade;
	public Color color;
	
	public Food(World world) {
		this(world, new Vector(GMath.random(world.size.x), GMath.random(world.size.y)));
	}
	
	public Food(World world, Vector position) {
		this.world    = world;
		this.position = new Vector(position).clip(new Vector(), world.size);
		this.size     = 0.2 + GMath.random(0.2);
		this.energy   = 10.0;
		this.age      = 0.0;
		this.lifeSpan = 3 + GMath.random(6);
		this.dead     = false;
		this.fade     = 1.0;
		this.color    = new Color(0, 255, 0);
	}
	
	public double getEnergy() {
		return energy;
	}
	
	public Color getColor() {
		return new Color(
				(int) ((double) color.getRed() * fade),
				(int) ((double) color.getGreen() * fade),
				(int) ((double) color.getBlue() * fade)
		);	
	}
	
	public boolean isAlive() {
		return (!dead);
	}
	
	public void update() {
		//age += 1.0 / Main.FPS;
		
		if (age > lifeSpan && !dead) {
			dead = true;
			int seeds = 2 + GMath.randomInt(13);
			for (int i = 0; i < seeds; i++) {
				if (GMath.random(5) >= 3) {
    				if (world.foodList.size() >= World.MAX_FOOD_COUNT) {
    					break;
    				}
    				
    				double dir   = GMath.random(GMath.TWO_PI);
    				double len   = GMath.random(0.9);
    				Vector pos   = position.plus(Vector.polarVector(len, dir));
    				Food newFood = new Food(world, pos);
    				world.foodList.add(newFood);
				}
			}
		}
		
		if (dead) {
			fade -= 0.04;
		}
	}
	
	public void relocate() {
		position.set(new Vector(GMath.random(world.size.x), GMath.random(world.size.y)));
	}
}
