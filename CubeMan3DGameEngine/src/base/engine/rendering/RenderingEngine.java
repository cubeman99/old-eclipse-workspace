package base.engine.rendering;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;
import java.util.ArrayList;
import java.util.HashMap;
import base.engine.common.Vector3;
import base.engine.core.Transform;
import base.engine.entity.Camera;
import base.engine.entity.Entity;
import base.engine.entity.light.BaseLight;
import base.engine.rendering.resourceManagement.MappedValues;

public class RenderingEngine extends MappedValues {
	private HashMap<String, Integer> samplerMap;
	private ArrayList<BaseLight> lights;
	private BaseLight activeLight;

	private Shader forwardAmbient;
	private Camera mainCamera;

	
	
	// ================== CONSTRUCTORS ================== //

	public RenderingEngine() {
		super();
		
		lights     = new ArrayList<BaseLight>();
		samplerMap = new HashMap<String, Integer>();
		samplerMap.put("diffuse",   0);
		samplerMap.put("normalMap", 1);
		samplerMap.put("dispMap",   2);

		addVector3("ambient", new Vector3(0.1f, 0.1f, 0.1f));

		forwardAmbient = new Shader("forward-ambient");

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);

		glEnable(GL_DEPTH_CLAMP);

		glEnable(GL_TEXTURE_2D);
	}

	// =================== ACCESSORS =================== //

	public int getSamplerSlot(String samplerName) {
		return samplerMap.get(samplerName);
	}
	
	public BaseLight getActiveLight() {
		return activeLight;
	}

	public Camera getMainCamera() {
		return mainCamera;
	}

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}

	

	// ==================== MUTATORS ==================== //
	
	public void render(Entity entity) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		entity.render(forwardAmbient, this);

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);

		for (BaseLight light : lights) {
			activeLight = light;
			entity.render(light.getShader(), this);
		}

		glDepthFunc(GL_LESS);
		glDepthMask(true);
		glDisable(GL_BLEND);
	}
	
	public void addLight(BaseLight light) {
		lights.add(light);
	}
	
	public void updateUniformStruct(Transform transform, Material material,
			Shader shader, String uniformName, String uniformType)
	{
		throw new IllegalArgumentException(uniformType + " is not a supported type in RenderingEngine");
	}

	public void setMainCamera(Camera mainCamera) {
		this.mainCamera = mainCamera;
	}
}
