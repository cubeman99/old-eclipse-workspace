import java.util.ArrayList;
import java.util.HashMap;


public class Function {
	public String name;
	public int argCount;
	public DataValue[] dataTypes;
	
	public static final int MAX_ARGUMENTS  = 100;
	
	public static final double MIN_DELTA_X = 0.0001;
	public static final double MAX_DELTA_X = 1;
	public static final double PARTITION_SUB_INTERVALS = 20;
	
	public static int NTH_ROOT_ITERATIONS  = 100;
	
	public static ArrayList<Function> functionList = new ArrayList<Function>();
	public static boolean configuring = false;
	
	public static String funcString = "";
	public static boolean funcSetupImag = false;
	
	public Function() {
		
	}
	
	public Function(String name, int argCount, DataValue[] dataTypes) {
		this.name 	   = name;
		this.argCount  = argCount;
		this.dataTypes = dataTypes;
//		dataTypes = new DataValue[MAX_ARGUMENTS];
//		for (int i = 0; i < MAX_ARGUMENTS; i++) {
//			dataTypes[i] = new DataValue();
//			dataTypes[i].list   = null;
//			dataTypes[i].number = new Number(1, 1);
//		}
	}
	

	public static void configureFunctions() {
		configuring = true;
		DataValue[] temp = new DataValue[MAX_ARGUMENTS];
		for (int i = 0; i < MAX_ARGUMENTS; i++) {
			temp[i] = new DataValue(0);
			temp[i].list = new List();
		}
		perform("", temp, 0);
		configuring = false;
	}
	
	private static void addFunction(String name, int argCount, DataValue[] dataTypes) {
		functionList.add(new Function(name, argCount, dataTypes));
	}

	public static boolean funcNameI(String s) {
		return funcName(s, 1, true);
	}
	public static boolean funcNameI(String s, int argCount) {
		return funcName(s, argCount, true);
	}
	public static boolean funcNameE(String s) {
		return funcName(s, 1, false, true);
	}
	public static boolean funcNameE(String s, int argCount) {
		return funcName(s, argCount, false, true);
	}
	public static boolean funcName(String s) {
		return funcName(s, 1, false);
	}
	public static boolean funcName(String s, int argCount) {
		return funcName(s, argCount, false);
	}

	public static boolean funcName(String s, int argCount, boolean imags) {
		return funcName(s, argCount, imags, false);
	}
	
	public static boolean funcName(String s, int argCount, boolean imags, boolean inputExpression) {
		if (configuring) {
			DataValue[] dataTypes = new DataValue[MAX_ARGUMENTS];
			for (int i = 0; i < MAX_ARGUMENTS; i++) {
				if (imags)
					dataTypes[i] = new DataValue(1, 1);
				else if (inputExpression)
					dataTypes[i] = new DataValue(new Expression());
				else if (false)
					dataTypes[i] = new DataValue(new List());
				else
					dataTypes[i] = new DataValue(new Number(1, 1));
			}
			addFunction(s, argCount, dataTypes);
			return false;
		}
		return (funcString.equals(s));
	}
	
	public static DataValue returnNum(double real, double imag) {
		return new DataValue(new Number(real, imag));
	}
	public static DataValue returnNum(double real) {
		return new DataValue(new Number(real));
	}
	
	public static void funcInit(boolean handleImag) {
		
	}
	
	/*public static void initialize() {
		Function FF = new Function("sinh", 1) {
			public DataValue perform(ArrayList<DataValue> args) {
				return returnNum(args.get(0).number.real);
			}
		};
	}*/
	
