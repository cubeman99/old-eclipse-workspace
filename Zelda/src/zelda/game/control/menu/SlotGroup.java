package zelda.game.control.menu;

import java.util.ArrayList;
import zelda.common.geometry.Point;


public class SlotGroup implements SlotConnection {
	private ArrayList<Slot> slots;
	private Slot currentSlot;

	public SlotGroup() {
		slots = new ArrayList<Slot>();
		currentSlot = null;
	}

	public int getNumSlots() {
		return slots.size();
	}

	public Slot getSlot(int index) {
		return slots.get(index);
	}

	public Slot getCurrentSlot() {
		return currentSlot;
	}

	public void setCurrentSlot(Slot currentSlot) {
		this.currentSlot = currentSlot;
	}

	public Slot addSlot(int x, int y, Point size) {
		Slot s = new Slot(this, x, y, size);
		slots.add(s);
		if (slots.size() == 1)
			currentSlot = s;
		return s;
	}

	public void draw() {
		for (int i = 0; i < slots.size(); i++)
			slots.get(i).draw();
	}
}
