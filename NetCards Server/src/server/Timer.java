package server;

public class Timer {
	private long startTime;
	private boolean running;
	
	
	public Timer() {
		reset();
	}
	
	public boolean running() {
		return running;
	}
	
	public void reset() {
		startTime = 0;
		running   = false;
	}
	
	public void start() {
		startTime = getSystemTime();
		running   = true;
	}
	
	public void stop() {
		running = false;
	}
	
	public boolean pastTime(int time) {
		return (getTime() >= time);
	}
	
	public void setTime(int time) {
		startTime = getSystemTime() - time;
	}
	
	public int getTime() {
		return (int) (getSystemTime() - startTime);
	}
	
	private long getSystemTime() {
		return System.currentTimeMillis();
	}
}
