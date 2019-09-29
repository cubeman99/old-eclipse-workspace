package com.base.engine.rendering;

import java.util.ArrayList;
import com.base.engine.entity.lights.BaseLight;

public interface SceneGraph {
	public ArrayList<BaseLight> getLights();
	public void renderScene(Shader shader, RenderingEngine renderingEngine);
}
