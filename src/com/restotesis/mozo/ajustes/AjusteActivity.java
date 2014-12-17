package com.restotesis.mozo.ajustes;

import com.restotesis.mozo.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class AjusteActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.ajustes);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPref,
			String key) {
		if (key.equals("urlIP")) {
			Preference connectionPref = findPreference(key);
			connectionPref.setSummary(sharedPref.getString(key, ""));
		}
		if (key.equals("puerto")) {
			Preference connectionPref = findPreference(key);
			connectionPref.setSummary(sharedPref.getString(key, ""));
		}
	}

}
