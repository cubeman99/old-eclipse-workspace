import java.awt.Image;

import javax.swing.ImageIcon;


public class Block {
	public static final int BLOCK_AIR		= 0;
	public static final int BLOCK_DIRT		= 1;
	public static final int BLOCK_GRASS		= 2;
	public static final int BLOCK_STONE		= 3; 
	public static final int BLOCK_SAND		= 4;
	public static final int BLOCK_WATER		= 5;
	
	public static final int WIDTH  = 12;
	public static final int HEIGHT = 6;
	public static final int DEPTH  = 7;
	
	public BlockType type;
	public boolean solid;
	public boolean draw = true;
	
	public Block() {
		type = BlockType.TYPE_AIR;
		solid = false;
	}
	
	public Block(BlockType type) {
		this.type = type;
		solid = type.solid;
	}
	
}
