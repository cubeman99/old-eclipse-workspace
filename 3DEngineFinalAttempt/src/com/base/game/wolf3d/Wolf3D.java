package com.base.game.wolf3d;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.newdawn.slick.openal.WaveData;
import com.base.engine.audio.AudioEngine;
import com.base.engine.audio.Sound;
import com.base.engine.audio.SoundEmitter;
import com.base.engine.core.Util;
import com.base.engine.rendering.Bitmap;
import com.base.engine.rendering.Font;
import com.base.engine.rendering.Texture;
import com.base.engine.rendering.resourceManagement.ResourceManager;
import com.base.game.wolf3d.tile.*;
import com.base.game.wolf3d.tile.enemy.*;
import com.base.game.wolf3d.tile.mesh.*;
import com.base.game.wolf3d.tile.pickups.*;

public class Wolf3D {
	public static Font FONT;
	public static Font FONT_HUD;
	
	public static Texture TEXTURE_HUD;
	
	public static Texture[][] OBJECT_TEXTURES;
	public static Texture[][] WALL_TEXTURES;
	public static Texture[][] SHEET_BJ;
	public static Texture[][] SHEET_WEAPONS;
	public static Texture[][] SHEET_HUD_WEAPONS;
	public static Texture[][] SHEET_HUD_KEYS;
	public static Texture[][] SHEET_HUD_FACE;
	
	public static final int WEAPON_KNIFE       = 0;
	public static final int WEAPON_PISTOL      = 1;
	public static final int WEAPON_MACHINE_GUN = 2;
	public static final int WEAPON_CHAIN_GUN   = 3;
	public static final int NUM_WEAPONS        = 4;

	public static final int GOLD_KEY   = 0;
	public static final int SILVER_KEY = 1;
	public static final int NUM_KEYS   = 2;
	
	public static Tile[] OBJECT_TILES;
	public static AmmoPickup OBJ_CLIP;
	public static AmmoPickup OBJ_USED_CLIP;
	public static KeyPickup OBJ_GOLD_KEY;
	public static KeyPickup OBJ_SILVER_KEY;
	public static WeaponPickup OBJ_WEAPON_MACHINE_GUN;
	public static WeaponPickup OBJ_WEAPON_CHAIN_GUN;
	
	public static final int WINDOW_WIDTH    = 320;
	public static final int WINDOW_HEIGHT   = 200;
	public static final int VIEWPORT_WIDTH  = 304;
	public static final int VIEWPORT_HEIGHT = 152;
	public static final float WINDOW_ASPECT_RATIO   = WINDOW_WIDTH / (float) WINDOW_HEIGHT;
	public static final float VIEWPORT_ASPECT_RATIO = VIEWPORT_WIDTH / (float) VIEWPORT_HEIGHT;
	public static final int RESOLUTION_SCALE = 3;

	public static final float ENEMY_ALERT_DISTANCE = 6;
	
	public static final boolean GOD_MODE = true;
	
	
	public static Sound SOUND_KNIFE;
	public static Sound SOUND_PISTOL;
	public static Sound SOUND_MACHINE_GUN;
	public static Sound SOUND_CHAIN_GUN;
	public static Sound SOUND_DOOR_OPEN;
	public static Sound SOUND_DOOR_CLOSE;
	public static Sound SOUND_SECRET_PASSAGE;
	public static Sound SOUND_SWITCH;
	
