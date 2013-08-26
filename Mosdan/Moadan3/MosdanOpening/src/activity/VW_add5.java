package activity;

import ui.Mycommand;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class VW_add5 extends Activity {
	private int situation_idx;
	private Button set_more_st, save_new_st, return_main;
	private TextView siTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ����Activity
		setContentView(R.layout.choice_finish);
		findView();
		setListener();
		situation_idx = getIntent().getExtras().getInt("situation", 0);

		siTextView.setText("����" + (situation_idx+1));

		isFirst();
	}

	// �Ĥ@���i�J
	private void isFirst() {
		if (Data.getVW_BR_name(0).length <= 0) {
			showDialog_nameVW();
		}
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
				Toast.makeText(VW_add5.this, editText.getText().toString(),
						Toast.LENGTH_LONG).show();
				if (editText.getText().toString().equals("")) {
					showDialog_nameVW();
				} else {
					// �t�s�q���𱡹�,�í��s��s(�w�x�s���q���𱡹�)���
					save_VW(editText.getText().toString());
					dialog.dismiss();
				}
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
				VW_main.refersh_spinner(s);

				VW_add5.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(VW_add5.this, "�x�s����", Toast.LENGTH_LONG)
								.show();
					}
				});

			}
		}.start();
	}

	private void findView() {
		set_more_st = (Button) findViewById(R.id.set_more_st);
		save_new_st = (Button) findViewById(R.id.save_new_st);
		return_main = (Button) findViewById(R.id.return_main);
		siTextView = (TextView) findViewById(R.id.sit);

	}

	private void setListener() {
		set_more_st.setOnClickListener(onClickListener);
		save_new_st.setOnClickListener(onClickListener);
		return_main.setOnClickListener(onClickListener);
	}

	private void go_to_new_situation() {
		// ����n�x�s�b���ӱ��ҳ]�w����
		int situation_index = checkSituaton();
		// �٦�����,�i�J�s�W����
		if (situation_index > 0) {
			// �ǰe�@�ӷǳ��x�s�����Ҧ�m�L�h
			Bundle bundle = new Bundle();
			bundle.putInt("situation", situation_index);
			Intent intent = new Intent();
			intent.setClass(VW_add5.this, VW_add1.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}
		// 30�ӥΧ�,�i�D�ϥΪ̫�ĳ�ƶ�
		else
			show_Dialog_proposal();
	}

	// 30�ӱ��ҥΧ�,�X�{��ĳ�ƶ�����ܮ�
	private void show_Dialog_proposal() {
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("���ҳ]�w�w��");
		d.setMessage("�ܩ�p�A�z�ثe�ҮM�Ϊ��q���𱡹Ҥ��A30�ӳ]�w�w�����Q�ϥΡC��ĳ�z�^��D�e���A�ϥΡuMenu�v���u�إߥ��s���ҡv�ӳW���z���s�]�w�C�άO�I��u���ҳ]�w�v�ק�w�ϥιL���]�w�C");
		// ���}�R���q����]�w��ܮ�
		d.setNegativeButton("���D�F",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		d.show();
	}

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
		return not_use_situation;
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			switch (v.getId()) {
			// �]�w��h���ҫ��s
			case R.id.set_more_st:

				go_to_new_situation();

				break;

			// �t�s���s���q���𱡹ҫ��s
			case R.id.save_new_st:

				showDialog_nameVW();

				break;

			// ��^�D�e�����s
			case R.id.return_main:

				finish();

				break;
			}
		}
	};
}
