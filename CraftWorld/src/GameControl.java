/*
 * CraftWorld - By David Jordan
 * 
 */



import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JComponent;


public class GameControl extends JComponent {
	public static Image buffer;
	public static Image imageMap;
	
	public static final int WORLD_WIDTH  = 800;
	public static final int WORLD_HEIGHT = 500;
	
	public static final int terrain_spanmin = (int)((float)WORLD_HEIGHT * 0.8f);
	public static final int terrain_spanmax = (int)((float)WORLD_HEIGHT * 0.6f);
	
	public static final int VIEW_WIDTH   = 512;//256;
	public static final int VIEW_HEIGHT  = 512;//256;
	public static int VIEW_X = 0;
	public static int VIEW_Y = 0;
	
	public static final int KEYS_DIM 	= 400;
	public static boolean[] keyDown 	= new boolean[KEYS_DIM];
	public static boolean[] keyPressed 	= new boolean[KEYS_DIM];
	public static boolean[] keyReleased = new boolean[KEYS_DIM];
	public static int cursorX = 0;
	public static int cursorY = 0;
	public static int mouseX = 0;
	public static int mouseY = 0;
	public static boolean mouseButtonLeft   = false;
	public static boolean mouseButtonRight  = false;
	public static boolean mouseButtonMiddle = false;
	
	public static Random rand;
	public static int blockDestroyProgress = 0; // [0 - 100]
	public static int blockDestroyX = 0;
	public static int blockDestroyY = 0;
	
	public static Image imageCursor 		= ImageLoader.loadImage("cursor.png");
	public static Image imageBackdrops 		= ImageLoader.loadImage("backdrops.png");
	
	public static BlockList blocklist = new BlockList();
	public static Block[][] blocks = new Block[WORLD_WIDTH][WORLD_HEIGHT];
	public static int[][] watergrid = new int[WORLD_WIDTH][WORLD_HEIGHT]; // [1 - 100]
	public static int[][] backdrops = new int[WORLD_WIDTH][WORLD_HEIGHT];
	public static Player player;
	
	public static final int maxEntities = 10000000;
	public static Entity[] entities = new Entity[maxEntities];
	public static boolean[] entityTaken = new boolean[maxEntities];
	
	
	
