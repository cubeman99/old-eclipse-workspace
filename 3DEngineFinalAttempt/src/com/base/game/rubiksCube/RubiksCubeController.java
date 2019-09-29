package com.base.game.rubiksCube;

import java.util.ArrayList;
import com.base.engine.common.GMath;
import com.base.engine.core.Keyboard;
import com.base.engine.entity.SceneObject;

public class RubiksCubeController extends SceneObject {
	private RubiksCube cube;
	private ArrayList<ControlKey> controls;
	private Algorithm solveAlgorithm;
	private RubiksCube cubePreSolve;
	
	
	
	public RubiksCubeController(RubiksCube cube) {
		this.cube = cube;
		controls = new ArrayList<ControlKey>();

		addControlKey(Keyboard.KEY_LEFT,  0, RubiksCube.Y_AXIS, true);
		addControlKey(Keyboard.KEY_RIGHT, 0, RubiksCube.Y_AXIS, false);
		addControlKey(Keyboard.KEY_UP,    0, RubiksCube.X_AXIS, true);
		addControlKey(Keyboard.KEY_DOWN,  0, RubiksCube.X_AXIS, false);
		
		addControlKey(Keyboard.KEY_I, RubiksCube.RIGHT, RubiksCube.X_AXIS, true);
		addControlKey(Keyboard.KEY_K, RubiksCube.RIGHT, RubiksCube.X_AXIS, false);
		addControlKey(Keyboard.KEY_J, RubiksCube.UP,    RubiksCube.Y_AXIS, true);
		addControlKey(Keyboard.KEY_L, RubiksCube.UP,    RubiksCube.Y_AXIS, false);
		addControlKey(Keyboard.KEY_O, RubiksCube.FRONT, RubiksCube.Z_AXIS, true);
		addControlKey(Keyboard.KEY_U, RubiksCube.FRONT, RubiksCube.Z_AXIS, false);
		
		addControlKey(Keyboard.KEY_T, RubiksCube.MIDDLE, RubiksCube.X_AXIS, true);
		addControlKey(Keyboard.KEY_G, RubiksCube.MIDDLE, RubiksCube.X_AXIS, false);
		addControlKey(Keyboard.KEY_F, RubiksCube.MIDDLE, RubiksCube.Y_AXIS, true);
		addControlKey(Keyboard.KEY_H, RubiksCube.MIDDLE, RubiksCube.Y_AXIS, false);
		addControlKey(Keyboard.KEY_Y, RubiksCube.MIDDLE, RubiksCube.Z_AXIS, true);
		addControlKey(Keyboard.KEY_R, RubiksCube.MIDDLE, RubiksCube.Z_AXIS, false);
		
		addControlKey(Keyboard.KEY_W, RubiksCube.LEFT,  RubiksCube.X_AXIS, true);
		addControlKey(Keyboard.KEY_S, RubiksCube.LEFT,  RubiksCube.X_AXIS, false);
		addControlKey(Keyboard.KEY_A, RubiksCube.DOWN,  RubiksCube.Y_AXIS, true);
		addControlKey(Keyboard.KEY_D, RubiksCube.DOWN,  RubiksCube.Y_AXIS, false);
		addControlKey(Keyboard.KEY_E, RubiksCube.BACK,  RubiksCube.Z_AXIS, true);
		addControlKey(Keyboard.KEY_Q, RubiksCube.BACK,  RubiksCube.Z_AXIS, false);
		
		addControlKey(Keyboard.KEY_NUMPAD1, "Sune", "R U R' U R U2 R'");
		addControlKey(Keyboard.KEY_NUMPAD2, "Antisune", "R U2 R' U' R U' R'");
		addControlKey(Keyboard.KEY_NUMPAD3, "Move3", "F R U R' U' F'");
		addControlKey(Keyboard.KEY_NUMPAD4, "Move4", "U R U' L' U R' U' L");
		addControlKey(Keyboard.KEY_NUMPAD5, "Final", "R' D' R D R' D' R D");
		
		addControlKey(Keyboard.KEY_NUMPAD6, "Checker", "M2 S2 E2");
		addControlKey(Keyboard.KEY_NUMPAD7, "Cube in Cube", "F L F U' R U F2 L2 U' L' B D' B' L2 U");
		addControlKey(Keyboard.KEY_NUMPAD8, "Cubeception", "U' L' U' F' R2 B' R F U B2 U B' L U' F U R F'");
//		addControlKey(Keyboard.KEY_NUMPAD9, "Six Spots", "U D' R L' F B' U D'");
		
		addControlKey(Keyboard.KEY_NUMPAD9, "Tom Parks' Pattern", "L U F2 R L' U2 B' U D B2 L F B' R' L F' R");
		
		addControlKey(Keyboard.KEY_NUMPAD0, "Test", "F F F' F' R R R' R' U U U' U' L L L' L' B B B' B' D D D' D'");
	}
	
