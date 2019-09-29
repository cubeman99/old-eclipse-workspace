package zelda.game.control.event;

import java.util.ArrayList;


/**
 * An Event Queue is an event itself that handles multiple
 * events ordered sequentially. The first event in the
 * sequence of events will begin and be updated, waiting to
 * be ended in order for the next event to begin and be
 * updated. This is repeated until the last event in the
 * sequence has been ended thus ending the Event Queue itself.
 * Only the currently updated event will be drawn.
 * 
 * @see Event
 * @see EventStack
 * @author David Jordan
 */
public class EventQueue extends Event {
	protected ArrayList<Event> events;
	protected int index;


	// ================== CONSTRUCTORS ================== //
	
	/** Construct an empty event queue. **/
	public EventQueue() {
		events = new ArrayList<Event>();
	}
	
	/** Construct an event queue from the given sequence of events. **/
	public EventQueue(Event... events) {
		this.events = new ArrayList<Event>(events.length);
		for (int i = 0; i < events.length; i++)
			this.events.add(events[i]);
	}



	// =================== ACCESSORS =================== //
	
	/** Return the currently updated event. **/
	public Event getCurrentEvent() {
		return events.get(index);
	}



	// ==================== MUTATORS ==================== //
	
	/** Append an event to the end of the sequence. **/
	public void addEvent(Event e) {
		events.add(e);
	}
	
	/** Insert an event in the sequence at the given index. **/
	public void addEvent(int index, Event e) {
		events.add(index, e);
	}
	
	/** Advance to the next event in the sequence. **/
	public void nextEvent() {
		index++;
		if (index >= events.size())
			end();
		else
			getCurrentEvent().begin(game);
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void begin() {
		index = 0;
		events.get(0).begin(game);
		while (isOccuring() && !getCurrentEvent().isOccuring())
			nextEvent();
	}

	@Override
	public void update() {
		getCurrentEvent().update();
		while (isOccuring() && !getCurrentEvent().isOccuring())
			nextEvent();
	}

	@Override
	public void draw() {
		if (index >= events.size()) {
			System.out.println("ASD");
		}
		getCurrentEvent().preDraw();
		getCurrentEvent().draw();
		getCurrentEvent().postDraw();
	}
}
