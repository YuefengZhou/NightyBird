package com.example.nightybird;

import java.util.ArrayList;

import entities.SleepData;
import entities.SleepDataManager;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends Activity 
implements NavigationDrawerFragment.NavigationDrawerCallbacks{

	private NavigationDrawerFragment mNavigationDrawerFragment;

	ListView listView;

	SleepDataManager sleepDataManager = SleepDataManager.getInstance();

	SleepDataAdapter sleepDataAdapter;

	ArrayList<SleepData> sleepDataList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

//		if (savedInstanceState == null) {
//			getFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}

		// Set up the drawer
		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getFragmentManager().findFragmentById(R.id.navigation_drawer);

		if (mNavigationDrawerFragment != null){
			mNavigationDrawerFragment.setUp(
					R.id.navigation_drawer,
					(DrawerLayout) findViewById(R.id.drawer_layout_history));
		}

		listView = (ListView)findViewById(R.id.list);
		
		updateSleepDataList();
		sleepDataAdapter = new SleepDataAdapter();
		listView.setAdapter(sleepDataAdapter);

		registerForContextMenu(listView);
	}
	
	public void updateSleepDataList() {
		sleepDataList = sleepDataManager.getAllSleepData();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

		menu.setHeaderTitle("Options");
		menu.add(1, 1, 1, "Edit");
		menu.add(1, 2, 2, "Delete");
		menu.add(1, 3, 3, "Cancel");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.history, menu);
		return true;
	}

	public void deleteItem(int position) {
		sleepDataManager.deleteSleepData(sleepDataList.get(position).getSdid());
		sleepDataList.remove(position);
		sleepDataAdapter.notifyDataSetChanged();	
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo(); 

		int position = (int)info.id;
		switch(itemId) {
		case 1: /* Edit Item */
			Intent intent = new Intent(HistoryActivity.this, EditHistoryActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("SleepData", sleepDataList.get(position));
			intent.putExtras(b); 
			startActivityForResult(intent, 1);
			break;
		case 2: /* Delete item */
			deleteItem(position);
			break;
		case 3: /* Cancel */
			break;
		default:
			break;
		}

		return true;
	}

	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) {     
		super.onActivityResult(requestCode, resultCode, data); 

		SleepData sleepData = (SleepData) data.getSerializableExtra("SleepData");
		
		if (resultCode != Activity.RESULT_OK || sleepData == null)
			return;
		
		if (requestCode == 1) { /* return from an update */
			sleepDataManager.updateSleepData(sleepData);
			updateSleepDataList();
			Toast.makeText(this, "Updated.", Toast.LENGTH_SHORT).show();
		} else { /* return from an add */
			sleepDataManager.insertSleepData(sleepData);
			updateSleepDataList();
			Toast.makeText(this, "Added.", Toast.LENGTH_SHORT).show();
		}
		sleepDataAdapter.notifyDataSetChanged();
	}
	
	public void onAddButtonClick(View v) {
		Intent intent = new Intent(HistoryActivity.this, EditHistoryActivity.class);
		Bundle b = new Bundle();
		b.putSerializable("SleepData", new SleepData());
		intent.putExtras(b); 
		startActivityForResult(intent, 2);
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
			View rootView = inflater.inflate(R.layout.fragment_history,
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
		// TODO Auto-generated method stub
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
		.replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
		.commit();

	}

	/* adapter for the list view */
	public class SleepDataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return sleepDataList.size();
		}

		@Override
		public SleepData getItem(int arg0) {
			return sleepDataList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if(arg1==null) {
				LayoutInflater inflater = (LayoutInflater) HistoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				arg1 = inflater.inflate(R.layout.sleepdata_item, arg2, false);
			}

			TextView dateText = (TextView)arg1.findViewById(R.id.textView1);
			TextView sleepTimeText = (TextView)arg1.findViewById(R.id.textView2);

			SleepData sleepData = sleepDataList.get(arg0);
			
			String sleepTimeStr = SleepDataManager.getAbbrTime(sleepData.getStart()) + " - " + SleepDataManager.getAbbrTime(sleepData.getEnd());
			String DateStr = SleepDataManager.getAbbrDate(sleepData.getStart());

			sleepTimeText.setText("Your sleep time: " + sleepTimeStr);
			dateText.setText("On " + DateStr);

			return arg1;
		}

		public SleepData getSleepData(int position) {
			return sleepDataList.get(position);
		}

	}

}
