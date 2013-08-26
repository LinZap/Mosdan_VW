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
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Broadcast extends Activity {

	private boolean isLoading = false;
	public static Context context;
	public static Activity broadcastActivity;
	public static ListviewAdapter BRAdapter;
	private static ListView broadcastListView;
	private String[] br_name;
	private static int nowBr = -1;
	private Integer[] memberIdx;
	private boolean[] isDeleteCheck;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.broadcast_layout);
		context = this;
		broadcastActivity = this;

		br_name = Data.getVW_BR_name(1);

		broadcastListView = (ListView) findViewById(R.id.broadcastListView);
		BRAdapter = new ListviewAdapter(this, br_name, Data.VW_pic);
		broadcastListView.setAdapter(BRAdapter);
		broadcastListView.setOnItemClickListener(OnItemClickListener);

		// 沒有抓到資料表是第一次進入
		if (br_name.length <= 0) {
			// 前往新增一個廣播的歡迎畫面
			Intent intent = new Intent();
			intent.setClass(Broadcast.this, welcome_broadcast.class);
			startActivity(intent);

		}

	}

	// 套用群組廣播設定
	private void apply_br_sit(final String bro_name) {

		isLoading = true;
		final ProgressDialog PDialog = ProgressDialog.show(context, "處理中",
				"正在連線到伺服器...", true);

		new Thread() {

			@Override
			public void run() {

				// 載入指定電視牆情境(群組廣播情境)
				Mycommand a = new Mycommand() {
					@Override
					public void command() {
						Broadcast.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("正在載入群組廣播情境...");
							}
						});

						Turbo_View tv = new Turbo_View();
						tv.VW_load("br&&&" + bro_name);
					}

				};
				a.start();
				try {
					a.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				memberIdx = Data.get_Situation_Member_idx("br&&&" + bro_name);

				for (int i = 0; i < memberIdx.length; i++) {

					final int idx = i;
					// 重新綁定成員所屬情境
					Mycommand r = new Mycommand() {
						@Override
						public void command() {
							Broadcast.this.runOnUiThread(new Runnable() {
								public void run() {
									PDialog.setMessage("正在重新綁定成員情境: "
											+ Data.Rx_mac[memberIdx[idx]]
											+ " ...");
								}
							});

							Turbo_View tv = new Turbo_View();
							tv.VW_Rx_set(Data.Rx_mac[memberIdx[idx]], "1");
						}

					};
					r.start();
					try {
						r.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

				}

				// step2 載入TX端
				Mycommand b = new Mycommand() {
					@Override
					public void command() {
						Broadcast.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("重新載入傳送端...");
							}
						});

						Turbo_View tv = new Turbo_View();
						tv.Do_searchTx();
					}

				};
				// step3 載入RX端
				Mycommand c = new Mycommand() {
					@Override
					public void command() {
						Broadcast.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("重新載入接收端...");
							}
						});

						Turbo_View tv = new Turbo_View();
						tv.Do_searchRx();
					}

				};

				// 啟動下命令

				b.start();
				c.start();

				// 必須先完成下命令,才能執行後續工作
				try {
					b.join();
					c.join();
				} catch (InterruptedException e) {
					Log.i("載入情境命令時發生錯誤...", e.getMessage());
				}

				Mycommand d = new Mycommand() {
					@Override
					public void command() {
						Broadcast.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("正在重新載入資料...");
							}
						});

						// 從資料庫中重新載入資料
						Data.getTxData();
						Data.getRxData();
						Data.getSituationData();

					}
				};

				d.start();
				try {
					d.join();
				} catch (InterruptedException e) {
					Log.i("載入資料庫時發生錯誤...", e.getMessage());
				}

				Broadcast.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// 重新更新3個ListView
						// 30情境
						Broadcast.this.runOnUiThread(new Runnable() {
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
							View_container.Txadapter.ref(Data.Tx_name,
									Data.Tx_status);
						// Rx
						if (View_container.Rxadapter != null)
							View_container.Rxadapter.ref(Data.Rx_name,
									Data.Rx_status);
						// 結束等待對話框
						PDialog.dismiss();
						isLoading = false;
						goto_perfer();

					}

				});

			}
		}.start();

	}

	// 前往指定設定 (永遠是情境1)
	private void goto_perfer() {

		Bundle bundle = new Bundle();
		// 永遠只有一個情境
		bundle.putInt("index", 0);
		bundle.putInt("mode", 1);
		Intent intent = new Intent();
		intent.setClass(Broadcast.this, Situation_set_view.class);
		intent.putExtras(bundle);
		startActivity(intent);

	}

	private OnItemClickListener OnItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View v, int i, long arg3) {

			if (nowBr != i) {
				apply_br_sit(br_name[i]);
				nowBr = i;
			} else
				goto_perfer();

		}

	};

	// 返回鍵鎖定
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (isLoading)
				return false;

		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.progress_scanner, menu);
		menu.removeItem(R.id.sync);
		menu.removeItem(R.id.set);
		menu.removeItem(R.id.status_main);
		menu.removeItem(R.id.status_arrow);
		menu.removeItem(R.id.save);
		menu.removeItem(R.id.gconn_refresh);
		menu.add(0, 0, 0, "刪除廣播設定");
		// menu.findItem(R.id.VW_listView);
		// menu.findItem(R.id.add);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
		case R.id.add:

			Bundle bundle = new Bundle();
			bundle.putInt("mode", 0);
			Intent intent = new Intent();
			intent.setClass(Broadcast.this, br_add1.class);
			intent.putExtras(bundle);
			startActivity(intent);

			break;

		case R.id.VW_refresh:

			refersh_ListView();
			break;

		case 0:
			showDialog_VWname();

			break;

		}

		return super.onMenuItemSelected(featureId, item);
	}

	// 選擇要刪除的電視牆設定名稱列表對話框
	private void showDialog_VWname() {

		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("刪除電視牆設定");
		isDeleteCheck = new boolean[br_name.length];
		Arrays.fill(isDeleteCheck, false);
		d.setMultiChoiceItems(br_name, isDeleteCheck,
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
							final String name = br_name[i];
							Mycommand a = new Mycommand() {
								@Override
								public void command() {
									broadcastActivity
											.runOnUiThread(new Runnable() {
												public void run() {
													PDialog.setMessage("正在刪除群組廣播: "
															+ name + " ...");
												}
											});
									Turbo_View tv = new Turbo_View();
									tv.VW_delete("br&&&" + name);
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
							refersh_ListView();
							PDialog.dismiss();
						}
					}

				}

			}.start();

		}
	};

	// 搜尋並刷新 T R S 3端的資料,並刷新UI介面
	private void reload_tx_rx_sit_adapter(final ProgressDialog PDialog) {

		// step2 載入TX端
		Mycommand b = new Mycommand() {
			@Override
			public void command() {
				broadcastActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("重新載入傳送端...");
					}
				});
				Turbo_View tv = new Turbo_View();
				tv.Do_searchTx();
			}
		};
		// step3 載入RX端
		Mycommand c = new Mycommand() {
			@Override
			public void command() {
				broadcastActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("重新載入接收端...");
					}
				});
				Turbo_View tv = new Turbo_View();
				tv.Do_searchRx();
			}
		};

		b.start();
		c.start();

		// 必須先完成下命令,才能執行載入資料庫資料
		try {
			b.join();
			c.join();
		} catch (InterruptedException e) {
			Log.i("載入情境命令時發生錯誤...", e.getMessage());
		}

		Thread sqlThread = new Thread() {
			@Override
			public void run() {
				broadcastActivity.runOnUiThread(new Runnable() {
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
				broadcastActivity.runOnUiThread(new Runnable() {
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
		sqlThread = new Thread() {
			@Override
			public void run() {
				broadcastActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("正在重新載入電視牆資料...");
					}
				});
				Data.getVWData();
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
				broadcastActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("正在重新載入一般連線資料...");
					}
				});
				Data.getGconnData();
			}
		};
		sqlThread.start();
		try {
			sqlThread.join();

		} catch (InterruptedException e) {
			go_to_noconn();
		}

		broadcastActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// 重新更新3個ListView
				// 30情境
				broadcastActivity.runOnUiThread(new Runnable() {
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
				// 結束等待對話框
				PDialog.dismiss();
				isLoading = false;
			}
		});
	}

	// 重新抓取資料庫資料,更新Spinner
	public static void refersh_ListView() {

		broadcastActivity.runOnUiThread(new Runnable() {
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
				Broadcast.BRAdapter.ref(Data.getVW_BR_name(1), Data.VW_pic);
			}
		});
	}

	private void go_to_noconn() {

		broadcastActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(broadcastActivity, "沒有網路連線", Toast.LENGTH_LONG)
						.show();

				Intent intent = new Intent();
				intent.setClass(broadcastActivity, Noconn.class);
				startActivity(intent);
				finish();

			}
		});

	}
}
