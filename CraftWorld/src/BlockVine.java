import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;


public class BlockVine extends Block {
	public int randsub;
	public int growtimer = 0;
	public int growtimer_max;
	
	public BlockVine(int x, int y) {
		super(ImageLoader.imageVines, false, x, y);
		randsub = 0;
		if( new Random().nextBoolean() )
			randsub = 1;
		subx = randsub;
		suby = 0;
		resetGrowTimer();
	}
	public BlockVine() {
		super();
	}
	public Block getNewBlock(int x, int y) {
		return new BlockVine(x, y).setID(id);
	}
	
	public void resetGrowTimer() {
		growtimer_max = 20 + (Math.abs(new Random().nextInt()) % 30);
	}
	
	public void update() {
		if( worldy < GameControl.WORLD_HEIGHT - 1 ) {
			if( GameControl.blocks[worldx][worldy + 1].id == Block.air.getID() ) {
				// Grow over time
				growtimer += 1;
				if( growtimer > 60 * growtimer_max ) {
					growtimer = 0;
					resetGrowTimer();
					GameControl.setBlock(Block.vine, worldx, worldy + 1);
				}
			}
		}
		
		if( GameControl.blocks[worldx][worldy - 1].id != Block.vine.getID() && GameControl.blocks[worldx][worldy - 1].id != Block.dirt.getID() )
			GameControl.blocks[worldx][worldy] = new BlockAir(worldx, worldy);
		
	}
	
	public void connect() {
		subx = 2;
		suby = 0;
		if( worldy < GameControl.WORLD_HEIGHT - 1 ) {
			if( GameControl.blocks[worldx][worldy + 1].id == id ) {
				// Sprite continues downward
				subx = randsub;
			}
		}
	}
}
