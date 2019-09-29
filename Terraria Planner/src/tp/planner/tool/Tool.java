package tp.planner.tool;

import java.awt.Cursor;
import java.awt.Graphics;
import tp.main.ImageLoader;
import tp.main.Mouse;
import tp.main.Keyboard.Key;
import tp.planner.Control;

public abstract class Tool {
	protected Control control;
	private String name;
	private String hotKeyName;
	private Key hotKey;
	private Cursor cursor;
	
	public Tool(String name, String hotKeyName, int hotKeyCode) {
		this.name       = name;
		this.hotKeyName = hotKeyName;
		this.hotKey     = new Key(hotKeyCode);
		this.cursor     = Mouse.createCustomCursor(ImageLoader.loadImage(name + " Cursor.png"));
	}
	
	public boolean isHotKeyPressed() {
		return hotKey.pressed();
	}
	
	public String getName() {
		return name;
	}
	
	public String getHotKeyName() {
		return hotKeyName;
	}
	
	public Cursor getCursor() {
		return cursor;
	}
	
	public void setControl(Control control) {
		this.control = control;
	}
	
	
	public void onStart() {}
	public void onFinish() {}
	
	public abstract void update() ;
	
	public void draw(Graphics g) {}
}
