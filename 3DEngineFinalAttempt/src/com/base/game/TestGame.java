package com.base.game;

import java.util.ArrayList;
import com.base.engine.common.Matrix4f;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.AbstractGame;
import com.base.engine.core.Keyboard;
import com.base.engine.core.Time;
import com.base.engine.core.Transform;
import com.base.engine.core.Util;
import com.base.engine.entity.CameraController;
import com.base.engine.entity.EntityObject;
import com.base.engine.entity.LookControllerOLD;
import com.base.engine.entity.MeshRenderer;
import com.base.engine.entity.Model;
import com.base.engine.entity.MoveControllerOLD;
import com.base.engine.entity.lights.BaseLight;
import com.base.engine.entity.lights.DirectionalLight;
import com.base.engine.entity.lights.PointLight;
import com.base.engine.entity.lights.SpotLight;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.Brush;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.DynamicMesh;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Shader;
import com.base.engine.rendering.Terrain;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Wave;
import com.base.engine.rendering.Window;
import com.base.engine.rendering.resourceManagement.OBJModel;
import com.base.engine.rendering.resourceManagement.OBJModelLoader;
import com.base.engine.rendering.resourceManagement.ResourceManager;
import com.base.engine.rendering.resourceManagement.TGALoader;

import static org.lwjgl.opengl.GL11.*;

public class TestGame extends AbstractGame {
	private Camera camera;
	private Camera altCamera;
		
	private EntityObject cameraObject;
	
	private Material material;
	private Material matBricks;
	private Material wood;
	
	private Brush brush;
	
	private EntityObject obj1;
	private EntityObject obj2;
	private PointLight playerLight;
	private SpotLight spotLight1;
	private DirectionalLight dirLight;
	
	private float temp = 0.0f;
	
	private MeshRenderer waterObject;
	private Shader waterShader;
	private ArrayList<Wave> waves;
	
	private Camera cameraXZ;
	private Camera cameraXY;
	private Camera cameraZY;
	
//	private DirectionalLight createSunlight(Vector3f lightColor, float lightIntensity,
//								Vector3f ambientColor, float ambientIntensity,
//								Vector3f sunPositionInSkyBox)
//	{
//		DirectionalLight light = new DirectionalLight(lightColor,lightIntensity, 40);
//		EntityObject obj = new EntityObject().addComponent(light);
//		getEngine().getRenderingEngine().setAmbientLight(ambientColor.times(ambientIntensity));
//		obj.getTransform().setDirection(sunPositionInSkyBox.inverse());
//		world.addChild(obj);
//		return light;
//	}
	
