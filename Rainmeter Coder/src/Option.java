import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;
import com.sun.org.omg.SendingContext.CodeBasePackage.ValueDescSeqHelper;



public class Option {
	private ArrayList<Option> listOptions;
	private String variableName;
	private String variableTitle;
	private String description;
	private String value;
	private String defaultValue;
	private Color color;
	private boolean isColor;
	private boolean isVariableColor;
	private int lineIndex;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Option(ArrayList<Option> listOptions, int lineIndex, String variableName, String value) {
		this.listOptions     = listOptions;
		this.variableName    = variableName;
		this.value           = value;
		this.defaultValue    = "";
		this.variableTitle   = variableName;
		this.description     = "";
		this.lineIndex       = lineIndex;
		this.isColor         = false;
		this.isVariableColor = false;
		this.color           = processColor();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public String getVariableName() {
		return variableName;
	}
	
	public String getVariableTitle() {
		return variableTitle;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getValue() {
		return value;
	}
	
	public int getLineIndex() {
		return lineIndex;
	}
	
	public Color getColor() {
		if (color == null)
			return null;
		return new Color(color.getRed(), color.getGreen(), color.getBlue());
	}
	
	public boolean isColor() {
		return isColor;
	}
	
	public String getLineString() {
		return (variableName + "=" + value);
	}
	
	private Color searchVariableColor(String varName) {
		for (int i = 0; i < listOptions.size(); i++) {
			Option opt = listOptions.get(i);
			if (opt != this && opt.getVariableName().equals(varName)) {
				return opt.getColor();
			}
		}
		
		return null;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setVariableTitle(String variableTitle) {
		this.variableTitle = variableTitle;
		if (variableTitle.toLowerCase().contains("color"))
			isColor = true;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setValue(String value) {
		this.value           = value;
		this.isColor         = false;
		this.isVariableColor = false;
		this.color           = processColor();
	}
	
	public void setColor(Color color) {
		this.color = color;
		this.value = color.getRed() + "," + color.getGreen() + ","
				+ color.getBlue() + "," + color.getAlpha();
	}
	
	public void setToDefault() {
		setValue(defaultValue);
	}
	
	public void processDefault(String defLine) {
		defaultValue = "";
		for (int i = 0; i < defLine.length(); i++) {
			if (defLine.charAt(i) == '=' && i + 1 < defLine.length())
				defaultValue = defLine.substring(i + 1);
		}
	}
	
	private Color processColor() {
		int index = 0;
		int[] vars = new int[4];
		String variableColorName = "";
		
		for (int i = 0; i < 4; i++)
			vars[i] = 0;
		
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			
			if (c == ',') {
				if (++index >= 4)
					return null;
			}
			
			if (c >= '0' && c <= '9') {
				vars[index] = (vars[index] * 10) + (c - '0');
				if (vars[index] > 255)
					vars[index] = 255;
			}
			else if (c != '#')
				variableColorName += c;
		}
		if (index < 2) {
			Color col = searchVariableColor(variableColorName);
			if (col != null)
				isColor = true;
			return col;
		}
			
		isColor = true;
		value   = vars[0] + "," + vars[1] + "," + vars[2];
		if (index == 3)
			value += "," + vars[3];
		return new Color(vars[0], vars[1], vars[2], vars[3]);
	}
}
