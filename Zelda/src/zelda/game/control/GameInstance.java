package zelda.game.control;

import java.awt.Graphics;
import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.collision.Collision;
import zelda.common.geometry.Point;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.common.util.Direction;
import zelda.game.control.event.Event;
import zelda.game.control.event.EventStack;
import zelda.game.control.event.EventTextMessage;
import zelda.game.control.hud.HUD;
import zelda.game.control.map.MapScreen;
import zelda.game.control.menu.Menu;
import zelda.game.control.text.Message;
import zelda.game.control.transition.TransitionEntrance;
import zelda.game.control.transition.TransitionExit;
import zelda.game.control.transition.TransitionPush;
import zelda.game.control.transition.TransitionPushFlash;
import zelda.game.entity.Entity;
import zelda.game.entity.collectable.Collectables;
import zelda.game.entity.object.global.ObjectPlayerStart;
import zelda.game.player.Player;
import zelda.game.world.Frame;
import zelda.game.world.Level;
import zelda.game.world.World;
import zelda.game.world.tile.GridTile;
import zelda.game.world.tile.ObjectTile;
import zelda.main.GameRunner;
import zelda.main.Keyboard;


/**
 * The Legends of Zelda Oracle of Ages/Seasons Replication. Project started
 * Tuesday, September 10, 2013, 7:05 PM.
 * 
 * @author David Jordan
 */
public class GameInstance {
	private View view;
	private Player player;
	private World world;
	private HUD hud;
	private Menu menu;
	private GameDebug debug;
	public Collectables collectables;
	private EventStack eventStack;
	private Vector viewFollowFocus;
	
	

	// ================== CONSTRUCTORS ================== //

	public GameInstance(GameRunner runner) {
		Collision.setControl(this);
		
		this.world        = new World(this);
		this.player       = new Player(this);
		this.menu         = new Menu(this);
		this.view         = new View(this);
		this.hud          = new HUD(this);
		this.debug        = new GameDebug(this);
		this.collectables = new Collectables(this);
		this.eventStack   = new EventStack();
		this.viewFollowFocus = new Vector();
		
		eventStack.addEvent(new Event() {
			@Override
			public void update() {
				if (Keyboard.start.pressed())
					game.getMenu().open();
				else if (Keyboard.select.pressed() && getLevel().getDungeon() != null)
					new MapScreen(game).open();
				else {
					game.setViewFollowFocus(game.getPlayer().getCenter());
					game.getWorld().update();
					game.getDebug().update();
					game.getView().panTo(viewFollowFocus);
					Resources.update();
				}
			}
			
			@Override
			public void draw() {
				Draw.setViewPosition(view.getPosition().minus(0, 16));
				if (game.getFrame() != null)
					game.getFrame().draw();
				game.getHud().draw();
			}
		});
		
		eventStack.begin(this);
		
		this.world.addLevel("Overworld", 1, 1, Frame.SIZE_SMALL);
	}



	// =================== ACCESSORS =================== //

	public ArrayList<Entity> getEntities() {
		return getFrame().getEntities();
	}

	public Player getPlayer() {
		return player;
	}

	public Menu getMenu() {
		return menu;
	}

	public View getView() {
		return view;
	}

	/** Return the world. **/
	public World getWorld() {
		return world;
	}

	/** Return the current level. **/
	public Level getLevel() {
		return world.getCurrentLevel();
	}
	
	public GameDebug getDebug() {
		return debug;
	}

	public HUD getHud() {
		return hud;
	}
	
	public int getInstanceNumber(Class<? extends Entity> cls) {
		int count = 0;
		for (int i = 0; i < getFrame().getEntities().size(); i++) {
			if (cls.isAssignableFrom(getFrame().getEntities().get(i).getClass()))
				count++;
		}
		return count;
	}

	/** Return the current frame. **/
	public Frame getFrame() {
		return world.getCurrentFrame();
	}
	
	

	// ==================== MUTATORS ==================== //

	public Entity addEntity(Entity e) {
		return getFrame().addEntity(this, e);
	}

	public boolean removeEntity(Entity e) {
		return getFrame().removeEntity(e);
	}

	public void playEvent(Event e) {
		if (e != null) {
			System.out.println("PLAYING EVENT " + e.getClass().getSimpleName());
			eventStack.newEvent(e);
		}
	}
	
	public void setViewFollowFocus(Vector focus) {
		this.viewFollowFocus.set(focus);
	}

	/** Set the world the game is taking place in. **/
	public void setWorld(World world) {
		getFrame().leave();
		this.world = world;
		this.world.setControl(this);

		// Find the frame with the player in it.
		for (int i = 0; i < world.getNumLevels(); i++) {
			Level level = world.getLevel(i);

			for (int x = 0; x < level.getSize().x; x++) {
				for (int y = 0; y < level.getSize().y; y++) {
					Frame frame = level.getFrame(x, y);
					for (ObjectTile t : frame.getObjectTiles()) {
						if (t.getFrameObject() != null
								&& t.getFrameObject() instanceof ObjectPlayerStart) {
							world.setCurrentLevel(i);
							player.setPosition(new Vector(t.getPosition()));
							getLevel().setLocation(new Point(x, y));
							getFrame().enter();
							addEntity(player);
							view.centerAt(player);
							player.recordFrameEnterPosition();
							return;
						}
					}
				}
			}
		}

		getFrame().enter();
	}

	/** Attempt to transition to a relatively located frame. **/
	public void nextFrame(int dir) {
		Point relativePos = Direction.getDirPoint(dir);
		Point nextFrameLoc = getLevel().getLocation().plus(relativePos);
		Frame frame = getFrame();
		String transitionName = frame.getProperties().get(
				"transition" + dir + "_type", "push");

		// System.out.println("Transition " + transitionName + " : " + dir +
		// "   transition" + dir + "_type", "push");


		if (transitionName.equals("push")) {
			if (getLevel().frameExists(nextFrameLoc)) {
				playEvent(new TransitionPush(this, relativePos));
			}
		}
		else if (transitionName.equals("flash")) {
			if (getLevel().frameExists(nextFrameLoc)) {
				playEvent(new TransitionPushFlash(this, relativePos));
			}
		}
		else if (transitionName.equals("exit")
				|| transitionName.equals("entrance")) {
			Frame frameNew = null;
			String levelName = frame.getProperties().get(
					"transition" + dir + "_warp_level", "");
			String warpID = frame.getProperties().get(
					"transition" + dir + "_warp_point", "");
			Level level = world.getLevel(levelName);

			if (level != null) {
				GridTile warpTile = level.getGridTile(warpID);

				if (warpTile != null) {
					if (transitionName.equals("exit"))
						playEvent(new TransitionExit(this,
								warpTile.getFrame(), warpTile.getPosition()));
					else
						playEvent(new TransitionEntrance(this,
								warpTile.getFrame(), warpTile.getPosition()));
					// player.setPosition(warpTile.getPosition());
				}
			}
			
			// frameTransition = new TransitionSplit(this, frameNew);
		}
	}

	/** Make the text reader read the given message. **/
	public void readMessage(Message msg) {
		playEvent(new EventTextMessage(msg));
	}
	
	/** Update the game stack. **/
	public void update() {
//		System.out.println(eventStack.getSize());
		eventStack.update();
	}

	/** Draw the game stack. **/
	public void draw(Graphics g) {
		eventStack.draw();
		debug.draw();
	}
}
