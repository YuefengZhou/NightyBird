package ws.local;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.TextView;
import entities.PreferenceManager;
import entities.SleepData;

public class ReportServiceClient extends AsyncTask<ArrayList<SleepData>, Void, String> {
	private String ServerIP;
	private TextView textViewToUpdate;
	
	public String getDailyReport(SleepData sleepData) {
		ServerIP = PreferenceManager.getInstance().getReportServiceAddress();
		return getReport(ServerIP + "daily?username=" + PreferenceManager.getInstance().getUsername()
										+ "&start=" + sleepData.getStart().getTime()
										+ "&end=" + sleepData.getEnd().getTime());
	}
	
	public String getWeeklyReport(ArrayList<SleepData> sleepDataList) {
		if (sleepDataList.size() < 7)
			return null;
		
		ServerIP = PreferenceManager.getInstance().getReportServiceAddress();
		StringBuffer uri = new StringBuffer(ServerIP);
		uri.append("weekly?username=");
		uri.append(PreferenceManager.getInstance().getUsername());
		
		for(int i = 0; i < 7; i++) {
			uri.append("&start" + Integer.toString(i+1) + "=" + sleepDataList.get(i).getStart().getTime());
			uri.append("&end" + Integer.toString(i+1) + "=" + sleepDataList.get(i).getEnd().getTime());
		}
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
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		int code = response.getStatusLine().getStatusCode();
		if (code == 200) {
			InputStream is = null;
			try {
				is = response.getEntity().getContent();
			} catch (IllegalStateException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			String result = convertStreamToString(is);
			System.out.println(result);
			try {
				myObject = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			System.out.println("RETURN CODE:" + code);
			return null;
		}
		try {
			return myObject.getString("report");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
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
		if (result == null)
			textViewToUpdate.setText("Report Service Problem: please check your server ip setting or remote server!");
		else
			textViewToUpdate.setText(result);
    }

	public void setTextViewToUpdate(TextView textViewToUpdate) {
		this.textViewToUpdate = textViewToUpdate;
	}
}
