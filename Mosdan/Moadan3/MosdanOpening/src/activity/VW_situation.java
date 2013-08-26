package activity;

import ui.ListviewAdapter;
import ui.Mycommand;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class VW_situation extends Activity
{

	public static Context context;
	private static Activity vw_situationActivity;
	public static ListView Group_listView;
	public static ListviewAdapter Group_adapter;

	private static String[] rx_name;
	private static Integer[] rx_pic, idx;
	public static MenuItem arrow;
	private String name;
	public static MenuItem  status_main, VW_refresh;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		
		
		setContentView(R.layout.group);
		
		
		name = getIntent().getExtras().getString("name", "auto-save");
		vw_situationActivity = this;
		context = this;
		// Group list
		Group_listView = (ListView) findViewById(R.id.listView4);
		// sub listView


		Group_adapter = new ListviewAdapter(this, Data.Situation_name, Data.Situation_status);

		// top
		Drawable d = getResources().getDrawable(R.drawable.barrepeat);
		getActionBar().setBackgroundDrawable(d);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);

		// first time
		// parseData();


		Group_listView.setAdapter(Group_adapter);
		Group_listView.setOnItemClickListener(OnItemClickListener4);


	}

	// parse from status of rx and get their members,status
	public static void parseData(String situationName)
	{
		idx = Data.get_Situation_Member_idx(situationName);

		if (idx != null){
			rx_name = new String[idx.length];
			rx_pic = new Integer[idx.length];
			for (int j = 0; j < idx.length; j++)
			{
				rx_name[j] = Data.Rx_name[idx[j]];
				rx_pic[j] = Data.Rx_status[idx[j]];
			}
		}

	}

	private OnItemClickListener OnItemClickListener4 = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> arg0, View v, int i, long arg3)
		{

			Bundle bundle2 = new Bundle();
			bundle2.putInt("index", i);
			Intent intent2 = new Intent();
			intent2.setClass(VW_situation.this, Situation_set_view.class);
			intent2.putExtras(bundle2);
			startActivity(intent2);

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.progress_scanner, menu);
		
		menu.removeItem(R.id.set);
		menu.removeItem(R.id.sync);
		menu.removeItem(R.id.add);
		menu.removeItem(R.id.gconn_refresh);
		menu.removeItem(R.id.status_arrow);
		menu.removeItem(R.id.status_arrow);
		menu.removeItem(R.id.status_main);
	
		
	
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		
			
		// 重新刷新情境列表
		case R.id.VW_refresh:
			
			Thread sqlThread = new Thread(){
				@Override
				public void run(){
				//重新取得情境列表資料
				Data.getSituationData();				
			}};
			sqlThread.start();
			try {
				sqlThread.join();
			} catch (InterruptedException e) {
				Log.i("重新取得情境列表資料時發生錯誤", e.getMessage());
			}
			//刷新情境列表
			Group_adapter.ref(Data.Situation_name, Data.Situation_status);
			break;
			
			
			
		case R.id.save:
			
			save_VW(name);
		
			break;
			
			
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
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
							tv.VW_save("vw&&&" + s);
						}
					};
					a.start();
					try {
						a.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// 刷新Spinner
					refersh_spinner(s);

					VW_situation.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(VW_main.context, "儲存完成",
									Toast.LENGTH_LONG).show();
						}
					});

				}
			}.start();
		}

		// 重新抓取資料庫資料,更新Spinner
		public static void refersh_spinner(final String default_select) {

			vw_situationActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Thread sqlThread = new Thread() {
						@Override
						public void run() {
							// 重新搜尋 所有 已儲存的電視牆情境
							Data.getVWData();
						}
					};
					sqlThread.start();
					try {
						sqlThread.join();
					} catch (InterruptedException e) {
						Log.i("下達搜尋電視牆情境時發生錯誤", e.getMessage());
					}

					VW_main.VW_name_Adapter
							.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
					VW_main.vw_name = Data.getVW_BR_name(0);
					VW_main.VW_name_Adapter.notifyDataSetChanged();

					if (default_select != null)
						for (int i = 0; i < VW_main.vw_name.length; i++)
							if (VW_main.vw_name[i].equals(default_select)) {
								VW_main.VW_Spinner.setSelection(i);
								break;
							}
				}
			});
		}
}
