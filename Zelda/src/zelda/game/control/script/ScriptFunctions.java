package zelda.game.control.script;

import java.util.ArrayList;
import zelda.common.properties.Properties;
import zelda.common.properties.Property;
import zelda.common.properties.PropertyHolder;
import zelda.common.util.GMath;
import zelda.game.control.text.Message;
import zelda.game.entity.Entity;
import zelda.game.entity.FrameEntity;
import zelda.game.entity.object.dungeon.door.ObjectBasicDoor;
import zelda.game.monster.Monster;
import zelda.game.world.Frame;
import zelda.game.world.tile.FrameTileObject;
import zelda.game.world.tile.ObjectTile;


public class ScriptFunctions {
	private ArrayList<Function> functions;


	public ScriptFunctions() {
		functions = new ArrayList<Function>();



		// ============== GENERIC FUNCTIONS ================ //

		// log: <message>
		addFunction(new Function("log", 1) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				System.out.println(Script.parseString(args.get(0)));
				return "";
			}
		});

		// read: <message>
		// Read the given text message.
		addFunction(new Function("read", 1) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				frame.getGame().readMessage(new Message((Script.parseString(args.get(0)))));
				return "";
			}
		});



		// ============== BOOLEAN LOGIC ================ //

		// equals: <a> <b> <...>
		addFunction(new Function("equals", 2) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				for (int i = 1; i < args.size(); i++) {
					if (!args.get(i).equals(args.get(0)))
						return "false";
				}
				return "true";
			}
		});

		// notequals: <a> <b>
		addFunction(new Function("notequals", 2) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				return ("" + !args.get(0).equals(args.get(1)));
			}
		});

		// not: <a>
		addFunction(new Function("not", 1) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				return ("" + !Boolean.parseBoolean(args.get(0)));
			}
		});

		// and: <a> <b> <...>
		addFunction(new Function("and", 2, true) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				boolean output = true;
				for (int i = 0; i < args.size(); i++)
					output = output && Boolean.parseBoolean(args.get(i));
				return ("" + output);
			}
		});

		// or: <a> <b> <...>
		addFunction(new Function("or", 2, true) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				boolean output = false;
				for (int i = 0; i < args.size(); i++)
					output = output || Boolean.parseBoolean(args.get(i));
				return ("" + output);
			}
		});
		
		// ifthen: <condition> <success_script>
		addFunction(new Function("ifthen", 2) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				if (Boolean.parseBoolean(args.get(0)))
					return Script.execute("", Script.parseString(args.get(1)),
							holder, frame);
				return "";
			}
		});
		
		// ifelse: <condition> <success_script> <failure_script>
		addFunction(new Function("ifelse", 3) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				if (Boolean.parseBoolean(args.get(0)))
					return Script.execute("", Script.parseString(args.get(1)),
							holder, frame);
				else {
					return Script.execute("", Script.parseString(args.get(2)),
							holder, frame);
				}
			}
		});



		// ================= ARITHMATIC =================== //

		// rand!
		addFunction(new Function("rand", 0) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				return ("" + GMath.random.nextDouble());
			}
		});



		// ================== PROPERTIES ==================== //
		
		
		// setparonly: <target> <prop_name> <prop_value>
		// Set the property for the tile of the given target.
		addFunction(new Function("setparonly", 3) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				ArrayList<PropertyHolder> targets = getTargets(args.get(0),
						holder, frame);
				for (int i = 0; i < targets.size(); i++) {
					if (targets.get(i) instanceof FrameEntity) {
						PropertyHolder source = ((FrameEntity) targets.get(i))
								.getObjectData().getSource();
						if (source != null) {
							source.getProperties().set(
									Script.parseString(args.get(1)),
									Script.parseString(args.get(2)));
							Property tp = source.getProperties().getProperty(
									Script.parseString(args.get(1)));
							source.onChangeProperty(tp);
						}
					}
				}
				return "";
			}
		});
		
		
		// setpar: <target> <prop_name> <prop_value>
		// Set the property for the tile of the given target.
		addFunction(new Function("setpar", 3) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				ArrayList<PropertyHolder> targets = getTargets(args.get(0),
						holder, frame);
				for (int i = 0; i < targets.size(); i++) {
					if (targets.get(i) instanceof FrameEntity) {
						PropertyHolder source = ((FrameEntity) targets.get(i))
								.getObjectData().getSource();
						if (source != null) {
							source.getProperties().set(
									Script.parseString(args.get(1)),
									Script.parseString(args.get(2)));
							Property tp = source.getProperties().getProperty(
									Script.parseString(args.get(1)));
							source.onChangeProperty(tp);
						}
					}
					targets.get(i).getProperties().set(
							Script.parseString(args.get(1)),
							Script.parseString(args.get(2)));
					Property p = targets.get(i).getProperties()
							.getProperty(Script.parseString(args.get(1)));
					targets.get(i).onChangeProperty(p);
				}
				return "";
			}
		});

		// set: <target> <prop_name> <prop_value>
		addFunction(new Function("set", 3) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				ArrayList<PropertyHolder> targets = getTargets(args.get(0),
						holder, frame);
				for (int i = 0; i < targets.size(); i++) {
					targets.get(i)
							.getProperties()
							.set(Script.parseString(args.get(1)),
									Script.parseString(args.get(2)));
					Property p = targets.get(i).getProperties()
							.getProperty(Script.parseString(args.get(1)));
					targets.get(i).onChangeProperty(p);
				}
				return "";
			}
		});

		// get: <target> <prop_name>
		addFunction(new Function("get", 2) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				PropertyHolder target = getTarget(args.get(0), holder, frame);
				if (target != null)
					return target.getProperties().get(
							Script.parseString(args.get(1)), "");
				return "";
			}
		});

		// get: <target> <prop_name> <default_value>
		addFunction(new Function("get", 3) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				PropertyHolder target = getTarget(args.get(0), holder, frame);
				if (target != null)
					return target.getProperties().get(
							Script.parseString(args.get(1)),
							Script.parseString(args.get(2)));
				return args.get(2);
			}
		});

		// exists: <target> <prop_name>
		addFunction(new Function("exists", 2) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				PropertyHolder target = getTarget(args.get(0), holder, frame);
				if (target != null)
					return ""
							+ target.getProperties().exists(
									Script.parseString(args.get(1)));
				return "false";
			}
		});

		// trigger: <target> <event_name> <args...>
		addFunction(new Function("trigger", 3, true) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				ArrayList<PropertyHolder> targets = getTargets(args.get(0),
						holder, frame);
				for (int i = 0; i < targets.size(); i++) {
					String out = holder.getProperties().script(
							Script.parseString(args.get(1)), targets.get(i),
							frame);
					if (i == targets.size() - 1)
						return out;
				}
				return "";
			}
		});

		// func: <target> <func_name> <args...>
		addFunction(new Function("func", 2, true) {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame)
			{
				ArrayList<PropertyHolder> targets = getTargets(args.get(0),
						holder, frame);
				for (int i = 0; i < targets.size(); i++) {
					if (targets.get(i) instanceof FrameEntity){
						// TODO: arguments...
						return ((FrameEntity) targets.get(i)).getObjectData()
								.callFunction(Script.parseString(args.get(1)),
								new ArrayList<String>(), targets.get(i), frame);
					}
				}
				return "";
			}
		});

		// open_doors!
		addFunction(new Function("open_doors") {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				ArrayList<Entity> entities = frame.getEntities();
				for (int i = 0; i < entities.size(); i++) {
					Entity e = entities.get(i);
					if (e instanceof ObjectBasicDoor)
						((ObjectBasicDoor) e).open();
				}
				return "";
			}
		});
		
		// close_doors!
		addFunction(new Function("close_doors") {
			public String execute(ArrayList<String> args,
					PropertyHolder holder, Frame frame) {
				ArrayList<Entity> entities = frame.getEntities();
				for (int i = 0; i < entities.size(); i++) {
					Entity e = entities.get(i);
					if (e instanceof ObjectBasicDoor)
						((ObjectBasicDoor) e).close();
				}
				return "";
			}
		});

		// count: <target>
		addFunction(new Function("count", 1) {
			public String execute(ArrayList<String> args, PropertyHolder holder, Frame frame) {
				return "" + getTargets(Script.parseString(args.get(0)), holder,
								frame).size();
			}
		});

		// no_monsters!
		addFunction(new Function("no_monsters") {
			public String execute(ArrayList<String> args, PropertyHolder holder, Frame frame) {
				ArrayList<Entity> entities = frame.getEntities();
				for (int i = 0; i < entities.size(); i++) {
					if (entities.get(i) instanceof Monster)
						return "false";
				}
				return "true";
			}
		});
	}

	public static PropertyHolder getTarget(String targetName, PropertyHolder holder,
			Frame frame) {
		if (targetName.equals("this"))
			return holder;

		ArrayList<Entity> entities = frame.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (isValidTarget(targetName, e))
				return (PropertyHolder) e;
		}

		return null;
	}

	public static ArrayList<PropertyHolder> getTargets(String targetName,
			PropertyHolder holder, Frame frame) {
		ArrayList<PropertyHolder> targets = new ArrayList<PropertyHolder>();

		if (targetName.equals("this"))
			targets.add(holder);
		else if (targetName.startsWith("$")) {
			targetName = targetName.substring(1);
			for (int i = 0; i < frame.getObjectTiles().size(); i++) {
				ObjectTile t = frame.getObjectTiles().get(i);
				if (isValidTargetClassName(targetName, t.getFrameObject())
					|| isValidTargetName(targetName, t.getProperties().get("id")))
					targets.add(t);
			}
		}
		else {
			ArrayList<Entity> entities = frame.getEntities();
			for (int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				if (isValidTarget(targetName, e))
					targets.add((PropertyHolder) e);
			}
		}
		return targets;
	}
	
	private static boolean isValidTarget(String targetName, Object e) {
		if (e instanceof PropertyHolder && ((PropertyHolder) e).getProperties() != null) {
			Properties p = ((PropertyHolder) e).getProperties();
			
			if (isValidTargetName(targetName, p.get("id", "")))
				return true;
			if (isValidTargetClassName(targetName, e))
					return true;
		}
		return false;
	}
	
	private static boolean isValidTargetClassName(String targetName, Object obj) {
		Class<? extends Object> cls = obj.getClass();
		while (cls != null) {
			if (isValidTargetName(targetName, cls.getSimpleName()))
				return true;
			if (Entity.class.isAssignableFrom(cls))
				cls = (Class<? extends Object>) cls.getSuperclass();
			else
				cls = null;
		}
		return false;
	}
	
	private static boolean isValidTargetName(String targetName, String testName) {
		if (targetName.endsWith("*")) {
			return testName.startsWith(targetName.substring(
					0, targetName.length() - 1));
		}
		return targetName.equals(testName);
	}

	private Function addFunction(Function func) {
		functions.add(func);
		return func;
	}

	public int getNumFunctions() {
		return functions.size();
	}

	public Function getFunction(int index) {
		return functions.get(index);
	}
}
