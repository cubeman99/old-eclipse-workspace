package com.base.game.editor.handle;

import com.base.engine.common.Rect2f;
import com.base.engine.common.Vector2f;

public class ScaleHandle {
	private Vector2f position;
	
	public ScaleHandle(float posX, float posY) {
		position = new Vector2f(posX, posY);
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getPosition(Rect2f boundingBox) {
		return boundingBox.getPosition().plus(position.times(boundingBox.getSize()));
	}
}
