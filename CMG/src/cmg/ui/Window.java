package cmg.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JFrame;
import cmg.graphics.Draw;
import cmg.math.GMath;
import cmg.math.geometry.Point;
import cmg.math.geometry.Polygon;

public class Window extends Container {
	private static final int WINDOW_XPAD = 16;
	private static final int WINDOW_YPAD = 38;
	private JFrame frame;
	private Mouse mouse;
	private WindowContent windowContent;
	private boolean dragging;
	private Point dragPos;
	

	// ================== CONSTRUCTORS ================== //

	public Window(int width, int height, boolean showBorder) {
		mouse = new Mouse(this);
		
		frame = new JFrame();
		if (!showBorder)
			frame.setUndecorated(true);
		frame.setPreferredSize(new Dimension(
				width  + (showBorder ? WINDOW_XPAD : 0),
				height + (showBorder ? WINDOW_YPAD : 0)));
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.pack();
		frame.setResizable(true);
		frame.setVisible(true);
		
		rect.size.set(width, height);
		windowContent = new WindowContent(width, height);
		java.awt.Container c = frame.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(windowContent);
		
		// Move the frame to the center of the screen:
		Dimension screenSize = frame.getToolkit().getScreenSize();
		frame.setLocation((screenSize.width / 2) - (frame.getWidth() / 2),
				(screenSize.height / 2) - (frame.getHeight() / 2));
		frame.pack();
	}

	
	
	// =================== ACCESSORS =================== //
	
	public JFrame getJFrame() {
		return frame;
	}
	
	public WindowContent getWindowContent() {
		return windowContent;
	}
	
	public boolean isFocused() {
		return frame.isFocused();
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	@Override
	public void step() {
		windowContent.update();
		//Keyboard.update();
		mouse.update();
		Mouse.current = mouse;
		super.step();
		windowContent.repaint();
		windowContent.requestFocusInWindow();
		
		if (dragging) {
			if (!mouse.left.down || !isFocused())
				dragging = false;
			else if (!dragPos.equals(mouse.position())) {
				Point ms = new Point(MouseInfo.getPointerInfo().getLocation());
				frame.setLocation(ms.x - dragPos.x,
						ms.y - dragPos.y);
			}
		}
		else if (mouse.left.pressed() && mouse.y() < 64) {
			dragging = true;
			dragPos  = mouse.position();
		}
	}
	
	public void render(Graphics g) {
		int w = (int) windowContent.getViewSize().getWidth();
		int h = (int) windowContent.getViewSize().getHeight();
		
		Draw.setGraphics(g);
		Draw.getView().set(0, 0);

		Draw.setGraphics(getGraphics());
		
		// Draw background.
		
		
		Draw.setColor(UIConfig.COLOR_BACKGROUND);
		Draw.fillRect(0, 0, windowContent.getViewSize().getWidth(),
				windowContent.getViewSize().getHeight());
		
		/*
		GradientPaint grad = new GradientPaint(rect.getX1(), rect.getY1(), new Color(37, 48, 68),
				rect.getX1(), 100, UIConfig.COLOR_BACKGROUND);
		Draw.getGraphics().setPaint(grad);
		Draw.fillRect(0, 0, windowContent.getViewSize().getWidth(),
				100);
		
		int n = 200;
		for (int i = 0; i < n; i++) {
			Draw.setColor(new Color(37, 48, 68, (int) (GMath.sqr((double) (n - i) / n) * 255)));
//			Draw.drawLine(0, i, w, i);
		}
		*/
		/*
		// Draw top handle.
		Draw.getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		Polygon p = new Polygon();
		p.addVertex(73, 2);
		p.addVertex(80, 9);
		p.addVertex(w - 80, 9);
		p.addVertex(w - 73, 2);
		Draw.setColor(isFocused() ? new Color(58, 67, 83) : new Color(27, 34, 47));
		Draw.fill(p);
		
		Draw.setColor(isFocused() ? new Color(255, 255, 255, 42) : new Color(47, 56, 72));
//		Draw.setColor(Color.RED);
		Draw.drawLine(p.getVertex(0).minus(1, 0), p.getVertex(1).minus(1, 0));
		Draw.drawLine(p.getVertex(1), p.getVertex(2));
//		Draw.drawLine(p.getVertex(2).plus(1, 0), p.getVertex(3).plus(1, 0));
//		Draw.setColor(Color.RED);
//		Draw.drawLine(p.getVertex(2).minus(-1, 0), p.getVertex(3).plus(1, 0));
//		g.drawLine(w - 73 + 1, 2, w - 80 + 1, 9);
		Draw.setColor(new Color(255, 255, 255, 255));
		g.drawLine(w - 73 + 1, 2, w - 73 + 1 - 100, 2 + 100);
		draw();

		Draw.getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.fillRoundRect(4, 100, 7, 300, 8, 8);
		Draw.getGraphics().setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		*/
		
		// Draw border
		Draw.setColor(UIConfig.COLOR_FRAME_BORDER1);
		Draw.drawRect(0, 0, windowContent.getViewSize().getWidth(),
				windowContent.getViewSize().getHeight());
		Draw.setColor(new Color(192, 192, 192, isFocused() ? 50 : 30));
		Draw.drawRect(1, 1, windowContent.getViewSize().getWidth() - 2,
				windowContent.getViewSize().getHeight() - 2);
		
		Mouse.current = mouse;
		
		draw();
		renderComponents();
		g.drawImage(getCanvas(), getPosition().x, getPosition().y, null);
	}
	


	// ================= INNER CLASSES ================= //
	
	public class WindowContent extends JComponent {
		private static final long serialVersionUID = 1L;

		private Image canvas;
		private Dimension size;


		/** Setup the component. **/
		public WindowContent(int width, int height) {
			super();
			setFocusable(true);
			addMouseListener(mouse);
			addMouseWheelListener(mouse);
			//addKeyListener(keyboard);
			
			size   = new Dimension(width, height);
			canvas = new BufferedImage(size.width,
					size.height, BufferedImage.TYPE_INT_RGB);
		}

		/** Return the size of the game's view. **/
		public Dimension getViewSize() {
			return size;
		}

		/** Update the component, checking for a change in window size. **/
		public void update() {
			if (!size.equals(getSize()))
				resizeCanvas(getSize());
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
			render(bufferGraphics);
			bufferGraphics.dispose();
			g.drawImage(canvas, 0, 0, null);
		}
	}
}
