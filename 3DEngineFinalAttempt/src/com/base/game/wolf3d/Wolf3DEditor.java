package com.base.game.wolf3d;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.*;
import java.awt.Color;
import java.io.File;
import com.base.engine.common.GMath;
import com.base.engine.common.Matrix4f;
import com.base.engine.common.Point;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.AbstractGame;
import com.base.engine.core.CoreEngine;
import com.base.engine.core.Keyboard;
import com.base.engine.core.Mouse;
import com.base.engine.core.Util;
import com.base.engine.rendering.Bitmap;
import com.base.engine.rendering.Camera;
import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Font;
import com.base.engine.rendering.RenderingEngine;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.Window;
import com.base.game.wolf3d.tile.ObjectTile;
import com.base.game.wolf3d.tile.PatrolNode;
import com.base.game.wolf3d.tile.PlayerStart;
import com.base.game.wolf3d.tile.Tile;
import com.base.game.wolf3d.tile.TileData;
import com.base.game.wolf3d.tile.enemy.BasicEnemy;
import com.base.game.wolf3d.tile.enemy.Guard;
import com.base.game.wolf3d.tile.mesh.Door;
import com.base.game.wolf3d.tile.mesh.Elevator;
import com.base.game.wolf3d.tile.mesh.SecretPassage;



public class Wolf3DEditor extends AbstractGame {
	private static int NUM_TEXTURES   = 114;
	private static int NUM_OBJECTS    = 50;
	private static int TEXTURE_OFFSET = 1;
	private static int OBJECT_OFFSET  = TEXTURE_OFFSET + NUM_TEXTURES;
	
	private static Texture[][] ICONS;
	
	
	private Camera camera;
	private LevelFileData level;
	private Texture[][] objectTextures;
	private Texture[][] wallTextures;
	private int paintId;
	private int eraseId;
	private Vector2f viewPosition;
	private float zoom;
	
	private File file;
	
	
	