	public GameControl() {
		buffer = new BufferedImage(VIEW_WIDTH, VIEW_HEIGHT, BufferedImage.TYPE_INT_RGB);
		imageMap = new BufferedImage(WORLD_WIDTH, WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		player = new Player(2, WORLD_HEIGHT - terrain_spanmin);
		
		setPreferredSize(new Dimension(VIEW_WIDTH, VIEW_HEIGHT));
		
		// Create and set the game cursor
		Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(imageCursor, new Point(0, 0), "game cursor");
		this.setCursor(cursor);
		
		for( int i = 0; i < KEYS_DIM; i++ ) {
			keyDown[i] 		= false;
			keyPressed[i] 	= false;
			keyReleased[i] 	= false;
		}
		
		initBlocks();
		installListeners();
	}
	
	private void initBlocks() {
		
		// Set a blank world
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			for( int y = 0; y < WORLD_HEIGHT; y++ ) {
				blocks[x][y] = new BlockAir(x, y);
				watergrid[x][y] = 0;
			}
		}
		// generate terrain
		generateTerrain();
		
		// make block connections
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			for( int y = 0; y < WORLD_HEIGHT; y++ ) {
				blocks[x][y].connect();
			}
		}
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
				// Modify KeyCode Arrays
				if( !keyDown[e.getKeyCode()] )
					keyPressed[e.getKeyCode()] = true;
				keyDown[e.getKeyCode()] = true;
			}
			public void keyReleased(KeyEvent e) {
				// Modify KeyCode Arrays
				if( keyDown[e.getKeyCode()] )
					keyReleased[e.getKeyCode()] = true;
				keyDown[e.getKeyCode()] = false;
				
			}
			public void keyTyped(KeyEvent e) {
				// Not Used
			}
		});
	}
	
	public static void connectArea(int x, int y) {
		int startx = Math.max(0, x - 1);
		int starty = Math.max(0, y - 1);
		int endx 	= Math.min(GameControl.WORLD_WIDTH - 1, x + 1);
		int endy	= Math.min(GameControl.WORLD_HEIGHT - 1, y + 1);
		
		for( int xx = startx; xx <= endx; xx++ ) {
			for( int yy = starty; yy <= endy; yy++ ) {
				blocks[xx][yy].connect();
			}
		}
	}
	
	public static Block setBlock(Block template, int x, int y, boolean connect) {
		if( x >= 0 && y >= 0 && x < WORLD_WIDTH && y < WORLD_HEIGHT ) {
			blocks[x][y] = blocklist.get(template.getID()).getNewBlock(x, y);
			if( template.id != Block.air.id )
				watergrid[x][y] = 0;
			if( connect )
				connectArea(x, y);
			return blocks[x][y];
		}
		return null;
	}
	public static Block setBlock(Block template, int x, int y) {
		return setBlock(template, x, y, true);
	}
	
	public static void carveCircle(int x, int y, float r, boolean connect) {
		int startx = Math.max(0, x - (int)r - 2);
		int starty = Math.max(0, y - (int)r - 2);
		int endx 	= Math.min(GameControl.WORLD_WIDTH - 1, x + (int)r + 2);
		int endy	= Math.min(GameControl.WORLD_HEIGHT - 1, y + (int)r + 2);
		for( int xx = startx; xx < endx; xx++ ) {
			for( int yy = starty; yy < endy; yy++ ) {
				int dx = xx - x;
				int dy = yy - y;
				if( Math.sqrt((dx * dx) + (dy * dy)) <= (double)r ) {
					setBlock(Block.air, xx, yy, connect);
				}
			}
		}
	}
	
	public static void carveCircle(int x, int y, float r) {
		carveCircle(x, y, r, true);
	}
	
	public void generateTerrainRandom() {
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			for( int y = 0; y < WORLD_HEIGHT; y++ ) {
				Random r = new Random();
				if( r.nextBoolean() )
					blocks[x][y] = new BlockAir(x, y);
				else
					blocks[x][y] = new BlockDirt(x, y);
			}
		}
		blocks[0][0] = new BlockAir(0, 0);
		blocks[0][1] = new BlockAir(0, 1);
	}
	
	public void updateMap() {
		Graphics g = imageMap.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
		
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			for( int y = 1; y < WORLD_HEIGHT; y++ ) {
				g.setColor(blocks[x][y].template().mapColor);
				if( blocks[x][y].id == Block.air.getID() && backdrops[x][y] == 1 )
					g.setColor(new Color(57, 44, 42));
				g.drawRect(x, y, 0, 0);
			}
		}
	}
	
	public void generateTerrain() {
		
		// Initiate the Random Number Generator
		rand = new Random();
		rand.setSeed(rand.nextLong());
		
		/*
		 * TERRAIN GENERATION PROCESS:
		 * --------------------------
		 * 1. Fill Ground with Ore
		 * 2. Raise terrain using diamond square method
		 * 3. Carve out caves
		 * 4. Grow vines off of grass and trees
		 * 
		 * 
		 * After: draw out map
		 */
		
		System.out.println("----------GENERATING TERRAIN-----------");
		
		// FILL GROUND WITH ORE
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			for( int y = 0; y < WORLD_HEIGHT; y++ ) {
				backdrops[x][y] = 1;
				setBlock(Block.stone, x, y, false);
			}
		}
		
		long tm, tmTotal;
		tmTotal = System.currentTimeMillis();
		
		// RAISE TERRAIN
		System.out.print("Raising Terrain...");
		tm = System.currentTimeMillis();
		
		int[] heightmap = TerrainGen.generateTerrain(rand, WORLD_WIDTH, WORLD_HEIGHT);
		int[] topblocks = new int[WORLD_WIDTH];
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			for( int y = 0; y < WORLD_HEIGHT; y++ ) {
				if( GameControl.WORLD_HEIGHT - y > heightmap[x] ) {
					backdrops[x][y] = 0;
					setBlock(Block.air, x, y, false);
				}
				else if( GameControl.WORLD_HEIGHT - y > heightmap[x] - 8 ) {
					setBlock(Block.dirt, x, y, false);
				}
			}
		}
		// make array of the surface blocks
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			topblocks[x] = WORLD_HEIGHT - 1;
			for( int y = 0; y < WORLD_HEIGHT; y++ ) {
				if( blocks[x][y].id != Block.air.getID() ) {
					topblocks[x] = y;
					break;
				}
			}
		}
		System.out.println((float)(System.currentTimeMillis() - tm) / 1000.0f);
		
		// DIG CAVES
		System.out.print("Digging Caves...");
		tm = System.currentTimeMillis();
		
		// Create some random, snaking tunnels
		int tunnel_count = 150;
		for( int i = 0; i < tunnel_count; i++ ) {
			// Create a tunnel and give it unique properties
			float dir		= rand.nextFloat() * 360.0f;
			float radius	= 1.0f + (rand.nextFloat() * 3.0f);
			float x			= (int)(rand.nextFloat() * (float)WORLD_WIDTH);
			float y			= WORLD_HEIGHT - (int)(rand.nextFloat() * (float)terrain_spanmax);
			
			// Stretch for a somewhat random distance
			int maxdist = 100 + (int)(rand.nextFloat() * 150.0f);
			for( int dist = 0; dist < maxdist; dist++ ) {
				radius	 = Math.max(1.5f, Math.min(radius, 4.0f));
				// Carve a circle
				carveCircle((int)x, (int)y, radius, false);
				
				float dirR = 60.0f;
				dir		+= (dirR / 2.0f) - (rand.nextFloat() * dirR);
				x		+= (float)Math.cos(Math.toRadians(dir));
				y		+= (float)Math.sin(Math.toRadians(dir));
				radius	+= 1.0f - (2.0f * rand.nextFloat());
			}
		}
		System.out.println((float)(System.currentTimeMillis() - tm) / 1000.0f);
		
		// PLANT TREES
		System.out.print("Planting Trees...");
		tm = System.currentTimeMillis();
		
		int xx = 2;
		while( xx < WORLD_WIDTH - 2 ) {
			// Find ground level
			for( int y = 50; y < WORLD_HEIGHT; y++ ) {
				if( blocks[xx][y].id == Block.dirt.id ) {
					plantTree(xx, y - 1, false);
					break;
				}
			}
			xx += 5 + (Math.abs(rand.nextInt()) % 50);
		}
		System.out.println((float)(System.currentTimeMillis() - tm) / 1000.0f);
		
		// GROW VINES
		System.out.print("Growing Vines...");
		tm = System.currentTimeMillis();
		
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			for( int y = 1; y < WORLD_HEIGHT; y++ ) {
				if( blocks[x][y].id == Block.air.getID() && blocks[x][y - 1].id == Block.dirt.getID() ) {
					for( int i = 0; i < 8; i++ ) {
						if( y + i < WORLD_HEIGHT && blocks[x][y + i].id == Block.air.getID() && rand.nextBoolean() ) {
							//blocks[x][y + i] = new BlockVine(x, y + i);
							setBlock(Block.vine, x, y + i, false);
						}
						else
							break;
					}
				}
			}
		}
		System.out.println((float)(System.currentTimeMillis() - tm) / 1000.0f);
		
		// Draw the map
		System.out.print("Drawing Map...");
		tm = System.currentTimeMillis();
		updateMap();
		System.out.println((float)(System.currentTimeMillis() - tm) / 1000.0f);
		
		
		// DONE!!!
		System.out.print("\nTook ");
		System.out.print((float)(System.currentTimeMillis() - tmTotal) / 1000.0f);
		System.out.println(" seconds");
		System.out.println("------------------DONE-----------------\n");
	}
	
	public static void plantTree(int x, int y, boolean connect) {
		if( x >= 2 && y > 20 && x < WORLD_WIDTH - 2 && y < WORLD_HEIGHT - 2 ) {
			// Create randomness
			int base = 2 + (Math.abs(rand.nextInt()) % 3);
			
			// Design:
			//
			//  ###
			// #####
			// #####
			// #####
			//  ###
			//   |
			//   |  <--- base varies in height
			//   |
			
			// Start with leaves,
			int cx = x - 2;
			int cy = y - base - 4;
			for( int xx = 0; xx < 5; xx++ ) {
				for( int yy = 0; yy < 5; yy++ ) {
					if( (xx != 0 || yy != 0) && (yy != 4 || xx != 0) && (xx != 4 || yy != 0) && (xx != 4 || yy != 4) ) {
						boolean hidden;
						hidden = (xx == 2) && (yy > 1);
						setBlock(Block.leaves, cx + xx, cy + yy, connect);
					}
				}
			}
			//then make base (trunk)
			for( int i = 0; i < base; i++ ) {
				setBlock(Block.tree, x, y - i, connect);
			}
		}
	}
	public void plantTree(int x, int y) {
		plantTree(x, y, true);
	}
	
	public void updateBlocks() {
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			for( int y = 0; y < WORLD_HEIGHT; y++ ) {
				blocks[x][y].update();
			}
		}
	}
	public void updateWaterGrid() {
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			for( int y = 0; y < WORLD_HEIGHT; y++ ) {
				if( watergrid[x][y] > 0 ) {
					// First Priority: Move water down
					if( y < WORLD_HEIGHT - 1 ) {
						if( blocks[x][y + 1].id == Block.air.id && watergrid[x][y + 1] < 100 ) {
							// add water to block below
							watergrid[x][y + 1] += watergrid[x][y];
							if( watergrid[x][y + 1] > 100 ) {
								watergrid[x][y] -= watergrid[x][y + 1] - 100;
								watergrid[x][y + 1] = 100;
							}
							else
								watergrid[x][y] = 0;
							continue;
						}
					}
					// Second Priority: Move water laterally
					boolean left  = false;
					boolean right = false;
					if( x > 0 ) {
						if( blocks[x - 1][y].id == Block.air.id )
							left = true;
					}
					if( x < WORLD_WIDTH - 1 ) {
						if( blocks[x + 1][y].id == Block.air.id )
							right = true;
					}
					if( right && left ) {
						// Level the 3 blocks using averages
						int avg = (int)(((float)(watergrid[x][y] + watergrid[x - 1][y] + watergrid[x + 1][y]) / 3.0f) + 0.5f);
						watergrid[x][y] = avg;
						watergrid[x - 1][y] = avg;
						watergrid[x + 1][y] = avg;
					}
					else if( left ) {
						// Level the 2 blocks using averages
						if( watergrid[x][y] < 100 || watergrid[x - 1][y] < 100 ) {
							int avg = (int)(((float)(watergrid[x][y] + watergrid[x - 1][y]) / 2.0f) + 0.5f);
							watergrid[x][y] = avg;
							watergrid[x - 1][y] = avg;
						}
					}
					else if( right ) {
						// Level the 2 blocks using averages
						if( watergrid[x][y] < 100 || watergrid[x + 1][y] < 100 ) {
							int avg = (int)(((float)(watergrid[x][y] + watergrid[x + 1][y]) / 2.0f) + 0.5f);
							watergrid[x][y] = avg;
							watergrid[x + 1][y] = avg;
						}
					}
				}
			}
		}
	}
	public void drawWaterGrid(Graphics g) {
		for( int x = 0; x < WORLD_WIDTH; x++ ) {
			for( int y = 0; y < WORLD_HEIGHT; y++ ) {
				if( watergrid[x][y] > 0 ) {
					int h = (int)(((float)watergrid[x][y] / 100.0f) * 16.0f);
					g.setColor(Color.blue);
					g.fillRect(x * 16 - VIEW_X, (y * 16) + 16 - h - VIEW_Y, 16, h);
				}
			}
		}
	}
	
	public void update() {
		// Step Event
		// Get Mouse Position
		Point mousePosition = this.getMousePosition();
		if( mousePosition != null ) {
			cursorX = (int)mousePosition.getX();
			cursorY = (int)mousePosition.getY();
			mouseX = (int)(((float)(cursorX + VIEW_X) / 16.0f));
			mouseY = (int)(((float)(cursorY + VIEW_Y) / 16.0f));
		}
		
		// Check for Game End
		if( keyPressed[KeyEvent.VK_ESCAPE] ) {
			Main.gameEnd();
		}
		// Check for Restart
		if( keyPressed[KeyEvent.VK_R] ) {
			initBlocks();
			player.respawn();
		}
		
		if( mouseButtonLeft ) {
			// Create a block/water
			if( mouseX >= 0 && mouseY >= 0 && mouseX < WORLD_WIDTH && mouseY < WORLD_HEIGHT ) {
				if( keyDown[KeyEvent.VK_CONTROL] ) {
					// create water
					watergrid[mouseX][mouseY] = 100;
				}
				else {
					// create a block
					if( !player.blockMeeting(player.x, player.y, mouseX, mouseY)) {
						setBlock(Block.stone, mouseX, mouseY);
						connectArea(mouseX, mouseY);
					}
				}
			}
		}
		if( mouseButtonRight ) {
			if( mouseX >= 0 && mouseY >= 0 && mouseX < WORLD_WIDTH && mouseY < WORLD_HEIGHT ) {
				if( keyDown[KeyEvent.VK_CONTROL] ) {
					// create water
					watergrid[mouseX][mouseY] = 0;
				}
				else {
					if( blocks[mouseX][mouseY].id != Block.air.getID() ) {
						blockDestroyProgress = Math.max(0, blockDestroyProgress);
						// Destroy a block over time
						if( mouseX != blockDestroyX || mouseY != blockDestroyY )
							blockDestroyProgress = 0;
	
						blockDestroyX = mouseX;
						blockDestroyY = mouseY;
	
						blockDestroyProgress += 5;
						if( blockDestroyProgress >= 1 ) {
							// call block destroy() function
							
							Block b = blocks[mouseX][mouseY];
							setBlock(Block.air, mouseX, mouseY);
							b.destroy();
	
							connectArea(mouseX, mouseY);
							blockDestroyProgress = 0;
						}
					}
					else
						blockDestroyProgress = -1;
				}
			}
		}
		else
			blockDestroyProgress = -1;

		updateBlocks();
		updateWaterGrid();
		
		player.update();
		
		// Redraw the screen
		
		repaint();
		
		for( int i = 0; i < KEYS_DIM; i++ ) {
			keyPressed[i] 	= false;
			keyReleased[i] 	= false;
		}
	}
	
	public void render() {
		Graphics g = buffer.getGraphics();
		
		// Draw Sky Background
		g.drawImage(ImageLoader.imageSkygrad, 0, 0, null);
		
		// Set View
		VIEW_X = Math.min((WORLD_WIDTH * 16) - VIEW_WIDTH, Math.max(0, (int)(player.x * 16.0f) - (VIEW_WIDTH / 2)));
		VIEW_Y = Math.min((WORLD_HEIGHT * 16) - VIEW_HEIGHT, Math.max(0, (int)(player.y * 16.0f) - (VIEW_HEIGHT / 2)));
		
		
		int startx = Math.max(0, (int)((float)VIEW_X / 16.0f));
		int starty = Math.max(0, (int)((float)VIEW_Y / 16.0f));
		int endx = Math.min(WORLD_WIDTH, startx + (int)((float)VIEW_WIDTH / 16.0f) + 1);
		int endy = Math.min(WORLD_HEIGHT, starty + (int)((float)VIEW_HEIGHT / 16.0f) + 1);
		
		// Draw Backdrops
		for( int x = startx; x < endx; x++ ) {
			for( int y = starty; y < endy; y++ ) {
				g.drawImage(imageBackdrops,
						(x * 16) - VIEW_X,
						(y * 16) - VIEW_Y,
						(x * 16) - VIEW_X + 16,
						(y * 16) - VIEW_Y + 16, 
						backdrops[x][y] * 16,
						0,
						(backdrops[x][y] * 16) + 16,
						16,
						null);
			}
		}
		
		// Draw Player
		player.draw(g);
		
		// Draw Blocks
		for( int x = startx; x < endx; x++ ) {
			for( int y = starty; y < endy; y++ ) {
				blocks[x][y].draw(g, (x * 16) - VIEW_X, (y * 16) - VIEW_Y);
			}
		}
		
		// draw water grid
		drawWaterGrid(g);
		
		// Draw breaking cracks
		if( mouseButtonRight && blockDestroyProgress >= 0 ) {
			int index = (int)((float)blockDestroyProgress / 10.0f);
			g.drawImage(ImageLoader.imageCracks, (mouseX * 16) - VIEW_X, (mouseY * 16) - VIEW_Y,
					(mouseX * 16) + 16 - VIEW_X, (mouseY * 16) + 16 - VIEW_Y, index * 16, 0,
					(index * 16) + 16, 16, null);
		}

		// Draw Cursor Highlight
		g.setColor(new Color(0, 0, 0, 0.5f));
		g.drawRect((mouseX * 16) - VIEW_X, (mouseY * 16) - VIEW_Y, 16, 16);
		
		// Draw map
		if( keyDown[KeyEvent.VK_M] ) {
			// update map every so often
			if( Main.step % 60 == 0 )
				updateMap();
			
			g.drawImage(imageMap, 0, 0, null);
			// draw player position
			g.setColor(Color.red);
			g.drawRect((int)player.x, (int)player.y, 0, 1);
		}
		
		// Draw FPS
		g.setColor(Color.white);
		
		g.drawString(String.valueOf(Main.fpsCurrent), 4, 32);
		g.drawString(String.valueOf(Main.fpsAverage), 4, 52);
	}
	
	public void paintComponent(Graphics g) {
		render();
		//g.drawImage(buffer, 0, 0, Main.frame.getWidth(), Main.frame.getHeight(), null);
		g.drawImage(buffer, 0, 0, null);
	}
}
