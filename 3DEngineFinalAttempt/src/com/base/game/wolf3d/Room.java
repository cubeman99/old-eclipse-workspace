package com.base.game.wolf3d;

import java.util.ArrayList;
import com.base.engine.common.Vector2f;
import com.base.game.wolf3d.tile.ObjectTile;
import com.base.game.wolf3d.tile.enemy.BasicEnemy;
import com.base.game.wolf3d.tile.enemy.Enemy;
import com.base.game.wolf3d.tile.mesh.Door;

public class Room {
	private Level level;
	private ArrayList<RoomConnection> connections;
	private int index;
	
	
	// ================== CONSTRUCTORS ================== //

	public Room(Level level) {
		this.level = level;
		connections = new ArrayList<RoomConnection>();
	}
	
	

	// =================== ACCESSORS =================== //
	
	public boolean connectsTo(Room other) {
		if (other == null)
			return false;
		for (RoomConnection connection : connections) {
			if (connection.getConnection() == other)
				return true;
		}
		return false;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getNumConnections() {
		return connections.size();
	}
	
	public boolean hasDoor(Door door) {
		for (RoomConnection connection : connections) {
			if (connection.getDoor() == door)
				return true;
		}
		return false;
	}
	
	public ArrayList<RoomConnection> getConnections() {
		return connections;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void addDoor(Door door) {
		connections.add(new RoomConnection(this, door));
	}
	
	public void addConnection(Room room, Door door) {
		connections.add(new RoomConnection(this, room, door));
	}
	
	public void removeSelfConnections() {
		for (int i = 0; i < connections.size(); i++) {
			boolean remove = false;
			int count = 1;
			
			for (int j = i + 1; j < connections.size(); j++) {
				if (connections.get(i).getDoor() == connections.get(j).getDoor()) {
					connections.remove(j--);
					remove = true;
					count++;
				}
			}
			
			System.out.print(i + "[" + count + "]");
			
			if (remove)
				connections.remove(i--);
		}
	}
	
	public void alertEnemiesInConnectedRooms(Vector2f center) {
		alertEnemiesInConnectedRooms(center, Wolf3D.ENEMY_ALERT_DISTANCE);
	}
	
	public void alertEnemiesInConnectedRooms(Vector2f center, float minDistance) {
		for (RoomConnection connection : connections)
			connection.getConnection().alertEnemies(center, minDistance);
	}

	public void alertEnemies() {
		alertEnemies(null, -1);
	}

	public void alertEnemies(Vector2f center) {
		alertEnemies(center, Wolf3D.ENEMY_ALERT_DISTANCE);
	}
	
	public void alertEnemies(Vector2f center, float minDistance) {
		for (ObjectTile obj : level.getObjects()) {
			if (obj instanceof BasicEnemy) {
				BasicEnemy enemy = (BasicEnemy) obj;
				
				if (enemy.getRoom() == this && (center == null || minDistance < 0
						|| enemy.getPosition().distanceTo(center) <= minDistance))
				{
					enemy.alert();
				}
			}
		}
	}
}
