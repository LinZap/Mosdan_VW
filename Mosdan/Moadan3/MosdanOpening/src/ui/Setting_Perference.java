package ui;

import Data.Data;
import TV.Mosdan2.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class Setting_Perference extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	private EditTextPreference domain;
	private CheckBoxPreference syncdata;
	private Preference opensource;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);

		domain = (EditTextPreference) findPreference("domain");
		syncdata = (CheckBoxPreference) findPreference("syncdata");
		domain.setSummary(Data.system_server);
		domain.setText(Data.system_server);
		syncdata.setChecked(Data.isauto_getData);
		opensource = findPreference("manual");
		Intent i = new Intent(Intent.ACTION_VIEW,
				Uri.parse("http://github.com/st937072012/Mosdan_VW/wiki"));
		opensource.setIntent(i);

	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		if (key.equals("syncdata")) {
			Data.isauto_getData = syncdata.isChecked();
		} else if (key.equals("domain")) {
			Data.system_server = domain.getEditText().getText().toString();
			domain.setSummary(Data.system_server);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

}
