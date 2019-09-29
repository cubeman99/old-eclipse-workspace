import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexType;


public class Number {
	public double real;
	public double imag;
	
	
	public Number(double real, double imag) {
		this.real = real;
		this.imag = imag;
	}
	
	public Number(double real) {
		this.real = real;
		this.imag = 0;
	}
	
	public Number() {
		this.real = 0;
		this.imag = 0;
	}
	
	public Number(Number N) {
		this();
		if (N != null) {
			this.real = N.real;
			this.imag = N.imag;
		}
	}
	
	public Number toRadians() {
		return new Number(Math.toRadians(real), Math.toRadians(imag));
	}

	public Number toDegrees() {
		return new Number(Math.toRadians(real), Math.toRadians(imag));
	}

	public Number radians() {
		return new Number(GMath.radians(real), GMath.radians(imag));
	}
	
	public boolean isReal() {
		return (imag == 0);
	}

	public boolean isImag() {
		return (imag != 0);
	}
	
	public boolean isTrue() {
		return (real > 0);
	}
	
	public boolean isFalse() {
		return (real <= 0);
	}
	
	public Number inverse() {
		return new Number(-real, -imag);
	}
	
	public Number conjugate() {
		return new Number(real, -imag);
	}
	
	public static boolean checkImaginaryError(Number x, Number y) {
		if (x.isImag() || y.isImag()) {
			Main.printError("Imaginary Data Type");
			return true;
		}
		return false;
	}
	
	public static double dBool(boolean b) {
		if (b)
			return 1;
		return 0;
	}
	
	public static Number nBool(boolean b) {
		if (b)
			return new Number(1);
		return new Number(0);
	}
	
	public static double arg(Number n) {
		if (n.real != 0 || n.imag != 0) {
			if (n.real == 0 && n.imag != 0) {
				if (n.imag >= 0)
					return (Math.PI / 2);
				return ((3 * Math.PI) / 2);
			}
			return Math.atan2(n.imag, n.real);
		}
		Function.printDomainError();
		return 0;
	}
	
	public static double abs(Number n) {
		return Math.sqrt((n.real * n.real) + (n.imag * n.imag));
	}
	
	// TRIGONOMETRIC FUNCTIONS:
	public static Number sin(Number n) {
//		if (n.imag == 0)
//			return new Number(GMath.sin(n.real));
		return new Number(GMath.sin(n.real) * GMath.cosh(n.imag), GMath.cos(n.real) * GMath.sinh(n.imag));
	}
	
	public static Number cos(Number n) {
		return new Number(GMath.cos(n.real) * GMath.cosh(n.imag), -GMath.sin(n.real) * GMath.sinh(n.imag));
	}

	public static Number tan(Number n) {
		return divide(sin(n), cos(n));
	}

	public static Number csc(Number n) {
		return divide(new Number(1), sin(n));
	}

	public static Number sec(Number n) {
		return divide(new Number(1), cos(n));
	}
	
	public static Number cot(Number n) {
		return divide(cos(n), sin(n));
	}
	
	public static Number asin(Number n) {
		// asin(z) = -i * ln((i * z) + sqrt(1 - z^2))
//		if (n.real == 1)
//			return new Number(Math.PI / 2.0d);
		return multiply(GMath.I.inverse(), Function.complexLn(add(multiply(GMath.I, n), power(subtract(new Number(1), power(n, new Number(2))), new Number(0.5)))));
//		return new Number(Math.sin(n.real) * Math.cosh(n.imag), Math.cos(n.real) * Math.sinh(n.imag));
	}

	public static Number acos(Number n) {
		// asin(z) = -i * ln(z + i*sqrt(1 - z^2))
		return multiply(GMath.I.inverse(), Function.complexLn(add(n, multiply(GMath.I, power(subtract(new Number(1), power(n, new Number(2))), new Number(0.5))))));
//		return new Number(Math.sin(n.real) * Math.cosh(n.imag), Math.cos(n.real) * Math.sinh(n.imag));
	}
	
	// HYPERBOLIC FUNCTIONS:
	public static Number sinh(Number n) {
		// z = (e^n - e^(-n)) / 2
		Number x = n.radians();
		return divide(subtract(power(new Number(Math.E), x), power(new Number(Math.E), x.inverse())), new Number(2));
	}

	public static Number cosh(Number n) {
		// z = (e^n + e^(-n)) / 2
		Number x = n.radians();
		return divide(add(power(new Number(Math.E), x), power(new Number(Math.E), x.inverse())), new Number(2));
	}

