
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.math.*;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
 
public class GamePanel extends JPanel implements Runnable {
	private static final int FPS = 1000 / 60;
	public static JFrame parentWindow;
	public static int FRAME_WIDTH;
	public static int FRAME_HEIGHT;
	public static boolean LEFT_DOWN  = false;
	public static boolean RIGHT_DOWN = false;
	public static boolean UP_DOWN    = false;
	public static boolean DOWN_DOWN  = false;
	public static boolean[][][] blocks;
	public static Random random;
	public static Image image_grassblock;
	public static Image image_dirtblock;
	public static Image image_stoneblock;
	public static Image image_waterblock;
	public static Image image_sandblock;
	public static final int block_width   = 12;
	public static final int block_height  = 11;
	public static final int block_zheight = 7;
	public static int chunks_dim = 65;
	public static int chunks_width  = 13;
	public static int chunks_height = 13;
	public static final int VIEW_WIDTH = 2000;// 16 + (chunks_dim * block_width);
	public static final int VIEW_HEIGHT = (int) ((float)VIEW_WIDTH * (7.0f / 12.0f));// 16 + (((chunks_dim * block_height) + (chunks_dim * block_zheight)));
	public static int basex = VIEW_WIDTH / 2;
	public static int basey = 16 + (Chunk.depth * block_height);
	public static int playerx = 1;
	public static int playery = 1;
	public static double playerz = chunks_dim + 20;
	public static double player_zspeed = 0;
	public static boolean redraw = true;
	public static TerrainGen terriangen;
	public static Image display;
	public static float draw_scale = 0.5f;
	
