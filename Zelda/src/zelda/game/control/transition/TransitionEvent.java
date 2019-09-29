package zelda.game.control.transition;

import zelda.common.util.Destination;
import zelda.game.control.event.Event;
import zelda.game.world.Frame;


public class TransitionEvent extends Event {
	private Destination destination;
	
	public TransitionEvent(Destination dest) {
		this.destination = dest;
	}
	
	@Override
	public void begin() {
		super.begin();

		game.getPlayer().getInventory().interruptItems();
		
		game.getFrame().leave();
		Frame frame = destination.getFrame();
		
		game.getWorld().setCurrentLevel(frame.getLevel());
		game.getLevel().setCurrentFrame(frame);
		game.getPlayer().getPosition().set(destination
				.getPosition(game.getPlayer().getPosition()));
		
		frame.enter();

		game.addEntity(game.getPlayer());
		game.getPlayer().recordFrameEnterPosition();
		game.getPlayer().setDestroyed(false);
		game.getPlayer().onEnterFrame();
		game.getView().centerAt(game.getPlayer());
		
		end();
	}
}
