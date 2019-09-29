package zelda.common;

import java.awt.Image;
import zelda.common.graphics.Animation;
import zelda.common.graphics.AnimationFrame;
import zelda.common.graphics.Animations;
import zelda.common.graphics.ImageSheet;
import zelda.common.graphics.Sprite;
import zelda.common.graphics.TileAnimations;
import zelda.editor.Editor;
import zelda.game.control.script.Script;
import zelda.game.world.World;
import zelda.game.zone.Zones;


public class Resources {
	public static ImageSheet TILESET_OVERWORLD_SUMMER;
	public static ImageSheet ZONESET_SUMMER;
	public static Zones zones;
	
	public static Font FONT_TEXT;
	public static Font FONT_SMALL;

	public static ImageSheet SHEET_BACKGROUNDS;
	public static ImageSheet SHEET_PLAYER;
	public static ImageSheet SHEET_PLAYER_ITEMS;
	public static ImageSheet SHEET_MONSTERS;
	public static ImageSheet SHEET_MONSTERS_HURT;
	public static ImageSheet SHEET_MONSTER_ITEMS;
	public static ImageSheet SHEET_GENERAL_TILES;
	public static ImageSheet SHEET_MENU_LARGE;
	public static ImageSheet SHEET_MENU_SMALL;
	public static ImageSheet SHEET_ICONS_LARGE;
	public static ImageSheet SHEET_ICONS_THIN;
	public static ImageSheet SHEET_ICONS_SMALL;
	public static ImageSheet SHEET_EFFECTS;
	public static ImageSheet SHEET_SPECIAL_EFFECTS;
	public static ImageSheet SHEET_EDITOR_BUTTONS;
	public static ImageSheet SHEET_COLOR_CUBE;
	public static ImageSheet SHEET_LOGIC_TILES;
	
	public static Image IMAGE_DUNGEON_MAP;
	public static Image IMAGE_WORLD_MAP;

	public static Sprite SPRITE_SHADOW;
	public static Sprite SPRITE_FIRE;
	public static Sprite SPRITE_FALLING_OBJECT;
	public static Sprite SPRITE_CLING;
	public static Sprite SPRITE_CLING_LIGHT;
	public static Sprite SPRITE_SCENT_POD;
	public static Sprite SPRITE_BOMB;

	public static Sprite SPRITE_EFFECT_GRASS;
	public static Sprite SPRITE_EFFECT_WADE;
	public static Sprite SPRITE_EFFECT_BOMB_EXPLOSION;
	public static Sprite SPRITE_EFFECT_MONSTER_EXPLOSION;
	public static Sprite SPRITE_EFFECT_SPLASH_WATER;
	public static Sprite SPRITE_EFFECT_SPLASH_LAVA;
	public static Sprite SPRITE_EFFECT_GALE;
	public static Sprite SPRITE_EFFECT_SPRINT;

	public static Sprite SPRITE_COLLECTABLE_RUPEE_1;
	public static Sprite SPRITE_COLLECTABLE_RUPEE_5;
	public static Sprite SPRITE_COLLECTABLE_RUPEE_10;
	public static Sprite SPRITE_COLLECTABLE_RUPEE_100;
	public static Sprite SPRITE_COLLECTABLE_HEART;
	public static Sprite SPRITE_COLLECTABLE_ARROWS;
	public static Sprite SPRITE_COLLECTABLE_BOMBS;
	public static Sprite SPRITE_COLLECTABLE_BOMBCHUS;
	public static Sprite[] SPRITE_COLLECTABLE_SEEDS;
	
