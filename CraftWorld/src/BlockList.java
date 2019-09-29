import java.util.ArrayList;


public class BlockList {
	public ArrayList<Block> blocks;
	
	public BlockList() {
		blocks = new ArrayList<Block>();
	}
	
	
	public void addBlock(Block b) {
		blocks.add(b);
	}
	
	public Block get(int id) {
		return blocks.get(id);
	}
	
	public int size() {
		return blocks.size();
	}
	
	public Block[] getArray() {
		// Construct an Array of Blocks
		Block[] bArray = new Block[size()];
		for( int i = 0; i < size(); i++ )
			bArray[i] = get(i);
		
		return bArray;
	}
}
