import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;


public class Chunk {
	public static final int size  = 17;
	public static final int depth = 32;
	public static final int water_level = 13;
	public static TerrainGen terraingen = new TerrainGen();
	
	public Block[][][] blocks;
	public int x;
	public int y;
	public Chunk[] neighbor = new Chunk[8];
	public boolean valid;
	public float[][] terrain;
	public boolean generated = false;
	
	public Chunk() {
		blocks = new Block[size][size][depth];
		for( int xx = 0; xx < size; xx++ ) {
			for( int yy = 0; yy < size; yy++ ) {
				for( int z = 0; z < depth; z++ ) {
					blocks[xx][yy][z] = new Block();
				}
			}
		}
		
		valid = false;
	}
	
	public Chunk(int x, int y) {
		blocks = new Block[size][size][depth];
		for( int xx = 0; xx < size; xx++ ) {
			for( int yy = 0; yy < size; yy++ ) {
				for( int z = 0; z < depth; z++ ) {
					blocks[xx][yy][z] = new Block();
				}
			}
		}
		valid = false;
		
		this.x = x;
		this.y = y;
		valid = true;
		terrain = new float[size][size];
	}
	
	public void fillNeighbors() {
		boolean x1, y1, x2, y2;
		x1 = x > 0;
		y1 = y > 0;
		x2 = x < GamePanel.chunks_width - 1;
		y2 = y < GamePanel.chunks_height - 1;
		
		for( int i = 0; i < 8; i++ )
			neighbor[i] = new Chunk();
		
		if( x2 ) {
			neighbor[0] = GamePanel.chunks[x + 1][y];
			if( y1 )
				neighbor[1] = GamePanel.chunks[x + 1][y - 1];
		}
		if( y1 ) {
			neighbor[2] = GamePanel.chunks[x][y - 1];
			if( x1 )
				neighbor[3] = GamePanel.chunks[x - 1][y - 1];
		}
		if( x1 ) {
			neighbor[4] = GamePanel.chunks[x - 1][y];
			if( y2 )
				neighbor[5] = GamePanel.chunks[x - 1][y + 1];
		}
		if( y2 ) {
			neighbor[6] = GamePanel.chunks[x][y + 1];
			if( x2 )
				neighbor[7] = GamePanel.chunks[x + 1][y + 1];
		}
	}
	
	public void generateTerrain() {
		Random rand = new Random();
		
		generated = true;
		
		for( int xx = 0; xx < size; xx += 1 ) {
			for( int yy = 0; yy < size; yy += 1 ) {
				terrain[xx][yy] = -1;
			}
		}
		
		terrain[0][0]                     = rand.nextFloat() * (depth - 1);
		terrain[0][size - 1]              = rand.nextFloat() * (depth - 1);
		terrain[size - 1][0]              = rand.nextFloat() * (depth - 1);
		terrain[size - 1][size - 1] = rand.nextFloat() * (depth - 1);
		
		fillNeighbors();
		if( neighbor[0].valid && neighbor[0].generated ) {
			for( int i = 0; i < size; i++ )
				terrain[size - 1][i] = neighbor[0].terrain[0][i];
		}
		if( neighbor[1].valid && neighbor[1].generated ) {
			terrain[size - 1][0] = neighbor[1].terrain[0][0];
		}
		if( neighbor[2].valid && neighbor[2].generated ) {
			for( int i = 0; i < size; i++ )
				terrain[i][0] = neighbor[2].terrain[i][size - 1];
		}
		if( neighbor[3].valid && neighbor[3].generated ) {
			terrain[0][0] = neighbor[3].terrain[size - 1][size - 1];
		}
		if( neighbor[4].valid && neighbor[4].generated ) {
			for( int i = 0; i < size; i++ )
				terrain[0][i] = neighbor[4].terrain[size - 1][i];
		}
		if( neighbor[5].valid && neighbor[5].generated ) {
			terrain[0][size - 1] = neighbor[5].terrain[size - 1][0];
		}
		if( neighbor[6].valid && neighbor[6].generated ) {
			for( int i = 0; i < size; i++ )
				terrain[i][size - 1] = neighbor[6].terrain[i][0];
		}
		if( neighbor[7].valid && neighbor[7].generated ) {
			terrain[size - 1][size - 1] = neighbor[7].terrain[0][0];
		}
		
		
		int L = size;
		for( int i = 0; L >= 3; L = (L + 1) / 2, i += 1 ) {
			for( int xx = 0; xx <= size - L; xx += L - 1 ) {
				for( int yy = 0; yy <= size - L; yy += L - 1 ) {
					int halfL = (L - 1) / 2;
					// Give new points the midpoints of their surroundings
					//  then apply randomness
					float r = (float)L / 2.0f;
					//r = 0.0f;
					if( terrain[xx + halfL][yy] < 0 )
						terrain[xx + halfL][yy] = ((terrain[xx][yy] + terrain[xx + L - 1][yy]) / 2.0f) + ((-r / 2.0f) + (rand.nextFloat() * r));
					if( terrain[xx][yy + halfL] < 0 )
						terrain[xx][yy + halfL] = ((terrain[xx][yy] + terrain[xx][yy + L - 1]) / 2.0f) + ((-r / 2.0f) + (rand.nextFloat() * r));
					if( terrain[xx + halfL][yy + L - 1] < 0 )
						terrain[xx + halfL][yy + L - 1] = ((terrain[xx][yy + L - 1] + terrain[xx + L - 1][yy + L - 1]) / 2.0f) + ((-r / 2.0f) + (rand.nextFloat() * r));
					if( terrain[xx + L - 1][yy + halfL] < 0 )
						terrain[xx + L - 1][yy + halfL] = ((terrain[xx + L - 1][yy] + terrain[xx + L - 1][yy + L - 1]) / 2.0f) + ((-r / 2.0f) + (rand.nextFloat() * r));
					
					terrain[xx + halfL][yy + halfL] = (terrain[xx + halfL][yy] + terrain[xx][yy + halfL] +
						terrain[xx + halfL][yy + L - 1] + terrain[xx + L - 1][yy + halfL]) / 4.0f;
				}
			}
		}
		
		for( int xx = 0; xx < size; xx++ ) {
			for( int yy = 0; yy < size; yy++ ) {
				for( int z = 0; z < depth; z++ ) {
					//blocks[xx][yy][z] = new Block();
					if( terrain[xx][yy] > z ) {
						blocks[xx][yy][z] = new Block(BlockType.TYPE_GRASS);
					}
				}
			}
		}
		
		// Set Block Types
		for( int xx = 0; xx < size; xx++ ) {
			for( int yy = 0; yy < size; yy++ ) {
				for( int z = 0; z < depth; z++ ) {
					
					blocks[xx][yy][z].draw = true;
					
					if( blocks[xx][yy][z].type.solid ) {
						blocks[xx][yy][z].type = BlockType.TYPE_GRASS;
						
						if( z < water_level + 2 ) {
							blocks[xx][yy][z].type = BlockType.TYPE_SAND;
						}
						else if( z < depth - 1 ) {
							if( blocks[xx][yy][z + 1].type.solid )
								blocks[xx][yy][z].type = BlockType.TYPE_DIRT;
						}
						if( z < terrain[xx][yy] - 2 - (int)(new Random().nextFloat() * 3.0f) )
							blocks[xx][yy][z].type = BlockType.TYPE_STONE;
						
						// Don't Draw if its not see-able
						if( xx < size - 1 && yy < size - 1 && z < depth - 2 ) {
							if( blocks[xx + 1][yy][z].type.solid && blocks[xx + 1][yy][z + 1].type.solid && blocks[xx][yy + 1][z].type.solid &&
									blocks[xx][yy + 1][z + 1].type.solid ) {
								blocks[xx][yy][z].draw = false;
							}
						}
					}
					else if( z < water_level ) {
						blocks[xx][yy][z].draw = true;
						blocks[xx][yy][z].type = BlockType.TYPE_WATER;
					}
				}
			}
		}
		
		// Plant Trees
		for( int xx = 0; xx < size; xx++ ) {
			for( int yy = 0; yy < size; yy++ ) {
				for( int z = 0; z < depth; z++ ) {
					if( blocks[xx][yy][z].type == BlockType.TYPE_GRASS ) {
						if( new Random().nextInt(100) > 90 )
							plantTree(xx, yy, z + 1);
					}
				}
			}
		}
		
		
	}
	
