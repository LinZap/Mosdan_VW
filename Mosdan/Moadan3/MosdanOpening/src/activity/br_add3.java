package activity;

import TV.Mosdan2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class br_add3 extends Activity {

	private TextView resultTextView;
	private Button btn_more, btn_home;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.br_add3);

		String result = getIntent().getExtras().getString("result", "br&&&");

		resultTextView = (TextView) findViewById(R.id.result_name);
		resultTextView.setText(result);

		btn_more = (Button) findViewById(R.id.button1);
		btn_home = (Button) findViewById(R.id.button2);

		btn_home.setOnClickListener(click);
		btn_more.setOnClickListener(click);

	}

	// 兩個反應
	private OnClickListener click = new OnClickListener() {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {
			case R.id.button1:

				Bundle bundle = new Bundle();
				bundle.putInt("mode", 0);
				Intent intent = new Intent();
				intent.setClass(br_add3.this, br_add1.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();

				break;
			case R.id.button2:

				finish();

				break;
			}

		}
	};

}