	public static Sound SOUND_GUARD_ALERT;
	public static Sound SOUND_GUARD_FIRE;
	public static Sound SOUND_OFFICER_ALERT;
	public static Sound SOUND_OFFICER_FIRE;
	public static Sound SOUND_OFFICER_DIE;
	public static Sound SOUND_SS_ALERT;
	public static Sound SOUND_SS_FIRE;
	public static Sound SOUND_SS_DIE;
	public static Sound SOUND_MUTANT_DIE;
	public static Sound SOUND_DOG_BARK;
	public static Sound SOUND_DOG_ATTACK;
	public static Sound SOUND_DOG_DIE;
	
	
	public static void initialize() {
		Texture.DEFAULT_FILTERING = GL_NEAREST;
		ResourceManager.RESOURCE_DIRECTORY = "res/wolf3d/";

		FONT = new Font("font_console.png", 8, 12, 16);
		FONT_HUD = new Font("font_hud.png", 8, 16, 16);
		
		TEXTURE_HUD = new Texture("hud2.png");
		
		OBJECT_TEXTURES   = Util.createTextureSheet(new Bitmap("objects.png"), 5, 10, 64, 64, 1);
		WALL_TEXTURES     = Util.createTextureSheet(new Bitmap("walls.png"), 6, 19, 64, 64, 0);
		SHEET_BJ          = Util.createTextureSheet(new Bitmap("bj.png"), 4, 2, 64, 64, 0);
		SHEET_WEAPONS     = Util.createTextureSheet(new Bitmap("weapons.png"), 5, 4, 64, 64, 1);
		SHEET_HUD_WEAPONS = Util.createTextureSheet(new Bitmap("hud_weapons.png"), 4, 1, 48, 24, 1);
		SHEET_HUD_KEYS    = Util.createTextureSheet(new Bitmap("hud_keys.png"), 2, 1, 8, 16, 1);
		SHEET_HUD_FACE    = Util.createTextureSheet(new Bitmap("hud_face.png"), 3, 8, 24, 32, 1);
		
		OBJECT_TILES = new Tile[] {
			// 3D TILES:
			new PlayerStart(),
			new Door(),
			new SecretPassage(),
			new Elevator(),
			
			// SOLID OBJECTS:
			new ObjectTile("Green Barrel",     OBJECT_TEXTURES[3][0], true),
			new ObjectTile("Table/chairs",     OBJECT_TEXTURES[4][0], true),
			new ObjectTile("Floor lamp",       OBJECT_TEXTURES[0][1], true),
			new ObjectTile("Hanged man",       OBJECT_TEXTURES[2][1], true),
			new ObjectTile("Pillar",           OBJECT_TEXTURES[4][1], true),
			new ObjectTile("Tree",             OBJECT_TEXTURES[0][2], true),
			new ObjectTile("Sink",             OBJECT_TEXTURES[2][2], true),
			new ObjectTile("Potted plant",     OBJECT_TEXTURES[3][2], true),
			new ObjectTile("Urn",              OBJECT_TEXTURES[4][2], true),
			new ObjectTile("Bare table",       OBJECT_TEXTURES[0][3], true),
			new ObjectTile("Suit of armor",    OBJECT_TEXTURES[3][3], true),
			new ObjectTile("Hanging cage",     OBJECT_TEXTURES[4][3], true),
			new ObjectTile("Skeleton in cage", OBJECT_TEXTURES[0][4], true),
			new ObjectTile("Bed",              OBJECT_TEXTURES[4][4], true),
			new ObjectTile("Wood barrel",      OBJECT_TEXTURES[2][7], true),
			new ObjectTile("Well",             OBJECT_TEXTURES[3][7], true),
			new ObjectTile("Empty well",       OBJECT_TEXTURES[4][7], true),
			new ObjectTile("Flag",             OBJECT_TEXTURES[1][8], true),
			new ObjectTile("Stove",            OBJECT_TEXTURES[2][9], true),
			new ObjectTile("Spears",           OBJECT_TEXTURES[3][9], true),

			// NON SOLID OBJECTS:
			new ObjectTile("Chandelier",      OBJECT_TEXTURES[1][1], false),
			new ObjectTile("Skeleton",        OBJECT_TEXTURES[1][2], false),
			new ObjectTile("Light",           OBJECT_TEXTURES[1][3], false),
			new ObjectTile("Small pots",      OBJECT_TEXTURES[2][3], false),
			new ObjectTile("Skulls",          OBJECT_TEXTURES[1][4], false),
			new ObjectTile("Wood bucket",     OBJECT_TEXTURES[0][5], false),
			new ObjectTile("Bones and blood", OBJECT_TEXTURES[1][7], false),
			new ObjectTile("Litter1",         OBJECT_TEXTURES[3][8], false),
			new ObjectTile("Litter2",         OBJECT_TEXTURES[4][8], false),
			new ObjectTile("Litter3",         OBJECT_TEXTURES[0][9], false),
			new ObjectTile("Large pots",      OBJECT_TEXTURES[1][9], false),
		
			// PICKUPS:
			new HealthPickup("Dog food",    OBJECT_TEXTURES[3][1], 4),
			new HealthPickup("Dinner",      OBJECT_TEXTURES[1][5], 10),
			new HealthPickup("First aid",   OBJECT_TEXTURES[2][5], 25),
			new  BloodPickup("Blood",       OBJECT_TEXTURES[0][8], 1, 11),
			OBJ_CLIP               = new AmmoPickup("Clip",          OBJECT_TEXTURES[3][5], 8),
			OBJ_USED_CLIP          = new AmmoPickup("Used clip",     OBJECT_TEXTURES[3][5], 4),
			OBJ_GOLD_KEY           = new KeyPickup("Gold Key",       OBJECT_TEXTURES[2][4], GOLD_KEY),
			OBJ_SILVER_KEY         = new KeyPickup("Silver Key",     OBJECT_TEXTURES[3][4], SILVER_KEY),
			OBJ_WEAPON_MACHINE_GUN = new WeaponPickup("Machine gun", OBJECT_TEXTURES[4][5], WEAPON_MACHINE_GUN, 6),
			OBJ_WEAPON_CHAIN_GUN   = new WeaponPickup("Chain gun",   OBJECT_TEXTURES[0][6], WEAPON_CHAIN_GUN, 6),
			new   LootPickup("Cross",       OBJECT_TEXTURES[1][6], 100),
			new   LootPickup("Goblet",      OBJECT_TEXTURES[2][6], 500),
			new   LootPickup("Chest",       OBJECT_TEXTURES[3][6], 1000),
			new   LootPickup("Crown",       OBJECT_TEXTURES[4][6], 5000),
			new  OneUpPickup("One Up",      OBJECT_TEXTURES[0][7]),
			
			// ENEMIES:
			new PatrolNode(),
			new Guard(),
			new Officer(),
			new SS(),
			new Mutant(),
			new Dog(),
		};
		
		for (Tile obj : OBJECT_TILES) {
			if (obj instanceof Enemy)
				((Enemy) obj).setTexture(((Enemy) obj).getSprite().getCurrentFrame());
		}
		

		LevelFileData.NUM_OBJECTS = OBJECT_TILES.length;

//		new Sound("./res/wolf3d/sounds/pistol.wav", true);
		
		
		SOUND_GUARD_ALERT      = new Sound("./res/wolf3d/sounds/achtung.wav");
		SOUND_KNIFE        = new Sound("./res/wolf3d/sounds/knife.wav");
		SOUND_PISTOL       = new Sound("./res/wolf3d/sounds/pistol.wav");
		SOUND_MACHINE_GUN  = new Sound("./res/wolf3d/sounds/machine_gun.wav");
		SOUND_CHAIN_GUN    = new Sound("./res/wolf3d/sounds/chain_gun.wav");
		SOUND_DOOR_OPEN    = new Sound("./res/wolf3d/sounds/door_open.wav");
		SOUND_SWITCH      = new Sound("./res/wolf3d/sounds/switch.wav");
		SOUND_DOG_DIE      = new Sound("./res/wolf3d/sounds/dog_die.wav");
	}
}
