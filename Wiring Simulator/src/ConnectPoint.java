
public class ConnectPoint {
	public int x;
	public int y;
	public DataPoint dataPoint;
	
	public ConnectPoint(int x, int y) {
		this.x = x;
		this.y = y;
		this.dataPoint = null;
	}
	
	public boolean getState(int basex, int basey) {
		return WireSimulator.getWireState(basex + 1 + (x * 2), basey + 1 + (y * 2));
//		if (dataPoint != null) {
//			if (dataPoint.isOutput)
//				return dataPoint.state;
//		}
//		return false;
	}
	
	public void setState(boolean state) {
		if (dataPoint != null) {
			dataPoint.state = state;
		}
	}
	
	public ConnectPoint(int x, int y, DataPoint dataPoint) {
		this.x = x + dataPoint.x;
		this.y = y + dataPoint.y;
		this.dataPoint = dataPoint;
	}
}
