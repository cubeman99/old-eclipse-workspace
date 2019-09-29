package projects.physicsHomework.lab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Units {

	// ===================== STATIC ===================== //

	// Dimensions:
	public static final Dimension LENGTH = new Dimension("length");
	public static final Dimension TIME   = new Dimension("time");
	public static final Dimension MASS   = new Dimension("mass");
	public static final Dimension[] DIMENSIONS = {LENGTH, TIME, MASS};
	public static final int NUM_DIMENSIONS = 3;
	
	// Length.
	public static final Unit KILOMETERS  = new Unit(LENGTH, "kilometers",  "km", 1000);
	public static final Unit METERS      = new Unit(LENGTH, "meters",      "m",  1);
	public static final Unit CENTIMETERS = new Unit(LENGTH, "centimeters", "cm", 0.01);
	public static final Unit MILLIMETERS = new Unit(LENGTH, "millimeters", "mm", 0.001);

	// Time.
	public static final Unit HOURS        = new Unit(TIME, "hours",        "h",   3600);
	public static final Unit MINUTES      = new Unit(TIME, "minutes",      "min", 60);
	public static final Unit SECONDS      = new Unit(TIME, "seconds",      "s",   1);
	public static final Unit MILLISECONDS = new Unit(TIME, "milliseconds", "ms",  0.001);
	
	// Mass
	public static final Unit KILOGRAMS  = new Unit(MASS, "kilograms",   "kg", 1000);
	public static final Unit GRAMS      = new Unit(MASS, "grams",       "g",  1);
	public static final Unit MILLIGRAMS = new Unit(MASS,  "milligrams", "mg", 0.001);
	
	// SI Units.
	static {
		LENGTH.setSIUnit(METERS);
		TIME.setSIUnit(SECONDS);
		MASS.setSIUnit(KILOGRAMS);
	}
	
	public static Unit getUnit(String symbolOrName) {
		for (int i = 0; i < DIMENSIONS.length; i++) {
			ArrayList<Unit> units = DIMENSIONS[i].getUnits();
			for (int j = 0; j < units.size(); j++) {
				Unit u = units.get(j);
				if (u.getSymbol().equals(symbolOrName) ||
					u.getName().equalsIgnoreCase(symbolOrName))
				{
					return u;
				}
			}
		}
		return null;
	}
	
	public static Units parseUnits(String s) {
		if (s.length() == 0)
			return new Units();
		
		Unit u = getUnit(s);
		if (u != null)
			return new Units(u);
		
		return new Units();
	}
	
	
	

	// =================== MEMBER DATA ================== //
	
	static class UnitDimension {
		public Unit unit;
		public int power;
	}
	
	private HashMap<Dimension, UnitDimension> units;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Units() {
		units = new HashMap<Dimension, UnitDimension>();
	}

	public Units(Unit unit) {
		this(unit, 1);
	}
	
	public Units(Unit unit, int power) {
		this();
		addDimension(unit, power);
	}
	
	public Units(String unitsData) {
		this();
		Scanner scanner = new Scanner(unitsData);
		while (scanner.hasNext()) {
			String unitSymbol = scanner.next();
			int unitPower = scanner.nextInt();
			addDimension(getUnit(unitSymbol), unitPower);
		}
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public int getDimensionCount() {
		return units.size();
	}
	
	public Unit getDimensionUnit(Dimension dim) {
		if (units.containsKey(dim))
			return units.get(dim).unit;
		return null;
	}
	
	public int getDimensionPower(Dimension dim) {
		if (units.containsKey(dim))
			return units.get(dim).power;
		return 0;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void addDimension(Unit unit) {
		addDimension(unit, 1);
	}
	
	public void addDimension(Unit unit, int power) {
		UnitDimension dim = new UnitDimension();
		dim.unit  = unit;
		dim.power = power;
		units.put(dim.unit.getDimension(), dim);
	}
	
	

	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < NUM_DIMENSIONS; i++) {
			if (units.containsKey(DIMENSIONS[i])) {
				UnitDimension dim = units.get(DIMENSIONS[i]);
				if (str.length() > 0)
					str += " ";
				str += dim.unit.getSymbol();
				if (dim.power != 1 && dim.power != 0)
					str += "^" + dim.power;
			}
		}
		return str;
	}
}
