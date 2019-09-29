import java.util.ArrayList;


public class DataValue {
	public Variable variable	 = null; // TODO: Implement this data type
	public Number number 		 = null;
	public List list 	 		 = null;
	public String string 		 = null;
	public Expression expression = null; // TODO: Implement this data type
	
	public DataValue() {
		nullify();
	}
	public DataValue(DataValue dv) {
		set(dv);
	}
	public DataValue(Number number) {
		set(number);
	}
	public DataValue(double value) {
		set(value);
	}
	public DataValue(double real, double imag) {
		set(real, imag);
	}
	public DataValue(List list) {
		set(list);
	}
	public DataValue(String string) {
		set(string);
	}
	public DataValue(Expression expression) {
		set(expression);
	}
	public DataValue(Variable variable) {
		set(variable);
	}
	
	public void set(DataValue dv) {
		nullify();
		if (dv.isVariable())
			this.variable = new Variable(dv.variable);
		else if (dv.isNumber())
			this.number = new Number(dv.number);
		else if (dv.isList())
			this.list = new List(dv.list);
		else if (dv.isString())
			this.string = dv.string;
		else if (dv.isExpression())
			this.expression = new Expression(dv.expression);
	}
	public void set(double value) {
		nullify();
		this.number = new Number(value, 0);
	}
	public void set(Number number) {
		nullify();
		this.number = new Number(number.real, number.imag);
	}
	public void set(double real, double imag) {
		nullify();
		this.number = new Number(real, imag);
	}
	public void set(List list) {
		nullify();
		this.list = list;
	}
	public void set(String string) {
		nullify();
		this.string = string;
	}
	public void set(Expression expression) {
		nullify();
		this.expression = new Expression(expression);
	}
	public void set(Variable variable) {
		nullify();
		this.variable = new Variable(variable);
	}
	
	public void nullify() {
		this.variable 	= null;
		this.number 	= null;
		this.list   	= null;
		this.string 	= null;
		this.expression = null;
	}
	
	public boolean isNumber() {
		return (number != null);
	}
	public boolean isReal() {
		if (!isNumber())
			return false;
		return (number.isReal());
	}
	public boolean isImag() {
		if (!isNumber())
			return false;
		return (number.isImag());
	}
	public boolean isList() {
		return (list != null);
	}
	public boolean isString() {
		return (string != null);
	}
	public boolean isExpression() {
		return (expression != null);
	}
	public boolean isVariable() {
		return (variable != null);
	}
	
	public void operate(DataValue dv, Operator op) {
		if (isNumber() && dv.isNumber()) {
			// TWO NUMBERS
			number = op.perform(number, dv.number);
		}
		else if ((isNumber() && dv.isList()) || (dv.isNumber() && isList())) {
			// ONE LIST, ONE NUMBER
			if (dv.isList()) {
				this.list = dv.list;
				dv.number = this.number;
			}
			for (int i = 0; i < list.size(); i++)
				list.set(i, new DataValue(op.perform(list.get(i).number, dv.number)));
		}
		else if (isList() && dv.isList() && !dimMismatch(dv)) {
			// TWO LISTS
			for (int i = 0; i < list.size(); i++) {
				for (int j = 0; j < dv.list.size(); j++) {
					list.set(i, new DataValue(op.perform(list.get(i).number, dv.list.get(i).number)));
				}
			}
		}
		else if (isString() || dv.isString()) {
			// STRING AND DATAVALUE
			if (isString())
				this.string += dv.getPrintStringNoQuotes();
			else
				set(dv.string + getPrintStringNoQuotes());
		}
		else {
			// ERROR
			Main.printError("Data Type");
		}
	}
	
	public boolean dimMismatch(DataValue dv) {
		if (!dv.isList() || !isList())
			return true;
		if (dv.list.size() != list.size()) {
			Main.printError("Domain Mismatch");
			return true;
		}
		return false;
	}
	
	public String getPrintStringNoQuotes() {
		if (isString())
			return string;
		return getPrintString();
	}
	public String getPrintString() {
		if (isVariable())
			return variable.getPrintString();
		else if (isList())
			return list.getPrintString();
		else if (isNumber())
			return number.getPrintString();
		else if (isString())
			return ('"' + string + '"');
		else if (isExpression())
			return expression.getPrintString();
		return "ERROR";
	}
	
	public void print() {
		if (isVariable())
			variable.print();
		else if (isList())
			list.print();
		else if (isNumber())
			number.print();
		else if (isString())
			System.out.print('"' + string + '"');
		else if (isExpression())
			expression.print();
	}
}
