package graphics.library;

import graphics.Tileset;

/**
 * The library that stores all game tilsets.
 * @author	Robert Jordan
 */
public class Tilesets {

	// ======================= Members ========================
	
	public Tileset hudElements;
	public Tileset itemTileset;
	public Tileset weapons;
	public Tileset specialEffects;
	public Tileset physicalEffects;
	
	public Tileset linkNormal;
	public Tileset linkNormalRed;
	public Tileset linkNormalBlue;
	public Tileset linkNormalHurt;

	public Tileset itemsSmall;
	public Tileset itemsLarge;
	public Tileset menuSmall;
	public Tileset menuLarge;

	public Tileset itemsSmallLight;
	public Tileset itemsLargeLight;
	public Tileset menuSmallLight;
	public Tileset menuLargeLight;

	public Tileset testTiles;
	public Tileset landformTiles;
	public Tileset specialTiles;
	public Tileset waterTiles;
	
	// ===================== Constructors =====================
	
	/** Constructs the tilsets library. */
	public Tilesets() {
		hudElements = new Tileset("hud_elements", 8, 8, 1, 1);
		itemTileset = new Tileset("items", 16, 16, 1, 1);
		weapons = new Tileset("weapons", 16, 16, 1, 1, 17, 17);
		specialEffects = new Tileset("special_effects", 16, 16, 1, 1, 17, 17);
		physicalEffects = new Tileset("physical_effects", 16, 16, 1, 1, 17, 17);
		
		linkNormal = new Tileset("link_normal", 16, 16, 1, 1, 17, 17);
		linkNormalRed = new Tileset("link_normal_red", 16, 16, 1, 1, 17, 17);
		linkNormalBlue = new Tileset("link_normal_blue", 16, 16, 1, 1, 17, 17);
		
		itemsSmall = new Tileset("items_small", 8, 16, 1, 1, 17, 17);
		itemsLarge = new Tileset("items_large", 16, 16, 1, 1, 17, 17);
		menuSmall = new Tileset("menu_small", 8, 8, 1, 1, 17, 17);
		menuLarge = new Tileset("menu_large", 16, 16, 1, 1, 17, 17);
		
		itemsSmallLight = new Tileset("items_small_light", 8, 16, 1, 1, 17, 17);
		itemsLargeLight = new Tileset("items_large_light", 16, 16, 1, 1, 17, 17);
		menuSmallLight = new Tileset("menu_small_light", 8, 8, 1, 1, 17, 17);
		menuLargeLight = new Tileset("menu_large_light", 16, 16, 1, 1, 17, 17);
		
		testTiles = new Tileset("test_tiles", 16, 16, 1, 1, 17, 17);
		landformTiles = new Tileset("landforms", 16, 16, 1, 1, 17, 17);
		specialTiles = new Tileset("special", 16, 16, 1, 1, 17, 17);
		waterTiles = new Tileset("water", 16, 16, 1, 1, 17, 17);
	}
}