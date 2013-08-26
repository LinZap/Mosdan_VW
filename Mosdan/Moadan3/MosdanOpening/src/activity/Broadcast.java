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

		// �S������ƪ�O�Ĥ@���i�J
		if (br_name.length <= 0) {
			// �e���s�W�@�Ӽs�����w��e��
			Intent intent = new Intent();
			intent.setClass(Broadcast.this, welcome_broadcast.class);
			startActivity(intent);

		}

	}

	// �M�θs�ռs���]�w
	private void apply_br_sit(final String bro_name) {

		isLoading = true;
		final ProgressDialog PDialog = ProgressDialog.show(context, "�B�z��",
				"���b�s�u����A��...", true);

		new Thread() {

			@Override
			public void run() {

				// ���J���w�q���𱡹�(�s�ռs������)
				Mycommand a = new Mycommand() {
					@Override
					public void command() {
						Broadcast.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("���b���J�s�ռs������...");
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
					// ���s�j�w�������ݱ���
					Mycommand r = new Mycommand() {
						@Override
						public void command() {
							Broadcast.this.runOnUiThread(new Runnable() {
								public void run() {
									PDialog.setMessage("���b���s�j�w��������: "
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

				// step2 ���JTX��
				Mycommand b = new Mycommand() {
					@Override
					public void command() {
						Broadcast.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("���s���J�ǰe��...");
							}
						});

						Turbo_View tv = new Turbo_View();
						tv.Do_searchTx();
					}

				};
				// step3 ���JRX��
				Mycommand c = new Mycommand() {
					@Override
					public void command() {
						Broadcast.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("���s���J������...");
							}
						});

						Turbo_View tv = new Turbo_View();
						tv.Do_searchRx();
					}

				};

				// �ҰʤU�R�O

				b.start();
				c.start();

				// �����������U�R�O,�~��������u�@
				try {
					b.join();
					c.join();
				} catch (InterruptedException e) {
					Log.i("���J���ҩR�O�ɵo�Ϳ��~...", e.getMessage());
				}

				Mycommand d = new Mycommand() {
					@Override
					public void command() {
						Broadcast.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("���b���s���J���...");
							}
						});

						// �q��Ʈw�����s���J���
						Data.getTxData();
						Data.getRxData();
						Data.getSituationData();

					}
				};

				d.start();
				try {
					d.join();
				} catch (InterruptedException e) {
					Log.i("���J��Ʈw�ɵo�Ϳ��~...", e.getMessage());
				}

				Broadcast.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// ���s��s3��ListView
						// 30����
						Broadcast.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("���b����...");
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
						// �������ݹ�ܮ�
						PDialog.dismiss();
						isLoading = false;
						goto_perfer();

					}

				});

			}
		}.start();

	}

	// �e�����w�]�w (�û��O����1)
	private void goto_perfer() {

		Bundle bundle = new Bundle();
		// �û��u���@�ӱ���
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

	// ��^����w
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
		menu.add(0, 0, 0, "�R���s���]�w");
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

	// ��ܭn�R�����q����]�w�W�٦C���ܮ�
	private void showDialog_VWname() {

		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("�R���q����]�w");
		isDeleteCheck = new boolean[br_name.length];
		Arrays.fill(isDeleteCheck, false);
		d.setMultiChoiceItems(br_name, isDeleteCheck,
				new OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						isDeleteCheck[which] = isChecked;
					}
				});
		// �R���q����]�w
		d.setPositiveButton("�T�w", clickOK);
		// ���}�R���q����]�w��ܮ�
		d.setNegativeButton("����", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		d.show();
	}

	// ���UOK (�R���q����]�w��ܮ�)
	private OnClickListener clickOK = new OnClickListener() {
		// �Ұʵ��ݹ�ܮ�
		@Override
		public void onClick(DialogInterface dialog, int which) {

			isLoading = true;
			final ProgressDialog PDialog = ProgressDialog.show(context, "�B�z��",
					"���b�s�u����A��...", true);
			new Thread() {
				@Override
				public void run() {

					// �R���ҿ諸����
					for (int i = 0; i < isDeleteCheck.length; i++) {
						// �R���ҿ諸�q����]�w
						if (isDeleteCheck[i]) {
							final String name = br_name[i];
							Mycommand a = new Mycommand() {
								@Override
								public void command() {
									broadcastActivity
											.runOnUiThread(new Runnable() {
												public void run() {
													PDialog.setMessage("���b�R���s�ռs��: "
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

						// �R��������,���s��s TRS 3�ݻPUI���
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

	// �j�M�è�s T R S 3�ݪ����,�è�sUI����
	private void reload_tx_rx_sit_adapter(final ProgressDialog PDialog) {

		// step2 ���JTX��
		Mycommand b = new Mycommand() {
			@Override
			public void command() {
				broadcastActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("���s���J�ǰe��...");
					}
				});
				Turbo_View tv = new Turbo_View();
				tv.Do_searchTx();
			}
		};
		// step3 ���JRX��
		Mycommand c = new Mycommand() {
			@Override
			public void command() {
				broadcastActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("���s���J������...");
					}
				});
				Turbo_View tv = new Turbo_View();
				tv.Do_searchRx();
			}
		};

		b.start();
		c.start();

		// �����������U�R�O,�~�������J��Ʈw���
		try {
			b.join();
			c.join();
		} catch (InterruptedException e) {
			Log.i("���J���ҩR�O�ɵo�Ϳ��~...", e.getMessage());
		}

		Thread sqlThread = new Thread() {
			@Override
			public void run() {
				broadcastActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("���b���s���J�ǰe��...");
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
						PDialog.setMessage("���b���s���J�����ݸ��...");
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
						PDialog.setMessage("���b���s���J�q������...");
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
						PDialog.setMessage("���b���s���J�@��s�u���...");
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
				// ���s��s3��ListView
				// 30����
				broadcastActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("���b����...");
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
				// �������ݹ�ܮ�
				PDialog.dismiss();
				isLoading = false;
			}
		});
	}

	// ���s�����Ʈw���,��sSpinner
	public static void refersh_ListView() {

		broadcastActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Thread sqlThread = new Thread() {
					@Override
					public void run() {
						// ���s�j�M �Ҧ� �w�x�s���q���𱡹�
						Data.getVWData();
					}
				};
				sqlThread.start();
				try {
					sqlThread.join();
				} catch (InterruptedException e) {
					Log.i("�U�F�j�M�q���𱡹Үɵo�Ϳ��~", e.getMessage());
				}
				Broadcast.BRAdapter.ref(Data.getVW_BR_name(1), Data.VW_pic);
			}
		});
	}

	private void go_to_noconn() {

		broadcastActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(broadcastActivity, "�S�������s�u", Toast.LENGTH_LONG)
						.show();

				Intent intent = new Intent();
				intent.setClass(broadcastActivity, Noconn.class);
				startActivity(intent);
				finish();

			}
		});

	}
}
