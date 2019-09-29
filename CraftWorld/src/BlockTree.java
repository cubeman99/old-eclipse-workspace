import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;


public class BlockTree extends Block {
	public int[] subxMatrix = new int[4];
	public Image imageTree = ImageLoader.imageTree;
	
	public BlockTree(int x, int y) {
		super(ImageLoader.imageTerrain, true, x, y);
		suby = 3;
	}
	public BlockTree() {
		super();
	}
	public Block getNewBlock(int x, int y) {
		return new BlockTree(x, y).setID(id);
	}
	
	public void update() {
		
	}
	
	public void connect() {
		subxMatrix = Connecter.blockConnect(this);
	}
	
	public void draw(Graphics g, int x, int y) {
		g.drawImage(imageTree, x, y, x + 16, y + 16, 0, 0, 16, 16, null);
		
		// Draw Connection overlay
		drawSub(g, 0, x, y, 8, 0);
		drawSub(g, 1, x, y, 0, 0);
		drawSub(g, 2, x, y, 0, 8);
		drawSub(g, 3, x, y, 8, 8);
	}
	
	private void drawSub(Graphics g, int parti, int x, int y, int offx, int offy) {
		int sx = subxMatrix[parti];
		int sy = suby;
		int nx = x + offx;
		int ny = y + offy;
		g.drawImage(image, nx, ny, nx + 8, ny + 8, sx * 8, sy * 8, (sx * 8) + 8, (sy * 8) + 8, null);
	}
}
