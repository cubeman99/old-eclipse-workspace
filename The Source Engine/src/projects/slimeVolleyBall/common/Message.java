package projects.slimeVolleyBall.common;

import java.util.ArrayList;

public class Message {
	public static final char STRING_OPENER = '[';
	public static final char STRING_CLOSER = ']';
	private String text;
	private String command;
	private ArrayList<String> args;

	public Message(String text) {
		this.text = text;
		this.args = new ArrayList<String>();
		this.decodeText();
	}
	/*
	public Message(String command, Object[] args) {
		this.command = command;
		this.args = new ArrayList<String>();
		if (args != null) {
			for (int i = 0; i < args.length; i++)
				addObjectArg(args[i]);
		}
		this.compileText();
	}*/

	public Message(String command, Object arg0) {
		this.command = command;
		this.args = new ArrayList<String>();
		addObjectArg(arg0);
		this.compileText();
	}
	
	public Message(String command, Object arg0, Object arg1) {
		this.command = command;
		this.args = new ArrayList<String>();
		addObjectArg(arg0);
		addObjectArg(arg1);
		this.compileText();
	}
	
	public Message(String command, Object arg0, Object arg1, Object arg2) {
		this.command = command;
		this.args = new ArrayList<String>();
		addObjectArg(arg0);
		addObjectArg(arg1);
		addObjectArg(arg2);
		this.compileText();
	}
	
	public Message(String command, Object arg0, Object arg1, Object arg2, Object arg4) {
		this.command = command;
		this.args = new ArrayList<String>();
		addObjectArg(arg0);
		addObjectArg(arg1);
		addObjectArg(arg2);
		addObjectArg(arg4);
		this.compileText();
	}
	
	private void addObjectArg(Object obj) {
		if (obj instanceof String) {
			addStringArg((String) obj);
		}
		else if (obj instanceof Integer) {
			addIntArg((Integer) obj);
		}
		else if (obj instanceof Double) {
			addDoubleArg((Double) obj);
		}
		else if (obj instanceof Boolean) {
			addBooleanArg((Boolean) obj);
		}
	}
	
	public void addStringArg(String str) {
		args.add(STRING_OPENER + str + STRING_CLOSER);
		compileText();
	}

	public void addIntArg(int x) {
		args.add("" + x);
		compileText();
	}

	public void addDoubleArg(double x) {
		args.add("" + x);
		compileText();
	}

	public void addBooleanArg(boolean b) {
		if (b) {
			args.add("1");
		}
		args.add("0");
		compileText();
	}
	
	public boolean isCommand(String string) {
		return command.equals(string);
	}

	public String argString(int index) {
		return arg(index);
	}

	public int argInt(int index) {
		return Integer.parseInt(arg(index));
	}

	public double argDouble(int index) {
		return Double.parseDouble(arg(index));
	}
	
	public boolean argBoolean(int index) {
		int b = Integer.parseInt(arg(index));
		if (b == 1)
			return true;
		return false;
	}

	private String arg(int index) {
		return args.get(index);
	}
	
	private void compileText() {
		text = command;
		for (int i = 0; i < args.size(); i++) {
			text += " " + args.get(i);
		}
	}

	private void decodeText() {
		command = "";
		boolean commandRead   = false;
		boolean readingString = false;
		String buildStr       = "";
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (!commandRead) {
				if (c == ' ')
					commandRead = true;
				else
					command += c;
			}
			else {
				if (readingString) {
					if (c == STRING_CLOSER) {
						args.add(buildStr);
						readingString = false;
						buildStr = "";
					}
					else {
						buildStr += c;
					}
				}
				else {
					if (c == STRING_OPENER && buildStr.equals("")) {
						readingString = true;
					}
					else if (c == ' ' && !buildStr.equals("")) {
						args.add(buildStr);
						buildStr = "";
					}
					else if (c != ' '){
						buildStr += c;
						if (i == text.length() - 1)
							args.add(buildStr);
					}
				}
			}
		}

	}
	
	public String getText() {
		return text;
	}
	
	public void print() {
		System.out.println(text);
		System.out.println("command = " + command);
		for (int i = 0; i < args.size(); i++) {
			System.out.println("args[" + i + "] = " + args.get(i));
		}
	}
	
	public void printText() {
		System.out.println(text);
	}
}