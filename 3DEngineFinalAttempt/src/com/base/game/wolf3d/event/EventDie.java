package com.base.game.wolf3d.event;

import com.base.engine.common.GMath;
import com.base.engine.common.Quaternion;
import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.rendering.Draw2D;
import com.base.game.wolf3d.Game;

public class EventDie extends EventQueue {
	private Vector2f killerPosition;
	
	public EventDie(Vector2f killerPos) {
		this.killerPosition = killerPos;
		
		// Look at killer.
		addEvent(new Event() {
			private float timer;
			private Quaternion startRot;
			private Quaternion goalRot;
			private float speed;
			
			@Override
			public void begin() {
				Vector2f dirGoal = killerPosition.minus(getGame().getPlayer().getPosition()).normalize();
				
				goalRot  = new Quaternion().setDirection(new Vector3f(dirGoal.x, 0, dirGoal.y));
				startRot = new Quaternion(getGame().getPlayer().getTransform().getRotation());
				timer    = 0;
				
				float angularSpeed = GMath.HALF_PI;
				
				float angle = startRot.angleTo(goalRot);
				if (angle > 0.001) {
    				speed = angularSpeed / angle; // Radians per second
				}
				else
					end();
			}
			
			@Override
			public void update(float delta) {
				timer += delta * speed;
				
				getGame().getPlayer().getTransform().setRotation(startRot.nlerp(goalRot, timer, true));
				
				if (timer >= 1)
					end();
			}
		});
		
		// Short pause.
		addEvent(new EventWait(0.5f));
		
		// Fade screen to red.
		addEvent(new EventScreenFade(1.5f, Draw2D.RED, EventScreenFade.FADE_OUT));
		
		// Restart level.
		addEvent(new Event() {
			@Override
			public void begin() {
				getGame().restartLevel();
				end();
			}
		});
	}
}
