package zelda.common.drops;

import java.util.ArrayList;
import zelda.common.util.GMath;
import zelda.game.entity.EntityObject;


/**
 * DropList.
 * 
 * @author David Jordan
 */
public class DropList implements DropCreator {
	private ArrayList<DropCreator> drops;
	private int odds;


	// ================== CONSTRUCTORS ================== //

	public DropList() {
		this(1);
	}

	public DropList(int odds) {
		this.odds = odds;
		drops = new ArrayList<DropCreator>();
	}

	public DropList(int newOdds, DropList copy) {
		this.odds = newOdds;
		drops = new ArrayList<DropCreator>();
		for (int i = 0; i < copy.drops.size(); i++)
			drops.add(copy.drops.get(i));
	}



	// =================== ACCESSORS =================== //

	@Override
	public int getOdds() {
		return odds;
	}

	public EntityObject createDropObject() {
		if (GMath.random.nextInt(odds) == 0) {
			return createNewDropObject();
		}
		return null;
	}

	@Override
	public EntityObject createNewDropObject() {
		int n = GMath.random.nextInt(getTotalOdds());
		int total = 0;
		for (int i = 0; i < drops.size(); i++) {
			int dropOdds = drops.get(i).getOdds();
			if (n >= total && n < total + dropOdds)
				return drops.get(i).createNewDropObject();
			total += dropOdds;
		}
		return null;
	}

	public ArrayList<DropCreator> getDrops() {
		return drops;
	}

	private int getTotalOdds() {
		int total = 0;
		for (int i = 0; i < drops.size(); i++)
			total += drops.get(i).getOdds();
		return total;
	}



	// ==================== MUTATORS ==================== //

	public Drop addDrop(int odds, Drop drop) {
		Drop x = new Drop(odds, drop);
		drops.add(x);
		return x;
	}

	public DropList addDrop(int odds, DropList dropList) {
		DropList x = new DropList(odds, dropList);
		drops.add(x);
		return x;
	}
}
