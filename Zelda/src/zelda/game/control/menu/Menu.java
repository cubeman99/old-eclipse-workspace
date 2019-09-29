package zelda.game.control.menu;

import java.awt.Color;
import zelda.common.Settings;
import zelda.common.Sounds;
import zelda.common.geometry.Vector;
import zelda.common.graphics.Draw;
import zelda.game.control.GameInstance;
import zelda.game.control.event.Event;
import zelda.game.control.event.EventQueue;
import zelda.game.control.event.EventScreenFade;
import zelda.main.Keyboard;
import OLD.SpriteOLD;
import OLD.SpriteSheetOLD;


public class Menu extends Event {
	public static final Color COLOR_LIGHT = new Color(157, 56, 13);
	public static final Color COLOR_DARK = new Color(95, 33, 3);
	private static final int TRANSITION_SPEED = 12;
	private static final int TRANSITION_OFFSET = 8;

	private GameInstance game;
	private MenuScreen[] screens;
	private int screenIndex;
	private boolean transitioning;
	private int transitionDist;
	private boolean opened;
	private SpriteSheetOLD sheetScreens;



	// ================== CONSTRUCTORS ================== //

	public Menu(GameInstance game) {
		this.game = game;
		screenIndex = 0;
		transitioning = false;
		transitionDist = 0;
		opened = false;
		sheetScreens = new SpriteSheetOLD("menuScreens.png",
				Settings.VIEW_SIZE.x, Settings.VIEW_SIZE.y);

		screens = new MenuScreen[] {new ScreenInventory(this, 0),
				new ScreenItems(this, 1), new ScreenEssences(this, 2)};
	}



	// =================== ACCESSORS =================== //

	public SpriteOLD getScreenBackgroundSprite(int index) {
		return sheetScreens.getSprite(index, 0);
	}

	private MenuScreen getScreen() {
		return screens[screenIndex];
	}

	public GameInstance getGame() {
		return game;
	}

	public boolean isOpen() {
		return opened;
	}



	// ==================== MUTATORS ==================== //


	public void open() {
		opened = true;
		screenIndex = 0;
		final Menu menu = this;
		Sounds.SCREEN_OPEN.play();

		Event fadeOpen = new EventScreenFade(10, Color.WHITE, EventScreenFade.FADE_IN) {
			@Override
			public void begin() {
				super.begin();
			}

			@Override
			public void draw() {
				menu.draw();
				super.draw();
			}
		};

		Event fadeClose = new EventScreenFade(10, Color.WHITE,
				EventScreenFade.FADE_OUT) {
			@Override
			public void end() {
				super.end();
			}

			@Override
			public void draw() {
				menu.draw();
				super.draw();
			}
		};

		game.playEvent(new EventQueue(new EventScreenFade(10, Color.WHITE,
				EventScreenFade.FADE_OUT), fadeOpen, menu, fadeClose,
				new EventScreenFade(10, Color.WHITE, EventScreenFade.FADE_IN)));
	}

	private void nextScreen() {
		transitioning = true;
		transitionDist = TRANSITION_OFFSET;
		Sounds.SCREEN_OPEN.play();
	}


	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void begin() {
		super.begin();
		//open();
	}

	@Override
	public void end() {
		super.end();
		Sounds.SCREEN_CLOSE.play();
	}

	@Override
	public void update() {
		if (transitioning) {
			transitionDist += TRANSITION_SPEED;

			if (transitionDist >= Settings.VIEW_SIZE.x) {
				transitioning = false;
				transitionDist = 0;
				screenIndex = (screenIndex + 1) % screens.length;
			}
		}
		else if (Keyboard.select.pressed())
			nextScreen();

		if (!transitioning) {
			if (Keyboard.enter.pressed() && getScreen().canClose())
				end();
			else
				getScreen().update();
		}
	}

	@Override
	public void draw() {
		if (transitioning) {
			Draw.setViewPosition(new Vector(transitionDist - TRANSITION_OFFSET,
					-16));
			getScreen().draw();

			Draw.setViewPosition(new Vector(transitionDist
					- Settings.VIEW_SIZE.x, -16));
			screens[(screenIndex + 1) % screens.length].draw();
		}
		else {
			Draw.setViewPosition(new Vector(0, -16));
			getScreen().draw();
		}
	}
}
