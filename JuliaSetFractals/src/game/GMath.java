package game;

public class GMath {
	
	public static ComplexNumber sqr(ComplexNumber z) {
		ComplexNumber z2 = new ComplexNumber();
		z2.real = (z.real * z.real) - (z.imag * z.imag);
		z2.imag = 2 * z.real * z.imag;
		return z2;
	}
}
