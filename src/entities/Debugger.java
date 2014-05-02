package entities;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

public class Debugger {
	public static void printDate(Context context, Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm, dd/MM/yyyy");
		
		makeToast(context, 
				dateFormat.format(date));
	}
	public static void makeToast(Context context, String str) {
		Toast.makeText(context, 
				str, Toast.LENGTH_LONG).show();
	}
}
