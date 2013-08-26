package activity;

import ui.Mycommand;
import ui.Rx_Perference;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class Group_set_view extends Activity {
	private static Context context;
	private int index = -1;
	private static Activity group_set_viewActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState)
			throws NullPointerException {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.empty);

		context = this;
		group_set_viewActivity = this;
		Bundle bun = this.getIntent().getExtras();
		int index = bun.getInt("index");

		FragmentManager fm = this.getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Rx_Perference rx_p = null;

		rx_p = new Rx_Perference(index);
		FragmentTransaction ft2 = ft.replace(android.R.id.content, rx_p);
		ft2.commit();

		// top
		Drawable d = getResources().getDrawable(R.drawable.barrepeat);
		getActionBar().setBackgroundDrawable(d);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		// �^�_���w�]��
		case 0:
			reset();
			break;
		// �R���ǰe��
		case 1:
			deleteGroup();
			break;

		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.progress_scanner, menu);
		menu.removeItem(R.id.set);
		menu.removeItem(R.id.status_main);
		menu.removeItem(R.id.status_arrow);
		menu.removeItem(R.id.add);
		menu.removeItem(R.id.gconn_refresh);
		menu.removeItem(R.id.save);
		menu.removeItem(R.id.VW_refresh);
		menu.removeItem(R.id.sync);
		menu.add(0, 0, 0, "Reset");
		menu.add(0, 1, 1, "Delete");
		return true;
	}

	private void deleteGroup() {

		new Thread() {

			public void run() {

				Mycommand sr = new Mycommand() {
					@Override
					public void command() {

						Turbo_View tv = new Turbo_View();
						tv.TR_delete(Data.Rx_mac[index]);
					}
				};

				try {
					sr.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Mycommand b = new Mycommand() {
					@Override
					public void command() {

						Data.getRxData();
					}
				};

				sr.start();
				b.start();

				try {
					sr.join();
					b.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				group_set_viewActivity.runOnUiThread(new Runnable() {
					public void run() {
						View_container.Rxadapter.ref(Data.Rx_name,
								Data.Rx_status);
						Toast.makeText(context, "�R�������ݦ��\", Toast.LENGTH_LONG)
								.show();

					}
				});

			}

		}.start();

	}

	// reset to default
	private void reset() {

		new Thread() {

			public void run() {

				Mycommand sr = new Mycommand() {
					@Override
					public void command() {

						Turbo_View tv = new Turbo_View();
						tv.TR_reset(Data.Rx_mac[index]);
					}
				};

				try {
					sr.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				Mycommand b = new Mycommand() {
					@Override
					public void command() {

						Data.getRxData();
					}
				};

				sr.start();
				b.start();

				try {
					sr.join();
					b.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				group_set_viewActivity.runOnUiThread(new Runnable() {
					public void run() {

						View_container.Rxadapter.ref(Data.Rx_name,
								Data.Rx_status);
						Toast.makeText(context, "�٭즨�w�]�Ȧ��\", Toast.LENGTH_LONG)
								.show();

					}
				});

			}

		}.start();

	}

}
