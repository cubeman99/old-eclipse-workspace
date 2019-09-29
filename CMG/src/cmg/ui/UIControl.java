package cmg.ui;

import java.awt.Graphics;
import java.util.ArrayList;


public class UIControl {
	private static final int WINDOW_XPAD = 16;
	private static final int WINDOW_YPAD = 38;
	public static int FPS = 60;
	public static double STEP_TIME = 1.0 / (double) FPS;
	public static int STEP_MILLISECONDS = (int) (STEP_TIME * 1000.0);
	private GameThread gameThread;
	
	private ArrayList<Window> windows;
	

	
	// ================== CONSTRUCTORS ================== //
	
	public UIControl(int fps) {
		FPS               = fps;
		STEP_TIME         = 1.0 / (double) FPS;
		STEP_MILLISECONDS = (int) (STEP_TIME * 1000.0);
		windows           = new ArrayList<Window>();
		
		initialize();
		
		// Start the game thread.
		gameThread = new GameThread();
		gameThread.start();
	}
	
	
	
	// =============== ABSTRACT METHODS =============== //

	protected void initialize() {};

	protected void update() {};

	protected void draw(Graphics g) {};
	
	
	
	// =================== ACCESSORS =================== //

	public ArrayList<Window> getWindows() {
		return windows;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void addWindow(Window wnd) {
		windows.add(wnd);
	}
	
	public void removeWindow(Window wnd) {
		windows.remove(wnd);
	}
	
	public void end() {
		gameThread.end();
	}
	
	
	
	// ================= INNER CLASSES ================= //
	
	private class GameThread extends Thread {
		private boolean running;

		@Override
		public void run() {
			running = true;

			while (running && windows.size() > 0) {
				long tickStartTime = System.currentTimeMillis();
				
				for (int i = 0; i < windows.size(); i++) {
					Window wnd = windows.get(i);
					wnd.step();
					if (!wnd.getJFrame().isVisible())
						windows.remove(i--);
				}

				int tickTime = (int) (System.currentTimeMillis() - tickStartTime);

				// Wait until the next step:
				if (tickTime < STEP_MILLISECONDS - 1) {
					try {
						Thread.sleep(Math.max(0, STEP_MILLISECONDS - tickTime));
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
			System.exit(0);
		}

		public void end() {
			running = false;
		}
	}
}
