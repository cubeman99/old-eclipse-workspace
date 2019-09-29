package map.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import map.editor.tools.Tool;


/**
 * This abstract class represents an action that can happen
 * when a button either on a tool bar or in a file menu is
 * pressed. 
 * 
 * @author David Jordan
 */
public abstract class LinkedAction implements ActionListener {
	public MapEditor editor;
	public ArrayList<AbstractButton> links;
	public String[] buttonText;
	public ImageIcon[] icons;
	public String shortcutText;
	public Tool toolDependency;
	public boolean useableWhileRunning;
	public boolean useableWhileBusy;
	
	// ================== CONSTRUCTORS ================== //
	
	/** Construct a new linked action. **/
	public LinkedAction() {
		links               = new ArrayList<AbstractButton>();
		buttonText          = new String[] {};
		icons               = new ImageIcon[] {};
		shortcutText        = "";
		editor              = null;
		toolDependency      = null;
		useableWhileRunning = false;
		useableWhileBusy    = false;
	}
	
	/** Construct this action with a enabled dependency on a given tool. **/
	public LinkedAction(Tool toolDependency) {
		this();
		this.toolDependency = toolDependency;
	}
	
	/** Construct this action with the ability to be used while the game is being tested. **/
	public LinkedAction(boolean useableWhileRunning) {
		this();
		this.useableWhileRunning = useableWhileRunning;
	}
	
	
	
	// ============= IMPLEMENTABLE METHODS ============= //
	
	/** The method that's called when this action is performed. **/
	public abstract void onPress(boolean state);

	/** Overridable method to tell whether this button is enabled. **/
	public boolean isEnabled() {return true;}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Called to tell if this action is enabled. **/
	public boolean isActionEnabled() {
		if (!useableWhileRunning && editor.editorComp.isTestRunning())
			return false;
		if (toolDependency != null)
			return ((editor.getTool() == toolDependency) && isEnabled());
		return isEnabled();
	}
	
	/** Return whether the buttons linked to this action are selected. **/
	public boolean getButtonState() {
		if (links.size() == 0)
			return false;
		return links.get(0).isSelected();
	}
	
	/** For shortcut string: return a list of modifiers in order of: 1. Ctrl, 2. Alt, 3. Shift**/
	private String getSortedShortCutModifiers(ArrayList<String> modifiers) {
		if (modifiers.size() == 0)
			return "";
		if (modifiers.size() == 1)
			return modifiers.get(0) + "+";
		if (modifiers.size() == 3)
			return ("Ctrl+Alt+Shift+");
		
		if (modifiers.contains("Ctrl") && modifiers.contains("Shift"))
			return ("Ctrl+Shift+");
		if (modifiers.contains("Ctrl") && modifiers.contains("Alt"))
			return ("Ctrl+Alt+");
		if (modifiers.contains("Alt") && modifiers.contains("Shift"))
			return ("Alt+Shift+");
		
		return "";
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Set whether this action can be used while a tool is busy. **/
	public LinkedAction setUseableWhileBusy(boolean useableWhileBusy) {
		this.useableWhileBusy = useableWhileBusy;
		return this;
	}
	
	/** Set whether this action can be used while the game is running. **/
	public LinkedAction setUseableWhileRunning(boolean useableWhileRunning) {
		this.useableWhileRunning = useableWhileRunning;
		return this;
	}
	
	/** Link a button to this action. **/
	public void addLink(AbstractButton link) {
		links.add(link);
	}
	
	/** Check if this action is enabled and enable/disable the buttons if necesary. **/
	public void refreshEnabled() {
		if (links.size() == 0)
			return;
		
		boolean enabled = isActionEnabled();
		for (AbstractButton link : links) {
			boolean linkEnabled = link.isEnabled();
			if (linkEnabled != enabled) {
				link.setEnabled(enabled);
			}
		}
	}
	
	/** Set the text if it is a on/off type of text (example: "Show Grid|Hide Grid") **/
	public void setText(String text) {
		if (!text.contains("|"))
			return;
		int index = text.indexOf('|');
		if (index < 0)
			return;
		buttonText = new String[2];
		buttonText[0] = text.substring(0, index);
		buttonText[1] = text.substring(index + 1);
	}
	
	/** Create a string representing the keyboard shortcut for this action. **/
	public void buildShortcutText(String acceleratorString) {
		ArrayList<String> modifiers = new ArrayList<String>();
		acceleratorString = acceleratorString.toLowerCase();
		
		boolean first = true;
		String str    = "";
		for (int i = 0; i < acceleratorString.length(); i++) {
			char c = acceleratorString.charAt(i);
			
			if (c == ' ') {
				if (!str.equalsIgnoreCase("pressed")) {
					boolean before = false;
					if (str.equals("ctrl"))
						before = true;
					else {
						if (modifiers.size() > 0) {
							if (modifiers.get(0).equals("shift"))
								before = true;
						}
					}
					if (before)
						modifiers.add(0, str);
					else
						modifiers.add(str);
				}
				first = true;
				str   = "";
			}
			else {
				if (c == '_') {
					first = true;
					str  += " ";
				}
				else if (first) {
					first = false;
					str  += ("" + c).toUpperCase();
				}
				else
					str += c;
			}
		}
		
		shortcutText = getSortedShortCutModifiers(modifiers) + str;
	}
	
	/** Set the two icons that this button cycles through. **/
	public void setIcons(ImageIcon img1, ImageIcon img2) {
		icons = new ImageIcon[2];
		icons[0] = img1;
		icons[1] = img2;
	}
	
	/** Give all buttons the same state and text. **/
	public void unifyLinks() {
		if (links.size() == 0)
			return;
		
		boolean selected = links.get(0).isSelected();
		for (AbstractButton link : links) {
			link.setSelected(selected);
			if (buttonText.length == 2) {
				if (link instanceof JMenuItem)
					link.setText(buttonText[selected ? 1 : 0]);
				else
					link.setToolTipText(buttonText[selected ? 1 : 0] + (shortcutText.equals("") ? "" : " [" + shortcutText + "]"));
			}
			if (icons.length == 2) {
				if (!(link instanceof JMenuItem))
					link.setIcon(icons[selected ? 1 : 0]);
			}
		}
	}
	
	
	
	// =============== INHERITED METHODS =============== //
	
	@Override
	/** Update this buttons action and links when pressed. **/
	public void actionPerformed(ActionEvent e) {
		if (!useableWhileBusy && editor.getTool().isBusy())
			return;
		
		boolean selected = true;
		
		AbstractButton source = (AbstractButton) e.getSource();
		selected = source.isSelected();
		
		for (AbstractButton link : links) {
			if (link != source) {
				link.setSelected(selected);
			}
			if (buttonText.length == 2) {
				if (link instanceof JMenuItem)
					link.setText(buttonText[selected ? 1 : 0]);
				else
					link.setToolTipText(buttonText[selected ? 1 : 0] + (shortcutText.equals("") ? "" : " [" + shortcutText + "]"));
					
			}
			if (icons.length == 2) {
				if (!(link instanceof JMenuItem))
					link.setIcon(icons[selected ? 1 : 0]);
			}
		}
		
		this.onPress(selected);
	}
}
