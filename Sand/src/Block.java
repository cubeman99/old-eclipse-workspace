import java.awt.Color;


public class Block {
	public static final int TYPE_NULL   = 0;
	public static final int TYPE_SAND   = 1;
	public static final int TYPE_STATIC = 2;
	
	public Color color;
	public int type;
	public boolean updated = false;
	public boolean permanent = false;
	public boolean moved = false;
	
	public Block() {
		type = TYPE_NULL;
		
	}
	
	public Block(int type) {
		this.type  = type;
	}
	
	public Block(int type, Color color) {
		this.type  = type;
		this.color = color;
	}
	
	public boolean getSolid() {
		return( type != TYPE_NULL );
	}
}