	public static DataValue perform(String s, DataValue[] arg, int argsSize) {
//		double a = arg[0];
//		double b = arg[1];
//		double c = arg[2];
//		double d = arg[3];
//		double e = arg[4];
//		double f = arg[5];
//		double g = arg[6];
//		double h = arg[7];
//		double x = a;
//		double y = b;
		
		Number n1 = arg[0].number;
		Number n2 = arg[1].number;
		Number n3 = arg[2].number;
		Number n4 = arg[3].number;
		
		funcString = s;
		
		boolean allNumber = true;
		boolean allReal   = true;
		for (int i = 0; i < argsSize; i++) {
			if (!arg[i].isNumber())
				allNumber = false;
			else if (arg[i].isImag())
				allReal = false;
		}
		
		if (!configuring && argsSize == 0) {
			Main.printError("Argument is NULL");
			return new DataValue();
		}
		if (!configuring && getFunction(s) != null) {
			int maxargs = getFunction(s).argCount;
			if (argsSize != maxargs && maxargs > 0) {
				String plural = "";
				if (maxargs > 1)
					plural = "s";
				Main.printError("Function \"" + s + "\" takes " + maxargs + " argument" + plural);
				return new DataValue();
			}
		}
		
		if (n1 != null) {
			if (funcNameI("inv") || funcName("-"))
				return returnNum(-n1.real, -n1.imag);
			funcInit(true);
			if (funcName("not") || funcName("!"))
				return returnNum(dBool(n1.real <= 0));
			if (funcNameI("sqr"))
				return new DataValue(Number.power(n1, new Number(2)));
			if (funcNameI("sqrt"))
				return new DataValue(Number.power(n1, new Number(1/2)));
			if (funcNameI("cbrt"))
				return new DataValue(Number.power(n1, new Number(1/3)));
			if (funcNameI("abs")) {
				if (n1.isReal())
					return returnNum( Math.abs(n1.real));
				return returnNum(Math.sqrt((n1.real * n1.real) + (n1.imag * n1.imag)));
			}
			if (funcNameI("conj"))
				return new DataValue(n1.conjugate());
			if (funcName("sign"))
				return returnNum( Math.signum(n1.real));
			if (funcNameI("int"))
				return returnNum((int) n1.real, (int) n1.imag);
			if (funcNameI("floor"))
				return returnNum(Math.floor(n1.real), Math.floor(n1.imag));
			if (funcNameI("ceil"))
				return returnNum(Math.ceil(n1.real), Math.ceil(n1.imag));
			if (funcNameI("fix")) {
				double R = Math.floor(n1.real);
				if (n1.real >= 0)
					R = Math.ceil(n1.real);
				double I = Math.floor(n1.imag);
				if (n1.imag >= 0)
					I = Math.ceil(n1.imag);
				return returnNum(R, I);
			}
			if (funcNameI("round"))
				return returnNum(Math.round(n1.real), Math.round(n1.imag));
			if (funcName("degrees"))
				return returnNum( Math.toDegrees(n1.real));
			if (funcName("radians"))
				return returnNum( Math.toRadians(n1.real));
			if (funcNameI("ln")) {
				if (n1.real != 0 || n1.imag != 0) {
					return new DataValue(complexLogarithm(Math.E, n1));
//					return returnNum( Math.log(n1.real));
				}
				printDomainError();
			}
			if (funcNameI("log") || funcNameI("log10")) {
				if (n1.real != 0 || n1.imag != 0)
					return new DataValue(complexLogarithm(10, n1));
//					return returnNum( Math.log10(n1.real));
				printDomainError();
			}
			if (funcNameI("log2")) {
				if (n1.real != 0 || n1.imag != 0)
					return new DataValue(complexLogarithm(2, n1));
//					return returnNum( (Math.log10(n1.real) / Math.log10(2)));
				printDomainError();
			}
			
			if (funcNameI("sin")) {
				return new DataValue(Number.sin(n1));
			}
			if (funcNameI("cos")) {
				return new DataValue(Number.cos(n1));
			}
			if (funcNameI("tan")) {
				return new DataValue(Number.tan(n1));
			}
			if (funcNameI("csc")) {
				return new DataValue(Number.csc(n1));
			}
			if (funcNameI("sec")) {
				return new DataValue(Number.sec(n1));
			}
			if (funcNameI("cot")) {
				return new DataValue(Number.cot(n1));
			}
			
			if (funcName("sinh")) {
				return new DataValue(Number.sinh(n1));
			}
			if (funcName("cosh")) {
				return new DataValue(Number.cosh(n1));
			}
			if (funcName("tanh")) {
				return new DataValue(Number.tanh(n1));
			}
			if (funcName("csch")) {
				return new DataValue(Number.csch(n1));
			}
			if (funcName("sech")) {
				return new DataValue(Number.sech(n1));
			}
			if (funcName("coth")) {
				return new DataValue(Number.coth(n1));
			}
			

			if (funcName("asin")) {
				return new DataValue(Number.asin(n1));
			}
			if (funcName("acos")) {
				return new DataValue(Number.acos(n1));
			}
			
			/*if (funcName("acos")) {
				if (!domainError(n1.real, -1, 1))
					return returnNum(invAngleUnit(Math.asin(n1.real)));
			}
			if (funcName("atan")) {
				return returnNum(invAngleUnit(Math.atan(n1.real)));
			}*/
			
			if (funcName("factorial")) {
				double value = 1;
				if (n1.real == 0)
					return returnNum(0);
				if (n1.real <= 0 || n1.real % 1 != 0)
					printDomainError();
				for (int i = 1; i <= n1.real; i++)
					value *= i;
				return returnNum(value);
			}
		}
		if (n1 != null && n2 != null) {
			double x = n1.real;
			double y = n2.real;
			
			if (funcName("length", 2))
				return returnNum(Math.sqrt((x * x) + (y * y)));
			if (funcNameI("nroot", 2))
				return new DataValue(Number.power(n2, Number.divide(new Number(1), n1)));
			if (funcNameI("pow", 2))
				return new DataValue(Number.power(n1, n2));
			if (funcNameI("logbase", 2)) {
				return new DataValue(complexLogarithm(n1, n2));
//				printDomainError();
			}
			if (funcName("atan2", 2))
				return returnNum(invAngleUnit(Math.atan2(y, x)));
			
			if (n3 != null && n4 != null) {
				double x2 = n3.real;
				double y2 = n4.real;
				if (funcName("dist", 4))
					return returnNum(Math.sqrt(((x2 - x) * (x2 - x)) + ((y2 - y) * (y2 - y))));
			}
		}
		
		
		
		
		if (funcName("max", -1)) {
			DataValue value = arg[0];
			for (int i = 1; i < argsSize; i++) {
				value.number.real = Math.max(value.number.real, arg[i].number.real);
			}
			return value;
		}
		if (funcName("min", -1)) {
			DataValue value = arg[0];
			for (int i = 1; i < argsSize; i++) {
				value.number.real = Math.min(value.number.real, arg[i].number.real);
			}
			return value;
		}
		if (funcNameI("sum", -1))
			return new DataValue(arraySum(arg, argsSize));
		if (funcNameI("avg", -1) || funcName("mean", -1))
			return new DataValue(Number.divide(arraySum(arg, argsSize), new Number(argsSize)));
		
		if (funcNameI("size", -1))
			return returnNum(argsSize);
		
		
		
		
		if (funcName("rand"))
			return returnNum(Main.random.nextDouble());
		

//		if (funcName("mode", -1)) {
//			HashMap<Double, Double> listMap = new HashMap<Double, Double>();
//			for (int i = 0; i < argsSize; i++)
//				listMap.put(arg[i], 1);
//			for (int i = 0; i < argsSize; i++)
//				listMap.put(arg[i], listMap.get(arg[i]) + 1);
//			double mode = 0;
//			int highestCount = 0;
//			for (int i = 0; i < argsSize; i++)
//				listMap.put(arg[i], listMap.get(arg[i]) + 1);
//			if (highestCount > 0)
//			return (arraySum(arg, argsSize)) / argsSize;
//		}
		if (funcName("range", -1)) {
			double min = arg[0].number.real;
			double max = arg[0].number.real;
			for (int i = 0; i < argsSize; i++) {
				if (arg[i].number.real < min)
					min = arg[i].number.real;
				if (arg[i].number.real > max)
					max = arg[i].number.real;
			}
			return returnNum(max - min);
		}
		
		if (funcNameE("function", 2)) {
			Expression tempExp   = new Expression(arg[0].expression);
			tempExp.dependentVar = arg[1].string;
			return new DataValue(tempExp);
		}
		
		if (funcNameE("expression", 2)) {
			return arg[0];
		}

		if (funcNameE("func_get", 2)) {
			DataValue ans = new DataValue(new Expression(arg[0].expression));
			do {
				Expression tempExp = new Expression(ans.expression);
				tempExp.setVariable(tempExp.dependentVar, new DataValue(n2.real));
				tempExp.fillVariables();
				tempExp.print();
				System.out.println();
				ans = Main.solveExpression(tempExp);
			} while (ans.isExpression());
			return ans;
		}
		
		if (funcNameE("fnint", 4)) {
			return solveDefiniteIntegral(arg[0].expression, arg[1], arg[2], arg[3]);
		}
		
		if (funcName("list_init", -1)) {
			List newList = new List();
			for (int i = 0; i < argsSize; i++) {
				newList.add(arg[i]);
			}
			return new DataValue(newList);
		}
		if (funcName("list_get", 2)) {
			if (arg[0].isList() && arg[1].isNumber() && arg[1].isReal()) {
				int index = (int) arg[1].number.real;
				if (index == arg[1].number.real && index >= 0 && index < arg[0].list.size()) {
					return new DataValue(arg[0].list.get((int) arg[1].number.real));
				}
				printOutOfBoundsError();
			}
			printDataTypeError();
			return new DataValue();
		}
		
		
		if (!configuring && !Main.foundError)
			Main.printError("Unknown Function \"" + s + "\"");
		return new DataValue();
	}
	