	@Override
	public void init() {
		camera = new Camera((float) Math.toRadians(60.0f), Window.getAspectRatio(), 0.01f, 1000.0f);
		engine.getRenderingEngine().setMainCamera(camera);
		
		// Create camera controller.
		cameraObject = new EntityObject()
        		.addComponent(new LookControllerOLD(0.2f))
        		.addComponent(new MoveControllerOLD(10.0f));
//        		.addComponent(camera);
//		world.addChild(cameraObject);
		camera.getTransform().setPosition(0.29f, 7.21f, -15.66f);
		camera.getTransform().setRotation(-0.14f, -0.04f, 0.01f, -0.99f);

		cameraXZ = new Camera(Matrix4f.createOrthographic(-15, 15, -15, 15, 0.1f, 1000).times(
				Matrix4f.createRotation(new Vector3f(0, -1, 0), new Vector3f(1, 0, 0))));
		cameraXY = new Camera(Matrix4f.createOrthographic(-15, 15, -15, 15, 0.1f, 1000).times(
				Matrix4f.createRotation(new Vector3f(1, 0, 0), new Vector3f(0, 1, 0))));
		cameraZY = new Camera(Matrix4f.createOrthographic(-15, 15, -15, 15, 0.1f, 1000).times(
				Matrix4f.createRotation(new Vector3f(0, 0, 1), new Vector3f(0, 1, 0))));
//		new EntityObject().addComponent(cameraXZ);
//		new EntityObject().addComponent(cameraXY);
//		new EntityObject().addComponent(cameraZY);
		
		
		altCamera = new Camera((float) Math.toRadians(60.0f), Window.getAspectRatio(), 0.01f, 1000.0f);
//		new EntityObject().addComponent(altCamera);
		
		CameraController camControl = new CameraController(4, 0.1f);
		camControl.addAttatchment(camera);
		world.addChild(camControl);
		
//		camera.setProjection(Matrix4f.createOrthographic(-20, 20, -20, 20, -20, 20));
		
		// Create meshes and materials.
		material = new Material();
		material.setTexture(new Texture("bricks.jpg"));
		material.setTexture(new Texture("grass1.jpg"));
//		material.setTexture(new Texture("water1.png"));
//		material.setNormalMap(new Texture("water1_normal.jpg"));
//		material.setTexture(new Texture("white.jpg"));
//		material.setNormalMap(new Texture("bricks_normal.jpg"));
//		material.setDispMap(new Texture("bricks_disp.jpg"), 0.03f, -0.7f);
		material.setSpecular(0.5f, 4.0f);
//		material.setReflectivity(0.5f);
		material.setSpecular(1, 8);
		
		matBricks = new Material();
		matBricks.setTexture(new Texture("bricks2.jpg"));
		matBricks.setNormalMap(new Texture("bricks2_normal_smooth.jpg"));
		matBricks.setDispMap(new Texture("bricks2_disp.png"), 0.03f, -0.7f);
		matBricks.setSpecular(1.0f, 8.0f);

		Material reflectMat = new Material();
		reflectMat.setTexture(new Texture("white.jpg"));
		reflectMat.setTexture(new Texture("models/couch.jpg"));
		reflectMat.setSpecular(0.5f, 4f);
//		reflectMat.setSpecular(2.0f, 16.0f);
//		reflectMat.setReflectivity(1.0f);

		wood = new Material();
		wood.setTexture(new Texture("wood.jpg"));
		wood.setNormalMap(new Texture("wood_normal.jpg"));
		wood.setSpecular(0.5f, 4.0f);

//		TGALoader.loadImage("./res/textures/Lara_torso_D.tga");
		
		Material testMat = new Material();
		testMat.setTexture(new Texture("white.jpg"));
//		testMat.setTexture(new Texture("Lara_torso_D.tga"));
		testMat.setSpecular(1, 8);
//		testMat.setReflectivity(1.0f);

		Material tigerSkin = new Material();
		tigerSkin.setTexture(new Texture("models/tiger.jpg"));
		tigerSkin.setSpecular(1, 8);
		

		Material devMat = new Material();
		devMat.setTexture(new Texture("dev512.jpg"));
		devMat.setSpecular(1, 8);
		

		waterObject = new MeshRenderer(TestModels.createDisplacementMesh(400, 400, 0.7f), material);
		waterShader = new Shader("water");
//		new EntityObject().addComponent(waterObject);
		waves = new ArrayList<Wave>();
		waves.add(new Wave(2.0f, 12, 2.7f, new Vector2f(1, 0)));
		waves.add(new Wave(0.1f, 4, 3.5f, new Vector2f(1, -0.5f)));
		waves.add(new Wave(0.4f, 8, 1.2f, new Vector2f(0, 1)));
		waves.add(new Wave(0.7f, 14, 2.7f, new Vector2f(-1, 0.7f)));
		waves.add(new Wave(0.2f, 3, 0.7f, new Vector2f(-1, -1)));
		
//		waves.clear();
//
//		waves.add(new Wave(1.2f, 8, 0.5f, new Vector2f(1, 0)));
//		waves.add(new Wave(0, 1, 1, new Vector2f(1, 1)));
//		waves.add(new Wave(0, 1, 1, new Vector2f(1, 1)));
		
		// Sunlight and Skybox
		
		/*
		dirLight = createSunlight(
				new Vector3f(1.00f, 1.00f, 1.0f), 1.0f,
//				new Vector3f(1.00f, 1.00f, 0.60f), 0.9f,
//				new Vector3f(0.54f, 0.65f, 0.93f), 0.2f,
				new Vector3f(0.30f, 0.70f, 1.00f), 0.5f,
//				new Vector3f(1, 1, 1), 1.0f,
				new Vector3f(1.0f, 0.5f, -0.3f));
				*/
		engine.getRenderingEngine().setSkyBox(new Texture("sky4.png", true));
		
		EntityObject tempObj;
		
		dirLight = new DirectionalLight(new Vector3f(1, 1, 1),0.8f, 40);
		world.addChild(dirLight);
		
		// Point light
		playerLight = new PointLight(new Vector3f(1, 1, 1), 0.8f, new Attenuation(0,0,0.1f));
//		world.addChild(new EntityObject().addComponent(playerLight));
//		playerLight.setPosition(new Vector3f(-2,0,5f));
		
		// Spot light
		/*
		spotLight1 = new SpotLight(new Vector3f(1, 1, 1), 0.8f, new Attenuation(0,0,0.05f), (float) Math.toRadians(80), 10);
		tempObj = new EntityObject().addComponent(spotLight1);
		world.addChild(tempObj);
		spotLight1.getTransform().setPosition(-1.56f, 1.59f, 0.21f);
		spotLight1.getTransform().setRotation(0.16f, -0.37f, 0.84f, -0.37f);
		spotLight1.getTransform().setPosition(100, 100, 100);
		*/
		
		// Room
		obj1 = new EntityObject();
//		obj1.addComponent(new MeshRenderer(TestModels.createBoxMesh(), material));
//		obj1.addComponent(new MeshRenderer(Terrain.loadTerrian("heightmap1.jpg"), material));
		obj1.getTransform().setScale(9, 0.1f, 9);
		obj1.getTransform().setPosition(0, -4, 0);
//		world.addChild(obj1);
		obj1.getTransform().setScale(160, 20, 160);

		// Tiger
		obj2 = new EntityObject();
//		obj2.addComponent(new MeshRenderer(OBJModelLoader.loadModel("tiger.obj"), tigerSkin));
		obj2.getTransform().setScale(0.002f);
		obj2.getTransform().setPosition(4, 2, -4);
//		obj2.getTransform().rotate(new Vector3f(1, 0, 0), (float) Math.toRadians(-90));
//		world.addChild(obj2);
		
		// Floor
		brush = new Brush();
//		tempObj = new EntityObject()
//			.addComponent(new MeshRenderer(brush.getMesh(), devMat));
//		world.addChild(tempObj);
//		tempObj.getTransform().setScale(10, 0.2f, 10);
//		tempObj.getTransform().setScale(10, 0.1f, 10);
//		tempObj.getTransform().setPosition(0, -0.1f, 0);

		
		// Test Model
		ResourceManager.TEXTURE_DIRECTORY = "textures/models/";
		Model tempModel = new Model("Lara_Croft_v1.obj");
		ResourceManager.TEXTURE_DIRECTORY = "textures/";
//		tempObj = new EntityObject().addComponent(new Model("liz.obj"));
		world.addChild(tempModel);
		tempModel.getTransform().setScale(2.2f);
		tempModel.getTransform().setPosition(9, 0, 9);
//		tempObj.getTransform().rotate(new Vector3f(1, 0, 0), (float) Math.toRadians(-90));
		tempModel.getTransform().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(-140));
		
		
		
