import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Main {
	public static final int DECIMAL_PLACE_MAX   = 9;
	public static final int TOKEN_TYPE_NUMBER   = 1;
	public static final int TOKEN_TYPE_CONSTANT = 2;
	
	public static ArrayList<Token> tokens = new ArrayList<Token>();
	public static String declarationVariable = "";
	public static boolean foundError = false;
	public static Random random = new Random();
	public static int currentTokenIndex = 0;
	public static boolean modeRadians = true;
	
	public static boolean debugShowOperations  = false;
	public static boolean debugShowFunctions   = false;
	public static boolean debugShowDefinitions = false;
	
	
	public Main() {
		ArrayList<Character> charList = new ArrayList<Character>();
		Scanner scanner = new Scanner(System.in);
		Constant.configureConstants();
		Operator.configureOperators();
		Function.configureFunctions();
		
		DataValue answer = new DataValue();
		String inputPrev = "";
		
		System.out.println("////////////////////////////");
		System.out.println("//   CONSOLE CALCULATOR   //");
		System.out.println("//     <David Jordan>     //");
		System.out.println("////////////////////////////");
		System.out.println();
		
		////////////////////
		//   MAIN LOOP:   //
		////////////////////
		while (true) {
			foundError = false;
			String input = scanner.nextLine();
			if (input.equals(""))
				input = inputPrev;
			if (input.equals("end"))
				break;

			// Check if it is a command First
			if (parseCommands(input))
				continue;
			
			// PARSE THE INPUT
			Expression expr = tokenizeString(input);
			if (foundError)
				continue;
			// Print the Tokenized String
			expr.print();
			System.out.println();
			
			// FILL VARIABLES
			expr.fillVariables();
			
			// SOLVE THE EXPRESSION
			answer = solveExpression(expr);
			if (foundError)
				continue;
			// Print the Answer
			answer.print();
			System.out.println();
			System.out.println();
			
			Constant.constants.get(0).data = answer;
			inputPrev = input;
		}
		
		System.out.println("Process Terminated.");
	}

	////////////////////////////////////////////////////////
	// PARSE A STRING INTO AN EXPRESSION (ARRAY OF TOKENS //
	////////////////////////////////////////////////////////
	public static Expression tokenizeString(String expString) {
		ArrayList<Character> charList = new ArrayList<Character>();
		
		// Clear blank spaces while adding to a character array
		boolean inString = false;
		for (int i = 0; i < expString.length(); i++) {
			if (expString.charAt(i) == '"') {
				inString = !inString;
			}
			if (expString.charAt(i) != ' ' || inString)
				charList.add(expString.charAt(i));
		}
		return tokenizeString(charList, 0);
	}
	public static Expression tokenizeString(ArrayList<Character> charList, int startingPoint) {
		ArrayList<Token> tokenList = new ArrayList<Token>();
		
		// Distribute Tokens into an array
		String str     		= "";
		String oper    		= "";
		int openPara   		= 0;
		int closedPara 		= 0;
		int netPara    	 	= 0;
		boolean stringBuild = false;
		boolean isString    = false;
		boolean done		= false;
		Expression exp 		= null;
		String[] funcArray  = new String[100];
		for (int i = 0; i < 100; i++)
			funcArray[i] = "";
		
		for (int i = startingPoint; i < charList.size(); i++) {
			char c = charList.get(i);
			boolean parse = false;
			
			if (!stringBuild && c == '"') {
				if (isString)
					parse = true;
				else {
					stringBuild = true;
					isString    = true;
					continue;
				}
			}
			else if (stringBuild) {
				if (c == '"') {
					stringBuild = false;
					if (i == charList.size() - 1)
						parse = true;
					else
						continue;
				}
				else {
					str += c;
					continue;
				}
			}
			
			if (Operator.isOperator(c) && c != '-' && c != '!' && i == 0)
				str = "ans";
			
			if (c == '}' || c == ']')
				c = ')';
			
			if (c == '(' && str == "")
				openPara += 1;
			else if (c == ')') {
				closedPara += 1;
//				if (openPara - closedPara < 0) {
//					parse = true;
//					done  = true;
////					closedPara -= 1;
//				}
			}
			
			if (Operator.isOperator(c)) {
				if ((c == '-' || c == '!') && oper == "" && str == "") {
					if (c == '-') 
						funcArray[openPara] = "-"; // Negative sign (Used as a function)
					else if (c == '!')
						funcArray[openPara] = "!"; // Exclamation Mark (Used as a function)
					if (i < charList.size() - 1) {
						if (charList.get(i + 1) == '(')
							continue;
					}
					openPara   += 1;
					closedPara += 1;
				}
				else 
					oper += c;
			}
			
			if (c == '(' && str != "") {
				parse = true;
			}
			if (i == charList.size() - 1)
				parse = true;
			else if (oper != "" && i < charList.size() - 1) {
				char cc = charList.get(i + 1);
				if (!Operator.isOperator(cc) || cc == '-' || cc == '!' || cc == '"')
					parse = true;
			}
			
			if (c == '(' && str != "" && Function.isFunction(str)) {
				parse = false;
				funcArray[openPara] = str;
				openPara += 1;
				Function F = Function.getFunction(str);
				if (F.dataTypes[0].isExpression() && i < charList.size() - 1) {
					// Parse an Expression
					exp = new Expression(tokenizeString(charList, i + 1));
					// Find the pickup point
					int par = 0;
					for (int j = i + 1; j < charList.size(); j++) {
						char cc = charList.get(j);
						if (cc == '(')
							par += 1;
						if (cc == ')')
							par -= 1;
						if (cc == ',' && par == 0) {
							i = j - 1;
							str = "TEMP";
							break;
						}
					}
					continue;
				}
				else
					str = "";
				
			}
			if (c == '{') {
				parse = false;
				funcArray[openPara] = "list_init";
				str = "";
				openPara += 1;
			}
			if (c == '[') {
				parse = true;
				funcArray[openPara] = "list_get";
				openPara += 1;
				oper = ",";
			}
			
			if (parse) {
				int newOpenPara = 0;
				if (!Operator.isFullOperator(oper) && c != ')' && c != '"' && i == charList.size() - 1) {
					str += c;
					c    = ' ';
					oper = "";
				}
				if (c == '(' && str != "") { // 2(4 + 1) ---> 2 * (4 + 1)
					Constant cnst = Constant.getConstant(str);
					if (cnst == null)
						cnst = new Constant();
					if (cnst.data.isExpression()) {
						funcArray[openPara] = "func_get";
						openPara += 1;
						oper = ",";
					}
					else {
						oper = "*";
						newOpenPara = 1;
					}
				}
				if (oper.equals(",") && (netPara + openPara - closedPara) == 0) {
					oper = "";
					done = true;
				}
				else if (oper.equals(",")) {
				}
				DataValue DV = new DataValue();
				if (exp != null)
					DV.set(exp);
				else if (isString) {
					DV.set(str);
					isString    = false;
				}
				else {
					int type = tokenType(str, oper.equals("=") && tokenList.size() == 0);
					if (type == 1)
						DV.set(readNumber(str));
					else if (type == 2)
						DV.set(readConstant(str));
				}
				if (oper.equals("=") && tokenList.size() > 0)
					printError("Unexpected token \"=\"");
				
				netPara += openPara - closedPara;
				Token T = new Token(str, DV, oper, openPara, closedPara, funcArray);
				tokenList.add(T);
//				T.print();
				
				openPara   = newOpenPara;
				closedPara = 0;
				str  = ""; oper = "";
				exp = null;
				for (int j = 0; j < 100; j++)
					funcArray[j] = "";
				if (done)
					break;
			}
			else if (c != '(' && c != ')' && c != '{' && c != '}' && !Operator.isOperator(c)) {
				str += c;
				if (closedPara > 0)
					Main.printTokenError(str);
			}
		}
//		System.out.println();
		if (netPara > 0)
			printError("\")\" Expected");
		else if (netPara < 0)
			printError("\"(\" Expected");
			
		if (tokenList.get(tokenList.size() - 1).operator.symbol != "") {
			printError("Unexpected Terminal Operator");
			tokenList.get(tokenList.size() - 1).operator = new Operator();
		}
		
		return new Expression(tokenList);
	}
	
	

	/////////////////////////
	// SOLVE AN EXPRESSION //
	/////////////////////////
	public static DataValue solveExpression(Expression expression) {
		return solveExpression(expression, "", new DataValue());
	}
	public static DataValue solveExpression(Expression expression, String varName, DataValue varData) {
		ArrayList<Token> tokenList = (ArrayList<Token>) expression.getArrayList().clone();
		if (foundError)
			return new DataValue();
		while (tokenList.size() > 1 || tokenList.get(0).openPar > 0 || tokenList.get(0).closedPar > 0) {
			// (1) Find a Parentheses group and conquer it
			Point paras   = getParenthesesBlock(tokenList);
			int paraStart = paras.x;
			int paraEnd   = paras.y;
			
			// (2) Find operators with highest priority
			int highest = getHighestOperatorPriority(tokenList, paraStart, paraEnd);
			
			// (3) Perform those operations while shrinking the Token array
			if (paraStart == paraEnd) {
				Token T      = tokenList.get(paraStart);
				String funct = T.func[T.openPar - 1];
				if (funct != "")
					performFunction(tokenList, paraStart, paraEnd);
				else {
					T.openPar   -= 1;
					T.closedPar -= 1;
				}
			}
			else if (highest > 0) {
				if (highest == Operator.COMMA_PRIORITY)
					performFunction(tokenList, paraStart, paraEnd);
				else {
					for (int i = paraStart; i < Math.min(paraEnd, tokenList.size()); i++) {
						Token T = tokenList.get(i);
						if (T.operator.priority == highest) {
							T.operate(tokenList.get(i + 1));
//							T.data.operate(tokenList.get(i + 1).data, T.operator);
							T.operator = tokenList.get(i + 1).operator;
							int minPara = Math.min(tokenList.get(i).openPar, tokenList.get(i + 1).closedPar);
							tokenList.get(i).openPar -= minPara;
							tokenList.get(i).closedPar = tokenList.get(i + 1).closedPar - minPara;

							if (paraEnd == paraStart + 1) {
								tokenList.get(i).openPar += 1;
								tokenList.get(i).closedPar += 1;
							}
							
							tokenList.remove(i + 1);
							i -= 1; paraEnd -= 1;
						}
					}
				}
			}
			if (foundError)
				break;
		}
		if (foundError)
			return new DataValue();
		return tokenList.get(0).data;
	}
	
	public static Token getCurrentToken() {
		return tokens.get(currentTokenIndex);
	}
	
	public static int getHighestOperatorPriority(ArrayList<Token> tokenList, int start, int end) {
		int highest = -1;
		for (int i = start; i < Math.min(end, tokenList.size()); i++) {
			Token T = tokenList.get(i);
			if (T.operator.priority > highest)
				highest = T.operator.priority;
		}
		return highest;
	}
	
	public static Point getParenthesesBlock(ArrayList<Token> tokenList) {
		Point P = new Point(0, tokenList.size() - 1);
		for (int i = 0; i < tokenList.size(); i++) {
			Token T = tokenList.get(i);
			if (T.openPar > 0)
				P.x = i;
			if (T.closedPar > 0) {
				// GROUP FOUND!
				P.y = i;
				break;
			}
		}
		return P;
	}
	
	public static boolean parseCommands(String s) {
		if (s.length() > 1) {
			if (s.substring(0, 2).equals("//")) {
				performCommand(s.toLowerCase());
				return true;
			}
		}
		return false;
	}
	
	public static boolean performCommand(String input) {
		ArrayList<String> args = new ArrayList<String>();
		
		// Parse Command
		String command = "";
		String str = "";
		for (int i = 2; i < input.length(); i++) {
			char c = input.charAt(i);
			if ((c == ' ' || i == input.length() - 1) && str != "") {
				if (c != ' ')
					str += c;
				if (command == "")
					command = str;
				else
					args.add(str);
				str = "";
			}
			else if (c != ' ')
				str += c;
		}
		
		String[] arg = new String[40];
		for (int i = 0; i < args.size(); i++) {
			arg[i] = args.get(i);
		}
		
		
		if (command.equals("hello"))
			System.out.println("Hello, David.");
		else if (command.equals("print")) {
			for (String s : args) {
				System.out.print(s + " ");
			}
			System.out.println();
		}
		else if (command.equals("mode")) {
			if (arg[0].equals("radian") || arg[0].equals("radians")) {
				System.out.println("**RADIAN MODE SET**");
				modeRadians = true;
			}
			else if (arg[0].equals("degree") || arg[0].equals("degrees")) {
				System.out.println("**DEGREE MODE SET**");
				modeRadians = false;
			}
			else
				printError("Unknown argument \"" + arg[0] + "\" for command \"mode\"");
		}
		else
			printError("Unknown command \"" + command + "\"");
		return true;
	}

	public static void performFunction(int paraStart, int paraEnd) {
		performFunction(tokens, paraStart, paraEnd);
	}
	public static void performFunction(ArrayList<Token> tokenList, int paraStart, int paraEnd) {
		// PERFORM FUNCTION:
		Token T = tokenList.get(paraStart);
		String funct = T.func[Math.max(0, T.openPar - 1)];
		
		DataValue[] args = new DataValue[Function.MAX_ARGUMENTS];
		args[0] = T.data;
		int argIndex = 1;
		Operator newOp = T.operator;

		for (int i = 1; i < Function.MAX_ARGUMENTS; i++)
			args[i] = new DataValue();
		

		Function F = Function.getFunction(funct);
		if (F != null) {
			if (T.data.isList() && F.argCount < 0) {
				argIndex = T.data.list.size();
				for (int i = 0; i < T.data.list.size(); i++)
					args[i] = new DataValue(T.data.list.get(i));
			}
			else {
				for (int i = 1; i <= paraEnd - paraStart; i++) {
					args[argIndex] = tokenList.get(paraStart + 1).data;
					newOp = tokenList.get(paraStart + 1).operator;
					if (i == paraEnd - paraStart)
						T.closedPar = tokenList.get(paraStart + 1).closedPar;
					tokenList.remove(paraStart + 1);
					argIndex += 1;
				}
			}
		}
		
		DataValue result = Function.perform(funct, args, argIndex);
		
		if (debugShowFunctions) {
			System.out.print(funct + "(");
			for (int i = 0; i < argIndex; i++) {
				args[i].print();
				if (i < argIndex - 1)
					System.out.print(", ");
			}
			System.out.print(") = ");
			result.print();
			System.out.println();
		}
		
		T.data = result;
		T.func[Math.max(0, T.openPar - 1)] = "";
		T.operator = newOp;
	}
	
	public static void printTokens() {
		for (int i = 0; i < tokens.size(); i++) {
			Token T = tokens.get(i);
			for (int j = 0; j < T.openPar; j++) {
				System.out.print(T.func[j]);
				System.out.print("(");
			}
			if (T.operator.symbol.equals("="))
				System.out.print(T.string);
			else
				T.data.print();
			
			for (int j = 0; j < T.closedPar; j++)
				System.out.print(")");
			if (T.operator.symbol != ",")
				System.out.print(" ");
			if (T.operator.symbol != " ")
				System.out.print(T.operator.symbol + " ");
		}
	}
	
	public static void printError(String errorString) {
		if (!foundError) {
			System.out.println("ERROR: " + errorString);
			new Exception("ERROR: " + errorString).printStackTrace();
		}
		foundError = true;
	}
	
	public static int tokenType(String s, boolean declare) {
		int type = -1;
		boolean decimal = false;
		
		// [1 = number, 2 = constant/variable]
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int ind = (int) c;
			if (type < 0) {
				if (c == '.' || c == 'i' || (ind >= 48 && ind < 58)) {
					// NUMBER
					type = TOKEN_TYPE_NUMBER;
					if (c == '.') {
						decimal = true;
						if (s.length() == 1) {
							printTokenError(s);
							return -1;
						}
						if (i == 0 && s.length() == 2) {
							if (s.charAt(1) == 'i') {
								printTokenError(s);
								return -1;
							}
						}
					}
					if (c == 'i') {
						if (i < s.length() - 1) {
							printTokenError(s);
							return -1;
						}
					}
				}
				else {
					// CONSTANT
					if (stringIsAlphaNumerical(s))
						return TOKEN_TYPE_CONSTANT;
					printTokenError(s);
					break;
				}
			}
			else if (type == TOKEN_TYPE_NUMBER) { // NUMBER
				if (c == '.') {
					if (decimal) {
						printTokenError(s);
						return -1;
					}
					decimal = true;
				}
				else if(c == 'i') {
					if (i < s.length() - 1) {
						printTokenError(s);
						return -1;
					}
				}
				else if (ind < 48 || ind >= 58) {
					printTokenError(s);
					return -1;
				}
			}
		}
		return type;
	}
	
	public static boolean stringIsAlphaNumerical(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c < 48 || (c > 57 && c < 65) || (c > 90 && c < 97) || c > 122)
				return false;
		}
		return true;
	}
	
	public static void printTokenError(String s) {
		printError("Unexpected token \"" + s + "\"");
//		new Exception().printStackTrace();
	}
	
	public static DataValue readConstant(String s) {
		Variable V = new Variable(s);
		return new DataValue(V);
	}
	
	public static Number readNumber(String s) {
		double value = 0;
		double decimalExp = 1;
		int decimalPlace = 0;
		for (int i = 0; i < s.length(); i++) {
			double dgt = (double) s.charAt(i);
			if (dgt >= 48 && dgt < 58) { // Digits (0 - 9)
				dgt -= 48;
				if (decimalExp < 1) {
					value += decimalExp * dgt;
					decimalExp /= 10d;
					decimalPlace += 1;
					if (decimalPlace > DECIMAL_PLACE_MAX)
						break;
				}
				else
					value = (value * 10d) + dgt;
			}
			else if (dgt == 46) { // Decimal Dot
				decimalExp /= 10d;
				decimalPlace = 1;
			}
			else if (dgt == 105) {// Imaginary i
				if (i == 0)
					value = 1;
				return new Number(0, value);
			}
		}
		return new Number(value);
	}
	
	public static void main(String[] args) {
    	new Main();
	}
}
