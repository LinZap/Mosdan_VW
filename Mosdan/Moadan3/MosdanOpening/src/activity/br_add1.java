package activity;

import ui.Mycommand;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class br_add1 extends Activity {
	private boolean isLoading = false;
	private ListView br_tx_ListvListView;
	private Button next, cancel;
	private ArrayAdapter<String> adapter;
	private TextView titTextView;
	private int Tx_index = -1, mode = 0;
	private int situation_idx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.br_add1);
		mode = getIntent().getExtras().getInt("mode", 0);
		situation_idx = getIntent().getExtras().getInt("situation", 0);

		br_tx_ListvListView = (ListView) findViewById(R.id.br_tx_listView);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, Data.Tx_name);
		br_tx_ListvListView.setAdapter(adapter);
		br_tx_ListvListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		br_tx_ListvListView.setOnItemClickListener(itemclick);
		next = (Button) findViewById(R.id.sor_next);
		titTextView = (TextView) findViewById(R.id.tit);
		cancel = (Button) findViewById(R.id.sor_cancel);
		next.setOnClickListener(click);
		cancel.setOnClickListener(click);
		if (mode == 1) {
			next.setText("確定");
			cancel.setText("取消");
			titTextView.setText("編輯群組廣播");
			int pos = getIndex(situation_idx);
			if(pos!=-1)
			br_tx_ListvListView.setItemChecked(pos, true);
		}
	}

	private int getIndex(int sit_idx) {

		for (int i = 0; i < Data.Tx_name.length; i++) {
			if (Data.Tx_mac[i].equals(Data.Situation_srctx[sit_idx])) 
				return i;
			

		}
		return -1;
	}

	private OnItemClickListener itemclick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {

			Tx_index = index;

		}
	};

	private OnClickListener click = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.sor_cancel:
				finish();
				break;

			case R.id.sor_next:
				doset_srctx();
				break;
			}
		}
	};

	private void doset_srctx() {

		isLoading = true;
		final ProgressDialog PDialog = ProgressDialog.show(br_add1.this, "處理中",
				"正在連線到伺服器...", true);

		new Thread() {
			@Override
			public void run() {

				if (Tx_index != -1) {

					if (mode != 1) {
						// 載入空白情境

						Mycommand d = new Mycommand() {
							@Override
							public void command() {

								br_add1.this.runOnUiThread(new Runnable() {
									public void run() {
										PDialog.setMessage("正在設定水平組成");
									}
								});

								Turbo_View turbo_View = new Turbo_View();
								turbo_View.VW_X("1", 1);
							}
						};

						d.start();
						try {
							d.join();
						} catch (InterruptedException e) {
							go_to_noconn();
						}

						Mycommand f = new Mycommand() {
							@Override
							public void command() {

								br_add1.this.runOnUiThread(new Runnable() {
									public void run() {
										PDialog.setMessage("正在設垂直組成");
									}
								});
								Turbo_View turbo_View = new Turbo_View();
								turbo_View.VW_Y("1", 1);
							}
						};

						f.start();
						try {
							f.join();
						} catch (InterruptedException e) {
							go_to_noconn();
						}

					}

					Mycommand a = new Mycommand() {
						@Override
						public void command() {

							br_add1.this.runOnUiThread(new Runnable() {
								public void run() {
									PDialog.setMessage("正在設定傳送端來源:"
											+ Data.Tx_mac[Tx_index]);
								}
							});

							Turbo_View t = new Turbo_View();
							t.VW_connTx("1", Data.Tx_mac[Tx_index]);
							// 指令+INTENT
							Data.Situation_srctx[Data.index_situation] = Data.Tx_mac[Tx_index];

							// 結束等待對話框
							PDialog.dismiss();
							isLoading = false;

							if (mode != 1) {

								Bundle bundle = new Bundle();
								bundle.putInt("mode", 0);
								Intent intent = new Intent();
								intent.setClass(br_add1.this, br_add2.class);
								intent.putExtras(bundle);
								startActivity(intent);
							}

							finish();

						}
					};
					a.start();
					try {
						a.join();
					} catch (InterruptedException e) {
						go_to_noconn();
						
					}

				} else {

					Toast.makeText(br_add1.this, "請先選擇傳送端", Toast.LENGTH_SHORT)
							.show();
				}

			}

		}.start();

	}
	
	private void go_to_noconn() {

		br_add1.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(br_add1.this, "沒有網路連線", Toast.LENGTH_LONG)
						.show();

				Intent intent = new Intent();
				intent.setClass(br_add1.this, Noconn.class);
				startActivity(intent);
				finish();

			}
		});

	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (isLoading)
				return false;

		}
		return super.onKeyDown(keyCode, event);
	}
}