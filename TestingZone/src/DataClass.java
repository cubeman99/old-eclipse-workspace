
public class DataClass {
	public Data data1;
	public Data data2;
	public Data data3;
	
	public DataClass(Data data1, Data data2, Data data3) {
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;
	}
	

	public DataClass(DataClass DS) {
		this(DS.data1, DS.data2, DS.data3);
	}
}
