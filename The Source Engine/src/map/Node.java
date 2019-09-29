package map;

import java.util.ArrayList;
import common.Vector;


/**
 * Node.
 * 
 * @author David Jordan
 */
public class Node extends Vector {
	public ArrayList<Node> nodeLinks;
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Node(Vector position) {
		super(position);
		this.nodeLinks = new ArrayList<Node>();
	}
	
	
	
	// =================== ACCESSORS =================== //
	
	
	
	
	
	// ==================== MUTATORS ==================== //
	
	
}
