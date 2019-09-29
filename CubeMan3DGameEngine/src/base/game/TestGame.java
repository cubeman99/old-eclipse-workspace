package base.game;

import OLD.BasicShader;
import base.engine.common.Matrix4f;
import base.engine.common.Quaternion;
import base.engine.common.ResourceLoader;
import base.engine.common.Vector;
import base.engine.common.Vector3;
import base.engine.core.Game;
import base.engine.core.Input;
import base.engine.core.Transform;
import base.engine.entity.Camera;
import base.engine.entity.Entity;
import base.engine.entity.Model;
import base.engine.entity.light.BaseLight;
import base.engine.entity.light.DirectionalLight;
import base.engine.entity.light.PointLight;
import base.engine.entity.light.SpotLight;
import base.engine.rendering.Attenuation;
import base.engine.rendering.Material;
import base.engine.rendering.Mesh;
import base.engine.rendering.RenderingEngine;
import base.engine.rendering.Texture;
import base.engine.rendering.Vertex;
import base.engine.rendering.Window;

public class TestGame extends Game {
	private static final Vector3 yAxis = new Vector3(0,1,0);

	private boolean mouseLocked;
	private float sensitivity;
	
	private Transform transform;
	private Camera camera;
	
	private Entity model;
	private Entity model2;
	private SpotLight spotLight;
	private PointLight pointLight;
	
    @Override
    public void initialize() {
    	transform   = new Transform();
    	sensitivity = 0.1f;
    	mouseLocked = false;
    	camera      = new Camera(70, Window.getWidth() / Window.getHeight(), 0.1f, 1000);
    	
    	Mesh mesh    = createInvertedBoxMesh();
    	Material mat = new Material();
    	mat.setTexture(new Texture("bricks2.jpg"));
    	mat.setNormalMap(new Texture("bricks2_normal.jpg"));
    	mat.setColor(new Vector3(1, 1, 1));
    	mat.setSpecularIntensity(0.1f);
    	mat.setSpecularPower(2);
    	
    	model = new Entity().addComponent(new Model(mesh, mat));
    	model2 = new Entity().addComponent(new Model(mesh, mat));
    	world.addEntity(model);
    	
//    	Entity e1 = new Entity().addComponent(new Model(mesh, mat));
//    	world.addEntity(e1);
//    	e1.getTransform().setTranslation(1, 3, 2);
//    	
//    	Entity e2 = new Entity().addComponent(new Model(mesh, mat));
//    	world.addEntity(e2);
//    	e2.getTransform().setTranslation(-1, 0, 4);
//    	
    	engine.getRenderingEngine().setMainCamera(camera);
    	
    	pointLight = new PointLight(new Vector3(1, 1, 1), 0.8f, new Attenuation(0, 0, 0.1f), 30);
    	new Entity().addComponent(pointLight);
    	engine.getRenderingEngine().addLight(pointLight);
    	pointLight.getTransform().setTranslation(0, 0, 0);
    	
//    	DirectionalLight dl = new DirectionalLight(new Vector3(1, 1, 1), 1.0f);
//    	Entity e = new Entity().addComponent(dl);
//    	world.addEntity(e);
    	
    	
    	
    	
//		e.setWorld(world);
//		e.onEnterWorld();
		
//    	pl.getTransform().setTranslation(0, 5, 0);
//    	world.addEntity(new Entity().addComponent(dl));
//    	dl.getTransform().setRotation(new Quaternion(Matrix4f.createRotation(1, 1, 1)));
    	
//    	PhongShader.setAmbientLight(new Vector3(0.2f,0.2f,0.2f));
//		PhongShader.setDirectionalLight(new DirectionalLight(new BaseLight(new Vector3(1,1,1), 0.8f), new Vector3(1,1,1)));
//		pointLight = new PointLight(new Vector3(1, 1, 1), 0.8f, new Attenuation(0, 0, 0.1f), 30);
//		spotLight  = new SpotLight(new Vector3(1, 1, 1), 0.8f, new Attenuation(0, 0, 0.1f), 30, 0.3f);
//		PhongShader.setPointLight(new PointLight[] {pointLight});
//		PhongShader.setSpotLight(new SpotLight[] {spotLight});
    }

