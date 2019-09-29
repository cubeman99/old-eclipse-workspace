package com.base.game.editor;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.HashMap;
import com.base.engine.common.Axis;
import com.base.engine.common.Matrix4f;
import com.base.engine.common.Rect2f;
import com.base.engine.common.Rect3f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.*;
import com.base.engine.entity.*;
import com.base.engine.entity.lights.*;
import com.base.engine.rendering.*;
import com.base.engine.rendering.resourceManagement.MappedValues;
import com.base.game.editor.tools.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;


public class Editor extends AbstractGame implements SceneGraph {
	private HashMap<Integer, Vector3f> axisColors;
	private ArrayList<EntityObject> objects;
	private ArrayList<Brush> brushes;
	
	private Camera camera;
	private EntityObject cameraObject;
	private int axisX;
	private int axisY;
	private Rect2f view;
	private Vector2f scale; // Zoom Scale
	private float gridSize;
	private EditorTool tool;
	private boolean mode3d;
	
	private DirectionalLight sunlight;
	private Transform transform;
	private boolean dragging;
	private Vector2f dragPoint;
	private Vector2f dragOffset;
	private Texture texture;
	private Material material;
	private Material brushMaterial;
	private MeshRenderer brushRenderer;
	private Model selectedModel;
	private boolean draggingVertex;
	private Vector3f dragVertex;
	private int dragScaleAxis;
	private Brush dragBrush;
	
	private SceneObject scene;
	private ArrayList<BaseLight> lights;
	
	
	
	// ================= INITIALIZATION================== //
	
