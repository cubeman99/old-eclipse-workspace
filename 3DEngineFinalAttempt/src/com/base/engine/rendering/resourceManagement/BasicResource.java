package com.base.engine.rendering.resourceManagement;


public abstract class BasicResource {
	private int numReferences;
	
	
	// ================== CONSTRUCTORS ================== //

	public BasicResource(boolean addInitialReference) {
		this.numReferences = (addInitialReference ? 1 : 0);
	}
	


	// =============== ABSTRACT METHODS =============== //
	
	@Override
	protected abstract void finalize();
	
	

	// =================== ACCESSORS =================== //

	public int getNumReferences() {
		return numReferences;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void addReference() {
		numReferences++;
	}
	
	public boolean removeReference() {
		numReferences--;
		return (numReferences <= 0);
	}
}
