package activity;

import ui.Mycommand;
import ui.Situation_Perference;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Situation_set_view extends Activity {
	private static Context context;
	private Situation_Perference sit_p;
	private int mode = 0;
	private String save_br_name ="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.empty);
		context = this;

		// 設定點選位置
		Data.index_situation = getIntent().getExtras().getInt("index");
		mode = getIntent().getExtras().getInt("mode");
		
		if(mode==1)
			save_br_name = getIntent().getExtras().getString("brname");
		
		sit_p = new Situation_Perference(context, mode);
		FragmentManager fm = this.getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		FragmentTransaction ft2 = ft.replace(android.R.id.content, sit_p);
		ft2.commit();

		// top
		Drawable d = getResources().getDrawable(R.drawable.barrepeat);
		getActionBar().setBackgroundDrawable(d);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	protected void onResume() {

		if (mode != 1) {
			sit_p.name.setSummary(Data.Situation_name[Data.index_situation]);
			sit_p.srctx.setSummary(Data.Situation_srctx[Data.index_situation]);
			sit_p.bulid.setSummary(Data.Situation_bulidx[Data.index_situation]
					+ " X " + Data.Situation_bulidy[Data.index_situation]);
			sit_p.member.setSummary(memberString());
		} else {
			sit_p.srctx.setSummary(Data.Situation_srctx[Data.index_situation]);
			sit_p.member.setSummary(memberString());
		}
		super.onResume();
	}

	private String memberString() {
		
		int idx = mode==1?0:Data.index_situation;
		
		Integer[] m = Data
				.get_Situation_Member_idx(Data.Situation_name[idx]);
		String ans = "";
		for (Integer member : m)
			ans += Data.Rx_mac[member] + " ";
		return ans;
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.progress_scanner, menu);
		menu.removeItem(R.id.sync);
		menu.removeItem(R.id.set);
		menu.removeItem(R.id.status_main);
		menu.removeItem(R.id.status_arrow);
		menu.removeItem(R.id.add);
		menu.removeItem(R.id.gconn_refresh);
		if(mode!=1)menu.removeItem(R.id.save);
		menu.removeItem(R.id.VW_refresh);
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {

		case R.id.save:		
			save_VW(save_br_name);
		}
		return super.onMenuItemSelected(featureId, item);
	}
	// (另)儲存電視牆情境(s為另存名稱)
	private void save_VW(final String s) {
		// 先下儲存命令 > 重新抓取資料庫資料並解析 > 刷新表單
		new Thread() {
			@Override
			public void run() {
				Mycommand a = new Mycommand() {
					@Override
					public void command() {
						Turbo_View tv = new Turbo_View();
						tv.VW_save("br&&&" + s);
					}
				};
				a.start();
				try {
					a.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Situation_set_view.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(VW_main.context, "儲存完成",
								Toast.LENGTH_LONG).show();
					}
				});

			}
		}.start();
	}

}
