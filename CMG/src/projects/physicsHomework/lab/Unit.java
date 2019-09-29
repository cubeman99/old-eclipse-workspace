package projects.physicsHomework.lab;



public class Unit {
	private String symbol;
	private String name;
	private double conversionFactor;
	private Dimension dimension;
	

	
	// ================== CONSTRUCTORS ================== //

	public Unit(Dimension dimension, String name, String symbol, double conversionFactor) {
		this.dimension = dimension;
		this.name      = name;
		this.symbol    = symbol;
		this.conversionFactor = conversionFactor;
		dimension.addUnit(this);
	}
	
	
	
	// =================== ACCESSORS =================== //

	public String getName() {
		return name;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public double getConversionFactor() {
		return conversionFactor;
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
}
