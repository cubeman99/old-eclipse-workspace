package zelda.common;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import zelda.common.util.GMath;
import zelda.main.Sound;

public class Sounds {
	private static ArrayList<Sound> sounds;
	
	public static Sound COLLECT_HEART;
	public static Sound COLLECT_HEART_CONTAINER;
	public static Sound COLLECT_ITEM;
	public static Sound COLLECT_RUPEE;
	public static Sound COLLECT_RUPEE_5;

	public static Sound EFFECT_BOMB_EXPLODE;
	public static Sound EFFECT_CLING;
	public static Sound EFFECT_EMBER_SEED;
	public static Sound EFFECT_FALL;
	public static Sound EFFECT_FLOOR_CRUMBLE;
	public static Sound EFFECT_GALE_SEED;
	public static Sound EFFECT_LEAVES;
	public static Sound EFFECT_MYSTERY;
	public static Sound EFFECT_POOF;
	public static Sound EFFECT_SCENT_SEED;
	
	public static Sound ITEM_BIGGORON_SWORD;
	public static Sound ITEM_BOOMERANG;
	public static Sound ITEM_FIRE_ROD;
	public static Sound ITEM_SCENT_POD;
	public static Sound ITEM_SEED_SHOOTER;
	public static Sound ITEM_SHIELD;
	public static Sound ITEM_SHIELD_DEFLECT;
	public static Sound ITEM_SHOVEL;
	public static Sound ITEM_SWITCH_HOOK;
	public static Sound ITEM_SWITCH_HOOK_SWITCH;
	public static Sound ITEM_SWORD_BEAM;
	public static Sound ITEM_SWORD_CHARGE;
	public static Sound ITEM_SWORD_SPIN;
	public static Sound ITEM_SWING;
	public static Sound[] ITEM_SWORD_SLASH;

	public static Sound MONSTER_BOSS_DIE;
	public static Sound MONSTER_BOSS_EXPLODE;
	public static Sound MONSTER_BOSS_HURT;
	public static Sound MONSTER_DIE;
	public static Sound MONSTER_HURT;
	public static Sound MONSTER_JUMP;
	public static Sound MONSTER_SHOOT;
	
	public static Sound NPC_CUCCO;
	public static Sound NPC_DEKU_SCRUB;
	public static Sound NPC_DIMITRI;
	public static Sound NPC_GORON;
	public static Sound NPC_MOOSH;
	public static Sound NPC_RICKY;
	public static Sound NPC_TOKAY;

	public static Sound OBJECT_BOMB_BOUNCE;
	public static Sound OBJECT_BREAK;
	public static Sound OBJECT_CHEST_OPEN;
	public static Sound OBJECT_DOOR;
	public static Sound OBJECT_KEY_BOUNCE;
	public static Sound OBJECT_MINE_CART;
	public static Sound OBJECT_MOVE;
	public static Sound OBJECT_ROLLER;
	public static Sound OBJECT_STOP_LIGHT;
	public static Sound OBJECT_SWITCH;
	public static Sound OBJECT_TURNSTILE;
	
	public static Sound PLAYER_DIE;
	public static Sound PLAYER_FALL;
	public static Sound PLAYER_HURT;
	public static Sound PLAYER_JUMP;
	public static Sound PLAYER_LAND;
	public static Sound PLAYER_LOW_HEALTH;
	public static Sound PLAYER_PICKUP;
	public static Sound PLAYER_SWIM;
	public static Sound PLAYER_THROW;
	public static Sound PLAYER_WADE;

	public static Sound SCREEN_CLOSE;
	public static Sound SCREEN_CURSOR;
	public static Sound SCREEN_OPEN;
	public static Sound SCREEN_SELECT;

	public static Sound TEXT_CONTINUE;
	public static Sound TEXT_READ;
	
	public static Sound TUNE_ESSENCE;
	public static Sound TUNE_REWARD;
	public static Sound TUNE_SECRET;
	public static Sound TUNE_SECRET_LONG;
	public static Sound TUNE_TREASURE;
	
	public static Sound WARP_EXIT;
	
	
	
