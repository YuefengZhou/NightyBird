package entities;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceManager {
	private static PreferenceManager instance = null;
	private static SharedPreferences sharedPref;
	@SuppressWarnings("unused")
	private Context context;
	
	private PreferenceManager() {
	}
	
	public static PreferenceManager getInstance() {
		if (instance == null)
			instance = new PreferenceManager();
		return instance;
	}
	public int getStayupThreshold() {
		return Integer.parseInt(sharedPref.getString("threshold", "23"));
	}
	public int getRemindPeriod() {
		return Integer.parseInt(sharedPref.getString("period", "30"));
	}
	public String getUsername() {
		return sharedPref.getString("name", "Darling");
	}
	public String getReportServiceAddress() {
		return sharedPref.getString("service_address", "");
	}
	public String getGender() {
		return sharedPref.getString("gender", "male");
	}
	public void setContext(Context context) {
		this.context = context;
		sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
	}
	public void checkinSleep() {
		Calendar c = Calendar.getInstance();
		Date currentTime = c.getTime();
		Editor ed = sharedPref.edit();
		
		ed.putString("starttime", Long.toString(currentTime.getTime()));
		ed.commit();
	}
	public void checkoutSleep() {
		Date sleepTime = new Date(Long.parseLong(sharedPref.getString("starttime", "0")));
		Date wakeupTime = Calendar.getInstance().getTime();
		SleepDataManager.getInstance().insertSleepData(new SleepData(sleepTime, wakeupTime));
	}
	public void setReminderStatus(boolean status) {
		Editor ed = sharedPref.edit();
		ed.putString("reminderstatus", Boolean.toString(status));
		ed.commit();
	}
	public boolean getReminderStatus() {
		return Boolean.parseBoolean(sharedPref.getString("reminderstatus", "true"));
	}
}
