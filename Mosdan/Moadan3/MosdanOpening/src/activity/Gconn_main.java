package activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import ui.Gconn_Adapter;
import ui.Gconn_GroupAdapter;
import ui.MyFlow_View_Adapter;
import ui.Mycommand;
import ui.TR_selector;
import ui.TitleFlowIndicator;
import ui.ViewFlow;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Gconn_main extends Activity {

	private static TextView Gconn_tx_name, Gconn_tx_subname, Gconn_tx_mode,
			Gconn_tx_atimode, Gconn_tx_hint, Gconn_tx_ip, Gconn_tx_mac,
			Gconn_rx_title, Gconn_rx_name, Gconn_rx_subname, Gconn_rx_group,
			Gconn_rx_situation, Gconn_rx_hint, Gconn_rx_ip, Gconn_rx_mac,
			Gconn_group_member;
	private static ImageView Gconn_tx_more, Gconn_rx_more;
	private static Button Gconn_btn_conn, Gconn_btn_save;
	private static LinearLayout Gconn_tx_inf, Gconn_rx_inf,
			Gconn_rx_member_inf;
	private static RelativeLayout Gconn_brk_rx, Gconn_brk_tx;
	private static String[] group_list;
	private int control = -1;
	public static ViewFlow viewFlow;
	public static Gconn_Adapter Gconn_Adapter;
	public static Gconn_GroupAdapter Gconn_GroupAdapter;
	private ListView Gconn_ListView, gconn_group_ListView;
	public static Context context;
	// private MenuItem add;
	public static Activity gconn_Activity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myflow_view_layout);
		context = this;
		gconn_Activity = this;
		// 設定最初顯示
		set_folwview();

		// top
		Drawable d = getResources().getDrawable(R.drawable.barrepeat);
		getActionBar().setBackgroundDrawable(d);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);

		findview();
		basicset();
		setAdapter();
		Gconn_tx_inf.setVisibility(View.GONE);
		Gconn_rx_inf.setVisibility(View.GONE);
		Gconn_rx_member_inf.setVisibility(View.GONE);
		group_list = Data.get_Rx_Group_name();
		setListener();

	}

	// 設定頂端橫向SlideBar
	private void set_folwview() {
		viewFlow = (ViewFlow) findViewById(R.id.viewflow);
		MyFlow_View_Adapter adapter = new MyFlow_View_Adapter(this);
		viewFlow.setAdapter(adapter);
		TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(R.id.viewflowindic);
		indicator.setMode(0);
		viewFlow.setFlowIndicator(indicator);
		// 起始畫面 = 一般連線
		viewFlow.setSelection(1);
		viewFlow.setSelection(1);
	}

	private void setListener() {
		Gconn_btn_conn.setOnClickListener(Gconn_listener);
		Gconn_btn_save.setOnClickListener(Gconn_listener);
		Gconn_tx_more.setOnClickListener(Gconn_listener);
		Gconn_rx_more.setOnClickListener(Gconn_listener);
		Gconn_brk_rx.setOnClickListener(Gconn_listener);
		Gconn_brk_tx.setOnClickListener(Gconn_listener);
		Gconn_ListView.setOnItemClickListener(Gconn_itemListener);
		Gconn_ListView.setOnItemLongClickListener(Gconn_LongitemListener);
		gconn_group_ListView.setOnItemClickListener(Gconn_groupListener);
		// gconn_group_ListView.setOnItemLongClickListener(Gconn_LonggroupListener);

	}

	public static void basicset() {
		// 設定Rx磚,基本顯示

		// 為Rx單一時
		if (Data.Gconn_rx_index != -1) {
			Gconn_rx_name.setText(Data.Rx_name[Data.Gconn_rx_index]);
			Gconn_rx_title.setText("接收端");
			Gconn_rx_subname.setText(Data.Rx_name[Data.Gconn_rx_index]);
			Gconn_rx_group.setText(Data.Rx_group[Data.Gconn_rx_index]);
			Gconn_rx_situation.setText(Data.Rx_sitsution[Data.Gconn_rx_index]);
			Gconn_rx_hint.setText(Data.Rx_explain[Data.Gconn_rx_index]);
			Gconn_rx_ip.setText(Data.Rx_ip[Data.Gconn_rx_index]);
			Gconn_rx_mac.setText(Data.Rx_mac[Data.Gconn_rx_index]);
		} else {
			Gconn_rx_name.setText("請選擇");
			Gconn_rx_title.setText("接收端");
			Gconn_rx_subname.setText("無");
			Gconn_rx_group.setText("無");
			Gconn_rx_situation.setText("無");
			Gconn_rx_hint.setText("無");
			Gconn_rx_ip.setText("無");
			Gconn_rx_mac.setText("無");
		}

		if (Gconn_rx_member_inf.getVisibility() == View.VISIBLE) {
			Gconn_rx_member_inf.setVisibility(View.GONE);
			Gconn_rx_more.setImageResource(R.drawable.more_rx);
		}

		// 群組
		if (Data.Gconn_group_index != -1) {
			group_list = Data.get_Rx_Group_name();
			Gconn_rx_name.setText(group_list[Data.Gconn_group_index]);
			Gconn_rx_title.setText("群組");
			String result = getMemberString(Data.Gconn_group_index);
			if (result == null)
				Gconn_group_member.setText("無");
			else
				Gconn_group_member.setText(result);

			if (Gconn_rx_inf.getVisibility() == View.VISIBLE) {
				Gconn_rx_inf.setVisibility(View.GONE);
				Gconn_rx_more.setImageResource(R.drawable.more_rx);
			}

		}

		// 設定Tx磚,基本顯示
		if (Data.Gconn_tx_index != -1) {
			Gconn_tx_name.setText(Data.Tx_name[Data.Gconn_tx_index]);
			Gconn_tx_subname.setText(Data.Tx_name[Data.Gconn_tx_index]);
			Gconn_tx_mode.setText(Data.Tx_mode[Data.Gconn_tx_index]);
			Gconn_tx_atimode.setText(Data.Tx_ati_mode[Data.Gconn_tx_index]);
			Gconn_tx_hint.setText(Data.Tx_explain[Data.Gconn_tx_index]);
			Gconn_tx_ip.setText(Data.Tx_ip[Data.Gconn_tx_index]);
			Gconn_tx_mac.setText(Data.Tx_mac[Data.Gconn_tx_index]);
		} else {
			Gconn_tx_name.setText("請選擇");
			Gconn_tx_subname.setText("無");
			Gconn_tx_mode.setText("無");
			Gconn_tx_atimode.setText("無");
			Gconn_tx_hint.setText("無");
			Gconn_tx_ip.setText("無");
			Gconn_tx_mac.setText("無");

		}

		// 下方按鈕的使用權限

		if (Data.Gconn_tx_index != -1 && Data.Gconn_rx_index != -1) {

			Gconn_btn_conn.setEnabled(true);
			Gconn_btn_save.setEnabled(true);

		} else if (Data.Gconn_group_index != -1 && Data.Gconn_tx_index != -1) {
			Gconn_btn_conn.setEnabled(true);
			Gconn_btn_save.setEnabled(false);

		} else {
			Gconn_btn_conn.setEnabled(false);
			Gconn_btn_save.setEnabled(false);
		}

	}

	// 解析群組名稱的成員,並產生字串
	private static String getMemberString(int idx) {
		Integer[] member_index = Data.get_Rx_Group_member(group_list[idx]);
		if (member_index != null) {
			String result = "";
			result += Data.Rx_name[member_index[0]];
			for (int i = 1; i < member_index.length; i++)
				result += " , " + Data.Rx_name[member_index[i]];
			return result;
		}
		return null;
	}

	private void findview() {
		Gconn_tx_name = (TextView) findViewById(R.id.gconn_tx_name);
		Gconn_tx_subname = (TextView) findViewById(R.id.gconn_tx_subname);
		Gconn_tx_mode = (TextView) findViewById(R.id.gconn_tx_mode);
		Gconn_tx_atimode = (TextView) findViewById(R.id.gconn_tx_atimode);
		Gconn_tx_hint = (TextView) findViewById(R.id.gconn_tx_hint);
		Gconn_tx_ip = (TextView) findViewById(R.id.gconn_tx_ip);
		Gconn_tx_mac = (TextView) findViewById(R.id.gconn_tx_mac);
		Gconn_rx_title = (TextView) findViewById(R.id.gconn_rx_title);
		Gconn_rx_name = (TextView) findViewById(R.id.gconn_rx_name);
		Gconn_rx_subname = (TextView) findViewById(R.id.gconn_rx_subname);
		Gconn_rx_group = (TextView) findViewById(R.id.gconn_rx_group);
		Gconn_rx_situation = (TextView) findViewById(R.id.gconn_rx_situation);
		Gconn_rx_hint = (TextView) findViewById(R.id.gconn_rx_hint);
		Gconn_rx_ip = (TextView) findViewById(R.id.gconn_rx_ip);
		Gconn_rx_mac = (TextView) findViewById(R.id.gconn_rx_mac);
		Gconn_group_member = (TextView) findViewById(R.id.gconn_group_member);
		Gconn_tx_more = (ImageView) findViewById(R.id.gconn_tx_more);
		Gconn_rx_more = (ImageView) findViewById(R.id.gconn_rx_more);
		Gconn_btn_conn = (Button) findViewById(R.id.gconn_btn_conn);
		Gconn_btn_save = (Button) findViewById(R.id.gconn_btn_save);
		Gconn_tx_inf = (LinearLayout) findViewById(R.id.gconn_tx_inf);
		Gconn_rx_inf = (LinearLayout) findViewById(R.id.gconn_rx_inf);
		Gconn_rx_member_inf = (LinearLayout) findViewById(R.id.gconn_rx_member_inf);
		Gconn_brk_tx = (RelativeLayout) findViewById(R.id.Gconn_brk_tx);
		Gconn_brk_rx = (RelativeLayout) findViewById(R.id.Gconn_brk_rx);
		Gconn_ListView = (ListView) findViewById(R.id.gconn_listView);
		gconn_group_ListView = (ListView) findViewById(R.id.gconn_group_listView);
	}

	private OnClickListener Gconn_listener = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {

			// 選擇觀看更多Tx詳細資訊
			case R.id.gconn_tx_more:
				if (Gconn_tx_inf.getVisibility() == View.GONE) {
					Gconn_tx_inf.setVisibility(View.VISIBLE);
					Gconn_tx_more.setImageResource(R.drawable.more_tx_r);
				} else {
					Gconn_tx_inf.setVisibility(View.GONE);
					Gconn_tx_more.setImageResource(R.drawable.more_tx);
				}
				break;
			// 選擇觀看更多Rx詳細資訊
			case R.id.gconn_rx_more:
				if (Data.Gconn_rx_index != -1)
					if (Gconn_rx_inf.getVisibility() == View.GONE) {
						Gconn_rx_inf.setVisibility(View.VISIBLE);
						Gconn_rx_more.setImageResource(R.drawable.more_rx_r);
					} else {
						Gconn_rx_inf.setVisibility(View.GONE);
						Gconn_rx_more.setImageResource(R.drawable.more_rx);
					}

				if (Data.Gconn_group_index != -1)
					if (Gconn_rx_member_inf.getVisibility() == View.GONE) {
						Gconn_rx_member_inf.setVisibility(View.VISIBLE);
						Gconn_rx_more.setImageResource(R.drawable.more_rx_r);
					} else {
						Gconn_rx_member_inf.setVisibility(View.GONE);
						Gconn_rx_more.setImageResource(R.drawable.more_rx);
					}

				break;

			// 選擇Tx來源端
			case R.id.Gconn_brk_tx:
				TR_selector tx_selector = new TR_selector(Gconn_main.this);
				control = 0;
				tx_selector.setDetail("選擇傳送端", control, null);
				tx_selector.show();
				break;

			case R.id.Gconn_brk_rx:
				TR_selector rx_selector = new TR_selector(Gconn_main.this);
				control = 1;
				rx_selector.setDetail("接收端類型", control, null);
				rx_selector.show();
				break;
			// 連線
			case R.id.gconn_btn_conn:

				// TR 直接連線
				Mycommand sr = new Mycommand() {

					@Override
					public void command() {
						Turbo_View tv = new Turbo_View();

						if (Data.Gconn_rx_index != -1)
							tv.Rx_connTx(Data.Rx_mac[Data.Gconn_rx_index],
									Data.Tx_mac[Data.Gconn_tx_index]);
						else
							tv.Group_connTx(
									Data.get_Rx_Group_name()[Data.Gconn_group_index],
									Data.Tx_mac[Data.Gconn_tx_index]);

					}
				};
				sr.start();
				Toast.makeText(context, "已連線接收端與傳送端", Toast.LENGTH_SHORT)
						.show();

				break;
			case R.id.gconn_btn_save:

				// TR 儲存成情境
				save_as_TRsituation();

				break;
			}
		}

	};

	private void save_as_TRsituation() {

		Mycommand srt = new Mycommand() {
			@Override
			public void command() {

				Turbo_View tv = new Turbo_View();
				// 使用目前時間做為鍵值(情境名稱)
				SimpleDateFormat sdFormat = new SimpleDateFormat(
						"yyyyMMddhhmmss");
				Date date = new Date();

				final String iname = sdFormat.format(date);
				final String itx = Data.Tx_mac[Data.Gconn_tx_index];
				final String irx = Data.Rx_mac[Data.Gconn_rx_index];

				// 下指令
				tv.RxSituation_set(iname, irx, itx);

				// 非同步處理資料
				gconn_Activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Data.Gconn_save(iname, itx, irx);
						Gconn_Adapter.ref();
					}
				});

			}
		};
		srt.start();

		Toast.makeText(context, "已儲存連線", Toast.LENGTH_SHORT).show();

	}

	private void setAdapter() {
		Gconn_Adapter = new Gconn_Adapter(this, Data.Gconn_tx, Data.Gconn_rx);
		Gconn_ListView.setAdapter(Gconn_Adapter);
		Gconn_GroupAdapter = new Gconn_GroupAdapter(this,
				Data.get_Rx_Group_name());
		gconn_group_ListView.setAdapter(Gconn_GroupAdapter);
	}

	private OnItemClickListener Gconn_itemListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int d, long arg3) {

			int tx_idx = -1;
			int rx_idx = -1;

			for (int i = 0; i < Data.Tx_mac.length; i++) {
				if (Data.Tx_mac[i].equals(Data.Gconn_tx[d])) {
					tx_idx = i;
					break;
				}
			}

			for (int i = 0; i < Data.Rx_mac.length; i++) {
				if (Data.Rx_mac[i].equals(Data.Gconn_rx[d])) {
					rx_idx = i;
					break;
				}
			}

			Data.Gconn_rx_index = rx_idx;
			Data.Gconn_tx_index = tx_idx;
			Data.Gconn_group_index = -1;
			basicset();

			viewFlow.setSelection(1);
			viewFlow.setSelection(1);
			viewFlow.setSelection(1);
			Toast.makeText(Gconn_main.this, "click", Toast.LENGTH_SHORT).show();
		}
	};

	private OnItemClickListener Gconn_groupListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			TR_selector tx_selector = new TR_selector(Gconn_main.this);
			tx_selector.setDetail("設置", 4, Data.get_Rx_Group_name()[arg2]);
			tx_selector.show();
		}

	};

	private OnItemLongClickListener Gconn_LongitemListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {

			Data.Gconn_LongClick_index = arg2;

			TR_selector tx_selector = new TR_selector(Gconn_main.this);
			tx_selector.setDetail("設置", 6, null);
			tx_selector.show();

			return true;
		}
	};

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {

		case R.id.add:

			final AlertDialog.Builder d = new AlertDialog.Builder(this);
			d.setTitle("新增接收端群組");
			final EditText editText = new EditText(this);
			editText.setHint("輸入群組名稱");
			d.setView(editText);
			d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {

					Toast.makeText(context, editText.getText().toString(),
							Toast.LENGTH_LONG).show();

					if (editText.getText().toString().equals("")) {
						d.setMessage("請輸入名稱");
					} else {
						TR_selector ts = new TR_selector(context);
						ts.setDetail("選擇群組成員", 5, editText.getText().toString());
						ts.show();
						dialog.dismiss();
					}

				}
			});
			d.show();

			break;

		// 重新整理 Gconn 所有的 ListView
		case R.id.gconn_refresh:

			gconn_Activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {

					Thread sqltThread = new Thread() {
						@Override
						public void run() {

							// 重新取得TR連線情境
							Data.getGconnData();
							// 重新取得Rx資料交給後續解析成RX群組列表
							Data.getRxData();
						}
					};
					sqltThread.start();
					try {
						sqltThread.join();
					} catch (InterruptedException e) {
						Log.i("重新取得數據發生錯誤", e.getMessage());
					}

					// 重新刷新表格
					Gconn_Adapter.ref();
					Gconn_GroupAdapter.ref();

				}
			});

			break;

		default:
			return super.onMenuItemSelected(featureId, item);
		}
		return super.onMenuItemSelected(featureId, item);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.progress_scanner, menu);
		// add = (MenuItem) findViewById(R.id.add);
		menu.removeItem(R.id.sync);
		menu.removeItem(R.id.save);
		menu.removeItem(R.id.set);
		menu.removeItem(R.id.status_main);
		menu.removeItem(R.id.status_arrow);
		menu.removeItem(R.id.VW_refresh);
		return true;
	}

}
