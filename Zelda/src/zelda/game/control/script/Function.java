package zelda.game.control.script;

import java.util.ArrayList;
import zelda.common.properties.PropertyHolder;
import zelda.game.world.Frame;


/**
 * Function.
 * 
 * @author David Jordan
 */
public abstract class Function {
	private String functionName;
	private int numArguments;
	private boolean variableParameters;



	// ================== CONSTRUCTORS ================== //

	public Function(String name) {
		this(name, 0, false);
	}

	public Function(String name, boolean variableParameters) {
		this(name, 0, variableParameters);
	}

	public Function(String name, int numArguments) {
		this(name, numArguments, false);
	}

	public Function(String name, int numArguments, boolean variableParameters) {
		this.functionName = name;
		this.numArguments = numArguments;
		this.variableParameters = variableParameters;
	}



	// =============== ABSTRACT METHODS =============== //

	public abstract String execute(ArrayList<String> args,
			PropertyHolder holder, Frame frame);



	// =================== ACCESSORS =================== //

	public boolean canUseThisFunction(String functionName, int numArguments) {
		if (!functionName.equals(functionName))
			return false;
		if (variableParameters && numArguments >= this.numArguments)
			return true;
		return (numArguments == this.numArguments);
	}

	public int getNumArguments() {
		return numArguments;
	}

	public String getName() {
		return functionName;
	}

	public boolean canHaveVariableParameters() {
		return variableParameters;
	}
}
