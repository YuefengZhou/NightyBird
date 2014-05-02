package ws.local;

import java.util.Timer;
import java.util.TimerTask;

import com.example.nightybird.MainActivity;
import com.example.nightybird.R;
import com.example.nightybird.ReminderActivity;
import com.example.nightybird.SplashActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class StayupReminder {
	static StayupReminder stayupReminder;
	static int notifyID = 1;
    private static Timer timer;
    private static TimerTask task;
    public static int timeLauchReminder;
    public static int intervalTimeLauchReminder;
    private static ActivityManager activityManager;
    private static NotificationManager mNotificationManager ;
    private static Activity orgActivity = null;
    private static String message = null;
    
    @SuppressWarnings("unused")
	private void stayupReminder () {
    }
    
    public static StayupReminder getInstance() {
    	if (stayupReminder != null){
    		return stayupReminder;
    	} else {
    		return new StayupReminder();
    	}
    }
    
    public void initiate (Activity activity, String reminderMessage) {
		orgActivity = activity;
		activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		mNotificationManager = (NotificationManager) orgActivity.getSystemService(Context.NOTIFICATION_SERVICE);
		message = reminderMessage;
	}
	
    public void update (Activity activity) {
		orgActivity = activity;
		activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		mNotificationManager = (NotificationManager) orgActivity.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public static String getReminderMessage(){
		return message;
	}
	
	public void updateActivity (Activity activity) {
		orgActivity = activity;
	}
	
	public static void test(Activity activity, String message){
		StayupReminder reminder = StayupReminder.getInstance();
		reminder.initiate(activity, message);
		reminder.startReminder(1,5); // 1 minutes
		//notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	}
	
	public void startReminder(int time, int timeInterval) { // time: seconds
		timeLauchReminder = time;
		intervalTimeLauchReminder = timeInterval;
		closeReminder();
		timer = new Timer();
        task = new TimerTask() {
        	int timeCounter = 0;
        	int interval =0;
        	@Override
            public void run() {
        		timeCounter++;
        		if (timeCounter>= timeLauchReminder){
        			if (interval==0){
        				sendReminder(message);
        			}
        			interval++;
        			if (interval>=intervalTimeLauchReminder){
        				interval = 0;
        			}
        		}
            }
        };
        timer.schedule(task, 1*1000, 1*1000);	// check every 1 seconds
        //timer.schedule(task, time*60*1000, time*60*1000);
		sendReminder("Reminder started");
        System.out.println ("startReminder: set timer");
	}
	
	public void closeReminder() {
		if(null != timer){
			timer.cancel();
            task.cancel();
    	    System.out.println ("Reminder closed");
    	    AlertDialog.Builder builder1 = new AlertDialog.Builder(orgActivity);
	        builder1.setMessage("Reminder is closed");
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
		timer = null;
		task = null;
	}
	
	public void sendReminder(String reminderMessage){
		Notification notification = createNotification(reminderMessage);
		mNotificationManager.notify(
	            notifyID,
	            notification);
	}
	
	public static Notification createNotification(String reminderText){
		NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder (getActivity())
	    .setContentTitle("NightyBird Reminder")
	    .setContentText(reminderText)
		.setSmallIcon(R.drawable.ic_launcher); // if there is no icon, the notification will not appear
		
		// set intent for the notification
		Intent resultIntent = new Intent(orgActivity, ReminderActivity.class);
		// TaskStackBuilder stackBuilder = TaskStackBuilder.create(orgActivity);
		// stackBuilder.addParentStack(ReminderActivity.class);
		// stackBuilder.addNextIntent(resultIntent);
		// PendingIntent resultPendingIntent = stackBuilder.getPendingIntent( 0, PendingIntent.FLAG_UPDATE_CURRENT );
		
		PendingIntent resultPendingIntent = PendingIntent.getActivity(orgActivity, 0, resultIntent, 0);
		mNotifyBuilder.setContentIntent(resultPendingIntent);
		
		Notification notification = mNotifyBuilder.build();
		// notification.defaults |= Notification.DEFAULT_ALL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE ;
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_AUTO_CANCEL; // disappear after click
		return notification;
	}
	
	public static Activity getActivity(){
		return orgActivity;
	}
	

}
