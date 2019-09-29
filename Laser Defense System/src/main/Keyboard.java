package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * A static key listener class that handles
 * key states with custom key bindings.
 * 
 * @author David
 */
public class Keyboard implements KeyListener {
	
	// CUSTOM KEY BINDINGS:
	public static Key up		= new Key(KeyEvent.VK_W, KeyEvent.VK_UP);
	public static Key down		= new Key(KeyEvent.VK_S, KeyEvent.VK_DOWN);
	public static Key left		= new Key(KeyEvent.VK_A, KeyEvent.VK_LEFT);
	public static Key right		= new Key(KeyEvent.VK_D, KeyEvent.VK_RIGHT);
	
	public static Key space		= new Key(KeyEvent.VK_SPACE);
	public static Key backspace = new Key(KeyEvent.VK_BACK_SPACE);
	public static Key enter		= new Key(KeyEvent.VK_ENTER);
	public static Key control	= new Key(KeyEvent.VK_CONTROL);
	public static Key shift		= new Key(KeyEvent.VK_SHIFT);
	public static Key tab		= new Key(KeyEvent.VK_TAB);
	
	public static Key escape	= new Key(KeyEvent.VK_ESCAPE);
	public static Key restart	= new Key(KeyEvent.VK_R);
	
	
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
				if (keyTyped[k])
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
	
	
	private static final int KEYCODE_SIZE = 0x108;
	private static boolean[] rawKeyDown   = new boolean[KEYCODE_SIZE];
	private static boolean[] keyDown      = new boolean[KEYCODE_SIZE];
	private static boolean[] keyDownPrev  = new boolean[KEYCODE_SIZE];
	private static boolean[] rawKeyTyped  = new boolean[KEYCODE_SIZE];
	private static boolean[] keyTyped     = new boolean[KEYCODE_SIZE];
	private static boolean[] keyTypedPrev = new boolean[KEYCODE_SIZE];
}
