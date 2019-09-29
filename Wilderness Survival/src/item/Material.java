package item;

/**
 * This class represents a material needed
 * in a crafting recipe. The material contains
 * the data for what item id it is and how
 * much is needed.
 * 
 * @author David Jordan
 */
public class Material {
	public int id;
	public int amount;
	

	// ====== CONSTRUCTORS ====== //
	
	/** Construct a material with an item ID and amount. **/
	public Material(int id, int amount) {
		this.id     = id;
		this.amount = amount;
	}

	/** Construct a material with an item name and amount. **/
	public Material(String itemName, int amount) {
		this.id     = ItemData.getNewItem(itemName).id;
		this.amount = amount;
	}

	/** Construct a material with an amount of one and a given item ID. **/
	public Material(int id) {
		this(id, 1);
	}

	/** Construct a material with an amount of one and a given item name. **/
	public Material(String itemName) {
		this(itemName, 1);
	}
	
	
	
	// ====== ACCESSORS ====== //
	
	/** Return whether an item is this given material. **/
	public boolean isItem(Item checkItem) {
		return (id == checkItem.id);
	}
	
	/** Return a new Item of this material's item ID. **/
	public Item getItem() {
		return ItemData.getNewItem(id);
	}
}
