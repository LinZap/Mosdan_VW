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
		// 完成Activity
		setContentView(R.layout.choice_finish);
		findView();
		setListener();
		situation_idx = getIntent().getExtras().getInt("situation", 0);

		siTextView.setText("情境" + (situation_idx+1));

		isFirst();
	}

	// 第一次進入
	private void isFirst() {
		if (Data.getVW_BR_name(0).length <= 0) {
			showDialog_nameVW();
		}
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
				Toast.makeText(VW_add5.this, editText.getText().toString(),
						Toast.LENGTH_LONG).show();
				if (editText.getText().toString().equals("")) {
					showDialog_nameVW();
				} else {
					// 另存電視牆情境,並重新刷新(已儲存的電視牆情境)表單
					save_VW(editText.getText().toString());
					dialog.dismiss();
				}
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
				VW_main.refersh_spinner(s);

				VW_add5.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(VW_add5.this, "儲存完成", Toast.LENGTH_LONG)
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
		// 抓取要儲存在哪個情境設定索引
		int situation_index = checkSituaton();
		// 還有情境,進入新增情境
		if (situation_index > 0) {
			// 傳送一個準備儲存的情境位置過去
			Bundle bundle = new Bundle();
			bundle.putInt("situation", situation_index);
			Intent intent = new Intent();
			intent.setClass(VW_add5.this, VW_add1.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		}
		// 30個用完,告訴使用者建議事項
		else
			show_Dialog_proposal();
	}

	// 30個情境用完,出現建議事項的對話框
	private void show_Dialog_proposal() {
		AlertDialog.Builder d = new AlertDialog.Builder(this);
		d.setTitle("情境設定已滿");
		d.setMessage("很抱歉，您目前所套用的電視牆情境中，30個設定已全部被使用。建議您回到主畫面，使用「Menu」→「建立全新情境」來規劃您的新設定。或是點選「情境設定」修改已使用過的設定。");
		// 離開刪除電視牆設定對話框
		d.setNegativeButton("知道了",
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
			// 設定更多情境按鈕
			case R.id.set_more_st:

				go_to_new_situation();

				break;

			// 另存為新的電視牆情境按鈕
			case R.id.save_new_st:

				showDialog_nameVW();

				break;

			// 返回主畫面按鈕
			case R.id.return_main:

				finish();

				break;
			}
		}
	};
}
