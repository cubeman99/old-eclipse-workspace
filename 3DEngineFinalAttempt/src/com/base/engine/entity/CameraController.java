package com.base.engine.entity;

import com.base.engine.common.Vector2f;
import com.base.engine.common.Vector3f;
import com.base.engine.core.Keyboard;
import com.base.engine.core.Mouse;
import com.base.engine.rendering.Window;

public class CameraController extends SceneObject {
	private static final Vector3f yAxis = new Vector3f(0,1,0);
	
	private boolean mouseLocked;
	private float sensitivity;
	private int unlockMouseKey;
	private float speed;
	private int forwardKey;
	private int backKey;
	private int leftKey;
	private int rightKey;
	

	
	public CameraController(float speed, float sensitivity) {
		this(speed, sensitivity, Keyboard.KEY_ESCAPE, Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D);
	}

	public CameraController(float speed, float sensitivity, int unlockMouseKey, int forwardKey, int backKey, int leftKey, int rightKey) {
		this.speed          = speed;
		this.forwardKey     = forwardKey;
		this.backKey        = backKey;
		this.leftKey        = leftKey;
		this.rightKey       = rightKey;
		this.sensitivity    = sensitivity;
		this.unlockMouseKey = unlockMouseKey;
		this.mouseLocked    = false;
	}

	private void move(Vector3f dir, float amt) {
		getTransform().getPosition().add(dir.times(amt));
	}

	
	@Override
	public void update(float delta) {
		// Update Movement
		float movAmt = speed * delta;

		if(Keyboard.isKeyDown(Keyboard.KEY_E))
			getTransform().getPosition().y += movAmt;
		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
			getTransform().getPosition().y -= movAmt;
		if(Keyboard.isKeyDown(forwardKey))
			move(getTransform().getRotation().getForward(), movAmt);
		if(Keyboard.isKeyDown(backKey))
			move(getTransform().getRotation().getForward(), -movAmt);
		if(Keyboard.isKeyDown(leftKey))
			move(getTransform().getRotation().getLeft(), movAmt);
		if(Keyboard.isKeyDown(rightKey))
			move(getTransform().getRotation().getRight(), movAmt);
		
		// Update Look
		Vector2f centerPosition = new Vector2f(Window.getWidth()/2, Window.getHeight()/2);
		

		if (mouseLocked) {
			Vector2f deltaPos = Mouse.getPosition().minus(centerPosition);
			boolean rotH = (deltaPos.getX() != 0);
			boolean rotV = (deltaPos.getY() != 0);

			if(rotH)
				getTransform().rotate(yAxis, (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if(rotV)
				getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(deltaPos.getY() * sensitivity));

			if(rotH || rotV)
				Mouse.setMousePosition(centerPosition);

			if (Keyboard.isKeyDown(unlockMouseKey) || !Mouse.left.down()) {
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