	@Override
	public void init() {
		Wolf3D.initialize();
		
		NUM_OBJECTS = Wolf3D.OBJECT_TILES.length;
		
		ICONS = Util.createTextureSheet(new Bitmap("editor_icons.png"), 10, 10, 32, 32, 1);
		
		camera = new Camera(Matrix4f.createOrthographic(0, Window.getWidth(), 0, Window.getHeight(), -100, 100));
		getEngine().getRenderingEngine().setMainCamera(camera);
		getEngine().getRenderingEngine().setAmbientLight(new Vector3f(1));
		
		paintId = 1;
		eraseId = 0;
		
		level = new LevelFileData(80, 80);

		
		
		zoom = 64;
		viewPosition = new Vector2f();
		
		Bitmap bitmap = new Bitmap("objects.png");
		objectTextures = Util.createTextureSheet(bitmap, 5, 10, 64, 64, 1);
		bitmap = new Bitmap("walls.png");
		wallTextures = Util.createTextureSheet(bitmap, 6, 19, 64, 64, 0);
		
		file = null;
		
		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				level.setData(x, y, 0);
				if (x == 0 || y == 0 || x == level.getWidth() - 1 || y == level.getHeight() - 1)
					level.setData(x, y, 1);
			}
		}
	}
	
	private void setZoomFocus(Vector2f focus, float newZoom) {
		viewPosition.sub(focus.times(zoom / newZoom).sub(focus).dividedBy(zoom));
		zoom = newZoom;
	}
	
	private void updateViewControls() {
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
			return;
		
		float panSpeed = 16 / zoom;
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
			viewPosition.x -= panSpeed;
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
			viewPosition.x += panSpeed;
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
			viewPosition.y -= panSpeed;
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
			viewPosition.y += panSpeed;

		float zoomScale = 1.2f;
		if (Mouse.onWheelUp())
			setZoomFocus(Mouse.getPosition(), zoom * zoomScale);
		if (Mouse.onWheelDown())
			setZoomFocus(Mouse.getPosition(), zoom / zoomScale);
	}
	
	private Point getCursorPosition() {
		Point p = new Point(Mouse.getPosition().dividedBy(zoom).plus(viewPosition));
		p.y = level.getHeight() - p.y - 1;
		return p;
	}
	
	@Override
	public void update(float delta) {
		updateViewControls();
		Point cursor = getCursorPosition();
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyPressed(Keyboard.KEY_S)) {
			if (file == null)
				file = new File("./res/wolf3d/levels/testLevel.lvl");
			level.save(file);
			System.out.println("Saved to " + file.getName());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyPressed(Keyboard.KEY_O)) {
			file = new File("./res/wolf3d/levels/testLevel.lvl");
			level.load(file);
			System.out.println("Loaded " + file.getName());
		}

		if (Keyboard.isKeyPressed(Keyboard.KEY_1))
			paintId = LevelFileData.TEXTURE_OFFSET;
		if (Keyboard.isKeyPressed(Keyboard.KEY_2))
			paintId = LevelFileData.OBJECT_OFFSET;
		if (Keyboard.isKeyPressed(Keyboard.KEY_3))
			paintId = LevelFileData.OBJECT_OFFSET + Wolf3D.OBJECT_TILES.length - 1;
		
		if (Keyboard.isKeyPressed(Keyboard.KEY_E))
			paintId++;
		if (Keyboard.isKeyPressed(Keyboard.KEY_Q) && paintId > 0)
			paintId--;
		
		if (cursor.x >= 0 && cursor.y >= 0 && cursor.x < level.getWidth() && cursor.y < level.getHeight()) {
    		if (Keyboard.isKeyDown(Keyboard.KEY_R) && Mouse.left.down())
    			paintId = level.getData(cursor);
    		else if (Mouse.left.down())
    			level.setData(cursor, paintId);
    		if (Mouse.right.down()) {
    			level.setData(cursor, eraseId);
    			level.getTileData(cursor).clear();
    		}

			TileData td = level.getTileData(cursor);
    		if (Keyboard.isKeyPressed(Keyboard.KEY_TAB)) {
				int newDir = td.getInt("direction", -1) + 1;
				if (newDir >= 8)
					td.removeInt("direction");
				else
					td.setInt("direction", newDir);
    		}
    		else if (Keyboard.isKeyPressed(Keyboard.KEY_BACKSLASH)) {
    			int state = td.getInt("state", -1);
    			if (state < 0)
    				td.setInt("state", BasicEnemy.AMBUSH);
    			else if (state == BasicEnemy.AMBUSH)
    				td.setInt("state", BasicEnemy.PATROL);
    			else if (state == BasicEnemy.PATROL)
    				td.removeInt("state");
    		}
    		if (Keyboard.isKeyPressed(Keyboard.KEY_APOSTROPHE)) {
				int key = td.getInt("key", -1) + 1;
				if (key >= 2)
					td.removeInt("key");
				else
					td.setInt("key", key);
    		}
    		else if (Keyboard.isKeyPressed(Keyboard.KEY_TILDE)) {
    			if (td.existsInt("textureX")) {
    				td.removeInt("textureX");
    				td.removeInt("textureY");
    			}
    			else {
        			int texId = paintId - TEXTURE_OFFSET;
        			td.setInt("textureX", texId % 6);
        			td.setInt("textureY", texId / 6);
    			}
    		}

    		if (Mouse.middle.down())
    			paintId = level.getData(cursor);
		}
	}
	
	private Texture getIdTexture(int id) {
		if (id >= OBJECT_OFFSET) {
			int texId = id - OBJECT_OFFSET;
			Tile tile = Wolf3D.OBJECT_TILES[texId];
			
			if (tile instanceof ObjectTile)
				return ((ObjectTile) Wolf3D.OBJECT_TILES[texId]).getTexture();
			else if (tile instanceof Elevator)
				return ICONS[0][0];
			else if (tile instanceof Door)
				return ICONS[0][1];
			else if (tile instanceof SecretPassage)
				return ICONS[0][2];
			else if (tile instanceof PatrolNode)
				return ICONS[0][3];
//				return Wolf3D.WALL_TEXTURES[2 + ((Door) tile).getOrientation()][18];
			else if (tile instanceof PlayerStart)
				return Wolf3D.SHEET_BJ[0][0];
		}
		else if (id >= TEXTURE_OFFSET) {
			int texId = id - TEXTURE_OFFSET;
			int texX  = texId % 6;
			int texY  = texId / 6;
			return wallTextures[texX][texY];
		}
		return null;
	}
	
	@Override
	public void render(RenderingEngine renderingEngine) {
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
    	
    	Draw2D.setFont(Wolf3D.FONT);
    	
    	// Draw the map.
    	glMatrixMode(GL_PROJECTION);
    	glLoadIdentity();
    	glOrtho(viewPosition.x, viewPosition.x + Window.getWidth() / zoom,
    			viewPosition.y + Window.getHeight() / zoom, viewPosition.y, -1, 1);
    	glMatrixMode(GL_MODELVIEW);
    	glLoadIdentity();
    	
    	// Draw border.
    	float padding = 0.25f;
    	Draw2D.setColor(Draw2D.YELLOW);
    	Draw2D.drawRect(-padding, -padding, level.getWidth() + padding * 2,
    			level.getHeight() + padding * 2);
    	Draw2D.setColor(Draw2D.WHITE);
    	
    	// Draw tiles
		for (int x = 0; x < level.getWidth(); x++) {
			for (int y = 0; y < level.getHeight(); y++) {
				int id = level.getData(x, y);
				int dx = x;
				int dy = level.getHeight() - y - 1;
				
				Texture tex = getIdTexture(id);
				if (tex != null)
					Draw2D.drawTexture(tex, dx, dy, 1, 1);
				
				if (level.getTileData(x, y).existsInt("textureX")) {
					float pad = 0.1875f;
					int texX = level.getTileData(x, y).getInt("textureX");
					int texY = level.getTileData(x, y).getInt("textureY");
					Draw2D.drawTexture(wallTextures[texX][texY], dx + pad, dy + pad, 1 - (pad * 2), 1 - (pad * 2));
				}

    			int state = level.getTileData(x, y).getInt("state", -1);
    			if (state == Guard.PATROL)
					Draw2D.drawTexture(ICONS[2][0], dx, dy, 1, 1);
    			else if (state == Guard.AMBUSH)
					Draw2D.drawTexture(ICONS[2][1], dx, dy, 1, 1);
    			
    			int key = level.getTileData(x, y).getInt("key", -1);
				if (key >= 0)
					Draw2D.drawTexture(ICONS[2][2 + key], dx, dy, 1, 1);
					
				if (level.getTileData(x, y).existsInt("direction")) {
					Draw2D.drawTexture(ICONS[1][level.getTileData(x, y).getInt("direction")], dx, dy, 1, 1);
				}
			}
		}
		
		// Draw cursor
		Point cursor = getCursorPosition();
    	Draw2D.setColor(Draw2D.WHITE);
    	if (Mouse.middle.down() || Keyboard.isKeyDown(Keyboard.KEY_R))
    		Draw2D.setColor(Draw2D.MAGENTA);
    	Draw2D.drawRect(cursor.x, level.getHeight() - cursor.y - 1, 1, 1);
    	Draw2D.setColor(Draw2D.WHITE);
		
		
		// Draw HUD.
    	glMatrixMode(GL_PROJECTION);
    	glLoadIdentity();
    	glOrtho(0, Window.getWidth(), Window.getHeight(), 0, -1, 1);
    	glMatrixMode(GL_MODELVIEW);
    	glLoadIdentity();
    	
    	

    	// Paint Texture
		Texture tex = getIdTexture(paintId);
		if (tex != null)
			Draw2D.drawTexture(tex, 0, Window.getHeight() - 64, 64, 64);
		
		// Labels
		String str = "ID " + paintId;
		if (paintId == 0)
			str += " (Nothing)";
		else if (LevelFileData.isWall(paintId))
			str += " (Wall)";
		else if (LevelFileData.isObject(paintId))
			str += " (" + Wolf3D.OBJECT_TILES[LevelFileData.getObjectId(paintId)].getName() + ")";
		
		
		Draw2D.setColor(Draw2D.BLACK);
		Draw2D.fillRect(0, 0, 256, 128);
		Draw2D.setColor(Draw2D.WHITE);
		Draw2D.drawRect(0, 0, 256, 128);
		
		Draw2D.setColor(Draw2D.GREEN);
		
		Draw2D.drawText(str, 96, Window.getHeight() - 64, 8);
		String fileName = (file == null ? "<untitled>" : file.getName());
		Draw2D.drawText(fileName, 8, 8, 8);
		Draw2D.drawText(level.getWidth() + " x " + level.getHeight(), 8, 28, 8);
		
    	Draw2D.setColor(Draw2D.WHITE);
	}
	
	
	
	// ================= MAIN FUNCTION ================= //
	
	public static void main(String[] args) {
		CoreEngine engine = new CoreEngine(1000, 800, 60, new Wolf3DEditor());
		engine.createWindow("Wolfenstein 3D");
		engine.start();
	}
}
