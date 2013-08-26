package ui;

import Data.Data;
import TV.Mosdan2.R;
import activity.VW_add1;
import activity.VW_add2;
import activity.VW_add3;
import activity.VW_add4;
import activity.br_add1;
import activity.br_add2;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class Situation_Perference extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	public Preference name, bulid, srctx, member, drop;
	private Context context;
	private int mode;

	public Situation_Perference(Context c, int mode) {
		context = c;
		this.mode = mode;
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (mode == 1)
			addPreferencesFromResource(R.xml.france4);
		else
			addPreferencesFromResource(R.xml.france3);

		Bundle bundle = new Bundle();
		bundle.putInt("mode", 1);
		bundle.putInt("situation", Data.index_situation);

		srctx = findPreference("srctx");
		member = findPreference("member");
		srctx.setSummary(Data.Situation_srctx[Data.index_situation]);
		member.setSummary(memberString());

		if (mode != 1) {
			name = findPreference("name");
			bulid = findPreference("bulid");
			drop = findPreference("drop");
			name.setSummary(Data.Situation_name[Data.index_situation]);
			bulid.setSummary(Data.Situation_bulidx[Data.index_situation]
					+ " X " + Data.Situation_bulidy[Data.index_situation]);
			Intent intent2 = new Intent(context, VW_add2.class);
			intent2.putExtras(bundle);
			bulid.setIntent(intent2);

			Intent intent4 = new Intent(context, VW_add4.class);
			intent4.putExtras(bundle);
			drop.setIntent(intent4);

			Intent intent = new Intent(context, VW_add1.class);
			intent.putExtras(bundle);
			srctx.setIntent(intent);

			Intent intent3 = new Intent(context, VW_add3.class);
			intent3.putExtras(bundle);
			member.setIntent(intent3);
		} else {

			Intent intent = new Intent(context, br_add1.class);
			intent.putExtras(bundle);
			srctx.setIntent(intent);
			
			Intent intent3 = new Intent(context, br_add2.class);
			intent3.putExtras(bundle);
			member.setIntent(intent3);
		}

	}

	private String memberString() {
		Integer[] m = Data
				.get_Situation_Member_idx(Data.Situation_name[Data.index_situation]);
		String ans = "";
		for (Integer member : m)
			ans += Data.Rx_mac[member] + " ";
		return ans;
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

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}

}
