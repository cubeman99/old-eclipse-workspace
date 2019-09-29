package simulator.datatypes;

public class TypeBoolean extends DataType {
	public boolean value;

	public TypeBoolean() {
		this(false);
	}
	
	public TypeBoolean(boolean value) {
		super(Type.BOOLEAN);
		this.value = value;
	}
	
	public void setBoolean(boolean value) {
		this.value = value;
	}
	
	public boolean getBoolean() {
		return value;
	}
	
//	public boolean canCastTo(DataType t) {
//		return (t instanceof TypeInteger || t instanceof TypeDouble);
//	}
}
