import java.awt.Graphics;


public class Entity {
	public int id;
	public int depth = 0;
	public boolean removed = false;
	public String classname = "";
	
	
	public Entity() {
		// Add this Entity to the entity list
		// and call the onCreate method
		id = PollyWorld.entity_list.size();
		PollyWorld.entity_list.add(this);
		onCreate();
	}
	
	public Entity(String classname) {
		this();
		this.classname = classname;
	}
	
	public Entity(String classname, int depth) {
		this();
		this.classname = classname;
		this.depth = depth;
	}
	
	public Entity(int depth) {
		this();
		this.depth = depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public int getDepth() {
		return(depth);
	}
	
	public int getID() {
		return(id);
	}
	
	// Inherited Methods
	public void update() {}
	public void draw(Graphics g) {}
	public void onCreate() {}
	public void onDestroy() {}
	
	public void destroy() {
		// Remove this Entity from entity list
		// and call the onDestroy method
		if( !removed )
			onDestroy();
		removed = true;
	}
}
