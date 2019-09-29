package zelda.game.player;

import java.util.ArrayList;
import zelda.common.Resources;
import zelda.common.Sounds;
import zelda.common.drops.Drop;
import zelda.common.drops.DropList;
import zelda.common.graphics.Sprite;
import zelda.common.util.Currency;
import zelda.game.entity.collectable.CollectableFairy;
import zelda.game.monster.MonsterBeetle;
import zelda.game.monster.walkMonster.chargeMonster.MonsterRope;


/**
 * Purse.
 * 
 * @author David Jordan
 */
public class Purse {
	private Player player;
	private ArrayList<Currency> currencies;

	public DropList dropsDig;
	public DropList dropsDefault;

	public DropList dropsAll;
	public DropList dropsRupees;
	public DropList dropsAllRupees;
	public DropList dropsHearts;
	public DropList dropsSeeds;
	public DropList dropsAmmo;
	public DropList dropsMonsters;

	public Drop dropDebug;
	public Drop dropArrows;
	public Drop dropBombs;
	public Drop[] dropSeeds;
	public Drop dropRupee1;
	public Drop dropRupee5;
	public Drop dropRupee10;
	public Drop dropRupee100;
	public Drop dropHeart;
	public Drop dropFairy;
	public Drop dropBeetle;
	public Drop dropRope;



	// ================== CONSTRUCTORS ================== //

	public Purse(Player player) {
		this.player = player;
		currencies = new ArrayList<Currency>();
	}

	public Purse(Player player, Currency... cs) {
		this.player = player;

		currencies = new ArrayList<Currency>();
		for (int i = 0; i < cs.length; i++)
			currencies.add(cs[i]);
	}



	// =================== ACCESSORS =================== //

	public Currency getCurrency(int index) {
		return currencies.get(index);
	}

	public int getNumCurrencies() {
		return currencies.size();
	}

	public ArrayList<Currency> getCurrencies() {
		return currencies;
	}



	// ==================== MUTATORS ==================== //

	public void initialize() {

		// SINGULAR DROPS:
		dropDebug = new Drop(1, null, new Sprite(Resources.SHEET_ICONS_SMALL,
				4, 1).setOrigin(4, 6));
		dropsSeeds = new DropList();
		dropSeeds = new Drop[5];
		for (int i = 0; i < 5; i++) {
			dropSeeds[i] = new Drop(5, player.currencySeeds[i],
					Resources.SPRITE_COLLECTABLE_SEEDS[i], Sounds.COLLECT_ITEM);
			dropsSeeds.addDrop(1, dropSeeds[i]);
		}
		dropArrows = new Drop(5, player.currencyArrows,
				Resources.SPRITE_COLLECTABLE_ARROWS, Sounds.COLLECT_ITEM);
		dropBombs = new Drop(4, player.currencyBombs,
				Resources.SPRITE_COLLECTABLE_BOMBS, Sounds.COLLECT_ITEM);
		dropRupee1 = new Drop(1, player.currencyRupees,
				Resources.SPRITE_COLLECTABLE_RUPEE_1, Sounds.COLLECT_RUPEE);
		dropRupee5 = new Drop(5, player.currencyRupees,
				Resources.SPRITE_COLLECTABLE_RUPEE_5, Sounds.COLLECT_RUPEE_5);
		dropRupee10 = new Drop(10, player.currencyRupees,
				Resources.SPRITE_COLLECTABLE_RUPEE_10, Sounds.COLLECT_RUPEE_5);
		dropRupee100 = new Drop(100, player.currencyRupees,
				Resources.SPRITE_COLLECTABLE_RUPEE_100, Sounds.COLLECT_RUPEE_5);
		dropHeart = new Drop(4, player.currencyHearts,
				Resources.SPRITE_COLLECTABLE_HEART, Sounds.COLLECT_HEART);
		dropFairy = new Drop(new CollectableFairy(player));
		dropBeetle = new Drop(new MonsterBeetle());
		dropRope = new Drop(new MonsterRope());


		dropsAll = new DropList(1);
		dropsAll.addDrop(1, dropsSeeds);
		dropsAll.addDrop(1, dropArrows);
		dropsAll.addDrop(1, dropBombs);
		dropsAll.addDrop(1, dropRupee1);
		dropsAll.addDrop(1, dropRupee5);
		dropsAll.addDrop(1, dropRupee10);
		dropsAll.addDrop(1, dropRupee100);
		dropsAll.addDrop(1, dropHeart);
		dropsAll.addDrop(1, dropFairy);
		dropsAll.addDrop(1, dropBeetle);
		dropsAll.addDrop(1, dropRope);



		// DROP LISTS:
		dropsRupees = new DropList();
		dropsRupees.addDrop(30, dropRupee1);
		dropsRupees.addDrop(10, dropRupee5);
		dropsRupees.addDrop(5, dropRupee10);

		dropsAllRupees = new DropList();
		dropsAllRupees.addDrop(25, dropsRupees);
		dropsAllRupees.addDrop(1, dropRupee100);

		dropsHearts = new DropList();
		dropsHearts.addDrop(20, dropHeart);
		dropsHearts.addDrop(5, dropFairy);

		dropsAmmo = new DropList(1);
		dropsAmmo.addDrop(1, dropArrows);
		dropsAmmo.addDrop(1, dropBombs);
		for (int i = 0; i < 5; i++)
			dropsAmmo.addDrop(1, dropSeeds[i]);

		dropsMonsters = new DropList();
		dropsMonsters.addDrop(5, dropBeetle);
		dropsMonsters.addDrop(2, dropRope);



		// DIGGING DROPS:
		dropsDig = new DropList(4);
		dropsDig.addDrop(46, dropsAllRupees);
		dropsDig.addDrop(25, dropsHearts);
		dropsDig.addDrop(7, dropsMonsters);

		// DEFAULT DROPS:
		dropsDefault = new DropList(3);
		dropsDefault.addDrop(50, dropsAmmo);
		dropsDefault.addDrop(46, dropsRupees);
		dropsDefault.addDrop(25, dropsHearts);
	}

	public Purse addCurrency(Currency curr) {
		currencies.add(curr);
		return this;
	}
}
