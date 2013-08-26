package activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import ui.Mycommand;
import Data.Data;
import Internet.Turbo_View;
import TV.Mosdan2.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class VW_add4 extends Activity {

	private int WindowWidth, WindowHeight, column = 4, row = 4, boxWidth = 120,
			boxHeight = 110, autoMargin, boxGap = 40, boxTop = 100,
			containerTop = 100;;
	private MyLinerLayout container;
	private LinearLayout[][] pushMap;
	private String[][] contrastMap;
	private AbsoluteLayout all;
	private ArrayList<TextView> member;
	private ArrayList<String> osdmember;
	private Button order_next, order_prev;
	private boolean isLoading = false;
	private int situation_idx, mode;
	private Integer[] memberIndex;
	private HashMap<String, Integer> hintRxIndex;
	private HashMap<Integer, String> hintWord;
	private CheckBox auto_refresh;
	private Timer timer, timer2;
	private TimerTask auto_command, auto_osd;
	private TextView titTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.choice_order);

		
		findView();

		mode = getIntent().getExtras().getInt("mode", 0);

		if (mode == 1) {
			order_prev.setText("取消");
			order_next.setText("確定");
			titTextView.setText("編輯電視牆設定");
		}

		situation_idx = getIntent().getExtras().getInt("situation", 0);
		memberIndex = Data
				.get_Situation_Member_idx(Data.Situation_name[situation_idx]);
		setListener();

		bulidView();

		auto_command();

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		if(auto_command!=null)auto_command.cancel();
		if(auto_osd!=null)auto_osd.cancel();
		if(timer!=null)timer.cancel();
		if(timer2!=null)timer2.cancel();
		super.onDestroy();
	}

	// 自動下命令設定顯示位置 + 提示文字
	private void auto_command() {
		
		
		
		new Thread(){
			
			@Override
			public void run(){
				
				Thread sqlThread = new Thread() {
					@Override
					public void run() {
						Data.getRxData();
					}
				};
				sqlThread.start();
				try {
					sqlThread.join();
				} catch (InterruptedException e) {}
				
				
				auto_command = new TimerTask() {
					@Override
					public void run() {
						if (auto_refresh.isChecked())
							compare_and_command();
					}
				};

				auto_osd = new TimerTask() {
					@Override
					public void run() {

						display_hintOSD();
					}
				};

				timer = new Timer();
				timer.schedule(auto_command, 1000, 3500);
				timer2 = new Timer();
				timer2.schedule(auto_osd, 1000, 9000);		
				
			}	
		}.start();
			
		
	}

	private void display_hintOSD() {

		for (int i = 0; i < osdmember.size(); i++) {

			final int idx = i;
			Mycommand a = new Mycommand() {
				@Override
				public void command() {

					Turbo_View turbo_View = new Turbo_View();
					turbo_View.Rx_OSD(
							Data.Rx_ip[hintRxIndex.get(osdmember.get(idx))],
							hintWord.get(idx));
				}
			};
			a.start();
			try {
				a.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void compare_and_command() {

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {

				if (pushMap[i][j].getChildCount() > 0) {
					final int x = j + 1;
					final int y = i + 1;
					final String rx_key = ((TextView) pushMap[i][j]
							.getChildAt(0)).getText().toString();
					final String mac = Data.Rx_mac[hintRxIndex.get(rx_key)];
					if (!rx_key.equals(contrastMap[i][j])) {
						contrastMap[i][j] = rx_key;

						Mycommand a = new Mycommand() {
							@Override
							public void command() {
								Turbo_View turbo_View = new Turbo_View();
								turbo_View.VW_Rx_locX(mac, x);
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
								Turbo_View turbo_View = new Turbo_View();
								turbo_View.VW_Rx_locY(mac, y);
							}
						};

							b.start();
						try {
							b.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}

			}
		}
	}

	private void findView() {
		order_next = (Button) findViewById(R.id.order_next);
		order_prev = (Button) findViewById(R.id.order_prev);
		all = (AbsoluteLayout) findViewById(R.id.all);
		auto_refresh = (CheckBox) findViewById(R.id.auto_refresh);
		titTextView = (TextView) findViewById(R.id.tit);
	}

	private void setListener() {
		order_next.setOnClickListener(onClickListener);
		order_prev.setOnClickListener(onClickListener);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		public void onClick(View v) {

			switch (v.getId()) {
			// 選擇組成的下一步按鈕
			// INTENT下指令
			case R.id.order_next:

				set_cancel_timer_and_setLocXY();

				break;

			// 選擇組成的上一步按鈕
			case R.id.order_prev:

				if (mode != 1) {
					Bundle bundle2 = new Bundle();
					bundle2.putInt("situation", situation_idx);
					Intent intent2 = new Intent();
					intent2.setClass(VW_add4.this, VW_add3.class);
					intent2.putExtras(bundle2);
					startActivity(intent2);
				}
				finish();
				break;

			}
		}

	};

	// 取消Timer,執行最後一是設定位置
	private void set_cancel_timer_and_setLocXY() {

		isLoading = true;
		final ProgressDialog PDialog = ProgressDialog.show(VW_add4.this, "處理中",
				"設定中...", true);

		new Thread() {

			@Override
			public void run() {

				compare_and_command();
				isLoading = false;
				PDialog.dismiss();

				if (mode != 1) {
					Bundle bundle2 = new Bundle();
					bundle2.putInt("situation", situation_idx);
					Intent intent2 = new Intent();
					intent2.setClass(VW_add4.this, VW_add5.class);
					intent2.putExtras(bundle2);
					startActivity(intent2);
				}
				finish();

			}

		}.start();

	}

	private void bulidView() {
		// 取得螢幕寬度

		column = Data.Situation_bulidx[situation_idx];
		row = Data.Situation_bulidy[situation_idx];

		int[] size = getWindowWidth();
		WindowWidth = size[0];
		WindowHeight = size[1];
		boxWidth = Math.min(WindowWidth, WindowHeight) / (column + 2);
		boxHeight = (WindowHeight / 17);
		boxTop = 0;
		boxGap = (WindowHeight / 68);
		containerTop = (WindowWidth > WindowHeight) ? boxGap : 2 * boxGap;
		// 兩邊自動置中對齊
		autoMargin = (WindowWidth - (boxWidth * column + boxGap * (column - 1))) / 2;

		pushMap = new LinearLayout[row][column];
		contrastMap = new String[row][column];

		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {

				LinearLayout box = new LinearLayout(this);

				LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
						boxWidth, boxHeight);
				mLayoutParams.gravity = Gravity.CENTER;
				box.setGravity(Gravity.CENTER);
				box.setBackgroundColor(Color.rgb(222, 222, 222));
				box.setLayoutParams(mLayoutParams);

				box.setX(autoMargin + j * boxWidth + j * boxGap);
				box.setY(boxTop + i * boxHeight + i * boxGap);

				pushMap[i][j] = box;
				contrastMap[i][j] = "";
				all.addView(box);
			}
		}

		member = new ArrayList<TextView>();
		osdmember = new ArrayList<String>();
		hintRxIndex = new HashMap<String, Integer>();
		hintWord = new HashMap<Integer, String>();

		// 建立成員

		for (int i = 0; i < memberIndex.length; i++) {

			// 綁定KEY + VALUE
			hintRxIndex.put("Rx" + (i + 1), memberIndex[i]);
			// 綁定 VALUE + KEY
			hintWord.put(memberIndex[i], "Rx" + (i + 1));

			// 傳入KEY,建立成員
			member.add(newText("Rx" + (i + 1), true));

			osdmember.add("Rx" + (i + 1));
		}

		container = new MyLinerLayout(
				all,
				member,
				(int) (pushMap[row - 1][column - 1].getY() + boxHeight + containerTop),
				WindowWidth, boxWidth, boxHeight, boxGap - 20);

	}

	// 返回鍵鎖定
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isLoading)
				return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private OnTouchListener touch = new OnTouchListener() {

		LinearLayout cover = null;
		TextView realson = null;
		Object[] CoverZone;
		boolean bePushed = false, isleave = true;
		int cover_i, cover_j;

		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:

				target = null;
				cover = null;
				realson = null;
				bePushed = false;
				isleave = true;

				// 假的移動VIEW
				fakeView(((TextView) v).getText().toString(), v.getX(),
						v.getY());

				v.setVisibility(View.GONE);
				break;

			case MotionEvent.ACTION_MOVE:

				if (fake != null) {
					fake.setX(v.getX() + event.getX()
							- fake.getLayoutParams().width / 2);
					fake.setY(v.getY() + event.getY()
							- fake.getLayoutParams().height / 2);

					// Fake 物件是否有覆蓋區域,回傳覆蓋區域[0],區域的Y (i) [1]座標,區域的X (j) [2]座標
					CoverZone = iscover(fake.getX(), fake.getY());
				}
				// isIn = (CoverZone != null) ? true : false;
				// 如果有覆蓋到區域
				if (CoverZone != null) {

					if (isleave) {
						isleave = false;

						// 被覆蓋的區域
						cover = ((LinearLayout) CoverZone[0]);
						// 設定被覆蓋區域的背景提示

						Log.i("STAT", "IN:" + cover.toString());

						// 被覆蓋區域所屬的XY座標位置
						cover_i = (Integer) CoverZone[1];
						cover_j = (Integer) CoverZone[2];

						// android.util.Log.i("COVER", (cover_i+1)
						// +" , "+(cover_j+1));

						// 有兒子,表示需要推擠
						if (cover.getChildCount() > 0) {

							// 紀錄要被推擠的對象(兒子)
							realson = (TextView) cover.getChildAt(0);

							// 推擠到新的位置

							for (int i = 1; i < 26; i++)
								if (target == null)
									pushTo(i, cover_i, cover_j, realson);
								else
									break;

							if (target != null) {
								target.setBackgroundColor(Color.rgb(255, 255,
										255));
								bePushed = true;
							}

						} else {
							// Log.i("SET", cover.toString());
							cover.setBackgroundColor(Color.rgb(122, 122, 122));
						}

					}

				}
				// 如果沒有覆蓋

				else {

					isleave = true;

					// textView.setText(string);
					// 曾經被推擠過(離開了馬上還原)
					if (bePushed) {

						if (cover != null) {
							bePushed = false;

							cover.setBackgroundColor(Color.rgb(255, 255, 255));

							cover.addView(newText(realson.getText().toString(),
									false));

							// Log.i("RE", cover.toString());

							Log.i("STAT", "LEAVE:" + cover.toString());

							cover = null;
							target.setBackgroundColor(Color.rgb(222, 222, 222));
							target.removeAllViews();
							target = null;

						}
					} else {
						if (cover != null) {

							if (cover.getChildCount() > 0)
								cover.setBackgroundColor(Color.rgb(255, 255,
										255));
							else
								cover.setBackgroundColor(Color.rgb(222, 222,
										222));

							Log.i("STAT", "LEAVE:" + cover.toString());

							cover = null;
						}

					}

				}
				break;

			case MotionEvent.ACTION_UP:

				if (cover != null) {
					if (CoverZone != null && cover.getChildCount() < 1) {

						cover.addView(newText(((TextView) v).getText()
								.toString(), false));
						cover.setBackgroundColor(Color.rgb(255, 255, 255));
						container.deleteRx(((TextView) v));
						cover = null;

					} else {

						v.setVisibility(View.VISIBLE);
					}

				} else {

					v.setVisibility(View.VISIBLE);
				}
				fake.setVisibility(View.GONE);
				// recoverBox();

				break;
			default:
				return false;
			}
			return true;
		}

	};
	// //////////////////////////////////////////////////////////////////////////////////

	private OnTouchListener touch2 = new OnTouchListener() {

		LinearLayout cover = null;
		TextView realson = null;
		Object[] CoverZone;
		boolean bePushed = false, isleave = true;
		int cover_i, cover_j;
		LinearLayout father;

		public boolean onTouch(View v, MotionEvent event) {

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:

				target = null;
				cover = null;
				realson = null;
				bePushed = false;
				isleave = true;
				father = null;

				father = (LinearLayout) v.getParent();
				father.removeAllViews();

				// 假的移動VIEW
				fakeView(((TextView) v).getText().toString(), father.getX(),
						father.getY());

				break;

			case MotionEvent.ACTION_MOVE:

				if (fake != null) {
					fake.setX(father.getX() + event.getX()
							- fake.getLayoutParams().width / 2);
					fake.setY(father.getY() + event.getY()
							- fake.getLayoutParams().height / 2);

					// Fake 物件是否有覆蓋區域,回傳覆蓋區域[0],區域的Y (i) [1]座標,區域的X (j) [2]座標
					CoverZone = iscover(fake.getX(), fake.getY());
				}
				// isIn = (CoverZone != null) ? true : false;
				// 如果有覆蓋到區域
				if (CoverZone != null) {

					if (isleave) {
						isleave = false;

						// 被覆蓋的區域
						cover = ((LinearLayout) CoverZone[0]);
						// 設定被覆蓋區域的背景提示

						Log.i("STAT", "IN:" + cover.toString());

						// 被覆蓋區域所屬的XY座標位置
						cover_i = (Integer) CoverZone[1];
						cover_j = (Integer) CoverZone[2];

						// 有兒子,表示需要推擠
						if (cover.getChildCount() > 0) {

							// 紀錄要被推擠的對象(兒子)
							realson = (TextView) cover.getChildAt(0);

							// 推擠到新的位置

							for (int i = 1; i < 26; i++)
								if (target == null)
									pushTo(i, cover_i, cover_j, realson);
								else
									break;

							if (target != null) {
								target.setBackgroundColor(Color.rgb(255, 255,
										255));
								bePushed = true;
							}

						} else {
							cover.setBackgroundColor(Color.rgb(122, 122, 122));
						}

					}

				}
				// 如果沒有覆蓋

				else {

					isleave = true;
					if (bePushed) {

						if (cover != null) {
							bePushed = false;

							cover.setBackgroundColor(Color.rgb(255, 255, 255));

							cover.addView(newText(realson.getText().toString(),
									false));

							Log.i("STAT", "LEAVE:" + cover.toString());

							cover = null;

							target.setBackgroundColor(Color.rgb(222, 222, 222));
							target.removeAllViews();
							target = null;

						}
					} else {
						if (cover != null) {

							if (cover.getChildCount() > 0)
								cover.setBackgroundColor(Color.rgb(255, 255,
										255));
							else
								cover.setBackgroundColor(Color.rgb(222, 222,
										222));

							Log.i("STAT", "LEAVE:" + cover.toString());

							cover = null;
						}

					}

				}
				break;

			case MotionEvent.ACTION_UP:

				if (cover != null) {
					if (CoverZone != null && cover.getChildCount() < 1) {

						cover.addView(newText(((TextView) v).getText()
								.toString(), false));
						cover.setBackgroundColor(Color.rgb(255, 255, 255));
						// container.deleteRx(view2);
						cover = null;

					} else {

						father.addView(newText(((TextView) v).getText()
								.toString(), false));

					}
				} else {

					father.addView(newText(((TextView) v).getText().toString(),
							false));
					father.setBackgroundColor(Color.rgb(255, 255, 255));
				}

				fake.setVisibility(View.GONE);

				break;

			default:
				return false;
			}

			return true;

		}
	};

	// //////////////////////////////////////////////////////////////////////////////////

	// 新字串物件
	private TextView newText(String s, boolean boo) {

		TextView rx = new TextView(this);
		rx.setLayoutParams(new LayoutParams(boxWidth - 20, boxHeight - 20));
		rx.setTextSize(15);
		rx.setText(s);
		rx.setTextColor(Color.BLACK);
		rx.setGravity(Gravity.CENTER);
		rx.setBackgroundColor(Color.rgb(225, 194, 200));

		if (boo)
			rx.setOnTouchListener(touch);
		else
			rx.setOnTouchListener(touch2);

		return rx;

	}

	// 假的移動View
	private TextView fake;

	private void fakeView(String s, float x, float y) {

		if (fake == null) {
			fake = new TextView(this);
			fake.setLayoutParams(new LayoutParams(boxWidth - 20, boxHeight - 20));
			fake.setTextSize(15);
			fake.setText(s);
			fake.setTextColor(Color.BLACK);
			fake.setGravity(Gravity.CENTER);
			fake.setBackgroundColor(Color.rgb(225, 194, 200));
			all.addView(fake);
		}

		fake.setVisibility(View.VISIBLE);
		fake.setX(x);
		fake.setY(y);
	}

	// 螢幕寬度
	private int[] getWindowWidth() {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		((WindowManager) this.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(mDisplayMetrics);

		return new int[] { mDisplayMetrics.widthPixels,
				mDisplayMetrics.heightPixels };
	}

	// 是否覆蓋,覆蓋救回傳一個被覆蓋的View
	private Object[] iscover(float f, float g) {

		for (int i = 0; i < row; i++)
			for (int j = 0; j < column; j++) {
				Rect r = new Rect();
				pushMap[i][j].getHitRect(r);
				if (r.contains((int) f, (int) g)) {
					return new Object[] { pushMap[i][j], i, j };
				}
			}
		return null;
	}

	private LinearLayout target;

	private void pushTo(int index, int i, int j, TextView realson) {

		target = null;

		switch (index) {

		case 1:

			if (j == 0)
				target = null;
			else if (pushMap[i][j - 1].getChildCount() > 0)
				target = null;
			else {
				pushMap[i][j].removeAllViews();
				pushMap[i][j - 1].addView(newText(realson.getText().toString(),
						false));
				target = pushMap[i][j - 1];
			}
			break;

		case 2:

			if (i == 0)
				target = null;
			else if (pushMap[i - 1][j].getChildCount() > 0)
				target = null;
			else {
				pushMap[i][j].removeAllViews();
				pushMap[i - 1][j].addView(newText(realson.getText().toString(),
						false));
				target = pushMap[i - 1][j];
			}
			break;

		case 3:

			if (i == 0 || j == 0)
				target = null;
			else if (pushMap[i - 1][j - 1].getChildCount() > 0)
				target = null;
			else {
				pushMap[i][j].removeAllViews();
				pushMap[i - 1][j - 1].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i - 1][j - 1];
			}
			break;

		case 4:

			if (j == column - 1)
				target = null;
			else if (pushMap[i][j + 1].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i][j + 1].addView(newText(realson.getText().toString(),
						false));
				target = pushMap[i][j + 1];
			}
			break;

		case 5:

			if (i == row - 1)
				target = null;
			else if (pushMap[i + 1][j].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i + 1][j].addView(newText(realson.getText().toString(),
						false));
				target = pushMap[i + 1][j];

			}
			break;

		case 6:
			if (i == row - 1 || j == 0)
				target = null;
			else if (pushMap[i + 1][j - 1].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i + 1][j - 1].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i + 1][j - 1];

			}
			break;

		case 7:
			if (i == row - 1 || j == column - 1)
				target = null;
			else if (pushMap[i + 1][j + 1].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i + 1][j + 1].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i + 1][j + 1];

			}
			break;

		case 8:
			if (i == 0 || j == column - 1)
				target = null;
			else if (pushMap[i - 1][j + 1].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i - 1][j + 1].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i - 1][j + 1];

			}
			break;

		// //////////////////////////////////////////////

		case 9:

			if (j <= 1)
				target = null;
			else if (pushMap[i][j - 2].getChildCount() > 0) {
				target = null;
			} else {

				pushMap[i][j].removeAllViews();
				pushMap[i][j - 2].addView(newText(realson.getText().toString(),
						false));
				target = pushMap[i][j - 2];
			}
			break;

		case 10:

			if (j <= 1 || i <= 0)
				target = null;
			else if (pushMap[i - 1][j - 2].getChildCount() > 0) {
				target = null;
			} else {

				pushMap[i][j].removeAllViews();
				pushMap[i - 1][j - 2].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i - 1][j - 2];
			}
			break;

		case 11:

			if (i <= 1)
				target = null;
			else if (pushMap[i - 2][j].getChildCount() > 0)
				target = null;
			else {
				pushMap[i][j].removeAllViews();
				pushMap[i - 2][j].addView(newText(realson.getText().toString(),
						false));
				target = pushMap[i - 2][j];
			}
			break;

		case 12:

			if (i <= 1 || j >= column - 1)
				target = null;
			else if (pushMap[i - 2][j + 1].getChildCount() > 0)
				target = null;
			else {
				pushMap[i][j].removeAllViews();
				pushMap[i - 2][j + 1].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i - 2][j + 1];
			}
			break;

		case 13:

			if (i <= 1 || j <= 1)
				target = null;
			else if (pushMap[i - 2][j - 2].getChildCount() > 0)
				target = null;
			else {
				pushMap[i][j].removeAllViews();
				pushMap[i - 2][j - 2].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i - 2][j - 2];
			}
			break;

		case 14:

			if (i <= 1 || j <= 0)
				target = null;
			else if (pushMap[i - 2][j - 1].getChildCount() > 0)
				target = null;
			else {
				pushMap[i][j].removeAllViews();
				pushMap[i - 2][j - 1].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i - 2][j - 1];
			}
			break;

		case 15:

			if (j >= column - 2)
				target = null;
			else if (pushMap[i][j + 2].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i][j + 2].addView(newText(realson.getText().toString(),
						false));
				target = pushMap[i][j + 2];
			}
			break;

		case 16:

			if (i <= 0 || j >= column - 2)
				target = null;
			else if (pushMap[i - 1][j + 2].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i - 1][j + 2].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i - 1][j + 2];
			}
			break;

		case 17:

			if (i >= row - 2)
				target = null;
			else if (pushMap[i + 2][j].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i + 2][j].addView(newText(realson.getText().toString(),
						false));
				target = pushMap[i + 2][j];

			}
			break;

		case 18:

			if (i >= row - 2 || j >= column - 1)
				target = null;
			else if (pushMap[i + 2][j + 1].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i + 2][j + 1].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i + 2][j + 1];

			}
			break;

		// 左下
		case 19:
			if (i >= row - 2 || j <= 1)
				target = null;
			else if (pushMap[i + 2][j - 2].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i + 2][j - 2].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i + 2][j - 2];

			}
			break;

		// 左下 右
		case 20:
			if (i >= row - 1 || j <= 1)
				target = null;
			else if (pushMap[i + 1][j - 2].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i + 1][j - 2].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i + 1][j - 2];

			}
			break;

		// 右下

		case 21:
			if (i >= row - 2 || j >= column - 2)
				target = null;
			else if (pushMap[i + 2][j + 2].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i + 2][j + 2].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i + 2][j + 2];

			}
			break;

		// 右下 上

		case 22:
			if (i >= row - 1 || j >= column - 2)
				target = null;
			else if (pushMap[i + 1][j + 2].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i + 1][j + 2].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i + 1][j + 2];

			}
			break;

		// 右上

		case 23:
			if (i <= 1 || j >= column - 2)
				target = null;
			else if (pushMap[i - 2][j + 2].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i - 2][j + 2].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i - 2][j + 2];

			}
			break;

		// 右上 左
		case 24:
			if (i <= 1 || j >= column - 1)
				target = null;
			else if (pushMap[i - 2][j + 1].getChildCount() > 0)
				target = null;
			else {

				pushMap[i][j].removeAllViews();
				pushMap[i - 2][j + 1].addView(newText(realson.getText()
						.toString(), false));
				target = pushMap[i - 2][j + 1];

			}
			break;

		case 25:

			int hi = -1,
			hj = -1;

			for (int ii = 0; ii < row; ii++) {
				for (int jj = 0; jj < column; jj++)
					if (pushMap[ii][jj].getChildCount() == 0) {
						hi = ii;
						hj = jj;
						break;
					}
			}

			if (hi != -1 && hj != -1) {

				target = pushMap[hi][hj];
				pushMap[i][j].removeAllViews();
				pushMap[hi][hj].addView(newText(realson.getText().toString(),
						false));

			} else {
				target = null;
			}

			break;
		}
	}
}

