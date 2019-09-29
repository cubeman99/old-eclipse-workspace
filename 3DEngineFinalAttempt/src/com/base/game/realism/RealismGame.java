package com.base.game.realism;

import com.base.engine.common.GMath;
import com.base.engine.common.Vector3f;
import com.base.engine.core.AbstractGame;
import com.base.engine.core.CoreEngine;
import com.base.engine.entity.CameraController;
import com.base.engine.entity.Model;
import com.base.engine.entity.lights.DirectionalLight;
import com.base.engine.entity.lights.PointLight;
import com.base.engine.entity.lights.SpotLight;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Primitives;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Window;
import com.base.engine.rendering.resourceManagement.OBJModel;
import com.base.engine.rendering.resourceManagement.ResourceManager;
import com.base.game.TestModels;

public class RealismGame extends AbstractGame {
	private Camera camera;
	private DirectionalLight sun;
	private SpotLight spotLight;
	private PointLight pointLight;
	private Material matFloor;
	private Material matWall;
	private Material matCeiling;

	private Model modelSecondHand;
	private Model modelMinuteHand;
	private Model modelHourHand;
	
	
	@Override
	public void init() {
		sun = new DirectionalLight(new Vector3f(1.0f, 0.96f, 0.9f), 1.0f, 20);
		sun.getTransform().getRotation().setDirection(new Vector3f(-1, -1, 1), Vector3f.Y_AXIS);
//		world.addChild(sun);

//		spotLight = new SpotLight(new Vector3f(1.0f, 0.96f, 0.9f), 1.0f, new Attenuation(0, 0, 0.05f), 2.0f, 10);
//		spotLight.getTransform().getRotation().setDirection(new Vector3f(0, -1, 0), Vector3f.Y_AXIS);
//		spotLight.getTransform().setPosition(0, 7, 0);
//		world.addChild(spotLight);
		
//		pointLight = new PointLight(new Vector3f(1.0f, 0.93f, 0.8f), 1.0f, new Attenuation(1, 0, 0.04f));
//		pointLight.getTransform().setPosition(0, 7, 0);
//		world.addChild(pointLight);
		
		ResourceManager.RESOURCE_DIRECTORY = "res/realism/";
		
		camera = new Camera(GMath.toRadians(60), Window.getAspectRatio(), 0.01f, 1000.0f);
		getEngine().getRenderingEngine().setMainCamera(camera);
		getEngine().getRenderingEngine().setAmbientLight(new Vector3f(0.25f));
		camera.addAttatchment(new CameraController(4, 0.1f));
		world.addChild(camera);
		
		camera.getTransform().setPosition(0, 5, -6);
		camera.getTransform().getRotation().setDirection(camera.getTransform().getPosition().inverse(), Vector3f.Y_AXIS);
		
		
		matFloor = new Material();
		matFloor.setTexture(new Texture("carpet.jpg"));
//		matFloor.setTexture(new Texture("white.jpg"));
		matFloor.setNormalMap(new Texture("carpet_normal.jpg"));
//		matFloor.setTexture(new Texture("wood_floor.jpg"));
//		matFloor.setNormalMap(new Texture("wood_floor_normal.jpg"));
//		matFloor.setSpecularMap(new Texture("footprints_spec.jpg"), 1, 16);
		matFloor.setSpecular(0.25f, 4);
		
		matWall = new Material();
		matWall.setTexture(new Texture("plaster_blue.jpg"));
		matWall.setSpecular(1, 8);
		
		matCeiling = new Material();
		matCeiling.setTexture(new Texture("plaster.jpg"));
		matCeiling.setSpecular(0.4f, 4);
		
		Material matClock = new Material();
		matClock.setTexture(new Texture("clock_base.jpg"));
		Model modelClock = new Model(Primitives.createCircle(0, 0, 1.0f, 20, false), matClock);
		modelClock.getTransform().setPosition(0, 0.1f, 0);
//		world.addChild(modelClock);

		OBJModel objTable = new OBJModel("Table1.obj");
		objTable.calcNormals(false);
		world.addChild(new Model(objTable));
		
		ResourceManager.RESOURCE_DIRECTORY = "res/";
		createRoom(-4, 0, -4, 8, 8, 8);
	}
	
	public void createRoom(float x, float y, float z, float width, float height, float length) {
		world.addChild(new Model(Primitives.createXZPlane(x, x + width, z, z + length, y, false), matFloor));
		world.addChild(new Model(Primitives.createXZPlane(x, x + width, z, z + length, y + height, true), matCeiling));
		
		world.addChild(new Model(Primitives.createXYPlane(x, x + width, y, y + height, z, true), matWall));
		world.addChild(new Model(Primitives.createXYPlane(x, x + width, y, y + height, z + length, false), matWall));
		world.addChild(new Model(Primitives.createZYPlane(z, z + length, y, y + height, x, false), matWall));
		world.addChild(new Model(Primitives.createZYPlane(z, z + length, y, y + height, x + width, true), matWall));
		
		PointLight pointLight = new PointLight(new Vector3f(1.0f, 0.93f, 0.8f), 1.0f, new Attenuation(1, 0, 0.04f));
		pointLight = new PointLight(new Vector3f(1.0f, 0.93f, 0.8f), 1.0f, new Attenuation(3.0f, 0, 0.02f));
		pointLight.getTransform().setPosition(x + width / 2, y + (height * 0.75f), z + length / 2);
		world.addChild(pointLight);

		SpotLight spotLight = new SpotLight(new Vector3f(1.0f, 0.96f, 0.9f), 1.0f, new Attenuation(0, 0, 0.05f), 2.5f, 10);
		spotLight.getTransform().getRotation().setDirection(new Vector3f(0, -1, 0), Vector3f.Y_AXIS);
		spotLight.getTransform().setPosition(x + width / 2, y + (height * 0.75f), z + length / 2);
		world.addChild(spotLight);

		world.addChild(new Model(Primitives.createSphere(0, height * 0.3f, 0, 0.5f, 24, 12), matWall));
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
//		sun.getTransform().rotate(Vector3f.Y_AXIS, 0.03f);
	}
	
	@Override
	public void render(RenderingEngine renderingEngine) {
		super.render(renderingEngine);
	}
	
	public static void main(String[] args) {
		CoreEngine engine = new CoreEngine(800, 600, 60, new RealismGame());
		engine.createWindow("3D Realism Work");
		engine.start();
	}
}
