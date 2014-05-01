package entities;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
	private int stayupThreshold; // hour
	private int remindPeriod; // in minutes
	private boolean isGoingtoStayup;
	private boolean isStayingup;
	private String username;
	private String gender;
	private int age;
	private String occupation;
	private static Context context;
	private static PreferenceManager instance = null;
	private static SharedPreferences sharedPref;
	
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
	public void setStayupThreshold(int stayupThreshold) {
		sharedPref.edit().putString("threshold", Integer.toString(stayupThreshold));
		sharedPref.edit().commit();
	}
	public int getRemindPeriod() {
		return remindPeriod;
	}
	public void setRemindPeriod(int remindPeriod) {
		this.remindPeriod = remindPeriod;
	}
	public boolean isGoingtoStayup() {
		return isGoingtoStayup;
	}
	public void setGoingtoStayup(boolean isGoingtoStayup) {
		this.isGoingtoStayup = isGoingtoStayup;
	}
	public boolean isStayingup() {
		return isStayingup;
	}
	public void setStayingup(boolean isStayingup) {
		this.isStayingup = isStayingup;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public void setContext(Context context) {
		this.context = context;
		sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
	}
}
