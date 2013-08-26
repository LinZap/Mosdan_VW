package activity;

import java.util.Arrays;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

public class VW_add3 extends Activity {
	private Button member_next, member_prev;
	private ListView member_listView;
	private boolean[] isRx;
	private CheckBox select_all;
	private ArrayAdapter<String> RxArrayData;
private TextView titTextView;
	private boolean isLoading = false;
	private int situation_idx,mode;
	private Integer[] memberIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ܦ���Activity
		setContentView(R.layout.choice_member);
		findView();
		
		mode = getIntent().getExtras().getInt("mode", 0);
		if(mode==1){			
			member_prev.setText("����");
			member_next.setText("�T�w");		
			titTextView.setText("�s��q����]�w");
		}
		
		isRx = new boolean[Data.Rx_name.length];
		situation_idx = getIntent().getExtras().getInt("situation", 0);
		memberIndex = Data
				.get_Situation_Member_idx(Data.Situation_name[situation_idx]);

		setListener();
		setAdapter();
	}

	private void findView() {
		member_next = (Button) findViewById(R.id.member_next);
		member_prev = (Button) findViewById(R.id.member_prev);
		member_listView = (ListView) findViewById(R.id.member_listView);
		select_all = (CheckBox) findViewById(R.id.select_all);
		titTextView = (TextView) findViewById(R.id.tit);
	}

	private void setListener() {
		member_next.setOnClickListener(onClickListener);
		member_prev.setOnClickListener(onClickListener);

		member_listView.setOnItemClickListener(OnItemClickListener);
		select_all.setOnCheckedChangeListener(OnCheckedChangeListener);

	}

	private OnItemClickListener OnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int i, long arg3) {
			isRx[i] = !isRx[i];
		}
	};

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			switch (v.getId()) {

			case R.id.member_next:

				// ��ܦ������U�@�B���s
				// INTENT,�U���O
				set_situation_member();

				break;

			// ��ܦ������W�@�B���s
			case R.id.member_prev:

				if(mode!=1){
				Bundle bundle2 = new Bundle();
				bundle2.putInt("situation", situation_idx);
				Intent intent2 = new Intent();
				intent2.setClass(VW_add3.this, VW_add2.class);
				intent2.putExtras(bundle2);
				startActivity(intent2);}
				finish();
				break;

			}
		}

	};

	// �]�w���Ҧ���+Intent�U�@�B
	private void set_situation_member() {

		isLoading = true;
		final ProgressDialog PDialog = ProgressDialog.show(VW_add3.this, "�B�z��",
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

								VW_add3.this.runOnUiThread(new Runnable() {
									public void run() {
										PDialog.setMessage("���b�]�w����: " + mac
												+ "...");
									}
								});
								Turbo_View turbo_View = new Turbo_View();
								turbo_View.VW_Rx_set(mac,
										Data.Situation_name[situation_idx]);
							}
						};
						a.start();
						try {
							a.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						
						Data.Rx_sitsution[i] = Data.Situation_name[situation_idx];
					} else {
						if (Data.Rx_sitsution[i]
								.equals(Data.Situation_name[situation_idx])) {
							
							final String mac = Data.Rx_mac[i];
							
							Mycommand a = new Mycommand() {
								@Override
								public void command() {

									VW_add3.this.runOnUiThread(new Runnable() {
										public void run() {
											PDialog.setMessage("���b�]�w����: " + mac
													+ "...");
										}
									});
									Turbo_View turbo_View = new Turbo_View();
									turbo_View.VW_Rx_set(mac,"0");
								}
							};
							a.start();
							try {
								a.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}						
							Data.Rx_sitsution[i] = "0";							
						}
					}
				}

				isLoading = false;
				PDialog.dismiss();

				
				if(mode!=1){
				Bundle bundle2 = new Bundle();
				bundle2.putInt("situation", situation_idx);
				Intent intent2 = new Intent();
				intent2.setClass(VW_add3.this, VW_add4.class);
				intent2.putExtras(bundle2);
				startActivity(intent2);}
				finish();

			}

		}.start();

	}

	private void setAdapter() {
		RxArrayData = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_multiple_choice, Data.Rx_name);
		member_listView.setAdapter(RxArrayData);
		member_listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

		Arrays.fill(isRx, false);
		// �w�]���Ħ���(�P�P�_�}�C)
		if (memberIndex != null)
			for (int i = 0; i < memberIndex.length; i++) {
				member_listView.setItemChecked(memberIndex[i], true);
				isRx[memberIndex[i]] = true;
			}

	}

	// ����Υ��������Ҧ�������Checkbox
	private CheckBox.OnCheckedChangeListener OnCheckedChangeListener = new CheckBox.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			if (select_all.isChecked() == isChecked) {
				for (int i = 0; i < RxArrayData.getCount(); i++)
					member_listView.setItemChecked(i, isChecked);
				Arrays.fill(isRx, isChecked);

			}
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

}
