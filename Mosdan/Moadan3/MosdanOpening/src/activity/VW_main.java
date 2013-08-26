package activity;

import java.util.Arrays;
import ui.ListviewAdapter;
import ui.Mycommand;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class VW_main extends Activity {
	public static Spinner VW_Spinner;
	private Button VW_setSituation;
	public static ListviewAdapter VW_save_Adapter;
	public static ArrayAdapter<String> VW_name_Adapter;
	public static Context context;
	public static Activity VW_mainActivity;
	private static boolean isLoading = false;
	public static String[] vw_name;
	private boolean[] isDeleteCheck;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.vw_main);

		context = this;
		VW_mainActivity = this;
		// 處理頂FlowView

		VW_Spinner = (Spinner) findViewById(R.id.VW_spinner);
		VW_setSituation = (Button) findViewById(R.id.VW_btn_setSituation);

		vw_name = Data.getVW_BR_name(0);
		VW_name_Adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, vw_name);
		VW_name_Adapter
				.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		VW_Spinner.setAdapter(VW_name_Adapter);

		VW_setSituation.setOnClickListener(onClickListener);
		VW_Spinner.setOnItemSelectedListener(onItemSelectedListener);

		// top
		Drawable d = getResources().getDrawable(R.drawable.barrepeat);
		getActionBar().setBackgroundDrawable(d);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);

		// 完全沒有電視牆情境,進入歡迎畫面
		if (vw_name.length <= 0) {
			Intent intent = new Intent();
			intent.setClass(VW_main.this, welcome_vw.class);
			startActivity(intent);
		}
	}

	// Spinner 選擇後直接套用情境
	private OnItemSelectedListener onItemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			apply_vw_sit(0,"");
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	};

	// 選擇情境捨設定，前往30情境設定畫面
	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			Bundle bundle = new Bundle();
			bundle.putString(
					"name",
					(VW_Spinner.getSelectedItem().toString() != null) ? VW_Spinner
							.getSelectedItem().toString() : "auto-save");
			Intent intent = new Intent();
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	/**
	 * @mode=0 套用一般情境
	 * @mode=-1 套用預設情境
	 * */
	private void apply_vw_sit(final int mode, final String ss) {

		if (VW_Spinner.getSelectedItem() != null) {
			isLoading = true;
			final String vw_string = (mode == 0) ? "vw&&&"
					+ VW_Spinner.getSelectedItem().toString() : "defaultmosdan";
			// 啟動等待對話框
			final ProgressDialog PDialog = ProgressDialog.show(context, "處理中",
					"正在連線到伺服器...", true);
			// 套用的 一連串更新動作
			new Thread() {
				@Override
				public void run() {
					// step1 載入電視牆情境
					Mycommand a = new Mycommand() {
						@Override
						public void command() {
							VW_mainActivity.runOnUiThread(new Runnable() {
								public void run() {
									PDialog.setMessage("正在載入電視牆情境...");
								}
							});
							Turbo_View tv = new Turbo_View();
							tv.VW_load(vw_string);
						}
					};
					a.start();
					try {
						a.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 重新搜尋並刷新 T R S 3端的資料,更新UI資料
					if (mode == -1) {save_VW(ss);
					reload_tx_rx_sit_adapter(PDialog);}
					else
					{
						isLoading =false;
						PDialog.dismiss();
					}
				}
			}.start();
		}
	}

	// 搜尋並刷新 T R S 3端的資料,並刷新UI介面
	private void reload_tx_rx_sit_adapter(final ProgressDialog PDialog) {

		// step2 載入TX端
		Mycommand b = new Mycommand() {
			@Override
			public void command() {
				VW_mainActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("重新載入傳送端...");
					}
				});
				Turbo_View tv = new Turbo_View();
				tv.Do_searchTx();
			}
		};
		b.start();
		try {
			b.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		// step3 載入RX端
		Mycommand c = new Mycommand() {
			@Override
			public void command() {
				VW_mainActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("重新載入接收端...");
					}
				});
				Turbo_View tv = new Turbo_View();
				tv.Do_searchRx();
			}
		};

		
		c.start();

		// 必須先完成下命令,才能執行載入資料庫資料
		try {
			c.join();

		} catch (InterruptedException e) {
			Log.i("載入情境命令時發生錯誤...", e.getMessage());
		}

		Thread sqlThread = new Thread() {
			@Override
			public void run() {
				VW_mainActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("正在重新載入傳送端...");
					}
				});
				Data.getTxData();
			}

		};
		sqlThread.start();
		try {
			sqlThread.join();

		} catch (InterruptedException e) {
			go_to_noconn();
		}

		sqlThread = new Thread() {
			@Override
			public void run() {
				VW_mainActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("正在重新載入接收端資料...");
					}
				});
				Data.getRxData();
			}

		};
		sqlThread.start();
		try {
			sqlThread.join();
		} catch (InterruptedException e) {
			go_to_noconn();
		}
		sqlThread = new Thread() {
			@Override
			public void run() {
				Data.getSituationData();
			}

		};
		sqlThread.start();
		try {
			sqlThread.join();
		} catch (InterruptedException e) {

			go_to_noconn();
		}

	

		VW_mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// 重新更新3個ListView
				// 30情境
				VW_mainActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("正在完成...");
					}
				});
				// Situation
				if (VW_situation.Group_adapter != null)
					VW_situation.Group_adapter.ref(Data.Situation_name,
							Data.Situation_status);
				// Tx
				if (View_container.Txadapter != null)
					View_container.Txadapter.ref(Data.Tx_name, Data.Tx_status);
				// Rx
				if (View_container.Rxadapter != null)
					View_container.Rxadapter.ref(Data.Rx_name, Data.Rx_status);
				
				
				refersh_spinner(null);
				
				// 結束等待對話框
				PDialog.dismiss();
				isLoading = false;
			}
		});
	}

	// 選單選項
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.progress_scanner, menu);
		menu.removeItem(R.id.sync);
		menu.removeItem(R.id.set);
		menu.removeItem(R.id.save);
		menu.removeItem(R.id.status_main);
		menu.removeItem(R.id.status_arrow);
		menu.removeItem(R.id.gconn_refresh);
		menu.add(0, 0, 0, "建立全新情境");
		menu.add(0, 1, 1, "刪除情境");
		return true;
	}

	// 選單選項功能
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {

		// 重新更新Spinner資料
		case R.id.VW_refresh:

			if (VW_Spinner.getSelectedItem() != null)
				refersh_spinner(null);
			break;

		// 判斷是否要新增電視牆情境，否則選定一的情境設定進入-->"新增一個電視牆設定"
		case R.id.add:
			go_to_new_situation();
			break;
		// 刪除以儲存的電視牆情境
		case 1:
			showDialog_VWname();
			break;
		// 新增全新電視牆情境
		case 0:
			new_30situation();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void go_to_new_situation() {
		// 抓取要儲存在哪個情境設定索引
		int situation_index = checkSituaton();
		// 還有情境,進入新增情境
		if (situation_index >= 0) {
			// 傳送一個準備儲存的情境位置過去
			Bundle bundle = new Bundle();
			bundle.putInt("situation", situation_index);
			Intent intent = new Intent();
			intent.setClass(VW_main.this, VW_add1.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		// 30個用完,告訴使用者建議事項
		else
			show_Dialog_proposal();
	}

	// 建立全新的30個電視牆情境
	private void new_30situation() {

		// 命名 新電視牆情境 名稱對話框
		showDialog_nameVW();
	}

	// 命名 新電視牆情境 名稱對話框
	private void showDialog_nameVW() {
		final AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("新電視牆情境名稱");
		final EditText editText = new EditText(this);
		editText.setHint("命名電視牆情境名稱");
		// 只能輸入英文
		editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		// 一進來便取得焦點
		editText.requestFocus();
		d.setView(editText);
		d.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(context, editText.getText().toString(),
						Toast.LENGTH_LONG).show();
				if (editText.getText().toString().equals("")) {
					showDialog_nameVW();
				} else {
					// 套用預設空白情境
					apply_vw_sit(-1,editText.getText().toString());
					// 另存電視牆情境,並重新刷新(已儲存的電視牆情境)表單
					dialog.dismiss();
				}
			}
		});
		d.show();
	}

	// 檢查情境的使用狀況,回傳一個尚未使用(srctx=None,組成1*1,multihost=0)的情境索引
	private int checkSituaton() {

		int not_use_situation = -1;
		for (int i = 0; i < Data.Situation_name.length; i++) {
			if (Data.Situation_srctx[i].equals("None")
					&& Data.Situation_bulidx[i] == 1
					&& Data.Situation_bulidy[i] == 1
					&& Data.Situation_multihost[i] == 0) {
				not_use_situation = i;
				break;
			}
		}
		Log.i("not_use_situation", not_use_situation + "");

		return not_use_situation;
	}

	// 選擇要刪除的電視牆設定名稱列表對話框
	private void showDialog_VWname() {

		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("刪除電視牆設定");
		isDeleteCheck = new boolean[vw_name.length];
		Arrays.fill(isDeleteCheck, false);
		d.setMultiChoiceItems(vw_name, isDeleteCheck,
				new OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						isDeleteCheck[which] = isChecked;
					}
				});
		// 刪除電視牆設定
		d.setPositiveButton("確定", clickOK);
		// 離開刪除電視牆設定對話框
		d.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		d.show();
	}

	// 按下OK (刪除電視牆設定對話框)
	private OnClickListener clickOK = new OnClickListener() {
		// 啟動等待對話框
		@Override
		public void onClick(DialogInterface dialog, int which) {

			isLoading = true;
			final ProgressDialog PDialog = ProgressDialog.show(context, "處理中",
					"正在連線到伺服器...", true);
			new Thread() {
				@Override
				public void run() {

					// 刪除所選的情境
					for (int i = 0; i < isDeleteCheck.length; i++) {
						// 刪除所選的電視牆設定
						if (isDeleteCheck[i]) {
							final String name = vw_name[i];
							Mycommand a = new Mycommand() {
								@Override
								public void command() {
									VW_mainActivity
											.runOnUiThread(new Runnable() {
												public void run() {
													PDialog.setMessage("正在刪除電視牆設定: "
															+ name + " ...");
												}
											});
									Turbo_View tv = new Turbo_View();
									tv.VW_delete("vw&&&" + name);
								}
							};
							a.start();
							try {
								a.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						// 刪除完畢後,重新刷新 TRS 3端與UI顯示
						if (i == isDeleteCheck.length - 1) {
							
							reload_tx_rx_sit_adapter(PDialog);
							refersh_spinner(VW_Spinner.getSelectedItem()
									.toString() != null ? VW_Spinner
									.getSelectedItem().toString() : "");
							isLoading =false;
							PDialog.dismiss();
						}
					}

				}

			}.start();

		}
	};

	// 30個情境用完,出現建議事項的對話框
	private void show_Dialog_proposal() {
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("情境設定已滿");
		d.setMessage("很抱歉，您目前所套用的電視牆情境中，30個設定已全部被使用。建議您使用「Menu」→「建立全新情境」來規劃您的新設定。或是點選「情境設定」修改已使用過的設定。");
		// 離開刪除電視牆設定對話框
		d.setNegativeButton("知道了", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		d.show();
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

				VW_mainActivity.runOnUiThread(new Runnable() {
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

		VW_mainActivity.runOnUiThread(new Runnable() {
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

				vw_name = Data.getVW_BR_name(0);
				VW_name_Adapter = new ArrayAdapter<String>(context,
						android.R.layout.simple_spinner_item, vw_name);
				VW_name_Adapter
						.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
	
				VW_Spinner.setAdapter(VW_name_Adapter);
				
				if (default_select != null)
					for (int i = 0; i < vw_name.length; i++)
						if (vw_name[i].equals(default_select)) {
							VW_Spinner.setSelection(i);
							break;
						}
			}
		});
	}

	// 返回鍵鎖定
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isLoading)
				return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void go_to_noconn() {

		VW_main.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(VW_main.this, "沒有網路連線", Toast.LENGTH_LONG)
						.show();

				Intent intent = new Intent();
				intent.setClass(VW_main.this, Noconn.class);
				startActivity(intent);
				finish();

			}
		});

	}

}
