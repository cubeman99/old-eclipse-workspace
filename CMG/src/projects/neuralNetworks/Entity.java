package projects.neuralNetworks;

import cmg.math.geometry.Vector;

public abstract class Entity {
	protected World world;
	protected Vector position;
	protected double radius;
	
	public Entity() {
		position = new Vector();
	}
	
	public void begin(World world) {
		this.world = world;
		begin();
	}
	
	protected void begin() {}
	
	public abstract void update();
	public abstract void draw();
	
	public World getWorld() {
		return world;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public Vector getPosition() {
		return position;
	}
}
