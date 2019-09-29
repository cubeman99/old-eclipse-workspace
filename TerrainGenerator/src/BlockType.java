import java.awt.Image;

import javax.swing.ImageIcon;


public class BlockType {
	public static final BlockType TYPE_AIR		= new BlockType(null, false);
	public static final BlockType TYPE_GRASS	= new BlockType("GrassBlock", true);
	public static final BlockType TYPE_DIRT		= new BlockType("DirtBlock", true);
	public static final BlockType TYPE_STONE	= new BlockType("StoneBlock", true);
	public static final BlockType TYPE_SAND		= new BlockType("SandBlock", true);
	public static final BlockType TYPE_WATER	= new BlockType("WaterBlock", true);
	public static final BlockType TYPE_WOOD		= new BlockType("WoodBlock", true);
	public static final BlockType TYPE_LEAF		= new BlockType("LeafBlock", true);
	
	public Image image;
	public boolean solid;
	
	public BlockType(String imagename, boolean solid) {
		this.solid = solid;
		if( imagename != null )
			image = (new ImageIcon(this.getClass().getResource("images/" + imagename + ".PNG"))).getImage();
	}
}
