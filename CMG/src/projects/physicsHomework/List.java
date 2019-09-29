package projects.physicsHomework;
import java.util.ArrayList;


public class List {
	private String name;
	private ArrayList<Double> data;
	
	
	
	// ================== CONSTRUCTORS ================== //
	
	public List(String name) {
		data = new ArrayList<Double>();
		this.name = name;
	}
	


	// =================== ACCESSORS =================== //
	
	public String getName() {
		return name;
	}
	
	public double getElement(int index) {
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
	
	public double getElementNearestToAverage() {
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
	
	
	
	// ==================== MUTATORS ==================== //
	
	public void setElement(int index, double value) {
		data.set(index, value);
	}
	
	public void append(double value) {
		data.add(value);
	}
	
	public void insert(int index, double value) {
		data.add(index, value);
	}
	
	public void remove(int index) {
		data.remove(index);
	}
}