	@Override
	public void init() {
		
		axisX = Axis.X_AXIS;
		axisY = Axis.Z_AXIS;
		
		EntityObject tempObj;
		scene          = new SceneObject();
		transform      = new Transform();
		camera         = new Camera();
		lights         = new ArrayList<BaseLight>();
		objects        = new ArrayList<EntityObject>();
		brushes        = new ArrayList<Brush>();
		selectedModel  = null;
		brushRenderer  = new MeshRenderer(null, null);
		brushMaterial  = new Material();
		draggingVertex = false;
		dragVertex     = null;
		dragOffset     = new Vector2f();
		dragBrush      = null;
		gridSize       = 0.25f;
		scale          = new Vector2f(1, 1);
		dragScaleAxis  = 0;
		view           = new Rect2f(-1, -1, 1, 1);
		axisColors     = new HashMap<Integer, Vector3f>();
		tool           = new ToolSelection(this);
		mode3d         = false;
		cameraObject = new EntityObject()
        		.addComponent(new LookControllerOLD(0.2f))
        		.addComponent(new MoveControllerOLD(10.0f));
//        		.addComponent(camera);
        getEngine().getRenderingEngine().setMainCamera(camera);
        cameraObject.getTransform().setPosition(0, 3, 0);
		setScale(8);
		getEngine().getRenderingEngine().setAmbientLight(new Vector3f(1));
		
		axisColors.put(Axis.X_AXIS, Draw2D.RED);
		axisColors.put(Axis.Y_AXIS, Draw2D.GREEN);
		axisColors.put(Axis.Z_AXIS, Draw2D.BLUE);
		
		brushMaterial.setTexture(new Texture("bricks.jpg"));
//		brushMaterial.setNormalMap(new Texture("bricks_normal.jpg"));
//		brushMaterial.setDispMap(new Texture("bricks_disp.jpg"), 0.03f, -0.7f);
		brushMaterial.setSpecular(1.0f, 8.0f);
		
		int pns = 64;
		brushMaterial.setTexture(new Texture(pns, pns, Util.createNoiseNormalMap(pns, pns, 5)));
//		brushMaterial.setTexture(new Texture("white.jpg"));
//		brushMaterial.setNormalMap(new Texture(pns, pns, Util.createNoiseMap(pns, pns, 5)));
		
		sunlight = new DirectionalLight(new Vector3f(1), 1, 40);
		sunlight.getTransform().setDirection(new Vector3f(-1, -1, -1));
		scene.addChild(sunlight);
		lights.add(sunlight);
		
		PointLight light = new PointLight(new Vector3f(1, 1, 0.4f), 0.8f, new Attenuation(0, 0, 0.1f));
		scene.addChild(light);
		lights.add(light);
		
		dragging  = false;
		dragPoint = new Vector2f();
		
		texture = new Texture("bricks.jpg");
		material = new Material();
		material.setTexture(texture);
		
		// Create a brush.
//		new EntityObject().addComponent(brushRenderer);
		brushes.add(new Brush());
		/*
		// Couch
		tempObj = addObject(new Model("couch.obj"));
		tempObj.getTransform().setPosition(3, 0, 3);
		
		// Lara
		tempObj = addObject(new Model("liz.obj"));
//		tempObj = addObject(new Model("Lara_Croft_v1.obj"));
//		tempObj = addObject(new Model("Halo_3_SPARTAN.obj"));
		tempObj.getTransform().setScale(1.0f);
		tempObj.getTransform().setPosition(-3, 0, -3);
//		tempObj.getTransform().rotate(new Vector3f(1, 0, 0), (float) Math.toRadians(90));
//		tempObj.getTransform().rotate(new Vector3f(0, 0, 1), (float) Math.toRadians(180));
		 */
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Vector2f getViewPosition(Vector2f v) {
		Vector2f camPos = camera.getTransform().getPosition().swizzle(axisX, axisY);
		camPos.y *= -1;
		v.y *= -1;
		Vector2f result = v.minus(camPos);
		result.x /= scale.x;
		result.y /= scale.y;
		result.x = (result.x * 0.5f) + 0.5f;
		result.y = (result.y * 0.5f) + 0.5f;
		result.x *= Window.getWidth();
		result.y *= Window.getHeight();
		return result;
	}
	
	public Vector2f getMousePosition() {
		Vector2f mousePos = Mouse.getPosition();
		Vector2f camPos = camera.getTransform().getPosition().swizzle(axisX, axisY);
		mousePos.x /= Window.getWidth();
		mousePos.y /= Window.getHeight();
		mousePos.x = (mousePos.x * 2) - 1;
		mousePos.y = (mousePos.y * 2) - 1;
		mousePos.x *= scale.x;
		mousePos.y *= -scale.y;
		mousePos.add(camPos);
		return mousePos;
	}
	
	public Vector2f getSnappedToGrid(Vector2f v) {
		return new Vector2f((float) Math.floor((v.x / gridSize) + 0.5f) * gridSize,
							(float) Math.floor((v.y / gridSize) + 0.5f) * gridSize);
	}
	
	public float getWorldLength(float viewLength) {
		return (viewLength * (2 * scale.x / Window.getWidth()));
	}
	
	public SceneObject getScene() {
		return scene;
	}
	
	public ArrayList<Brush> getBrushes() {
		return brushes;
	}
	
	public int getAxisX() {
		return axisX;
	}
	
	public int getAxisY() {
		return axisY;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setAxes(int axisX, int axisY) {
		this.axisX = axisX;
		this.axisY = axisY;
		this.mode3d = false;
		setScale(scale.x);
	}
	
	public void enable3DMode() {
		mode3d = true;
		camera.setProjection(Matrix4f.createPerspective((float) Math.toRadians(60), Window.getAspectRatio(), 0.001f, 1000));
//					  .times(Matrix4f.createRotation(new Vector3f(0, -1, 0), new Vector3f(0, 0, 1))));
	}
	
	public void addObject(EntityObject obj) {
		objects.add(obj);
	}
	
	public EntityObject addObject(ObjectComponent component) {
		EntityObject obj = new EntityObject().addComponent(component);
		objects.add(obj);
		return obj;
	}
	
	public void setScale(float xScale) {
		scale.x = xScale;
		scale.y = xScale / Window.getAspectRatio();
		Vector3f up = new Vector3f(0, 0, 1);
		Vector3f forward = new Vector3f(0, -1, 0);
		if (axisY == Axis.Y_AXIS)
			up = new Vector3f(0, 1, 0);
		if (axisX == Axis.X_AXIS && axisY == Axis.Y_AXIS)
			forward = new Vector3f(0, 0, 1);
		if (axisX == Axis.Z_AXIS)
			forward = new Vector3f(-1, 0, 0);
		
		camera.setProjection(Matrix4f.createOrthographic(-scale.x, scale.x, -scale.y,
				scale.y,-1000, 1000).times(Matrix4f.createRotation(forward, up)));
		camera.getTransform().getRotation().set(0, 0, 0, 1);
		camera.getTransform().getScale().set(1, 1, 1);
	}
	
	public void setup2D() {
		material.getTexture().bind(0); // Do this to avoid strange color issues
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
    	Vector2f viewPos = camera.getTransform().getPosition().swizzle(axisX, axisY);
    	glOrtho(viewPos.x - scale.x, viewPos.x + scale.x,
    			viewPos.y - scale.y, viewPos.y + scale.y, -1, 1);
    	glMatrixMode(GL_MODELVIEW);
    	glLoadIdentity();
	}
	
	public void setup3D() {
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glBindTexture(GL_TEXTURE_2D, 0);
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void update(float delta) {
		super.update(delta);
		
		Vector2f mp = Mouse.getPosition();
		
		if (mode3d) {
			cameraObject.update(delta);
			return;
		}
		
		tool.update();
	}
	
	@Override
	public void input(float delta) {
		super.input(delta);
		
		if (Keyboard.isKeyDown(Keyboard.KEY_1))
			setAxes(Axis.X_AXIS, Axis.Z_AXIS);
		if (Keyboard.isKeyDown(Keyboard.KEY_2))
			setAxes(Axis.X_AXIS, Axis.Y_AXIS);
		if (Keyboard.isKeyDown(Keyboard.KEY_3))
			setAxes(Axis.Z_AXIS, Axis.Y_AXIS);
		if (Keyboard.isKeyDown(Keyboard.KEY_4))
			enable3DMode();
		
		if (Keyboard.isKeyPressed(Keyboard.KEY_P)) {
			getEngine().getRenderingEngine().blurFilter(brushMaterial.getNormalMap(), new Texture("grass1.jpg"), 3.0f);
			System.out.println("BLUR");
		}
		
		if (mode3d) {
			cameraObject.input(delta);
			return;
		}
		
		float speed = scale.x * 0.03f;
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			cameraObject.getTransform().getPosition().add(Axis.getAxisVector(axisX).times(speed));
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			cameraObject.getTransform().getPosition().sub(Axis.getAxisVector(axisX).times(speed));
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			cameraObject.getTransform().getPosition().add(Axis.getAxisVector(axisY).times(speed));
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			cameraObject.getTransform().getPosition().sub(Axis.getAxisVector(axisY).times(speed));
		if (Mouse.onWheelDown())
			setScale(scale.x * 1.5f);
		if (Mouse.onWheelUp())
			setScale(scale.x / 1.5f);
		view.set(cameraObject.getTransform().getPosition().swizzle(axisX, axisY).minus(scale), scale.times(2));
		
		
		
		if (Keyboard.isKeyPressed(Keyboard.KEY_ENTER))
			brushes.add(new Brush());
		
		Vector2f mousePos = Mouse.getPosition();

		if (selectedModel != null) {
			float ang = 0.02f;
    		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7))
        		selectedModel.getTransform().rotate(new Vector3f(0, 1, 0), -ang);
    		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9))
    			selectedModel.getTransform().rotate(new Vector3f(0, 1, 0), ang);
    		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8))
    			selectedModel.getTransform().rotate(new Vector3f(1, 0, 0), ang);
    		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD5))
    			selectedModel.getTransform().rotate(new Vector3f(1, 0, 0), -ang);
    		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4))
    			selectedModel.getTransform().rotate(new Vector3f(0, 0, 1), ang);
    		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6))
    			selectedModel.getTransform().rotate(new Vector3f(0, 0, 1), -ang);
		}
		
		if (Keyboard.isKeyPressed(Keyboard.KEY_RBRACKET))
			gridSize *= 2.0f;
		if (Keyboard.isKeyPressed(Keyboard.KEY_LBRACKET))
			gridSize *= 0.5f;
	}
	
	@Override
	public ArrayList<BaseLight> getLights() {
		return lights;
	}

	@Override
	public void renderScene(Shader shader, RenderingEngine renderingEngine) {
        // Draw objects.
		for (EntityObject object : objects) {
			object.render(shader, renderingEngine);
		}
        
        // Draw Brushes.
		for (Brush brush : brushes) {
			shader.bind();
			shader.updateUniforms(transform, renderingEngine, brushMaterial);
			brush.getMesh().draw();
		}
	}

	public void render3d(RenderingEngine renderingEngine) {
		renderingEngine.setAmbientLight(new Vector3f(0.15f));
		renderingEngine.render(this);
		renderingEngine.setAmbientLight(new Vector3f(1));
		/*
		glClearColor(0, 0, 0, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		setup3D();
		
        // Draw objects.
		for (EntityObject object : objects) {
			object.render(renderingEngine.getForwardAmbient(), renderingEngine);
		}
        
		renderingEngine.setMainCamera(camera);
        // Draw Brushes.
		for (Brush brush : brushes) {
			renderingEngine.getForwardAmbient().bind();
			renderingEngine.getForwardAmbient().updateUniforms(transform, renderingEngine, brushMaterial);
			brush.draw();
		}
		*/
		glUseProgram(0);
	}
	
	@Override
	public void render(RenderingEngine renderingEngine) {
		if (mode3d) {
			render3d(renderingEngine);
			return;
		}
		glClearColor(0, 0, 0, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		setup2D();
		
		// Draw grid.
		Draw2D.setColor(Draw2D.DARK_GRAY);
		for (float x = view.getMinX() - (view.getMinX() % gridSize); x < view.getMaxX(); x += gridSize)
			Draw2D.drawLine(x, view.getMinY(), x, view.getMaxY());
		for (float y = view.getMinY() - (view.getMinY() % gridSize); y < view.getMaxY(); y += gridSize)
			Draw2D.drawLine(view.getMinX(), y, view.getMaxX(), y);
		
		// Draw the axis lines.
		Draw2D.setColor(axisColors.get(axisX));
		Draw2D.drawLine(view.getMinX(), 0, view.getMaxX(), 0);
		Draw2D.setColor(axisColors.get(axisY));
		Draw2D.drawLine(0, view.getMinY(), 0, view.getMaxY());
        
        setup3D();
        renderingEngine.setMainCamera(camera);
        
        // Draw objects.
		for (EntityObject object : objects) {
			object.render(renderingEngine.getForwardAmbient(), renderingEngine);
		}

		setup2D();
		
        // Draw objects.
//		for (EntityObject object : objects) {
//			float size = 1;
//			Draw2D.setColor(Draw2D.YELLOW);
//			Draw2D.drawRect(object.getTransform().getPosition().swizzle(axisX, axisY).minus(size / 2), new Vector2f(size));
//		}
		
        // Draw scene objects.
		for (SceneObject object : scene.getChildren()) {
			float size = 1;
			Draw2D.setColor(Draw2D.YELLOW);
			Rect3f bounds = object.getBoundingBox();
			Draw2D.drawRect(bounds.getPosition().swizzle(axisX, axisY),
							bounds.getSize().swizzle(axisX, axisY));
		}
		
		// Draw brushes.
		for (Brush brush : brushes) {
			for (int i = 0; i < brush.getNumVertices(); i++) {
				glColor4f(0, 0.5f, 0.5f, 1);
				brush.drawWireframe(axisX, axisY);
			}
		}
        
		// Draw the current tool.
		tool.draw();
		
		
		
		
		setup3D();
	}
}