	public static Number tanh(Number n) {
		return divide(sinh(n), cosh(n));
	}
	
	public static Number csch(Number n) {
		return divide(new Number(1), sinh(n));
	}

	public static Number sech(Number n) {
		return divide(new Number(1), cosh(n));
	}
	
	public static Number coth(Number n) {
		return divide(cosh(n), sinh(n));
	}
	
	
	public static void printValue(double value) {
		System.out.print(value);
		/*
		double X = Math.abs(value);
		int mult = 0;
		if (value < 0)
			System.out.print("-");
		while (X >= 10) {
			X /= 10.0d;
			mult += 1;
		}
		while (true) {
//			if (X == 0)
//				break;
			if (mult == -1)
				System.out.print(".");
			System.out.print((int) X);
			mult -= 1;
			X = (X - ((int) X)) * 10;
			if (mult < -10)
				break;
		}
		*/
	}
	
	public static String numString(double x) {
		String s = "" + x;
		if (Math.abs(x) < Math.pow(10, -Main.DECIMAL_PLACE_MAX))
			s = "" + 0;
		return s;
	}
	
	public String getPrintString() {
		String s = "";
		if (real != 0) {
			if (imag != 0)
				s += "(";
			s += numString(real);
			if (imag != 0) {
				if (imag > 0)
					s += " + ";
				else if (imag < 0)
					s += " - ";
				if (Math.abs(imag) != 1)
					s += numString(Math.abs(imag));
				s += "i)";
			}
		}
		else if (imag != 0) {
			if (imag == -1)
				s += "-";
			else if (imag != 1)
				s += numString(imag);
			s += "i";
		}
		else
			return ("" + 0);
		return s;
	}
	
	public void print() {
		System.out.print(getPrintString());
	}
	
	/////////////////
	// OPERATIONS: //
	/////////////////
	public static Number add(Number x, Number y) {
		return new Number(x.real + y.real, x.imag + y.imag);
	}

	public static Number subtract(Number x, Number y) {
		return new Number(x.real - y.real, x.imag - y.imag);
	}
	
	public static Number multiply(Number x, Number y) {
		return new Number((x.real * y.real) - (x.imag * y.imag), (x.real * y.imag) + (y.real * x.imag));
	}
	
	public static Number divide(Number x, Number y) {
		double divisor = (y.real * y.real) + (y.imag * y.imag);
		return new Number(((x.real * y.real) + (x.imag * y.imag)) / divisor,
					      ((y.real * x.imag) - (x.real * y.imag)) / divisor);
	}
	
	public static Number power(Number x, Number y) {
		if (x.real == 0 && x.imag == 0)
			return new Number(0);
		
	    double r = abs(x); // Polar Coordinate: (radius)
	    double t = arg(x); // Polar Coordinate: (theta)

		double real = Math.pow(r, y.real) * Math.exp(-y.imag * t) * Math.cos((y.real * t) + (y.imag * Math.log(r)));
		double imag = Math.pow(r, y.real) * Math.exp(-y.imag * t) * Math.sin((y.real * t) + (y.imag * Math.log(r)));
		return new Number(real, imag);
	}
	
	public static Number equal(Number x, Number y) {
		return new Number(Operator.dBool((x.real == y.real) && (x.imag == y.imag)));
	}
	
	public static Number unequal(Number x, Number y) {
		return new Number(Operator.dBool((x.real != y.real) || (x.imag != y.imag)));
	}
	
	///////////////////////////
	// REAL ONLY OPERATIONS: //
	///////////////////////////
	public static Number modulus(Number x, Number y) {
		if (checkImaginaryError(x, y))
			return new Number();
		return new Number(x.real % y.real);
	}
	
	public static Number and(Number x, Number y) {
		if (checkImaginaryError(x, y))
			return new Number();
		return nBool(x.isTrue() && y.isTrue());
	}
	
	public static Number or(Number x, Number y) {
		if (checkImaginaryError(x, y))
			return new Number();
		return nBool(x.isTrue() || y.isTrue());
	}

	public static Number greaterThan(Number x, Number y, boolean orEqualTo) {
		if (checkImaginaryError(x, y))
			return new Number();
		if (orEqualTo)
			return nBool(x.real >= y.real);
		else
			return nBool(x.real > y.real);
	}

	public static Number lessThan(Number x, Number y, boolean orEqualTo) {
		if (checkImaginaryError(x, y))
			return new Number();
		if (orEqualTo)
			return nBool(x.real <= y.real);
		else
			return nBool(x.real < y.real);
	}
}
