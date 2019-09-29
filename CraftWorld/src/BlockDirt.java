import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;


public class BlockDirt extends Block {
	public int[] subxMatrix = new int[4];
	public int growtimer = 0;
	public int growtimer_max; // in seconds
	
	public BlockDirt(int x, int y) {
		super(ImageLoader.imageTerrain, true, x, y);
		suby = 0;
		growtimer_max = 20 + (Math.abs(new Random().nextInt()) % 30);
	}
	public BlockDirt() {
		super();
	}
	public Block getNewBlock(int x, int y) {
		return (new BlockDirt(x, y)).setID(id);
	}
	
	public void update() {
		if( worldy < GameControl.WORLD_HEIGHT - 1 ) {
			if( GameControl.blocks[worldx][worldy + 1].id == Block.air.getID() ) {
				// Grow a vine over time
				growtimer += 1;
				if( growtimer > 60 * growtimer_max ) {
					growtimer = 0;
					growtimer_max = 20 + (new Random().nextInt() % 30);
					GameControl.setBlock(Block.vine, worldx, worldy + 1);
				}
			}
		}
	}
	
	public void connect() {
		subxMatrix = Connecter.blockConnect(this);
	}
	
	public void draw(Graphics g, int x, int y) {
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
