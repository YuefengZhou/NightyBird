package com.example.nightybird;

import android.app.Activity;
import android.os.Bundle;

public class SettingActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new UserPreferenceFragment())
                .commit();
    }
}
