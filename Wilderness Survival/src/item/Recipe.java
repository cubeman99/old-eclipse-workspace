package item;

import java.util.ArrayList;


/**
 * This is a class to represent a
 * crafting recipe: a recipe to make an
 * item requiring a certain amount of
 * materials.
 * 
 * @author David Jordan
 */
public class Recipe {
	public ArrayList<Material> materials;
	

	// ====== CONSTRUCTORS ====== //
	
	/** Construct a recipe with an array of given materials. **/
	public Recipe(Material... mats) {
		for (int i = 0; i < mats.length; i++) {
			materials.add(mats[i]);
		}
	}
	
	/** Construct a recipe with an array list of given materials. **/
	public Recipe(ArrayList<Material> mats) {
		for (int i = 0; i < mats.size(); i++) {
			materials.add(mats.get(i));
		}
	}
	
	
}
