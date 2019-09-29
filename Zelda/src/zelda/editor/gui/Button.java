package zelda.editor.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.geometry.Rectangle;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.main.Keyboard;
import zelda.main.Keyboard.Key;
import zelda.main.Mouse;


public class Button {
	private Rectangle rect;
	private Panel panel;
	private boolean pressing;
	private boolean pressed;
	private boolean hovering;
	private String text;
	private Point iconSourcePos;
	private boolean toggled;
	private boolean toggles;
	private BufferedImage imageDisabled;
	private boolean enabled;
	private Key hotkey;
	private Key[] hotkeyReqs;



	// ================== CONSTRUCTORS ================== //

	public Button(Panel panel, String text, Point sourcePos, Point drawPos) {
		this.panel = panel;
		this.rect = new Rectangle(drawPos, new Point(24, 24));
		this.text = text;
		this.pressing = false;
		this.pressed = false;
		this.hovering = false;
		this.iconSourcePos = new Point(sourcePos);
		this.toggled = false;
		this.toggles = false;
		this.enabled = true;
		this.hotkey = null;
		this.hotkeyReqs = new Key[0];

		BufferedImage img = new BufferedImage(24, 24,
				BufferedImage.TYPE_INT_ARGB);
		Draw.setGraphics(img.getGraphics());
		Draw.setColor(Color.LIGHT_GRAY);
		Draw.fillRect(new Rectangle(0, 0, 24, 24));
		Draw.drawImage(Resources.SHEET_EDITOR_BUTTONS, iconSourcePos,
				new Vector());

		imageDisabled = new BufferedImage(20, 20, BufferedImage.TYPE_BYTE_GRAY);
		Draw.setGraphics(imageDisabled.getGraphics());
		Draw.drawImage(img, -2, -2);
		Draw.setColor(new Color(192, 192, 192, 180));
		Draw.fillRect(new Rectangle(0, 0, 20, 20));
	}



	// =================== ACCESSORS =================== //

	public String getText() {
		return text;
	}

	public boolean isHovering() {
		return hovering;
	}

	public boolean isPressed() {
		return pressed;
	}

	public boolean isToggled() {
		return toggled;
	}

	public boolean isEnabled() {
		return enabled;
	}



	// ==================== MUTATORS ==================== //

	public void update() {
		Point ms = panel.getMousePos();
		pressed = false;
		hovering = false;

		if (!enabled) {
			pressed = false;
		}
		else if (pressing) {
			if (!Mouse.left.down()) {
				pressing = false;
				if (rect.contains(ms)) {
					pressed = true;
					if (toggles)
						toggle();
				}
			}
		}
		else if (isHotkeyPressed()) {
			if (toggles)
				toggle();
			else
				pressed = true;
		}
		else if (panel.containsMouse() && rect.contains(ms)) {
			hovering = true;
			panel.editor.setToolTip(text, panel.position.x + rect.getX1() + 8,
					panel.position.y + rect.getY2() + 10);
			
			if (Mouse.left.pressed()) {
				pressing = true;
			}
		}
	}

	public Button toggle() {
		toggled = !toggled;
		return this;
	}

	public void setToggled(boolean toggled) {
		this.toggled = toggled;
	}

	public Button setHotkeys(int hotkeyCode, Key... hotkeyReqs) {
		this.hotkey = new Key(hotkeyCode);
		this.hotkeyReqs = hotkeyReqs;
		return this;
	}

	public void setToggles(boolean toggles) {
		this.toggles = toggles;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void enable() {
		if (!enabled)
			setEnabled(true);
	}

	public void disable() {
		if (enabled)
			setEnabled(false);
	}

	public void draw() {
		Point ms = panel.getMousePos();
		boolean sink = toggled || pressed
				|| (pressing && panel.containsMouse() && rect.contains(ms));
		Point sp = new Point(sink ? 1 : 0, 0);
		Vector add = (sink ? new Vector(1, 0) : new Vector());

		if (!enabled) {
			Draw.drawImage(Resources.SHEET_EDITOR_BUTTONS, new Point(2, 0),
					new Vector(rect.corner));
			Draw.drawImage(imageDisabled, rect.corner.plus(2, 2));
		}
		else {
			Draw.drawImage(Resources.SHEET_EDITOR_BUTTONS, sp, new Vector(
					rect.corner));
			Draw.drawImage(Resources.SHEET_EDITOR_BUTTONS, iconSourcePos,
					new Vector(rect.corner).plus(add));
		}
	}

	private boolean isHotkeyPressed() {
		if (hotkey == null || !hotkey.pressed())
			return false;
		if (!hasHotKeyReq(Keyboard.control) && Keyboard.control.down())
			return false;
		if (!hasHotKeyReq(Keyboard.shift) && Keyboard.shift.down())
			return false;
		if (!hasHotKeyReq(Keyboard.alt) && Keyboard.alt.down())
			return false;
		for (Key k : hotkeyReqs) {
			if (!k.down())
				return false;
		}
		return true;
	}

	private boolean hasHotKeyReq(Key key) {
		for (Key k : hotkeyReqs) {
			if (k == key)
				return true;
		}
		return false;
	}
}
