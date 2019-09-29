
public class GMath {
	public static final Number I = new Number(0, 1);
	
	public static double radians(double x) {
		if (Main.modeRadians)
			return x;
		return Math.toRadians(x);
	}
	
	public static double sin(double x) {
		return Math.sin(radians(x));
	}

	public static double cos(double x) {
		return Math.cos(radians(x));
	}

	public static double tan(double x) {
		return Math.tan(radians(x));
	}
	

	public static double sinh(double x) {
		return Math.sinh(radians(x));
	}

	public static double cosh(double x) {
		return Math.cosh(radians(x));
	}

	public static double tanh(double x) {
		return Math.tanh(radians(x));
	}
}
