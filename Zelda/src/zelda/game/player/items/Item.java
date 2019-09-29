package zelda.game.player.items;

import java.awt.Color;
import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.geometry.Point;
import zelda.common.graphics.Draw;
import zelda.common.graphics.ImageSheet;
import zelda.common.graphics.Sprite;
import zelda.common.util.Currency;
import zelda.game.control.GameInstance;
import zelda.game.control.menu.SlotFiller;
import zelda.game.entity.Entity;
import zelda.game.entity.collectable.CollectableReward;
import zelda.game.player.Player;
import zelda.main.Keyboard;
import zelda.main.Keyboard.Key;


public abstract class Item implements SlotFiller {
	protected static final int MAX_LEVELS = 3;
	protected static final int MAX_TYPES = 5;

	protected GameInstance game;
	protected Player player;
	protected String defaultName;

	protected int numLevels;
	protected int level;
	protected String[] name;
	protected String[] description;

	protected int numTypes;
	protected int typeIndex;
	protected String[] typeName;
	protected String[] typeDescription;
	protected Currency[] ammo;

	protected EntityTracker entityTracker;

	protected boolean twoHanded;

	protected Sprite[] slotIcons;
	protected Sprite[] slotEquippedIcons;
	protected Sprite[] slotTypeIcons;
	protected Sprite[] typeIcons;

	protected Sprite[] collectableSprites;
	protected int collectableAmmoAmount; // TODO

	protected String[] rewardMessages;
	protected int[] rewardHoldTypes;

	protected boolean obtained;
	protected boolean usedInMineCart;

	

	// ================== CONSTRUCTORS ================== //

	public Item(Player player, String defaultName) {
		this.game = player.getGame();
		this.player = player;
		this.defaultName = defaultName;

		obtained = false;

		numLevels = 1;
		level = 0;
		name = new String[MAX_LEVELS];
		description = new String[MAX_LEVELS];

		numTypes = 1;
		typeIndex = 0;
		typeName = new String[MAX_TYPES];
		typeDescription = new String[MAX_TYPES];
		ammo = new Currency[MAX_TYPES];
		collectableSprites = new Sprite[MAX_TYPES];
		collectableAmmoAmount = 5;
		twoHanded = false;

		slotIcons = new Sprite[MAX_LEVELS];
		slotEquippedIcons = new Sprite[MAX_LEVELS];
		slotTypeIcons = new Sprite[MAX_TYPES];
		typeIcons = new Sprite[MAX_TYPES];

		rewardMessages = new String[MAX_LEVELS];
		rewardHoldTypes = new int[MAX_LEVELS];
		entityTracker = new EntityTracker(0);
		
		usedInMineCart = false;

		for (int i = 0; i < MAX_LEVELS; i++) {
			rewardMessages[i] = "...";
			rewardHoldTypes[i] = CollectableReward.TYPE_TWO_HAND;
		}
	}



	// ============== DO-NOTHING METHODS ============== //

	public void onStart() {
	} // Called when the item is switched to.

	public void onEnd() {
	} // Called when the item is put away.

	public void interrupt() {
	} // Immediately interrupt this item (ex: if link falls in a hole).

	public void onChangeLevel() {
	} // Called when the item's level is changed.

	public void drawUnder() {
	} // Draws under link's sprite.

	public void drawOver() {
	} // Draws over link's sprite.



	// =================== ACCESSORS =================== //

	public boolean isObtained() {
		return obtained;
	}
	
	public boolean canBeUsedInMineCart() {
		return usedInMineCart;
	}
	
	public boolean isEquipped() {
		return player.isItemEquipped(this);
	}

	public Player getPlayer() {
		return player;
	}

	/** Return whether this item is two-handed. **/
	public boolean isTwoHanded() {
		return twoHanded;
	}

	public int getLevel() {
		return level;
	}

	public boolean canHoldAmmo() {
		return (ammo[0] != null);
	}

	public EntityTracker getEntityTracker() {
		return entityTracker;
	}

	public Sprite getTypeIcon(int typeIndex) {
		return typeIcons[typeIndex];
	}

	/** Return the number of types this item has. **/
	public int getNumTypes() {
		return numTypes;
	}

	public int getTypeIndex() {
		return typeIndex;
	}

	public Sprite getSlotIcon(int level) {
		return slotIcons[level];
	}

	public Currency getAmmo(int typeIndex) {
		return ammo[typeIndex];
	}

	public String getTypeName(int typeIndex) {
		return typeName[typeIndex];
	}

	public String getTypeDescription(int typeIndex) {
		return typeDescription[typeIndex];
	}

	public Sprite getCollectableSprite(int typeIndex) {
		return collectableSprites[typeIndex];
	}

	public int getCollectableAmmoAmount() {
		return collectableAmmoAmount;
	}

	public String getDefaultName() {
		return defaultName;
	}

	public String getRewardMessage(int level) {
		return rewardMessages[level];
	}

	public int getRewardHoldType(int level) {
		return rewardHoldTypes[level];
	}

	/** Return whether this item's key is down. **/
	protected boolean keyDown() {
		if (!player.isItemEquipped(this) || !player.canUseItem(this))
			return false;
		
		if (twoHanded)
			return (Keyboard.a.down() || Keyboard.b.down());

		Key k = Keyboard.actions[player.getInventory().getEquippedSlot(this)];
		return k.down();
	}

	/** Return whether this item's key is pressed. **/
	protected boolean keyPressed() {
		if (!player.isItemEquipped(this) || !player.canUseItem(this))
			return false;

		if (twoHanded)
			return (Keyboard.a.pressed() || Keyboard.b.pressed());

		Key k = Keyboard.actions[player.getInventory().getEquippedSlot(this)];
		return k.pressed();
	}

