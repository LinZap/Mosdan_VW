package activity;

import shared.ui.actionscontentview.ActionsContentView;
import ui.ExpandAdapter;
import ui.ListviewAdapter;
import ui.Mycommand;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class View_container extends Activity {

	private ActionsContentView viewActionsContentView;
	private ViewFlipper viewflipper;
	private ExpandAdapter sideListAdpter;
	private ExpandableListView sideList;
	private Typeface font_family;
	private Button home_tx, home_rx, home_tryit;
	private LinearLayout home_groupsingset, home_aboutmos, home_contactmos,
			home_moremos, generalcon;
	private TextView generalconbig, generalconsmall, home_groupsing, home_set,
			home_custom1, home_custom2, home_about, home_contact, home_more;
	private MenuItem sync, gear;
	private ListView Rxlistview, Txlistview, Situation_listView;
	public static ListviewAdapter Rxadapter, Txadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.actionscontentview);

		// all of element
		findView();
		Data.view_display = Data.view_Home;
		viewflipper.setDisplayedChild(Data.view_display);
		// Data.record.add(Data.view_display);
		// top
		Drawable d = getResources().getDrawable(R.drawable.barrepeat);
		getActionBar().setBackgroundDrawable(d);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowTitleEnabled(false);

		// side list
		sideListAdpter = new ExpandAdapter(this);
		sideList.setAdapter(sideListAdpter);
		sideList.setOnChildClickListener(sideListListener);

		// put in
		setListener();
		setFontFamily();
		setAdapter();
		setSideList();

	}

	private void setFontFamily() {

		home_tx.setTypeface(font_family);

		home_rx.setTypeface(font_family);

		home_groupsing.setTypeface(font_family);
		home_set.setTypeface(font_family);
		home_tryit.setTypeface(font_family);
		home_custom1.setTypeface(font_family);
		home_custom2.setTypeface(font_family);
		home_about.setTypeface(font_family);
		home_contact.setTypeface(font_family);
		home_more.setTypeface(font_family);

		generalconbig.setTypeface(font_family);
		generalconsmall.setTypeface(font_family);
		// sub_title.setTypeface(font_family);
	}

	private void findView() {

		// local
		viewflipper = (ViewFlipper) findViewById(R.id.flip_container);
		viewActionsContentView = (ActionsContentView) findViewById(R.id.content);
		sideList = (ExpandableListView) findViewById(R.id.actions);
		font_family = Typeface.createFromAsset(getAssets(), "fonts/japan.otf");
		home_tx = (Button) findViewById(R.id.button1);
		home_rx = (Button) findViewById(R.id.button3);
		home_tryit = (Button) findViewById(R.id.textView5);
		home_groupsingset = (LinearLayout) findViewById(R.id.VW_layout);
		home_aboutmos = (LinearLayout) findViewById(R.id.set3);
		home_contactmos = (LinearLayout) findViewById(R.id.set2);
		home_moremos = (LinearLayout) findViewById(R.id.set1);
		home_groupsing = (TextView) findViewById(R.id.textView2);
		home_set = (TextView) findViewById(R.id.textView3);
		home_custom1 = (TextView) findViewById(R.id.textView1);
		home_custom2 = (TextView) findViewById(R.id.textView4);
		home_about = (TextView) findViewById(R.id.textView6);
		home_contact = (TextView) findViewById(R.id.textView8);
		home_more = (TextView) findViewById(R.id.textView10);

		// Rx
		Rxlistview = (ListView) findViewById(R.id.listView1);
		// Tx
		Txlistview = (ListView) findViewById(R.id.listView2);
		// Situation
		Situation_listView = (ListView) findViewById(R.id.listView3);

		// 一般連線
		generalcon = (LinearLayout) findViewById(R.id.generalcon);
		generalconbig = (TextView) findViewById(R.id.generalconbig);
		generalconsmall = (TextView) findViewById(R.id.generalconsmall);
	}

	private void setAdapter() {
		// default
		Rxadapter = new ListviewAdapter(this, Data.Rx_name, Data.Rx_status);
		Txadapter = new ListviewAdapter(this, Data.Tx_name, Data.Tx_status);
		Rxlistview.setAdapter(Rxadapter);
		Txlistview.setAdapter(Txadapter);

	}

	private void setListener() {
		home_tx.setOnClickListener(onClickListener);
		home_rx.setOnClickListener(onClickListener);
		home_tryit.setOnClickListener(onClickListener);
		home_groupsingset.setOnClickListener(onClickListener);
		home_aboutmos.setOnClickListener(onClickListener);
		home_contactmos.setOnClickListener(onClickListener);
		home_moremos.setOnClickListener(onClickListener);
		generalcon.setOnClickListener(onClickListener);
		Rxlistview.setOnItemClickListener(OnItemClickListener);
		Txlistview.setOnItemClickListener(OnItemClickListener2);
		Situation_listView.setOnItemClickListener(OnItemClickListener3);

	}

	private OnItemClickListener OnItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View v, int i, long arg3) {
			Bundle bundle = new Bundle();
			bundle.putInt("index", i);
			Intent intent = new Intent();
			intent.setClass(View_container.this, Rx_set_view.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	private OnItemClickListener OnItemClickListener2 = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View v, int i, long arg3) {
			Bundle bundle = new Bundle();
			bundle.putInt("index", i);
			Intent intent = new Intent();
			intent.setClass(View_container.this, Tx_set_view.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}

	};

	private OnItemClickListener OnItemClickListener3 = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> arg0, View v, int i, long arg3) {
			Bundle bundle = new Bundle();
			bundle.putInt("index", i);
			Intent intent = new Intent();
			intent.setClass(View_container.this, Situation_set_view.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	// GoBack button onClick
	// 如果畫面正顯示Group sub ListView 就隱藏並重新顯示 Group ListView
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK)
			if (Data.view_display != 0) {

				Data.view_display = 0;
				viewActionsContentView.showContent();
				viewflipper.setDisplayedChild(0);
				change_view_doSomething(0);

				return false;
			} else {

				final AlertDialog.Builder d = new AlertDialog.Builder(this);
				d.setTitle("離開");
				d.setMessage("確定離開本程式嗎?");
				d.setNegativeButton("確定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								View_container.this.finish();
								dialog.dismiss();
							}

						});
				d.setNeutralButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}

				});
				d.show();

				return false;
			}
		return super.onKeyDown(keyCode, event);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			switch (v.getId()) {

			// Tx client button
			case R.id.button1:

				change_view(Data.view_Tx);
				Set_Expandablelist_Record(1, 0);
				// getData_In_Ui_Thread();

				break;

			// Rx client button
			case R.id.button3:

				change_view(Data.view_Rx);
				Set_Expandablelist_Record(1, 1);
				// getData_In_Ui_Thread();
				break;

			// GroupControl button
			case R.id.generalcon:
				Intent intent = new Intent();
				intent.setClass(View_container.this, Gconn_main.class);
				startActivity(intent);
				break;

			// Tryit button
			case R.id.textView5:

				Intent intent3 = new Intent();
				intent3.setClass(View_container.this, Broadcast.class);
				startActivity(intent3);

				break;

			// GroupRadio button
			case R.id.VW_layout:
				Intent intent2 = new Intent();
				intent2.setClass(View_container.this, VW_main.class);
				startActivity(intent2);
				break;
			// AboutMosdan button
			case R.id.set3:
				break;
			// ContactMosdan button
			case R.id.set2:
				break;
			// MoreMosdan button
			case R.id.set1:
				break;

			}
		}
	};

	// 返回時 更新對應畫面的LisView
	@Override
	protected void onResume() {
		switch (Data.view_display) {
		// Rx
		case 1:
			refListView(1);
			break;
		// Tx
		case 2:
			refListView(2);
			break;
		}
		super.onResume();
	}

	private void refListView(final int i) {

		new Thread() {

			@Override
			public void run() {
				Thread a = new Thread() {
					@Override
					public void run() {
						if (i == 1)
							Data.getRxData();
						else if (i == 2)
							Data.getTxData();
					}
				};

				a.start();
				try {
					a.join();
				} catch (InterruptedException e) {
					go_to_noconn();
				}

				if (i == 1) {

					View_container.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Rxadapter.ref(Data.Rx_name, Data.Rx_status);
							Toast.makeText(View_container.this, "更新完成",
									Toast.LENGTH_SHORT).show();
						}
					});

				} else if (i == 2) {

					View_container.this.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Txadapter.ref(Data.Tx_name, Data.Tx_status);
							Toast.makeText(View_container.this, "更新完成",
									Toast.LENGTH_SHORT).show();
						}
					});

				}

			}

		}.start();

	}

	// 改變目前顯示的畫面, view = 欲前往的畫面;
	private boolean change_view(int view) {
		if (view != 0) {
			Data.view_display = view;
			viewActionsContentView.showContent();
			viewflipper.setDisplayedChild(Data.view_display);
			change_view_doSomething(view);
			return false;
		} else {
			return false;
		}

	}

	public void Set_Expandablelist_Record(int Group, int Child) {
		Data.groupSelected = Group;
		Data.childSelected = Child;
		sideListAdpter.notifyDataSetChanged();
	}

	public void Go_Back_For_Expandablelist(int event) {
		switch (event) {
		case 0:
			Set_Expandablelist_Record(0, 0);

			break;
		case 1:
			Set_Expandablelist_Record(1, 1);

			break;
		case 2:
			Set_Expandablelist_Record(1, 0);

			break;
		case 3:
			Set_Expandablelist_Record(0, 1);

			break;
		case 4:
			Set_Expandablelist_Record(0, 2);

			break;
		case 5:

			break;
		}
	}

	private void change_view_doSomething(int view) {
		switch (view) {

		case 0:
			sync.setVisible(false);
			gear.setVisible(true);
			break;
		case 1:
			sync.setVisible(true);
			gear.setVisible(false);
			break;
		case 2:
			sync.setVisible(true);
			gear.setVisible(false);
			break;
		case 3:
			sync.setVisible(true);
			gear.setVisible(false);
			break;
		case 4:
			sync.setVisible(true);
			gear.setVisible(false);
			break;
		}
	}

	// side List fixed
	private void setSideList() {

		// Auto expand all item
		for (int i = 0; i < sideList.getCount(); i++)
			sideList.expandGroup(i);
		// fixed the slide bar group title
		sideList.setOnGroupClickListener(new OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				sideList.expandGroup(groupPosition);
				return true;
			}

		});

	}

	// sideList Listener
	private OnChildClickListener sideListListener = new ExpandableListView.OnChildClickListener() {
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {

			switch (groupPosition) {

			case 0:
				switch (childPosition) {
				case 0:
					change_view(Data.view_Home);
					Set_Expandablelist_Record(groupPosition, childPosition);
					break;
				case 1:
					Set_Expandablelist_Record(groupPosition, childPosition);
					break;
				case 2:
					Set_Expandablelist_Record(groupPosition, childPosition);
					break;
				case 3:
					Set_Expandablelist_Record(groupPosition, childPosition);
					break;
				}
				break;

			case 1:
				switch (childPosition) {
				case 0:
					change_view(Data.view_Tx);
					Set_Expandablelist_Record(groupPosition, childPosition);
					break;
				case 1:
					change_view(Data.view_Rx);
					Set_Expandablelist_Record(groupPosition, childPosition);
					break;
				}
				break;
			}

			return false;
		}

	};

	// MenuBar List
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.progress_scanner, menu);
		menu.removeItem(R.id.save);
		menu.removeItem(R.id.status_main);
		menu.removeItem(R.id.status_arrow);
		menu.removeItem(R.id.add);
		menu.removeItem(R.id.gconn_refresh);
		menu.removeItem(R.id.VW_refresh);
		gear = menu.findItem(R.id.set);
		sync = menu.findItem(R.id.sync);
		sync.setVisible(false);
		return true;
	}

	// MenuBar Listener
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (viewActionsContentView.isActionsShown())
				viewActionsContentView.showContent();
			else
				viewActionsContentView.showActions();
			return super.onOptionsItemSelected(item);
		case R.id.sync:
			sync.setEnabled(false);
			switch (Data.view_display) {
			case Data.view_Rx:

				View_container.this.runOnUiThread(new Runnable() {
					public void run() {
						Thread network = new Thread() {
							public void run() {
								Data.getRxData();
							}
						};
						network.start();
						try {
							network.join();
						} catch (InterruptedException e) {
							go_to_noconn();
						}
						Rxadapter.ref(Data.Rx_name, Data.Rx_status);
						sync.setEnabled(true);
					}
				});

				break;
			case Data.view_Tx:

				View_container.this.runOnUiThread(new Runnable() {
					public void run() {
						Thread network = new Thread() {
							public void run() {
								Data.getTxData();
							}

						};
						network.start();
						try {
							network.join();
						} catch (InterruptedException e) {
							go_to_noconn();
						}
						Txadapter.ref(Data.Tx_name, Data.Tx_status);
						sync.setEnabled(true);
					}
				});
				break;

			}

			// getData_In_Ui_Thread();
			return super.onOptionsItemSelected(item);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void go_to_noconn() {

		View_container.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(View_container.this, "沒有網路連線", Toast.LENGTH_LONG)
						.show();

				Intent intent = new Intent();
				intent.setClass(View_container.this, Noconn.class);
				startActivity(intent);
				finish();

			}
		});

	}

}
