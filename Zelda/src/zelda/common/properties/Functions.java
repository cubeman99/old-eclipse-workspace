package zelda.common.properties;

import java.util.ArrayList;
import zelda.game.control.script.Function;
import zelda.game.world.Frame;

public class Functions {
	private ArrayList<Function> functions;
	
	
	
	// ================== CONSTRUCTORS ================== //

	public Functions() {
		functions = new ArrayList<Function>();
	}
	
	

	// =================== ACCESSORS =================== //
	
	public Function getFunction(String funcName) {
		for (int i = 0; i < functions.size(); i++) {
			if (functions.get(i).getName().equals(funcName))
				return functions.get(i);
		}
		return null;
	}
	
	// ==================== MUTATORS ==================== //
	
	public Function addFunction(Function func) {
		functions.add(func);
		return func;
	}
	
	public String callFunction(String funcName, ArrayList<String> args, PropertyHolder holder, Frame frame) {
		Function func = getFunction(funcName);
		if (func != null)
			func.execute(args, holder, frame);
		return "";
	}
}
