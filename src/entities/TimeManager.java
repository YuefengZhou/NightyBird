package entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import ws.local.StayupReminder;


public class TimeManager {
	final static boolean DEBUG_TIME = true;
	
	private static int requiredSleepHour = 23, requiredSleepMinute = 0; // the time user should go to sleep
	private static TimeManager timeManager;
	public boolean sleepReminderStatus = false; // true means turn on; false turn off
	private static Activity currentActivity;
	
	// start from 08:00 daytimeStart should < 12 (noon)
	public static int daytimeStart = 8; 
	
	// start from 20:00. nighttimeStart should > 12 (noon)
	public static int nighttimeStart = 20;
	
	// timer and task for sleep monitor
	Timer timer;
	TimerTask task;
	

	private void TimeManager() {
		
	}
	
	public static void setStayupTimeThreshold ( int stayuplateThreshold ) {
		requiredSleepHour = requiredSleepHour;
		requiredSleepMinute = 0;
	}

	public static TimeManager getInstance(Activity activity) {
		if ( timeManager == null ) {
			timeManager = new TimeManager();
		}
		currentActivity = activity;
		return timeManager;
	}
	
	public static boolean isSleepTime(Date currentDate)
	{
		int currentHour = getHourFromDate (currentDate);
		int currentMinute = getMinuteFromDate (currentDate);
		
		/*
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
		*/
		if ( currentHour == requiredSleepHour && currentMinute == requiredSleepMinute){
			return true;
		} else {
			return false;
		}
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
	
	public void startSleepMonitor () {
		sleepReminderStatus = true;
		timer = new Timer();
		task = new TimerTask() {
	    	int timeCounter = 0;
	    	@Override
	        public void run() { 
	    		if (timeCounter > 0) {
	    			timeCounter--;
	    		} else {
	    			if ( sleepReminderStatus && TimeManager.isSleepTime (new Date()) 
	    					&& (StayupReminder.stayupReminderStatus == false) ){
	    				timeCounter = 60;
	    				StayupReminder reminder = StayupReminder.getInstance();
	    				reminder.initiate(currentActivity, "Time up to sleep");
	    				reminder.startReminder(0); // 1 minutes
	    			}
	    		}
	        }
	    };
	    timer.schedule(task, 10*1000, 10*1000);	// check every 20 seconds
	}
	
	public void closeSleepMonitor (){
		if (timer!=null){
			timer.cancel();
			task.cancel();
			sleepReminderStatus = false;
		}
	}
	
}
