package com.base.game.jets;

import java.util.ArrayList;
import com.base.engine.common.GMath;
import com.base.engine.common.Point;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector3f;
import com.base.engine.core.AbstractGame;
import com.base.engine.core.CoreEngine;
import com.base.engine.entity.CameraController;
import com.base.engine.entity.Model;
import com.base.engine.entity.lights.BaseLight;
import com.base.engine.entity.lights.DirectionalLight;
import com.base.engine.entity.lights.PointLight;
import com.base.engine.entity.lights.SpotLight;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Primitives;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Window;
import com.base.engine.rendering.resourceManagement.ResourceManager;
import com.base.game.TestModels;
import com.base.game.rubiksCube.RubiksCube;

public class JetGame extends AbstractGame {
	private Player player;
	private float distance;
	private ArrayList<Track> tracks;
	private Camera camera;
	
	private int mapSize;
	private boolean[][][] map;
	private Vector3f trackPosition;
	private Quaternion trackRotation;
	
	private Model modelEarth;
	private Mesh testMesh;
	
	private BaseLight dirLight;
	private PointLight pointLight;
	private SpotLight spotLight;
	
	
	public void init() {
		
//		SpotLight spotLight = new SpotLight(new Vector3f(1.0f), 1.0f, new Attenuation(0, 0, 0.1f), GMath.HALF_PI, 10);
//		spotLight.getTransform().rotate(Vector3f.X_AXIS, GMath.HALF_PI);
//		spotLight.getTransform().setPosition(0, 1.5f, 4);
//		world.addChild(spotLight);

		
		dirLight = new DirectionalLight(new Vector3f(1.0f), 1.0f, 20);
//		dirLight.getTransform().rotate(Vector3f.X_AXIS, GMath.HALF_PI);
		dirLight.getTransform().rotate(Vector3f.X_AXIS, GMath.QUARTER_PI);
		dirLight.getTransform().rotate(Vector3f.Y_AXIS, -GMath.QUARTER_PI);
//		world.addChild(dirLight);
		
		
		pointLight = new PointLight(new Vector3f(1, 1, 1), 1.0f, new Attenuation(1.5f, 0, 0.01f));
//		world.addChild(pointLight);
		
		spotLight = new SpotLight(new Vector3f(1, 1, 1), 1.0f, new Attenuation(0, 0, 0.003f), 0.9f, 10);
		world.addChild(spotLight);
		
		SpotLight spotLight2 = new SpotLight(new Vector3f(1, 1, 1), 1.0f, new Attenuation(0.5f, 0, 10), 0.9f, 10);
		spotLight2.getTransform().setDirection(new Vector3f(0, -1, -0.1f));
		spotLight2.getTransform().setPosition(0, 0.2f, 0);
		world.addLight(spotLight2);
		
		ResourceManager.RESOURCE_DIRECTORY = "res/jets/";
		
		camera = new Camera(GMath.toRadians(60), Window.getAspectRatio(), 0.01f, 1000.0f);
		getEngine().getRenderingEngine().setMainCamera(camera);
		getEngine().getRenderingEngine().setAmbientLight(new Vector3f(0.1f));
		
		camera.addAttatchment(spotLight2);
		
		player = new Player();
//		player.addAttatchment(new JetController(2.0f));
		player.addAttatchment(new JetController(528 * 0.01f)); // 1900 km/h = 528 m/s
		world.addChild(player);
		
//		Model modelJet = new Model("f14d.obj");
		Model modelJet = new Model("Mig-29_Fulcrum.obj");
		modelJet.getTransform().rotate(Vector3f.X_AXIS, -GMath.HALF_PI);
		modelJet.getTransform().rotate(Vector3f.Y_AXIS, GMath.PI);
		modelJet.getTransform().setScale(0.01f);
		player.addChild(modelJet);
		
		Material matWhite = new Material().setTexture(new Texture("bricks.jpg")).setNormalMap(new Texture("bricks_normal.jpg")).setSpecular(1, 8);
//		world.addChild(new Model(Primitives.createTorus(0, 0, 4, 0.4f, 0.2f, 20, 20), matWhite));
		Material matEarth = new Material();
		matEarth.setTexture(new Texture("earth.jpg"));
//		matEarth.setNormalMap(new Texture("earth_normal.jpg"));
		matEarth.setSpecular(1, 8);
//		world.addChild(new Model(Primitives.createSphere(0, 0.3f, 4, 0.5f, 20, 10), matEarth));
		world.addChild(new Model(Primitives.createXZPlane(-2, 2, 2, 6, -1, false), matWhite));
		
		
//		modelEarth = new Model(Primitives.createSphere(0, 0, 0, 80, 40, 80), matEarth);
//		modelEarth.getTransform().setPosition(0, 0, 160);
//		world.addChild(modelEarth);
		
		testMesh = TestModels.createBoxMesh();
		
		mapSize = 8;
		map = new boolean[mapSize][mapSize][mapSize];

		tracks        = new ArrayList<Track>();
		trackPosition = new Vector3f(3.5f);
		trackRotation = new Quaternion();
		
//		trackRotation.rotate(Vector3f.Y_AXIS, GMath.PI);
		
		generateTrack();
	}
	
