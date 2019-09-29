package projects.physicsHomework.lab;

public class Measure {
	private String name;
	private double value;
	private double error;
	private Units units;
	
	

	// ================== CONSTRUCTORS ================== //

	public Measure(String name, double value, double error, Unit unit) {
		this(name, value, error, new Units(unit));
	}
	
	public Measure(String name, double value, double error, Units units) {
		this.name  = name;
		this.value = value;
		this.error = error;
		this.units = units;
	}
	
	
	
	// =================== ACCESSORS =================== //

	public String getName() {
		return name;
	}
	
	public double getValue() {
		return value;
	}
	
	public double getError() {
		return error;
	}
	
	public Units getUnits() {
		return units;
	}
	
	public String getString(boolean includeError) {
		String str = value + "";
		String u = units + "";
		char plusOrMinus = (char) 177;//(char) 242; // +- symbol
		if (includeError && error != 0)
			str += " " + plusOrMinus + " " + error;
		if (u.length() > 0)
			str += " " + u;
		return str;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public void setError(double error) {
		this.error = error;
	}
	
	public void setUnits(Units units) {
		this.units = units;
	}
	
	
	
	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public String toString() {
		return getString(true);
	}
}
