package zelda.main;

import java.awt.event.KeyEvent;
import zelda.main.Keyboard.Key;


public class Hotkey {
	public static final int NONE = 0;
	public static final int CONTROL = 0;
	public static final int SHIFT = 0;
	public static final int ALT = 0;

	private static final String[] MODIFIER_NAMES = {"", "Ctrl", "Shift", "Alt"};
	private static final Key[] MODIFIER_KEYS = {null, Keyboard.control,
			Keyboard.shift, Keyboard.alt};

	private Key key;
	private int[] modifiers;



	// ================== CONSTRUCTORS ================== //

	public Hotkey(int keyCode, int... modifiers) {
		this(new Key(keyCode), modifiers);
	}

	public Hotkey(Key key, int... modifiers) {
		this.key = key;
		this.modifiers = modifiers;
	}



	// =================== ACCESSORS =================== //

	public boolean down() {
		return (key.down() && modifiersDown());
	}

	public boolean pressed() {
		return (key.down() && modifiersDown());
	}

	public String getHotkeyText() {
		String str = "";
		for (int i = 0; i < modifiers.length; i++)
			str += MODIFIER_NAMES[modifiers[i]] + "+";
		return (str + KeyEvent.getKeyText(key.getKeyCodes()[0]));
	}

	private boolean modifiersDown() {
		for (int i = 0; i < modifiers.length; i++) {
			if (!MODIFIER_KEYS[modifiers[i]].down())
				return false;
		}
		return true;
	}
}
