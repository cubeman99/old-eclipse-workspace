package com.base.game.rubiksCube;

import com.base.engine.core.Util;

public class CubeSolver {
	private static final int X_AXIS = 0;
	private static final int Y_AXIS = 1;
	private static final int Z_AXIS = 2;
	private static final int X      = 0;
	private static final int Y      = 1;
	private static final int Z      = 2;
	private static final int NONE   = -1;
	private static final int BLUE   = 0;
	private static final int GREEN  = 1;
	private static final int WHITE  = 2;
	private static final int YELLOW = 3;
	private static final int RED    = 4;
	private static final int ORANGE = 5;
	
	private RubiksCube cube;
	private Algorithm solveAlgorithm;
	
	public static final Algorithm ALG_EDGE_FIT_RIGHT = new Algorithm("Right Fit", "U R U' R' U' F' U F", new Notation());
	public static final Algorithm ALG_EDGE_FIT_LEFT  = new Algorithm("Left Fit", "U' F' U F U R U' R'", new Notation());
	public static final Algorithm ALG_CYCLE_CROSS    = new Algorithm("Cycle Cross", "F R U R' U' F'", new Notation());
	public static final Algorithm ALG_SUNE           = new Algorithm("Sune", "R U R' U R U2 R'", new Notation());
	public static final Algorithm ALG_CYCLE_CORNERS  = new Algorithm("Cycle Corners", "U R U' L' U R' U' L", new Notation());
	public static final Algorithm ALG_ORIENT_FINAL   = new Algorithm("Final Orient", "R' D' R D R' D' R D", new Notation());
	
	public static OLLCase[] OLL_CASES = new OLLCase[58];
	public static PLLCase[] PLL_CASES = new PLLCase[22];
	
	
	public class OLLCase {
		public int[] ollCase;
		public Algorithm algorithm;
		
		public OLLCase(int[] ollCase, String algorithm) {
			this.ollCase = ollCase;
			this.algorithm = new Algorithm("", algorithm, new Notation());
		}
	}
	
	public class PLLCase {
		public int[] pllCase;
		public Algorithm algorithm;
		
