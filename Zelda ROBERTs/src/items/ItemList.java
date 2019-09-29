package items;

import items.equipment.Equipment;

/**
 * 
 * @author	Robert Jordan
 */
public class ItemList {

	// ====================== Constants =======================
	
	
	// ====================== Variables =======================
	
	public static Item kokiriEmerald;
	public static Item goronRuby;
	public static Item zoraSaphire;
	
	public static Equipment kokiriTunic;
	public static Equipment goronTunic;
	public static Equipment zoraTunic;
	
	public static Item flippers;
	public static Item mermaidSuit;
	public static Item magicPotion;
	
	// ===================== Constructors =====================
	

	/** Initializes the library collections. */
	public static void initialize() {
		
		kokiriEmerald		= new BasicItem("essence_1", "Koriri Emerald", "Given to you by the great Deku Tree.", 2, 9, true);
		goronRuby			= new BasicItem("essence_2", "Goron Ruby", "Given to you for clearing Dodongo Caverns.", 3, 9, true);
		zoraSaphire			= new BasicItem("essence_3", "Zora Saphire", "Given to you as a wedding gift by Princess Ruto.", 4, 9, true);
		
		kokiriTunic			= new Equipment("kokiri_tunic", "Kokiri Tunic", "Hero's clothes.", 0, 1, true);
		goronTunic			= new Equipment("goron_tunic", "Goron Tunic", "Fire resistant.", 1, 1, true);
		zoraTunic			= new Equipment("zora_tunic", "Zora Tunic", "Breath underwater.", 2, 1, true);
		
		flippers			= new BasicItem("flippers", "Zora's Flippers", "Hit the beach.", 6, 1, true);
		mermaidSuit			= new BasicItem("mermaid_suit", "Mermaid Suit", "Dive underwater", 7, 1, true);
		magicPotion			= new BasicItem("red_potion", "Magic Potion", "Fill your heart!", 6, 3, true);
	}
}
