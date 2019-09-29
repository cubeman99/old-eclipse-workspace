package zelda.common.drops;

import zelda.common.graphics.Sprite;
import zelda.common.util.Currency;
import zelda.game.entity.EntityObject;
import zelda.game.entity.collectable.Collectable;
import zelda.main.Sound;


/**
 * Drop.
 * 
 * @author David Jordan
 */
public class Drop implements DropCreator {
	private int odds;
	private EntityObject object;


	// ================== CONSTRUCTORS ================== //

	public Drop(int amount, Currency curr, Sprite sprCollectable) {
		this(1, amount, curr, sprCollectable, null);
	}
	
	public Drop(int amount, Currency curr, Sprite sprCollectable, Sound sound) {
		this(1, amount, curr, sprCollectable, sound);
	}

	public Drop(int odds, int amount, Currency curr, Sprite sprCollectable, Sound sound) {
		this(odds, new Collectable(amount, curr, sprCollectable, sound));
	}

	public Drop(EntityObject obj) {
		this(1, obj);
	}

	public Drop(int odds, EntityObject obj) {
		this.odds = odds;
		this.object = obj;
	}

	public Drop(int newOdds, Drop copy) {
		this.odds = newOdds;
		this.object = copy.object;
	}



	// =================== ACCESSORS =================== //

	public int getOdds() {
		return odds;
	}

	public EntityObject createNewDropObject() {
		return object.clone();
	}



	// ==================== MUTATORS ==================== //


}