		public PLLCase(int[] pllCase, String algorithm) {
			this.pllCase = pllCase;
			this.algorithm = new Algorithm("", algorithm, new Notation());
			for (int i = 0; i < 9; i++) {
				if (pllCase[i] == 0)
					pllCase[i] = i + 1;
			}
		}
	}
	
	
	public CubeSolver(RubiksCube cube) {
		this.cube = new RubiksCube(cube);
		
		if (PLL_CASES[0] == null) {
			// Solved Case.
			PLL_CASES[0]  = new PLLCase(new int[] {0,0,0, 0,0,0, 0,0,0}, "");
			
			// Corners Only.
			PLL_CASES[1]  = new PLLCase(new int[] {3,0,9, 0,0,0, 0,0,1}, "x (R' U R') D2 (R U' R') D2 R2");
			PLL_CASES[2]  = new PLLCase(new int[] {9,0,1, 0,0,0, 0,0,3}, "x R2 D2 (R U R') D2 (R U' R)");
			PLL_CASES[3]  = new PLLCase(new int[] {7,0,9, 0,0,0, 1,0,3}, "x' (R U') (R' D) (R U R' D') (R U R' D) (R U') (R' D')");
			
			// Edges Only.
			PLL_CASES[4]  = new PLLCase(new int[] {0,0,0, 8,0,4, 0,6,0}, "F2 U' L R' F2 R L' U' F2");
			PLL_CASES[5]  = new PLLCase(new int[] {0,0,0, 6,0,8, 0,4,0}, "F2 U L R' F2 R L' U F2");
			PLL_CASES[6]  = new PLLCase(new int[] {0,8,0, 6,0,4, 0,2,0}, "(M2' U) (M2' U2) (M2' U) M2'");
			PLL_CASES[7]  = new PLLCase(new int[] {0,4,0, 2,0,8, 0,6,0}, "(M2' U) (M2' U) (M' U2) (M2' U2) (M' U2)");
			
			
			// Swapping Two Adjacent Corners & Two Edges.
			PLL_CASES[8]  = new PLLCase(new int[] {3,4,1, 2,0,0, 0,0,0}, "(R' U L') U2 (R U' R') U2 (L R U')");
			PLL_CASES[9]  = new PLLCase(new int[] {0,0,9, 0,0,8, 0,6,3}, "(R U R' F') (R U R' U') (R' F) (R2 U') (R' U')");
			PLL_CASES[10] = new PLLCase(new int[] {0,0,9, 6,0,4, 0,0,3}, "(R U R' U') (R' F) (R2 U') (R' U' R U) (R' F')");
			PLL_CASES[11] = new PLLCase(new int[] {3,0,1, 0,0,8, 0,6,0}, "(R' U2) (R U2) (R' F R U R' U') (R' F' R2 U')");
			PLL_CASES[12] = new PLLCase(new int[] {0,4,9, 2,0,0, 0,0,3}, "R U R' F' R U2 R' U2 R' F R U R U2 R' U'");
			PLL_CASES[13] = new PLLCase(new int[] {0,8,9, 0,0,0, 0,2,3}, "R' U' F' (R U R' U') (R' F) (R2 U') (R' U' R U) (R' U R)");
			
			// Cycling Three Corners & Three Edges.
			PLL_CASES[14]  = new PLLCase(new int[] {3,4,7, 6,0,2, 1,0,0}, "(R2' Uw) (R' U R' U' R Uw') R2' y' (R' U R)");
			PLL_CASES[15]  = new PLLCase(new int[] {7,8,0, 2,0,0, 9,4,1}, "(R' U' R) y (R2' Uw R' U) (R U' R Uw' R2')");
			PLL_CASES[16]  = new PLLCase(new int[] {7,0,0, 6,0,8, 9,4,1}, "(R2' Uw' R U') (R U R' Uw R2) (Fw R' Fw')");
			PLL_CASES[17]  = new PLLCase(new int[] {3,4,7, 8,0,0, 1,2,0}, "(R U R') y' (R2' Uw' R U') (R' U R' Uw R2)");
			
			// Permutations Of Two Diagonal Corners & Two Edges.
			PLL_CASES[18]  = new PLLCase(new int[] {9,6,0, 0,0,2, 0,0,1}, "(R' U R' Dw') (R' F' R2 U') (R' U R' F) (R F)");
			PLL_CASES[19]  = new PLLCase(new int[] {9,8,0, 0,0,0, 0,2,1}, "(z) D (R' U) (R2 D' R D U') (R' U) (R2 D' R U' R)");
			PLL_CASES[20]  = new PLLCase(new int[] {0,8,7, 0,0,0, 3,2,0}, "(z) U' (R D') (R2' U R' D U') (R D') (R2' U R' D R')");
			PLL_CASES[21]  = new PLLCase(new int[] {9,4,0, 2,0,0, 0,0,1}, "(F R U') (R' U' R U) (R' F') (R U R' U') (R' F R F')");
		}
		
		if (OLL_CASES[0] == null) {
    		// Solved Case.
    		OLL_CASES[0]  = new OLLCase(new int[] {Y,Y,Y, Y,Y,Y, Y,Y,Y}, "");
    		
    		// Corners Correct, Edges Flipped.
    		OLL_CASES[28] = new OLLCase(new int[] {Y,Y,Y, Y,Y,X, Y,Z,Y}, "F R U R' U' F2 L' U' L U F");
    		OLL_CASES[57] = new OLLCase(new int[] {Y,Z,Y, Y,Y,Y, Y,Z,Y}, "R' U' R U M U' R' U Rw");
    		OLL_CASES[20] = new OLLCase(new int[] {Y,Z,Y, X,Y,X, Y,Z,Y}, "Rw' (R U) (R U R' U' Rw2) (R2' U) (R U') Rw'");
    		
    		
    		// All Edges Flipped Correctly.
    		OLL_CASES[23] = new OLLCase(new int[] {Y,Y,Y, Y,Y,Y, Z,Y,Z}, "(R2' D) (R' U2) (R D') (R' U2 R')");
    		OLL_CASES[24] = new OLLCase(new int[] {Y,Y,Z, Y,Y,Y, Y,Y,Z}, "(Lw' U') (L U) (R U') (Rw' F)");
    		OLL_CASES[25] = new OLLCase(new int[] {Y,Y,X, Y,Y,Y, Z,Y,Y}, "(R' F) (R B') (R' F') (R B)");
    		OLL_CASES[27] = new OLLCase(new int[] {Z,Y,X, Y,Y,Y, Y,Y,Z}, "(R U R' U) (R U2 R')");
    		OLL_CASES[26] = new OLLCase(new int[] {X,Y,Y, Y,Y,Y, Z,Y,X}, "(R U2) (R' U' R U' R')");
    		OLL_CASES[22] = new OLLCase(new int[] {X,Y,Z, Y,Y,Y, X,Y,Z}, "(R U2') (R2' U') (R2 U') (R2' U2' R)");
    		OLL_CASES[21] = new OLLCase(new int[] {Z,Y,Z, Y,Y,Y, Z,Y,Z}, "(R U2) (R' U' R U R' U' R U' R')");
    		
    		// No Edges Flipped Correctly.
    		OLL_CASES[3]  = new OLLCase(new int[] {Z,Z,X, X,Y,X, X,Z,Y}, "Fw (R U R' U') Fw' U' F (R U R' U') F'");
    		OLL_CASES[4]  = new OLLCase(new int[] {X,Z,Y, X,Y,X, Z,Z,X}, "Fw (R U R' U') Fw' U F (R U R' U') F'");
    		OLL_CASES[17] = new OLLCase(new int[] {Y,Z,Z, X,Y,X, X,Z,Y}, "(R U R' U) (R' F R F') U2 (R' F R F')");
    		OLL_CASES[19] = new OLLCase(new int[] {Y,Z,Y, X,Y,X, X,Z,X}, "Rw' (R U) (R U R' U' Rw) x (R2' U) (R U')");
    		OLL_CASES[18] = new OLLCase(new int[] {Z,Z,Z, X,Y,X, Y,Z,Y}, "F (R U R' U) y' (R' U2) (R' F R F')");
    		OLL_CASES[2]  = new OLLCase(new int[] {X,Z,Z, X,Y,X, X,Z,Z}, "F (R U R' U') S (R U R' U') Fw'");
    		OLL_CASES[1]  = new OLLCase(new int[] {X,Z,X, X,Y,X, X,Z,X}, "(R U2) (R2' F R F') U2' (R' F R F')");
    		
    		// "T" Shapes.
    		OLL_CASES[33] = new OLLCase(new int[] {Z,Z,Y, Y,Y,Y, Z,Z,Y}, "(R U R' U') (R' F R F')");
    		OLL_CASES[45] = new OLLCase(new int[] {X,Z,Y, Y,Y,Y, X,Z,Y}, "F (R U R' U') F'");
    		
    		// "P" Shapes.
    		OLL_CASES[44] = new OLLCase(new int[] {X,Z,Y, X,Y,Y, X,Y,Y}, "Fw (R U R' U') Fw'");
    		OLL_CASES[43] = new OLLCase(new int[] {Y,Z,X, Y,Y,X, Y,Y,X}, "Fw' (L' U' L U) Fw");
    		OLL_CASES[32] = new OLLCase(new int[] {Z,Z,Y, X,Y,Y, Z,Y,Y}, "(R Dw) (L' Dw') (R' U) (Lw U Lw')");
    		OLL_CASES[31] = new OLLCase(new int[] {Z,Y,Y, X,Y,Y, Z,Z,Y}, "(R' U') F (U R U' R') F' R");
    		
    		// "W" Shapes.
    		OLL_CASES[38] = new OLLCase(new int[] {Z,Y,Y, Y,Y,X, Y,Z,X}, "(R U R' U) (R U' R' U') (R' F R F')");
    		OLL_CASES[36] = new OLLCase(new int[] {Y,Y,Z, X,Y,Y, X,Z,Y}, "(L' U' L U') (L' U L U) (L F' L' F)");
    		
    		// "L" Shapes.
    		OLL_CASES[54] = new OLLCase(new int[] {X,Y,X, X,Y,Y, X,Z,X}, "(Rw U) (R' U) (R U') (R' U) (R U2' Rw')");
    		OLL_CASES[53] = new OLLCase(new int[] {X,Z,X, X,Y,Y, X,Y,X}, "(Rw' U') (R U') (R' U) (R U') (R' U2 Rw)");
    		OLL_CASES[50] = new OLLCase(new int[] {X,Z,Z, X,Y,Y, X,Y,Z}, "(R B' R B R2') U2 (F R' F' R)");
    		OLL_CASES[49] = new OLLCase(new int[] {X,Y,Z, X,Y,Y, X,Z,Z}, "(R' F R' F' R2) U2 y (R' F R F')");
    		OLL_CASES[48] = new OLLCase(new int[] {X,Y,Z, Y,Y,X, X,Z,Z}, "F (R U R' U') (R U R' U') F'");
    		OLL_CASES[47] = new OLLCase(new int[] {Z,Y,X, X,Y,Y, Z,Z,X}, "F' (L' U' L U) (L' U' L U) F");
    		
    		// Big Lightning Bolts.
    		OLL_CASES[39] = new OLLCase(new int[] {Z,Z,Y, Y,Y,Y, Y,Z,X}, "(L F') (L' U' L U) F U' L'");
    		OLL_CASES[40] = new OLLCase(new int[] {Y,Z,Z, Y,Y,Y, X,Z,Y}, "(R' F) (R U R' U') F' U R");
    		
    		// "C" Shapes.
    		OLL_CASES[34] = new OLLCase(new int[] {X,Z,X, Y,Y,Y, Y,Z,Y}, "(R U R2' U') (R' F) (R U) (R U') F'");
    		OLL_CASES[46] = new OLLCase(new int[] {Y,Y,X, X,Y,X, Y,Y,X}, "(R' U') (R' F R F') (U R)");
    		
    		// Squares.
    		OLL_CASES[5]  = new OLLCase(new int[] {Z,Z,X, X,Y,Y, X,Y,Y}, "(Rw' U2) (R U R' U Rw)");
    		OLL_CASES[6]  = new OLLCase(new int[] {X,Y,Y, X,Y,Y, Z,Z,X}, "(Rw U2) (R' U' R U' Rw')");
    		
    		// Small Lightning Bolts.
    		OLL_CASES[7]  = new OLLCase(new int[] {Z,Y,X, Y,Y,X, Y,Z,Z}, "(Rw U R' U) (R U2 Rw')");
    		OLL_CASES[12] = new OLLCase(new int[] {X,Z,Z, Y,Y,X, Z,Y,Y}, "(M U2) (R' U' R U') (R' U2 R) U M'");
    		OLL_CASES[8]  = new OLLCase(new int[] {Y,Z,Z, Y,Y,X, Z,Y,X}, "(Rw' U' R U') (R' U2 Rw)");
    		OLL_CASES[11] = new OLLCase(new int[] {Z,Z,X, X,Y,Y, Y,Y,Z}, "Rw' (R2 U R' U R U2 R') U M'");
    		
    		// Fish Shapes.
    		OLL_CASES[37] = new OLLCase(new int[] {Y,Y,X, Y,Y,X, Z,Z,Y}, "F (R U') (R' U' R U) (R' F')");
    		OLL_CASES[35] = new OLLCase(new int[] {Y,Z,X, X,Y,Y, Z,Y,Y}, "(R U2) (R2 F) (R F' R U2 R')");
    		OLL_CASES[10] = new OLLCase(new int[] {Z,Z,Y, Y,Y,X, X,Y,Z}, "(R U R' U) (R' F R F') (R U2 R')");
    		OLL_CASES[9]  = new OLLCase(new int[] {X,Y,Z, Y,Y,X, Z,Z,Y}, "(R U R' U' R' F) (R2 U R' U' F')");
    		
    		// "I" Shapes.
    		OLL_CASES[51] = new OLLCase(new int[] {X,Z,Z, Y,Y,Y, X,Z,Z}, "Fw (R U R' U') (R U R' U') Fw'");
    		OLL_CASES[52] = new OLLCase(new int[] {Z,Y,X, X,Y,X, Z,Y,X}, "(R U R' U R Dw') (R U' R' F')");
    		OLL_CASES[56] = new OLLCase(new int[] {Z,Y,Z, X,Y,X, Z,Y,Z}, "Fw (R U R' U') Fw' F (R U R' U') (R U R' U') F'");
    		OLL_CASES[55] = new OLLCase(new int[] {X,Y,X, X,Y,X, X,Y,X}, "(R U2) (R2 U' R U' R' U2) (F R F')");
    		
    		// "Knight Move" Shapes.
    		OLL_CASES[13] = new OLLCase(new int[] {Z,Z,X, Y,Y,Y, Y,Z,Z}, "(Rw U' Rw' U' Rw U Rw' y' (R' U R)");
    		OLL_CASES[16] = new OLLCase(new int[] {X,Z,Y, Y,Y,Y, Z,Z,X}, "(Rw U Rw') (R U R' U') (Rw U' Rw')");
    		OLL_CASES[14] = new OLLCase(new int[] {X,Z,Z, Y,Y,Y, Z,Z,Y}, "(R' F) (R U R' F' R) y' (R U' R')");
    		OLL_CASES[15] = new OLLCase(new int[] {Y,Z,X, Y,Y,Y, X,Z,Z}, "(Lw' U' Lw) (L' U' L U) (Lw' U Lw)");
    		
    		// The "Awkward" Shapes.
    		OLL_CASES[41] = new OLLCase(new int[] {Y,Z,Y, X,Y,Y, Z,Y,Z}, "(R U') (R' U2) (R U) y (R U') (R' U' F')");
    		OLL_CASES[30] = new OLLCase(new int[] {Y,Z,Y, X,Y,Y, X,Y,X}, "(R2' U R' B') (R U') (R2' U) (Lw U Lw')");
    		OLL_CASES[42] = new OLLCase(new int[] {Y,Z,Y, Y,Y,X, Z,Y,Z}, "(L' U) (L U2') (L' U') y' (L' U) (L U F)");
    		OLL_CASES[29] = new OLLCase(new int[] {Y,Z,Y, Y,Y,X, X,Y,X}, "(L2 U' L B) (L' U) (L2 U') (Rw' U' Rw)");
		}
	}
	
