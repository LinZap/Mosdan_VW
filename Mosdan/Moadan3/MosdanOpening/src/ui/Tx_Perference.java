package ui;

import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class Tx_Perference extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {

	private ListPreference tx_mode, ati_mode;
	private EditTextPreference name, explain;
	private Preference ip, mac, port;

	public Tx_Perference(int i) {
		Data.index_tx = i;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.france2);

		// find element

		name = (EditTextPreference) findPreference("name");
		explain = (EditTextPreference) findPreference("hint");
		tx_mode = (ListPreference) findPreference("tx_mode");
		ati_mode = (ListPreference) findPreference("ati_mode");
		ip = findPreference("ipaddress");
		mac = findPreference("macaddress");
		port = findPreference("port");

		if (Data.Tx_status[Data.index_tx] == R.drawable.not_connection) {
			name.setEnabled(false);
			explain.setEnabled(false);
			tx_mode.setEnabled(false);
			ati_mode.setEnabled(false);
		}

		name.setSummary(Data.Tx_name[Data.index_tx]);
		name.setText(Data.Tx_name[Data.index_tx]);

		explain.setSummary(Data.Tx_explain[Data.index_tx]);
		explain.setText(Data.Tx_explain[Data.index_tx]);

		// information ok
		ip.setSummary(Data.Tx_ip[Data.index_tx]);
		mac.setSummary(Data.Tx_mac[Data.index_tx]);
		port.setSummary("unknow");

		tx_mode.setSummary(Data.Tx_mode[Data.index_tx]);
		ati_mode.setSummary(Data.Tx_ati_mode[Data.index_tx]);
		tx_mode.setValue(Data.Tx_mode[Data.index_tx]);
		ati_mode.setValue(Data.Tx_ati_mode[Data.index_tx]);

	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

		if (key.equals("name")) {

			String s = name.getEditText().getText().toString();

			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.TR_rename(Data.Tx_mac[Data.index_tx], name.getEditText()
							.getText().toString());
				}
			}.start();

			for (int i = 0; i < Data.Situation_srctx.length; i++)
				if (Data.Tx_name[Data.index_tx].equals(Data.Situation_srctx[i]))
					Data.Situation_srctx[i] = s;
			Data.Tx_name[Data.index_tx] = s;
			name.setSummary(s);

		} else if (key.equals("hint")) {

			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.TR_explain(Data.Tx_mac[Data.index_tx], explain
							.getEditText().getText().toString());
				}
			}.start();

			Data.Tx_explain[Data.index_tx] = explain.getEditText().getText()
					.toString();
			explain.setSummary(explain.getEditText().getText().toString());
		}

		else if (key.equals("tx_mode")) {

			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.Tx_setMode(Data.Tx_mac[Data.index_tx], tx_mode.getValue()
							.toString().equals("Graphic") ? 0 : 1);
				}
			}.start();

			Data.Tx_mode[Data.index_tx] = tx_mode.getValue().toString();
			tx_mode.setSummary(tx_mode.getValue());

		}

		else if (key.equals("ati_mode")) {

			new Mycommand() {
				@Override
				public void command() {
					int i = 0;
					String ati = ati_mode.getValue().toString();
					if (ati.equals("OFF"))
						i = 0;
					else if (ati.equals("Type 1"))
						i = 1;
					else
						i = 2;
					Turbo_View t = new Turbo_View();
					t.Tx_setATIMode(Data.Tx_mac[Data.index_tx], i);
				}
			}.start();

			Data.Tx_ati_mode[Data.index_tx] = ati_mode.getValue().toString();
			ati_mode.setSummary(ati_mode.getValue());

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
