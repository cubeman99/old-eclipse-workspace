import java.util.ArrayList;


public class Operator {
	public String symbol;
	public String parentSymbol;
	public int priority;
	
	public static final int COMMA_PRIORITY = 4;
	
	public static ArrayList<Operator> operatorList = new ArrayList<Operator>();
	
	public Operator() {
		this.symbol       = "";
		this.priority     = 9999;
		this.parentSymbol = "";
	}
	
	public Operator(Operator O) {
		this.symbol       = O.symbol;
		this.priority     = O.priority;
		this.parentSymbol = O.parentSymbol;
	}
	
	public Operator(String symbol, int priority) {
		this.symbol       = symbol;
		this.priority     = priority;
		this.parentSymbol = symbol;
	}
	
	public Operator(String symbol, int priority, String parentSymbol) {
		this.symbol       = symbol;
		this.priority     = priority;
		this.parentSymbol = parentSymbol;
	}
	
	public static void configureOperators() {

		addOperator("=", 1);  // Special Variable Declaration Operator
		
		addOperator(",", COMMA_PRIORITY); // Special Function Argument Separator
		
		addOperator("&&", 5);
		addOperator("||", 5);
		
		addOperator("<",  6);
		addOperator(">",  6);
		addOperator("<=", 6);
		addOperator(">=", 6);
		addOperator("==", 6);
		addOperator("!=", 6);
		
		addOperator("+", 10);
		addOperator("-", 10);
		addOperator("*", 11);
		addOperator("/", 11);
		addOperator("%", 12);
		addOperator("^", 13);
		addOperator("E", 14); // x * 10^y
	}
	
	private static void addOperator(String symbol, int priority) {
		operatorList.add(new Operator(symbol, priority));
	}
	
	private static void addOperator(String symbol, int priority, String parentSymbol) {
		operatorList.add(new Operator(symbol, priority, parentSymbol));
	}
	
	public Number perform(Number x, Number y) {
		
		if (parentSymbol == "+")
			return Number.add(x, y);
		if (parentSymbol == "-")
			return Number.subtract(x, y);
		if (parentSymbol == "*")
			return Number.multiply(x, y);
		if (parentSymbol == "/") {
			if (y.real == 0) {
				Main.printError("Divide by Zero");
				return new Number();
			}
			return Number.divide(x, y);
		}
		if (parentSymbol == "^")
			return Number.power(x, y);
		if (parentSymbol == "==")
			return Number.equal(x, y);
		if (parentSymbol == "!=")
			return Number.unequal(x, y);
		
		// REAL ONLY:
		if (parentSymbol == "%")
			return Number.modulus(x, y);
		if (parentSymbol == "&&")
			return Number.and(x, y);
		if (parentSymbol == "||")
			return Number.or(x, y);
		if (parentSymbol == ">")
			return Number.greaterThan(x, y, false);
		if (parentSymbol == ">=")
			return Number.greaterThan(x, y, true);
		if (parentSymbol == "<")
			return Number.lessThan(x, y, false);
		if (parentSymbol == "<=")
			return Number.lessThan(x, y, true);
		
		
		Main.printError("Unknown Operator \"" + symbol + "\"");
		return new Number();
	}
	
	public static double dBool(boolean b) {
		if (b)
			return 1;
		return 0;
	}

	public static int getPriority(String s) {
		for (Operator op : operatorList) {
			if (op.symbol.equals(s))
				return op.priority;
		}
		return 0;
	}
	
	public static Operator getOperator(String symbol) {
		if (symbol == "")
			return new Operator();
		for (Operator op : operatorList) {
			if (op.symbol.equals(symbol))
				return op;
		}
		Main.printError("Unknown Operator \"" + symbol + "\"");
		return new Operator();
	}

	public static boolean isOperator(char c) {
		for (Operator op : operatorList) {
			for (int i = 0; i < op.symbol.length(); i++) {
				if (c == op.symbol.charAt(i))
					return true;
			}
		}
		return false;
	}
	
	public static boolean isFullOperator(String s) {
		for (Operator op : operatorList) {
			if (op.symbol.equals(s))
				return true;
		}
		return false;
	}
}