	public Algorithm getSolveAlgorithm() {
		solveAlgorithm = new Algorithm("Solve");
		
		solveWhiteCross();
		solveF2L();
		solveOLL();
		solvePLL();
		
		// TODO: Remove orientation moves.
		solveAlgorithm.simplify();
		System.out.println("Solved in " + solveAlgorithm.getLength() + " moves.");
		System.out.println(new Notation().getNotation(solveAlgorithm));
		return solveAlgorithm;
	}
	
	public void solveWhiteCross() {
		// Orient white to up face.
		orientToUpFace(2, 1, 2);
		
		// SOLVE WHITE CROSS.
		for (int i = 0; i < 4; i++) {
    		Cubie edge = cube.getOriginalCubie(1, 1, 2);
    		if (i == 0)
    			edge = cube.getOriginalCubie(1, 1, 2);
    		else if (i == 1)
    			edge = cube.getOriginalCubie(2, 1, 3);
    		else if (i == 2)
    			edge = cube.getOriginalCubie(3, 1, 2);
    		else
    			edge = cube.getOriginalCubie(2, 1, 1);
    		
    		if (edge.getFace(0, 1, 0) == WHITE) {
    			if (i == 0) {
    				while (edge.getX() != 3)
            			addMove(new Move(Y_AXIS, 3, false));
    			}
    			else {
        			if (edge.getZ() == 1)
            			addMove(new Move(Y_AXIS, 3, false));
        			else if (edge.getX() == 1)
            			addMove(new Move(X_AXIS, 1, false));
        			else if (edge.getZ() == 3)
            			addMove(new Move(Z_AXIS, 3, false));
    			}
    		}
    		
    		if (edge.getFace(0, -1, 0) == WHITE) {
        		while (edge.getX() != 3)
        			addMove(new Move(Y_AXIS, edge.getY(), true));
    			addMove(new Move(X_AXIS, 3, true));
    			addMove(new Move(X_AXIS, 3, true));
    		}
    		else if (edge.getFace(0, 1, 0) != WHITE) {
    			if (edge.getY() == 3) {
    				if (edge.getX() == 1)
            			addMove(new Move(X_AXIS, 1, true));
    				else if (edge.getX() == 2)
            			addMove(new Move(Z_AXIS, edge.getZ(), true));
    			}
        		while (edge.getFace(1, 0, 0) != WHITE)
        			addMove(new Move(Y_AXIS, edge.getY(), true));
    
        		if (edge.getFace(1, 0, 0) == WHITE) {
        			if (edge.getY() != 2) {
        				addMove(new Move(X_AXIS, 3, edge.getY() == 3));
        			}
        			addMove(new Move(Y_AXIS, 2, edge.getZ() == 3));
        			addMove(new Move(X_AXIS, 3, edge.getZ() == 1));
        		}
    		}
    		
    		addMove(new Move(Y_AXIS, 0, true));
		}
	}
	