	public static void initialize() {
		sounds = new ArrayList<Sound>();
		
		
		
		COLLECT_HEART				= loadSound("collect_heart");
		COLLECT_HEART_CONTAINER		= loadSound("collect_heart_container");
		COLLECT_ITEM				= loadSound("collect_reward");
		COLLECT_RUPEE				= loadSound("collect_rupee");
		COLLECT_RUPEE_5				= loadSound("collect_rupee_5");

		EFFECT_BOMB_EXPLODE			= loadSound("effect_bomb_explode");
		EFFECT_CLING				= loadSound("effect_cling");
		EFFECT_EMBER_SEED			= loadSound("effect_ember_seed");
		EFFECT_FALL					= loadSound("effect_fall");
		EFFECT_FLOOR_CRUMBLE		= loadSound("effect_floor_crumble");
		EFFECT_GALE_SEED			= loadSound("effect_gale_seed");
		EFFECT_LEAVES				= loadSound("effect_leaves");
		EFFECT_MYSTERY				= loadSound("effect_mystery");
		EFFECT_POOF					= loadSound("effect_poof");
		EFFECT_SCENT_SEED			= loadSound("effect_scent_seed");
		
		ITEM_BIGGORON_SWORD			= loadSound("item_biggoron_sword");
		ITEM_BOOMERANG				= loadSound("item_boomerang");
		ITEM_FIRE_ROD				= loadSound("item_fire_rod");
		ITEM_SCENT_POD				= loadSound("item_scent_pod");
		ITEM_SEED_SHOOTER			= loadSound("item_seed_shooter");
		ITEM_SHIELD					= loadSound("item_shield");
		ITEM_SHIELD_DEFLECT			= loadSound("item_shield_deflect");
		ITEM_SHOVEL					= loadSound("item_shovel");
		ITEM_SWITCH_HOOK			= loadSound("item_switch_hook");
		ITEM_SWITCH_HOOK_SWITCH		= loadSound("item_switch_hook_switch");
		ITEM_SWORD_BEAM				= loadSound("item_sword_beam");
		ITEM_SWORD_CHARGE			= loadSound("item_sword_charge");
		ITEM_SWORD_SPIN				= loadSound("item_sword_spin");
		ITEM_SWING					= loadSound("item_sword_slash_1");
		
		ITEM_SWORD_SLASH = new Sound[] {
			ITEM_SWING, loadSound("item_sword_slash_2"),
			loadSound("item_sword_slash_3")};

		MONSTER_BOSS_DIE			= loadSound("monster_boss_die");
		MONSTER_BOSS_EXPLODE		= loadSound("monster_boss_explode");
		MONSTER_BOSS_HURT			= loadSound("monster_boss_hurt");
		MONSTER_DIE					= loadSound("monster_die");
		MONSTER_HURT				= loadSound("monster_hurt");
		MONSTER_JUMP				= loadSound("monster_jump");
		MONSTER_SHOOT				= loadSound("monster_shoot");
		
		NPC_CUCCO					= loadSound("npc_cucco");
		NPC_DEKU_SCRUB				= loadSound("npc_deku_scrub");
		NPC_DIMITRI					= loadSound("npc_dimitri");
		NPC_GORON					= loadSound("npc_goron");
		NPC_MOOSH					= loadSound("npc_moosh");
		NPC_RICKY					= loadSound("npc_ricky");
		NPC_TOKAY					= loadSound("npc_tokay");

		OBJECT_BOMB_BOUNCE			= loadSound("object_bomb_bounce");
		OBJECT_BREAK				= loadSound("object_break");
		OBJECT_CHEST_OPEN			= loadSound("object_chest_open");
		OBJECT_DOOR					= loadSound("object_door");
		OBJECT_KEY_BOUNCE			= loadSound("object_key_bounce");
		OBJECT_MINE_CART			= loadSound("object_mine_cart");
		OBJECT_STOP_LIGHT			= loadSound("object_mine_cart_track");
		OBJECT_MOVE					= loadSound("object_move");
		OBJECT_ROLLER				= loadSound("object_roller");
		OBJECT_SWITCH				= loadSound("object_switch");
		OBJECT_TURNSTILE			= loadSound("object_turnstile");
		
		PLAYER_DIE					= loadSound("player_die");
		PLAYER_FALL					= loadSound("player_fall");
		PLAYER_HURT					= loadSound("player_hurt");
		PLAYER_JUMP					= loadSound("player_jump");
		PLAYER_LAND					= loadSound("player_land");
		PLAYER_LOW_HEALTH			= loadSound("player_low_health");
		PLAYER_PICKUP				= loadSound("player_pickup");
		PLAYER_SWIM					= loadSound("player_swim");
		PLAYER_THROW				= loadSound("player_throw");
		PLAYER_WADE					= loadSound("player_wade");

		SCREEN_CLOSE				= loadSound("screen_close");
		SCREEN_CURSOR				= loadSound("screen_cursor");
		SCREEN_OPEN					= loadSound("screen_open");
		SCREEN_SELECT				= loadSound("screen_select");

		TEXT_CONTINUE				= loadSound("text_continue");
		TEXT_READ					= loadSound("text_read");
		
		TUNE_ESSENCE				= loadSound("tune_essence");
		TUNE_REWARD					= loadSound("tune_reward");
		TUNE_SECRET					= loadSound("tune_secret");
		TUNE_SECRET_LONG			= loadSound("tune_secret_long");
		TUNE_TREASURE				= loadSound("tune_treasure");
		
		WARP_EXIT					= loadSound("warp_exit");
	}
	
	

	public static void playRandom(Sound[] sounds) {
		if (sounds.length > 0) {
			int index = GMath.random.nextInt(sounds.length);
			play(sounds[index]);
		}
	}
	
	public static void play(Sound sound) {
		if (sound != null)
			sound.play();
	}
	
	
	public static Sound loadSound(String path) {
		return loadSound(path, 1, 0);
	}

	public static Sound loadSound(String path, int numChannels) {
		return loadSound(path, numChannels, 0);
	}
	
	public static Sound loadSound(String path, float volumeOffset) {
		return loadSound(path, 1, volumeOffset);
	}
	
	public static Sound loadSound(String path, int numChannels, float volumeOffset) {
		try {
			if (!path.endsWith(".wav"))
				path += ".wav";
			System.out.println("- sounds/" + path);

			Sound sound = new Sound(path, numChannels, volumeOffset);
			for (int i = 0; i < numChannels; i++) {
				Clip clip = loadClip("/sounds/" + path);
				if (clip != null) {
					sound.setClip(i, clip);
				}
				else {
					return null;
				}
			}
//			sound.setVolume(volumeOffset);
			sounds.add(sound);
			return sound;
		}
		catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
	    	e.printStackTrace();
	    }
		catch (LineUnavailableException e) {
	    	e.printStackTrace();
	    }
		return null;
	}

	public static Clip loadClip(String path)
			throws UnsupportedAudioFileException, IOException,
			LineUnavailableException
	{
		URL url = Sounds.class.getResource(path);
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
		return clip;
	}
}
