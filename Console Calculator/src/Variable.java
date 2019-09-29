
public class Variable {
	public String name;

	public Variable(Variable variable) {
		name = "";
		if (variable != null)
			this.name = variable.name;
	}
	
	public Variable(String string) {
		this.name = string;
	}

	public Variable(Constant C) {
		this.name = C.name;
	}
	
	public DataValue getData() {
		Constant C = Constant.getConstant(name);
		if (C == null) {
			Main.printTokenError(name);
			return new DataValue();
		}
		return new DataValue(C.data);
	}
	
	public String getPrintString() {
		return name;
	}
	
	public void print() {
		System.out.print(getPrintString());
	}
}
