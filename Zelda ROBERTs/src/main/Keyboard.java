package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

/**
 * A static key listener class that handles key states with custom
 * key bindings.
 * @author	David Jordan
 * @author	Robert Jordan
 */
public class Keyboard implements KeyListener, WindowFocusListener {
	
	// =================== Key Definitions ====================
	
	// Gameboy buttons
	public static Key up		= new Key(KeyEvent.VK_UP);
	public static Key down		= new Key(KeyEvent.VK_DOWN);
	public static Key left		= new Key(KeyEvent.VK_LEFT);
	public static Key right		= new Key(KeyEvent.VK_RIGHT);
	public static Key a			= new Key(KeyEvent.VK_Z);
	public static Key b			= new Key(KeyEvent.VK_X);
	public static Key x			= new Key(KeyEvent.VK_C);
	public static Key y			= new Key(KeyEvent.VK_V);
	public static Key z			= new Key(KeyEvent.VK_D);
	public static Key l			= new Key(KeyEvent.VK_A);
	public static Key r			= new Key(KeyEvent.VK_S);
	public static Key start		= new Key(KeyEvent.VK_ENTER);
	public static Key select	= new Key(KeyEvent.VK_BACK_SLASH);
	
	// Debug keys
	public static Key debug			= new Key(KeyEvent.VK_F3);
	public static Key screenshot	= new Key(KeyEvent.VK_F12);
	
	public static Key control		= new Key(KeyEvent.VK_CONTROL);
	public static Key shift			= new Key(KeyEvent.VK_SHIFT);
	public static Key alt			= new Key(KeyEvent.VK_ALT);
	
	public static Key space			= new Key(KeyEvent.VK_SPACE);
	public static Key backspace		= new Key(KeyEvent.VK_BACK_SPACE);
	public static Key tab			= new Key(KeyEvent.VK_TAB);
	public static Key escape		= new Key(KeyEvent.VK_ESCAPE);

	public static Key insert		= new Key(KeyEvent.VK_INSERT);
	public static Key delete		= new Key(KeyEvent.VK_DELETE);
	public static Key home			= new Key(KeyEvent.VK_HOME);
	public static Key end			= new Key(KeyEvent.VK_END);
	public static Key pageUp		= new Key(KeyEvent.VK_PAGE_UP);
	public static Key pageDown		= new Key(KeyEvent.VK_PAGE_DOWN);

	public static Key equals		= new Key(KeyEvent.VK_EQUALS);
	public static Key minus			= new Key(KeyEvent.VK_MINUS);
	
	// ====================== Variables =======================
	
	private static final int	NUM_KEYS		= 0x108;
	
	private static byte[]		keyState		= new byte[NUM_KEYS];
	private static boolean[]	rawKeyState		= new boolean[NUM_KEYS];
	private static boolean[]	keyTyped		= new boolean[NUM_KEYS];
	private static boolean[]	rawKeyTyped		= new boolean[NUM_KEYS];

	private static char			charTyped		= '\0';
	private static char			rawCharTyped	= '\0';
	
	// ===================== Constructors =====================
	
	/** Constructs the listener by setting the key states. */
	public Keyboard() {
		for (int i = 0; i < NUM_KEYS; i++) {
			keyState[i]		= 0;
			rawKeyState[i]	= false;
			keyTyped[i]		= false;
			rawKeyTyped[i]	= false;
		}
	}

	// =================== Implementations ====================
	
	/** Called whenever a key is pressed. */
	public void keyPressed(KeyEvent e) {
		rawKeyState[e.getKeyCode()] = true;
		rawKeyTyped[e.getKeyCode()] = true;
	}
	/** Called whenever a key is released. */
	public void keyReleased(KeyEvent e) {
		rawKeyState[e.getKeyCode()] = false;
	}
	/** Called whenever a key is typed. */
	public void keyTyped(KeyEvent e) {
		rawCharTyped = e.getKeyChar();
		rawKeyTyped[e.getKeyCode()] = true;
	}
	/** Called whenever the window gains focus. */
	public void windowGainedFocus(WindowEvent e) {
		
	}
	/** Called whenever the window loses focus. */
	public void windowLostFocus(WindowEvent e) {
		// Reset all key states:
		for (int i = 0; i < NUM_KEYS; i++) {
			rawKeyState[i]	= false;
			rawKeyTyped[i]	= false;
		}
		rawCharTyped = '\0';
	}

	// ======================= Updating =======================
	
	/** Called every step to update the key states. */
	public static void update() {
		// Update each of the key states:
		for (int i = 0; i < NUM_KEYS; i++) {
			// Update key typed:
			keyTyped[i]     = rawKeyTyped[i];
			rawKeyTyped[i]  = false;
			
			// Update key state:
			if (rawKeyState[i]) {
				if (keyState[i] <= 1)
					keyState[i] = 3;
				else
					keyState[i] = 2;
			}
			else {
				if (keyState[i] >= 2)
					keyState[i] = 1;
				else
					keyState[i] = 0;
			}
		}
		
		// Update char typed:
		charTyped		= rawCharTyped;
		rawCharTyped	= '\0';
	}
	/** Called when the key states need to be reset to the default. */
	public static void reset() {
		for (int i = 0; i < NUM_KEYS; i++) {
			rawKeyState[i]	= false;
			rawKeyTyped[i]	= false;
			rawCharTyped	= '\0';
		}
	}

	// ====================== Key Events ======================

	/** Returns a key class with the given key codes. */
	public static Key getKey(int... keyCodes) {
		return new Key(keyCodes);
	}
	/** Tests whether a character was typed this step. */
	public static boolean isCharTyped() {
		return charTyped != '\0';
	}
	/** Returns the character that was typed this step. */
	public static char getTypedChar() {
		return charTyped;
	}
	
	// ==================== Key Sub-Class =====================
	
	/** A sub-class that represents a key that can have multiple bindings. */
	public static class Key {
		
		// ======================= Members ========================
		
		/** The list of key codes that can represent the key. */
		private int[] keyCodes;
		
		// ===================== Constructors =====================
		
		/** Initialize a key with any amount of key bindings. */
		public Key(int... keyCodes) {
			this.keyCodes = new int[keyCodes.length];
			for (int i = 0; i < keyCodes.length; i++)
				this.keyCodes[i] = keyCodes[i];
		}

		// ====================== Key States ======================
		
		/** Returns true if the key was pressed. */
		public boolean pressed() {
			// Only true if no other key is down
			boolean pressed = false;
			for (int k : keyCodes) {
				if (keyState[k] == 3)
					pressed = true;
				else if (keyState[k] == 2)
					return false;
			}
			return pressed;
		}
		/** Returns true if the key is down. */
		public boolean down() {
			// True if any key is down
			for (int k : keyCodes) {
				if (keyState[k] >= 2)
					return true;
			}
			return false;
		}
		/** Returns true if the key was released. */
		public boolean released() {
			// Only true if every other key is up or released
			boolean released = false;
			for (int k : keyCodes) {
				if (keyState[k] == 1)
					released = true;
				else if (keyState[k] >= 2)
					return false;
			}
			return released;
		}
		/** Returns true if the key is up. */
		public boolean up() {
			// Only true if every other key is up or released
			for (int k : keyCodes) {
				if (keyState[k] >= 2)
					return false;
			}
			return true;
		}
		/** Returns true if the key was typed. */
		public boolean typed() {
			// True if any key is typed
			for (int k : keyCodes) {
				if (keyTyped[k])
					return true;
			}
			return false;
		}
	}
}