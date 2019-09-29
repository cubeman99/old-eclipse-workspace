package com.base.game.wolf3d.event;

public class EventWait extends Event {
	private float timer;
	private float waitTime;
	
	public EventWait(float waitTime) {
		this.waitTime = waitTime;
	}
	
	@Override
	public void begin() {
		timer = 0;
	}
	
	@Override
	public void update(float delta) {
		timer += delta;
		if (timer >= waitTime)
			end();
	}
}
