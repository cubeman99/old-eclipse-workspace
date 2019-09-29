package com.base.game.wolf3d;

import static org.lwjgl.opengl.GL11.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import com.base.engine.common.GMath;
import com.base.engine.common.Line2f;
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
import com.base.engine.entity.EntityObject;
import com.base.engine.entity.LookController;
import com.base.engine.entity.LookControllerOLD;
import com.base.engine.entity.MeshRenderer;
import com.base.engine.entity.Model;
import com.base.engine.entity.MoveController;
import com.base.engine.entity.MoveControllerOLD;
import com.base.engine.entity.lights.BaseLight;
import com.base.engine.entity.lights.DirectionalLight;
import com.base.engine.entity.lights.PointLight;
import com.base.engine.entity.lights.SpotLight;
import com.base.engine.rendering.Attenuation;
import com.base.engine.rendering.Brush;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.DynamicMesh;
import com.base.engine.rendering.Font;
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
import com.base.engine.rendering.resourceManagement.TextureResource;
import com.base.game.TestModels;
import com.base.game.jets.JetController;
import com.base.game.wolf3d.event.Event;
import com.base.game.wolf3d.event.EventDie;
import com.base.game.wolf3d.event.EventScreenFade;
import com.base.game.wolf3d.event.EventStack;
import com.base.game.wolf3d.tile.Player;
import com.base.game.wolf3d.tile.enemy.Enemy;
import com.base.game.wolf3d.tile.enemy.Guard;
import com.base.game.wolf3d.tile.enemy.Officer;
import com.base.game.wolf3d.tile.enemy.SS;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_RG32F;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

public class Game extends AbstractGame {
	private Level level;
	private Episode episode;
	
	private int score;
	private int numLives;
	private int maxScore;
	private int maxNumLives;
	private int levelIndex;
	
	private Vector3f fadeColor;
	private float fadeAlpha;
	private boolean paused;
	
	private boolean dying;
	
	private EventStack eventStack;
	private EventScreenFade eventFade;
	
	private float time;
	
	private int windowWidth;
	private int windowHeight;
	private int viewPortWidth;
	private int viewPortHeight;
	
	private Texture sceneTexture;
	
	
	
	// ================= INITIALIZATION ================= //
	