    private void move(Vector3 dir, float amt) {
    	camera.getTransform().setTranslation(camera.getTransform().getTranslation().plus(dir.scale(amt)));
    }
    
    private Mesh createBoxMesh() {
    	Vertex[] vertices = new Vertex[] {
    		// Bottom
        	new Vertex(-1, -1, -1, 0, 0),
        	new Vertex(-1,  1, -1, 0, 1),
        	new Vertex( 1, -1, -1, 1, 0),
        	new Vertex( 1,  1, -1, 1, 1),
    		// Top
        	new Vertex(-1, -1,  1, 0, 1),
        	new Vertex(-1,  1,  1, 0, 0),
        	new Vertex( 1, -1,  1, 1, 1),
        	new Vertex( 1,  1,  1, 1, 0),
    		// Right
        	new Vertex( 1, -1, -1, 0, 0),
        	new Vertex( 1,  1, -1, 0, 1),
        	new Vertex( 1, -1,  1, 1, 0),
        	new Vertex( 1,  1,  1, 1, 1),
    		// Left
        	new Vertex(-1, -1, -1, 0, 1),
        	new Vertex(-1,  1, -1, 0, 0),
        	new Vertex(-1, -1,  1, 1, 1),
        	new Vertex(-1,  1,  1, 1, 0),
        	// Front
        	new Vertex(-1, -1, -1, 0, 0),
        	new Vertex( 1, -1, -1, 0, 1),
        	new Vertex(-1, -1,  1, 1, 0),
        	new Vertex( 1, -1,  1, 1, 1),
        	// Back
        	new Vertex(-1,  1, -1, 0, 1),
        	new Vertex( 1,  1, -1, 0, 0),
        	new Vertex(-1,  1,  1, 1, 1),
        	new Vertex( 1,  1,  1, 1, 0),
    	};
    	
    	int[] indices = new int[] {
    		// Bottom
    		0, 1, 3,
    		0, 3, 2,
    		// Top
    		4, 7, 5,
    		4, 6, 7,
    		// Right
    		8, 9, 11,
    		8, 11, 10,
    		// Left
    		12, 15, 13,
    		12, 14, 15,
    		// Front
    		16, 17, 19,
    		16, 19, 18,
    		// Back
    		20, 23, 21,
    		20, 22, 23
    	};

    	return new Mesh(vertices, indices, true, true);
    }
    
    
    private Mesh createInvertedBoxMesh() {
    	Vertex[] vertices = new Vertex[] {
    		// Bottom
        	new Vertex(-1, -1, -1, 0, 1),
        	new Vertex(-1,  1, -1, 0, 0),
        	new Vertex( 1, -1, -1, 1, 1),
        	new Vertex( 1,  1, -1, 1, 0),
    		// Top
        	new Vertex(-1, -1,  1, 0, 0),
        	new Vertex(-1,  1,  1, 0, 1),
        	new Vertex( 1, -1,  1, 1, 0),
        	new Vertex( 1,  1,  1, 1, 1),
    		// Right
        	new Vertex( 1, -1, -1, 0, 1),
        	new Vertex( 1,  1, -1, 0, 0),
        	new Vertex( 1, -1,  1, 1, 1),
        	new Vertex( 1,  1,  1, 1, 0),
    		// Left
        	new Vertex(-1, -1, -1, 0, 0),
        	new Vertex(-1,  1, -1, 0, 1),
        	new Vertex(-1, -1,  1, 1, 0),
        	new Vertex(-1,  1,  1, 1, 1),
        	// Front
        	new Vertex(-1, -1, -1, 0, 1),
        	new Vertex( 1, -1, -1, 0, 0),
        	new Vertex(-1, -1,  1, 1, 1),
        	new Vertex( 1, -1,  1, 1, 0),
        	// Back
        	new Vertex(-1,  1, -1, 0, 0),
        	new Vertex( 1,  1, -1, 0, 1),
        	new Vertex(-1,  1,  1, 1, 0),
        	new Vertex( 1,  1,  1, 1, 1),
    	};
    	
    	int[] indices = new int[] {
    		// Bottom
    		0, 3, 1,
    		0, 2, 3,
    		// Top
    		4, 5, 7,
    		4, 7, 6,
    		// Right
    		8, 11, 9,
    		8, 10, 11,
    		// Left
    		12, 13, 15,
    		12, 15, 14,
    		// Front
    		16, 19, 17,
    		16, 18, 19,
    		// Back
    		20, 21, 23,
    		20, 23, 22
    	};
    	
    	return new Mesh(vertices, indices, true, true);
    }
    
    
    @Override
	public void input(float frameTime) {
    	super.input(frameTime);
    	
    	float moveAmount   = 0.1f;
    	float rotateAmount = 0.04f;
    	
		if(Input.getKey(Input.KEY_W))
			move(camera.getTransform().getRotation().getForward(), moveAmount);
		if(Input.getKey(Input.KEY_S))
			move(camera.getTransform().getRotation().getForward(), -moveAmount);
		if(Input.getKey(Input.KEY_A))
			move(camera.getTransform().getRotation().getLeft(), moveAmount);
		if(Input.getKey(Input.KEY_D))
			move(camera.getTransform().getRotation().getRight(), moveAmount);

		if(Input.getKey(Input.KEY_UP))
			camera.getTransform().rotate(camera.getTransform().getRotation().getRight(), -rotateAmount);
		if(Input.getKey(Input.KEY_DOWN))
			camera.getTransform().rotate(camera.getTransform().getRotation().getRight(), rotateAmount);
		if(Input.getKey(Input.KEY_LEFT))
			camera.getTransform().rotate(camera.getTransform().getRotation().getUp(), -rotateAmount);
		if(Input.getKey(Input.KEY_RIGHT))
			camera.getTransform().rotate(camera.getTransform().getRotation().getUp(), rotateAmount);

		Vector centerPosition = new Vector(Window.getWidth()/2, Window.getHeight()/2);

		if(Input.getKey(Input.KEY_SPACE) || Input.getKey(Input.KEY_E))
		{
			Input.setCursor(true);
			mouseLocked = false;
		}
		if(Input.getMouseDown(0))
		{
			Input.setMousePosition(centerPosition);
			Input.setCursor(false);
			mouseLocked = true;
		}

		if(mouseLocked)
		{
			Vector deltaPos = Input.getMousePosition().minus(centerPosition);

			boolean rotY = deltaPos.x != 0;
			boolean rotX = deltaPos.y != 0;

			if(rotY)
				camera.getTransform().rotate(yAxis, (float) Math.toRadians(deltaPos.x * sensitivity));
			if(rotX)
				camera.getTransform().rotate(camera.getTransform().getRotation().getRight(), (float) Math.toRadians(-deltaPos.y * sensitivity));

			if(rotY || rotX)
				Input.setMousePosition(centerPosition);
			
			if (Input.getMouseUp(0)) {
				Input.setCursor(true);
				mouseLocked = false;
			}
		}
		
//		spotLight.getPointLight().getPosition().set(camera.getTransform().getTranslation());
//		spotLight.getDirection().set(camera.getTransform().getRotation().getForward());
	}

