package base.game;

import base.engine.core.CoreEngine;

public class Main {
	public static void main(String[] args)
	{
		CoreEngine engine = new CoreEngine(1000, 1000, 60, new TestGame());
		engine.createWindow("3D Game Engine");
		engine.start();
	}
}
