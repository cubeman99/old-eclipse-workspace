package base.engine.core;

import java.util.ArrayList;
import base.engine.common.Vector3;
import base.engine.entity.Entity;
import base.engine.entity.light.BaseLight;
import base.engine.rendering.Mesh;
import base.engine.rendering.RenderingEngine;

public class World {
	public ArrayList<Entity> entities;
	private ArrayList<BaseLight> lights;
	private CoreEngine engine;
	
	

	// ================== CONSTRUCTORS ================== //

	public World(CoreEngine engine) {
		entities = new ArrayList<Entity>();
		lights   = new ArrayList<BaseLight>();
		this.engine = engine;
	}
	
	

	// =================== ACCESSORS =================== //
	
	public ArrayList<BaseLight> getLights() {
		return lights;
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public CoreEngine getEngine() {
		return engine;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void addLight(BaseLight light) {
		lights.add(light);
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
		e.setWorld(this);
		e.onEnterWorld();
	}
	
	public void input(float frameTime) {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).input(frameTime);
	}
	
	public void update(float frameTime) {
		for (int i = 0; i < entities.size(); i++)
			entities.get(i).update(frameTime);
	}
	
	public void render(RenderingEngine renderingEngine) {
		for (int i = 0; i < entities.size(); i++) {
			renderingEngine.render(entities.get(i));
		}
	}
}