	public static DataValue solveDefiniteIntegral(Expression expression, DataValue lim1, DataValue lim2, DataValue varName) {
		// Solve a Definite Integral using LRAM
		if (!lim1.isReal() || !lim2.isReal() || !varName.isString()) {
			printDataTypeError();
		}
		double a = lim1.number.real;
		double b = lim2.number.real;
		String var  = varName.string;
		double dx = Math.max(MIN_DELTA_X, Math.min(MAX_DELTA_X, (a + b) / PARTITION_SUB_INTERVALS));
		dx = 0.001;
		System.out.println("DEFINITE INTEGRAL:");
		System.out.println("a = " + a);
		System.out.println("b = " + b);
		System.out.println("dx = " + dx);
		double sum = 0;
		boolean db1 = Main.debugShowOperations;
		boolean db2 = Main.debugShowFunctions;
		Main.debugShowOperations = false;
		Main.debugShowFunctions  = false;
		// pi = fnint(4 / (1 + x^2), 0, 1, "x")
		for (double x = a; x < b; x += dx) {
			Expression tempExp = new Expression(expression);
			tempExp.setVariable(var, new DataValue(x));
			DataValue DV = Main.solveExpression(tempExp);
			if (DV.isImag() || !DV.isNumber()) {
				printDataTypeError();
				return new DataValue();
			}
			sum += DV.number.real * dx;
		}
		Main.debugShowOperations = db1;
		Main.debugShowFunctions  = db2;
		
		return new DataValue(sum);
	}
	
