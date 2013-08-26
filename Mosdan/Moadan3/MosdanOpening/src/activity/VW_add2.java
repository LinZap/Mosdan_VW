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
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class VW_add2 extends Activity {
	private Button makeof_next, makeof_prev;
	private NumberPicker pickerX, pickerY;
	private boolean isLoading = false;
	private int situation_idx, mode;
	private TextView titTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ��ܲզ�Activity
		setContentView(R.layout.choice_makeof);

		makeof_next = (Button) findViewById(R.id.makeof_next);
		makeof_prev = (Button) findViewById(R.id.makeof_prev);
		pickerX = (NumberPicker) findViewById(R.id.makeof_Picker1);
		pickerY = (NumberPicker) findViewById(R.id.makeof_Picker2);
		titTextView = (TextView) findViewById(R.id.tit);
		situation_idx = getIntent().getExtras().getInt("situation", 0);

		mode = getIntent().getExtras().getInt("mode", 0);
		if (mode == 1) {
			makeof_prev.setText("����");
			makeof_next.setText("�T�w");
			titTextView.setText("�s��q����]�w");
		}

		pickerX.setMaxValue(30);
		pickerY.setMaxValue(30);
		pickerX.setMinValue(1);
		pickerY.setMinValue(1);
		pickerX.setValue(Data.Situation_bulidx[situation_idx]);
		pickerY.setValue(Data.Situation_bulidy[situation_idx]);

		setListener();
		picker_set();
	}

	private void setListener() {
		makeof_next.setOnClickListener(onClickListener);
		makeof_prev.setOnClickListener(onClickListener);
	}

	private void picker_set() {
		pickerX.setMinValue(1);
		pickerX.setMaxValue(4);
		pickerY.setMinValue(1);
		pickerY.setMaxValue(4);

	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			switch (v.getId()) {

			case R.id.makeof_next:

				// ��ܲզ����U�@�B���s
				// INTENT+�U���O
				set_bulid_xy();

				break;

			// ��ܲզ����W�@�B���s
			case R.id.makeof_prev:
				
				if(mode!=1){
				Bundle bundle2 = new Bundle();
				bundle2.putInt("situation", situation_idx);
				Intent intent2 = new Intent();
				intent2.setClass(VW_add2.this, VW_add1.class);
				intent2.putExtras(bundle2);
				startActivity(intent2);}
				finish();
				break;

			}
		}

	};

	// �إ� XY �զ�
	private void set_bulid_xy() {

		isLoading = true;
		final ProgressDialog PDialog = ProgressDialog.show(VW_add2.this, "�B�z��",
				"���b�s�u����A��...", true);

		new Thread() {

			@Override
			public void run() {

				Mycommand a = new Mycommand() {
					@Override
					public void command() {

						VW_add2.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("���b�]�w�����զ�...");
							}
						});
						Turbo_View t = new Turbo_View();
						t.VW_X(Data.Situation_name[situation_idx],
								pickerX.getValue());
					}
				};
				a.start();
				try {
					a.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Mycommand b = new Mycommand() {
					@Override
					public void command() {
						VW_add2.this.runOnUiThread(new Runnable() {
							public void run() {
								PDialog.setMessage("���b�]�w�����զ�...");
							}
						});
						Turbo_View t = new Turbo_View();
						t.VW_Y(Data.Situation_name[situation_idx],
								pickerY.getValue());
					}
				};
				b.start();
				try {
					b.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Data.Situation_bulidx[situation_idx] = pickerX.getValue();
				Data.Situation_bulidy[situation_idx] = pickerY.getValue();

				PDialog.dismiss();
				isLoading = false;

				// �e���U�@�B
				if(mode!=1){
				Bundle bundle = new Bundle();
				bundle.putInt("situation", situation_idx);
				Intent intent = new Intent();
				intent.setClass(VW_add2.this, VW_add3.class);
				intent.putExtras(bundle);
				startActivity(intent);}
				finish();

			}

		}.start();

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
}
