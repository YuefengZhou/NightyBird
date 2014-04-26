package dblayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class SleepDataManager {
	private long date2Long(Date date) {
		return date.getTime() / 1000L;
	}
	private Context context;

	private static long strDateToUnixTimestamp(String dt) {
		DateFormat formatter;
		Date date = null;
		long unixtime;
		formatter = new SimpleDateFormat("dd/MM/yy");
		try {
			date = formatter.parse(dt);
		} catch (ParseException ex) {

			ex.printStackTrace();
		}
		unixtime = date.getTime() / 1000L;
		return unixtime;
	}

	public SleepDataManager(Context context) {
		this.context = context;
	}
	boolean insertSleepData(SleepData d) {
		ContentValues values = new ContentValues();

		values.put(SleepDataProvider.STARTTIME, date2Long(d.getStart()));
		values.put(SleepDataProvider.ENDTIME, date2Long(d.getEnd()));

		Uri uri = context.getContentResolver().insert(SleepDataProvider.CONTENT_URI, values);

		return true;
	}
	boolean deleteSleepData(int sdid) {
		return false;
	}
	ArrayList<SleepData> getAllSleepData() {
		ArrayList<SleepData> res = new ArrayList<SleepData>();
		
		String URL = "content://com.example.provider.SleepData/sleepdata";
		Uri sleepdata = Uri.parse(URL);
		Cursor c = context.getContentResolver().query(sleepdata, null, null, null, "StartTime");
		
		if (c.moveToFirst()) {
			do{
				res.add(new SleepData(
								c.getInt(c.getColumnIndex(SleepDataProvider.SLEEPDATAID)), 
								new Date(c.getInt(c.getColumnIndex(SleepDataProvider.STARTTIME))), 
								new Date(c.getInt(c.getColumnIndex(SleepDataProvider.ENDTIME))))
						);
			} while (c.moveToNext());
		}

		return res;
	}

}
