import java.util.ArrayList;


public class Constant {
	public String name;
	public DataValue data;
	public boolean official;
	
	public static ArrayList<Constant> constants 	= new ArrayList<Constant>();
	public static ArrayList<Constant> tempConstants = new ArrayList<Constant>();
	
	public Constant() {
		this.name     = "";
		this.data     = new DataValue();
		this.official = false;
	}
	
	public Constant(String name, DataValue dv, boolean official) {
		this.name     = name;
		this.data     = new DataValue(dv);
		this.official = official;
	}
	
	public Constant(String name, Number number, boolean official) {
		this.name     = name;
		this.data     = new DataValue(number);
		this.official = official;
	}
	
	public Constant(String name, List l, boolean official) {
		this.name     = name;
		this.data     = new DataValue(l);
		this.official = official;
	}
	
	public static void configureConstants() {

		addConstant("ans", 0);
		
		addConstant("pi", 3.1415926535);
		addConstant("e", 2.7182818284);
		addConstant("true", 1);
		addConstant("false", 0);
		addConstant("i", 0, 1);
		
		
	}
	
	public static void addConstant(String name, double value) {
		constants.add(new Constant(name, new Number(value), true));
	}
	
	public static void addConstant(String name, double real, double imag) {
		constants.add(new Constant(name, new Number(real, imag), true));
	}
	
	public static int addCustomConstant(String name, DataValue dv) {
		constants.add(new Constant(name, dv, false));
		return (constants.size() - 1);
	}
	
	public static boolean isConstant(String name) {
		for (Constant c : constants) {
			if (c.name.equals(name) && c.official)
				return true;
		}
		return false;
	}
	
	public static Constant getConstant(String name) {
		for (Constant c : constants) {
			if (c.name.equals(name))
				return c;
		}
		return null;
	}

	public static boolean isAnyConstant(String name) {
		for (Constant c : constants) {
			if (c.name.equals(name))
				return true;
		}
		return false;
	}
	
	public static boolean isPossibleVariable(String varName) {
		if (Main.tokenType(varName, true) == Main.TOKEN_TYPE_NUMBER)
			return false;
		return (!isConstant(varName) && !Function.isFunction(varName));
	}
	
	public static int setVariable(String varName, DataValue dv) {
		for (int i = 0; i < constants.size(); i++) {
			Constant c = constants.get(i);
			if (c.name.equals(varName)) {
				c.data = dv;
				if (Main.debugShowDefinitions) {
					dv.print();
					System.out.println(" stored in \"" + varName + "\"");
				}
				return i;
			}
		}
		// Variable not initialized yet, so Declare it here!
		int ind = addCustomConstant(varName, dv);
		if (Main.debugShowDefinitions) {
			dv.print();
			System.out.println(" stored in \"" + varName + "\" (initialized)");
		}
		return ind;
	}
}
