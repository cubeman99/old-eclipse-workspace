package map.editor.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import common.GMath;
import map.editor.MapEditor;


public class Toolbar {
	private ArrayList<JToggleButton> buttons;
	private ArrayList<Tool> tools;
	private MapEditor editor;
	private int toolIndex;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Toolbar(MapEditor editor) {
		this.editor  = editor;
		this.tools   = new ArrayList<Tool>();
		this.buttons = new ArrayList<JToggleButton>();
		
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	/** Get the current tool index. **/
	public int getToolIndex() {
		return toolIndex;
	}
	
	/** Get the current tool being used. **/
	public Tool getTool() {
		return tools.get(toolIndex);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	/** Set the index of the tool to be used. **/
	public Tool setToolIndex(int newToolIndex) {
		getTool().terminate();
		toolIndex = GMath.getWrappedValue(newToolIndex, tools.size());
		for (int i = 0; i < tools.size(); i++)
			buttons.get(i).setSelected(this.toolIndex == i);
		getTool().initialize();
		return getTool();
	}
	
	/** Set the tool to use. **/
	public void setToolIndex(Tool newTool) {
		int index = tools.indexOf(newTool);
		if (index >= 0)
			setToolIndex(index);
	}
	
	/** Create the different tools for the tool bar. **/
	public void createTools(Tool... tools) {
		for (int i = 0; i < tools.length; i++) {
			Tool t = tools[i];
			this.tools.add(t);
			final JToggleButton button = new JToggleButton(t.icon);
//			final JToggleButton button = new JToggleButton("TOOL");
			buttons.add(button);
			
			button.setToolTipText(t.name);
    		button.setFocusable(false);
			
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					int index       = buttons.indexOf(button);
					JToggleButton b = buttons.get(index);
					if (b.isSelected()) {
						setToolIndex(index);
						editor.editorComp.statusTool.setText(getTool().name);
					}
					else
						b.setSelected(true);
				}
			});
			
			setToolIndex(0);
		}
	}
	
	/** Add the tool bar buttons to the frame. **/
	public void addToolsToToolbar(JToolBar t) {
		for (int i = 0; i < tools.size(); i++) {
			t.add(buttons.get(i));
		}
	}
}
