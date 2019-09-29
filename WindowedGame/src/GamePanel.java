import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;




public class GamePanel extends JPanel implements Runnable {
	private static final int FPS = 1000 / 60;
	private static final int PWIDTH = 700; // size of the panel
	private static final int PHEIGHT = 400;
	
	private Thread animator;
	private volatile boolean running = false; // stops the animation
	
	public static Vector mouseVec;
	public static float  mouseX;
	public static float  mouseY;
	public static JFrame parentWindow;
	private static BufferedImage bImg;
	public static boolean mousePressed;
	public static ArrayList<Line> listLines;
	
	public static PhysicsBody pbCircle;
	
	
	
	public GamePanel() {
		try {
			bImg = ImageIO.read(getClass().getResource("image.png"));
		}
		catch(IOException e) {
			System.out.println("Image Loading Error");
		}
		startGame();
	}
	
	public static void main(String [] args) {
		parentWindow = new JFrame("The Game - By David Jordan");
    	
    	parentWindow.getContentPane().add(new GamePanel());
    	parentWindow.setSize(PWIDTH, PHEIGHT);
        parentWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parentWindow.setVisible(true);
        parentWindow.setResizable(false);
        
        parentWindow.addMouseListener(new MouseListener() {
        	public void mousePressed(MouseEvent e) {
        		mousePressed = true;
        	}
			public void mouseClicked(MouseEvent arg0) {
				
			}
			public void mouseEntered(MouseEvent arg0) {
				
			}
			public void mouseExited(MouseEvent arg0) {
				
			}
			public void mouseReleased(MouseEvent arg0) {
				
			}
        });
	}
	
	private void startGame() {
		pbCircle = new PhysicsBody(new Circle(128, 32, 16));
		pbCircle.setVspeed(1);
		
		listLines = new ArrayList();
		listLines.add(new Line(32, 128, 256, 150));
		listLines.add(new Line(400, 32, 300, 256));
		
		if( animator == null || !running ) {
			animator = new Thread(this);
			animator.start();
		}
	} // end of startGame()
	
	public void stopGame() {
		running = false;
	}
	
	public void run() {
		// Remember the starting time
    	long tm = System.currentTimeMillis();
    	
		running = true;
		while(running) {
			gameUpdate();
			repaint();
			
			try {
				tm += FPS;
                Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
			}
			catch(InterruptedException e) {
				System.out.println(e);
			}
		}
		System.exit(0);
	} // end of run()
	
	public void updateMousePosition() {
		mouseVec = new Vector(MouseInfo.getPointerInfo().getLocation());
		Vector winVec   = new Vector(parentWindow.getLocation());
		mouseVec.sub(winVec);
		mouseVec.set(mouseVec);
		mouseVec.sub(4, 26);
		mouseX = mouseVec.getX();
		mouseY = mouseVec.getY();
	}
	
	private void gameUpdate() {
		// Set Mouse Position
		updateMousePosition();
		
	}
	
	public void paint(Graphics g) {
		// draw white background
		g.setColor(Color.white);
		g.fillRect(0, 0, PWIDTH, PHEIGHT);
		
		Line l = new Line(64, 64, 512, 200);
		
		
		pbCircle.update();
		
		Circle c = new Circle(mouseVec.getX(), mouseVec.getY(), 24);
		Vector v = new Vector(l.getClosestPoint(c.getCenter()));
		//pbCircle.cutDir((float) ((3 * Math.PI) / 2));
		
		g.setColor(Color.black);
		l.draw(g);
		
		if( c.collisionLine(l) )
			g.setColor(Color.red);
		c.draw(g);
		
		g.setColor(Color.black);
		pbCircle.draw(g);
		g.drawString(String.valueOf(pbCircle.hspeed), 4, 20);
		g.drawString(String.valueOf(pbCircle.vspeed), 4, 40);
		
		g.setColor(Color.red);
		v.draw(g, 2);
		
		g.setColor(Color.black);
		for( int i = 0; i < listLines.size(); i++ ) {
			listLines.get(i).draw(g);
		}
	}
	
} // end of GamePanel class
