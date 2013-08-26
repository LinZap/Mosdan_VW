package activity;

import java.util.Arrays;

import ui.Mycommand;

import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class br_add2 extends Activity {

	private ListView br_tx_ListvListView;
	private Button next, cancel;
	private ArrayAdapter<String> adapter;
	private static boolean[] isRx;
	private boolean isLoading = false;
	private CheckBox select_all;
	private TextView titTextView;
	private int mode=0;
	private Integer[] memberIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.br_add2);

		mode = getIntent().getExtras().getInt("mode", 0);
		titTextView = (TextView) findViewById(R.id.tit);
		br_tx_ListvListView = (ListView) findViewById(R.id.member_listView);
		select_all = (CheckBox) findViewById(R.id.checkBox1);
		select_all.setOnCheckedChangeListener(change);
		// if(Data.Rx_neme)

		isRx = new boolean[Data.Rx_name.length];
		memberIndex = Data.get_Situation_Member_idx(Data.Situation_name[0]);


		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, Data.Rx_name);
		br_tx_ListvListView.setAdapter(adapter);
		br_tx_ListvListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		br_tx_ListvListView.setOnItemClickListener(itemclick);
		
		
		Arrays.fill(isRx, false);
		// 預設打勾成員(與判斷陣列)
		
		if(mode==1)
		if (memberIndex != null)
			for (int i = 0; i < memberIndex.length; i++) {
				br_tx_ListvListView.setItemChecked(memberIndex[i], true);
				isRx[memberIndex[i]] = true;
			}
		
	

		next = (Button) findViewById(R.id.member_next);
		cancel = (Button) findViewById(R.id.member_prev);
		next.setOnClickListener(click);
		cancel.setOnClickListener(click);

		if (mode == 1) {
			next.setText("確定");
			cancel.setText("取消");
			titTextView.setText("編輯群組廣播");
		}
	}

	private OnCheckedChangeListener change = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			for (int i = 0; i < isRx.length; i++)
				br_tx_ListvListView.setItemChecked(i, isChecked);
			Arrays.fill(isRx, isChecked);
		}
	};

	private OnItemClickListener itemclick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {

			isRx[index] = !isRx[index];

		}
	};

	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {

			case R.id.member_prev:

				if (mode != 1) {
					
					
					Bundle bundle = new Bundle();
					bundle.putInt("mode", 0);
					Intent intent = new Intent();
					intent.setClass(br_add2.this, br_add1.class);
					intent.putExtras(bundle);
					startActivity(intent);
				}
				finish();
				break;

			case R.id.member_next:

				// 下一步
				if(mode!=1)
				alertSave();
				else {
					refresh_bor_List("成員設定");
				}
				break;

			}

		}
	};

	private void alertSave() {

		final AlertDialog.Builder d = new AlertDialog.Builder(br_add2.this);
		d.setTitle("群組廣播名稱");
		d.setMessage("為這個群組廣播設定一個名稱");
		final EditText editText = new EditText(br_add2.this);
		editText.setHint("Bro_001");

		d.setView(editText);
		d.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (editText.getText().toString().equals("")) {
					d.setMessage("請輸入名稱");
					alertSave();
				} else {
					// 另存群組廣播情境,並重新整理清單
					refresh_bor_List(editText.getText().toString());
				}
			}

		});
		d.show();

	}

	private void refresh_bor_List(final String s) {

		// 先下儲存命令 > 重新抓取資料庫資料並解析 > 刷新表單

		isLoading = true;
		final ProgressDialog PDialog = ProgressDialog.show(br_add2.this, "處理中",
				"正在連線到伺服器...", true);

		new Thread() {

			@Override
			public void run() {

				
				
				
				
				
				// 設定Rx所屬情境

				for (int i = 0; i < isRx.length; i++) {

					if (isRx[i]) {
						final String mac = Data.Rx_mac[i];
						Mycommand a = new Mycommand() {
							@Override
							public void command() {

								br_add2.this.runOnUiThread(new Runnable() {
									public void run() {
										PDialog.setMessage("正在載入: " + mac
												+ "...");
									}
								});

								Turbo_View turbo_View = new Turbo_View();
								turbo_View.VW_Rx_set(mac, "1");
							}
						};

						a.start();
						try {
							a.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					} else {
						if (Data.Rx_sitsution[i].equals(Data.Situation_name[0])) {

							final String mac = Data.Rx_mac[i];

							Mycommand a = new Mycommand() {
								@Override
								public void command() {

									br_add2.this.runOnUiThread(new Runnable() {
										public void run() {
											PDialog.setMessage("正在設定成員: " + mac
													+ "...");
										}
									});
									Turbo_View turbo_View = new Turbo_View();
									turbo_View.VW_Rx_set(mac, "1");
								}
							};
							a.start();
							try {
								a.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							Data.Rx_sitsution[i] = "1";
						}
					}

				}

				if(mode!=1){

					// 儲存情境
					Mycommand a = new Mycommand() {
						@Override
						public void command() {

							br_add2.this.runOnUiThread(new Runnable() {
								public void run() {
									PDialog.setMessage("正在儲存情境: " + s + "...");
								}
							});
							Turbo_View tv = new Turbo_View();
							tv.VW_save("br&&&" + s);
						}
					};
					a.start();
					try {
						a.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					
					
					
					// 重新載入資料
					Thread b = new Thread() {
						@Override
						public void run() {
							br_add2.this.runOnUiThread(new Runnable() {
								public void run() {
									PDialog.setMessage("重新載入資料: " + s + "...");
								}
							});
							Data.getVWData();

						}
					};

				
					b.start();

					try {
						b.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				
				
				// 更新ListView顯示 , 傳到完成畫面
				br_add2.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						br_add2.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("正在完成: " + s + "...");
							}
						});

						Broadcast.BRAdapter.ref(Data.getVW_BR_name(1),
								Data.VW_pic);
						Toast.makeText(br_add2.this, "另存完成", Toast.LENGTH_LONG)
								.show();
						
						// 結束等待對話框

						PDialog.dismiss();
						isLoading = false;

						if (mode != 1) {
							Bundle bundle = new Bundle();
							bundle.putString("result", s);
							Intent intent = new Intent();
							intent.setClass(br_add2.this, br_add3.class);
							intent.putExtras(bundle);
							startActivity(intent);
						}

						finish();
					}
				});

			}

		}.start();

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