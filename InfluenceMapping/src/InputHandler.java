
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class InputHandler {
	public Map<Integer, Key> mappings = new HashMap<Integer, Key>();
	public Keys keys;

	public static final int KEYS_DIM = 400;
	public Key[] keyList			= new Key[KEYS_DIM];
	public boolean[] keyDown 		= new boolean[KEYS_DIM];
	public boolean[] keyDownPrev 	= new boolean[KEYS_DIM];
	public boolean[] keyPressed 	= new boolean[KEYS_DIM];
	public boolean[] keyReleased	= new boolean[KEYS_DIM];
	
	public InputHandler(Keys keys) {
		this.keys = keys;
		mapKey(KeyEvent.VK_UP, keys.up);
		mapKey(KeyEvent.VK_UP, keys.up);
		mapKey(KeyEvent.VK_DOWN, keys.down);
		mapKey(KeyEvent.VK_LEFT, keys.left);
		mapKey(KeyEvent.VK_RIGHT, keys.right);

		mapKey(KeyEvent.VK_W, keys.up);
		mapKey(KeyEvent.VK_S, keys.down);
		mapKey(KeyEvent.VK_A, keys.left);
		mapKey(KeyEvent.VK_D, keys.right);
		
		mapKey(KeyEvent.VK_Q, keys.switchWeapons);
		mapKey(KeyEvent.VK_E, keys.use);
		mapKey(KeyEvent.VK_R, keys.reload);
	}
	
	private void mapKey(int keycode, Key k) {
		keyList[keycode] = k;
	}
	
	public void keyPress(KeyEvent e) {
		if (keyList[e.getKeyCode()] != null) {
			if (!keyList[e.getKeyCode()].isDown)
				keyList[e.getKeyCode()].press();
		}
	}
	
	public void keyRelease(KeyEvent e) {
		if (keyList[e.getKeyCode()] != null) {
			keyList[e.getKeyCode()].release();
		}
	}
}
