package game;

public class ComplexNumber {
	public double real;
	public double imag;
	
	public ComplexNumber(double real, double imag) {
		this.real = real;
		this.imag = imag;
	}
	
	public ComplexNumber() {
		this(0, 0);
	}
	
	
	
	public void square() {
		double r  = real;
		double i  = imag;
		this.real = (r * r) - (i * i);
		this.imag = 2 * r * i;
	}
}
