package com.example.nightybird;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.os.Build;

public class ReportActivity extends Activity 
implements NavigationDrawerFragment.NavigationDrawerCallbacks{

	private NavigationDrawerFragment mNavigationDrawerFragment;
	
	private GraphicalView chartView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);

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
	                (DrawerLayout) findViewById(R.id.drawer_layout_report));
		}
		
		
		/*testing code*/
		double[] minValues = new double[] { -24, -19, -10, -1, 7, 12, 15, 14, 9, 1, -11, -16 };
	    double[] maxValues = new double[] { 7, 12, 24, 28, 33, 35, 37, 36, 28, 19, 11, 4 };

	    XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
	    RangeCategorySeries series = new RangeCategorySeries("Temperature");
	    int length = minValues.length;
	    for (int k = 0; k < length; k++) {
	      series.add(minValues[k], maxValues[k]);
	    }
	    dataset.addSeries(series.toXYSeries());
	    int[] colors = new int[] { Color.CYAN };
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	    setChartSettings(renderer, "Monthly temperature range", "Month", "Celsius degrees", 0.5, 12.5,
	        -30, 45, Color.GRAY, Color.LTGRAY);
	    renderer.setBarSpacing(0.5);
	    renderer.setXLabels(0);
	    renderer.setYLabels(10);
	    renderer.addXTextLabel(1, "Jan");
	    renderer.addXTextLabel(3, "Mar");
	    renderer.addXTextLabel(5, "May");
	    renderer.addXTextLabel(7, "Jul");
	    renderer.addXTextLabel(10, "Oct");
	    renderer.addXTextLabel(12, "Dec");
	    renderer.addYTextLabel(-25, "Very cold");
	    renderer.addYTextLabel(-10, "Cold");
	    renderer.addYTextLabel(5, "OK");
	    renderer.addYTextLabel(20, "Nice");
	    renderer.setMargins(new int[] {30, 70, 10, 0});
	    renderer.setYLabelsAlign(Align.RIGHT);
	    SimpleSeriesRenderer r = renderer.getSeriesRendererAt(0);
	    r.setDisplayChartValues(true);
	    r.setChartValuesTextSize(12);
	    r.setChartValuesSpacing(3);
	    r.setGradientEnabled(true);
	    r.setGradientStart(-20, Color.BLUE);
	    r.setGradientStop(20, Color.GREEN);
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
		chartView = ChartFactory.getRangeBarChartView(this, dataset, renderer, Type.DEFAULT);
		
		layout.addView(chartView, new LayoutParams(LayoutParams.MATCH_PARENT,
		          LayoutParams.MATCH_PARENT));
		
		chartView.repaint();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.report, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_report,
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
	
	private XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    renderer.setAxisTitleTextSize(16);
	    renderer.setChartTitleTextSize(20);
	    renderer.setLabelsTextSize(15);
	    renderer.setLegendTextSize(15);
	    int length = colors.length;
	    for (int i = 0; i < length; i++) {
	      SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	      r.setColor(colors[i]);
	      renderer.addSeriesRenderer(r);
	    }
	    return renderer;
	  }
	
	private void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
		      String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
		      int labelsColor) {
		    renderer.setChartTitle(title);
		    renderer.setXTitle(xTitle);
		    renderer.setYTitle(yTitle);
		    renderer.setXAxisMin(xMin);
		    renderer.setXAxisMax(xMax);
		    renderer.setYAxisMin(yMin);
		    renderer.setYAxisMax(yMax);
		    renderer.setAxesColor(axesColor);
		    renderer.setLabelsColor(labelsColor);
		  }

}
