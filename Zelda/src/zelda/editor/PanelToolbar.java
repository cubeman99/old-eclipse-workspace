package zelda.editor;

import java.awt.Color;
import java.util.ArrayList;
import zelda.common.geometry.Point;
import zelda.editor.gui.Button;
import zelda.editor.gui.Panel;
import zelda.editor.tools.EditorTool;
import zelda.editor.tools.ToolDraw;
import zelda.editor.tools.ToolEyedrop;
import zelda.editor.tools.ToolSelection;


public class PanelToolbar extends Panel {
	private ArrayList<Button> buttons;

	public Button buttonSelect;
	public Button buttonDraw;
	public Button buttonEyedrop;
	public Button buttonWarpLinker;

	private EditorTool[] tools;
	private int toolIndex;



	// ================== CONSTRUCTORS ================== //

	public PanelToolbar(Editor editor) {
		super(editor);
		draggable = false;
		backgroundColor = new Color(224, 224, 224);
		buttons = new ArrayList<Button>();

		buttonDraw = addButton("Draw Tool", 6, 2).toggle();
		buttonSelect = addButton("Select Tool", 3, 2);
		buttonEyedrop = addButton("Eyedrop Tool", 4, 2);
		// TODO: buttonWarpLinker = addButton("Warp Point Linker", 5, 2);
		// TODO: buttonPropertyBrush = addButton(
		
		
		tools = new EditorTool[] {new ToolDraw(editor),
				new ToolSelection(editor), new ToolEyedrop(editor)};
		toolIndex = 0;
	}



	// =================== ACCESSORS =================== //

	public EditorTool getTool() {
		return tools[toolIndex];
	}


	// ==================== MUTATORS ==================== //

	public void setToolIndex(int toolIndex) {
		if (this.toolIndex != toolIndex) {
			getTool().onEnd();
			this.toolIndex = toolIndex;
			getTool().onStart();

			buttons.get(toolIndex).setToggled(true);
			for (int i = 0; i < buttons.size(); i++) {
				if (i != toolIndex)
					buttons.get(i).setToggled(false);
			}
		}
	}

	private Button addButton(String text, int sourceX, int sourceY) {
		Button b = new Button(this, text, new Point(sourceX, sourceY),
				new Point(4 + (28 * buttons.size()), 4));
		buttons.add(b);
		b.setToggles(true);
		return b;
	}



	// ================ IMPLEMENTATIONS ================ //

	@Override
	public void update() {
		super.update();

		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).update();
			if (buttons.get(i).isPressed())
				setToolIndex(i);
		}

		if (!getTool().isBusy()) {
			for (int i = 0; i < tools.length; i++) {
				if (tools[i].getHotkey().pressed())
					setToolIndex(i);
			}
		}
	}

	@Override
	public void draw() {
		for (Button b : buttons)
			b.draw();
	}
}
