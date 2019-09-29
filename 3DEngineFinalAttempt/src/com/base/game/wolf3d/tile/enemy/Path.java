package com.base.game.wolf3d.tile.enemy;

import java.util.ArrayList;
import com.base.engine.common.Vector2f;

public class Path {
	private ArrayList<Vector2f> nodes;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Path() {
		nodes = new ArrayList<Vector2f>();
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Vector2f getNode(int index) {
		return nodes.get(index);
	}
	
	public int size() {
		return nodes.size();
	}
	
	public float getDistance() {
		float distance = 0;
		for (int i = 1; i < nodes.size(); i++) {
			distance += nodes.get(i).distanceTo(nodes.get(i - 1));
		}
		return distance;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void addNode(Vector2f node) {
		nodes.add(new Vector2f(node));
	}
	
	public void addNode(float x, float y) {
		nodes.add(new Vector2f(x, y));
	}
}