	public static Number complexLogarithm(double base, Number x) {
		return complexLogarithm(new Number(base), x);
	}
	public static Number complexLogarithm(Number base, Number x) {
		Number z = Number.divide(complexLn(x), complexLn(base));
		return z;
	}
	public static Number complexLn(Number x) {
		double r = Number.abs(x); // Polar Coordinate: (radius)
	    double t = Number.arg(x); // Polar Coordinate: (theta)
		Number z = new Number(Math.log(r), t);
		return z;
	}
	
	public static boolean isEven(double x) {
		return (x % 2 == 0);
	}
	public static boolean isOdd(double x) {
		return ((x + 1) % 2 == 0);
	}
	
	public static double nthRoot(double a, double n) {
		if (a == 0 || a == 1)
			return a;
		double x = 1;
		for (int i = 0; i < NTH_ROOT_ITERATIONS; i++)
			x = (1 / n) * (((n - 1) * x) + (a / Math.pow(x, n - 1)));
		if (n == 0) {
			printDomainError();
			return 0;
		} 
		if (isEven(n) && a < 0) {
			printNotRealError();
			return 0;
		} 
		return x;
	}
	
	public static Number arraySum(DataValue[] list, int size) {
		Number sum = new Number(0, 0);
		for (int i = 0; i < size; i++)
			sum = Number.add(sum, list[i].number);
		return sum;
	}
	
	public static boolean zeroError(double x) {
		if (x == 0) {
			Main.printError("Invalid domain");
			return true;
		}
		return false;
	}
	public static boolean domainError(double x, double min, double max) {
		// BOTH INCLUSIVE
		if (x < min || x > max) {
			Main.printError("Invalid domain");
			return true;
		}
		return false;
	}
	
	public static void printImaginaryDomainError() {
		Main.printError("Invalid imaginary domain");
	}
	
	public static void printOutOfBoundsError() {
		Main.printError("Invalid Dimensions");
	}
	
	public static void printDataTypeError() {
		Main.printError("Data type");
	}
	
	public static void printDomainError() {
		Main.printError("Invalid domain");
	}
	
	public static void printNotRealError() {
		Main.printError("Answer is not real");
	}
	
	public static double angleUnit(double a) {
		if (!Main.modeRadians)
			return Math.toRadians(a);
		return a;
	}

	public static double invAngleUnit(double a) {
		if (!Main.modeRadians)
			return Math.toDegrees(a);
		return a;
	}
	
	public static boolean isFunction(String s) {
		for (Function func : functionList) {
			if (func.name.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	public static Function getFunction(String s) {
		for (Function func : functionList) {
			if (func.name.equals(s)) {
				return func;
			}
		}
		return null;
	}
	
	public static double dBool(boolean b) {
		if (b)
			return 1;
		return 0;
	}
}
