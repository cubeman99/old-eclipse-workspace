package cmg.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import cmg.graphics.Draw;


public abstract class GameRunner {
	private static final int WINDOW_XPAD = 16;
	private static final int WINDOW_YPAD = 38;
	public static int FPS = 60;
	public static double STEP_TIME = 1.0 / (double) FPS;
	public static int STEP_MILLISECONDS = (int) (STEP_TIME * 1000.0);
	private GameThread gameThread;
	private JFrame frame;
	private String title;
	private GameComponent gameComponent;



	public GameRunner(int viewWidth, int viewHeight, CanvasMode canvasMode) {
		this(60, viewWidth, viewHeight, canvasMode);
	}

	public GameRunner(int fps, int viewWidth, int viewHeight,
			CanvasMode canvasMode) {
		FPS = fps;
		STEP_TIME = 1.0 / (double) FPS;
		STEP_MILLISECONDS = (int) (STEP_TIME * 1000.0);
		title = "Untitled - David Jordan";

		// Initialize a new frame and game object:
		frame = new JFrame(title);
		ImageLoader.loadAllImages();

		// Configure the frame settings:
		frame.setPreferredSize(new Dimension(viewWidth + WINDOW_XPAD,
				viewHeight + WINDOW_YPAD));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);

		gameComponent = new GameComponent(viewWidth, viewHeight, canvasMode);
		Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(gameComponent);


		// Move the frame to the center of the screen:
		Dimension screenSize = frame.getToolkit().getScreenSize();
		frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2),
				(screenSize.height / 2) - (frame.getHeight() / 2));
		frame.pack();

		// Initialize Game:
		initialize();
		gameComponent.setReady();
		
		// Start the game thread:
		gameThread = new GameThread();
		gameThread.start();
	}

	public void end(boolean exitOnEnd) {
		gameThread.end(exitOnEnd);
	}

	public JFrame getFrame() {
		return frame;
	}

	public GameComponent getGameComponent() {
		return gameComponent;
	}

	public int getViewWidth() {
		return gameComponent.getViewSize().width;
	}

	public int getViewHeight() {
		return gameComponent.getViewSize().height;
	}

	public Dimension getViewSize() {
		return gameComponent.getViewSize();
	}

	/** Set the title of the window. **/
	public void setTitle(String title) {
		this.title = title;
		frame.setTitle(title);
	}

	/** Change the size of the window. **/
	public void resizeWindow(int newWidth, int newHeight) {
		frame.setSize(newWidth + WINDOW_XPAD, newHeight + WINDOW_YPAD);
		Dimension screenSize = frame.getToolkit().getScreenSize();
		frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2),
				(screenSize.height / 2) - (frame.getHeight() / 2));
	}

	/** Maximize the window. **/
	public void windowMaximize() {
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	protected abstract void initialize();

	protected abstract void update();

	protected abstract void draw(Graphics g);

	private class GameThread extends Thread {
		private boolean running;
		private boolean exitOnEnd;

		@Override
		public void run() {
			running = true;
			exitOnEnd = true;

			while (running) {
				long tickStartTime = System.currentTimeMillis();
				Cursor prevCursor = Mouse.getCursor();
				Mouse.setDefaultCursor();

				// Update the mouse and keyboard inputs, then update and repaint
				// the game:
				gameComponent.update();
				Keyboard.update();
				Mouse.update();
				update();
				gameComponent.repaint();
				gameComponent.requestFocusInWindow();

				if (Mouse.getCursor() != prevCursor)
					frame.setCursor(Mouse.getCursor());

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

			// End the game:
			if (exitOnEnd)
				System.exit(0);
			else {
				System.out.println("Exited...");
				frame.setVisible(false);
			}
		}

		public void end(boolean exitOnEnd) {
			this.running = false;
			this.exitOnEnd = exitOnEnd;
		}
	}

	protected enum CanvasMode {
		MODE_RESIZE, MODE_STRETCH
	}

	private class GameComponent extends JComponent {
		private static final long serialVersionUID = 1L;

		private CanvasMode canvasMode;
		private Image canvas;
		private Dimension size;
		private boolean ready;
		private Mouse mouse;
		private Keyboard keyboard;


		/** Setup the component. **/
		public GameComponent(int width, int height, CanvasMode canvasMode) {
			mouse = new Mouse(this);
			keyboard = new Keyboard();
			addMouseListener(mouse);
			addMouseWheelListener(mouse);
			addKeyListener(keyboard);

			ready = false;
			size = new Dimension(width, height);
			canvas = new BufferedImage(size.width, size.height,
					BufferedImage.TYPE_INT_RGB);
			this.canvasMode = canvasMode;

			this.setFocusable(true);
		}

		/** Set this component as ready to draw. **/
		public void setReady() {
			ready = true;
		}

		/** Return the size of the game's view. **/
		public Dimension getViewSize() {
			return size;
		}

		/** Update the component, checking for a change in window size. **/
		public void update() {
			if (canvasMode == CanvasMode.MODE_RESIZE) {
				if (!size.equals(getSize())) {
					resizeCanvas(getSize());
				}
			}
		}

		/** Resize the image canvas. **/
		public void resizeCanvas(Dimension newSize) {
			size.setSize(Math.max(newSize.width, 1),
					Math.max(newSize.height, 1));
			canvas = new BufferedImage(size.width, size.height,
					BufferedImage.TYPE_INT_RGB);
		}

		@Override
		/** Handle rendering and draw the raw canvas image to the window. **/
		protected void paintComponent(Graphics g) {
			Graphics bufferGraphics = canvas.getGraphics();
			bufferGraphics.setColor(Color.LIGHT_GRAY);
			bufferGraphics.fillRect(0, 0, size.width, size.height);

			if (ready)
				draw(bufferGraphics);
			bufferGraphics.dispose();
			if (canvasMode == CanvasMode.MODE_RESIZE)
				g.drawImage(canvas, 0, 0, null);
			else if (canvasMode == CanvasMode.MODE_STRETCH)
				g.drawImage(canvas, 0, 0, super.getWidth(), super.getHeight(),
						0, 0, size.width, size.height, null);
		}
	}
}