	public void solveF2L() {
		// SOLVE WHITE CORNERS.
		for (int i = 0; i < 4; i++) {
    		Cubie corner;
    		if (i == 0)
    			corner = cube.getOriginalCubie(1, 1, 1);
    		else if (i == 1)
    			corner = cube.getOriginalCubie(1, 1, 3);
    		else if (i == 2)
    			corner = cube.getOriginalCubie(3, 1, 3);
    		else
    			corner = cube.getOriginalCubie(3, 1, 1);
    		
    		if (corner.isAtPosition(3, 3, 1) && corner.getFace(0, 1, 0) == WHITE) {
    			// Already Solved
    		}
    		else {
    			if (corner.getY() == 3) {
    				boolean cw = (corner.getX() == 3);
    				int z = corner.getZ();
    				addMove(new Move(Z_AXIS, z, cw));
        			addMove(new Move(Y_AXIS, 1, true));
    				addMove(new Move(Z_AXIS, z, !cw));
    			}
    			
    			if (corner.getFace(0, -1, 0) == WHITE) {
    				while (!corner.isAtPosition(3, 1, 1))
            			addMove(new Move(Y_AXIS, 1, true));

        			addMove(new Move(X_AXIS, 3, false));
        			addMove(new Move(Y_AXIS, 1, false));
        			addMove(new Move(X_AXIS, 3, true));

        			addMove(new Move(Z_AXIS, 1, true));
        			addMove(new Move(Y_AXIS, 1, false));
        			addMove(new Move(Y_AXIS, 1, false));
        			addMove(new Move(Z_AXIS, 1, false));
    			}
    			else {
    				while (corner.getFace(0, 0, 1) != WHITE)
            			addMove(new Move(Y_AXIS, 1, true));
    				
    				if (corner.getX() == 3) {
            			addMove(new Move(Z_AXIS, 1, true));
            			addMove(new Move(Y_AXIS, 1, true));
            			addMove(new Move(Z_AXIS, 1, false));
    				}
    				else {
            			addMove(new Move(X_AXIS, 3, false));
            			addMove(new Move(Y_AXIS, 1, true));
            			addMove(new Move(Y_AXIS, 1, true));
            			addMove(new Move(X_AXIS, 3, true));
    				}
    			}
    		}

    		addMove(new Move(Y_AXIS, 0, true));
		}
		
		// Align the 2nd layer centers with the 1st layer.
		while (cube.getCubie(3, 2, 2).getFace(1, 0, 0) != BLUE)
			addMove(new Move(Y_AXIS, 2, true));
		
		// Orient the 1st layer to the bottom.
		addMove(new Move(Z_AXIS, 0, true));
		addMove(new Move(Z_AXIS, 0, true));
		
		// SOLVE THE SECOND LAYER EDGES.
		for (int i = 0; i < 4; i++) {
    		Cubie edge = cube.getOriginalCubie(1, 2, 2);
    		if (i == 0)
    			edge = cube.getOriginalCubie(3, 2, 1);
    		else if (i == 1)
    			edge = cube.getOriginalCubie(1, 2, 1);
    		else if (i == 2)
    			edge = cube.getOriginalCubie(1, 2, 3);
    		else
    			edge = cube.getOriginalCubie(3, 2, 3);
    		
    		int faceRight = cube.getCubie(3, 2, 2).getFace(1, 0, 0); 
    		
    		if (edge.isAtPosition(3, 2, 1) && edge.getFace(1, 0, 0) == faceRight) {
    			// Solved.
    		}
    		else {
    			if (edge.getY() == 2) {
        			while (!edge.isAtPosition(3, 2, 1))
        	    		addMove(new Move(Y_AXIS, 2, true));
        			
        			addMoves(ALG_EDGE_FIT_RIGHT);
    
        			while (cube.getCubie(3, 2, 2).getFace(1, 0, 0) != faceRight)
        				addMove(new Move(Y_AXIS, 2, true));
    			}
    			
    			if (edge.getFace(0, 1, 0) == faceRight) {
        			while (edge.getZ() != 1)
        	    		addMove(new Move(Y_AXIS, 3, true));
        			addMoves(ALG_EDGE_FIT_RIGHT);
    			}
    			else {
        			while (edge.getX() != 3)
        	    		addMove(new Move(Y_AXIS, 3, true));
        			addMoves(ALG_EDGE_FIT_LEFT);
    			}
    		}

    		addMove(new Move(Y_AXIS, 0, false));
		}
	}
	
