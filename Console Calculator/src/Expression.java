import java.util.ArrayList;


public class Expression {
	public ArrayList<Token> tokens = new ArrayList<Token>();
	public String dependentVar = "";
	
	public Expression() {
	}
	
	public Expression(ArrayList<Token> tokens) {
		set(tokens);
	}
	public Expression(Expression expression) {
		set(expression);
	}
	
	public void clear() {
		tokens.clear();
	}
	
	public void add(Token T) {
		tokens.add(T);
	}
	
	public void set(int index, Token T) {
		tokens.set(index, T);
	}

	public void set(Expression expression) {
		if (expression != null) {
			dependentVar = expression.dependentVar;
			if (expression.tokens != null)
				set(expression.tokens);
		}
	}
	
	public void set(ArrayList<Token> newTokens) {
		for (int i = 0; i < newTokens.size(); i++) {
			this.tokens.add(new Token(newTokens.get(i)));
		}
	}
	
	public Token get(int index) {
		return tokens.get(index);
	}
	
	public void fillVariables() {
		for (Token T : tokens) {
			if (T.data.isVariable() && !T.operator.parentSymbol.equals("=")) {
				// Set this variable to it's appropriate constant
				T.data = T.data.variable.getData();
			}
		}
	}
	
	public void setVariable(String varName, DataValue data) {
		for (Token T : tokens) {
			if (T.data.isVariable()) {
				if (T.data.variable.name.equals(varName)) {
					// Set this variable to "data"
					T.data = new DataValue(data);
				}
			}
		}
	}
	
	public ArrayList<Token> getArrayList() {
		return tokens;
	}
	
	public String getPrintString() {
		String str = "|[";
		for (int i = 0; i < tokens.size(); i++) {
			str += tokens.get(i).getPrintString();
		}
		str += "]|";
		return str;
	}
	
	public void print() {
		System.out.print(getPrintString());
	}
}
