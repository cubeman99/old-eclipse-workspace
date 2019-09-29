package projects.physicsHomework.lab;

import java.util.ArrayList;

public class Dimension {
	private String name;
	private ArrayList<Unit> units;
	private Unit siUnit;
	
	
	// ================== CONSTRUCTORS ================== //

	public Dimension(String name) {
		this.name  = name;
		this.units  = new ArrayList<Unit>();
		this.siUnit = null;
	}

	
	
	// =================== ACCESSORS =================== //

	public String getName() {
		return name;
	}
	
	public ArrayList<Unit> getUnits() {
		return units;
	}
	
	public Unit getSIUnit() {
		return siUnit;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void addUnit(Unit unit) {
		units.add(unit);
	}
	
	public void setSIUnit(Unit siUnit) {
		this.siUnit = siUnit;
	}
}
