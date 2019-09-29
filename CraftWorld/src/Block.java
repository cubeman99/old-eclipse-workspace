import java.awt.*;
import java.util.Random;


/*
 * The dynamic class Block can be
 * used as an in-game object or
 * a template for other blocks/items
 * 
 */
public class Block {
	
	public Image image;
	public boolean solid = false;
	public int worldx = 0;
	public int worldy = 0;
	public ConnectGroup connectgroup;
	public int id;
	
	public float destroytime = 1.0f; // in seconds
	
	public int subx = 0;
	public int suby = 0;
	
	public Color mapColor = Color.white;
	
	

	public Block(Image image, boolean solid, int x, int y) {
		this.solid 		= solid;
		this.worldx 	= x;
		this.worldy 	= y;
		
		// Load Image
		this.image = image;
	}
	public Block() {}
	
	public int getID() {
		return id;
	}
	
	public Block template() {
		return GameControl.blocklist.get(id);
	}
	
	/*
	 * Construction Modifications
	 * 
	 */
	public Block setID(int id) {
		this.id = id;
		// Add ID to BlockList
		GameControl.blocklist.addBlock(this);
		
		return this;
	}
	
	public Block setMapColor(Color c) {
		mapColor = c;
		return this;
	}
	
	public Block setSolid(boolean solid) {
		this.solid = solid;
		return this;
	}
	
	public Block setConnectGroup() {
		connectgroup = new ConnectGroup();
		return this;
	}
	
	public Block setConnectGroup(ConnectGroup group) {
		connectgroup = group;
		return this;
	}
	
	public Block setDestroyTime(float seconds) {
		destroytime = seconds;
		return this;
	}
	
	// Inherited Methods
	public void connect() {}
	public void update() {}
	public void destroy() {}
	public void onUse() {}
	
	
	public Block getNewBlock(int x, int y) {
		return null;
	}
	
	public void draw(Graphics g, int x, int y) {
		if( image != null )
			g.drawImage(image, x, y, x + 16, y + 16, subx * 16, suby * 16, (subx * 16) + 16, (suby * 16) + 16, null);
	}
	
	
	public static final Block air		= new BlockAir().setID(0).setSolid(true).setDestroyTime(1.0f);
	public static final Block dirt		= new BlockDirt().setID(1).setConnectGroup(ConnectGroup.GROUND).setSolid(true).setDestroyTime(1.0f).setMapColor(new Color(81, 63, 60));
	public static final Block stone		= new BlockStone().setID(2).setConnectGroup(ConnectGroup.GROUND).setSolid(true).setDestroyTime(1.0f).setMapColor(new Color(94, 92, 91));
	public static final Block tree		= new BlockTree().setID(3).setConnectGroup().setSolid(true).setDestroyTime(1.0f).setMapColor(new Color(93, 76, 74));
	public static final Block leaves	= new BlockLeaves().setID(4).setConnectGroup().setSolid(true).setDestroyTime(1.0f).setMapColor(new Color(94, 148, 54));
	public static final Block vine		= new BlockVine().setID(5).setSolid(false).setDestroyTime(1.0f).setMapColor(new Color(0, 200, 0));
	public static final Block brick		= new BlockBrick().setID(6).setConnectGroup().setSolid(true).setDestroyTime(1.0f).setMapColor(new Color(0, 0, 140));
}
