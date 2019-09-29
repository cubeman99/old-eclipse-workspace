package projects.physicsHomework.lab;

import java.util.ArrayList;
import projects.physicsHomework.*;


public class LabScriptReader extends ScriptReader {
	private LabData labData;
	private Experiment currentExperiment;
	private Dataset currentDataset;
	
	
	public LabData getLabData() {
		return labData;
	}
	
	
	@Override
	protected void beginReading() {
		labData           = new LabData();
		currentExperiment = null;
		currentDataset    = null;
	}
	
	@Override
	protected void endReading() {
		
	}
	
	@Override
	protected void readLine(String line) {
		if (currentDataset != null && !line.startsWith("@")) {
			if (!line.isEmpty())
				currentDataset.addData(Double.parseDouble(line));
		}
		else {
			super.readLine(line);
		}
	}
	
	@Override
	protected void readCommand(String command, ArrayList<String> args) {
		
		// Declare a new experiment.
		// @experiment [name]
		if (command.equals("experiment")) {
			currentExperiment = new Experiment(args.get(0));
			labData.addExperiment(currentExperiment);
		}
		
		// Define a new measurement.
		// @measure [name] [value] [error] [units]
		else if (command.equals("measure")) {
			String name     = args.get(0);
			double value    = Double.parseDouble(args.get(1));
			double error    = Double.parseDouble(args.get(2));
			Units units     = Units.parseUnits(args.get(3));
			Measure measure = new Measure(name, value, error, units);
			currentExperiment.addMeasure(measure);
		}
		
		// Define a dataset.
		// @dataset [name] [error] [units]
		else if (command.equals("dataset")) {
			String name    = args.get(0);
			if (args.size() > 1) {
				double error   = Double.parseDouble(args.get(1));
    			Units units    = new Units(Units.getUnit(args.get(2)));
    			currentDataset = new Dataset(name, error, units);
			}
			else
				currentDataset = new Dataset(name, 0, null);
			currentExperiment.addDataset(currentDataset);
		}
		
		// End dataset definition.
		// @end
		else if (command.equals("end")) {
			currentDataset = null;
		}
		
		else {
			super.readCommand(command, args);
		}
	}
}