	private void generateTestTrack() {
		ArrayList<Vector3f> history;
		Vector3f pos = new Vector3f(2.5f);
		
		for (int i = 0; i < 10; i++) {
			ArrayList<Vector3f> moveList = new ArrayList<Vector3f>();
			
			checkPossibleTestMove(pos.plus(0, 0, 0), moveList);
		}
	}
	
	private void checkPossibleTestMove(Vector3f pos, ArrayList<Vector3f> moveList) {
		if (checkMap(pos))
			moveList.add(pos);
	}
	
	private void generateTrack() {
		ArrayList<Vector3f> positionHistory   = new ArrayList<Vector3f>();
		ArrayList<Quaternion> rotationHistory = new ArrayList<Quaternion>();
		ArrayList<Boolean> curveHistory       = new ArrayList<Boolean>();
		
		for (int moveIndex = 0; positionHistory.size() < 160; moveIndex++) {
			
			ArrayList<Integer> moveList = new ArrayList<Integer>();
			for (int i = 0; i < 5; i++)
				checkPossibleMove(i, moveList);
			
			setMap(trackPosition, true);

			System.out.print(trackPosition + " [" + moveList.size() + "] ");
			
			if (moveList.size() == 0) {
				// Backtrack
				trackPosition.set(positionHistory.get(positionHistory.size() - 1));
				trackRotation.set(rotationHistory.get(rotationHistory.size() - 1));
				positionHistory.remove(positionHistory.size() - 1);
				rotationHistory.remove(rotationHistory.size() - 1);
				curveHistory.remove(curveHistory.size() - 1);
				System.out.println("BACK");
			}
			else {
				positionHistory.add(new Vector3f(trackPosition));
				
				int index = GMath.random.nextInt(moveList.size());
				boolean curved = (moveList.get(index) > 0);
				
				curveHistory.add(curved);
				
//				trackPosition.add(trackRotation.getForward().times(curved ? 0.5f : 1.0f));
				
				if (curved) {
					applyMove(moveList.get(index), trackRotation);
//					trackPosition.add(trackRotation.getForward().times(0.5f));
				}

				trackPosition.add(trackRotation.getForward().times(1));
				

				rotationHistory.add(new Quaternion(trackRotation));
				
				System.out.println(moveList.get(index));
				
//				createTrack(curved);
			}
		}
		
		for (int i = 0; i < positionHistory.size(); i++) {
			createTrack(positionHistory.get(i), rotationHistory.get(i), curveHistory.get(i));
		}
	}
	
	private void applyMove(int moveIndex, Quaternion rot) {
		if (moveIndex > 0) {
			if (moveIndex == 2)
				rot.rotate(rot.getForward(), GMath.HALF_PI);
			else if (moveIndex == 3)
				rot.rotate(rot.getForward(), -GMath.HALF_PI);
			else if (moveIndex == 4)
				rot.rotate(rot.getForward(), GMath.PI);
			rot.rotate(rot.getRight(), GMath.HALF_PI);
		}
	}
	
	private void checkPossibleMove(int moveIndex, ArrayList<Integer> moveList) {
		Quaternion rot = trackRotation;
		
		if (moveIndex > 0) {
			rot = new Quaternion(trackRotation);
			if (moveIndex == 2)
				rot.rotate(rot.getForward(), GMath.HALF_PI);
			else if (moveIndex == 3)
				rot.rotate(rot.getForward(), -GMath.HALF_PI);
			else if (moveIndex == 4)
				rot.rotate(rot.getForward(), GMath.PI);
			rot.rotate(rot.getRight(), GMath.HALF_PI);
		}
		
		if (checkMap(trackPosition.plus(rot.getForward())))
			moveList.add(moveIndex);
	}
	
	private void setMap(Vector3f pos, boolean state) {
		int x = (int) Math.floor(pos.x);
		int y = (int) Math.floor(pos.y);
		int z = (int) Math.floor(pos.z);
		map[x][y][z] = state;
	}
	
	private boolean checkMap(Vector3f pos) {
		int x = (int) Math.floor(pos.x);
		int y = (int) Math.floor(pos.y);
		int z = (int) Math.floor(pos.z);
		return (x >= 0 && x < mapSize && y >= 0 && y < mapSize && z >= 0 && z < mapSize && !map[x][y][z]);
	}
	
