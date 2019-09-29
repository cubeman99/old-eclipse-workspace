import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComponent;


public class PollyWorld extends JComponent {
	public static final int GAME_X = 0;
	public static final int GAME_Y = 0;
	public static final int GAME_WIDTH   = 480;
	public static final int GAME_HEIGHT  = 480;
	public static final int IMAGE_WIDTH  = 720;
	public static final int IMAGE_HEIGHT = 480;
	
	public static final int KEYS_DIM 	= 400;
	public static boolean[] keyDown 	= new boolean[KEYS_DIM];
	public static boolean[] keyDownPrev = new boolean[KEYS_DIM];
	public static boolean[] keyPressed 	= new boolean[KEYS_DIM];
	public static boolean[] keyReleased = new boolean[KEYS_DIM];
	public static boolean mouseButtonLeft	= false;
	public static boolean mouseButtonMiddle	= false;
	public static boolean mouseButtonRight	= false;
	
	public static int pollyCount = 0;
	public static final int maxPollies = 100;
	public static int plantCount = 0;
	public static final int maxPlants = 500;
	public static int speciesCount = 0;
	
	public static final boolean scale_buffer = true;
	public static Image buffer;
	public static Random random = new Random();
	public static ArrayList<Entity> entity_list;
	public static boolean[][] walls;
	public static final int wall_width = 16;
	public static final int wall_height = 16;
	public static final int wall_amountx = GAME_WIDTH / wall_width;
	public static final int wall_amounty = IMAGE_HEIGHT / wall_height;
	
	
	public PollyWorld() {
		buffer = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		entity_list = new ArrayList<Entity>();
		
		walls = new boolean[wall_amountx][wall_amounty];
		for( int x = 0; x < wall_amountx; x++ ) {
			for( int y = 0; y < wall_amounty; y++ ) {
				walls[x][y] = ( x == 0 || y == 0 || x == wall_amountx - 1 || y == wall_amounty - 1 );
			}
		}
		
		for( int i = 0; i < KEYS_DIM; i++ ) {
			keyDown[i] 		= false;
			keyDownPrev[i] 	= false;
			keyPressed[i] 	= false;
			keyReleased[i] 	= false;
		}
		
		installListeners();
		
		initScenario();
	}
	
	public void initScenario() {
		
		for( int i = 0; i < 10; i++ )
			new Polly(200 + MyMath.random(-64.0f, 64.0f), 200 + MyMath.random(-64.0f, 64.0f)).initSpecies();
		for( int i = 0; i < 80; i++ )
			new Plant(200 + MyMath.random(-64.0f, 64.0f), 200 + MyMath.random(-64.0f, 64.0f));
		
	}
	
	private void installListeners() {
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if( e.getButton() == MouseEvent.BUTTON1 ) {
					mouseButtonLeft = true;
					
				}
				if( e.getButton() == MouseEvent.BUTTON2 ) {
					mouseButtonMiddle = true;
					
				}
				if( e.getButton() == MouseEvent.BUTTON3 ) {
					mouseButtonRight = true;
					
				}
			}
			public void mouseReleased(MouseEvent e) {
				if( e.getButton() == MouseEvent.BUTTON1 ) {
					mouseButtonLeft = false;
					
				}
				if( e.getButton() == MouseEvent.BUTTON2 ) {
					mouseButtonMiddle = false;
					
				}
				if( e.getButton() == MouseEvent.BUTTON3 ) {
					mouseButtonRight = false;
					
				}
			}
		});
		this.setFocusable(true);
		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				keyDown[e.getKeyCode()] = true;
			}
			public void keyReleased(KeyEvent e) {
				keyDown[e.getKeyCode()] = false;
			}
			public void keyTyped(KeyEvent e) {
				// Not Used
			}
		});
	}
	
	
	public void update() {
		// Update Keys
		for( int i = 0; i < KEYS_DIM; i++ ) {
			keyPressed[i]  = ( keyDown[i] && !keyDownPrev[i] );
			keyReleased[i] = ( !keyDown[i] && keyDownPrev[i] );
		}
		
		if( keyPressed[KeyEvent.VK_R] ) {
			entity_list.clear();
			initScenario();
		}

		// Update Entities
		for( int i = 0; i < entity_list.size(); i++ ) {
			entity_list.get(i).update();
		}
		// Remove Entities
		for( int i = 0; i < entity_list.size(); i++ ) {
			if( entity_list.get(i).removed ) {
				entity_list.remove(i);
				i += 1;
			}
		}
		
		// Lastly, repaint the screen using double-buffering
		repaint();
		
		for( int i = 0; i < KEYS_DIM; i++ ) {
			keyDownPrev[i] = keyDown[i];
		}
	}
	
	public int[] entityDepthList() {
		int[] list = new int[entity_list.size()];
		
		for( int i = 0; i < entity_list.size(); i++ )
			list[i] = i;
		
		// SORT BY LOWEST DEPTH
		for( int i = 0; i < entity_list.size(); i++ ) {
		    for( int j = entity_list.size() - 1; j > i; j-- ) {
		        if( entity_list.get(list[j]).depth > entity_list.get(list[i]).depth ) {
		        	int temp = list[i];
		        	list[i] = list[j];
		        	list[j] = temp;
		        }
		    }
		}
		return list;
	}
	
	public void drawEntities(Graphics g) {
		// draw Entities sorted by depth
		int[] list = entityDepthList();
		for( int i = 0; i < entity_list.size(); i++ ) {
			entity_list.get(list[i]).draw(g);
			//entity_list.get(i).draw(g);
		}
	}
	
	public void drawInfo(Graphics g) {
		g.setColor(Color.white);
		g.drawString("Entites: " + String.valueOf(entity_list.size()), 0, 20);
		g.drawString("Pollies: " + String.valueOf(pollyCount), 0, 40);
		g.drawString("Plants:  " + String.valueOf(plantCount), 0, 60);
	}
	
	public void render() {
		Graphics g = buffer.getGraphics();
		
		// Draw a black background
		g.setColor(Color.black);
		g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		
		// draw walls
		g.setColor(Color.darkGray);
		for( int x = 0; x < wall_amountx; x++ ) {
			for( int y = 0; y < wall_amounty; y++ ) {
				if( walls[x][y] )
					g.fillRect(x * wall_width, y * wall_height, wall_width, wall_height);
			}
		}
		// Draw all Entities
		drawEntities(g);
		
		// Draw Information Panel
		drawInfo(g);
	}
	
	public void paintComponent(Graphics g) {
		render();
		if( scale_buffer ) {
			float H = (float) (Main.frame.getHeight() - 64);
			float W = H * ((float)IMAGE_WIDTH / (float)IMAGE_HEIGHT);
			g.drawImage(buffer, 0, 0, (int)W, (int)H, null);
		}
		else {
			g.drawImage(buffer, 0, 0, null);
		}
	}
}
