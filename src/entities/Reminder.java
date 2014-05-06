package entities;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.nightybird.R;
import com.example.nightybird.ReminderActivity;

public class Reminder {
	public static Reminder instance = null;
	private int delay, period;
	private Timer timer;
	private TimerTask task;
	private Context context;
	private NotificationManager notificationManager;
	private final static int checkPeriod = 60 * 1000;
	public final static String reminderMessage = "Time to Sleep Now.";
	private Reminder() {

	}
	public synchronized void start() {
		timer = new Timer();
		task = new TimerTask() {
			int count = 0;
			public void run() {
				System.out.println("In timer task! delay = " + delay + ", count = " + count + ", period = " + period);
				synchronized(Reminder.getInstance()) {
					delay--;
					if (PreferenceManager.getInstance().getReminderStatus()) {
						if (delay == 0) {
							count = 0;
							sendReminderText(reminderMessage);
						}
						else if (delay < 0) {
							count ++;
							if (count >= period) {
								count = 0;
								sendReminderText(reminderMessage);
							}
						} else
							count = 0;

					}

				}
			}
		};
		timer.schedule(task, checkPeriod, checkPeriod);
	}
	public synchronized void stop() {
		if (timer != null) 
			timer.cancel();
		timer = null;
	}
	public static Reminder getInstance() {
		if (instance == null)
			instance = new Reminder();
		return instance;
	}
	public int getPeriod() {
		return period;
	}
	public synchronized void setPeriod(int period) {
		this.period = period;
	}
	public int getDelay() {
		return delay;
	}
	public synchronized void setDelay(int delay) {
		this.delay = delay;
	}
	public synchronized void setContext(Context context) {
		this.context = context;
		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void sendReminderText(String reminderMessage){
		Notification notification = createNotification(reminderMessage);
		notificationManager.notify(1, notification);
	}
	public Notification createNotification(String reminderText){
		NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context)
		.setContentTitle("NightyBird Reminder")
		.setContentText(reminderText)
		.setSmallIcon(R.drawable.ic_launcher); // if there is no icon, the notification will not appear

		// set intent for the notification
		Intent resultIntent = new Intent(context, ReminderActivity.class);	
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
		mNotifyBuilder.setContentIntent(resultPendingIntent);

		Notification notification = mNotifyBuilder.build();
		// notification.defaults |= Notification.DEFAULT_ALL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE ;
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.flags |= Notification.FLAG_AUTO_CANCEL; // disappear after click
		return notification;
	}

}
