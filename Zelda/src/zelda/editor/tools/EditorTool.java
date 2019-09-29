package zelda.editor.tools;

import zelda.editor.Editor;
import zelda.main.Hotkey;


public abstract class EditorTool {
	protected Editor editor;
	protected String name;
	protected Hotkey hotkey;



	// ================== CONSTRUCTORS ================== //

	public EditorTool(Editor editor, String name, Hotkey hotkey) {
		this.editor = editor;
		this.name = name;
		this.hotkey = hotkey;
	}



	// =============== ABSTRACT METHODS =============== //

	public abstract boolean isBusy();

	public abstract void onStart();

	public abstract void onEnd();

	public abstract void update();

	public abstract void draw();



	// =================== ACCESSORS =================== //

	public Hotkey getHotkey() {
		return hotkey;
	}

	public String getName() {
		return name;
	}

	public Editor getEditor() {
		return editor;
	}
}
