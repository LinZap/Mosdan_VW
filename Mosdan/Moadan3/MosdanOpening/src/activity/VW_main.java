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
		// �B�z��FlowView

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

		// �����S���q���𱡹�,�i�J�w��e��
		if (vw_name.length <= 0) {
			Intent intent = new Intent();
			intent.setClass(VW_main.this, welcome_vw.class);
			startActivity(intent);
		}
	}

	// Spinner ��ܫ᪽���M�α���
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

	// ��ܱ��ұ˳]�w�A�e��30���ҳ]�w�e��
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
	 * @mode=0 �M�Τ@�뱡��
	 * @mode=-1 �M�ιw�]����
	 * */
	private void apply_vw_sit(final int mode, final String ss) {

		if (VW_Spinner.getSelectedItem() != null) {
			isLoading = true;
			final String vw_string = (mode == 0) ? "vw&&&"
					+ VW_Spinner.getSelectedItem().toString() : "defaultmosdan";
			// �Ұʵ��ݹ�ܮ�
			final ProgressDialog PDialog = ProgressDialog.show(context, "�B�z��",
					"���b�s�u����A��...", true);
			// �M�Ϊ� �@�s���s�ʧ@
			new Thread() {
				@Override
				public void run() {
					// step1 ���J�q���𱡹�
					Mycommand a = new Mycommand() {
						@Override
						public void command() {
							VW_mainActivity.runOnUiThread(new Runnable() {
								public void run() {
									PDialog.setMessage("���b���J�q���𱡹�...");
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
					// ���s�j�M�è�s T R S 3�ݪ����,��sUI���
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

	// �j�M�è�s T R S 3�ݪ����,�è�sUI����
	private void reload_tx_rx_sit_adapter(final ProgressDialog PDialog) {

		// step2 ���JTX��
		Mycommand b = new Mycommand() {
			@Override
			public void command() {
				VW_mainActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("���s���J�ǰe��...");
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
		// step3 ���JRX��
		Mycommand c = new Mycommand() {
			@Override
			public void command() {
				VW_mainActivity.runOnUiThread(new Runnable() {
					public void run() {
						PDialog.setMessage("���s���J������...");
					}
				});
				Turbo_View tv = new Turbo_View();
				tv.Do_searchRx();
			}
		};

		
		c.start();

		// �����������U�R�O,�~�������J��Ʈw���
		try {
			c.join();

		} catch (InterruptedException e) {
			Log.i("���J���ҩR�O�ɵo�Ϳ��~...", e.getMessage());
		}

		Thread sqlThread = new Thread() {
			@Override
			public void run() {
				VW_mainActivity.runOnUiThread(new Runnable() {
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
				VW_mainActivity.runOnUiThread(new Runnable() {
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

	

		VW_mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// ���s��s3��ListView
				// 30����
				VW_mainActivity.runOnUiThread(new Runnable() {
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
				
				
				refersh_spinner(null);
				
				// �������ݹ�ܮ�
				PDialog.dismiss();
				isLoading = false;
			}
		});
	}

	// ���ﶵ
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.progress_scanner, menu);
		menu.removeItem(R.id.sync);
		menu.removeItem(R.id.set);
		menu.removeItem(R.id.save);
		menu.removeItem(R.id.status_main);
		menu.removeItem(R.id.status_arrow);
		menu.removeItem(R.id.gconn_refresh);
		menu.add(0, 0, 0, "�إߥ��s����");
		menu.add(0, 1, 1, "�R������");
		return true;
	}

	// ���ﶵ�\��
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {

		// ���s��sSpinner���
		case R.id.VW_refresh:

			if (VW_Spinner.getSelectedItem() != null)
				refersh_spinner(null);
			break;

		// �P�_�O�_�n�s�W�q���𱡹ҡA�_�h��w�@�����ҳ]�w�i�J-->"�s�W�@�ӹq����]�w"
		case R.id.add:
			go_to_new_situation();
			break;
		// �R���H�x�s���q���𱡹�
		case 1:
			showDialog_VWname();
			break;
		// �s�W���s�q���𱡹�
		case 0:
			new_30situation();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private void go_to_new_situation() {
		// ����n�x�s�b���ӱ��ҳ]�w����
		int situation_index = checkSituaton();
		// �٦�����,�i�J�s�W����
		if (situation_index >= 0) {
			// �ǰe�@�ӷǳ��x�s�����Ҧ�m�L�h
			Bundle bundle = new Bundle();
			bundle.putInt("situation", situation_index);
			Intent intent = new Intent();
			intent.setClass(VW_main.this, VW_add1.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
		// 30�ӥΧ�,�i�D�ϥΪ̫�ĳ�ƶ�
		else
			show_Dialog_proposal();
	}

	// �إߥ��s��30�ӹq���𱡹�
	private void new_30situation() {

		// �R�W �s�q���𱡹� �W�ٹ�ܮ�
		showDialog_nameVW();
	}

	// �R�W �s�q���𱡹� �W�ٹ�ܮ�
	private void showDialog_nameVW() {
		final AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("�s�q���𱡹ҦW��");
		final EditText editText = new EditText(this);
		editText.setHint("�R�W�q���𱡹ҦW��");
		// �u���J�^��
		editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
		// �@�i�ӫK���o�J�I
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
					// �M�ιw�]�ťձ���
					apply_vw_sit(-1,editText.getText().toString());
					// �t�s�q���𱡹�,�í��s��s(�w�x�s���q���𱡹�)���
					dialog.dismiss();
				}
			}
		});
		d.show();
	}

	// �ˬd���Ҫ��ϥΪ��p,�^�Ǥ@�ө|���ϥ�(srctx=None,�զ�1*1,multihost=0)�����ү���
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

	// ��ܭn�R�����q����]�w�W�٦C���ܮ�
	private void showDialog_VWname() {

		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("�R���q����]�w");
		isDeleteCheck = new boolean[vw_name.length];
		Arrays.fill(isDeleteCheck, false);
		d.setMultiChoiceItems(vw_name, isDeleteCheck,
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
							final String name = vw_name[i];
							Mycommand a = new Mycommand() {
								@Override
								public void command() {
									VW_mainActivity
											.runOnUiThread(new Runnable() {
												public void run() {
													PDialog.setMessage("���b�R���q����]�w: "
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

						// �R��������,���s��s TRS 3�ݻPUI���
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

	// 30�ӱ��ҥΧ�,�X�{��ĳ�ƶ�����ܮ�
	private void show_Dialog_proposal() {
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("���ҳ]�w�w��");
		d.setMessage("�ܩ�p�A�z�ثe�ҮM�Ϊ��q���𱡹Ҥ��A30�ӳ]�w�w�����Q�ϥΡC��ĳ�z�ϥΡuMenu�v���u�إߥ��s���ҡv�ӳW���z���s�]�w�C�άO�I��u���ҳ]�w�v�ק�w�ϥιL���]�w�C");
		// ���}�R���q����]�w��ܮ�
		d.setNegativeButton("���D�F", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		d.show();
	}

	// (�t)�x�s�q���𱡹�(s���t�s�W��)
	private void save_VW(final String s) {
		// ���U�x�s�R�O > ���s�����Ʈw��ƨøѪR > ��s���
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

				// ��sSpinner
				refersh_spinner(s);

				VW_mainActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(VW_main.context, "�x�s����",
								Toast.LENGTH_LONG).show();
					}
				});

			}
		}.start();
	}

	// ���s�����Ʈw���,��sSpinner
	public static void refersh_spinner(final String default_select) {

		VW_mainActivity.runOnUiThread(new Runnable() {
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

	// ��^����w
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
				Toast.makeText(VW_main.this, "�S�������s�u", Toast.LENGTH_LONG)
						.show();

				Intent intent = new Intent();
				intent.setClass(VW_main.this, Noconn.class);
				startActivity(intent);
				finish();

			}
		});

	}

}
