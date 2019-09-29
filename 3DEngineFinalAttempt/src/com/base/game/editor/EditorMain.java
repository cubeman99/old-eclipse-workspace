package com.base.game.editor;

import com.base.engine.core.CoreEngine;

public class EditorMain {
	public static void main(String[] args) {
		CoreEngine engine = new CoreEngine(800, 600, 60, new Editor());
		engine.createWindow("David's 3D Game Editor");
		engine.start();
	}
}
