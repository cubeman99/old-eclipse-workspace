
public class GMath {
	
	
	public static double getDistance(double... values) {
		double sum = 0;
		for (int i = 0; i < values.length; i++)
			sum += values[i] * values[i];
		return Math.sqrt(sum);
	}
}