	private void createTrack(Vector3f position, Quaternion rotation, boolean curved) {
//		Model model = new Model(testMesh, new Material().setTexture(new Texture("bricks.jpg")));
//		world.addChild(model);
//		model.getTransform().setScale(0.3f);
//		model.getTransform().setPosition(position.times(1));
		
		Track track = new Track(curved);
		track.getTransform().setPosition(position.times(Track.LENGTH));
		track.getTransform().setRotation(rotation);
		tracks.add(track);
		world.addChild(track);
		
		if (curved) {
    		track.getTransform().rotate(track.getTransform().getRotation().getForward(), GMath.HALF_PI);
    		track.getTransform().rotate(track.getTransform().getRotation().getUp(), GMath.HALF_PI);
		}
	}
	
	/*
	private void createTrack(boolean rotate) {
//		Model model = new Model(testMesh, new Material().setTexture(new Texture("bricks.jpg")));
//		world.addChild(model);
//		model.getTransform().setScale(0.1f);
//		model.getTransform().setPosition(trackPosition);
//
//		trackPosition.add(trackRotation.getForward());
//		
//		if (rotate) {
//			trackRotation.rotate(trackRotation.getRight(), GMath.QUARTER_PI);
//		}
		
		
		
		// Create a new track.
		Track track = new Track(rotate);
		
		track.getTransform().setPosition(trackPosition);
		trackPosition.add(trackRotation.getForward().times(Track.LENGTH));
		
		int rand = GMath.random.nextInt(4);
		if (rotate) {
			if (rand == 1)
				trackRotation.rotate(trackRotation.getForward(), GMath.HALF_PI);
			else if (rand == 2)
				trackRotation.rotate(trackRotation.getForward(), -GMath.HALF_PI);
			else if (rand == 3)
				trackRotation.rotate(trackRotation.getForward(), GMath.PI);

			trackRotation.rotate(trackRotation.getRight(), GMath.HALF_PI);
//			trackRotation.rotate(Vector3f.X_AXIS, 0.1f);
		}
//		else if (rand == 1)
//			trackRotation.rotate(trackRotation.getForward(), GMath.HALF_PI);
		
		track.getTransform().setRotation(trackRotation);
		if (rotate)
			trackPosition.add(trackRotation.getForward().times(Track.LENGTH));
		
		if (rotate) {
    		track.getTransform().rotate(track.getTransform().getRotation().getForward(), GMath.HALF_PI);
    		track.getTransform().rotate(track.getTransform().getRotation().getUp(), GMath.HALF_PI);
		}
		
		System.out.println(trackPosition + ", " + trackRotation.getForward().normalize());
		
		tracks.add(track);
		world.addChild(track);
		
	}
	*/
	
	@Override
	public void update(float delta) {
		super.update(delta);

		float distancePrev = distance;
		distance = player.getTransform().getPosition().getZ();
		float trackLength = 10;
		
//		dirLight.getTransform().setPosition(camera.getTransform().getPosition().plus(0, 4, 0));
		
		/*
		if (distance % trackLength < distancePrev % trackLength && distance > distancePrev) {
			// Create a new track.
			Track track = new Track();
			track.getTransform().setPosition(0, 0, ((int) (distance / trackLength) + trackOffset) * trackLength);
			tracks.add(track);
			world.addChild(track);
			System.out.println("Create Track");
		}
		*/

//		pointLight.getTransform().setPosition(player.getTransform().getPosition());
		spotLight.getTransform().setPosition(player.getTransform().getPosition().plus(player.getTransform().getRotation().getForward().times(0.1f)));
		spotLight.getTransform().setRotation(player.getTransform().getRotation());
		
		spotLight.setAtten(new Attenuation(0.5f, 0, 0.005f));
		
		camera.getTransform().setRotation(camera.getTransform().getRotation()
				.nlerp(player.getTransform().getRotation(), delta * 4, true));
		
		camera.getTransform().setPosition(player.getTransform().getPosition());
		camera.getTransform().getPosition().add(camera.getTransform().getRotation().getBack().times(0.2f));
		camera.getTransform().getPosition().add(camera.getTransform().getRotation().getUp().times(0.05f));
//		camera.getTransform().setRotation(player.getTransform().getRotation());
	}
	
	
	@Override
	public void render(RenderingEngine renderingEngine) {
		super.render(renderingEngine);
		
	}
	
	public static void main(String[] args) {
		CoreEngine engine = new CoreEngine(800, 600, 60, new JetGame());
		engine.createWindow("Jets");
		engine.start();
	}
}
