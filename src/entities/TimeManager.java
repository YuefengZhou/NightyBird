package entities;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeManager {
	static int requiredSleepHour, requiredSleepMinute;
	final static boolean DEBUG_TIME = true;
	
	// start from 08:00 daytimeStart should < 12 (noon)
	public final static int daytimeStart = 8; 
	// start from 20:00. nighttimeStart should > 12 (noon)
	public final static int nighttimeStart = 20;

	
	public static void main(String[] args) {
		TimeManager t = new TimeManager();
		t.start();
	}
	
	public void start(){
		System.out.println("Current hours: " + getCurrentHour());
		System.out.println("Current minutes: " + getCurrentMinute());
	}
	
	public static boolean isSleepTime(Date currentDate, int requiredSleepHour, int requiredSleepMinute)
	{
		int currentHour = getHourFromDate (currentDate);
		int currentMinute = getMinuteFromDate (currentDate);
		
		if ( currentHour < daytimeStart) {
			// e.g. current date 02:10, required date 22:00
			currentHour += 24;
		}
		if ( requiredSleepHour < daytimeStart ) {
			// e.g. current date 23:10, required date 03:00
			currentHour -= 24;
		}
		
		// compare current time and required sleep time
		if ( currentHour > requiredSleepHour ) {
			// e.g. current date 23:10, required date 22:00
			return true;
		} else if (currentHour == requiredSleepHour) {
			if (currentMinute > requiredSleepMinute) {
				// e.g. current date 23:10, required date 23:00
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isNight() {
		int hour = getCurrentHour();
		if (DEBUG_TIME) System.out.println("isNight: currenthour:" + hour);
		
		if ( hour >= nighttimeStart ||  hour < daytimeStart){
			if (DEBUG_TIME) System.out.println("isnight: true");
			return true;
		}
		if (DEBUG_TIME)  System.out.println("isnight: false");
		return false;
	}
	
	public boolean isDaylight() {
		int hour = getCurrentHour();
		if (DEBUG_TIME) System.out.println("isDaylight: currenthour:" + hour);
		
		if ( hour >= daytimeStart && hour < nighttimeStart ){
			if (DEBUG_TIME) System.out.println("isDaylight: true");
			return true;
		}
		if (DEBUG_TIME) System.out.println("isDaylight: false");
		return false;
	}
	
	public static int getHourFromDate(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
		return Integer.parseInt(dateFormat.format(date));
	}
	
	public static int getMinuteFromDate(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("mm");
		return Integer.parseInt(dateFormat.format(date));
	}
	
	public static int getCurrentHour(){
		Date currentDate = new Date();
		return getHourFromDate(currentDate);
	}
	
	public static int getCurrentMinute(){
		Date currentDate = new Date();
		return getMinuteFromDate(currentDate);
	}
	

}
