import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.swing.*;


public class SandControl extends JComponent {
	public static final int SAND_WIDTH  = 64;
	public static final int SAND_HEIGHT = 64;
	public static final int SPACE_SIZE  = 8;
	public static final int PWIDTH  = SAND_WIDTH  * SPACE_SIZE;
	public static final int PHEIGHT = SAND_HEIGHT * SPACE_SIZE;
	
	public static final int TOOL_ERASER = 0;
	public static final int TOOL_STATIC = 1;
	public static final int TOOL_SAND   = 2;
	
	public Image buffer;
	public Image backGrid;
	
	public Block[][] blocklist;
	
	public Point posMouse;
	public boolean mouseLeftDown  = false;
	public boolean mouseRightDown = false;
	
	public int toolIndex = TOOL_SAND;
	public boolean choosing_color = false;
	public ColorWindow color_window;
	public Color colorSand = Color.red;
	
	public SandControl() {
		buffer = new BufferedImage(PWIDTH, PHEIGHT, BufferedImage.TYPE_INT_RGB);
		
		backGrid = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
		Graphics g = backGrid.getGraphics();
		g.fillRect(0, 0, SPACE_SIZE, SPACE_SIZE);
		
		installListeners();
		initBlocks();
	}
	
	private void installListeners() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if( e.getButton() == MouseEvent.BUTTON1 )
					mouseLeftDown = true;
				else if( e.getButton() == MouseEvent.BUTTON3 )
					mouseRightDown = true;
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if( e.getButton() == MouseEvent.BUTTON1 )
					mouseLeftDown = false;
				else if( e.getButton() == MouseEvent.BUTTON3 )
					mouseRightDown = false;
			}
		});
	}
	
	public void initBlocks() {
		blocklist = new Block[SAND_WIDTH][SAND_HEIGHT];
		posMouse = new Point(0, 0);
		
		for( int x = 0; x < SAND_WIDTH; x++ ) {
			for( int y = 0; y < SAND_HEIGHT; y++ ) {
				blocklist[x][y] = new Block(Block.TYPE_NULL);
				if( x == 0 || y == 0 || x == SAND_WIDTH - 1 || y == SAND_HEIGHT - 1 ) {
					blocklist[x][y] = new Block(Block.TYPE_STATIC, Color.black);
					blocklist[x][y].permanent = true;
				}
				else
					blocklist[x][y] = new Block(Block.TYPE_NULL);
			}
		}
	}
	
	public void setTool(int index) {
		toolIndex = index;
	}
	
	public void updateBlocks() {
		for( int x = 0; x < SAND_WIDTH; x++ ) {
			for( int y = 0; y < SAND_HEIGHT; y++ ) {
				blocklist[x][y].updated = false;
				blocklist[x][y].moved = false;
			}
		}
		// Only update blocks inside boundaries
		// 		(to avoid out-of-bounds exception)
		for( int x = 1; x < SAND_WIDTH - 1; x++ ) {
			for( int y = SAND_HEIGHT - 2; y > 0; y-- ) {
				Block b = blocklist[x][y];
				if( !b.updated && b.type == Block.TYPE_SAND ) {
					b.updated = true;
					int side_value = 1;
					if( Math.random() < 0.5d )
						side_value = -1;
					
					if( !blocklist[x][y + 1].getSolid() ) {
						// Fall down a space
						b.moved = true;
						blocklist[x][y + 1] = b;
						blocklist[x][y] = new Block(Block.TYPE_NULL);
					}
					else if( !blocklist[x + side_value][y + 1].getSolid() && !blocklist[x + side_value][y].getSolid() ) {
						// Fall diagonally to the Right
						b.moved = true;
						blocklist[x + side_value][y + 1] = b;
						blocklist[x][y] = new Block(Block.TYPE_NULL);
					}
					else if( !blocklist[x - side_value][y + 1].getSolid() && !blocklist[x - side_value][y].getSolid() ) {
						// Fall diagonally to the Left
						b.moved = true;
						blocklist[x - side_value][y + 1] = b;
						blocklist[x][y] = new Block(Block.TYPE_NULL);
					}
				}
			}
		}
	}
	
	public void update() {
		posMouse = this.getMousePosition();
		if( posMouse == null )
			posMouse = new Point(-100, -100);
		posMouse.x = (int) Math.floor((double)posMouse.x / (double)SPACE_SIZE);
		posMouse.y = (int) Math.floor((double)posMouse.y / (double)SPACE_SIZE);
		
		if( posMouse.x > 0 && posMouse.y > 0 && posMouse.x < SAND_WIDTH - 1
				&& posMouse.y < SAND_HEIGHT - 1 )
		{
			if(  mouseLeftDown ) {
				if( toolIndex == TOOL_SAND )
					blocklist[posMouse.x][posMouse.y] = new Block(Block.TYPE_SAND, colorSand);
				else if( toolIndex == TOOL_STATIC )
					blocklist[posMouse.x][posMouse.y] = new Block(Block.TYPE_STATIC, colorSand);
			}
			if(  mouseRightDown || toolIndex == TOOL_ERASER )
				blocklist[posMouse.x][posMouse.y] = new Block(Block.TYPE_NULL);
		}
		
		colorSand = Game.colorChooser.getColor();
			
		
		updateBlocks();
		repaint();
	}
	
	public void drawBlocks() {
		Graphics g = buffer.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, SPACE_SIZE * SAND_WIDTH, SPACE_SIZE * SAND_HEIGHT);
		
		for( int x = 0; x < SAND_WIDTH; x++ ) {
			for( int y = 0; y < SAND_HEIGHT; y++ ) {
				Block b = blocklist[x][y];
				if( b.type != Block.TYPE_NULL ) {
					g.setColor(b.color);
					if( b.moved ) {
						// Set to a lighter Color
						g.setColor(new Color(
								b.color.getRed(), 
								b.color.getGreen(),
								b.color.getBlue(),
								180
								));
					}
					g.fillRect(x * SPACE_SIZE, y * SPACE_SIZE, SPACE_SIZE, SPACE_SIZE);
				}
				else
					g.drawImage(backGrid, x * SPACE_SIZE, y * SPACE_SIZE, null);
			}
		}
	}
	
	
	public void paintComponent(Graphics g) {
		drawBlocks();
		g.drawImage(buffer, 0, 0, null);
		
		g.drawRect(posMouse.x * SPACE_SIZE, posMouse.y * SPACE_SIZE, SPACE_SIZE, SPACE_SIZE);
		//g.drawImage(backGrid, 0, 0, null);
	}
}
