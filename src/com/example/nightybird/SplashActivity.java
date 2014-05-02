package com.example.nightybird;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import dblayout.SleepData;
import dblayout.SleepDataManager;
import entities.PreferenceManager;
import entities.TimeManager;
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
import android.os.Build;

public class SplashActivity extends Activity {

    public static Intent initialJumpIntent;
    private Timer timer;
    private TimerTask task;
    public static SplashActivity splashActivityClass;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        
        // Jump to sleep or wakeup page based on time
        // mainActivityClass = this;
        System.out.println ("main: before jump to other activity");
        TimeManager timeManager = new TimeManager();
        initialJumpIntent = new Intent(); 
        if (timeManager.isDaylight()){
        	initialJumpIntent.setClass(this, WakeupActivity.class);
        } else {
        	initialJumpIntent.setClass(this, StayupActivity.class);
        }
        //startActivity(initialJumpIntent);
        //this.finish();
        
        
        // used to show a start view
        splashActivityClass = this;
        timer = new Timer();
        task = new TimerTask() {
        	@Override
            public void run() {
        		System.out.println ("timer: begin jump");
                startActivity(initialJumpIntent);
                SplashActivity.splashActivityClass.finish();
            }
        };
        timer.schedule(task, 1000*2); // jump after 2 s
        System.out.println ("main: set timer");
        
		// SleepDataManager is used to read and set sleep data
        SleepDataManager sleepDataManager = SleepDataManager.getInstance();
        sleepDataManager.setContext(this);
		// PreferenceManager includes preferrence information and user information
        PreferenceManager.getInstance().setContext(this);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // Initiate sleep data
        if (sleepDataManager.getAllSleepData().size() < 7){
        	try {
    			sleepDataManager.insertSleepData( new SleepData
    					(format.parse("2014-04-26 23:55"), format.parse("2014-04-26 6:30") ));
    			sleepDataManager.insertSleepData( new SleepData
    	        		(format.parse("2014-04-26 23:58"), format.parse("2014-04-27 6:02") ));
    	        sleepDataManager.insertSleepData( new SleepData
    	        		(format.parse("2014-04-28 00:13"), format.parse("2014-04-28 6:07") ));
    	        sleepDataManager.insertSleepData( new SleepData
    	        		(format.parse("2014-04-28 23:58"), format.parse("2014-04-29 5:45") ));
    	        sleepDataManager.insertSleepData( new SleepData
    	        		(format.parse("2014-04-30 00:15"), format.parse("2014-04-30 6:21") ));
    	        sleepDataManager.insertSleepData( new SleepData
    	        		(format.parse("2014-05-01 01:22"), format.parse("2014-05-01 7:22") ));
    	        sleepDataManager.insertSleepData( new SleepData
    	        		(format.parse("2014-05-01 23:22"), format.parse("2014-05-02 6:10") ));
    	        System.out.println ("main: set sleep data");
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}	
        }
    }
	
    @Override
    protected void onPause() {
        super.onPause();
        if(null != timer){
            timer.cancel();
            task.cancel();
        }
        System.out.println ("main: paused");
    }
    

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_splash,
					container, false);
			return rootView;
		}
	}

}
