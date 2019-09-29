import java.awt.Graphics;


public class BlockAir extends Block {
	public int[] subxMatrix = new int[4];
	public int[] subyMatrix = new int[4];
	
	public BlockAir(int x, int y) {
		super(null, false, x, y);
	}
	public BlockAir() {
		super();
	}
	public Block getNewBlock(int x, int y) {
		return new BlockAir(x, y).setID(id);
	}
}
