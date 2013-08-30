package activity;

import ui.Setting_Perference;
import TV.Mosdan2.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

public class Setting extends Activity {
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.empty);

		
		FragmentManager fm = this.getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Setting_Perference setting_p = new Setting_Perference();
		FragmentTransaction ft2 = ft.replace(android.R.id.content, setting_p);
		ft2.commit();
		
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(true);
		getActionBar().setTitle("³]©w");
		View cView = getLayoutInflater().inflate(R.layout.ation_bar, null);
		getActionBar().setCustomView(cView);
	}

}
