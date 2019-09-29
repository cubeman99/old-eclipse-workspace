package com.base.engine.core;

public class Key {
	private int[] keyCodes;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Key(int... keyCodes) {
		this.keyCodes = keyCodes;
	}
	
	public Key(int keyCode) {
		this.keyCodes = new int[] {keyCode};
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Check if the key is currently down. **/
	public boolean down() {
		for (int key : keyCodes) {
			if (Keyboard.keys[key])
				return true;
		}
		return false;
	}
	
	/** Check if the key was pressed. **/
	public boolean pressed() {
		for (int key : keyCodes) {
			if (Keyboard.keys[key] && !Keyboard.lastKeys[key])
				return true;
		}
		return false;
	}

	/** Check if the key was released. **/
	public boolean released() {
		for (int key : keyCodes) {
			if (Keyboard.keys[key] || !Keyboard.lastKeys[key])
				return false;
		}
		return true;
	}
}
