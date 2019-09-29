package projects.physicsHomework.lab;
import java.util.ArrayList;


public class Dataset {
	private String name;
	private double error;
	private Units units;
	private ArrayList<Double> data;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public Dataset(String name, double error, Units units) {
		this.name  = name;
		this.error = error;
		this.units = units;
		this.data  = new ArrayList<Double>();
	}
	


	// =================== ACCESSORS =================== //
	
	public String getName() {
		return name;
	}
	
	public double getError() {
		return error;
	}
	
	public Units getUnits() {
		return units;
	}
	
	public ArrayList<Double> getData() {
		return data;
	}
	
	public double getData(int index) {
		return data.get(index);
	}
	
	public int size() {
		return data.size();
	}
	
	public double getSum() {
		double sum = 0;
		for (int i = 0; i < data.size(); i++)
			sum += data.get(i);
		return sum;
	}
	
	public double getAverage() {
		return (getSum() / data.size());
	}
	
	public double getRange() {
		return getMax() - getMin();
	}
	public double getMin() {
		double min = data.get(0);
		for (int i = 1; i < data.size(); i++) {
			if (data.get(i) < min)
				min = data.get(i);
		}
		return min;
	}
	
	public double getMax() {
		double max = data.get(0);
		for (int i = 1; i < data.size(); i++) {
			if (data.get(i) > max)
				max = data.get(i);
		}
		return max;
	}
	
	public double getNearestToAverage() {
		double nearest = data.get(0);
		double mean = getAverage();
		for (int i = 1; i < data.size(); i++) {
			double e = data.get(i);
			if (Math.abs(mean - e) < Math.abs(mean - nearest))
				nearest = e;
		}
		return nearest;
	}
	
	public double getStandardDeviation() {
		double mean = getAverage();
		double sum = 0;
		for (int i = 0; i < data.size(); i++)
			sum += (data.get(i) - mean) * (data.get(i) - mean);
		return Math.sqrt(sum / (data.size() - 1));
	}
	
	public Dataset subset(int startIndex, int length) {
		Dataset subset = new Dataset(name + " subset", length, units);
		for (int i = 0; i < length; i++)
			subset.addData(getData(startIndex + i));
		return subset;
	}
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setData(int index, double value) {
		data.set(index, value);
	}
	
	public void addData(double value) {
		data.add(value);
	}
	
	public void insertData(int index, double value) {
		data.add(index, value);
	}
	
	public void removeData(int index) {
		data.remove(index);
	}
}
