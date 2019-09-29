package tp.planner;

import java.util.ArrayList;

public class ItemData {
	private static ArrayList<Item> items;
	private static String itemNameLast;
	private static ConnectionScheme currentScheme;
	private static int currentGridIndex;
	public static Item DIRT_BLOCK, STONE_BLOCK, ASH_BLOCK, MUD_BLOCK, SAPPHIRE,
			RUBY, EMERALD, TOPAZ, AMETHYST, DIAMOND, TORCH, BLUE_TORCH,
			RED_TORCH, GREEN_TORCH, PURPLE_TORCH, WHITE_TORCH, YELLOW_TORCH,
			DEMON_TORCH, CURSED_TORCH, DOOR_CLOSED, DOOR_OPEN, WOOD_PLATFORM,
			ACORN, POT, CANDLE, WATER_CANDLE, BOOK, SIGN, GLOWING_MUSHROOM,
			DAYBLOOM, MOONGLOW, BLINKROOT, DEATHWEED, WATERLEAF, FIREBLOSSOM,
			SHORT_WEEDS, TALL_WEEDS, DEMON_ALTAR, SHADOW_ORB, CORRUPT_WEEDS,
			SHORT_JUNGLE_WEEDS, TALL_JUNGLE_WEEDS, CORAL, MANNEQUIN,
			CRYSTAL_SHARD, SWITCH, BLUE_LIGHT, RED_LIGHT, GREEN_LIGHT, GRASS,
			CORRUPT_GRASS, HALLOWED_GRASS, JUNGLE_GRASS, MUSHROOM_GRASS,
			SPIKES, COBWEB, VINES, CORRUPT_THORNS, JUNGLE_THORNS, JUNGLE_VINES,
			HALLOWED_VINES, WOODEN_BEAM, WIRE, WATER, LAVA, EXPLOSIVES,
			CLAY_POT, WOODEN_TABLE, WOODEN_CHAIR, TOILET, IRON_ANVIL, FURNACE,
			HELLFORGE, WORK_BENCH, CHEST, LOCKED_SHADOW_CHEST, SUNFLOWER, BED,
			MUSHROOM, VILE_MUSHROOM, NATURES_GIFT, JUNGLE_SPORES,
			SHORT_HALLOWED_WEEDS, TALL_HALLOWED_WEEDS, ADAMANTITE_FORGE,
			MYTHRIL_ANVIL, DART_TRAP, RED_PRESSURE_PLATE, BROWN_PRESSURE_PLATE,
			ARMOR_STATUE, PIRANHA_STATUE, GLASS, BOULDER, INLET_PUMP,
			OUTLET_PUMP, ACTIVE_STONE_BLOCK, INACTIVE_STONE_BLOCK, TREE_TRUNK,
			TREE_STUMP, TREE_BRANCH_BARE, TREE_BRANCH, TREE_TOP, TREE_TOP_BARE,
			SHROOM_STEM, SHROOM_TOP;

	
	
