package com.example.nightybird;

import entities.PreferenceManager;
import entities.Reminder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ReminderActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reminder);

//		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
		
		TextView reminderDisplay = (TextView) findViewById(R.id.reminderMessage);
		String reminderText = Reminder.reminderMessage;
		if ( reminderText!= null) {
			reminderDisplay.setText(reminderText);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reminder, menu);
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
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_reminder,
//					container, false);
//			return rootView;
//		}
//	}
	
	public void clickHandler_close_reminder(View v){
		PreferenceManager.getInstance().setReminderStatus(false);
		
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Reminder closed for today.");
        builder1.setCancelable(true);
        builder1.setNegativeButton("OK",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                finish();
            }
        });
        AlertDialog alert1 = builder1.create();
        alert1.show();
	}

}
