package ws.local;

import java.util.Timer;
import java.util.TimerTask;

import com.example.nightybird.MainActivity;
import com.example.nightybird.R;
import com.example.nightybird.SplashActivity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class StayupReminder {
	static int notifyID = 0;
    private Timer timer;
    private TimerTask task;
    
	public StayupReminder () {
		
	}
	
	public static void test(Activity activity, String message){
		StayupReminder reminder = new StayupReminder();
		reminder.sendReminder(activity);
				//notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	}
	
	public void startReminder(Activity activity, int time) { // time: minutes
		timer = new Timer();
        task = new TimerTask() {
        	@Override
            public void run() {
        		System.out.println ("timer: begin jump");
        		sendReminder(getActivity());
            }
        };
        timer.schedule(task, time*60*1000, time*60*1000);
        System.out.println ("startReminder: set timer");
	}
	
	public void closeReminder() {
		if(null != timer){
			timer.cancel();
            task.cancel();
		}
	    System.out.println ("Reminder closed");
	}
	
	public void sendReminder(Activity activity){
		System.out.println("start reminder");
		NotificationManager mNotificationManager = 
				(NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = createNotification(activity, "Reminder: timeout");
		// Intent notificationIntent = new Intent(activity, MainActivity.class);
		// PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, notificationIntent, 0);
		System.out.println("built notification");
		mNotificationManager.notify(
	            notifyID,
	            notification);
		System.out.println("notify notification");
	}
	
	public void updateReminder(Activity activity, String reminderText){
		NotificationManager mNotificationManager = 
				(NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);  
		
		mNotificationManager.notify(
	            notifyID,
	            createNotification(activity, "Hi, timeup!"));
	}
	
	public Notification createNotification(Activity activity, String reminderText){
		Notification.Builder mNotifyBuilder = new Notification.Builder (activity)
	    .setContentTitle("NightyBird Reminder")
	    .setContentText(reminderText)
		.setSmallIcon(R.drawable.ic_launcher); // if there is no icon, the notification will not appear
		
		Notification notification = mNotifyBuilder.build();
		notification.defaults |= Notification.DEFAULT_ALL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE ;
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_AUTO_CANCEL; // disappear after click
		return notification;
	}
	
	public Activity getActivity(){
		return null;
	}
	

}
