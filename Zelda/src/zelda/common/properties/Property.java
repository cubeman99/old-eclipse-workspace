package zelda.common.properties;

import java.io.Serializable;


/**
 * Property.
 * 
 * @author David Jordan
 */
public class Property implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String value;



	// ================== CONSTRUCTORS ================== //

	public Property(String name) {
		this(name, "");
	}

	public Property(Property copy) {
		this(copy.name, copy.value);
	}

	public Property(String name, Object value) {
		this.name  = name;
		this.value = value.toString();
	}



	// =================== ACCESSORS =================== //

	public String getName() {
		return name;
	}

	public boolean hasName(String name) {
		return this.name.equals(name);
	}
	
	public String getDescription() {
		return "";
	}
	
	public boolean isNull() {
		return value.isEmpty();
	}
	
	public String get() {
		return value;
	}
	
	public String getString() {
		return value;
	}

	public boolean getBoolean() {
		return Boolean.parseBoolean(value);
	}

	public int getInt() {
		return Integer.parseInt(value);
	}

	public double getDouble() {
		return Double.parseDouble(value);
	}



	// ==================== MUTATORS ==================== //
	
	public Property setName(String name) {
		this.name = name;
		return this;
	}
	
	public Property set(Object value) {
		this.value = value.toString();
		return this;
	}
	


	// ================ IMPLEMENTATIONS ================ //
	
	@Override
	public String toString() {
		return (name + " = " + value);
	}
}
