package com.example.nightybird;

import java.util.Calendar;
import java.util.Date;

import dblayout.SleepData;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.os.Build;

public class EditHistoryActivity extends Activity {

	private int sdid;
	DatePicker datePicker;
	TimePicker timePicker1, timePicker2;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_history);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		datePicker = (DatePicker)findViewById(R.id.datePicker);
		timePicker1 = (TimePicker)findViewById(R.id.timePicker1);
		timePicker2 = (TimePicker)findViewById(R.id.timePicker2);
		
		Bundle b = getIntent().getExtras();
		
		SleepData sleepData = (SleepData)b.getSerializable("SleepData");
		if (sleepData != null) {
			sdid = sleepData.getSdid();
			
//			Debugger.printDate(this, sleepData.getStart());
//			Debugger.printDate(this, sleepData.getEnd());
			
//			Debugger.makeToast(this, Integer.toString(sleepData.getStart().getMonth() + 1));
//			Debugger.makeToast(this, Integer.toString(sleepData.getStart().getDate()));
			
			datePicker.updateDate(sleepData.getStart().getYear() + 1900, sleepData.getStart().getMonth(), sleepData.getStart().getDate());
			
			timePicker1.setCurrentHour(sleepData.getStart().getHours());
			timePicker1.setCurrentMinute(sleepData.getStart().getMinutes());
			
			timePicker2.setCurrentHour(sleepData.getEnd().getHours());
			timePicker2.setCurrentMinute(sleepData.getEnd().getMinutes());
		} else {
			//TODO: set a default time and date
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_history, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_edit_history,
					container, false);
			return rootView;
		}
	}
	
	private Date getDateFromDatePicker(DatePicker datePicker){
	    int day = datePicker.getDayOfMonth();
	    int month = datePicker.getMonth();
	    int year =  datePicker.getYear();

	    Calendar calendar = Calendar.getInstance();
	    calendar.set(year, month, day);
	    
	    return calendar.getTime();
	}
	
	private Date getDateFromTimePicker(DatePicker datePicker, TimePicker timePicker) {
		int day = datePicker.getDayOfMonth();
	    int month = datePicker.getMonth();
	    int year =  datePicker.getYear();
	    int hour = timePicker.getCurrentHour();
	    int minute = timePicker.getCurrentMinute();
	    
	    Calendar calendar = Calendar.getInstance();
	    calendar.set(year, month, day, hour, minute);
	    
	    return calendar.getTime();
	}
	
	public void onBackPressed() {
		setResult(Activity.RESULT_CANCELED, new Intent());
		finish();
	}
	
	public void onOKButtonClick(View v) {
		DatePicker datePicker = (DatePicker)findViewById(R.id.datePicker);
		TimePicker timePicker1 = (TimePicker)findViewById(R.id.timePicker1);
		TimePicker timePicker2 = (TimePicker)findViewById(R.id.timePicker2);
		
		//TODO: validity check for timepickers
		
		SleepData sleepData = new SleepData(sdid, getDateFromTimePicker(datePicker, timePicker1), getDateFromTimePicker(datePicker, timePicker2));
		
		Intent resultIntent = new Intent();
		Bundle b = new Bundle();
		b.putSerializable("SleepData", sleepData);
		resultIntent.putExtras(b); 
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
		
	}

}
