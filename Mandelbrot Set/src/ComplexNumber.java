
public class ComplexNumber {
	public double real;
	public double imag;
	
	public ComplexNumber() {
		this.real = 0;
		this.imag = 0;
	}
	
	public ComplexNumber(double real, double imag) {
		this.real = real;
		this.imag = imag;
	}
	
	public ComplexNumber(ComplexNumber c) {
		this.real = c.real;
		this.imag = c.imag;
	}
	
	public void set(double real, double imag) {
		this.real = real;
		this.imag = imag;
	}
	
	public void set(ComplexNumber c) {
		this.real = c.real;
		this.imag = c.imag;
	}
	
	public void setReal(double real) {
		this.real = real;
	}
	public void setImag(double imag) {
		this.imag = imag;
	}
	
	public double getReal(double real) {
		return real;
	}
	public double getImag(double real) {
		return imag;
	}
	
	public void square() {
		double r  = real;
		double i  = imag;
		this.real = (r * r) - (i * i);
		this.imag = 2 * r * i;
	}
	
}