	/** Return whether this item's key is released. **/
	protected boolean keyReleased() {
		if (!player.isItemEquipped(this) || !player.canUseItem(this))
			return false;

		if (twoHanded)
			return (Keyboard.a.released() || Keyboard.b.released());

		Key k = Keyboard.actions[player.getInventory().getEquippedSlot(this)];
		return k.released();
	}



	// ==================== MUTATORS ==================== //

	/** Update the item. PLEASE IMPLEMENT THIS. **/
	public void update() {
		entityTracker.update();
	}

	public void setLevel(int level) {
		if (this.level != level) {
			this.level = level;
			onChangeLevel();
		}
	}

	public void setObtained(boolean obtained) {
		this.obtained = obtained;
	}

	protected void setSlotIcons(ImageSheet sheet, int... sourcePositions) {
		for (int i = 0; i < sourcePositions.length; i += 2) {
			slotIcons[i / 2] = new Sprite(sheet, sourcePositions[i],
					sourcePositions[i + 1]);
			if (slotEquippedIcons[i / 2] == null)
				slotEquippedIcons[i / 2] = slotIcons[i / 2];
		}
	}

	protected void setEquippedSlotIcons(ImageSheet sheet,
			int... sourcePositions) {
		for (int i = 0; i < sourcePositions.length; i += 2) {
			slotEquippedIcons[i / 2] = new Sprite(sheet, sourcePositions[i],
					sourcePositions[i + 1]);
		}
	}

	protected void setSlotTypeIcons(ImageSheet sheet, int... sourcePositions) {
		for (int i = 0; i < sourcePositions.length; i += 2) {
			slotTypeIcons[i / 2] = new Sprite(sheet, sourcePositions[i],
					sourcePositions[i + 1]);
		}
	}

	protected void setTypeIcons(ImageSheet sheet, int... sourcePositions) {
		for (int i = 0; i < sourcePositions.length; i += 2) {
			typeIcons[i / 2] = new Sprite(sheet, sourcePositions[i],
					sourcePositions[i + 1]);
		}
	}

	public void setTypeIndex(int typeIndex) {
		this.typeIndex = typeIndex;
	}

	/** Change which type this item is currently. **/
	public void shiftType() {
		if (numLevels > 1) {
			level = (level + 1) % numLevels;
			onChangeLevel();
		}
		else if (numTypes > 1)
			typeIndex = (typeIndex + 1) % numTypes;
	}

	public void drawSlot(Point pos, boolean equipped) {
		// Draw item icon.
		if (equipped && slotEquippedIcons[level] != null) {
			Draw.drawSprite(slotEquippedIcons[level], pos);

			if (twoHanded && level + 1 < slotEquippedIcons.length
					&& slotEquippedIcons[level] != null) {
				// Draw.drawSprite(slotEquippedIcons[level + 1],
				// pos.plus(slotEquippedIcons[level].getWidth(), 0));
			}
		}
		else if (slotIcons[level] != null)
			Draw.drawSprite(slotIcons[level], pos);
		// Draw.drawSprite(slotIcons[level], pos);

		// Draw item type icon.
		if (numTypes > 1 && slotTypeIcons[typeIndex] != null)
			Draw.drawSprite(slotTypeIcons[typeIndex], pos.plus(8, 0));

		int iconWidth = (equipped ? slotEquippedIcons[level].getSheet()
				.getImageSize().x
				: slotIcons[level].getSheet().getImageSize().x);

		// Draw level display.
		if (numLevels > 1) {
			Draw.drawText("<lvl>" + (level + 1), pos.plus((int) iconWidth, 8),
					Resources.FONT_SMALL, Color.BLACK);
		}

		// Draw ammo display.
		if (canHoldAmmo()) {
			Draw.drawText(ammo[typeIndex].getFormattedString(),
					pos.plus((int) iconWidth, 8), Resources.FONT_SMALL,
					Color.BLACK);
		}
	}

	/** Return whether there is any ammo to use. **/
	public boolean hasAmmo() {
		if (ammo[typeIndex] == null)
			return false;
		return !ammo[typeIndex].empty();
	}

	public void giveAmmo(int amount) {
		giveAmmo(typeIndex, amount);
	}

	public void giveAmmo(int typeIndex, int amount) {
		if (ammo[typeIndex] != null)
			ammo[typeIndex].give(amount);
	}

	/** Use a piece of ammo and return whether there is no ammo anymore. **/
	protected boolean useAmmo() {
		if (ammo[typeIndex] != null)
			ammo[typeIndex].take(1);
		return (!hasAmmo());
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public String getName() {
		return name[level];
	}

	@Override
	public String getDescription() {
		return description[level];
	}

	@Override
	public void drawSlot(Point pos) {
		drawSlot(pos, false);
	}



	// =========== ENTITY-TRACKER SUB-CLASS =========== //

	/** Used to keep track of entities. **/
	public class EntityTracker {
		private int maxNumEntities;
		private ArrayList<Entity> entities;


		public EntityTracker(int maxNumEntities) {
			this.maxNumEntities = maxNumEntities;
			this.entities = new ArrayList<Entity>();
		}

		public boolean canMakeEntity() {
			return (entities.size() < maxNumEntities);
		}

		public boolean makeEntity(Entity e) {
			if (canMakeEntity()) {
				entities.add(e);
				return true;
			}
			return false;
		}

		public boolean replaceEntity(Entity oldEntity, Entity newEntity) {
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i) == oldEntity) {
					entities.set(i, newEntity);
					return true;
				}
			}
			return false;
		}

		public void setMaxNumEntities(int maxNumEntities) {
			this.maxNumEntities = maxNumEntities;
		}

		public void update() {
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i) == null || entities.get(i).isDestroyed())
					entities.remove(i--);
			}
		}
	}
}