@SuppressWarnings("deprecation")
class MyLinerLayout {

	public float locX[];
	public float locY[];
	private AbsoluteLayout father;
	public ArrayList<TextView> member;

	public MyLinerLayout(AbsoluteLayout father, ArrayList<TextView> member,
			int top, int windowWidth, int boxWidth, int boxHeight, int GapSize) {

		this.member = member;
		this.father = father;

		locX = new float[member.size()];
		locY = new float[member.size()];

		int columnNum = windowWidth / (boxWidth + GapSize);

		int Margin = (windowWidth - (columnNum * boxWidth + (columnNum - 1)
				* GapSize)) / 2;

		int idx = 0;
		int row = 0;
		for (int i = 0; i < member.size(); i++) {

			if (idx >= columnNum) {

				idx = 0;
				row += 1;
			}

			locX[i] = Margin + ((idx + 1) * GapSize + idx * boxWidth);
			locY[i] = top + ((row + 1) * GapSize + row * boxHeight);
			idx += 1;
			member.get(i).setX(locX[i]);
			member.get(i).setY(locY[i]);
			father.addView(member.get(i));

		}
	}

	private void refreshLoc() {

		for (int i = 0; i < member.size(); i++) {
			member.get(i).setX(locX[i]);
			member.get(i).setY(locY[i]);
		}

	}

	public void addRx(TextView t) {
		member.add(t);

		int i = member.size() - 1;

		t.setX(locX[i]);
		t.setY(locY[i]);
		father.addView(t);

	}

	public void deleteRx(TextView t) {
		member.remove(t);
		father.removeView(t);
		refreshLoc();
	}

}
