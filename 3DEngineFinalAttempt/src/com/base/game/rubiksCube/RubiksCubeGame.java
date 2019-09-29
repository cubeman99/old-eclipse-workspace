package com.base.game.rubiksCube;

import java.util.ArrayList;
import com.base.engine.common.GMath;
import com.base.engine.common.Point;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.AbstractGame;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.Keyboard;
import com.base.engine.core.Mouse;
import com.base.engine.core.Util;
import com.base.engine.entity.CameraController;
import com.base.engine.entity.Model;
import com.base.engine.entity.lights.BaseLight;
import com.base.engine.entity.lights.DirectionalLight;
import com.base.engine.entity.lights.SpotLight;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Font;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Primitives;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Window;
import com.base.engine.rendering.resourceManagement.ResourceManager;
import com.base.game.TestModels;
import com.base.game.rubiksCube.RubiksCube;
import com.base.game.wolf3d.Wolf3D;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class RubiksCubeGame extends AbstractGame {
	private Camera camera;
	private BaseLight dirLight;
	private RubiksCube rubiksCube;
	private float timer;
	private float viewDistance;
	private Cubie testCubie;
	
	
	@Override
	public void init() {
		Texture.DEFAULT_FILTERING = GL_NEAREST;
		Draw2D.setFont(new Font("font_console.png", 8, 12, 16));
		Texture.DEFAULT_FILTERING = GL_LINEAR;
		
		dirLight = new DirectionalLight(new Vector3f(1.0f), 1.0f, 20);
//		dirLight.getTransform().rotate(Vector3f.X_AXIS, GMath.HALF_PI);
		dirLight.getTransform().rotate(Vector3f.X_AXIS, GMath.QUARTER_PI);
		dirLight.getTransform().rotate(Vector3f.Y_AXIS, -GMath.QUARTER_PI);
		world.addChild(dirLight);
		
		ResourceManager.RESOURCE_DIRECTORY = "res/jets/";
		
		camera = new Camera(GMath.toRadians(60), Window.getAspectRatio(), 0.01f, 1000.0f);
		getEngine().getRenderingEngine().setMainCamera(camera);
		getEngine().getRenderingEngine().setAmbientLight(new Vector3f(0.25f));
		
//		camera.getTransform().setRotation(dirLight.getTransform().getRotation());
		camera.getTransform().setPosition(4, 4, -4);
		camera.getTransform().setPosition(4, 0, -4);
//		camera.getTransform().setPosition(0, 0, -4);
//		camera.getTransform().rotate(camera.getTransform().getRotation().getUp(), -GMath.QUARTER_PI);
//		camera.getTransform().rotate(camera.getTransform().getRotation().getRight(), GMath.QUARTER_PI);
//		camera.getTransform().rotate(new Vector3f(-1, 1, 1), -GMath.QUARTER_PI);
		
		viewDistance = 4;

		camera.getTransform().getRotation().setDirection(new Vector3f(-1, -1, 1), Vector3f.Y_AXIS);
		
		// Create Rubik's Cube
		rubiksCube = new RubiksCube(3, 3, 3);
		rubiksCube.getTransform().setScale(0.5f);
		world.addChild(rubiksCube);
		
		world.addChild(new RubiksCubeController(rubiksCube));

		Material matTable = new Material();
//		matTable.setTexture(new Texture("12_DIFFUSE.jpg"));
//		matTable.setTexture(new Texture("white.jpg"));
//		matTable.setNormalMap(new Texture("12_NORMAL.jpg"));
//		matTable.setDispMap(new Texture("12_DISP.jpg"), 0.03f, -0.2f);
		matTable.setTexture(new Texture("marble.png"));
//		matTable.setNormalMap(new Texture("wood_normal.jpg"));
		world.addChild(new Model(Primitives.createXZPlane(-4, 4, -4, 4, -1.2f, false), matTable));
		
		Vector3f viewPos = new Vector3f(1, 1, -1);
		camera.getTransform().setPosition(viewPos.normalized().times(viewDistance));
		camera.getTransform().getRotation().setDirection(viewPos.inverse(), Vector3f.Y_AXIS);
		
		testCubie = rubiksCube.getCubie(1, 3, 1);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		timer += delta;

		float zoomSpeed = 0.3f;
		if (Mouse.onWheelUp())
			viewDistance -= zoomSpeed;
		if (Mouse.onWheelDown())
			viewDistance += zoomSpeed;
		viewDistance = Math.max(1, viewDistance);
		camera.getTransform().getPosition().setLength(viewDistance);
		
//		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
//    		camera.getTransform().setPosition(camera.getTransform().getPosition().rotate(1 * delta, Vector3f.Y_AXIS));
//		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
//    		camera.getTransform().setPosition(camera.getTransform().getPosition().rotate(-1 * delta, Vector3f.Y_AXIS));
		
		if (Mouse.left.down()) {
    		Vector2f mouseVel = Mouse.getPosition().minus(Mouse.getPositionLast());
    		camera.getTransform().setPosition(camera.getTransform().getPosition().rotate(mouseVel.x * 0.1f * delta, Vector3f.Y_AXIS));
		}
		
		camera.getTransform().getRotation().setDirection(camera.getTransform().getPosition().inverse(), Vector3f.Y_AXIS);
	}
	
	public void setup2D() {
		glActiveTexture(GL_TEXTURE0);
    	glUseProgram(0);
    	glEnable(GL_TEXTURE_2D);
    	glBindTexture(GL_TEXTURE_2D, 0);
    	glDepthMask(false);
    	glDisable(GL_CULL_FACE);
    	glDisable(GL_DEPTH_TEST);
    	glDisable(GL_DEPTH_CLAMP);
    	glEnable(GL_BLEND);
    	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    	
    	glMatrixMode(GL_PROJECTION);
    	glLoadIdentity();
    	glOrtho(0, Window.getWidth(), Window.getHeight(), 0, -1, 1);
    	glMatrixMode(GL_MODELVIEW);
    	glLoadIdentity();
    	
		Draw2D.setColor(Draw2D.WHITE);
	}
	
	public void setup3D() {
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glBindTexture(GL_TEXTURE_2D, 0);
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
	}
	
	@Override
	public void render(RenderingEngine renderingEngine) {
		super.render(renderingEngine);

		setup2D();
		
		Draw2D.setColor(Draw2D.GREEN);
		Move move = rubiksCube.getMove();
		if (move != null && rubiksCube.isTurning())
			Draw2D.drawText(new Notation().getNotation(move), 16, 16, 16);

		Draw2D.drawText(rubiksCube.isSolved() + "", 16, 48, 16);
		
		setup3D();
	}
	
	public static void main(String[] args) {
		CoreEngine engine = new CoreEngine(500, 500, 60, new RubiksCubeGame());
		engine.createWindow("Rubik's Cube");
		engine.start();
	}
}
