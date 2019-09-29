package base.engine.entity;

import base.engine.core.Transform;
import base.engine.core.World;
import base.engine.rendering.RenderingEngine;
import base.engine.rendering.Shader;


public class EntityComponent {
	private Entity parent;
	
	
	public void input(float frameTime) {}
	public void update(float frameTime) {}
	public void render(Shader shader, RenderingEngine renderingEngine) {}
	public void onCreate() {}
	public void onDestroy() {}
	
	
	public Transform getTransform() {
		return parent.getTransform();
	}
	
	public World getWolrd() {
		return parent.getWorld();
	}
	
	public Entity getParent() {
		return parent;
	}
	
	public void setParent(Entity parent) {
		this.parent = parent;
	}
}
