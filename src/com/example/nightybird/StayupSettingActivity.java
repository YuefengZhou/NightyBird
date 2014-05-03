package com.example.nightybird;

import entities.StayupReminder;
import entities.TimeManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

public class StayupSettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stayup_setting);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stayup_setting, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_stayup_setting,
					container, false);
			return rootView;
		}
	}
	
	public void clickHandler_stayup_setting_done (View v){
		TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker1);
		int setHour = timePicker.getCurrentHour();
		int setMinute = timePicker.getCurrentMinute();
		System.out.println("Time for reminder: " + setHour + " hour, " + setMinute + " minute");
		
		int currentHour = TimeManager.getCurrentHour();
		int currentMinute = TimeManager.getCurrentMinute();
		if (setHour<TimeManager.daytimeStart) {
			setHour += 24;
		}
		if (currentHour<TimeManager.daytimeStart) {
			currentHour += 24;
		}
		int timeLeftForReminder = (setHour-currentHour)*60 + setMinute - currentMinute;
		System.out.println("timeLeftForReminder: (minutes) " + timeLeftForReminder);
		String setFeedback = null;
		if ( timeLeftForReminder >= 0 ){
			System.out.println("set reminder"); 
			StayupReminder reminder = StayupReminder.getInstance();
			reminder.initiate(this, "Time up to sleep");
			setFeedback = "Reminder started successfully";
			reminder.startStayuplateReminder(timeLeftForReminder); // minutes
		} else {
			setFeedback = "Time left is " + timeLeftForReminder + ". Failed to set the reminder. Reminding Time should be less than 12 hours from now.";
		}
		
		//Pop an alert
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(setFeedback);
        builder1.setCancelable(true);
        builder1.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert1 = builder1.create();
        alert1.show();

	}
}
