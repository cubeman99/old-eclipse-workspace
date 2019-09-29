package com.base.engine.entity;

import java.awt.peer.LightweightPeer;
import java.util.ArrayList;
import com.base.engine.entity.lights.BaseLight;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.SceneGraph;
import com.base.engine.rendering.Shader;

public class World extends SceneObject implements SceneGraph {
	private ArrayList<BaseLight> lights;
	
	public World() {
		super();
		lights = new ArrayList<BaseLight>();
	}
	
	public void addLight(BaseLight light) {
		lights.add(light);
	}
	
	@Override
	public void addChild(SceneObject child) {
		if (child instanceof BaseLight)
			lights.add((BaseLight) child);
		super.addChild(child);
	}
	
	@Override
	public void renderScene(Shader shader, RenderingEngine renderingEngine) {
		render(shader, renderingEngine);
	}

	@Override
	public ArrayList<BaseLight> getLights() {
		return lights;
	}
}
