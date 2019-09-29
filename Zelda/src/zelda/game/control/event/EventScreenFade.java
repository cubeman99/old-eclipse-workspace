package zelda.game.control.event;

import java.awt.Color;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.graphics.Draw;


public class EventScreenFade extends Event {
	/** Go from opaque to transparent. **/
	public static final int FADE_IN  = 0;
	
	/** Go from transparent to opaque. **/
	public static final int FADE_OUT = 1;
	
	protected static final int DEFAULT_FADE_TIME = 20;
	protected static final Color DEFAULT_COLOR   = Color.WHITE;
	
	protected Color color;
	protected int type;
	protected int fadeTime;
	protected int timer;



	public EventScreenFade(int type) {
		this(DEFAULT_FADE_TIME, DEFAULT_COLOR, type);
	}
	
	public EventScreenFade(Color color, int type) {
		this(DEFAULT_FADE_TIME, color, type);
	}

	public EventScreenFade(int fadeTime, Color color, int type) {
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
	public void update() {
		super.update();
		timer++;
		if (timer > fadeTime)
			end();
	}

	@Override
	public void draw() {
		super.draw();
		Draw.setViewPosition(0, 0);
		
		int r = color.getRed();
		int g = color.getBlue();
		int b = color.getGreen();
		double t = (type == FADE_IN ? fadeTime - timer : timer);
		int a = (int) (color.getAlpha() * Math.min(1, 1.0 * (t / fadeTime)));
		
		Draw.setColor(new Color(r, g, b, a));
		Draw.fillRect(new Rectangle(new Point(), Settings.VIEW_SIZE.plus(0, 16)));
	}
}
