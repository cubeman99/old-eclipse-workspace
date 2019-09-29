package zelda.game.control.script;

import java.util.ArrayList;
import zelda.common.util.Colors;
import zelda.common.util.Direction;

public class ScriptConstants {
	private ArrayList<Constant> constants;


	public ScriptConstants() {
		constants = new ArrayList<Constant>();
		
		// Direction Constants:
		addConstant("LEFT",  Direction.LEFT);
		addConstant("UP",    Direction.UP);
		addConstant("RIGHT", Direction.RIGHT);
		addConstant("DOWN",  Direction.DOWN);
		addConstant("EAST",  Direction.EAST);
		addConstant("NORTH", Direction.NORTH);
		addConstant("WEST",  Direction.WEST);
		addConstant("SOUTH", Direction.SOUTH);
		
		// Color Constants:
		addConstant("RED",    Colors.RED);
		addConstant("BLUE",   Colors.BLUE);
		addConstant("GREEN",  Colors.GREEN);
		addConstant("YELLOW", Colors.YELLOW);
		addConstant("ORANGE", Colors.ORANGE);
		addConstant("GOLD",   Colors.GOLD);
		
		// Other Constants.
		addConstant("NULL", "");
	}
	
	public Constant getConstantIgnoreCase(String name) {
		return getConstant(name, true);
	}

	public Constant getConstant(String name) {
		return getConstant(name, false);
	}
	
	public Constant getConstant(String name, boolean ignoreCase) {
		String constName = name;
		while (constName.endsWith(";") || constName.endsWith("]"))
			constName = constName.substring(0, constName.length() - 1);
		for (int i = 0; i < constants.size(); i++) {
			if (ignoreCase && constants.get(i).getName().equalsIgnoreCase(constName))
				return constants.get(i);
			if (!ignoreCase && constants.get(i).getName().equals(constName))
				return constants.get(i);
		}
		return null;
	}
	
	private void addConstant(String name, String value) {
		constants.add(new BasicConstant(name, value));
	}
	
	private void addConstant(String name, int value) {
		constants.add(new BasicConstant(name, value));
	}
	
	private void addConstant(Constant c) {
		constants.add(c);
	}
}
