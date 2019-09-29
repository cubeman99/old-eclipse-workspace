package com.base.game.jets;

import com.base.engine.common.Vector3f;
import com.base.engine.core.Keyboard;
import com.base.engine.entity.SceneObject;

public class JetController extends SceneObject {
	private float speed;
	private int keyAccelerate;
	private int keyDecelerate;
	private int keyNoseUp;
	private int keyNoseDown;
	private int keyRollLeft;
	private int keyRollRight;
	private int keyTurnLeft;
	private int keyTurnRight;
	

	public JetController(float speed) {
		this.speed      = speed;
		this.keyAccelerate = Keyboard.KEY_W;
		this.keyDecelerate = Keyboard.KEY_S;
		this.keyNoseUp     = Keyboard.KEY_UP;
		this.keyNoseDown   = Keyboard.KEY_DOWN;
		this.keyRollLeft   = Keyboard.KEY_LEFT;
		this.keyRollRight  = Keyboard.KEY_RIGHT;
		this.keyTurnLeft   = Keyboard.KEY_A;
		this.keyTurnRight  = Keyboard.KEY_D;
	}

	private void move(Vector3f dir, float amt) {
		getTransform().getPosition().add(dir.times(amt));
	}

	@Override
	public void update(float delta) {
		float movAmt     = speed * delta;
		float tiltAmount = 0.02f;
		float rollAmount = 0.04f;
		float turnAmount = 0.04f;

		if(Keyboard.isKeyDown(keyAccelerate))
			move(getTransform().getRotation().getForward(), movAmt);
		if(Keyboard.isKeyDown(keyDecelerate))
			move(getTransform().getRotation().getForward(), -movAmt);

		if(Keyboard.isKeyDown(keyNoseUp))
			getTransform().rotate(getTransform().getRotation().getLeft(), -tiltAmount);
		if(Keyboard.isKeyDown(keyNoseDown))
			getTransform().rotate(getTransform().getRotation().getLeft(), tiltAmount);

		if(Keyboard.isKeyDown(keyTurnLeft))
			getTransform().rotate(getTransform().getRotation().getUp(), -turnAmount);
		if(Keyboard.isKeyDown(keyTurnRight))
			getTransform().rotate(getTransform().getRotation().getUp(), turnAmount);
		
		if(Keyboard.isKeyDown(keyRollLeft))
			getTransform().rotate(getTransform().getRotation().getForward(), rollAmount);
		if(Keyboard.isKeyDown(keyRollRight))
			getTransform().rotate(getTransform().getRotation().getForward(), -rollAmount);
	}
}
