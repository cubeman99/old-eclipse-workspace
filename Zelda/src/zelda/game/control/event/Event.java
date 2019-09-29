package zelda.game.control.event;

import zelda.game.control.GameInstance;
import zelda.game.entity.Entity;


/**
 * An Event represents an Entity that can begin,
 * be updated, and end. These are useful when dealing
 * with a sequence of events in which one type of event
 * will occur after another.
 * 
 * @see Entity
 * @see EventQueue
 * @see EventStack
 * @author David Jordan
 */
public abstract class Event extends Entity {
	protected boolean occuring;


	// ================== CONSTRUCTORS ================== //
	
	public Event() {
		occuring = false;
	}
	
	

	// ============== OVERRIDABLE METHODS ============== //
	
	/** Called once the event has begun and been associated
	 *  with a game instance.
	 */
	public void begin() {
		// This method is for implementational purposes.
	}
	
	/** End the event, setting it to not occuring anymore. **/
	public void end() {
		occuring = false;
		destroy();
	}


	// =================== ACCESSORS =================== //
	
	/** Wether the event is occuring (has not ended). **/
	public boolean isOccuring() {
		return occuring;
	}



	// ==================== MUTATORS ==================== //
	
	/** Begin this event by first associating it with a game instance
	 *  and then calling the begin function.
	 */
	public Event begin(GameInstance game) {
		this.game     = game;
		this.occuring = true;
		begin();
		return this;
	}
	
	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void update() {}

	@Override
	public void preDraw() {}
	
	@Override
	public void draw() {}

	@Override
	public void postDraw() {}
}
