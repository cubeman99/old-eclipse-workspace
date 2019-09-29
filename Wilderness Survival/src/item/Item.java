package item;

/**
 * Represents an item than can be picked up,
 * can be stored in an inventory, and can possibly
 * be used or placed.
 * 
 * @author David Jordan
 */
public class Item {
	// Defining Variables:
	public int id;
	public String name;
	public int stackSize;
	public ItemAction action;
	public Recipe recipe;
	public String actionDescription;
	
	// Entity Variables:
	public int count;
	
	

	// ====== CONSTRUCTORS ====== //
	
	/** Construct default values for entity variables and defining variables. **/
	public Item() {
		this.count = 0;
		this.id                = -1;
		this.name              = "unknown";
		this.stackSize         = 10;
		this.actionDescription = "";
		this.action            = null;
		this.recipe            = null;
	}
	
	public Item(int id, String name, int stackSize, String actionDescription, ItemAction action, Recipe recipe) {
		this();
		this.id                = id;
		this.name              = name;
		this.stackSize         = stackSize;
		this.actionDescription = actionDescription;
		this.action            = action;
		this.recipe            = recipe;
	}
	
	/** Construct an item with all the defining variables of a template item. **/
	public Item(Item template) {
		this();
		this.id                = template.id;
		this.name              = template.name;
		this.stackSize         = template.stackSize;
		this.actionDescription = template.actionDescription;
		this.action            = template.action;
		this.recipe            = template.recipe;
	}
	
	
	
	// ====== ACCESSORS ====== //
	
	/** Return a new item with the same defining variables as this item. **/
	public Item getCopy() {
		return new Item(this);
	}
	
	
	
	// ====== MUTATORS ====== //
	
	/** Use the item, returning whether it has a use. **/
	public boolean use() {
		if (action != null) {
			action.use();
			return true;
		}
		return false;
	}
}
