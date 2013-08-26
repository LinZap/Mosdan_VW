package activity;

import TV.Mosdan2.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Noconn extends Activity {
	
	boolean DEBUG = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.no_conn);
		
		
	
		
		
		Button button = (Button) findViewById(R.id.again);
		
		button.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				if(!DEBUG)intent.setClass(Noconn.this, Loading.class);
				else {
					intent.setClass(Noconn.this, View_container.class);
				}
				startActivity(intent);
				finish();
				
				
				
			}
		});
	}

}
