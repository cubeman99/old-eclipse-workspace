package zelda.game.control.script;

import java.util.ArrayList;
import zelda.common.properties.PropertyHolder;
import zelda.game.world.Frame;


public class Script {
	public static ScriptFunctions functions;
	public static ScriptConstants constants;

	/**
	 * Execute the given script code called by the given property holder.
	 * 
	 * @param scriptName
	 *            - the name of the script property
	 * @param script
	 *            - the code of the script to execute
	 * @param holder
	 *            - the property holder calling the script
	 * @param frame
	 *            - the frame the script was called in
	 * @return whatever the script returns
	 */
	public static String execute(String scriptName, String script,
			PropertyHolder holder, Frame frame) {
		Scanner scr = new Scanner(script);

		try {
			String result = "";

			while (scr.hasNext())
				result = execute(scriptName, scr, holder, frame);
			
			while (result.endsWith(";"))
				result = result.substring(0, result.length() - 1);

			return result;
		}
		catch (ScriptError e) {
			e.printStackTrace();
		}

		return "";
	}



	public static void initialize() {
		functions = new ScriptFunctions();
		constants = new ScriptConstants();
	}

	private static String execute(String scriptName, Scanner scr,
			PropertyHolder holder, Frame frame) throws ScriptError {
		String str = scr.next();

		if (str.endsWith("!")) {
			// Function with no parameters.
			return function(str.substring(0, str.length() - 1), holder, frame);
		}
		else if (str.endsWith(":")) {
			// Function with one or more parameters.
			ArrayList<String> params = new ArrayList<String>();
			String closing = "";

			while (scr.hasNext()) {
				String arg = execute(scriptName, scr, holder, frame);

				if (arg.endsWith(";")) {
					arg = arg.substring(0, arg.length() - 1);
					while (arg.endsWith(";")) {
						closing += ";";
						arg = arg.substring(0, arg.length() - 1);
					}
					params.add(arg);
					break;
				}
				else
					params.add(arg);
			}

			return (function(str.substring(0, str.length() - 1), params,
					holder, frame) + closing);
		}
		else {
			// Check for constants.
			Constant c = constants.getConstant(str);
			if (c != null) {
				String closing = "";
				while (str.endsWith(";")) {
					closing += ";";
					str = str.substring(0, str.length() - 1);
				}
				return (c.getValue() + closing);
			}
		}
		return str;
	}



	private static String function(String functionName, PropertyHolder holder,
			Frame frame) throws ScriptError {
		return function(functionName, new ArrayList<String>(), holder, frame);
	}



	private static String function(String functionName, ArrayList<String> args,
			PropertyHolder holder, Frame frame) throws ScriptError {
		boolean foundMatch = false;

		for (int i = 0; i < functions.getNumFunctions(); i++) {
			Function func = functions.getFunction(i);

			if (func.getName().equals(functionName)) {
				if (func.canUseThisFunction(functionName, args.size()))
					return func.execute(args, holder, frame);
				foundMatch = true;
			}
		}

		if (foundMatch) {
			throw new ScriptError(
					"Wrong number (" + args.size() + ") of arguments for the function \""
							+ functionName + "\"");
		}

		throw new ScriptError("No such function \"" + functionName
				+ "\" exists!");
	}


	public static String parseString(String arg) {
		if (arg.startsWith("["))
			return arg.substring(1, arg.length() - 1);
		return arg;
	}



	/** Exception Class to represent an error in parsing/executing a script. **/
	private static class ScriptError extends Exception {
		private static final long serialVersionUID = 1L;

		public ScriptError(String error) {
			super(error);
		}
	}

	public static class Scanner {
		private String str;

		public Scanner(String str) {
			this.str = str;
		}

		public boolean hasNext() {
			return str.trim().length() > 0;
		}

		public String next() {
			str = str.trim();
			String output = "";
			int index = 0;

			if (str.startsWith("[")) {
				boolean closed = false;
				for (; index < str.length(); index++) {
					output += str.charAt(index);
					if (closed) {
						if (str.substring(index, index + 1).equals(" "))
							break;
					}
					else if (str.charAt(index) == ']')
						closed = true;
				}
			}
			else {
				for (; index < str.length(); index++) {
					if (str.substring(index, index + 1).equals(" "))
						break;
					output += str.charAt(index);
				}
			}
			str = str.substring(index);
			return output;
		}
	}
}
