package common;


/**
 * A Timer class to keep track of timing.
 * It is used to tell when a certain time
 * has past. The units are in milliseconds.
 * 
 * @author David
 */
public class Timer {
	private long startTime;
	private long expireTime;
	private boolean running;
	
	
	/** Create a timer with a specified expire time. **/
	public Timer(int expireTime) {
		this.expireTime = expireTime;
		this.startTime  = 0;
		this.running    = false;
	}

	/** Create a timer without an expire time. **/
	public Timer() {
		this(0);
	}

	
	/** Start the timer. **/
	public void start() {
		startTime = getSystemTime();
		running   = true;
	}

	/** Stop the timer. **/
	public void stop() {
		running = false;
	}

	/** Set the timer's current time in milliseconds. **/
	public void setTime(long time) {
		startTime = getSystemTime() - time;
	}

	/** Set the timer's expire time in milliseconds. **/
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	
	
	/** Is the timer running? **/
	public boolean isRunning() {
		return running;
	}

	/** Has the timer past its expire time? **/
	public boolean isExpired() {
		return (getTime() >= expireTime);
	}

	/** Return whether the timer has past a given time. **/
	public boolean pastTime(long time) {
		return (getTime() >= time);
	}

	/** Get the timer's current time in milliseconds. **/
	public long getTime() {
		return (long) (getSystemTime() - startTime);
	}

	/** Get the timer's current time in seconds. **/
	public double getSeconds() {
		return ((double) getTime() / 1000.0);
	}

	/** Get the timer's current time in seconds. **/
	public double getMinutes() {
		return ((double) getTime() / 60000.0);
	}

	/** Return the percentage toward expiring. **/
	public double getPercentage() {
		if (getTime() < 0)
			return 0;
		if (getTime() >= expireTime)
			return 1;
		return ((double) getTime() / (double) expireTime);
	}

	/** Get the current system time in milliseconds. **/
	private long getSystemTime() {
		return System.currentTimeMillis();
	}
}
