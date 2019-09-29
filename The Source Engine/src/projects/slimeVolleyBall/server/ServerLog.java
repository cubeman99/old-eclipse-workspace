package projects.slimeVolleyBall.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerLog {
	public static final String MSGO    = "[MSGO]";
	public static final String MSGI    = "[MSGI]";
	public static final String INFO    = "[INFO]";
	public static final String CHAT    = "[CHAT]";
	public static final String ERROR   = "[ERROR]";
	public static final String WARNING = "[WARNING]";
	
	
	public static void log(String text) {
		log(INFO, text);
	}

	public static void logInfo(String text) {
		log(INFO, text);
	}
	public static void logChat(String text) {
		log(CHAT, text);
	}
	public static void logMessageOut(String text) {
		log(MSGO, text);
	}
	public static void logMessageIn(String text) {
		log(MSGI, text);
	}
	public static void logError(String text) {
		log(ERROR, text);
	}
	public static void logWarning(String text) {
		log(WARNING, text);
	}
	
	public static void log(String type, String text) {
		System.out.println(getTimeStamp() + " " + type + " " + text);
	}
	
	private static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat ("HH:mm:ss");
		return dateFormat.format(date);
	}
}
