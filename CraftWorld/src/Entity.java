
/* 
 * All game objects that must be updated
 * and drawn should extend this Entity class
 */
public class Entity {
	public int id;
	
	public Entity(int id) {
		this.id = id;
	}
	
	public void collected() {
		// Remove from list of Entities
		//  and add id to player's inventory
		destroy();
	}
	public void update() {}
	public void draw() {}
	public void destroy() {}
}
