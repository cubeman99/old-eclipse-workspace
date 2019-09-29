package projects.physicsHomework.lab;

import java.util.ArrayList;

public class LabData {
	public String name;
	public ArrayList<Experiment> experiments;
	
	

	// ================== CONSTRUCTORS ================== //

	public LabData() {
		this("");
	}
	
	public LabData(String name) {
		this.name = name;
		experiments = new ArrayList<Experiment>();
	}
	
	
	
	// =================== ACCESSORS =================== //

	public String getName() {
		return name;
	}
	
	public ArrayList<Experiment> getExperiments() {
		return experiments;
	}
	
	public Experiment getExperiment(String name) {
		for (int i = 0; i < experiments.size(); i++) {
			if (experiments.get(i).getName().equals(name))
				return experiments.get(i);
		}
		return null;
	}
	
	
	
	// ==================== MUTATORS ==================== //

	public void setName(String name) {
		this.name = name;
	}
	
	public void addExperiment(Experiment exp) {
		experiments.add(exp);
	}
	
	public void printData() {
		for (int i = 0; i < experiments.size(); i++) {
			Experiment e = experiments.get(i);
			ArrayList<Measure> measurements = e.getMeasurements();
			ArrayList<Dataset> datasets = e.getDatasets();
			
			System.out.println("Experiment: " + e.getName());
			
			for (int j = 0; j < measurements.size(); j++) {
				Measure m = measurements.get(j);
				System.out.println("Measure: " + m.getName() + " = " + m.getString(true));
			}
			
			for (int j = 0; j < datasets.size(); j++) {
				Dataset ds = datasets.get(j);
				System.out.printf("Dataset: %s (n = %d)%n", ds.getName(), ds.size());
			}

			System.out.println();
		}
	}
}
