package projects.towerDefense;

import java.util.ArrayList;
import projects.towerDefense.tile.Path;
import cmg.math.geometry.Point;

public class CreepPath {
	private ArrayList<Path> steps;

	

	// ================== CONSTRUCTORS ================== //
	
	public CreepPath() {
		steps = new ArrayList<Path>();
	}
	
	

	// =================== ACCESSORS =================== //
	
	public int numSteps() {
		return steps.size();
	}
	
	public Path getStep(int index) {
		return steps.get(index);
	}
	
	public boolean contains(Path p) {
		return steps.contains(p);
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void addStep(Path p) {
		steps.add(p);
	}
	
	public void clear() {
		steps.clear();
	}
}