	public void plantTree(int x, int y, int z) {
		int base_height = 2;
		base_height += new Random().nextInt(3);
		
		if( z + base_height + 6 < depth && x >= 5 && y >= 5 && x < size - 5 && y < size - 5 ) {
			// Build Leaves
			for( int i = 0; i < 4; i++ ) {
				if( i == 0 || i == 3 ) {
					for( int xx = 0; xx < 3; xx++ ) {
						for( int yy = 0; yy < 3; yy++ ) {
							blocks[x + xx - 1][y + yy - 1][z + base_height + i].type = BlockType.TYPE_LEAF;
						}
					}
				}
				else {
					for( int xx = 0; xx < 5; xx++ ) {
						for( int yy = 0; yy < 5; yy++ ) {
							if( (yy != 0 && yy != 4) || (xx != 0 && xx != 4) )
							blocks[x + xx - 2][y + yy - 2][z + base_height + i].type = BlockType.TYPE_LEAF;
						}
					}
				}
			}
			
			// Build Trunk
			for( int i = 0; i < base_height + 2; i++ ) {
				blocks[x][y][z + i].type = BlockType.TYPE_WOOD;
			}
		}
	}
	
	public void draw(Graphics g, int basex, int basey) {
		float draw_scale = GamePanel.draw_scale;
		
		for( int xx = 0; xx < size; xx++ ) {
			for( int yy = 0; yy < size; yy++ ) {
				for( int z = 0; z < depth; z++ ) {
					boolean draw = false;
					Image image = GamePanel.image_dirtblock;
					
					if( blocks[xx][yy][z].draw && blocks[xx][yy][z].type.solid ) {
						draw = true;
						if( xx < size - 1 && yy < size - 1 && z < depth - 2 ) {
							if( blocks[xx + 1][yy][z].solid && blocks[xx + 1][yy][z + 1].solid && blocks[xx][yy + 1][z].solid &&
									blocks[xx][yy + 1][z + 1].solid ) {
								draw = false;
							}
						}
					}
					//draw = true;
					if( draw ) {
						int ax = xx + (x * size) - 1;
						int ay = yy + (y * size) - 1;
						
						int drawx = basex + (ax * (Block.WIDTH / 2)) - (ay * (Block.WIDTH / 2));
						int drawy = basey + (ax * (Block.HEIGHT / 2)) + (ay * (Block.HEIGHT / 2));
						g.drawImage(blocks[xx][yy][z].type.image, (int)((float)drawx * draw_scale), (int)((float)(drawy - z * (Block.DEPTH)) * draw_scale), 
								(int)(12 * draw_scale), (int)(12 * draw_scale), null);
					}
				}
			}
		}
	}
}
