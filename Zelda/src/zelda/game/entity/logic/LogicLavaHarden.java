package zelda.game.entity.logic;

import java.util.ArrayList;
import zelda.common.Settings;
import zelda.common.geometry.Point;
import zelda.common.graphics.Animation;
import zelda.common.properties.FrameEntityData;
import zelda.common.properties.Properties;
import zelda.common.properties.PropertyHolder;
import zelda.game.control.script.Function;
import zelda.game.entity.Entity;
import zelda.game.entity.object.dungeon.ObjectHardenedLava;
import zelda.game.world.Frame;
import zelda.game.world.tile.GridTile;

public class LogicLavaHarden extends LogicEntity {
	private ArrayList<Point> sourceLocations;
	private ArrayList<Integer> addAmountList;
	private ArrayList<ObjectHardenedLava> lavaList;
	private boolean hardened;
	private boolean hardening;
	private boolean melting;
	private int timer;
	
	
	public LogicLavaHarden() {
		super();
		sprite.newAnimation(new Animation(2, 0));
	}
	
	private void createHardenedLava(Point loc) {
		ObjectHardenedLava lava = new ObjectHardenedLava();
		lava.setObjectData(new FrameEntityData(null));
		lavaList.add(lava);
		game.addEntity(lava);
		lava.setup();
		lava.begin(frame, loc.scaledBy(Settings.TS), new Properties());
	}
	
	public void hardenLava() {
		if (!hardened && !melting) {
    		hardening = true;
    		hardened  = true;
    		timer     = 0;
    		
    		for (int i = 0; i < sourceLocations.size(); i++) {
    			Point loc = sourceLocations.get(i);
    			createHardenedLava(loc);
    		}
		}
	}
	
	public void meltLava() {
		if (hardened && !melting) {
    		melting  = true;
    		hardened = false;
    		timer    = 0;
    		
    		for (int i = 0; i < sourceLocations.size() && lavaList.size() > 0; i++) {
    			lavaList.get(0).destroy();
    			lavaList.remove(0);
    		}
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		timer++;
		
		if (timer > 7) {
			timer = 0;

			if (hardening) {
				int listSize = lavaList.size();
				int amount = 0;

				for (int x = 0; x < frame.getWidth(); x++) {
					for (int y = 0; y < frame.getHeight(); y++) {
						GridTile t = frame.getGridTile(x, y);
						if (t.getProperties().getBoolean("lava", false)) {
							boolean good = false;

							for (int i = 0; i < listSize; i++) {
								if (t.getLocation().equals(lavaList.get(i).getLocation())) {
									good = false;
									break;
								}
								else if (t.getLocation().isAdjacentTo(lavaList.get(i).getLocation())) {
									good = true;
								}
							}

							if (good) {
								createHardenedLava(t.getLocation());
								amount++;
							}
						}
					}
				}

				addAmountList.add(amount);

				if (lavaList.size() == listSize)
					hardening = false;
			}
			else if (melting) {
				if (lavaList.size() == 0 || addAmountList.size() == 0) {
					melting = false;
					lavaList.clear();
					addAmountList.clear();
				}
				else {
					int amount = addAmountList.get(0);
					addAmountList.remove(0);
					for (int i = 0; i < amount; i++) {
						lavaList.get(0).destroy();
						lavaList.remove(0);
					}
				}
			}
		}
	}
	
	@Override
	public void begin() {
		super.begin();
		
		hardened = false;
		sourceLocations = new ArrayList<Point>();
		addAmountList   = new ArrayList<Integer>();
		lavaList = new ArrayList<ObjectHardenedLava>();
		
		ArrayList<Entity> entities = game.getEntities();
		for (int i = 0 ; i < entities.size(); i++) {
			if (entities.get(i) instanceof ObjectHardenedLava) {
				ObjectHardenedLava lava = (ObjectHardenedLava) entities.get(i);
				sourceLocations.add(lava.getLocation());
				lava.destroy();
			}
		}
	}
	
	@Override
	public void setup() {
		super.setup();

		objectData.addFunction(new Function("melt") {
			@Override
			public String execute(ArrayList<String> args, PropertyHolder holder,
					Frame frame)
			{
				meltLava();
				return "";
			}
		});
		
		objectData.addFunction(new Function("harden") {
			@Override
			public String execute(ArrayList<String> args, PropertyHolder holder,
					Frame frame)
			{
				hardenLava();
				return "";
			}
		});
	}
	
	@Override
	public LogicLavaHarden clone() {
		return new LogicLavaHarden();
	}
}
