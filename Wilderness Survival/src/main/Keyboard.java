package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * A static key listener class that handles
 * key states with custom key bindings.
 * 
 * @author David Jordan
 */
public class Keyboard implements KeyListener {
	
	// CUSTOM KEY BINDINGS:
	public static Key escape	= new Key(KeyEvent.VK_ESCAPE);
	
	
	
	/** Sub-class that represents a key that can have multiple bindings **/
	public static class Key {
		private int[] keyCodes;
		
		/** Initialize a key with any amount of key code bindings. **/
		public Key(int... keyCodes) {
			this.keyCodes = new int[keyCodes.length];
			for (int i = 0; i < keyCodes.length; i++)
				this.keyCodes[i] = keyCodes[i];
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
			boolean down     = false;
			boolean downPrev = false;
			for (int k : keyCodes) {
				down     = down || Keyboard.keyDown[k];
				downPrev = downPrev || Keyboard.keyDownPrev[k];
			}
			return (down && !downPrev);
		}

		/** Check if the key was released. **/
		public boolean released() {
			boolean down     = false;
			boolean downPrev = false;
			for (int k : keyCodes) {
				down     = down || Keyboard.keyDown[k];
				downPrev = downPrev || Keyboard.keyDownPrev[k];
			}
			return (!down && downPrev);
		}

		/** Check if the key was typed. **/
		public boolean typed() {
			for (int k : keyCodes) {
				if (Keyboard.keyTyped[k])
					return true;
			}
			return false;
		}
	}
	

	/** Initialize all key code states. **/
	public Keyboard() {
		for (int i = 0; i < KEYCODE_SIZE; i++) {
			rawKeyDown[i]  = false;
			keyDown[i]     = false;
			keyDownPrev[i] = false;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		rawKeyDown[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		rawKeyDown[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		rawKeyTyped[e.getKeyCode()] = true;
	}
	
	/** Update all key states. **/
	public static void update() {
		for (int i = 0; i < KEYCODE_SIZE; i++) {
			keyDownPrev[i]  = keyDown[i];
			keyDown[i]      = rawKeyDown[i];
			keyTypedPrev[i] = keyTyped[i];
			keyTyped[i]     = rawKeyTyped[i];
			rawKeyTyped[i]  = false;
		}
	}
	
	// Static package-private key arrays:
	static final int KEYCODE_SIZE = 0x108; // The maximum amount of keys to keep track of.
	static boolean[] rawKeyDown   = new boolean[KEYCODE_SIZE];
	static boolean[] keyDown      = new boolean[KEYCODE_SIZE];
	static boolean[] keyDownPrev  = new boolean[KEYCODE_SIZE];
	static boolean[] rawKeyTyped  = new boolean[KEYCODE_SIZE];
	static boolean[] keyTyped     = new boolean[KEYCODE_SIZE];
	static boolean[] keyTypedPrev = new boolean[KEYCODE_SIZE];
}
