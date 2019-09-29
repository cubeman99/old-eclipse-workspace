package game;

import items.Ammo;
import items.Item;
import items.equipment.Equipment;
import items.weapons.Weapon;

import java.util.ArrayList;

import main.Keyboard;

/**
 * The class managing the player's items and ammunition.
 * @author	Robert Jordan
 */
public class Inventory {

	// ====================== Constants =======================
	
	/** The number of different slots to place items in. */
	public static final int NUM_SLOT_TYPES	= 5;
	/** The capacity of each slot type. */
	public static final int[] SLOT_SIZES	= {2, 16, 3, 15, 8};
	
	public static final int SLOT_EQUIPED	= 0;
	public static final int SLOT_WEAPONS	= 1;
	public static final int SLOT_EQUIPMENT	= 2;
	public static final int SLOT_KEY_ITEMS	= 3;
	public static final int SLOT_ESSENCES	= 4;
	
	// ======================= Members ========================
	
	/** The instance of the game. */
	public GameInstance game;
	
	/** The collection of items for all the slot types. */
	private Item[][] items;
	/** The collection of ammo types the player has encountered. */
	private ArrayList<Ammo> ammunition;
	
	/** The index of the current piece of equipment that is equipped. */
	private int equipmentIndex;
	
	// ===================== Constructors =====================
	
	/** Constructs the default player inventory. */
	public Inventory() {
		this.game	= null;
		
		this.items = new Item[NUM_SLOT_TYPES][];
		for (int i = 0; i < NUM_SLOT_TYPES; i++) {
			this.items[i] = new Item[SLOT_SIZES[i]];
			for (int j = 0; j < SLOT_SIZES[i]; j++) {
				this.items[i][j] = null;
			}
		}
		this.ammunition = new ArrayList<Ammo>();
		this.equipmentIndex = 0;
	}
	/** Initializes the inventory and sets up the container variables. */
	public void initialize(GameInstance game) {
		this.game = game;
		
		for (int i = 0; i < NUM_SLOT_TYPES; i++) {
			for (int j = 0; j < SLOT_SIZES[i]; j++) {
				if (items[i][j] != null)
					items[i][j].initialize(game);
			}
		}
		
		for (int i = 0; i < ammunition.size(); i++) {
			ammunition.get(i).initialize(game);
		}
	}

	// ====================== Inventory =======================

	/** Gets the item in the specified slot, at the specified index. */
	public Item getItem(int slotType, int index) {
		return items[slotType][index];
	}
	/** Gets the item with the specified id. */
	public Item getItem(String id) {
		for (Item[] slot : items) {
			for (Item item : slot) {
				if (item != null) {
					if (item.getID().equals(id))
						return item;
				}
			}
		}
		return null;
	}
	/** Returns true if there is an item in the specified slot, at the specified index. */
	public boolean itemExists(int slotType, int index) {
		return items[slotType][index] != null;
	}
	/** Returns true if there is an item with the specified id. */
	public boolean itemExists(String id) {
		for (Item[] slot : items) {
			for (Item item : slot) {
				if (item != null) {
					if (item.getID().equals(id))
						return true;
				}
			}
		}
		return false;
	}
	/** Adds the item in the specified slot at the specified index. */
	public void addItem(Item item, int slot, int index) {
		items[slot][index] = item;
		if (game != null)
			item.initialize(game);
	}
	/** Removes the item in the specified slot at the specified index. */
	public void removeItem(int slot, int index) {
		items[slot][index] = null;
	}
	/** Removes the item with the given id. */
	public void removeItem(String id) {
		for (int i = 0; i < NUM_SLOT_TYPES; i++) {
			for (int j = 0; j < SLOT_SIZES[i]; j++) {
				if (items[i][j].getID().equals(id))
					items[i][j] = null;
			}
		}
	}
	/** Returns the slot the item is in. */
	public int getItemSlot(Item item) {
		for (int i = 0; i < NUM_SLOT_TYPES; i++) {
			for (int j = 0; j < SLOT_SIZES[i]; j++) {
				if (items[i][j] == item)
					return i;
			}
		}
		return -1;
	}
	/** Returns the index of the item. */
	public int getItemIndex(Item item) {
		for (int i = 0; i < NUM_SLOT_TYPES; i++) {
			for (int j = 0; j < SLOT_SIZES[i]; j++) {
				if (items[i][j] == item)
					return j;
			}
		}
		return -1;
	}
	
	// ======================= Weapons ========================
	
