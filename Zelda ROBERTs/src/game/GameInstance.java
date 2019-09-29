package game;

import graphics.library.Library;
import items.ItemList;
import items.ammo.*;
import items.equipment.*;
import items.weapons.*;

import java.awt.Graphics2D;
import java.util.ArrayList;

import main.Keyboard;
import menu.*;
import tile.BasicTile;
import tile.TileRegion;
import transition.*;
import world.Room;

import entity.living.Player;

/**
 * The main class that contains all the information about the game.
 * @author	Robert Jordan
 */
public class GameInstance {

	// ====================== Constants =======================
	
	public static final int NUM_DUNGEONS	= 4;
	public static final int MAX_RUPEES		= 999;
	
	// ======================= Members ========================

	
	/** The HUD drawn at the top of the screen. */
	public HUD hud;
	/** The player's inventory. */
	public Inventory inventory;
	/** The player entity. */
	public Player player;
	/** The current screen being displayed and updated. */
	public Screen screen;
	/** The current room the player is in. */
	public Room room;
	/** The list of menus in the game. */
	private ArrayList<Menu> menus;

	/** True if the game is in a transition between the room and menu. */
	private boolean inTransition;
	/** The current transition being drawn by the game. */
	private Transition transition;
	
	/** The number of rupees the player has. */
	public int rupees;
	/** The number of small keys the player has for each dungeon. */
	public int[] smallKeys;
	
	/** True if the player has a higher heart limit. */
	public boolean advancedGame;
	/** True if the menus should be advanced. */
	public boolean advancedMenus;
	/** The current dungeon the player is in. */
	public int currentDungeon;
	/** A debug mode for placing tiles. */
	public boolean constructMode;
	/** True if the game is currently in a menu. */
	public boolean inMenu;
	
	// ===================== Constructors =====================
	/** Constructs the default game instance. */
	public GameInstance() {
		
		ItemList.initialize();
		
		// Initialize settings:
		this.advancedGame	= false;
		this.advancedMenus	= false;
		this.currentDungeon	= -1;
		this.constructMode	= false;
		
		this.inMenu			= false;
		this.inTransition	= false;
		this.transition		= null;
		
		// Initialize linked classes:
		this.hud			= new HUD();
		this.inventory		= new Inventory();
		
		this.player			= new Player();
		this.room			= new Room();
		this.screen			= this.room;
		this.menus			= new ArrayList<Menu>();
		this.menus.add(new WeaponMenu());
		this.menus.add(new KeyItemMenu());
		this.menus.add(new EssenceMenu());
		
		this.room.addEntity(player);

		for (int i = 0; i < 10; i++) {
			room.setTile(i, 0, new BasicTile(CollisionType.solid, Library.tilesets.landformTiles, 1, 3));
		}
		room.setTile(0, 1, new BasicTile(CollisionType.solid, Library.tilesets.landformTiles, 7, 29));
		room.setTile(3, 4, new BasicTile(CollisionType.land, Library.tilesets.landformTiles, 7, 29, 22, 1, 3, 10, 8, 8));
		
		room.setTile(5, 3, new BasicTile(CollisionType.ladder, Library.tilesets.landformTiles, 8, 9));
		room.setTile(5, 4, new BasicTile(CollisionType.ladder, Library.tilesets.landformTiles, 8, 9));
		for (int i = 0; i < 4; i++) {
			//room.setTile(6 + i, 3, new BasicTile(CollisionType.air, Library.tilesets.landformTiles, 4, 25));
			//room.setTile(6 + i, 4, new BasicTile(CollisionType.air, Library.tilesets.landformTiles, 4, 25));
			//room.setTile(6 + i, 5, new BasicTile(CollisionType.air, Library.tilesets.landformTiles, 4, 25));
			room.setTile(6 + i, 3, new BasicTile(CollisionType.water, Library.tilesets.waterTiles, 0, 0, 1, 0, 2, 0, 3, 0));
			room.setTile(6 + i, 4, new BasicTile(CollisionType.water, Library.tilesets.waterTiles, 0, 0, 1, 0, 2, 0, 3, 0));
			room.setTile(6 + i, 5, new BasicTile(CollisionType.water, Library.tilesets.waterTiles, 0, 0, 1, 0, 2, 0, 3, 0));
		}
		
		room.setTile(2, 6, new BasicTile(CollisionType.hole, Library.tilesets.specialTiles, 8, 2));
		
		this.rupees			= 0;
		this.smallKeys		= new int[NUM_DUNGEONS];
		for (int i = 0; i < NUM_DUNGEONS; i++)
			this.smallKeys[i] = 0;
		

		this.inventory.addWeapon(new Shield(1));
		this.inventory.addWeapon(new Sword(1));
		
		this.inventory.addWeapon(new BiggoronSword());
		this.inventory.addWeapon(new SeedSatchel(1, 0));
		this.inventory.addWeapon(new BombBag(1));
		this.inventory.addWeapon(new PowerBracelet(1));
		this.inventory.addWeapon(new RocsFeather(1));
		this.inventory.addWeapon(new Slingshot(1, 0));

		this.inventory.addItem(ItemList.kokiriTunic, 2, 0);
		this.inventory.addItem(ItemList.goronTunic, 2, 1);
		this.inventory.addItem(ItemList.zoraTunic, 2, 2);
		
		this.inventory.addAmmo(new ScentSeeds());
		this.inventory.addAmmo(new PegasusSeeds());
		this.inventory.addAmmo(new GaleSeeds());
		this.inventory.addAmmo(new MysterySeeds());
		
		this.inventory.addItem(ItemList.kokiriEmerald, 4, 0);
		this.inventory.addItem(ItemList.goronRuby, 4, 1);
		this.inventory.addItem(ItemList.zoraSaphire, 4, 2);
		this.inventory.addItem(ItemList.flippers, 3, 0);
		this.inventory.addItem(ItemList.magicPotion, 3, 1);
		
		
		this.rupees				= 23;
		this.player.health		= 17;
		this.player.maxHealth	= 14 * 4;
		this.smallKeys[0]		= 3;
	}
	/** Initializes the game and sets up the container variables. */
	public void initialize() {
		room.initialize(this);
		for (int i = 0; i < menus.size(); i++) {
			menus.get(i).initialize(this);
		}
		hud.initialize(this);
		inventory.initialize(this);
		
		player.position.set(60, 40);
	}

