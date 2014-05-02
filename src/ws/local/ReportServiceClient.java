package ws.local;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.example.nightybird.R;

import android.os.AsyncTask;
import android.widget.TextView;
import dblayout.SleepData;
import entities.PreferenceManager;

public class ReportServiceClient extends AsyncTask<ArrayList<SleepData>, Void, String> {
	private String ServerIP = PreferenceManager.getInstance().getReportServiceAddress();
	private TextView textViewToUpdate;
	
	public String getDailyReport(SleepData sleepData) {
		return getReport(ServerIP + "daily?username=" + PreferenceManager.getInstance().getUsername()
										+ "&start=" + sleepData.getStart().getTime()
										+ "&end=" + sleepData.getEnd().getTime());
	}
	
	public String getWeeklyReport(ArrayList<SleepData> sleepDataList) {
		if (sleepDataList.size() < 7)
			return null;
		
		StringBuffer uri = new StringBuffer(ServerIP);
		uri.append("weekly?username=");
		uri.append(PreferenceManager.getInstance().getUsername());
		
		for(int i = 0; i < 7; i++) {
			if (sleepDataList.get(i).getStart().getTime() - sleepDataList.get(i).getEnd().getTime() >= 0) {
				System.out.println("ERRORRRRRR: " + i);
			}
			uri.append("&start" + Integer.toString(i+1) + "=" + sleepDataList.get(i).getStart().getTime());
			uri.append("&end" + Integer.toString(i+1) + "=" + sleepDataList.get(i).getEnd().getTime());
		}
		System.out.println(uri.toString());
		return getReport(uri.toString());
	}
	public String getReport(String uri) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(uri);
		HttpResponse response = null;
		JSONObject myObject = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			InputStream is = null;
			try {
				is = response.getEntity().getContent();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String result = convertStreamToString(is);
			System.out.println("------------------");
			System.out.println(result);
			try {
				myObject = new JSONObject(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("--------------------");
		System.out.println(myObject);
		try {
			return myObject.getString("report");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	@Override
	protected String doInBackground(ArrayList<SleepData>... params) {
		ArrayList<SleepData> sleepDataList = params[0];
		
		if (sleepDataList.size() == 1) {
			return getDailyReport(sleepDataList.get(0));
		} else if (sleepDataList.size() >= 7) {
			return getWeeklyReport(sleepDataList);
		}
		return null;
	}
	
	protected void onPostExecute(String result) {
		textViewToUpdate.setText(result);
    }

	public void setTextViewToUpdate(TextView textViewToUpdate) {
		this.textViewToUpdate = textViewToUpdate;
	}
}