	/** Gets the weapon in the specified slot, at the specified index. */
	public Weapon getWeapon(int slot, int index) {
		return (Weapon)items[slot][index];
	}
	/** Gets the weapon with the specified id. */
	public Weapon getWeapon(String id) {
		return (Weapon)getItem(id);
	}
	/** Adds a weapon to the arsenal. */
	public void addWeapon(Weapon weapon) {
		for (int i = 0; i < SLOT_SIZES[0]; i++) {
			if (items[0][i] == null) {
				items[0][i] = weapon;
				return;
			}
		}
		for (int i = 0; i < SLOT_SIZES[1]; i++) {
			if (items[1][i] == null) {
				items[1][i] = weapon;
				return;
			}
		}
		if (game != null)
			weapon.initialize(game);
	}
	/** Equips the weapon in the specified slot. */
	public void equipWeapon(int weaponIndex, int equipIndex) {
		
		// Store the weapon to be equipped
		Weapon weapon = (Weapon)items[1][weaponIndex];
		
		if (isTwoHandedEquipped()) {
			items[1][weaponIndex] = items[0][0];
			items[0][0] = null;
		}
		else {
			items[1][weaponIndex] = items[0][equipIndex];
			if (weapon != null) {
				if (weapon.isTwoHanded()) {
					for (int i = 0; i < SLOT_SIZES[1]; i++) {
						if (items[1][i] == null) {
							items[1][i] = items[0][1 - equipIndex];
							break;
						}
					}
					items[0][1] = null;
				}
			}
			else {
				items[0][equipIndex] = null;
			}
		}
		if (weapon != null) {
			if (weapon.isTwoHanded())
				items[0][0] = weapon;
			else
				items[0][equipIndex] = weapon;
		}
	}
	/** Returns true if a weapon with the given id is equipped. */
	public boolean isWeaponEquipped(String id) {
		if (items[0][0] != null) {
			if (items[0][0].getID().equals(id))
				return true;
		}
		if (items[0][1] != null) {
			if (items[0][1].getID().equals(id))
				return true;
		}
		return false;
	}
	/** Returns true if a two-handed weapon is equipped. */
	public boolean isTwoHandedEquipped() {
		if (items[0][0] != null)
			return ((Weapon)items[0][0]).isTwoHanded();
		return false;
	}
	/** Returns true if a weapon with the given id's button is pressed. */
	public boolean isWeaponButtonPressed(String id) {
		if (items[0][0] != null) {
			if (items[0][0].getID().equals(id) && Keyboard.b.pressed())
				return true;
			else if (((Weapon)items[0][0]).isTwoHanded() && Keyboard.a.pressed())
				return true;
		}
		if (items[0][1] != null) {
			if (items[0][1].getID().equals(id) && Keyboard.a.pressed())
				return true;
		}
		return false;
	}
	/** Returns true if a weapon with the given id's button is down. */
	public boolean isWeaponButtonDown(String id) {
		if (items[0][0] != null) {
			if (items[0][0].getID().equals(id) && Keyboard.b.down())
				return true;
		}
		if (items[0][1] != null) {
			if (items[0][1].getID().equals(id) && Keyboard.a.down())
				return true;
		}
		return false;
	}

	// ====================== Equipment =======================
	
	/** Equips the piece of equipment at the given index. */
	public void equipEquipment(int index) {
		if (items[SLOT_EQUIPMENT][index] != null) {
			equipmentIndex = index;
		}
	}
	/** Equips the piece of equipment with the given id. */
	public void equipEquipment(String id) {
		for (int i = 0; i < SLOT_SIZES[SLOT_EQUIPMENT]; i++) {
			if (items[SLOT_EQUIPMENT][i] != null) {
				if (items[SLOT_EQUIPMENT][i].getID().equals(id)) {
					equipmentIndex = i;
				}
			}
		}
	}
	/** Returns true if the equipment at the given index is equipped. */
	public boolean isEquipmentEquipped(int index) {
		if (items[SLOT_EQUIPMENT][index] != null) {
			return index == equipmentIndex;
		}
		return false;
	}
	/** Returns true if the equipment with the given id is equipped. */
	public boolean isEquipmentEquipped(String id) {
		if (items[SLOT_EQUIPMENT][equipmentIndex] != null) {
			return items[SLOT_EQUIPMENT][equipmentIndex].getID().equals(id);
		}
		return false;
	}
	
	// ====================== Ammunition ======================

	/** Gets the ammunition with the given id. */
	public Ammo getAmmo(String id) {
		for (int i = 0; i < ammunition.size(); i++) {
			if (ammunition.get(i).getID().equals(id)) {
				return ammunition.get(i);
			}
		}
		return null;
	}
	/** Returns true if the ammunition with the given id exists. */
	public boolean ammoExists(String id) {
		for (int i = 0; i < ammunition.size(); i++) {
			if (ammunition.get(i).getID().equals(id)) {
				return true;
			}
		}
		return false;
	}
	/** Adds the ammunition type to the collection. */
	public void addAmmo(Ammo ammo) {
		ammunition.add(ammo);
		if (game != null)
			ammo.initialize(game);
	}
	/** Removes the ammunition type from the collection. */
	public void removeAmmo(String id) {
		for (int i = 0; i < ammunition.size(); i++) {
			if (ammunition.get(i).getID().equals(id)) {
				ammunition.remove(i);
			}
		}
	}
	/** Gets the ammunition with the given id. */
	public int getAmmoCount(String id) {
		for (int i = 0; i < ammunition.size(); i++) {
			if (ammunition.get(i).getID().equals(id)) {
				return ammunition.get(i).getCount();
			}
		}
		return 0;
	}
}