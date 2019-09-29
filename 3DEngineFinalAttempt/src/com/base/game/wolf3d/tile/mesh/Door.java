package com.base.game.wolf3d.tile.mesh;

import com.base.engine.audio.AudioEngine;
import com.base.engine.common.GMath;
import com.base.engine.common.Line2f;
import com.base.engine.common.Rect2f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Keyboard;
import com.base.engine.entity.Model;
import com.base.engine.entity.SceneObject;
import com.base.engine.rendering.Material;
import com.base.engine.rendering.Mesh;
import com.base.engine.rendering.Primitives;
import com.base.engine.rendering.Texture;
import com.base.game.wolf3d.Wolf3D;
import com.base.game.wolf3d.tile.Player;
import com.base.game.wolf3d.tile.Unit;

public class Door extends MeshTile {
	private static final float SPEED       = 1.0f;
	private static final float THICKNESS   = 0.09375f;
	private static final float CLOSE_DELAY = 3;
	private Model baseModel;
	private Model doorModel;
	private boolean opening;
	private boolean closing;
	private boolean open;
	private float autoCloseTimer;
	private Vector2f direction;
	private int orientation;
	private int keyIndex;
	
	private float doorPosition;
	
	private Material materialFront;
	private Material materialSide;
	private Material materialSlot;
	
	

	// ================== CONSTRUCTORS ================== //
	
	public Door() {
		super("Door", true);
	}
	
	@Override
	public void initialize() {
		orientation = (getData().getInt("direction") / 2) % 4;
		keyIndex    = getData().getInt("key", -1);
		int texX    = getData().getInt("textureX", 2);
		int texY    = getData().getInt("textureY", (keyIndex < 0 ? 16 : 17));
		if (orientation % 2 == 1)
			texX++;
		
		doorPosition   = 0;
		open           = false;
		opening        = false;
		closing        = false;
		autoCloseTimer = 0;
		direction      = Vector2f.polarVector(1, orientation * GMath.HALF_PI);

		
		// Create 3D door model.
		Mesh[] meshes;
		Material[] materials;
		materialFront = new Material();
		materialFront.setTexture(Wolf3D.WALL_TEXTURES[texX][texY]);
		materialSlot = new Material();
		materialSlot.setTexture(Wolf3D.WALL_TEXTURES[4 + 1 - (orientation % 2)][16]);
		materialSide = new Material();
		materialSide.setTexture(new Texture("door_side.png"));
		
		meshes = new Mesh[2];
		meshes[0] = Primitives.createZYPlane(-0.5f, 0.5f, 0, 1, -0.5f, false);
		meshes[1] = Primitives.createZYPlane(0.5f, -0.5f, 0, 1, 0.5f, false);
		materials = new Material[] {materialSlot, materialSlot};
		baseModel = new Model(meshes, materials, 2);
		baseModel.getTransform().getPosition().setXZ(0.5f, 0.5f);
		baseModel.getTransform().rotate(Vector3f.Y_AXIS, -orientation * GMath.HALF_PI);
		addChild(baseModel);
		
		meshes = new Mesh[3];
		meshes[0] = Primitives.createXYPlane(-0.5f, 0.5f, 0, 1, -THICKNESS / 2, false);
		meshes[1] = Primitives.createXYPlane(-0.5f, 0.5f, 0, 1, THICKNESS / 2, true);
		meshes[2] = Primitives.createZYPlane(-THICKNESS / 2, THICKNESS / 2, 0, 1, -0.5f, true);
		materials = new Material[] {materialFront, materialFront, materialSide};
		doorModel = new Model(meshes, materials, 3);
		
		baseModel.addChild(doorModel);
	}

	
	
	// =================== ACCESSORS =================== //
	
	public boolean hasKey() {
		return (keyIndex < 0 || getPlayer().hasKey(keyIndex));
	}
	
	public boolean isObstructed() {
		Rect2f box = getCollisionBox();
		
		if (box.touches(getPlayer().getCollisionBox()))
			return true;
		
		// Check if a unit is blocking this door.
		for (SceneObject object : getLevel().getObjects()) {
			if (object instanceof Unit) {
				Unit unit = (Unit) object;
				if (unit.isSolid() && box.touches(unit.getCollisionBox()))
    				return true;
			}
		}
		
		return false;
	}
	
