package com.base.game.rubiksCube;

import java.util.ArrayList;

public class Notation {
	private static final String MOVE_CHARS = "XLMRYDEUZFSB";
	private static final char PRIME_CHAR   = '\'';
	
	public Notation() {
		
	}
	
	public ArrayList<Move> parse(String str) {
		ArrayList<Move> moves = new ArrayList<Move>();
		
		System.out.println(str);
		str = str.replaceAll("[()\\[\\]]", "");
		str = str.replaceAll("x", "X");
		str = str.replaceAll("y", "Y");
		str = str.replaceAll("z", "Z");
		System.out.println(str);
		
		if (str.length() > 0) {
    		String[] tokens = str.split("\\s+|\\t+");
    		for (int i = 0; i < tokens.length; i++)
    			addMove(tokens[i], moves);
		}
		
		for (int i = 0; i < moves.size(); i++)
			System.out.print(getNotation(moves.get(i)) + " ");
		System.out.println();
		System.out.println();
		
		return moves;
	}
	
	public void addMove(String token, ArrayList<Move> moveList) {
		int axis      = 0;
		int layer     = 0;
		int count     = 1;
		boolean clockwise = true;
		boolean twoLayers = false;
		
		if (token.length() > 1) {
			if (token.indexOf("2") >= 0)
				count = 2;
			if (token.charAt(1) == 'w'|| token.charAt(1) == 'W')
				twoLayers = true;
		}
		clockwise = !(token.endsWith(PRIME_CHAR + ""));
		
		int index = MOVE_CHARS.indexOf(token.substring(0, 1));
		if (index >= 0) {
			axis = index / 4;
			layer = (index % 4);
		}
		
		if (axis != 2 && layer == 2)
			clockwise = !clockwise;
		if (RubiksCube.FLIPPED[axis] && layer == 1)
			clockwise = !clockwise;
		else if (!RubiksCube.FLIPPED[axis] && layer == 3)
			clockwise = !clockwise;
		
		for (int i = 0; i < count; i++) {
			moveList.add(new Move(axis, layer, clockwise));
			if (twoLayers)
				moveList.add(new Move(axis, 2, clockwise));
		}
	}
	
	public String getNotation(Move move) {
		String str = "";
		int index = (move.getAxis() * 4) + move.getLayer();
		str += MOVE_CHARS.charAt(index);
		boolean cw = move.isClockwise();
		if (move.getAxis() != 2 && move.getLayer() == 2)
			cw = !cw;
		if (move.getLayer() == (RubiksCube.FLIPPED[move.getAxis()] ? 1 : 3))
			cw = !cw;
		if (!cw)
			str += PRIME_CHAR;
		return str;
	}
	
	
	public String getNotation(Algorithm algorithm) {
		String str = "";
    	for (int i = 0; i < algorithm.getLength(); i++)
    		str += getNotation(algorithm.getMove(i)) + " ";
		return str;
	}
	
}
