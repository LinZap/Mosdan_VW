package ui;

import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class Rx_Perference extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	private ListPreference situation;
	private EditTextPreference name, explain;
	private CheckBoxPreference usbmode, used_situation;
	private Picker_location pickerloc;
	private Preference ip, mac, port;

	public Rx_Perference(int i) {
		Data.index_rx = i;
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.france);

		usbmode = (CheckBoxPreference) findPreference("usbmode");
		name = (EditTextPreference) findPreference("name");
		explain = (EditTextPreference) findPreference("hint");
		situation = (ListPreference) findPreference("select_situation");
		pickerloc = (Picker_location) findPreference("select_location");
		used_situation = (CheckBoxPreference) findPreference("used_situation");
		ip = findPreference("ipaddress");
		mac = findPreference("macaddress");
		port = findPreference("port");

		name.setSummary(Data.Rx_name[Data.index_rx]);
		name.setText(Data.Rx_name[Data.index_rx]);

		// situation
		situation.setSummary(Data.Rx_sitsution[Data.index_rx]);
		situation.setValue(Data.Rx_sitsution[Data.index_rx]);

		if (Data.Rx_sitsution[Data.index_rx].equals("0"))
			used_situation.setChecked(false);
		else
			used_situation.setChecked(true);

		if (Data.Situation_name != null) {
			situation.setEntries(Data.Situation_name);
			situation.setEntryValues(Data.Situation_name);
		}

		// usb_mode, not yet
		usbmode.setEnabled(false);

		// hint
		explain.setSummary(Data.Rx_explain[Data.index_rx]);
		explain.setText(Data.Rx_explain[Data.index_rx]);

		// information ok
		ip.setSummary(Data.Rx_ip[Data.index_rx]);
		mac.setSummary(Data.Rx_mac[Data.index_rx]);
		port.setSummary("unknow");
		
		
		if(Data.Rx_status[Data.index_rx]==R.drawable.not_connection){
			name.setEnabled(false);
			explain.setEnabled(false);
			used_situation.setEnabled(false);
			used_situation.setChecked(false);
		}
		

	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {

		if (key.equals("name")) {

			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.TR_rename(Data.Rx_mac[Data.index_rx], name.getEditText()
							.getText().toString());
				}
			}.start();

			Data.Rx_name[Data.index_rx] = name.getEditText().getText()
					.toString();
			name.setSummary(name.getEditText().getText().toString());

			// 改變情境
		} else if (key.equals("select_situation")) {
			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.VW_Rx_set(Data.Rx_mac[Data.index_rx],situation.getValue().toString());
				}
			}.start();
			
			situation.setSummary(situation.getValue().toString());
			Data.Rx_sitsution[Data.index_rx] = situation.getValue().toString();
			// 改變情境後，設定新的組成數量與顯示位置改為預設(1,1)
			Data.Rx_sit_isChanged[Data.index_rx] = true;
			Data.Rx_locX[Data.index_rx] = 1;
			Data.Rx_locY[Data.index_rx] = 1;
			pickerloc.setSummary("1 , 1");

		} // 改變選擇是否套用情境
		else if(key.equals("used_situation")){
			
			if(used_situation.isChecked()){
				if(situation.getValue().toString().equals("0")){
					situation.setSummary(Data.Situation_name[0]);
					situation.setValue(Data.Situation_name[0]);
					Data.Rx_sitsution[Data.index_rx] = Data.Situation_name[0];		
				}				
				new Mycommand() {
					@Override
					public void command() {
						Turbo_View t = new Turbo_View();
						t.VW_Rx_set(Data.Rx_mac[Data.index_rx],situation.getValue().toString());
					}
				}.start();	
			}
			else{
				Data.Rx_sitsution[Data.index_rx] = "0";	
				situation.setSummary("0");
				situation.setValue("0");
				new Mycommand() {
					@Override
					public void command() {
						Turbo_View t = new Turbo_View();
						t.VW_Rx_set(Data.Rx_mac[Data.index_rx],"0");
					}
				}.start();	
			}
			
		}
		else if (key.equals("hint")) {
			new Mycommand() {
				@Override
				public void command() {
					Turbo_View t = new Turbo_View();
					t.TR_explain(Data.Rx_explain[Data.index_rx], explain
							.getEditText().getText().toString());
				}
			}.start();
			Data.Rx_explain[Data.index_rx] = explain.getEditText().getText()
					.toString();
			explain.setSummary(explain.getEditText().getText().toString());
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
