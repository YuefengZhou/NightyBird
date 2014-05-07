package entities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;


public class TimeManager {
	final static boolean DEBUG_TIME = true;
	public final static int SLEEP = 0;
	public final static int WAKEUP = 1;
	public final static int STAYUP = 2;
	
	private int requiredSleepHour = 23; // the default time user should go to sleep
	private static TimeManager timeManager = null;
	private  Context context;
	private boolean userSleeping;
	
	// start from 08:00 daytimeStart should < 12 (noon)
	public final static int daytimeStart = 6; 
	
	// start from 20:00. nighttimeStart should > 12 (noon)
	public final static int nighttimeStart = 20;
	
	// timer and task for sleep monitor
	Timer timer;
	TimerTask task;

	private TimeManager() {
	}
	
	public void setStayupTimeThreshold ( int stayuplateThreshold ) {
		System.out.println("Set requiredSleepHour to: " + stayuplateThreshold);
		requiredSleepHour = stayuplateThreshold;
	}

	public static TimeManager getInstance() {
		if ( timeManager == null ) {
			timeManager = new TimeManager();
		}
		return timeManager;
	}
	
	public int getStatus() {
		Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		System.out.println("REQUIRED SLEEP HOUR: " + requiredSleepHour);
		if (requiredSleepHour < 12) { //after midnight
			if (userSleeping)
				return TimeManager.SLEEP;
			else if (currentHour >= requiredSleepHour && currentHour < daytimeStart) {
				System.out.println("Staying up");
				return TimeManager.STAYUP;
			}
			else 
				return TimeManager.WAKEUP;
		}
		else {
			if (userSleeping)
				return TimeManager.SLEEP;
			else if (currentHour >= requiredSleepHour || currentHour < daytimeStart) {
				System.out.println("Staying up");
				return TimeManager.STAYUP;
			}
			else
				return TimeManager.WAKEUP;
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	public static int getHourFromDate(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH");
		return Integer.parseInt(dateFormat.format(date));
	}
	
	@SuppressLint("SimpleDateFormat")
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
	
	public int calDelay() {
		Calendar c = Calendar.getInstance();
		int currentHour = c.get(Calendar.HOUR_OF_DAY);
		int currentMinute = c.get(Calendar.MINUTE);
		
		int targetHour = requiredSleepHour;
		if (requiredSleepHour < 12)
			targetHour += 24;
		if (currentHour < 12)
			currentHour += 24;
		return (targetHour - currentHour) * 60 - currentMinute;
	}
	public void userSleeping() {
		this.userSleeping = true;
	}
	public void userWakeup() {
		this.userSleeping = false;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
}
