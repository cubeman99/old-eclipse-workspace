package zelda.game.control.map;

import java.awt.Color;
import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.common.graphics.Sprite;
import zelda.game.control.GameInstance;
import zelda.game.control.event.Event;
import zelda.game.control.event.EventQueue;
import zelda.game.control.event.EventScreenFade;
import zelda.game.world.Dungeon;
import zelda.game.world.Level;
import zelda.main.Keyboard;



public class MapScreen extends Event {
	private Dungeon dungeon;
	private ArrayList<MapFloor> floors;
	private int floorIndex;
	private int playerFloorIndex;
	private double floorDisplayAmount;
	private Sprite cursorSprite;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
    public MapScreen(GameInstance game) {
    	this.game         = game;
    	this.dungeon      = game.getLevel().getDungeon();
    	this.cursorSprite = new Sprite(Resources.SPRITE_MAP_CURSOR);
    	
    	ArrayList<Level> levels = game.getWorld().getDungeonLevels(dungeon);
    	floors = new ArrayList<MapFloor>();
    	floorIndex = 0;
    	
    	Point size = new Point(8, 8);
    	for (int i = 0; i < levels.size(); i++) {
    		MapFloor floor = new MapFloor(this, size, levels.get(i), floors.size());
    		if (levels.get(i) == game.getLevel())
    			floorIndex = floors.size();
    		if (hasMap() || floor.isDiscovered())
    			floors.add(floor);
    	}
    	playerFloorIndex   = floorIndex;
    	floorDisplayAmount = floorIndex;
    }


    
    // =================== ACCESSORS =================== //

    public Sprite getCursorSprite() {
		return cursorSprite;
	}
    
    public boolean hasCompass() {
    	return dungeon.hasItem("compass");
    }

    public boolean hasMap() {
    	return dungeon.hasItem("map");
    }
    
    public int getNumFloors() {
    	return floors.size();
    }
    
    public int getFloorIndex() {
		return floorIndex;
	}
    
    public int getPlayerFloorIndex() {
		return playerFloorIndex;
	}
    
    
    
    // ==================== MUTATORS ==================== //
    
	public void open() {
		final MapScreen map = this;
		Sounds.SCREEN_OPEN.play();

		Event fadeOpen = new EventScreenFade(10, Color.WHITE, EventScreenFade.FADE_IN) {
			@Override
			public void begin() {
				super.begin();
			}

			@Override
			public void draw() {
				map.draw();
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
				map.draw();
				super.draw();
			}
		};
		
		game.playEvent(new EventQueue(new EventScreenFade(10, Color.WHITE,
				EventScreenFade.FADE_OUT), fadeOpen, map, fadeClose,
				new EventScreenFade(10, Color.WHITE, EventScreenFade.FADE_IN)));
	}
	
    
    
    // ================ IMPLEMENTATIONS ================ //
    
    @Override
    public void update() {
    	super.update();
    	cursorSprite.update();
    	
    	// Arrow keys to traverse floors.
    	if (Keyboard.up.pressed() && floorIndex < floors.size() - 1) {
    		floorIndex++;
    		Sounds.SCREEN_CURSOR.play();
    	}
    	if (Keyboard.down.pressed() && floorIndex > 0) {
    		floorIndex--;
    		Sounds.SCREEN_CURSOR.play();
    	}
    	
    	// Update shifting floors.
		if (Math.abs(floorIndex - floorDisplayAmount) > 0.01)
			floorDisplayAmount += Math.signum(floorIndex - floorDisplayAmount) * 0.1;
		else
			floorDisplayAmount = floorIndex;
    	
		// Close the map.
    	if (Keyboard.select.pressed() || Keyboard.b.pressed()) {
    		end();
    		Sounds.SCREEN_CLOSE.play();
    	}
    }
    
	@Override
	public void draw() {
		super.draw();
		Draw.setViewPosition(0, 0);
		
		// Draw the map-screen background.
		Sprite spr = new Sprite(Resources.SHEET_BACKGROUNDS, 4, 0);
		Draw.drawSprite(spr, 0, 0);
		
		// Draw floors.
		for (int i = 0; i < floors.size(); i++)
			floors.get(i).draw(floorDisplayAmount - i);
		
		Draw.setViewPosition(0, 0);
		
		// Draw floor-traversal arrows.
		if (Math.abs(floorIndex - floorDisplayAmount) < 0.01) {
    		if (floorIndex > 0)
    			Draw.drawSprite(Resources.SPRITE_MAP_ARROW_DOWN, 108, 108);
    		if (floorIndex < floors.size() - 1)
    			Draw.drawSprite(Resources.SPRITE_MAP_ARROW_UP, 108, 28);
		}
		
		// Draw items.
		if (dungeon.hasItem("map"))
			Draw.drawImage(Resources.SHEET_ICONS_LARGE, 0, 5, 8, 110);
		if (dungeon.hasItem("compass"))
			Draw.drawImage(Resources.SHEET_ICONS_LARGE, 1, 5, 32, 110);
		if (dungeon.hasItem("boss_key"))
			Draw.drawImage(Resources.SHEET_ICONS_LARGE, 2, 5, 8, 128);
		
		// Draw Small Keys.
		int numKeys = dungeon.getNumSmallKeys();
		if (numKeys > 0) {
			Draw.drawImage(Resources.SHEET_ICONS_THIN, 0, 4, 32, 128);
			Draw.drawText("x" + numKeys, new Point(40, 136), Resources.FONT_SMALL, new Color(144, 136, 16));
			Draw.drawText("x" + numKeys, new Point(40, 135), Resources.FONT_SMALL, Color.BLACK);
		}
	}
}