	public void solveOLL() {
		// Orient the yellow to the top.
		orientToUpFace(2, 3, 2);
		
		for (int i = 0; i < 4; i++) {
			int[] ollCase = new int[9];
			for (int z = 0; z < 3; z++) {
				for (int x = 0; x < 3; x++) {
					Cubie cubie = cube.getCubie(x + 1, 3, 3 - z);
					int index = (z * 3) + x;
					ollCase[index] = (cubie.getFace(0, 1, 0) == YELLOW ? Y_AXIS : Z_AXIS);
					if (cubie.getFace(1, 0, 0) == YELLOW || cubie.getFace(-1, 0, 0) == YELLOW)
						ollCase[index] = X_AXIS;
				}
			}
			int ollIndex = getOLLCase(ollCase);
			if (ollIndex >= 0) {
				System.out.println("OLL Case " + ollIndex);
				addMoves(OLL_CASES[ollIndex].algorithm);
				break;
			}
			else if (ollIndex < 0)
				addMove(new Move(Y_AXIS, 0, false));
		}
	}
	
	public void solvePLL() {
		// Re-orient the yellow to the top.
		orientToUpFace(2, 3, 2);

		// PLL:
		int[] pllCase = new int[9];
		for (int z = 2; z >= 0; z--) {
			for (int x = 0; x < 3; x++) {
				Cubie cubie = cube.getCubie(x + 1, 3, z + 1);
				int index = ((2 - z) * 3) + x;
				int originX = cubie.originalPosition[0] / 2;
				int originZ = cubie.originalPosition[2] / 2;
				pllCase[index] = ((3 - originZ) * 3) + originX;
			}
		}
		
		boolean foundPll = false;
		for (int i = 0; i < 4 && !foundPll; i++) {
			for (int j = 0; j < 4 && !foundPll; j++) {
				int pllIndex = getPLLCase(pllCase);
				
				if (pllIndex >= 0) {
					System.out.println("PLL Case " + pllIndex);
					foundPll = true;
					addMoves(PLL_CASES[pllIndex].algorithm);
					break;
				}
				else {
					// Rotate the pll case data. (x, z) -> (-z, x)
					int [] newPllCase = new int[9];
					for (int z = 0; z < 3; z++) {
						for (int x = 0; x < 3; x++) {
							int conv  = pllCase[((2 - x) * 3) + z] - 1;
							int convX = conv % 3;
							int convZ = conv / 3;
							newPllCase[((2 - z) * 3) + (2 - x)] = ((2 - convX) * 3) + convZ + 1;
						}
					}
					pllCase = newPllCase;
					addMove(new Move(Y_AXIS, 0, false));
				}
			}
			
			if (!foundPll) {
    			// Rotate the pll case data. (x, z) -> (-z, x)
    			int[] newPllCase = new int[9];
    			for (int z = 0; z < 3; z++) {
    				for (int x = 0; x < 3; x++) {
    					newPllCase[(z * 3) + x] = pllCase[((2 - x) * 3) + z];
    				}
    			}
    			pllCase = newPllCase;
				addMove(new Move(Y_AXIS, 0, true));
			}
		}
		
		// Align the top layer with the solved cube.
		orientToUpFace(2, 3, 2);
		while (cube.getCubie(3, 3, 2).getFace(1, 0, 0) != cube.getCubie(3, 2, 2).getFace(1, 0, 0))
			addMove(new Move(Y_AXIS, 3, true));
	}
	
