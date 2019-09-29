package com.base.game.rubiksCube;

import java.util.ArrayList;

public class Algorithm {
	private String name;
	private ArrayList<Move> moves;
	private ArrayList<String> labels;
	
	
	// ================== CONSTRUCTORS ================== //

	public Algorithm(String name) {
		this.name = name;
		moves  = new ArrayList<Move>();
		labels = new ArrayList<String>();
	}
	
	public Algorithm(String name, String algorithm, Notation notation) {
		this(name);
		moves.addAll(notation.parse(algorithm));
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	public String getName() {
		return name;
	}
	
	public int getLength() {
		return moves.size();
	}
	
	public Move getMove(int index) {
		return moves.get(index);
	}
	
	public String getLabel(int index) {
		if (index < labels.size() || labels.get(index) == null)
			return "";
		return labels.get(index);
	}
	
	
	// ==================== MUTATORS ==================== //
	
	public void addMove(Move move) {
		moves.add(move);
		labels.add(""); // TODO
	}
	
	public void addMoves(Algorithm algorithm) {
		for (int i = 0; i < algorithm.getLength(); i++) {
			moves.add(algorithm.getMove(i));
			labels.add(algorithm.getName());
		}
	}
	
	public void removeMove(int index) {
		moves.remove(index);
		labels.remove(index);
	}
    	
    public void simplify() {
    	Notation notation = new Notation();
    	int numRemoves = 0;
    	int turnCount = 0;
    	
//    	System.out.println(notation.getNotation(this));
    	

//    	int turnCount = getMove(0).isClockwise() ? 1 : -1;
//		System.out.print(turnCount + " ");
//    	for (int i = 1; i < moves.size(); i++) {
//    		Move move = moves.get(i);
//    		Move movePrev = getMove(i - 1);
//    		
//    		if (move.getAxis() == movePrev.getAxis() && move.getLayer() == movePrev.getLayer())
//    			turnCount += move.isClockwise() ? 1 : -1;
//    		else
//    			turnCount = move.isClockwise() ? 1 : -1;
//    
//    		System.out.print(turnCount + " ");
//    	}
//		System.out.println();
    	
    	int tempRemoves = numRemoves;
		for (int pass = 0; pass < 10; pass++) {
        	for (int i = 1; i < getLength(); i++) {
        		Move move = getMove(i);
        		Move movePrev = getMove(i - 1);
        		
        		if (move.getAxis() == movePrev.getAxis() && move.getLayer() == movePrev.getLayer()) {
        			if (move.isClockwise() != movePrev.isClockwise()) {
        				moves.remove(i - 1);
        				moves.remove(i - 1);
        				i -= 2;
        				numRemoves += 2;
        				if (i < 0)
        					i = 0;
        			}
        		}
        	}
		}
		System.out.println("Removed " + (numRemoves - tempRemoves) +  " Anti-turns.");
		
		tempRemoves = numRemoves;
		for (int pass = 0; pass < 20; pass++) {
    		turnCount = getMove(0).isClockwise() ? 1 : -1;
        	for (int i = 1; i < moves.size(); i++) {
        		Move move = moves.get(i);
        		Move movePrev = getMove(i - 1);
        		
        		if (move.getAxis() == movePrev.getAxis() && move.getLayer() == movePrev.getLayer()) {
        			turnCount += move.isClockwise() ? 1 : -1;
        			
        			if (Math.abs(turnCount) == 3) {
        				move.setClockwise(!move.isClockwise());
        				moves.remove(i - 2);
        				moves.remove(i - 2);
        				numRemoves += 2;
        				break;
        			}
        		}
        		else
        			turnCount = move.isClockwise() ? 1 : -1;
        
        	}
		}
		System.out.println("Removed " + (numRemoves - tempRemoves) +  " Triple-turns.");
		

		System.out.println("Removed " + numRemoves + " moves.");
//    	System.out.println(notation.getNotation(this));
    }
}
