package com.base.engine.entity;

import java.util.ArrayList;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.Transform;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;

public class EntityObject extends Entity {
	private ArrayList<EntityObject> children;
	private ArrayList<ObjectComponent> components;
	private Transform transform;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public EntityObject() {
		children   = new ArrayList<EntityObject>();
		components = new ArrayList<ObjectComponent>();
		transform  = new Transform();
		engine     = null;
	}

	
	
	// =================== ACCESSORS =================== //
	
	public ArrayList<ObjectComponent> getComponents() {
		return components;
	}
	
	public ObjectComponent getComponent(int index) {
		return components.get(index);
	}
	
	public int numComponents() {
		return components.size();
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void addChild(EntityObject child) {
		children.add(child);
		child.setEngine(engine);
		child.getTransform().setParent(transform);
	}

	public EntityObject addComponent(ObjectComponent component) {
		components.add(component);
		component.setParent(this);
		return this;
	}

	public ArrayList<EntityObject> getAllAttached() {
		ArrayList<EntityObject> result = new ArrayList<EntityObject>();

		for(EntityObject child : children)
			result.addAll(child.getAllAttached());

		result.add(this);
		return result;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void input(float delta) {
		transform.update();

		for(ObjectComponent component : components)
			component.input(delta);
		
		for(EntityObject child : children)
			child.input(delta);
	}

	@Override
	public void update(float delta) {
		for(ObjectComponent component : components)
			component.update(delta);

		for(EntityObject child : children)
			child.update(delta);
	}

	@Override
	public void render(Shader shader, RenderingEngine renderingEngine) {
		for(ObjectComponent component : components)
			component.render(shader, renderingEngine);
		
		for(EntityObject child : children)
			child.render(shader, renderingEngine);
	}
	
	@Override
	public void setEngine(CoreEngine engine) {
		if (this.engine != engine)	{
			this.engine = engine;

			for(ObjectComponent component : components)
				component.addToEngine(engine);

			for(EntityObject child : children)
				child.setEngine(engine);
		}
	}
}
