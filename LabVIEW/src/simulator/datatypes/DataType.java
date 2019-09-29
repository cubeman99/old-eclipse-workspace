package simulator.datatypes;

public class DataType {
	public static final int BOOLEAN = 0;
	public static final int BYTE    = 1; // Casts to Integer, Double
	public static final int INTEGER = 2; // Casts to Double
	public static final int DOUBLE  = 3;
	public static final int STRING  = 4;
	
	
	public Type type;
	
	public DataType(Type t) {
		this.type = t;
	}
	
	public boolean getBoolean()	{return false;}
	public byte getByte()		{return 0;}
	public int getInteger()		{return 0;}
	public double getDouble()	{return 0;}
	public String getString()	{return "";}
	
	public void setBoolean(boolean value) {}
	public void setByte(byte value) {}
	public void setInteger(int value) {}
	public void setDouble(double value) {}
	public void setString(String value) {}
	
	public boolean canCastTo(DataType t) {return false;}
}


/* 
	WHEN ADDING A NEW DATA TYPE:
	Places to modify:
	 - DataType
	   - do-nothing: getT()
	   - do-nothing: setT(T value)
	 - Type
	   - T()
	 - IOPoint
	   - Cunstructer
	 - Gate
	   - getT(int index)
	   - setT(int index, T value)
	 - TypeT [a new class for the type]
	 
*/