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
		// �w�]���Ħ���(�P�P�_�}�C)
		
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
			next.setText("�T�w");
			cancel.setText("����");
			titTextView.setText("�s��s�ռs��");
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

				// �U�@�B
				if(mode!=1)
				alertSave();
				else {
					refresh_bor_List("�����]�w");
				}
				break;

			}

		}
	};

	private void alertSave() {

		final AlertDialog.Builder d = new AlertDialog.Builder(br_add2.this);
		d.setTitle("�s�ռs���W��");
		d.setMessage("���o�Ӹs�ռs���]�w�@�ӦW��");
		final EditText editText = new EditText(br_add2.this);
		editText.setHint("Bro_001");

		d.setView(editText);
		d.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if (editText.getText().toString().equals("")) {
					d.setMessage("�п�J�W��");
					alertSave();
				} else {
					// �t�s�s�ռs������,�í��s��z�M��
					refresh_bor_List(editText.getText().toString());
				}
			}

		});
		d.show();

	}

	private void refresh_bor_List(final String s) {

		// ���U�x�s�R�O > ���s�����Ʈw��ƨøѪR > ��s���

		isLoading = true;
		final ProgressDialog PDialog = ProgressDialog.show(br_add2.this, "�B�z��",
				"���b�s�u����A��...", true);

		new Thread() {

			@Override
			public void run() {

				
				
				
				
				
				// �]�wRx���ݱ���

				for (int i = 0; i < isRx.length; i++) {

					if (isRx[i]) {
						final String mac = Data.Rx_mac[i];
						Mycommand a = new Mycommand() {
							@Override
							public void command() {

								br_add2.this.runOnUiThread(new Runnable() {
									public void run() {
										PDialog.setMessage("���b���J: " + mac
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
											PDialog.setMessage("���b�]�w����: " + mac
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

					// �x�s����
					Mycommand a = new Mycommand() {
						@Override
						public void command() {

							br_add2.this.runOnUiThread(new Runnable() {
								public void run() {
									PDialog.setMessage("���b�x�s����: " + s + "...");
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

					
					
					
					// ���s���J���
					Thread b = new Thread() {
						@Override
						public void run() {
							br_add2.this.runOnUiThread(new Runnable() {
								public void run() {
									PDialog.setMessage("���s���J���: " + s + "...");
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
				
				
				
				// ��sListView��� , �Ǩ짹���e��
				br_add2.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						br_add2.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("���b����: " + s + "...");
							}
						});

						Broadcast.BRAdapter.ref(Data.getVW_BR_name(1),
								Data.VW_pic);
						Toast.makeText(br_add2.this, "�t�s����", Toast.LENGTH_LONG)
								.show();
						
						// �������ݹ�ܮ�

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