	public void solveLastLayer() {
		// SOLVE THE YELLOW CROSS.
		while (true) {
    		boolean[] crossState = new boolean[] {
    			cube.getCubie(3, 3, 2).getFace(0, 1, 0) == YELLOW,
    			cube.getCubie(2, 3, 1).getFace(0, 1, 0) == YELLOW,
    			cube.getCubie(1, 3, 2).getFace(0, 1, 0) == YELLOW,
    			cube.getCubie(2, 3, 3).getFace(0, 1, 0) == YELLOW
    		};
    		int crossCount = 0;
    		for (int i = 0; i < crossState.length; i++) {
    			if (crossState[i])
    				crossCount++;
    		}
    		
    		if (crossCount == 4) {
    			// Solved.
    			break;
    		}
    		else if (crossCount == 0) {
    			// No yellow facing up.
    			addMoves(ALG_CYCLE_CROSS);
    		}
    		else if (crossState[0] != crossState[2]) {
    			// "L" shape
    			if (crossState[0] && crossState[1])
    				addMoves(ALG_CYCLE_CROSS);
    			else
    	    		addMove(new Move(Y_AXIS, 3, true));
    		}
    		else {
    			// Bar shape
    			if (!crossState[0])
    	    		addMove(new Move(Y_AXIS, 3, true));
				addMoves(ALG_CYCLE_CROSS);
    		}
		}
		
		// SOLVE THE YELLOW EDGES.
		while (true) {
    		boolean[] edgeState = new boolean[] {
    			cube.getCubie(3, 3, 2).getFace(1, 0, 0)  == GREEN,
    			cube.getCubie(2, 3, 1).getFace(0, 0, -1) == RED,
    			cube.getCubie(1, 3, 2).getFace(-1, 0, 0) == BLUE,
    			cube.getCubie(2, 3, 3).getFace(0, 0, 1)  == ORANGE
    		};
    		int edgeCount = 0;
    		for (int i = 0; i < edgeState.length; i++) {
    			if (edgeState[i])
    				edgeCount++;
    		}
    		
    		if (edgeCount == 4) {
    			// Solved!
    			break;
    		}
    		else if (edgeCount < 2) {
	    		addMove(new Move(Y_AXIS, 3, true));
    		}
    		else if (edgeState[0] != edgeState[2]) {
    			// Adjacent edges are correct.
    			for (int i = 0; i < 4; i++) {
    				if (edgeState[i] && edgeState[(i + 3) % 4])
    					break;
    	    		addMove(new Move(Y_AXIS, 3, false));
    			}
				addMoves(ALG_SUNE);
    		}
    		else {
    			// Opposite Edges are correct
    			addMoves(ALG_SUNE);
    		}
		}
		
		// POSITION THE YELLOW CORNERS.
		while (true) {
    		boolean[] cornerState = new boolean[] {
    			cube.getOriginalCubie(3, 3, 1).isAtPosition(3, 3, 1),
    			cube.getOriginalCubie(1, 3, 1).isAtPosition(1, 3, 1),
    			cube.getOriginalCubie(1, 3, 3).isAtPosition(1, 3, 3),
    			cube.getOriginalCubie(3, 3, 3).isAtPosition(3, 3, 3)
    		};
    		int cornerCount = 0;
    		for (int i = 0; i < cornerState.length; i++) {
    			if (cornerState[i])
    				cornerCount++;
    		}
    		
    		if (cornerCount == 4) {
    			// Solved!
    			break;
    		}
    		else if (cornerCount == 0) {
    			// No corners are correct.
	    		addMoves(ALG_CYCLE_CORNERS);
    		}
    		else {
    			// One corner is correct.
    			int i;
    			for (i = 0; i < 4 && !cornerState[i]; i++)
    	    		addMove(new Move(Y_AXIS, 0, false));
	    		addMoves(ALG_CYCLE_CORNERS);

    			for (int j = 0; j < i; j++)
    	    		addMove(new Move(Y_AXIS, 0, true));
    		}
		}
		
		// ORIENT THE YELLOW CORNERS.
		for (int i = 0; i < 4; i++) {
			Cubie corner = cube.getCubie(3, 3, 1);
    		if (corner.getFace(0, 1, 0) != YELLOW) {
        		while (corner.getFace(0, 1, 0) != YELLOW)
        			addMoves(ALG_ORIENT_FINAL);
    		}
    		addMove(new Move(Y_AXIS, 3, false));
		}
	}
	