	@Override
	public void update(float delta) {
		boolean whole = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
		
		for (int i = 0; i < controls.size(); i++) {
			ControlKey ctrl = controls.get(i);
			if (Keyboard.isKeyPressed(ctrl.key)) {
				if (ctrl.algorithm != null)
					cube.apply(ctrl.algorithm, whole);
    			else {
    				if (whole)
    					cube.apply(ctrl.move.getAxis(), 0, ctrl.move.isClockwise());
    				else
    					cube.apply(ctrl.move);
				}
				break;
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			int axis  = GMath.random.nextInt(3);
			int layer = GMath.random.nextInt(cube.getSize(axis)) + 1;
			boolean clockwise = GMath.random.nextBoolean();
			cube.apply(axis, layer, clockwise, whole);
		}

		
		if (Keyboard.isKeyDown(Keyboard.KEY_HOME)) {
			if (cube.isSolved()) {
    			// Scramble.
    			for (int i = 0; i < 100; i++) {
        			int axis  = GMath.random.nextInt(3);
        			int layer = GMath.random.nextInt(cube.getSize(axis)) + 1;
        			boolean clockwise = GMath.random.nextBoolean();
        			cube.apply(axis, layer, clockwise, true);
    			}
    			// Solve.
    			cubePreSolve = new RubiksCube(cube);
    			solveAlgorithm = new CubeSolver(cube).getSolveAlgorithm();
    			cube.apply(solveAlgorithm, true);
			}
		}
		
		
		if (Keyboard.isKeyPressed(Keyboard.KEY_TAB)) {
//			Algorithm alg = new Algorithm("", "R R R U L' F F' F2 L U' L' U U' B", new Notation());
//			alg.simplify();
//			System.out.println(new Notation().getNotation(CubeSolver.ALG_EDGE_FIT_RIGHT));
			System.out.println(new Notation().getNotation(CubeSolver.ALG_ORIENT_FINAL));
		}
		
		if (Keyboard.isKeyPressed(Keyboard.KEY_BACKSPACE)) {
			cube.init(cubePreSolve);
		}
		if (Keyboard.isKeyPressed(Keyboard.KEY_BACKSLASH)) {
			cube.apply(solveAlgorithm);
		}
		if (Keyboard.isKeyPressed(Keyboard.KEY_ENTER)) {
			cubePreSolve = new RubiksCube(cube);
			solveAlgorithm = new CubeSolver(cube).getSolveAlgorithm();
			cube.apply(solveAlgorithm, Keyboard.isKeyDown(Keyboard.KEY_LCONTROL));
		}
	}
	
	private void addControlKey(int key, String name, String algorithm) {
		controls.add(new ControlKey(key, name, algorithm));
	}
	
	private void addControlKey(int key, int layer, int axis, boolean clockwise) {
		controls.add(new ControlKey(key, new Move(axis, layer, clockwise)));
	}
	
	private class ControlKey {
		public int key;
		public Move move;
		public Algorithm algorithm;
		
		public ControlKey(int key, Move move) {
			this.key       = key;
			this.move      = move;
			this.algorithm = null;
		}
		
		public ControlKey(int key, String name, String algorithm) {
			this.key       = key;
			this.move      = null;
			this.algorithm = new Algorithm(name, algorithm, new Notation());
		}
	}
}
