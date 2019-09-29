package tp.planner;

import java.util.ArrayList;

public class ItemGroup {
	private String name;
	private int index;
	private ArrayList<Item> items;
	
	public ItemGroup(String name, int index) {
		this.name  = name;
		this.index = index;
		this.items = new ArrayList<Item>();
	}
	
	public void addItem(String itemName) {
		Item item = ItemData.find(itemName);
		if (item != null)
			items.add(item);
		else
			System.out.println("Unknown item named " + itemName);
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public Item getItem(int index) {
		return items.get(index);
	}
	
	public int getSize() {
		return items.size();
	}
	
	public String getName() {
		return name;
	}
	
	public int findItem(Item item) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) == item)
				return i;
		}
		return -1;
	}
}