	public void orientToUpFace(int cubieX, int cubieY, int cubieZ) {
		// Re-orient the yellow to the top.
		Cubie cubie = cube.getOriginalCubie(cubieX, cubieY, cubieZ);
		if (cubie.positions[1] == 2)
			addMove(new Move(0, 0, true));
		if (cubie.positions[0] == 6)
			addMove(new Move(2, 0, false));
		else if (cubie.positions[0] == 2)
			addMove(new Move(2, 0, true));
		else if (cubie.positions[2] == 6)
			addMove(new Move(0, 0, false));
		else if (cubie.positions[2] == 2)
			addMove(new Move(0, 0, true));
	}
	
	private int getPLLCase(int[] pllCase) {
		for (int i = 0; i < PLL_CASES.length; i++) {
			if (checkPLLCase(pllCase, PLL_CASES[i]))
				return i;
		}
		return -1;
	}
	
	private int getOLLCase(int[] ollCase) {
		for (int i = 0; i < OLL_CASES.length; i++) {
			if (checkOLLCase(ollCase, OLL_CASES[i]))
				return i;
		}
		return -1;
	}
	
	private boolean checkPLLCase(int[] pllCase, PLLCase testCase) {
		if (testCase == null)
			return false;
		for (int i = 0; i < 9; i++) {
			if (pllCase[i] != testCase.pllCase[i])
				return false;
		}
		return true;
	}
	
	private boolean checkOLLCase(int[] ollCase, OLLCase testCase) {
		if (testCase == null)
			return false;
		for (int i = 0; i < 9; i++) {
			if (ollCase[i] != testCase.ollCase[i])
				return false;
		}
		return true;
	}
	
	private void addMove(Move move) {
		solveAlgorithm.addMove(new Move(move));
		cube.apply(move, true);
	}
	
	private void addMoves(Algorithm alg) {
		for (int i = 0; i < alg.getLength(); i++)
			addMove(alg.getMove(i));
	}
}
