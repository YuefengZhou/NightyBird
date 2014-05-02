package ws.local;

import java.util.Timer;
import java.util.TimerTask;

import com.example.nightybird.MainActivity;
import com.example.nightybird.R;
import com.example.nightybird.SplashActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class StayupReminder {
	static int notifyID = 1;
    private static Timer timer;
    private static TimerTask task;
    public static int timeLauchReminder;
    public static int intervalTimeLauchReminder;
    private static ActivityManager activityManager;
    private static NotificationManager mNotificationManager ;
    private static Activity orgActivity = null;
    private static String message = null;
    
	public StayupReminder (Activity activity, String reminderMessage) {
		orgActivity = activity;
		activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		mNotificationManager = (NotificationManager) orgActivity.getSystemService(Context.NOTIFICATION_SERVICE);
		message = reminderMessage;
	}
	
	public void updateActivity (Activity activity) {
		orgActivity = activity;
	}
	
	public static void test(Activity activity, String message){
		StayupReminder reminder = new StayupReminder(activity, message);
		reminder.startReminder(1,5); // 1 minutes
		//notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	}
	
	public void startReminder(int time, int timeInterval) { // time: minutes
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
        timer.schedule(task, 1*1000, 1*1000);	// check every minutes
        //timer.schedule(task, time*60*1000, time*60*1000);
        System.out.println ("startReminder: set timer");
	}
	
	public void closeReminder() {
		if(null != timer){
			timer.cancel();
            task.cancel();
		}
	    System.out.println ("Reminder closed");
	}
	
	public void sendReminder(String reminderMessage){
		Notification notification = createNotification(reminderMessage);
		// Intent notificationIntent = new Intent(activity, MainActivity.class);
		// PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, notificationIntent, 0);
		mNotificationManager.notify(
	            notifyID,
	            notification);
	}
	
	public static Notification createNotification(String reminderText){
		NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder (getActivity())
	    .setContentTitle("NightyBird Reminder")
	    .setContentText(reminderText)
		.setSmallIcon(R.drawable.ic_launcher); // if there is no icon, the notification will not appear
		
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
