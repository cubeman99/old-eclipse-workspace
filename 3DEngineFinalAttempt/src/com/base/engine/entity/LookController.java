package com.base.engine.entity;

import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Keyboard;
import com.base.engine.core.Mouse;
import com.base.engine.rendering.Window;

public class LookController extends SceneObject {
	private static final Vector3f yAxis = new Vector3f(0,1,0);

	private boolean mouseLocked = false;
	private float sensitivity;
	private int unlockMouseKey;

	public LookController(float sensitivity) {
		this(sensitivity, Keyboard.KEY_ESCAPE);
	}

	public LookController(float sensitivity, int unlockMouseKey) {
		this.sensitivity    = sensitivity;
		this.unlockMouseKey = unlockMouseKey;
	}

	@Override
	public void update(float delta) {
		Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);
		
		if (mouseLocked) {
			Vector2f deltaPos = Mouse.getPosition().minus(centerPosition);

			boolean rotY = (deltaPos.getX() != 0);
			boolean rotX = (deltaPos.getY() != 0);

			if(rotY)
				getTransform().rotate(yAxis, (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if(rotX)
				getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(deltaPos.getY() * sensitivity));

			if(rotY || rotX)
				Mouse.setMousePosition(centerPosition);
			
			if ((Keyboard.isKeyDown(unlockMouseKey) || !Mouse.left.down())) {
				Mouse.setCursorState(true);
				mouseLocked = false;
			}
		}
		else if (Mouse.left.pressed()) {
			Mouse.setMousePosition(centerPosition);
			Mouse.setCursorState(false);
			mouseLocked = true;
		}
	}
}