		// Chair
//		tempObj = new EntityObject()
//		.addComponent(new MeshRenderer(OBJModelLoader.loadModel("chair2.obj"), wood));
//		world.addChild(tempObj);
//		tempObj.getTransform().setScale(3.2f);
//		tempObj.getTransform().setPosition(3, 0, 3);
//		tempObj.getTransform().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(160));
		
		// Couch
		/*
		tempObj = new EntityObject()
//		.addComponent(new MeshRenderer(OBJModelLoader.loadModel("couch.obj"), reflectMat));
		.addComponent(new Model("couch.obj"));
		world.addChild(tempObj);
		tempObj.getTransform().setScale(1.8f);
		tempObj.getTransform().setPosition(-5, 0, -2);
		tempObj.getTransform().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(100));
		*/
		
		obj2.getTransform().setScale(0.001f);
		obj2.getTransform().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(180));
	}
	
	@Override
	public void input(float delta) {
		super.input(delta);
		
		if (Keyboard.isKeyPressed(Keyboard.KEY_RIGHT)) {
			float a = 10;
			brush.getVertexPosition(0).y -= a;
			brush.getVertexPosition(1).y -= a;
			brush.getVertexPosition(2).y -= a;
			brush.getVertexPosition(3).y -= a;
			brush.recalculate();
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
			matBricks.setReflectivity(matBricks.getReflectivity() + 0.02f);
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
			matBricks.setReflectivity(matBricks.getReflectivity() - 0.02f);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			dirLight.getTransform().setRotation(camera.getTransform().getRotation());
//			spotLight1.getTransform().setPosition(cameraObject.getTransform().getPosition());
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			dirLight.getTransform().setRotation(cameraObject.getTransform().getRotation());
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_BACKSPACE)) {
			dirLight.getTransform().rotate(new Vector3f(0, 0, 1), 0.007f);
		}

		if (Keyboard.isKeyPressed(Keyboard.KEY_ENTER)) {
			Vector3f pos = spotLight1.getTransform().getPosition();
			Quaternion rot = spotLight1.getTransform().getRotation();
			System.out.printf("Pos: (%.2ff, %.2ff, %.2ff)\n", pos.x, pos.y, pos.z);
			System.out.printf("Rot: (%.2ff, %.2ff, %.2ff, %.2ff)\n", rot.getX(), rot.getY(), rot.getZ(), rot.getW());
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
			Window.setFullScreen(!Window.isFullScreen());
		}
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		temp += delta;
		float sinTemp = (float)  Math.sin(temp * 3);
		
		obj2.getTransform().setScale(0.002f);
