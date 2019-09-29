import java.util.ArrayList;


public class List {
	public ArrayList<DataValue> data;
	
	public List() {
		data = new ArrayList<DataValue>();
	}
	
	public List(List l) {
		data = new ArrayList<DataValue>();
		if (l != null)
			set(l);
	}
	
	public DataValue get(int index) {
		return data.get(index);
	}
	
	public void add(DataValue value) {
		data.add(value);
	}
	
	public void add(int index, DataValue value) {
		data.add(index, value);
	}
	public void set(int index, DataValue value) {
		data.set(index, value);
	}
	
	public void set(List l) {
		data.clear();
		for (int i = 0; i < l.size(); i++) {
			add(i, l.get(i));
		}
	}
	
	public void remove(int index) {
		data.remove(index);
	}
	
	public void clear() {
		data.clear();
	}
	
	public DataValue sum() {
		DataValue total = new DataValue(0);
		for (DataValue dv : data)
			total = new DataValue(Number.add(total.number, dv.number));
		return total;
	}

	public int size() {
		return data.size();
	}
	
	public boolean isEmpty() {
		return data.isEmpty();
	}
	
	public String getPrintString() {
		String s = "{";
		for (int i = 0; i < data.size(); i++) {
			s += data.get(i).getPrintString();
			if (i < data.size() - 1)
				s += ", ";
		}
		s += "}";
		return s;
	}
	public void print() {
		System.out.print(getPrintString());
	}
}
