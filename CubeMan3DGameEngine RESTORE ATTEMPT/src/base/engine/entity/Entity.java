package base.engine.entity;

import java.util.ArrayList;
import base.engine.core.CoreEngine;
import base.engine.core.Transform;
import base.engine.core.World;
import base.engine.rendering.Shader;

public class Entity {
	private ArrayList<EntityComponent> components;
	private Transform transform;
	private World world;
	
	

	// ================== CONSTRUCTORS ================== //
	
	public Entity() {
		components = new ArrayList<EntityComponent>();
		transform  = new Transform();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public ArrayList<EntityComponent> getComponents() {
		return components;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public World getWorld() {
		return world;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void onEnterWorld() {
		for (int i = 0; i < components.size(); i++)
			components.get(i).onCreate();
	}
	
	public void input(float delta)
	{
//		transform.update();

		for (int i = 0; i < components.size(); i++)
			components.get(i).input(delta);
	}

	public void update(float frameTime)
	{
		for (int i = 0; i < components.size(); i++)
			components.get(i).update(frameTime);
	}

//	public void render(Shader shader, RenderingEngine renderingEngine)
//	{
//		for (int i = 0; i < components.size(); i++)
//			components.get(i).render(shader, renderingEngine);
//	}
	
	public Entity addComponent(EntityComponent c) {
		components.add(c);
		c.setParent(this);
		return this;
	}
	
	public Entity setTransform(Transform transform) {
		this.transform = transform;
		return this;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
}