	/** Initialize all items. **/
	public static void createAllItems(Control control) {
		items = new ArrayList<Item>();
		itemNameLast = "";
		
		
		currentScheme = null;
		currentGridIndex   = Item.TYPE_OBJECT;
		TORCH        = addItemObj("Torch", 1, 1);
		BLUE_TORCH   = addItemObjPart("Blue Torch", 1, 1, 0, 2);
		RED_TORCH    = addItemObjPart("Red Torch", 1, 1, 0, 4);
		GREEN_TORCH  = addItemObjPart("Green Torch", 1, 1, 0, 6);
		PURPLE_TORCH = addItemObjPart("Purple Torch", 1, 1, 0, 8);
		WHITE_TORCH  = addItemObjPart("White Torch", 1, 1, 0, 10);
		YELLOW_TORCH = addItemObjPart("Yellow Torch", 1, 1, 0, 12);
		DEMON_TORCH  = addItemObjPart("Demon Torch", 1, 1, 0, 14);
		CURSED_TORCH = addItemObjPart("Cursed Torch", 1, 1, 0, 16);
		DOOR_CLOSED  = addItemObj("Wooden Door Closed", 1, 3);
		DOOR_OPEN    = addItemObj("Wooden Door Open", 2, 3, true);
		addItemObj("Bottle", 1, 1);
		addItemObjPart("Healing Potion", 1, 1, 1, 0);
		addItemObjPart("Mana Potion", 1, 1, 2, 0);
		addItemObjPart("Pink Vase", 1, 1, 3, 0);
		addItemObjPart("Mug", 1, 1, 4, 0);
		WOODEN_TABLE  = addItemObj("Wooden Table", 3, 2);
		WOODEN_CHAIR  = addItemObj("Wooden Chair", 1, 2, true);
		TOILET        = addItemObjPart("Toilet", 1, 2, 0, 3, true);
		IRON_ANVIL    = addItemObj("Iron Anvil", 2, 1);
		FURNACE       = addItemObj("Furnace", 3, 2);
		HELLFORGE     = addItemObjPart("Hellforge", 3, 2, 3, 0);
		WORK_BENCH    = addItemObj("Work Bench", 2, 1);
		WOOD_PLATFORM = addItemObj("Wood Platform", 1, 1);
		ACORN         = addItemObj("Acorn", 1, 2);
		CHEST         = addItemObj("Chest", 2, 2);
		addItemObjPart("Gold Chest", 2, 2, 2, 0);
		addItemObjPart("Shadow Chest", 2, 2, 6, 0);
		addItemObjPart("Barrel", 2, 2, 10, 0);
		addItemObjPart("Trash Can", 2, 2, 12, 0);
		addItemObjPart("Locked Gold Chest", 2, 2, 4, 0);
		LOCKED_SHADOW_CHEST = addItemObjPart("Locked Shadow Chest", 2, 2, 8, 0);
		SUNFLOWER = addItemObj("Sunflower", 2, 4);
		POT = addItemObj("Pot", 2, 2);
		addItemObj("Piggy Bank", 2, 1);
		CANDLE       = addItemObj("Candle", 1, 1);
		WATER_CANDLE = addItemObjPart("Water Candle", 1, 1, 1, 0);
		addItemObj("Copper Chandelier", 3, 3);
		addItemObjPart("Silver Chandelier", 3, 3, 3, 0);
		addItemObjPart("Gold Chandelier", 3, 3, 6, 0);
		BOOK     = addItemObj("Book", 1, 1);
		SIGN     = addItemObj("Sign", 2, 2);
		CLAY_POT = addItemObj("Clay Pot", 1, 1);
		BED      = addItemObj("Bed", 4, 2, true);
		addItemObj("Tombstone", 2, 2);
		addItemObj("Loom", 3, 2);
		addItemObj("Piano", 3, 2);
		addItemObj("Dresser", 3, 2);
		addItemObj("Bench", 3, 2);
		addItemObj("Bathtub", 4, 2, true);
		addItemObj("Red Banner", 1, 3);
		addItemObjPart("Green Banner", 1, 3, 1, 0);
		addItemObjPart("Blue Banner", 1, 3, 2, 0);
		addItemObjPart("Yellow Banner", 1, 3, 3, 0);
		addItemObj("Lamp Post", 1, 6);
		addItemObj("Tiki Torch", 1, 3);
		addItemObj("Keg", 2, 2);
		addItemObj("Chinese Lantern", 2, 2);
		addItemObj("Cooking Pot", 2, 2);
		addItemObj("Safe", 2, 2);
		addItemObj("Skull Lantern", 2, 2);
		addItemObj("Candelabra", 2, 2);
		addItemObj("Bookcase", 3, 4);
		addItemObj("Throne", 3, 4);
		addItemObj("Bowl", 2, 1);
		addItemObj("Grandfather Clock", 2, 5);
		addItemObj("Sawmill", 3, 3);
		addItemObj("Chain Lantern", 1, 2);
		MUSHROOM         = addItemObj("Mushroom", 1, 1);
		VILE_MUSHROOM    = addItemObjPart("Vile Mushroom", 1, 1, 1, 0);
		GLOWING_MUSHROOM = addItemObj("Glowing Mushroom", 1, 1);
		addItemObj("Life Crystal", 2, 2);
		DAYBLOOM             = addItemObj("Daybloom", 1, 1);
		MOONGLOW             = addItemObjPart("Moonglow", 1, 1, 1, 0);
		BLINKROOT            = addItemObjPart("Blinkroot", 1, 1, 2, 0);
		DEATHWEED            = addItemObjPart("Deathweed", 1, 1, 3, 0);
		WATERLEAF            = addItemObjPart("Waterleaf", 1, 1, 4, 0);
		FIREBLOSSOM          = addItemObjPart("Fireblossom", 1, 1, 5, 0);
		SHORT_WEEDS          = addItemObj("Short Weeds", 1, 1);
		TALL_WEEDS           = addItemObj("Tall Weeds", 1, 2);
		DEMON_ALTAR          = addItemObj("Demon Altar", 3, 2);
		SHADOW_ORB           = addItemObj("Shadow Orb", 2, 2);
		CORRUPT_WEEDS        = addItemObj("Corrupt Weeds", 1, 1);
		SHORT_JUNGLE_WEEDS   = addItemObj("Short Jungle Weeds", 1, 1);
		NATURES_GIFT         = addItemObjPart("Nature's Gift", 1, 1, 8, 0);
		JUNGLE_SPORES        = addItemObjPart("Jungle Spores", 1, 1, 9, 0);
		TALL_JUNGLE_WEEDS    = addItemObj("Tall Jungle Weeds", 1, 2);
		SHORT_HALLOWED_WEEDS = addItemObj("Short Hallowed Weeds", 1, 1);
		TALL_HALLOWED_WEEDS  = addItemObj("Tall Hallowed Weeds", 1, 2);
		addItemObj("Water Bolt", 1, 1);
		CORAL = addItemObj("Coral", 3, 2);
		addItemObj("Tinkerer's Workshop", 3, 2);
		addItemObj("Crystal Ball", 2, 2);
		addItemObj("Disco Ball", 2, 2);
		MANNEQUIN     = addItemObj("Mannequin", 2, 3, true);
		CRYSTAL_SHARD = addItemObj("Crystal Shard", 1, 1);
		addItemObj("Lever", 2, 2, true);
		ADAMANTITE_FORGE   = addItemObj("Adamantite Forge", 3, 2);
		MYTHRIL_ANVIL      = addItemObj("Mythril Anvil", 2, 1);
		RED_PRESSURE_PLATE = addItemObj("Red Pressure Plate", 1, 1);
		addItemObjPart("Green Pressure Plate", 1, 1, 1, 0);
		addItemObjPart("Gray Pressure Plate", 1, 1, 2, 0);
		BROWN_PRESSURE_PLATE = addItemObjPart("Brown Pressure Plate", 1, 1, 3, 0);
		SWITCH      = addItemObj("Switch", 1, 1);
		DART_TRAP   = addItemObj("Dart Trap", 1, 1, true);
		BOULDER     = addItemObj("Boulder", 2, 2);
		EXPLOSIVES  = addItemObj("Explosives", 1, 1);
		INLET_PUMP  = addItemObj("Inlet Pump", 2, 2);
		OUTLET_PUMP = addItemObjPart("Outlet Pump", 2, 2, 2, 0);
		addItemObj("1 Second Timer", 1, 1);
		addItemObjPart("3 Second Timer", 1, 1, 1, 0);
		addItemObjPart("5 Second Timer", 1, 1, 2, 0);
		BLUE_LIGHT  = addItemObj("Blue Light", 1, 1);
		RED_LIGHT   = addItemObjPart("Red Light", 1, 1, 1, 0);
		GREEN_LIGHT = addItemObjPart("Green Light", 1, 1, 2, 0);
		
		ARMOR_STATUE = addItemObj("Armor Statue", 2, 3);
		addItemObjPart("Angel Statue", 2, 3, 2, 0);
		addItemObjPart("Star Statue", 2, 3, 4, 0);
		addItemObjPart("Sword Statue", 2, 3, 6, 0);
		addItemObjPart("Slime Statue", 2, 3, 8, 0);
		addItemObjPart("Goblin Statue", 2, 3, 10, 0);
		addItemObjPart("Shield Statue", 2, 3, 12, 0);
		addItemObjPart("Bat Statue", 2, 3, 14, 0);
		addItemObjPart("Fish Statue", 2, 3, 16, 0);
		addItemObjPart("Bunny Statue", 2, 3, 18, 0);

		addItemObjPart("Skeleton Statue", 2, 3, 20, 0);
		addItemObjPart("Reaper Statue", 2, 3, 22, 0);
		addItemObjPart("Woman Statue", 2, 3, 24, 0);
		addItemObjPart("Imp Statue", 2, 3, 26, 0);
		addItemObjPart("Gargoyle Statue", 2, 3, 28, 0);
		addItemObjPart("Gloom Statue", 2, 3, 30, 0);
		addItemObjPart("Hornet Statue", 2, 3, 32, 0);
		addItemObjPart("Bomb Statue", 2, 3, 34, 0);
		addItemObjPart("Crab Statue", 2, 3, 36, 0);
		addItemObjPart("Hammer Statue", 2, 3, 38, 0);

		addItemObjPart("Potion Statue", 2, 3, 40, 0);
		addItemObjPart("Spear Statue", 2, 3, 42, 0);
		addItemObjPart("Cross Statue", 2, 3, 44, 0);
		addItemObjPart("Jellyfish Statue", 2, 3, 46, 0);
		addItemObjPart("Bow Statue", 2, 3, 48, 0);
		addItemObjPart("Boomerang Statue", 2, 3, 50, 0);
		addItemObjPart("Boot Statue", 2, 3, 52, 0);
		addItemObjPart("Chest Statue", 2, 3, 54, 0);
		addItemObjPart("Bird Statue", 2, 3, 56, 0);
		addItemObjPart("Axe Statue", 2, 3, 58, 0);

		addItemObjPart("Corrupt Statue", 2, 3, 60, 0);
		addItemObjPart("Tree Statue", 2, 3, 62, 0);
		addItemObjPart("Anvil Statue", 2, 3, 64, 0);
		addItemObjPart("Picaxe Statue", 2, 3, 66, 0);
		addItemObjPart("Mushroom Statue", 2, 3, 68, 0);
		addItemObjPart("Eyeball Statue", 2, 3, 70, 0);
		addItemObjPart("Pillar Statue", 2, 3, 72, 0);
		addItemObjPart("Heart Statue", 2, 3, 74, 0);
		addItemObjPart("Pot Statue", 2, 3, 76, 0);
		addItemObjPart("Sunflower Statue", 2, 3, 78, 0);

		addItemObjPart("King Statue", 2, 3, 80, 0);
		addItemObjPart("Queen Statue", 2, 3, 82, 0);
		PIRANHA_STATUE = addItemObjPart("Piranha Statue", 2, 3, 84, 0);
		
		TREE_TRUNK       = addItemObj("Tree Trunk", 1, 1);
		TREE_TOP_BARE    = addItemObjPart("Tree Top Bare", 1, 1, 16, 0);
		TREE_STUMP       = addItemObjPart("Tree Stump", 1, 1, 17, 0);
		TREE_BRANCH_BARE = addItemObjPart("Tree Branch Bare", 1, 1, 19, 0);
		TREE_TOP         = addItemObj("Tree Top", 1, 1);
		SHROOM_STEM      = addItemObj("Shroom Stem", 1, 1);
		SHROOM_TOP       = addItemObj("Shroom Top", 1, 1);

		currentGridIndex = Item.TYPE_WIRE;
		currentScheme = null;
		WIRE = addItem("Wire");
		
		currentGridIndex = Item.TYPE_LIQUID;
		WATER = addItem("Water");
		LAVA = addItem("Lava");
		

		currentGridIndex = Item.TYPE_OBJECT;
		currentScheme = ConnectionScheme.DIRTBLEND;
		STONE_BLOCK = addItem("Stone Block");
		addItem("Sand Block");
		addItem("Clay Block");
		addItem("Copper Ore");
		addItem("Iron Ore");
		addItem("Silver Ore");
		addItem("Gold Ore");
		addItem("Ebonstone Block");
		addItem("Demonite Ore");
		addItem("Meteorite");
		addItem("Obsidian");
		SAPPHIRE = addItem("Sapphire");
		RUBY     = addItem("Ruby");
		EMERALD  = addItem("Emerald");
		TOPAZ    = addItem("Topaz");
		AMETHYST = addItem("Amethyst");
		DIAMOND  = addItem("Diamond");
		addItem("Wood");
		addItem("Gray Brick");
		addItem("Red Brick");
		addItem("Blue Brick");
		addItem("Green Brick");
		addItem("Pink Brick");
		addItem("Gold Brick");
		addItem("Silver Brick");
		addItem("Copper Brick");
		addItem("Pearlstone Brick");
		addItem("Iridescent Brick");
		addItem("Mudstone Block");
		addItem("Cobalt Brick");
		addItem("Mythril Brick");
		addItem("Demonite Brick");
		addItem("Cobalt Ore");
		addItem("Mythril Ore");
		addItem("Adamantite Ore");
		addItem("Ebonsand Block");
		addItem("Pearlsand Block");
		addItem("Pearlstone Block");
		addItem("Silt Block");
		ACTIVE_STONE_BLOCK   = addItem("Active Stone Block");
		INACTIVE_STONE_BLOCK = addItem("Inactive Stone Block");
		addItem("Candy Cane Block");
		addItem("Green Candy Cane Block");
		addItem("Snow Block");
		addItem("Snow Brick");
		

		currentScheme = ConnectionScheme.ASHBLEND;
		addItem("Hellstone");
		addItem("Hellstone Brick");
		addItem("Obsidian Brick");

		currentScheme = ConnectionScheme.MUDBLEND;
		DIRT_BLOCK    = addItem("Dirt Block");
		
		currentScheme = ConnectionScheme.STONEBLEND;
		MUD_BLOCK     = addItem("Mud Block");
		ASH_BLOCK     = addItem("Ash Block");

		currentScheme  = ConnectionScheme.DIRTGRASS;
		GRASS          = addItem("Grass");
		CORRUPT_GRASS  = addItem("Corrupt Grass");
		HALLOWED_GRASS = addItem("Hallowed Grass");

		currentScheme  = ConnectionScheme.MUDGRASS;
		JUNGLE_GRASS   = addItem("Jungle Grass");
		MUSHROOM_GRASS = addItem("Mushroom Grass");

		currentScheme  = ConnectionScheme.BLOCK;
		SPIKES         = addItem("Spikes");
		VINES          = addItem("Vines");
		COBWEB         = addItem("Cobweb");
		GLASS          = addItem("Glass");
		CORRUPT_THORNS = addItem("Corrupt Thorns");
		JUNGLE_THORNS  = addItem("Jungle Thorns");
		JUNGLE_VINES   = addItem("Jungle Vines");
		HALLOWED_VINES = addItem("Hallowed Vines");
		WOODEN_BEAM    = addItem("Wooden Beam");

		
		currentScheme = ConnectionScheme.BLOCK;
		currentGridIndex = Item.TYPE_WALL;
		addItemWall("Stone Wall", "Stone Block");
		addItemWall("Dirt Wall", "Dirt Block");
		addItemWall("Wood Wall", "Wood");
		addItemWall("Gray Brick Wall", "Gray Brick");
		addItemWall("Red Brick Wall", "Red Brick");
		addItemWall("Blue Brick Wall", "Blue Brick");
		addItemWall("Green Brick Wall", "Green Brick");
		addItemWall("Pink Brick Wall", "Pink Brick");
		addItemWall("Gold Brick Wall", "Gold Brick");
		addItemWall("Silver Brick Wall", "Silver Brick");
		addItemWall("Copper Brick Wall", "Copper Brick");
		addItemWall("Hellstone Brick Wall", "Hellstone Brick");
		addItemWall("Obsidian Brick Wall", "Obsidian Brick");
		addItemWall("Mud Wall", "Mud Block");
		addItemWall("Ebonstone Wall", "Ebonstone Block");
		addItemWall("Glass Wall", "Glass");
		addItemWall("Pearlstone Brick Wall", "Pearlstone Brick");
		addItemWall("Iridescent Brick Wall", "Iridescent Brick");
		addItemWall("Mudstone Brick Wall", "Mudstone Block");
		addItemWall("Cobalt Brick Wall", "Cobalt Brick");
		addItemWall("Mythril Brick Wall", "Mythril Brick");
		addItemWall("Planked Wall", "");
		addItemWall("Pearlstone Wall", "Pearlstone Block");
		addItemWall("Candy Cane Wall", "Candy Cane Block");
		addItemWall("Green Candy Cane Wall", "Green Candy Cane Block");
		addItemWall("Snow Brick Wall", "Snow Brick");
		


		BOOK.setRandomSubimageCount(5);
		SHORT_WEEDS.setRandomSubimageCount(8);
		TALL_WEEDS.setRandomSubimageCount(8);
		SHORT_JUNGLE_WEEDS.setRandomSubimageCount(8);
		TALL_JUNGLE_WEEDS.setRandomSubimageCount(8);
		SHORT_HALLOWED_WEEDS.setRandomSubimageCount(8);
		TALL_HALLOWED_WEEDS.setRandomSubimageCount(8);
		CORRUPT_WEEDS.setRandomSubimageCount(8);
		GLOWING_MUSHROOM.setRandomSubimageCount(5);
		ACORN.setRandomSubimageCount(3);
		POT.setRandomSubimageCount(3);
		CORAL.setRandomSubimageCount(6);
		CRYSTAL_SHARD.setRandomSubimageCount(9);
		EXPLOSIVES.setRandomSubimageCount(2);
		TREE_TOP.setRandomSubimageCount(3);
		SHROOM_TOP.setRandomSubimageCount(3);
		

		for (int i = CHEST.getIndex(); i <= LOCKED_SHADOW_CHEST.getIndex(); i++)
			get(i).setToExtendsDown();
		for (int i = RED_PRESSURE_PLATE.getIndex(); i <= BROWN_PRESSURE_PLATE.getIndex(); i++)
			get(i).setToExtendsDown();
		for (int i = ARMOR_STATUE.getIndex(); i <= PIRANHA_STATUE.getIndex(); i++)
			
			get(i).setToExtendsDown();
		for (int i = TORCH.getIndex(); i <= CURSED_TORCH.getIndex(); i++)
			get(i).setExtension(-1, 0, 3, 2);
		for (int i = DAYBLOOM.getIndex(); i <= FIREBLOSSOM.getIndex(); i++)
			get(i).setExtension(0, -1, 1, 3);
		
		MANNEQUIN.setExtension(-1, -1, 4, 4);
		TREE_TOP.setExtension(-2, -4, 5, 5);
		SHROOM_TOP.setExtension(-2, -2, 5, 3);
		
		CANDLE.setToExtendsUp();
		WATER_CANDLE.setToExtendsUp();
		
		WOODEN_TABLE.setToExtendsDown();
		WOODEN_CHAIR.setToExtendsDown();
		TOILET.setToExtendsDown();
		IRON_ANVIL.setToExtendsDown();
		WORK_BENCH.setToExtendsDown();
		FURNACE.setToExtendsDown();
		HELLFORGE.setToExtendsDown();
		ACORN.setToExtendsDown();
		SUNFLOWER.setToExtendsDown();
		BED.setToExtendsDown();
		MUSHROOM.setToExtendsDown();
		VILE_MUSHROOM.setToExtendsDown();
		CLAY_POT.setToExtendsDown();
		SHORT_WEEDS.setToExtendsDown();
		TALL_WEEDS.setToExtendsDown();
		CORRUPT_WEEDS.setToExtendsDown();
		SHORT_JUNGLE_WEEDS.setToExtendsDown();
		TALL_JUNGLE_WEEDS.setToExtendsDown();
		SHORT_HALLOWED_WEEDS.setToExtendsDown();
		TALL_HALLOWED_WEEDS.setToExtendsDown();
		NATURES_GIFT.setToExtendsDown();
		JUNGLE_SPORES.setToExtendsDown();
		GLOWING_MUSHROOM.setToExtendsDown();
		CORAL.setToExtendsDown();
		ADAMANTITE_FORGE.setToExtendsDown();
		MYTHRIL_ANVIL.setToExtendsDown();
		DEMON_ALTAR.setToExtendsDown();
		DART_TRAP.setToExtendsDown();
		INLET_PUMP.setToExtendsDown();
		OUTLET_PUMP.setToExtendsDown();
		
		TREE_TRUNK.setToExtendsDown();
		TREE_STUMP.setToExtendsDown();
		SHROOM_STEM.setToExtendsDown();
		
		
		CRYSTAL_SHARD.setToVerticalSubimageStrip();
		BLUE_LIGHT.setToVerticalSubimageStrip();
		RED_LIGHT.setToVerticalSubimageStrip();
		GREEN_LIGHT.setToVerticalSubimageStrip();
		
		DART_TRAP.setSolid(true);
		BOULDER.setSolid(true);
		GLASS.setSolid(true);
		DOOR_CLOSED.setSolid(true);
		INACTIVE_STONE_BLOCK.setSolid(false);
		
		ACTIVE_STONE_BLOCK.setParentItem(STONE_BLOCK);
		INACTIVE_STONE_BLOCK.setParentItem(STONE_BLOCK);
		for (int i = SAPPHIRE.getIndex(); i <= DIAMOND.getIndex(); i++)
			get(i).setParentItem(STONE_BLOCK);
	}
	
