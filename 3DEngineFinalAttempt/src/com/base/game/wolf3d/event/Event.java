package com.base.game.wolf3d.event;

import com.base.game.wolf3d.Game;


public abstract class Event {
	protected boolean occuring;
	private boolean destroyed;
	private Game game;
	

	// ================== CONSTRUCTORS ================== //
	
	public Event() {
		occuring = false;
	}
	
	

	// ============== OVERRIDABLE METHODS ============== //
	
	public void begin() {}
	
	public void end() {
		occuring = false;
		destroy();
	}

	public void update(float delta) {}
	
	public void render() {}
	


	// =================== ACCESSORS =================== //
	
	public Game getGame() {
		return game;
	}

	public boolean isDestroyed() {
		return destroyed;
	}
	
	public boolean isOccuring() {
		return occuring;
	}



	// ==================== MUTATORS ==================== //
	
	public void destroy() {
		destroyed = true;
	}
	
	public Event begin(Game game) {
		this.game     = game;
		this.occuring = true;
		begin();
		return this;
	}
	
	protected void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
}