	public int getOrientation() {
		return orientation;
	}
	
	public Rect2f getDoorBox() {
		Vector3f pos = getTransform().getPosition().plus(doorModel.getTransform().getPosition().minus(0.5f));
		if (orientation % 2 == 1) {
			Rect2f rect = new Rect2f(pos.x + 0.5f - THICKNESS / 2, pos.z, THICKNESS, 1);
			if (rect.getMaxY() > getTransform().getPosition().z + 1)
				rect.setHeight(getTransform().getPosition().z + 1 - rect.getMinY());
			return rect;
		}
		Rect2f rect = new Rect2f(pos.x, pos.z + 0.5f - THICKNESS / 2, 1, THICKNESS);
		if (rect.getMaxX() > getTransform().getPosition().x + 1)
			rect.setWidth(getTransform().getPosition().x + 1 - rect.getMinX());
		return rect;
	}
	
	

	// ==================== MUTATORS ==================== //
	
	public void close() {
		if (open && !opening && !closing && !isObstructed()) {
    		closing = true;
    		opening = false;
    		AudioEngine.playSound(Wolf3D.SOUND_DOOR_CLOSE, this);
		}
	}
	
	public void open() {
		if (!opening && (!open || closing)) {
			if (!closing)
	    		AudioEngine.playSound(Wolf3D.SOUND_DOOR_OPEN, this);
    		opening        = true;
    		closing        = false;
    		autoCloseTimer = 0;
		}
	}

	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public Door clone() {
		return new Door();
	}
	
	@Override
	public Rect2f getHitBox() {
		Vector2f pos = getPosition().plus(direction.times(doorPosition)).plus(0);
		if (orientation % 2 == 1)
			return new Rect2f(pos.x + 0.5f - THICKNESS / 2, pos.y, THICKNESS, 1);
		return new Rect2f(pos.x, pos.y + 0.5f - THICKNESS / 2, 1, THICKNESS);
	}
	
	@Override
	public Rect2f getCollisionBox() {
		Vector2f pos = getPosition();
		float thick = THICKNESS * 2;
		if (orientation % 2 == 1)
			return new Rect2f(pos.x + 0.5f - thick / 2, pos.y, thick, 1);
		return new Rect2f(pos.x, pos.y + 0.5f - thick / 2, 1, thick);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		// Update opening and closing.
		if (opening) {
			doorPosition += delta * SPEED;
			if (doorPosition >= 1.0f) {
				doorPosition = 1.0f;
				opening      = false;
				open         = true;
			}
		}
		else if (closing) {
			doorPosition -= delta * SPEED;
			if (doorPosition <= 0.0f) {
				doorPosition = 0.0f;
				closing      = false;
				open         = false;
			}
		}
		else if (open) {
			if (isObstructed())
				autoCloseTimer = 0;
			else {
    			autoCloseTimer += delta;
    			if (autoCloseTimer >= CLOSE_DELAY)
    				close();
			}
		}
		
		// Check for player opening door.
		if (Keyboard.isKeyDown(Player.KEY_USE) && hasKey()) { // && getPlayer().getRayCast().object == this) {
    		Vector2f pos       = getPosition();
    		Vector2f playerPos = level.getPlayer().getPosition();
    		Vector2f look      = level.getPlayer().getDirection().setLength(1.5f);
    		Line2f playerLine  = new Line2f(playerPos, playerPos.plus(look));
    		Line2f doorLine    = new Line2f(pos.plus(0.5f).plus(direction.times(0.5f)), pos.plus(0.5f).minus(direction.times(0.5f)));
    		
    		if (Line2f.intersection(playerLine, doorLine) != null) {
    			if (!opening && (!open || closing))
    				open();
    		}
		}

		solid = !open || closing || opening;
		doorModel.getTransform().getPosition().setX(doorPosition);
	}
	
//	@Override
//	public void render(Shader shader, RenderingEngine renderingEngine) {
//		baseModel.render(shader, renderingEngine);
//		if (!open || opening || closing)
//			doorModel.render(shader, renderingEngine);
//	}
}