	public static Sprite SPRITE_MAP_PLAYER;
	public static Sprite SPRITE_MAP_BOSS_ROOM;
	public static Sprite SPRITE_MAP_BOSS_FLOOR;
	public static Sprite SPRITE_MAP_TREASURE;
	public static Sprite SPRITE_MAP_CURSOR;
	public static Sprite SPRITE_MAP_ARROW_UP;
	public static Sprite SPRITE_MAP_ARROW_DOWN;
	public static Sprite SPRITE_MAP_FLOOR_ARROW;
	public static Sprite SPRITE_MAP_FLOOR_BOX;
	

	
	public static void initialize() {
		Sounds.initialize();
		Animations.initialize();
		TileAnimations.initialize();
		Script.initialize();

		zones = new Zones();

		FONT_TEXT = new Font("fontText.png", 8, 12,
				"<n><red><blue><A1><A2><B1><B2><pad><spd><hrt><dmd><clb><crc><tri><trid><trir><tril><sqr><au><ad><al><ar><msc> !<qt>#$%&<ap>()*+,-./0123456789:;<lt>=<gt>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~");
		FONT_SMALL = new Font("fontSmall.png", 8, 8, "?/x<lvl><north><south>0123456789BF");

		SHEET_BACKGROUNDS     = new ImageSheet("backgrounds.png",       160, 144, 2);
		SHEET_PLAYER          = new ImageSheet("sheetPlayer.png",         16, 16, 1);
		SHEET_PLAYER_ITEMS    = new ImageSheet("sheetPlayerItems.png",    16, 16, 1);
		SHEET_MONSTERS        = new ImageSheet("sheetMonsters.png",       16, 16, 1);
		SHEET_MONSTERS_HURT   = new ImageSheet("sheetMonstersHurt.png",   16, 16, 1);
		SHEET_MONSTER_ITEMS   = new ImageSheet("sheetMonsterItems.png",   16, 16, 1);
		SHEET_GENERAL_TILES   = new ImageSheet("sheetGeneralTiles.png",   16, 16, 1);
		SHEET_MENU_LARGE      = new ImageSheet("sheetMenuLarge.png",      16, 16, 1);
		SHEET_MENU_SMALL      = new ImageSheet("sheetMenuSmall.png",       8,  8, 1);
		SHEET_ICONS_LARGE     = new ImageSheet("sheetIconsLarge.png",     16, 16, 1);
		SHEET_ICONS_THIN      = new ImageSheet("sheetIconsThin.png",       8, 16, 1);
		SHEET_ICONS_SMALL     = new ImageSheet("sheetIconsSmall.png",      8,  8, 1);
		SHEET_EFFECTS         = new ImageSheet("sheetEffects.png",        16, 16, 1);
		SHEET_SPECIAL_EFFECTS = new ImageSheet("sheetSpecialEffects.png", 16, 16, 1);
		SHEET_EDITOR_BUTTONS  = new ImageSheet("sheetEditorButtons.png",  24, 24, 1);
		SHEET_COLOR_CUBE      = new ImageSheet("sheetColorCube.png",      16, 16, 1);
		SHEET_LOGIC_TILES     = new ImageSheet("sheetLogicTiles.png",     16, 16, 1);

		SPRITE_SHADOW = new Sprite(SHEET_EFFECTS, new Animation().addFrame(1,
				0, 0, 0, 0).addFrame(1, -1, -1, 0, 0));
		SPRITE_SHADOW.setOrigin(8, 8);

		SPRITE_FIRE = new Sprite(SHEET_SPECIAL_EFFECTS, Animations.EFFECT_FIRE,
				false);
		SPRITE_FIRE.setOrigin(8, 8);

		SPRITE_SCENT_POD = new Sprite(SHEET_EFFECTS,
				Animations.EFFECT_SCENT_POD);
		SPRITE_SCENT_POD.setOrigin(8, 14);

		// TODO
		SPRITE_FALLING_OBJECT = new Sprite(SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_FALLING_OBJECT, false);
		SPRITE_FALLING_OBJECT.setOrigin(8, 8);

		SPRITE_CLING = new Sprite(SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_CLING, false);
		SPRITE_CLING.setOrigin(8, 8);

		SPRITE_CLING_LIGHT = new Sprite(SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_CLING_LIGHT, false);
		SPRITE_CLING_LIGHT.setOrigin(8, 8);

		SPRITE_BOMB = new Sprite(SHEET_PLAYER_ITEMS, Animations.ITEM_BOMB);
		SPRITE_BOMB.setOrigin(8, 13);

		SPRITE_EFFECT_BOMB_EXPLOSION = new Sprite(SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_BOMB_EXPLOSION, false);
		SPRITE_EFFECT_BOMB_EXPLOSION.setOrigin(8, 8);
		SPRITE_EFFECT_MONSTER_EXPLOSION = new Sprite(SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_MONSTER_EXPLOSION, false);
		SPRITE_EFFECT_GRASS = new Sprite(SHEET_EFFECTS, Animations.EFFECT_GRASS)
				.setOrigin(8, 7);
		SPRITE_EFFECT_WADE = new Sprite(SHEET_EFFECTS, Animations.EFFECT_WADE);
		SPRITE_EFFECT_SPLASH_WATER = new Sprite(SHEET_EFFECTS,
				Animations.EFFECT_SPLASH_WATER, false);
		SPRITE_EFFECT_SPLASH_LAVA = new Sprite(SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_SPLASH_LAVA, false).setOrigin(8, 9);
		SPRITE_EFFECT_GALE = new Sprite(SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_GALE);
		SPRITE_EFFECT_SPRINT = new Sprite(SHEET_SPECIAL_EFFECTS,
				Animations.EFFECT_SPRINT, false).setOrigin(8, 12);

		SPRITE_COLLECTABLE_RUPEE_1 = new Sprite(SHEET_ICONS_THIN, 0, 3)
				.setOrigin(4, 12);
		SPRITE_COLLECTABLE_RUPEE_5 = new Sprite(SHEET_ICONS_THIN, 1, 3)
				.setOrigin(4, 12);
		SPRITE_COLLECTABLE_RUPEE_10 = new Sprite(SHEET_ICONS_THIN, 2, 3)
				.setOrigin(4, 12);
		SPRITE_COLLECTABLE_RUPEE_100 = new Sprite(SHEET_ICONS_LARGE, 0, 4)
				.setOrigin(8, 13);
		SPRITE_COLLECTABLE_HEART = new Sprite(SHEET_ICONS_SMALL, 5, 0)
				.setOrigin(4, 6);
		SPRITE_COLLECTABLE_ARROWS = new Sprite(SHEET_PLAYER_ITEMS, 0, 11)
				.setOrigin(8, 10);
		SPRITE_COLLECTABLE_BOMBS = new Sprite(SHEET_PLAYER_ITEMS, 2, 8)
				.setOrigin(8, 13);
		SPRITE_COLLECTABLE_SEEDS = new Sprite[5];
		for (int i = 0; i < 5; i++)
			SPRITE_COLLECTABLE_SEEDS[i] = new Sprite(SHEET_ICONS_SMALL, i, 0)
					.setOrigin(4, 6);

		TILESET_OVERWORLD_SUMMER = new ImageSheet("tilesetOverworldSummer.png",
				16, 16, 1);
		ZONESET_SUMMER = new ImageSheet("zonesetSummer.png", 16, 16, 1);
		
		SPRITE_MAP_PLAYER      = new Sprite(SHEET_MENU_SMALL, 5, 0);
		SPRITE_MAP_BOSS_ROOM   = new Sprite(SHEET_MENU_SMALL, 6, 1);
		SPRITE_MAP_BOSS_FLOOR  = new Sprite(SHEET_MENU_SMALL, 6, 0);
		SPRITE_MAP_TREASURE    = new Sprite(SHEET_MENU_SMALL, 5, 1);
		SPRITE_MAP_ARROW_UP    = new Sprite(SHEET_MENU_SMALL, 4, 4);
		SPRITE_MAP_ARROW_DOWN  = new Sprite(SHEET_MENU_SMALL, 4, 5);
		SPRITE_MAP_FLOOR_ARROW = new Sprite(SHEET_MENU_SMALL, 6, 4);
		SPRITE_MAP_FLOOR_BOX   = new Sprite(SHEET_MENU_SMALL, Animations.MAP_FLOOR_BOX);
		SPRITE_MAP_CURSOR      = new Sprite(SHEET_MENU_SMALL, Animations.MAP_CURSOR);
	}

	public static void update() {
		SPRITE_SHADOW.update();
		SPRITE_EFFECT_WADE.update();
	}



	public static void saveWorld(String fileName, World world) {
		// try {
		// FileOutputStream fileOs = new FileOutputStream(fileName);
		// ObjectOutputStream out = new ObjectOutputStream(fileOs);
		//
		// Frame frame = world.getCurrentFrame();
		// frame.save(out);
		//
		// out.close();
		// }
		// catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public static World loadWorld(String fileName, Editor editor) {
		// World world = null;
		//
		// try {
		// FileInputStream fileIs = new FileInputStream(fileName);
		// ObjectInputStream in = new ObjectInputStream(fileIs);
		//
		// Frame frame = world.getCurrentFrame();
		// frame.load(in, new TileSheetTemplate[] {editor.getTileMapSheet(),
		// editor.getTileObjectSheet()});
		//
		// in.close();
		// }
		// catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		// catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// return world;
		return null; // TODO
	}
}
