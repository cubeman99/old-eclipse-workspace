
public class Token implements Cloneable {
	public String string;
	public DataValue data;
	
	public Operator operator;
	public int openPar;
	public int closedPar;
	
	public String[] func;
	
	
	public Token(String s, DataValue data, String operatorSymbol, int openPar, int closedPar, String[] func) {
		this.string		= s;
		this.data		= data;
		this.operator	= Operator.getOperator(operatorSymbol);
		this.openPar	= openPar;
		this.closedPar	= closedPar;
		this.func = new String[100];
		for (int i = 0; i < 100; i++)
			this.func[i] = func[i];
	}
	
	public Token(Token T) {
		this.string		= T.string;
		this.data		= new DataValue(T.data);
		this.operator	= new Operator(T.operator);
		this.openPar	= T.openPar;
		this.closedPar	= T.closedPar;
		this.func 		= T.func.clone();
	}
	
	public void operate(Token T) {
		if (operator.parentSymbol.equals("=")) {
			// VARIABLE DEFINITION
			if (Constant.isPossibleVariable(string)) {
				Constant.setVariable(string, T.data);
				data.set(T.data);
				return;
			}
			else
				Main.printError("Unable to declare \"" + Main.declarationVariable + "\"");
			if (Function.isFunction(Main.declarationVariable))
				Main.printError("Conflicting names");
			else if (Constant.isConstant(Main.declarationVariable))
				Main.printError("Cannot re-define \"" + Main.declarationVariable + "\"");
			return;
		}

		if (Main.debugShowOperations) {
			data.print();
			System.out.print(" " + operator.symbol + " ");
			T.data.print();
			System.out.print(" = ");
		}
		
		data.operate(T.data, operator);
		
		if (Main.debugShowOperations) {
			data.print();
			System.out.println();
		}
	}
	
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
    }
	
	public String getPrintString() {
		String str = "";
		for (int i = 0; i < openPar; i++) {
			str += func[i];
			str += "(";
		}
		
		if (operator.symbol.equals("="))
			str += string;
		else
			str += data.getPrintString();
		
		for (int i = 0; i < closedPar; i++)
			str += ")";
		
		if (operator.symbol != "," && operator.symbol != "")
			str += " ";
		if (operator.symbol != "")
			str += operator.symbol + " ";
		
		return str;
	}

	public void print() {
		System.out.print(getPrintString());
	}
}
