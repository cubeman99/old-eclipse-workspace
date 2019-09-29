package com.base.game.wolf3d.tile;

import java.io.Serializable;
import java.util.HashMap;

public class TileData implements Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Integer> mapInt;

	
	// ================== CONSTRUCTORS ================== //

	public TileData() {
		mapInt = new HashMap<String, Integer>();
	}
	
	

	// =================== ACCESSORS =================== //

	public boolean existsInt(String name) {
		return mapInt.containsKey(name);
	}
	
	public int getInt(String name) {
		return getInt(name, 0);
	}
	
	public int getInt(String name, int defaultValue) {
		Integer result = mapInt.get(name);
		if (result != null)
			return result;
		return defaultValue;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void clear() {
		mapInt.clear();
	}
	
	public void setInt(String name, int value) {
		mapInt.put(name, value);
	}
	
	public void removeInt(String name) {
		mapInt.remove(name);
	}
}
