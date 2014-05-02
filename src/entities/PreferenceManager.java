package entities;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
	private static PreferenceManager instance = null;
	private static SharedPreferences sharedPref;
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
	public void setStayupThreshold(int stayupThreshold) {
		sharedPref.edit().putString("threshold", Integer.toString(stayupThreshold));
		sharedPref.edit().commit();
	}
	public int getRemindPeriod() {
		return Integer.parseInt(sharedPref.getString("period", "30"));
	}
	public void setRemindPeriod(int remindPeriod) {
		sharedPref.edit().putString("period", Integer.toString(remindPeriod));
		sharedPref.edit().commit();
	}
	public String getUsername() {
		return sharedPref.getString("name", "Darling");
	}
	public void setUsername(String username) {
		sharedPref.edit().putString("name", username);
		sharedPref.edit().commit();
	}
	public String getReportServiceAddress() {
		return sharedPref.getString("service_address", "");
	}
//	public String getGender() {
//		return gender;
//	}
//	public void setGender(String gender) {
//		this.gender = gender;
//	}
	public void setContext(Context context) {
		this.context = context;
		sharedPref = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
	}
}
