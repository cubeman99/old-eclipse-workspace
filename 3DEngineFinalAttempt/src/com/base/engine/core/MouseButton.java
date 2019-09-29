package com.base.engine.core;

public class MouseButton {
	protected int buttonIndex;
	protected boolean down;
	protected boolean downPrev;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public MouseButton(int buttonIndex) {
		this.buttonIndex = buttonIndex;
		this.down        = false;
		this.downPrev    = false;
	}
	
	

	// =================== ACCESSORS =================== //
	
	/** Check if the mouse button is down. **/
	public boolean down() {
		return Mouse.buttonStates[buttonIndex];
	}

	/** Check if the mouse button was pressed. **/
	public boolean pressed() {
		return (Mouse.buttonStates[buttonIndex] && !Mouse.buttonPrevStates[buttonIndex]);
	}

	/** Check if the mouse button was released. **/
	public boolean released() {
		return (!Mouse.buttonStates[buttonIndex] && Mouse.buttonPrevStates[buttonIndex]);
	}
}
