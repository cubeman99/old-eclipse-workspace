package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Keyboard implements KeyListener {
	// DEFINE CUSTOM KEYS HERE:
	
	// Default keys:
	public static Key viewUp	= new Key(KeyEvent.VK_W);
	public static Key viewDown	= new Key(KeyEvent.VK_S);
	public static Key viewLeft	= new Key(KeyEvent.VK_A);
	public static Key viewRight	= new Key(KeyEvent.VK_D);
	
	public static Key moveUp	= new Key(KeyEvent.VK_UP);
	public static Key moveDown	= new Key(KeyEvent.VK_DOWN);
	public static Key moveLeft	= new Key(KeyEvent.VK_LEFT);
	public static Key moveRight	= new Key(KeyEvent.VK_RIGHT);
	
	public static Key space		= new Key(KeyEvent.VK_SPACE);
	public static Key enter		= new Key(KeyEvent.VK_ENTER);
	public static Key control	= new Key(KeyEvent.VK_CONTROL);
	public static Key shift		= new Key(KeyEvent.VK_SHIFT);
	public static Key tab		= new Key(KeyEvent.VK_TAB);
	public static Key escape	= new Key(KeyEvent.VK_ESCAPE);

	public static Key newCritter	= new Key(KeyEvent.VK_P);
	
	public static Key restart	= new Key(KeyEvent.VK_R);
	
	
	
	// Sub-class Key
	public static class Key {
		private int[] keyCodes;
		
		public Key(int... keyCodes) {
			this.keyCodes = new int[keyCodes.length];
			for (int i = 0; i < keyCodes.length; i++)
				this.keyCodes[i] = keyCodes[i];
		}
		
		public boolean down() {
			for (int k : keyCodes) {
				if (Keyboard.keyDown[k])
					return true;
			}
			return false;
		}
		
		public boolean pressed() {
			boolean down     = false;
			boolean downPrev = false;
			for (int k : keyCodes) {
				down     = down || Keyboard.keyDown[k];
				downPrev = downPrev || Keyboard.keyDownPrev[k];
			}
			return (down && !downPrev);
		}
		
		public boolean released() {
			boolean down     = false;
			boolean downPrev = false;
			for (int k : keyCodes) {
				down     = down || Keyboard.keyDown[k];
				downPrev = downPrev || Keyboard.keyDownPrev[k];
			}
			return (!down && downPrev);
		}
		
		public boolean typed() {
			for (int k : keyCodes) {
				if (keyTyped[k])
					return true;
			}
			return false;
		}
	}

	// Initialize Key Arrays
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
	
	// Update key states
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
