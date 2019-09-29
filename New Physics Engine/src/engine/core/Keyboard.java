package engine.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * A static key listener class that handles key states with custom key bindings.
 * 
 * @author David Jordan
 */
public class Keyboard implements KeyListener {
	public static final int NUM_ARROW_KEYS = 4;
	public static final int NUM_ACTION_KEYS = 2;

	// Game Keys.
	public static Key restart     = new Key(KeyEvent.VK_R);
	public static final Key up    = new Key(KeyEvent.VK_W, KeyEvent.VK_UP);
	public static final Key down  = new Key(KeyEvent.VK_S, KeyEvent.VK_DOWN);
	public static final Key left  = new Key(KeyEvent.VK_A, KeyEvent.VK_LEFT);
	public static final Key right = new Key(KeyEvent.VK_D, KeyEvent.VK_RIGHT);
	public static final Key[] arrows = {right, up, left, down};
	
	// Regular Keys.
	public static Key space     = new Key(KeyEvent.VK_SPACE);
	public static Key enter     = new Key(KeyEvent.VK_ENTER);
	public static Key control   = new Key(KeyEvent.VK_CONTROL);
	public static Key shift     = new Key(KeyEvent.VK_SHIFT);
	public static Key alt       = new Key(KeyEvent.VK_ALT);
	public static Key backspace = new Key(KeyEvent.VK_BACK_SPACE);
	public static Key escape    = new Key(KeyEvent.VK_ESCAPE);
	
	public static Key insert   = new Key(KeyEvent.VK_INSERT);
	public static Key delete   = new Key(KeyEvent.VK_DELETE);
	public static Key home     = new Key(KeyEvent.VK_HOME);
	public static Key end      = new Key(KeyEvent.VK_END);
	public static Key pageUp   = new Key(KeyEvent.VK_PAGE_UP);
	public static Key pageDown = new Key(KeyEvent.VK_PAGE_DOWN);
	
	public static Key F1       = new Key(KeyEvent.VK_F1);
	public static Key F2       = new Key(KeyEvent.VK_F2);
	public static Key F3       = new Key(KeyEvent.VK_F3);
	public static Key F4       = new Key(KeyEvent.VK_F4);
	public static Key F5       = new Key(KeyEvent.VK_F5);
	public static Key F6       = new Key(KeyEvent.VK_F6);
	public static Key F7       = new Key(KeyEvent.VK_F7);
	public static Key F8       = new Key(KeyEvent.VK_F8);
	public static Key F9       = new Key(KeyEvent.VK_F9);
	public static Key F10      = new Key(KeyEvent.VK_F10);
	public static Key F11      = new Key(KeyEvent.VK_F11);
	public static Key F12      = new Key(KeyEvent.VK_F12);



	/** Sub-class that represents a key that can have multiple bindings **/
	public static class Key {
		private int[] keyCodes;

		/** Initialize a key with any amount of key code bindings. **/
		public Key(int... keyCodes) {
			this.keyCodes = new int[keyCodes.length];
			for (int i = 0; i < keyCodes.length; i++)
				this.keyCodes[i] = keyCodes[i];
		}

		public int[] getKeyCodes() {
			return keyCodes;
		}

		/** Check if the key is down. **/
		public boolean down() {
			for (int k : keyCodes) {
				if (Keyboard.keyDown[k])
					return true;
			}
			return false;
		}

		/** Check if the key was pressed. **/
		public boolean pressed() {
			boolean down = false;
			boolean downPrev = false;
			for (int k : keyCodes) {
				down = down || Keyboard.keyDown[k];
				downPrev = downPrev || Keyboard.keyDownPrev[k];
			}
			return (down && !downPrev);
		}

		/** Check if the key was released. **/
		public boolean released() {
			boolean down = false;
			boolean downPrev = false;
			for (int k : keyCodes) {
				down = down || Keyboard.keyDown[k];
				downPrev = downPrev || Keyboard.keyDownPrev[k];
			}
			return (!down && downPrev);
		}

		/** Check if the key was typed. **/
		public boolean typed() {
			for (int k : keyCodes) {
				if (keyTyped[k])
					return true;
			}
			return false;
		}

		public void clear() {
			for (int k : keyCodes) {
				keyDown[k] = false;
				keyDownPrev[k] = false;
				rawKeyDown[k] = false;
			}
		}

		@Override
		public String toString() {
			String str = "";
			for (int i = 0; i < keyCodes.length; i++) {
				str += keyCodes[i] + "=" + (down() ? "1" : "0") + ", ";
			}
			return str;
		}
	}


	/** Initialize all key code states. **/
	public Keyboard() {
		for (int i = 0; i < KEYCODE_SIZE; i++) {
			rawKeyDown[i] = false;
			keyDown[i] = false;
			keyDownPrev[i] = false;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (e.getKeyCode() < KEYCODE_SIZE)
			rawKeyDown[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() < KEYCODE_SIZE) {
			rawKeyDown[e.getKeyCode()] = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() < KEYCODE_SIZE)
			rawKeyTyped[e.getKeyCode()] = true;
	}

	/** Update all key states. **/
	public static void update() {
		for (int i = 0; i < KEYCODE_SIZE; i++) {
			keyDownPrev[i] = keyDown[i];
			keyDown[i] = rawKeyDown[i];
			keyTypedPrev[i] = keyTyped[i];
			keyTyped[i] = rawKeyTyped[i];
			rawKeyTyped[i] = false;
		}
	}

	public static void clear() {
		for (int i = 0; i < KEYCODE_SIZE; i++) {
			rawKeyDown[i] = false;
		}
	}


	private static final int KEYCODE_SIZE = 0x108; // The maximum amount of keys
												   // to keep track of.
	private static boolean[] rawKeyDown = new boolean[KEYCODE_SIZE];
	private static boolean[] keyDown = new boolean[KEYCODE_SIZE];
	private static boolean[] keyDownPrev = new boolean[KEYCODE_SIZE];
	private static boolean[] rawKeyTyped = new boolean[KEYCODE_SIZE];
	private static boolean[] keyTyped = new boolean[KEYCODE_SIZE];
	private static boolean[] keyTypedPrev = new boolean[KEYCODE_SIZE];
}
