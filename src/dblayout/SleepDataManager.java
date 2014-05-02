package dblayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import entities.Debugger;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SleepDataManager {
	private long date2Long(Date date) {
		return date.getTime();
	}
	private Context context;

	private static SleepDataManager instance = null;
	
	private static String[] monthName = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

	protected SleepDataManager() {
		
	}
	
	public static SleepDataManager getInstance() {
		if (instance == null)
			instance = new SleepDataManager();
		
		return instance;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	
	public boolean insertSleepData(SleepData d) {
		adjustSleepData(d);
		
		ContentValues values = new ContentValues();

		values.put(SleepDataProvider.STARTTIME, date2Long(d.getStart()));
		values.put(SleepDataProvider.ENDTIME, date2Long(d.getEnd()));

		Uri uri = context.getContentResolver().insert(SleepDataProvider.CONTENT_URI, values);

		return true;
	}
	public int deleteSleepData(int sdid) {
		int ret = context.getContentResolver().delete(SleepDataProvider.CONTENT_URI, "SDID=?", new String[]{Integer.toString(sdid)});
//		Debugger.makeToast(context, Integer.toString(ret));
		
		return ret;
	}
	public int updateSleepData(SleepData newSleepData) {
		adjustSleepData(newSleepData);
		
		ContentValues values = new ContentValues();
		
		values.put(SleepDataProvider.SLEEPDATAID, newSleepData.getSdid());
		values.put(SleepDataProvider.STARTTIME, date2Long(newSleepData.getStart()));
		values.put(SleepDataProvider.ENDTIME, date2Long(newSleepData.getEnd()));
		
		int ret = context.getContentResolver().update(SleepDataProvider.CONTENT_URI, values, "SDID=?", new String[]{Integer.toString(newSleepData.getSdid())});
		
//		Debugger.makeToast(context, Integer.toString(ret));
		return ret;
	}
	public ArrayList<SleepData> getAllSleepData() {
		ArrayList<SleepData> res = new ArrayList<SleepData>();
		
		String URL = "content://com.example.provider.SleepData/sleepdata";
		Uri sleepdata = Uri.parse(URL);
		
		Cursor c = context.getContentResolver().query(sleepdata, null, null, null, "StartTime");
		
		if (c.moveToFirst()) {
			do{
				res.add(new SleepData(
								c.getInt(c.getColumnIndex(SleepDataProvider.SLEEPDATAID)), 
								new Date(c.getLong(c.getColumnIndex(SleepDataProvider.STARTTIME))), 
								new Date(c.getLong(c.getColumnIndex(SleepDataProvider.ENDTIME))))
						);
			} while (c.moveToNext());
		}

		return res;
	}
	
	@SuppressWarnings("deprecation")
	public static double getHourDecimal(Date date) {
		int hour = date.getHours();
		int minute = date.getMinutes();
		
		return hour + minute / 60.0;
	}
	
	@SuppressWarnings("deprecation")
	public static String getAbbrDate(Date date) {
		int month = date.getMonth();
		int day = date.getDate();
		
		return monthName[month] + " " + Integer.toString(day);
	}
	
	public static String getAbbrTime(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		return simpleDateFormat.format(date);
	}
	
	public static void adjustSleepData(SleepData sleepData) {
		if (sleepData.getStart().getTime() - sleepData.getEnd().getTime() >= 0) {
			sleepData.getEnd().setDate(sleepData.getStart().getDate() + 1);
		}
	}

}
