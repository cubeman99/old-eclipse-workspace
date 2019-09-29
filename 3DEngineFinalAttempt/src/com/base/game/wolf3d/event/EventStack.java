package com.base.game.wolf3d.event;

import java.util.ArrayList;


/**
 * An Event Stack is an event itself that handles multiple
 * events ordered in a stack-structure, meaning that only the
 * uppermost event in the stack is updated while the events below
 * must wait for that uppermost event to end before lower events
 * in the stack will be updated. Regardless, all events are drawn
 * from the bottom to top.
 * 
 * @see Event
 * @see EventQueue
 * @author David Jordan
 */
public class EventStack extends Event {
	protected ArrayList<Event> events;

	
	// ================== CONSTRUCTORS ================== //
	
	/** Construct an empty event stack. **/
	public EventStack() {
		events = new ArrayList<Event>();
	}
	
	/** Construct an event stack bottom-up from the given events. **/
	public EventStack(Event... events) {
		this.events = new ArrayList<Event>(events.length);
		for (int i = 0; i < events.length; i++)
			this.events.add(events[i]);
	}



	// =================== ACCESSORS =================== //
	
	/** Return the uppermost event, which is the current one to update. **/
	public Event getCurrentEvent() {
		if (events.size() == 0)
			return null;
		return events.get(events.size() - 1);
	}
	
	public int getSize() {
		return events.size();
	}



	// ==================== MUTATORS ==================== //
	
	/** Add an event to the top of the stack. (Note: This is only to
	 *  be called before beginning this event-stack)
	 */
	public void addEvent(Event e) {
		events.add(e);
	}
	
	/** Start a new event, adding it to the top of the stack. **/
	public void newEvent(Event e) {
		events.add(e);
		e.begin(getGame());
		if (!e.isOccuring())
			nextEvent();
	}
	
	/** Get rid of the uppermost event, advancing to the event below it. **/
	public void nextEvent() {
		events.remove(events.size() - 1);
		if (events.size() == 0)
			end();
		else
			getCurrentEvent().begin(getGame());
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void begin() {
		// Begin all events in the stack, starting from the bottom.
		for (int i = 0; i < events.size(); i++)
			events.get(i).begin(getGame());

		// Search for a lowestmost ended-event, removing events above it.
		for (int i = 0; i < events.size(); i++) {
			if (!events.get(i).isOccuring()) {
				while (i + 1 < events.size())
					events.remove(i);
			}
		}
		
		// End this event if there are no more events in the stack.
		if (events.size() == 0)
			end();
	}
	
	@Override
	public void update(float delta) {
		// Check for any previously ended events.
		for (int i = 0; i < events.size(); i++) {
			if (!events.get(i).isOccuring())
				events.remove(i--);
		}
		if (events.size() == 0) {
			end();
			return;
		}
		
		// Update the uppermost event in the stack.
		getCurrentEvent().update(delta);
		
		// Check for advancement.
		if (!getCurrentEvent().isOccuring())
			nextEvent();
		
		// Check if there are no more events.
		if (events.size() == 0)
			end();
	}

	@Override
	public void render() {
		// Check for any previously ended events.
		for (int i = 0; i < events.size(); i++) {
			if (!events.get(i).isOccuring())
				events.remove(i--);
		}
		if (events.size() == 0) {
			end();
			return;
		}
		
		// Draw ALL events from bottom to top.
		for (int i = 0; i < events.size(); i++)
			events.get(i).render();
	}
}
