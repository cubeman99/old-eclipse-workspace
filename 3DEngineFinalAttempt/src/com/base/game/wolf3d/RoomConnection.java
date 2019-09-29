package com.base.game.wolf3d;

import com.base.game.wolf3d.tile.mesh.Door;

public class RoomConnection {
	private Room source;
	private Room connection;
	private Door door;

	
	
	// ================== CONSTRUCTORS ================== //

	public RoomConnection(Room source, Door door) {
		this(source, null, door);
	}
	
	public RoomConnection(Room source, Room connection, Door door) {
		this.source     = source;
		this.connection = connection;
		this.door       = door;
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public Room getSource() {
		return source;
	}
	
	public Room getConnection() {
		return connection;
	}
	
	public Door getDoor() {
		return door;
	}

	
	
	// ==================== MUTATORS ==================== //
	
	public void setSource(Room source) {
		this.source = source;
	}
	
	public void setConnection(Room connection) {
		this.connection = connection;
	}
	
	public void setDoor(Door door) {
		this.door = door;
	}
}
