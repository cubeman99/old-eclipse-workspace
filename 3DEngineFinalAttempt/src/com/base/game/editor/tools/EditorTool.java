package com.base.game.editor.tools;

import com.base.game.editor.Editor;

public abstract class EditorTool {
	protected Editor editor;
	protected String name;
	

	
	// ================== CONSTRUCTORS ================== //

	public EditorTool(Editor editor, String name) {
		this.editor = editor;
		this.name   = name;
	}
	
	
	
	// =============== ABSTRACT METHODS =============== //
	
	public abstract void update();
	
	public abstract void draw();
	

	// =================== ACCESSORS =================== //
	
	public Editor getEditor() {
		return editor;
	}
	
	public String getName() {
		return name;
	}
}
