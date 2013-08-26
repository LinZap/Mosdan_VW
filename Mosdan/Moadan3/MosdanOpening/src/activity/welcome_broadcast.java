package activity;

import TV.Mosdan2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class welcome_broadcast extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.broadcast_welcome);

		Button button = (Button) findViewById(R.id.ok_see);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 去新增的第一步

				Bundle bundle = new Bundle();
				bundle.putInt("mode", 0);
				Intent intent = new Intent();
				intent.setClass(welcome_broadcast.this, br_add1.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});

	}

}
