package control;

import java.awt.Color;
import java.util.ArrayList;
import common.Settings;
import common.Vector;
import common.shape.Rectangle;
import entity.Entity;
import entity.unit.Unit;

public class Team extends Entity {
	public Control control;
	public String name;
	public Color color;
	private ArrayList<Unit> units;
	private ArrayList<Unit> deadUnits;
	
	public Rectangle spawnZone;
	
	
	
	public Team(Control control, String name, Color color) {
		super(Settings.DEPTH_UNIT);
		this.control    = control;
		this.name       = name;
		this.units      = new ArrayList<Unit>();
		this.deadUnits  = new ArrayList<Unit>();
		this.color      = color;
	}
	
	public void addUnit(Unit u) {
		units.add(u);
	}
	
	/** Return the list of alive units on this team. **/
	public ArrayList<Unit> getAliveUnits() {
		return units;
	}
	
	@Override
	public void update() {
		// Update all alive units:
		for (int i = 0; i < units.size(); i++) {
			Unit u = units.get(i);
			u.update();
			if (u.isDestroyed()) {
				u.respawnTimer.start();
				deadUnits.add(u);
				units.remove(i);
				i--;
			}
		}

		// Update all dead units:
		for (int i = 0; i < deadUnits.size(); i++) {
			Unit u = deadUnits.get(i);
			if (u.respawnTimer.isExpired()) {
				u.respawn(new Vector(2, 2));
				units.add(u);
				deadUnits.remove(i);
				i--;
			}
		}
	}

	@Override
	public void draw() {
		// Draw all alive units:
		for (int i = 0; i < units.size(); i++) {
			units.get(i).draw();
		}
	}
}