//		obj2.getTransform().setScale(3f);
//		obj2.getTransform().setPosition(0, 2, 0);
		
//		spotLight1.getTransform().setRotation(cameraObject.getTransform().getRotation());
//		spotLight1.getTransform().setPosition(cameraObject.getTransform().getPosition());
		
		// Rotate the sunlight
//		dirLight.getTransform().rotate(new Vector3f(0, 1, 0), -0.007f);

//		dirLight.setDirection(new Vector3f(1.0f, -0.3f, 0.5f).negate());
//		obj2.getTransform().setRotation(dirLight.getTransform().getRotation());

//		dirLight.getTransform().setDirection(camera.getTransform().getPosition().inverse());
//		obj2.getTransform().setRotation(dirLight.getTransform().getRotation());
		
		// Create pulsing effect for the spot light
//		spotLight1.setViewAngle((float) Math.toRadians(90 + (sinTemp * 10)));

		// Rotate the cube
//		obj2.getTransform().rotate(new Vector3f(0, 1, 0), 0.01f);
		
		// Make light come from the player.
//		playerLight.setPosition(camera.getTransform().getPosition());
	}
	
	@Override
	public void render(RenderingEngine renderingEngine) {
		super.render(renderingEngine);
		if (true)
			return;
//		renderingEngine.render(world, Input.getKey(Input.KEY_HOME), true);
		

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
//		glDisable(GL_CULL_FACE);
		
		waterShader.setUniformWave("g_wave", waves.get(0));
//		waterShader.setUniformWave("g_wave2", waves.get(1));
//		waterShader.setUniformWave("g_wave3", waves.get(2));
		waterObject.render(waterShader, renderingEngine);
		
		/*
		int w = Window.getWidth();
		int h = Window.getHeight();
		Vector3f cam = camera.getTransform().getPosition();
		
		
		glEnable(GL_SCISSOR_TEST);

		cameraXZ.getTransform().setPosition(cam.x, 50, cam.z);
		renderingEngine.setMainCamera(cameraXZ);
		
		renderingEngine.setViewPort(0, 0, w / 2, h / 2);
		glScissor(0, 0, w / 2, h / 2);
		glMatrixMode(GL_MODELVIEW_MATRIX);
		glLoadIdentity();
		glOrtho(0, w / 2, h / 2, 0, -1, 1);
		renderingEngine.render(world);

		cameraXY.getTransform().setPosition(cam.x, cam.y, -5);
		renderingEngine.setMainCamera(cameraXY);
		
		renderingEngine.setViewPort(w / 2, 0, w / 2, h / 2);
		glScissor(w / 2, 0, w / 2, h / 2);
		glMatrixMode(GL_MODELVIEW_MATRIX);
		glLoadIdentity();
		glOrtho(0, w / 2, h / 2, 0, -1, 1);
		glTranslatef(w / 2, 0, 0);
		renderingEngine.render(world);
		
		cameraZY.getTransform().setPosition(-5, cam.y, cam.z);
		renderingEngine.setMainCamera(cameraZY);

		renderingEngine.setViewPort(0, h / 2, w / 2, h / 2);
		glScissor(0, h / 2, w / 2, h / 2);
		glMatrixMode(GL_MODELVIEW_MATRIX);
		glLoadIdentity();
		glOrtho(0, w / 2, h / 2, 0, -1, 1);
		glTranslatef(0, h / 2, 0);
		renderingEngine.render(world);

		renderingEngine.setMainCamera(camera);
		
		renderingEngine.setViewPort(w / 2, h / 2, w / 2, h / 2);
		glScissor(w / 2, h / 2, w / 2, h / 2);
		glMatrixMode(GL_MODELVIEW_MATRIX);
		glLoadIdentity();
		glOrtho(0, w / 2, h / 2, 0, -1, 1);
		glTranslatef(0, h / 2, 0);
		renderingEngine.render(world);
		*/
		
		/*

		renderingEngine.setViewPort(160, 0, 160, 240);
		glViewport(160, 0, 160, 240);
		glScissor(160, 0, 160, 240);
		glMatrixMode(GL_MODELVIEW_MATRIX);
		glLoadIdentity();
		glOrtho(0, 160, 240, 0, -1, 1);
		glTranslatef(160, 0, 0);
		renderingEngine.render(world);
		
		renderingEngine.setViewPort(160, 0, 160, 240);
		glViewport(160, 0, 160, 240);
		glScissor(160, 0, 160, 240);
		glMatrixMode(GL_MODELVIEW_MATRIX);
		glLoadIdentity();
		glOrtho(0, 160, 240, 0, -1, 1);
		glTranslatef(160, 0, 0);
		renderingEngine.render(world);*/
		/*
		// Top left
		renderingEngine.setViewPort(0, h, w / 2, h / 2);
		glScissor(0, h, w / 2, h / 2);
		renderingEngine.render(world);
		
		// Bottom left
		renderingEngine.setViewPort(0, 0, w / 2, h / 2);
		glScissor(0, h, w / 2, h / 2);
		renderingEngine.render(world);
		
		// Top right
		renderingEngine.setViewPort(w / 2, h, w / 2, h / 2);
		glScissor(w / 2, h, w / 2, h / 2);
		renderingEngine.render(world);
		
		// Bottom right
		renderingEngine.setViewPort(w / 2, 0, w / 2, h / 2);
		glScissor(w / 2, 0, w / 2, h / 2);
		renderingEngine.render(world);
		*/
	}
}
