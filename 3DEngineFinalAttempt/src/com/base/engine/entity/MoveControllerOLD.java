package com.base.engine.entity;

import com.base.engine.common.Vector3f;
import com.base.engine.core.Keyboard;

public class MoveControllerOLD extends ObjectComponent {
	private float speed;
	private int forwardKey;
	private int backKey;
	private int leftKey;
	private int rightKey;

	public MoveControllerOLD(float speed) {
		this(speed, Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D);
	}

	public MoveControllerOLD(float speed, int forwardKey, int backKey, int leftKey, int rightKey) {
		this.speed      = speed;
		this.forwardKey = forwardKey;
		this.backKey    = backKey;
		this.leftKey    = leftKey;
		this.rightKey   = rightKey;
	}

	private void move(Vector3f dir, float amt) {
		getTransform().getPosition().add(dir.times(amt));
	}

	@Override
	public void input(float delta) {
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
	}
}
