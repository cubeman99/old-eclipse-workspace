package com.base.game.wolf3d.event;

import com.base.engine.common.Vector3f;
import com.base.engine.rendering.Draw2D;
import com.base.engine.rendering.Window;

public class EventScreenFade extends Event {
	/** Go from opaque to transparent. **/
	public static final int FADE_IN  = 0;
	
	/** Go from transparent to opaque. **/
	public static final int FADE_OUT = 1;
	
	protected static final int DEFAULT_FADE_TIME  = 20;
	protected static final Vector3f DEFAULT_COLOR = new Vector3f(1, 1, 1); // default white
	
	protected Vector3f color;
	protected int type;
	protected float fadeTime;
	protected float timer;



	public EventScreenFade(int type) {
		this(DEFAULT_FADE_TIME, DEFAULT_COLOR, type);
	}
	
	public EventScreenFade(Vector3f color, int type) {
		this(DEFAULT_FADE_TIME, color, type);
	}

	public EventScreenFade(float fadeTime, Vector3f color, int type) {
		super();
		this.color    = color;
		this.type     = type;
		this.fadeTime = fadeTime;
	}
	
	@Override
	public void begin() {
		super.begin();
		timer = 0;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		timer += delta;
		if (timer > fadeTime)
			end();
	}

	@Override
	public void render() {
		super.render();
//		int alpha = (int) (color.getAlpha() * Math.min(1, 1.0 * (t / fadeTime))); // TODO: Alpha in colors 
		
		float t = (type == FADE_IN ? fadeTime - timer : timer);
		float alpha = 1 * Math.min(1, 1 * (t / fadeTime));
		
		Draw2D.setColor(color, alpha);
		Draw2D.fillRect(0, 0, Window.getWidth(), Window.getHeight());
	}
}
