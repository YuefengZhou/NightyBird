package com.example.nightybird;

import java.util.ArrayList;

import entities.PreferenceManager;
import entities.SleepData;
import entities.SleepDataManager;
import ws.local.ReportServiceClient;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WakeupActivity extends Activity
implements NavigationDrawerFragment.NavigationDrawerCallbacks{

	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	ArrayList<SleepData> sleepDataList = new ArrayList<SleepData>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wakeup);

		/*
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
		
		// Set up the drawer
		mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
		
		if (mNavigationDrawerFragment != null){
			mNavigationDrawerFragment.setUp(
	                R.id.navigation_drawer,
	                (DrawerLayout) findViewById(R.id.drawer_layout_wakeup));
		}
		
		TextView greetingText = (TextView)findViewById(R.id.textViewGreeting);
		greetingText.setText("Hi, " + PreferenceManager.getInstance().getUsername() + ", how are you doing today?");
		
		showAdvice();
		
	}
	
	@SuppressWarnings("unchecked")
	private void showAdvice() {
		ArrayList<SleepData> tmpList = SleepDataManager.getInstance().getAllSleepData();
		
		if (tmpList.size() < 1)
			return;
		
		ArrayList<SleepData> oneDataList = new ArrayList<SleepData>();
		
		oneDataList.add(tmpList.get(tmpList.size()-1));
		
		TextView adviceText = (TextView)findViewById(R.id.textViewDailyReport);
		ReportServiceClient client = new ReportServiceClient();
		client.setTextViewToUpdate(adviceText);
		client.execute(oneDataList);
	}
	
	@Override
	protected void onStart() {
        super.onStart();
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wakeup, menu);
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
		private static final String ARG_SECTION_NUMBER = "section_number";

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_wakeup,
					container, false);
			return rootView;
		}
		
		public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
	}
	
	public void onClickGotoSleep (View v){
		PreferenceManager.getInstance().checkinSleep();
    	Intent intent = new Intent(); 
    	intent.setClass(this, SleepActivity.class); 
    	startActivity(intent); 
    	this.finish();
	}

}
