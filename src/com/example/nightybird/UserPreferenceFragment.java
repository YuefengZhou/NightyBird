package com.example.nightybird;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class UserPreferenceFragment extends PreferenceFragment {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.preference);
 
    }
}
