package activity;

import ui.Mycommand;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Loading extends Activity {
	private Animation alphaAnimation = null;
	private Animation alphaAnimation2 = null;
	private TextView tvProgressCircle = null;
	private static Activity loadingActivity;
	private String status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.progress_scanner);
		loadingActivity = this;
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/japan.otf");
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setTypeface(tf);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), "fonts/mos.ttf");
		TextView tv2 = (TextView) findViewById(R.id.tv_progress_circle);
		tv2.setTypeface(tf2);
		Typeface tf3 = Typeface.createFromAsset(getAssets(), "fonts/japan.otf");
		TextView tv3 = (TextView) findViewById(R.id.textView1);
		tv3.setTypeface(tf3);
		TextView tv4 = (TextView) findViewById(R.id.textView2);
		tv4.setTypeface(tf3);
		ProgressBar pr = (ProgressBar) findViewById(R.id.scanner);
		tvProgressCircle = (TextView) findViewById(R.id.tv_progress_circle);

		alphaAnimation = AnimationUtils.loadAnimation(this,
				R.anim.mosdanopalpha2);
		alphaAnimation.setFillEnabled(true); // 啟動Fill維持
		alphaAnimation.setFillAfter(true); // 設置動畫的最後Fps保持在View上面
		pr.setAnimation(alphaAnimation);
		tv2.setAnimation(alphaAnimation);

		alphaAnimation2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
		alphaAnimation2.setFillEnabled(true); // 啟動Fill維持
		alphaAnimation2.setFillAfter(true);
		tv3.setAnimation(alphaAnimation2);
		tv4.setAnimation(alphaAnimation2);

		loading();
	}

	private void loading() {

		Thread a = new Thread() {

			@Override
			public void run() {

				Mycommand st = new Mycommand() {
					@Override
					public void command() {

						loadingActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tvProgressCircle.setText("正在搜尋傳送端");
							}
						});

						Turbo_View tv = new Turbo_View();
						status = tv.Do_searchTx();

					}
				};

				st.start();
				try {
					st.join(5000);
				} catch (InterruptedException e1) {
					go_to_noconn();
				}

				if (status != null)

				{
					if (status.equals("404")) {

						go_to_noconn();

					} else {

						Mycommand sr = new Mycommand() {
							@Override
							public void command() {

								loadingActivity.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										tvProgressCircle.setText("正在搜尋接收端");
									}
								});

								Turbo_View tv = new Turbo_View();
								tv.Do_searchRx();

							}
						};
						sr.start();
						try {
							sr.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						loadingActivity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tvProgressCircle.setText("正在更新資料");

							}
						});

						Thread sqlThread = new Thread() {
							@Override
							public void run() {
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
						sqlThread = new Thread() {
							@Override
							public void run() {
								Data.getVWData();
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
								Data.getGconnData();
							}
						};
						sqlThread.start();
						try {
							sqlThread.join();

						} catch (InterruptedException e) {
							go_to_noconn();
						}
						
						
						// 自動更新資料
						Data.auto_refreah_data();
						
						
						Intent intent = new Intent();
						intent.setClass(Loading.this, View_container.class);
						startActivity(intent);
						finish();

					}
				} else {
					go_to_noconn();
				}

			}

		};

		a.start();

	}

	private void go_to_noconn() {

		loadingActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(Loading.this, "沒有網路連線", Toast.LENGTH_LONG)
						.show();

				Intent intent = new Intent();
				intent.setClass(Loading.this, Noconn.class);
				startActivity(intent);
				finish();

			}
		});

	}

}