	public static Chunk[][] chunks;
	
	
	public static void main(String[] args)
    {
		parentWindow = new JFrame("Terrain Generation - By David Jordan");
    	
    	Container c = parentWindow.getContentPane();
    	c.setLayout(new BorderLayout());
    	c.add(new JScrollPane(new GamePanel()));
    	
    	
    	parentWindow.setSize(6 + 600, 25 + 600);
        parentWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parentWindow.setVisible(true);
        parentWindow.setResizable(true);
        
        parentWindow.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
            	keyHit(e);
            }
            public void keyPressed(KeyEvent e) {
            	/* QUIT IF ESCAPE KEY IS PRESSED */
            	if (e.getKeyCode() == 27) {
            		System.exit(0);
            	}
            	KeyPressed(e);
            }
            public void keyReleased(KeyEvent e) {
            	KeyReleased(e);
            }
        });

    }
	
	public GamePanel() {
		image_grassblock = (new ImageIcon(this.getClass().getResource("images/GrassBlock.PNG"))).getImage();
		image_dirtblock  = (new ImageIcon(this.getClass().getResource("images/DirtBlock.PNG"))).getImage();
		image_stoneblock = (new ImageIcon(this.getClass().getResource("images/StoneBlock.PNG"))).getImage();
		image_waterblock = (new ImageIcon(this.getClass().getResource("images/WaterBlock.PNG"))).getImage();
		image_sandblock  = (new ImageIcon(this.getClass().getResource("images/SandBlock.PNG"))).getImage();
		
		blocks = new boolean[chunks_dim][chunks_dim][chunks_dim];
		chunks = new Chunk[1000][1000];
		random = new Random();
		playerx = 4;
		playery = 5;
		terriangen = new TerrainGen();
		display = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);

		for( int x = 0; x < chunks_width; x++ ) {
			for( int y = 0; y < chunks_height; y++ ) {
				chunks[x][y] = new Chunk(x, y);
			}
		}
		for( int x = 0; x < chunks_width; x++ ) {
			for( int y = 0; y < chunks_height; y++ ) {
				chunks[x][y].generateTerrain();
			}
		}
		
		//generateTerrain();
		
		setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		
		new Thread(this).start();
	}
	
	private static void keyHit(KeyEvent e) {
	}
	private static void KeyPressed(KeyEvent e) {
		if( e.getKeyCode() == KeyEvent.VK_LEFT )
			LEFT_DOWN = true;
		if( e.getKeyCode() == KeyEvent.VK_UP )
			UP_DOWN = true;
		
		if( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
			RIGHT_DOWN = true;
			// Create New Chunk
			chunks_width += 1;
			for( int y = 0; y < chunks_height; y++ )
				chunks[chunks_width - 1][y] = new Chunk(chunks_width - 1, y);
		}
		if( e.getKeyCode() == KeyEvent.VK_DOWN ) {
			DOWN_DOWN = true;
			// Create New Chunk
			chunks_height += 1;
			for( int x = 0; x < chunks_width; x++ )
				chunks[x][chunks_height - 1] = new Chunk(x, chunks_height - 1);
		}
		if( e.getKeyCode() == KeyEvent.VK_Z )
			player_zspeed = 10;
		
		if( e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER ) {
			redraw = true;
			for( int x = 0; x < chunks_width; x++ ) {
				for( int y = 0; y < chunks_height; y++ ) {
					chunks[x][y] = new Chunk(x, y);
				}
			}
			for( int x = 0; x < chunks_width; x++ ) {
				for( int y = 0; y < chunks_height; y++ ) {
					chunks[x][y].generateTerrain();
				}
			}
			//generateTerrain();
		}
	}
	private static void KeyReleased(KeyEvent e) {
		//if (e.getKeyChar() == 'a')
        //	player.moveLeft(16);
		
		if( e.getKeyCode() == KeyEvent.VK_LEFT )
			LEFT_DOWN = false;
		if( e.getKeyCode() == KeyEvent.VK_RIGHT )
			RIGHT_DOWN = false;
		if( e.getKeyCode() == KeyEvent.VK_UP )
			UP_DOWN = false;
		if( e.getKeyCode() == KeyEvent.VK_DOWN )
			DOWN_DOWN = false;
	}
	
	public static void generateTerrain() {
		float[][] terrain = terriangen.generateTerrainDiamondSquare(chunks_dim, chunks_dim);
		for( int x = 0; x < chunks_dim; x++ ) {
			for( int y = 0; y < chunks_dim; y++ ) {
				for( int z = 0; z < chunks_dim; z++ ) {
					blocks[x][y][z] = (terrain[x][y] > z);
				}
			}
		}
	}
	
    public void run() {
    	
    	// Remember the starting time
    	long tm = System.currentTimeMillis();

        while( true ){
            
            update();
            repaint();
            
            try {
                tm += FPS;
                Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
            }
            catch( InterruptedException e ) {
            	System.out.println(e);
            }
        }
    }
    public void update() {
    	if( LEFT_DOWN )
    		playerx -= 1;
    	if( RIGHT_DOWN )
    		playerx += 1;
    	if( UP_DOWN )
    		playery -= 1;
    	if( DOWN_DOWN )
    		playery += 1;
    }
	public void paint(Graphics g) {
		if( redraw ) {
			Graphics bg = display.getGraphics();
			bg.setColor(Color.white);
			bg.fillRect(0, 0, VIEW_WIDTH, VIEW_HEIGHT);
			int size = 8;
			bg.setColor( Color.black );
			//g.drawRect( playerx * size, playery * size, size, size );
		
			int drawx;
			int drawy;
			int scale = 12;
			//float draw_scale = 0.5f;
			int water_level = (int)((float)chunks_dim * 0.4f);
			
			for( int x = 0; x < chunks_width; x++ ) {
				for( int y = 0; y < chunks_height; y++ ) {
					chunks[x][y].draw(bg, basex, basey);
				}
			}
			
			/*
			for( int i = 0; i < chunks_dim; i++ ) {
				for( int j = 0; j < chunks_dim; j++ ) {
					for( int z = 0; z < chunks_dim; z++ ) {
						boolean draw = false;
						Image image = image_grassblock;
						
						if( blocks[i][j][z] ) {
							draw = true;
							if( z < water_level + 2 ) {
								image = image_sandblock;
							}
							else if( z < chunks_dim - 1 ) {
								if( blocks[i][j][z + 1] )
									image = image_dirtblock;
							}
							if( i < chunks_dim - 1 && j < chunks_dim - 1 && z < chunks_dim - 2 ) {
								if( blocks[i + 1][j][z] && blocks[i + 1][j][z + 1] && blocks[i][j + 1][z] &&
										blocks[i][j + 1][z + 1] ) {
									draw = false;
								}
							}
						}
						else if( z < water_level ) {
							draw = true;
							image = image_waterblock;
						}
						if( draw ) {
							drawx = basex + (i * (block_width / 2)) - (j * (block_width / 2));
							drawy = basey + (i * (block_height / 2)) + (j * (block_height / 2));
							bg.drawImage(image, (int)((float)drawx * draw_scale), (int)((float)(drawy - z * (block_zheight)) * draw_scale), 
									(int)(12 * draw_scale), (int)(12 * draw_scale), null);
						}
							
							
						//int c = (int) (((float)z / 64.0f) * 255.0f);
						drawx = i * scale;
						drawy = j * scale;
						
						bg.setColor(Color.white);
						bg.fillRect(drawx, drawy, scale, scale);
						bg.setColor(Color.black);
						bg.drawRect(drawx, drawy, scale, scale);
						
						if( z < chunks_dim - 1 ) {
							if( blocks[i][j][z + 1] )
								draw = false;
						}
						if( draw )
							bg.drawString(String.valueOf(z), drawx, drawy + scale);
							
					}
				}
			}
			*/
			redraw = false;
		}
		
		g.drawImage(display, 0, 0, null);
	}
}

