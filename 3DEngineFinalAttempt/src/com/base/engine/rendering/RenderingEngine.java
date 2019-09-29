package com.base.engine.rendering;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.BufferUtils;
import com.base.engine.common.Matrix4f;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Transform;
import com.base.engine.core.Util;
import com.base.engine.core.Vertex;
import com.base.engine.entity.Entity;
import com.base.engine.entity.EntityObject;
import com.base.engine.entity.lights.BaseLight;
import com.base.engine.rendering.resourceManagement.MappedValues;
import com.base.engine.rendering.resourceManagement.ResourceManager;
import com.base.game.TestModels;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class RenderingEngine extends MappedValues {
	private static final int NUM_SHADOW_MAPS = 10;
	
	private HashMap<String, Integer> samplerMap;
	
	private Camera mainCamera;
	private Camera altCamera;
	
	private ArrayList<BaseLight> lights;
	private BaseLight activeLight;
	private Matrix4f lightMatrix;
	private Texture[] shadowMaps;
	private Texture[] shadowMapTempTargets;
	private Mesh filterMesh;
	private Transform filterTransform;
	private Material filterMaterial;
	private Mesh skyBoxMesh;
	private Texture skyBoxTexture;
	
	private Shader forwardAmbient;
	private Shader shadowMapShader;
	private Shader skyBoxShader;
	private Shader reflectionShader;
	private Shader nullFilter;
	private Shader gausBlurFilter;
	
	private static Texture defaultCubeMap;
	
	
	public int viewPortX;
	public int viewPortY;
	public int viewPortWidth;
	public int viewPortHeight;
	
	
	// ================== CONSTRUCTORS ================== //

	public RenderingEngine() {
		samplerMap = new HashMap<String, Integer>();
		samplerMap.put("diffuse",       0);
		samplerMap.put("normalMap",     1);
		samplerMap.put("dispMap",       2);
		samplerMap.put("shadowMap",     3);
		samplerMap.put("filterTexture", 4);
		samplerMap.put("cubeMap",       5);
//		samplerMap.put("blendDiffuse",  5);
		samplerMap.put("specularMap",   6);
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_DEPTH_CLAMP);
		glEnable(GL_TEXTURE_2D);
		
		defaultCubeMap = new Texture("unknown.jpg", true, false);
		setTexture("cubeMap", new Texture("black.jpg"));
		setVector3f("ambientIntensity", new Vector3f(0.2f, 0.2f, 0.2f));
		lights        = new ArrayList<BaseLight>();
		activeLight   = null;
		lightMatrix   = Matrix4f.createScale(0, 0, 0);
		skyBoxMesh    = TestModels.createCubeMap();
		skyBoxTexture = null;
		
		// Load shaders.
		shadowMapShader  = new Shader("shadowMapGenerator");
		nullFilter       = new Shader("filter-null");
		gausBlurFilter   = new Shader("filter-gausBlur7x1");
		skyBoxShader     = new Shader("skybox");
		forwardAmbient   = new Shader("forward-ambient");
		reflectionShader = new Shader("reflection");
		
		// Create the list of shadow maps.
		shadowMaps           = new Texture[NUM_SHADOW_MAPS];
		shadowMapTempTargets = new Texture[NUM_SHADOW_MAPS];
		for (int i = 0; i < NUM_SHADOW_MAPS; i++) {
			int size = 1 << (i + 1);
			shadowMaps[i] = new Texture(size, size, (ByteBuffer) null,
					GL_TEXTURE_2D, GL_LINEAR, GL_RG32F, GL_RGBA, true,
					GL_COLOR_ATTACHMENT0);
			shadowMapTempTargets[i] = new Texture(size, size, (ByteBuffer) null,
					GL_TEXTURE_2D, GL_LINEAR, GL_RG32F, GL_RGBA, true,
					GL_COLOR_ATTACHMENT0);
		}

		viewPortX = 0;
		viewPortY = 0;
		viewPortWidth = Window.getWidth();
		viewPortHeight = Window.getHeight();
		
		// Filter/temporary scene setup.
		altCamera  = new Camera((float) Math.toRadians(60.0f), Window.getAspectRatio(), 0.01f, 1000.0f);
		filterMesh = TestModels.createBoxMesh();
		filterMaterial = new Material();
		filterMaterial.setTexture(new Texture("white.jpg"));
		filterTransform = new Transform();
		filterTransform.rotate(new Vector3f(0, 0, 1), (float) Math.toRadians(180.0f));
	}

	
	
	// =================== ACCESSORS =================== //
	
	public ArrayList<BaseLight> getLights() {
		return lights;
	}
	
	public int getSamplerSlot(String samplerName) {
		return samplerMap.get(samplerName);
	}
	
	public Camera getMainCamera() {
		return mainCamera;
	}
	
	public BaseLight getActiveLight() {
		return activeLight;
	}
	
	public Matrix4f getLightMatrix() {
		return lightMatrix;
	}
	
	public Vector3f getAmbientLight() {
		return getVector3f("ambientIntensity");
	}

	public static String getOpenGLVersion() {
		return glGetString(GL_VERSION);
	}
	
	public Shader getForwardAmbient() {
		return forwardAmbient;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setAmbientLight(Vector3f ambientLight) {
		setVector3f("ambientIntensity", ambientLight);
	}
	
	public void setSkyBox(Texture texture) {
		skyBoxTexture = texture;
	}
	
	public void setViewPort(int x, int y, int width, int height) {
		viewPortX      = x;
		viewPortY      = y;
		viewPortWidth  = width;
		viewPortHeight = height;
	}
	
	public void addLight(BaseLight light) {
		lights.add(light);
	}

	public void removeLight(BaseLight light) {
		lights.remove(light);
	}
	
	public void setMainCamera(Camera mainCamera) {
		this.mainCamera = mainCamera;
	}
	
	public static void clearScreen() {
		// TODO: Stencil Buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public static void setClearColor(Vector3f color) {
		glClearColor(color.getX(), color.getY(), color.getZ(), 1.0f);
	}
	
	void applyFilter(Shader filter, Texture source, Texture destination) {
		if (source == destination) {
			System.err.println("Error: filters must be applied to a different texture than the source.");
			System.exit(1);
		}
		if (destination == null)
			Window.bindAsRenderTarget();
		else
			destination.bindAsRenderTarget();
		
		setTexture("filterTexture", source);
		
		altCamera.setProjection(Matrix4f.createIdentity());
		altCamera.getTransform().setPosition(new Vector3f(0, 0, -1));
		altCamera.getTransform().setRotation(new Quaternion(new Vector3f(0, 1, 0), (float) Math.PI));
		
		Camera temp = mainCamera;
		mainCamera = altCamera;
		
		glClear(GL_DEPTH_BUFFER_BIT);
		filter.bind();
		filter.updateUniforms(filterTransform, this, filterMaterial);
		filterMesh.draw();
		
		mainCamera = temp;

		setTexture("filterTexture", null);
	}
	
	public void blurFilter(Texture target, Texture tempTarget, float blurAmount) {
		// Blur x-axis.
		setVector3f("blurScale", new Vector3f(blurAmount / (target.getWidth()), 0.0f, 0.0f));
		applyFilter(gausBlurFilter, target, tempTarget);
		
		// Blur y-axis.
		setVector3f("blurScale", new Vector3f(0.0f, blurAmount / target.getHeight(), 0.0f));
		applyFilter(gausBlurFilter, tempTarget, target);
	}
	
	public void blurShadowMap(int shadowMapIndex, float blurAmount) {
		Texture shadowMap = shadowMaps[shadowMapIndex];
		// Blur x-axis.
		setVector3f("blurScale", new Vector3f(blurAmount / (shadowMap.getWidth()), 0.0f, 0.0f));
		applyFilter(gausBlurFilter, shadowMap, shadowMapTempTargets[shadowMapIndex]);
		
		// Blur y-axis.
		setVector3f("blurScale", new Vector3f(0.0f, blurAmount / shadowMap.getHeight(), 0.0f));
		applyFilter(gausBlurFilter, shadowMapTempTargets[shadowMapIndex], shadowMap);
	}

	public static void unbindTextures() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public void render(SceneGraph scene) {
		render(scene, null, false, true);
	}
	
	public void render(SceneGraph scene, Texture renderTarget, boolean wireFrame, boolean renderSky) {
		if (renderTarget == null)
			Window.bindAsRenderTarget(viewPortX, viewPortY, viewPortWidth, viewPortHeight);
		else
			renderTarget.bindAsRenderTarget();
		
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glDepthMask(true);
		Camera temp;
		
		if (wireFrame) {
    		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    		glDisable(GL_CULL_FACE);
		}
		else {
    		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
    		glEnable(GL_CULL_FACE);
		}
		
		// Render the Skybox.
		if (skyBoxTexture != null && renderSky) {
			glDisable(GL_DEPTH_TEST);
    		altCamera.setProjection(mainCamera.getProjection());
    		altCamera.getTransform().setPosition(0, 0, 0);
    		altCamera.getTransform().setRotation(mainCamera.getTransform().getRotation());
    		temp = mainCamera;
    		mainCamera  = altCamera;
    		
    		setTexture("cubeMap", skyBoxTexture);
    		skyBoxShader.bind();
    		skyBoxShader.updateUniforms(new Transform(), this, null);
    		skyBoxMesh.draw();
    		
    		mainCamera = temp;
			glEnable(GL_DEPTH_TEST);
		}
		
		// Render the forward ambient.
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		scene.renderScene(forwardAmbient, this);
		glDisable(GL_BLEND);
//		lightMatrix = mainCamera.getViewProjection();
//		scene.renderScene(forwardAmbient, this);
		
		// Render reflections.
		/*
		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);
		glDepthMask(false);
		glDepthFunc(GL_EQUAL);
		if (skyBoxTexture == null)
			setTexture("cubeMap", defaultCubeMap);
		else
			setTexture("cubeMap", skyBoxTexture);
		scene.renderScene(reflectionShader, this);
		glDepthFunc(GL_LESS);
		glDepthMask(true);
		glDisable(GL_BLEND);
		*/
		
		// DEBUG: RENDER A CUBE:
//		setAmbientLight(new Vector3f(1,1,1));
//		Shader.forwardAmbient.bind();
//		material.setTexture(getTexture("shadowMap"));
//		Shader.forwardAmbient.updateUniforms(transform, this, material);
//		mesh.draw();
//		setAmbientLight(new Vector3f(0.2f, 0.2f, 0.2f));
		
		
		// Render lighting.
		for (BaseLight light : scene.getLights()) {
			activeLight = light;
			
			// Calculate Shadow Map.
			int shadowMapIndex = 0;
			ShadowInfo shadowInfo = light.getShadowInfo();
			if (shadowInfo != null)
				shadowMapIndex = shadowInfo.getShadowMapSizeIndex() - 1;
			Texture shadowMap = shadowMaps[shadowMapIndex];

			// Clear the shadow map.
			shadowMap.bindAsRenderTarget();
			glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
			glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
			setTexture("shadowMap", shadowMap);
			
			if (shadowMapIndex != 0) {
				// Cast shadows.
				altCamera.setProjection(shadowInfo.getProjection());
				altCamera.getTransform().set(light.getShadowCameraTransform(mainCamera.getTransform()));
//				altCamera.getTransform().setPosition(light.getTransform().getTransformedPosition());
//				altCamera.getTransform().setRotation(light.getTransform().getTransformedRotation());
				
				lightMatrix = altCamera.getViewProjection();
				setFloat("minVariance", shadowInfo.getMinVariance());
				setFloat("lightBleedReduction", shadowInfo.getLightBleedReduction());
				if (shadowInfo.getFlipFaces())
					glCullFace(GL_FRONT);
				
				temp = mainCamera;
				mainCamera  = altCamera;
				scene.renderScene(shadowMapShader, this);
				mainCamera = temp;
				
				if (shadowInfo.getFlipFaces())
					glCullFace(GL_BACK);
				
				// Soften the shadow
				if (shadowInfo.getShadowSoftness() != 0)
					blurShadowMap(shadowMapIndex, shadowInfo.getShadowSoftness());
			}
			else {
				// Don't cast shadows.
				lightMatrix = Matrix4f.createScale(0, 0, 0);
				setFloat("minVariance", 0.00002f);
				setFloat("lightBleedReduction", 0.0f);
			}

			// Render Light
			if (renderTarget == null)
				Window.bindAsRenderTarget(viewPortX, viewPortY, viewPortWidth, viewPortHeight);
			else
				renderTarget.bindAsRenderTarget();
			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE);
			glDepthMask(false);
			glDepthFunc(GL_EQUAL);
			
			scene.renderScene(light.getShader(), this);
			
			glDepthFunc(GL_LESS);
			glDepthMask(true);
			glDisable(GL_BLEND);
		}
	}
}