    float temp = 0;

    @Override
	public void update(float frameTime)
	{
    	temp += 0.1;
    	
    	
    	
//		spotLight.setCutoff(0.5f);
//		spotLight.setCutoff(0.7f + 0.1f * (float) Math.sin(temp/2));
		
//    	pointLight.getTransform().getTranslation().set(camera.getTransform().getTranslation());
//    	spotLight.getPointLight().getBaseLight().getColor().x = (float) Math.sin(temp);
//    	spotLight.getPointLight().getBaseLight().getColor().z = (float) Math.cos(temp);

//    	model2.getTransform().setTranslation(1, 0, 5);
    	model2.getTransform().setScalation(0.5f, 0.5f, 0.5f);
    	
//    	model.getTransform().setTranslation(0, 0, 5);
    	model.getTransform().rotate(new Vector3(0.0f, 0.03f, -0.0f), 0.3f);
    	model.getTransform().setScalation(4, 4, 4);
//    	model.getTransform().setScalation(0.5f, 0.5f, 0.5f);
	}

    @Override
	public void render(RenderingEngine renderingEngine) {
//    	super.render(renderingEngine);
    	
    	renderingEngine.render(model2);
    	renderingEngine.render(model);
    	
//    	shader.bind();
//    	shader.updateUniforms(camera, transform.getTransformation(), camera.getViewProjection().times(transform.getTransformation()), material);
//		mesh.draw();
	}
}
