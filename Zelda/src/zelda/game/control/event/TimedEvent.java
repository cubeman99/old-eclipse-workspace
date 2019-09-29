package zelda.game.control.event;

public class TimedEvent extends Event {
	protected int timer;
	protected int time;

	public TimedEvent(int time) {
		super();
		this.time = time;
	}

	@Override
	public void begin() {
		super.begin();
		timer = 0;
	}

	@Override
	public void update() {
		super.update();
		if (timer++ > time)
			end();
	}
}