	@Override
	public void init() {
		Wolf3D.initialize();
		Draw2D.setFont(Wolf3D.FONT);
		engine.getRenderingEngine().setAmbientLight(new Vector3f(1));
		
		numLives    = 3;
		maxNumLives = 9;
		score       = 0;
		maxScore    = 999999;
		fadeColor   = Draw2D.BLACK;
		fadeAlpha   = 0;
		levelIndex  = 1;
		paused      = false;
		dying = false;
		
		time = 0;
		
		episode = new Episode("E1", 2);

		sceneTexture = new Texture(Wolf3D.VIEWPORT_WIDTH * Wolf3D.RESOLUTION_SCALE,
						Wolf3D.VIEWPORT_HEIGHT * Wolf3D.RESOLUTION_SCALE, (ByteBuffer) null,
						GL_TEXTURE_2D, GL_NEAREST, GL_RGBA, GL_RGBA, false,
						GL_COLOR_ATTACHMENT0);
		
		
//		level = episode.createLevel(this, levelIndex);
//		world.addChild(level);
		
		// Create the level.
		level = new Level(this);
		world.addChild(level);
//		level.load("testLevel.lvl");
//		level.load("empty.lvl");
		level.load("testLevelE1L1.lvl");
		

		eventStack = new EventStack();
		eventStack.addEvent(new Event() {
			@Override
			public void update(float delta) {
				time += delta;
				
				if (!paused)
					updateScene(delta);

				
				if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
					Window.setFullScreen(!Window.isFullScreen());
				}

				if (Keyboard.isKeyPressed(Keyboard.KEY_HOME)) {
					getPlayer().heal(100);
					getPlayer().giveAmmo(100);
				}
				

				if (Keyboard.isKeyPressed(Keyboard.KEY_BACKSPACE)) {
					level.getMeshTiles().clear();
					for(int i = 0; i < level.getObjects().size(); i++) {
						if (!(level.getObjects().get(i) instanceof Player))
							level.getObjects().remove(i--);
					}
				}
				
				if (Keyboard.isKeyPressed(Keyboard.KEY_ENTER)) {
					Enemy enemy = new Guard();
					level.addObject(enemy, 10 + GMath.random.nextFloat() * 10,
							0, 10 + GMath.random.nextFloat() * 10);
				}
				
				fadeAlpha = Math.max(0, fadeAlpha - (delta / 2));
			}
			
			@Override
			public void render() {
				// Render 3D level.
				setup3D();
				getEngine().getRenderingEngine().render(world, sceneTexture, false, false);
				
				// Draw to screen.
				Window.bindAsRenderTarget();
				setup2D();
				
				// Draw scene and hud background.
				Draw2D.drawTexture(Wolf3D.TEXTURE_HUD, 0, 0, Wolf3D.WINDOW_WIDTH, Wolf3D.WINDOW_HEIGHT);
				Draw2D.drawTexture(sceneTexture, 8, 4 + Wolf3D.VIEWPORT_HEIGHT, Wolf3D.VIEWPORT_WIDTH, -Wolf3D.VIEWPORT_HEIGHT);
				
				// Draw weapon
				float size = Wolf3D.VIEWPORT_HEIGHT;
				Texture tex = getPlayer().getWeaponSprite().getCurrentFrame();
				Draw2D.drawTexture(tex, 8 + (Wolf3D.VIEWPORT_WIDTH / 2) - (size / 2), 4, size, size);
				
				// Draw Labels.
				drawHUD();

				Draw2D.setFont(Wolf3D.FONT);
				Draw2D.setColor(Draw2D.GREEN);
				Draw2D.drawText("Range = " + getPlayer().getRayCast().distance, 16, 16, 8);
				Room room = level.getRoom(getPlayer().getPosition());
				int roomIndex = (room == null ? -1 : room.getIndex());
				Draw2D.drawText("Room = " + roomIndex, 16, 32, 8);
				Draw2D.setColor(Draw2D.WHITE);
				
				// Draw Fade overlay.
				if (fadeAlpha > 0) {
		    		Draw2D.setColor(fadeColor, fadeAlpha);
		    		Draw2D.fillRect(0, 0, Wolf3D.WINDOW_WIDTH, Wolf3D.WINDOW_HEIGHT);
				}
			}
		});
		
		
		eventStack.begin(this);
	}
	
	
	// =================== ACCESSORS =================== //
	
	public int getScore() {
		return score;
	}
	
	public int getNumLives() {
		return numLives;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public Player getPlayer() {
		return level.getPlayer();
	}
	
	
	
	// ==================== MUTATORS ==================== //

	private void updateScene(float delta) {
		super.update(delta);
	}
	
	public void playEvent(Event e) {
		if (e != null) {
			System.out.println("PLAYING EVENT " + e.getClass().getSimpleName());
			eventStack.newEvent(e);
		}
	}
	
	public void restartLevel() {
		level.load("testLevel.lvl");
//		level.load(episode.getLevelFileName(0)); TODO
	}
	
	public void nextLevel() {
		levelIndex++;
		
		restartLevel(); // TODO
//		world.removeChild(level);
//		level = episode.createLevel(this, 0);
//		world.addChild(level);
	}
	
	public void setFade(Vector3f fadeColor, float fadeAlpha) {
		this.fadeColor = fadeColor;
		this.fadeAlpha = fadeAlpha;
	}
	
	public void addLife() {
		numLives = Math.min(maxNumLives, numLives + 1);
	}
	
	public void addScore(int amount) {
		score = Math.min(maxScore, score + amount);
	}
	

	private void render2D() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT);
		
    	glEnable(GL_TEXTURE_2D);
    	glBindTexture(GL_TEXTURE_2D, 0);
    	glDepthMask(false);
    	glDisable(GL_CULL_FACE);
    	glDisable(GL_DEPTH_TEST);
    	glDisable(GL_DEPTH_CLAMP);
    	glEnable(GL_BLEND);
    	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    	
    	float zoom = 48;
    	Vector2f viewPosition = new Vector2f(getPlayer().getPosition().sub(Window.getCenter().dividedBy(zoom)));
    	
    	
    	// Draw the map.
    	glMatrixMode(GL_PROJECTION);
    	glLoadIdentity();
    	glOrtho(viewPosition.x, viewPosition.x + Window.getWidth() / zoom,
    			viewPosition.y, viewPosition.y + Window.getHeight() / zoom, -1, 1);
    	glMatrixMode(GL_MODELVIEW);
    	glLoadIdentity();
    	
    	LevelDrawer.drawLevel(level);
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
    	glOrtho(0, Wolf3D.WINDOW_WIDTH, Wolf3D.WINDOW_HEIGHT, 0, -1, 1);
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
	
	public void drawHUD() {
		Draw2D.setColor(Draw2D.WHITE);
		
		// Draw info labels.
    	Draw2D.setFont(Wolf3D.FONT_HUD);
		Draw2D.drawText("" + (levelIndex + 1), 32, 176, 8, Draw2D.RIGHT);
		Draw2D.drawText("" + score, 96, 176, 8, Draw2D.RIGHT);
		Draw2D.drawText("" + numLives, 120, 176, 8, Draw2D.RIGHT);
		Draw2D.drawText("" + getPlayer().getHealth(), 192, 176, 8, Draw2D.RIGHT);
		Draw2D.drawText("" + getPlayer().getAmmo(), 232, 176, 8, Draw2D.RIGHT);
		
		// Draw face.
		int faceX = 1;
		int faceY = 6 - (int) (6 * getPlayer().getHealth() / (float) getPlayer().getHealthMax());
		if (getPlayer().isDead()) {
			faceX = 1;
			faceY = 7;
		}
		Draw2D.drawTexture(Wolf3D.SHEET_HUD_FACE[faceX][faceY], 137, 164, 24, 32);
		
		// Draw keys.
		if (getPlayer().hasKey(Wolf3D.GOLD_KEY))
			Draw2D.drawTexture(Wolf3D.SHEET_HUD_KEYS[Wolf3D.GOLD_KEY][0], 240, 164, 8, 16);
		if (getPlayer().hasKey(Wolf3D.SILVER_KEY))
			Draw2D.drawTexture(Wolf3D.SHEET_HUD_KEYS[Wolf3D.SILVER_KEY][0], 240, 180, 8, 16);
		
		// Draw weapon icon.
		Draw2D.drawTexture(Wolf3D.SHEET_HUD_WEAPONS[getPlayer().getWeaponIndex()][0], 256, 168, 48, 24);
	}
	
	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public void update(float delta) {
		eventStack.update(delta);
	}
	
	@Override
	public void render(RenderingEngine renderingEngine) {
		eventStack.render();
	}
	
	
	
	// ================= MAIN FUNCTION ================= //
	
	public static void main(String[] args) {
		CoreEngine engine = new CoreEngine(Wolf3D.WINDOW_WIDTH * Wolf3D.RESOLUTION_SCALE,
				Wolf3D.WINDOW_HEIGHT * Wolf3D.RESOLUTION_SCALE, 60, new Game());
		engine.createWindow("Wolfenstein 3D");
		engine.start();
	}
}
