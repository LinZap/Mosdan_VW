package activity;

import ui.Mycommand;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class VW_add1 extends Activity {
	private Button sor_next, sor_cancel;
	private ListView sor_listView;
	private int situation_idx,mode;
	private TextView titTextView;
	private int tx_idx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 選擇來源Activity
		setContentView(R.layout.choice_sor);
		situation_idx = getIntent().getExtras().getInt("situation", 0);
		findView();
		mode = getIntent().getExtras().getInt("mode", 0);
		if(mode==1){			
			sor_cancel.setText("取消");
			sor_next.setText("確定");		
			titTextView.setText("編輯電視牆設定");
		}
		
		tx_idx = get_tx_mac();
		setListener();
		setAdapter();
		
		
		
		
	}

	//取得TX索引位置
	private int get_tx_mac() {
		int idx = -1;
		for (int i = 0; i < Data.Tx_mac.length; i++)
			if (Data.Tx_mac[i].equals(Data.Situation_srctx[situation_idx]))
				idx = i;

		return idx;
	}

	private void findView() {
		sor_next = (Button) findViewById(R.id.sor_next);
		sor_cancel = (Button) findViewById(R.id.sor_cancel);
		sor_listView = (ListView) findViewById(R.id.sor_listView);
		titTextView = (TextView) findViewById(R.id.tit);
	}

	private void setListener() {
		sor_next.setOnClickListener(onClickListener);
		sor_cancel.setOnClickListener(onClickListener);
		sor_listView.setOnItemClickListener(OnItemClickListener);
	}

	private void setAdapter() {
		ArrayAdapter<String> TxArrayData = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, Data.Tx_name);
		sor_listView.setAdapter(TxArrayData);
		sor_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		if (tx_idx != -1) {
			sor_listView.setItemChecked(tx_idx, true);
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			switch (v.getId()) {
			// 選擇來源的下一步按鈕
			case R.id.sor_next:

				if (tx_idx != -1)
					new Thread() {
						@Override
						public void run() {

							Mycommand a = new Mycommand() {
								@Override
								public void command() {
									Turbo_View t = new Turbo_View();
									t.VW_connTx(
											Data.Situation_name[situation_idx],
											Data.Tx_mac[tx_idx]);
								}
							};
							a.start();
							try {
								a.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							
							Data.Situation_srctx[situation_idx] = Data.Tx_mac[tx_idx];
							// INTENT下指令
							Log.i("Situation_srctx["+situation_idx+"]=" , "Tx_mac["+tx_idx+"]");
							
							//Android.Log.i("","");
							
							if(mode!=1){
							Bundle bundle = new Bundle();
							bundle.putInt("situation", situation_idx);
							bundle.putInt("srctx", tx_idx);
							Intent intent = new Intent();
							intent.setClass(VW_add1.this, VW_add2.class);
							intent.putExtras(bundle);
							startActivity(intent);
							}
							
							finish();
						}

					}.start();
				else
					Toast.makeText(VW_add1.this, "您必須選擇一個來源",
							Toast.LENGTH_SHORT).show();

				break;

			// 選擇來源的取消按鈕
			case R.id.sor_cancel:
				finish();
				break;
 
			}
		}
	};

	// 選擇來源listView事件監聽
	private OnItemClickListener OnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index,
				long arg3) {
			tx_idx = index;
			
		}
	};
}
