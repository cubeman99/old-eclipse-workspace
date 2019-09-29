package com.base.game.wolf3d.tile;

import com.base.engine.common.GMath;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Rect2f;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.entity.SceneObject;
import com.base.game.wolf3d.Game;
import com.base.game.wolf3d.Level;
import com.base.game.wolf3d.Room;

public class Tile extends SceneObject {
	protected Level level;
	protected TileData data;
	protected String name;
	protected boolean solid;
	protected boolean hitSolid;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Tile(String name, boolean solid) {
		this.name     = name;
		this.solid    = solid;
		this.hitSolid = solid;
		this.data     = new TileData();
	}

	
	
	public void initialize() {}
	
	

	// =================== ACCESSORS =================== //
	
	public TileData getData() {
		return data;
	}
	
	public Vector2f getPosition() {
		return getTransform().getPosition().getXZ();
	}
	
	public Vector2f getDirection() {
		return getTransform().getRotation().getForward().getXZ().normalize();
	}
	
	public boolean isSolid() {
		return solid;
	}
	
	public boolean isHitSolid() {
		return hitSolid;
	}
	
	public String getName() {
		return name;
	}
	
	public Tile clone() {
		return null;
	}
	
	public Level getLevel() {
		return level;
	}
	
	public Room getRoom() {
		return level.getRoom(getPosition());
	}
	
	public Player getPlayer() {
		return level.getPlayer();
	}
	
	public Game getGame() {
		return level.getGame();
	}
	
	public Rect2f getHitBox() {
		return getCollisionBox();
	}
	
	public Rect2f getCollisionBox() {
		return new Rect2f(getTransform().getPosition().x,
				getTransform().getPosition().z, 1, 1);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setData(TileData data) {
		this.data = data;
	}
	
	public void setPosition(Vector2f pos) {
		getTransform().getPosition().x = pos.x;
		getTransform().getPosition().z = pos.y;
	}
	
	public void setDirection(Vector2f dir) {
		setDirection(dir.getDirection());
	}
	
	public void setDirection(float angle) {
		getTransform().setRotation(new Quaternion(Vector3f.Y_AXIS, GMath.HALF_PI - angle));
	}
	
	public void initialize(Level level) {
		this.level = level;
		initialize();
	}
}