	// ======================= Updating =======================
	
	/** Called every step to update the game. */
	public void update() {
		
		if (Keyboard.insert.pressed()) {
			player.maxHealth += 4;
			if (player.maxHealth > (advancedGame ? 16 : 14) * 4)
				player.maxHealth = (advancedGame ? 16 : 14) * 4;
		}
		if (Keyboard.delete.pressed()) {
			player.maxHealth -= 4;
			if (player.maxHealth < 1 * 4)
				player.maxHealth = 1 * 4;
		}

		if (Keyboard.home.pressed()) {
			player.health += 1;
			if (player.health > player.maxHealth)
				player.health = player.maxHealth;
		}
		if (Keyboard.end.pressed()) {
			player.health -= 1;
			if (player.health < 1)
				player.health = 1;
		}
		if (Keyboard.backspace.pressed()) {
			advancedGame = !advancedGame;
		}
		
		// Check for transition events
		if (inTransition) {
			if (transition.isTransitionFinished()) {
				inTransition = false;
				transition = null;
				screen.enterScreen();
			}
			else if (transition.isOldScreenFinished()) {
				// Change the menu state if necessary
				if ((screen instanceof Menu) != inMenu) {
					inMenu = !inMenu;
				}
			}
		}
		
		// Update the screen or transition
		if (inTransition) {
			transition.update();
		}
		else {
			screen.update();
		}
		
	}
	/** Called every step to draw the game. */
	public void draw(Graphics2D g) {

		if (inTransition) {
			transition.draw(g);
		}
		else {
			screen.draw(g, 0, 16);
			hud.draw(g);
		}
	}


	// ===================== Information ======================

	/** Returns true if this game is an advanced game. */
	public boolean isAdvancedGame() {
		return advancedGame;
	}
	/** Returns true if this game is an advanced game. */
	public boolean isAdvancedMenus() {
		return advancedMenus;
	}
	/** Returns true if the player is in a dungeon. */
	public boolean isInDungeon() {
		return currentDungeon != -1;
	}
	/** Returns true if the game is in a menu. */
	public boolean isInMenu() {
		return inMenu;
	}
	/** Returns true if the game is in a transition. */
	public boolean isInTransition() {
		return inTransition;
	}
	
	// ======================== Menus =========================
	
	/** Gets the menu with the given id. */
	public Menu getMenu(String id) {
		for (Menu m : menus) {
			if (m.id.equals(id)) {
				return m;
			}
		}
		return null;
	}
	/** Opens the inventory menu. */
	public void openMenu(String id) {
		startTransition(getMenu(id), new Fade(screen, getMenu(id), hud));
	}
	/** Switches to the next inventory menu. */
	public void nextMenu(String id) {
		startTransition(getMenu(id), new Push(0, 8, screen, getMenu(id), hud));
	}
	/** Closes the current menu. */
	public void closeMenu() {
		startTransition(room, new Fade(screen, room, hud));
	}
	/** Starts the transition to another screen. */
	public void startTransition(Screen newScreen, Transition transition) {
		this.transition = transition;
		screen.leaveScreen();
		inTransition = true;
		screen = newScreen;
	}
}