package projects.physicsHomework.lab;

import java.util.ArrayList;
import projects.physicsHomework.List;

public class Experiment {
	public ArrayList<Dataset> datasets;
	public ArrayList<Measure> measurements;
	public String name;
	
	

	// ================== CONSTRUCTORS ================== //

	public Experiment() {
		this("");
	}
	
	public Experiment(String name) {
		this.name = name;
		datasets = new ArrayList<Dataset>();
		measurements = new ArrayList<Measure>();
	}
	
	
	
	// =================== ACCESSORS =================== //

	public String getName() {
		return name;
	}
	
	public ArrayList<Dataset> getDatasets() {
		return datasets;
	}
	
	public ArrayList<Measure> getMeasurements() {
		return measurements;
	}
	
	public Dataset getDataset(String name) {
		for (int i = 0; i < datasets.size(); i++) {
			if (datasets.get(i).getName().equals(name))
				return datasets.get(i);
		}
		return null;
	}
	
	public Measure getMeasurement(String name) {
		for (int i = 0; i < measurements.size(); i++) {
			if (measurements.get(i).getName().equals(name))
				return measurements.get(i);
		}
		return null;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addDataset(Dataset dataset) {
		datasets.add(dataset);
	}
	
	public void addMeasure(Measure measure) {
		measurements.add(measure);
	}
}
