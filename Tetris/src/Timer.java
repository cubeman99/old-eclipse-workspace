

public class Timer {
	public long startTime	= 0;
	public long endTime		= 0;
	public boolean running	= false;
	
	public Timer() {
	}
	
	public void start() {
		if (!running) {
			startTime = System.currentTimeMillis();
			running   = true;
		}
	}
	
	public void stop() {
		running = false;
		endTime = System.currentTimeMillis();
	}
	
	private long getCurrentTime() {
		if (running)
			return System.currentTimeMillis();
		return endTime;
	}
	
	public int getMilliseconds() {
		return (int) (getCurrentTime() - startTime);
	}
	
	public double getSeconds() {
		long tm = getCurrentTime() - startTime;
		return ((double) tm / 1000.0d);
	}
	
	public double getMinutes() {
		long tm = getCurrentTime() - startTime;
		return ((double) tm / 60000.0d);
	}
	
	public double getHours() {
		long tm = getCurrentTime() - startTime;
		return ((double) tm / 3600000.0d);
	}
	/*
	public boolean onSecond() {
		return (GMath.modulus(milliseconds, 1000) == 0);
	}
	
	public boolean onMinute() {
		return (GMath.modulus(milliseconds, 60000) == 0);
	}
	
	public boolean onHour() {
		return (GMath.modulus(milliseconds, 3600000) == 0);
	}
	*/
	
	public void reset() {
		startTime = System.currentTimeMillis();
		endTime	  = 0;
	}
}