	/** Return the item with the given index. **/
	public static Item get(int index) {
		return items.get(index);
	}
	
	public static Item find(String name) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getName().equals(name))
				return items.get(i);
		}
		return null;
	}
	
	public static int getTotalItems() {
		return items.size();
	}

	
	
	/** Add an item to the list. **/
	private static Item addItem(String name) {
		Item item = new Item(items.size(), name, 1, 1, false, currentScheme, currentGridIndex);
		itemNameLast = name;
		items.add(item);
		return item;
	}
	
	/** Add a wall item to the list. **/
	private static Item addItemWall(String name, String wallObjItemName) {
		Item item = new Item(items.size(), name, 1, 1, false, currentScheme, currentGridIndex);
		if (wallObjItemName.length() > 0) {
			if (find(wallObjItemName) == null)
				System.out.println(wallObjItemName + "???");
			item.setWallObjectItem(find(wallObjItemName));
		}
		itemNameLast = name;
		items.add(item);
		return item;
	}

	/** Add an object item to the list. **/
	private static Item addItemObj(String name, int width, int height) {
		Item item = new Item(items.size(), name, width, height, false, currentScheme, currentGridIndex);
		itemNameLast = name;
		items.add(item);
		return item;
	}
	
	/** Add an object item to the list. **/
	private static Item addItemObj(String name, int width, int height, boolean flippable) {
		Item item = new Item(items.size(), name, width, height, flippable, currentScheme, currentGridIndex);
		itemNameLast = name;
		items.add(item);
		return item;
	}

	/** Add an object item to the list from another item's image. **/
	private static Item addItemObjPart(String name, int width, int height, int offsetX, int offsetY) {
		Item item = new Item(items.size(), name, itemNameLast, width, height, offsetX, offsetY, false, currentScheme, currentGridIndex);
		items.add(item);
		return item;
	}

	/** Add an object item to the list from another item's image. **/
	private static Item addItemObjPart(String name, int width, int height, int offsetX, int offsetY, boolean flippable) {
		Item item = new Item(items.size(), name, itemNameLast, width, height, offsetX, offsetY, flippable, currentScheme, currentGridIndex);
		items.add(item);
		return item;
	}
}
