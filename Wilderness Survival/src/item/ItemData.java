package item;

import java.util.ArrayList;


/**
 * This static class creates and stores the 
 * information about all items in the game.
 * 
 * @author David Jordan
 */
public class ItemData {
	public static ArrayList<Item> items = new ArrayList<Item>();
	
	
	/** Initialize; constructing all item data. **/
	public static void initialize() {
		//////////////////////////////////
		//  CONSTRUCT ALLL ITEMS HERE:  //
		//////////////////////////////////
		
		
		// Rock
		addItem("Rock", 10, "Throw rock", new ItemAction() {
			public void use() {
				System.out.println("You threw a rock, yay!!!");
			}
		});
		
		// Stick
		addItem("Stick", 16);
		
	}
	

	
	// ====== ACCESSORS ====== //
	
	public static Item getNewItem(int id) {
		if (items.size() <= id)
			return null;
		return items.get(id).getCopy();
	}
	
	public static Item getNewItem(String name) {
		for (Item item : items) {
			if (item.name.equalsIgnoreCase(name))
				return item;
		}
		return null;
	}
	
	

	// ====== MUTATORS ====== //
	
	private static void addItem(int id, String name, int stackSize, String actionDescription, ItemAction action) {
		items.add(new Item(id, name, stackSize, actionDescription, action, null));
	}
	
	private static void addItem(String name, int stackSize, String actionDescription, ItemAction action) {
		items.add(new Item(items.size(), name, stackSize, actionDescription, action, null));
	}
	
	private static void addItem(String name, int stackSize) {
		items.add(new Item(items.size(), name, stackSize, "", null, null));
	}
}
