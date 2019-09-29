package tp.planner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ItemGroupData {
	private static ArrayList<ItemGroup> itemGroups;
	
	public static void initialize() {
		
		itemGroups = new ArrayList<ItemGroup>();
		
		try {
			Scanner reader = new Scanner(new File("itemGroupList.txt"));
			
			while (reader.hasNext()) {
				String str = reader.nextLine();
				
				if (str.length() > 0) {
					if (str.charAt(0) == ':')
						addGroup(str.substring(1));
					else
						addItem(str);
				}
			}
			
			reader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		addGroup("All");
		for (int i = 0; i < ItemData.getTotalItems(); i++)
			addItem(ItemData.get(i));
	}
	
	private static void addGroup(String name) {
		itemGroups.add(new ItemGroup(name, itemGroups.size()));
	}
	
	private static void addItem(String name) {
		itemGroups.get(itemGroups.size() - 1).addItem(name);
	}
	
	private static void addItem(Item item) {
		itemGroups.get(itemGroups.size() - 1).addItem(item);
	}
	
	public static ItemGroup get(int index) {
		return itemGroups.get(index);
	}
	
	public static int getTotalGroups() {
		return itemGroups.size();
	}
}
