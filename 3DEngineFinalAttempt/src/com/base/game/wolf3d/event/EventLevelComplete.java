package com.base.game.wolf3d.event;

import com.base.engine.common.Vector3f;
import com.base.engine.rendering.Draw2D;

public class EventLevelComplete extends EventQueue {

	public EventLevelComplete() {
		float fadeTime = 0.6f; // 1.5f
		float waitTime = 0.2f; // 0.6f
		
		// Short pause.
		addEvent(new EventWait(waitTime));
		
		// Fade screen to black.
		addEvent(new EventScreenFade(fadeTime, Draw2D.BLACK, EventScreenFade.FADE_OUT));
		
		// Next level.
		addEvent(new Event() {
			@Override
			public void begin() {
				getGame().nextLevel();
				end();
			}
		});
		
		// Fade screen in from black.
		addEvent(new EventScreenFade(fadeTime, Draw2D.BLACK, EventScreenFade.FADE_IN));
	}
}
