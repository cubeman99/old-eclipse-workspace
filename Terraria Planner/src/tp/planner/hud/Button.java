package tp.planner.hud;

import java.awt.Image;
import tp.common.Point;
import tp.main.Keyboard.Key;
import tp.planner.Control;

public class Button {
	protected Control control;
	protected Image image;
	private String name;
	private String hotKeyText;
	private Key hotKey;
	private Key[] maskKeys;
	private int toggleIndex;
	private int totalToggles;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Button(String name, String hotKeyText, int hotKeyCode) {
		this(name, 1, hotKeyText, hotKeyCode);
	}
	
	public Button(String name, int totalToggles, String hotKeyText, int hotKeyCode) {
		this.name         = name;
		this.hotKeyText   = hotKeyText;
		this.hotKey       = (hotKeyCode >= 0 ? new Key(hotKeyCode) : null);
		this.maskKeys     = new Key[] {};
		this.totalToggles = totalToggles;
		this.toggleIndex  = 0;
	}
	
	public Button(String name, int totalToggles, String hotKeyText, int hotKeyCode, int maskKeyCode) {
		this(name, totalToggles, hotKeyText, hotKeyCode);
		if (maskKeyCode >= 0)
			this.maskKeys = new Key[] {new Key(maskKeyCode)};
	}
	
	public Button(String name, String hotKeyText, int hotKeyCode, int maskKeyCode) {
		this(name, 1, hotKeyText, hotKeyCode, maskKeyCode);
	}
	
	public Button(String name) {
		this(name, 1, "", -1, -1);
	}
	
	public Button(String name, int totalToggles) {
		this(name, totalToggles, "", -1, -1);
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public int getToggleIndex() {
		return toggleIndex;
	}
	
	public String getName() {
		return name;
	}
	
	public String getHotKeyText() {
		return hotKeyText;
	}
	
	public boolean isKeyPressed() {
		if (hotKey == null)
			return false;
		for (Key maskKey : maskKeys) {
			if (!maskKey.down())
				return false;
		}
		return hotKey.pressed();
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setControl(Control control) {
		this.control = control;
	}
	
	public void press() {
		toggleIndex++;
		if (toggleIndex >= totalToggles)
			toggleIndex = 0;
		onPress();
	}
	
	public void onPress() {}
	
	
	public void draw(Point pos) {
		
//		control.hud.drawIconCentered(image, 0, pos.x, pos.y);
	}
}
