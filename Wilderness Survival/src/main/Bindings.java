package main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import main.Keyboard.Key;


/**
 * Class to hold Key Bindings.
 * 
 * @author David Jordan
 */
public class Bindings {
	
	// Custom Key Bindings:
	public static KeyBinding up     = new KeyBinding(KeyEvent.VK_UP);
	public static KeyBinding down   = new KeyBinding(KeyEvent.VK_DOWN);
	public static KeyBinding left   = new KeyBinding(KeyEvent.VK_LEFT);
	public static KeyBinding right  = new KeyBinding(KeyEvent.VK_RIGHT);
	public static KeyBinding a      = new KeyBinding(KeyEvent.VK_Z);
	public static KeyBinding b      = new KeyBinding(KeyEvent.VK_X);
	public static KeyBinding start  = new KeyBinding(KeyEvent.VK_ENTER);
	public static KeyBinding select = new KeyBinding(KeyEvent.VK_SHIFT);
	
	
	
	
	public static class KeyBinding {
		public ArrayList<Integer> keyCodes;
		
		/** Initialize a key binding with any amount of key code bindings. **/
		public KeyBinding(int... keyCodes) {
			for (int i = 0; i < keyCodes.length; i++)
				this.keyCodes.add(keyCodes[i]);
		}
		
		/** Initialize a key binding with an array list of bindings. **/
		public KeyBinding(ArrayList<Integer> keyCodes) {
			for (int i = 0; i < keyCodes.size(); i++)
				this.keyCodes.add(keyCodes.get(i));
		}
		
		/** Check if the binding is down. **/
		public boolean down() {
			for (int k : keyCodes) {
				if (Keyboard.keyDown[k])
					return true;
			}
			return false;
		}

		/** Check if the binding was pressed. **/
		public boolean pressed() {
			boolean down     = false;
			boolean downPrev = false;
			for (int k : keyCodes) {
				down     = down || Keyboard.keyDown[k];
				downPrev = downPrev || Keyboard.keyDownPrev[k];
			}
			return (down && !downPrev);
		}

		/** Check if the binding was released. **/
		public boolean released() {
			boolean down     = false;
			boolean downPrev = false;
			for (int k : keyCodes) {
				down     = down || Keyboard.keyDown[k];
				downPrev = downPrev || Keyboard.keyDownPrev[k];
			}
			return (!down && downPrev);
		}
	}
}
