package ui;

import Data.Data;
import Internet.Turbo_View;
import activity.Gconn_main;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.widget.ArrayAdapter;

public class TR_selector extends AlertDialog.Builder
{
	private Context c;
	private int control;
	private ArrayAdapter<String> adp;
	private String g_name;
	private String[] s_or_m =
	{ "接收端", "接收端群組" };
	private String[] m_group =
	{ "管理成員", "刪除群組" };
	private String[] m_tr =
	{ "載入連線", "刪除" };

	private boolean[] ischeck;

	public TR_selector(Context c)
	{
		super(c);
		this.c = c;
	}

	// 設定相關參數(mode=0 單選,其餘 多選)
	/**
	 * @title 對話框標題
	 * @mode 單選0 多選1
	 * @data 顯示的資料
	 * @control =0 選擇Tx端(單選)
	 * @control =1 選擇Rx端是單一還是群組(單選)
	 * @control =2 選擇單一Rx端(單選)
	 * @control =3 選擇單一群組(單選)
	 * @control =4 選擇管理成員或刪除群組群組(單選)
	 * @control =5 選擇管理群組成員(多選),必須多加 g_name 參數
	 * @control =6 選擇載入TR連線或是刪除連線(單選)
	 * */
	public void setDetail(String title, int control, String g_name)
	{

		setTitle(title);
		this.control = control;
		this.g_name = g_name;
		bulidAdapter();

		if (control != 5)
			setAdapter(adp, Select_item_listener);
		else
			setMultiChoiceItems(Data.Rx_name, ischeck, multilistener);

		// 設定下方按鈕,是否有確定
		if (control == 5)
			setPositiveButton("確定", Done_multiListener);

		setNegativeButton("取消", leave);
	}

	// 建立清單資料
	private void bulidAdapter()
	{
		adp = new ArrayAdapter<String>(c, (control == 5) ? android.R.layout.select_dialog_multichoice : android.R.layout.select_dialog_singlechoice);

		switch (control)
		{

		case 0:
			for (String tx : Data.Tx_name)
				adp.add(tx);
			break;

		case 1:
			for (String s : s_or_m)
				adp.add(s);
			break;

		case 2:
			for (String rx : Data.Rx_name)
				adp.add(rx);
			break;

		case 3:
			for (String g : Data.get_Rx_Group_name())
				adp.add(g);
			break;

		case 4:
			for (String s : m_group)
				adp.add(s);
			break;

		case 5:
			// 預先打勾 Rx群組 的成員
			if (Data.Rx_group != null)
			{
				ischeck = new boolean[Data.Rx_group.length];
				for (int i = 0; i < Data.Rx_group.length; i++)
				{
					if (Data.Rx_group[i] != null)
						if (Data.Rx_group[i].equals(g_name))
							ischeck[i] = true;
						else
							ischeck[i] = false;
				}
			}
			break;

		case 6:
			for (String s : m_tr)
				adp.add(s);
			break;
		}

	}

	// 多選擇結果
	private OnMultiChoiceClickListener multilistener = new OnMultiChoiceClickListener()
	{
		public void onClick(DialogInterface dialog, int which, boolean isChecked)
		{
			ischeck[which] = isChecked;
		}
	};

	// 選擇單選Item的傾聽器
	private OnClickListener Select_item_listener = new OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			// 單選時,選到就直接離開並反映
			switch (control)
			{

			case 0:
				Data.Gconn_tx_index = which;
				// 刷新
				Gconn_main.basicset();
				dialog.dismiss();
				break;

			// 詢問 Rx單選 OR 群組
			case 1:
				if (which == 0)
				{
					TR_selector D_single_rx = new TR_selector(c);
					D_single_rx.setDetail("選擇接收端", 2, null);
					D_single_rx.show();
					dialog.dismiss();
					break;
				} else
				{
					TR_selector D_single_g = new TR_selector(c);
					D_single_g.setDetail("選擇接收端群組", 3, null);
					D_single_g.show();
					dialog.dismiss();
					break;
				}

				// 詢問哪個單一Rx端
			case 2:
				Data.Gconn_rx_index = which;
				Data.Gconn_group_index = -1;
				// 刷新
				Gconn_main.basicset();
				dialog.dismiss();
				break;

			// 詢問載入TR連線設定或刪除
			case 6:
				do_group_things(dialog, which);
				dialog.dismiss();
				break;

			// 詢問哪個Rx群組
			case 3:
				Data.Gconn_group_index = which;
				Data.Gconn_rx_index = -1;
				// 刷新
				Gconn_main.basicset();
				dialog.dismiss();
				break;

			// 詢問編輯群組成員 OR 刪除群組
			case 4:
				if (which == 0)
				{

					TR_selector edit_mem = new TR_selector(c);
					edit_mem.setDetail("管理群組成員", 5, g_name);
					edit_mem.show();
					// 刷新
					Gconn_main.basicset();
					dialog.dismiss();
					break;
				} else
				{

					Mycommand sr = new Mycommand()
					{

						@Override
						public void command()
						{
							Turbo_View tv = new Turbo_View();
							tv.Group_clear(g_name);

							Gconn_main.gconn_Activity.runOnUiThread(new Runnable()
							{

								@Override
								public void run()
								{
									Data.delete_group(g_name);
									Gconn_main.Gconn_GroupAdapter.ref();

								}
							});

						}
					};
					sr.start();
					dialog.dismiss();
				}
				break;

			}
		}

	};


	private void do_group_things(DialogInterface dialog, int which)
	{

		// 將情境載入到 Gconn_main 中間畫面
		if (which == 0)
		{

			int tx_idx = -1;
			int rx_idx = -1;

			for (int i = 0; i < Data.Tx_mac.length; i++)
			{
				if (Data.Tx_mac[i].equals(Data.Gconn_tx[Data.Gconn_LongClick_index]))
				{
					tx_idx = i;
					break;
				}
			}

			for (int i = 0; i < Data.Rx_mac.length; i++)
			{
				if (Data.Rx_mac[i].equals(Data.Gconn_rx[Data.Gconn_LongClick_index]))
				{
					rx_idx = i;
					break;
				}
			}

			Data.Gconn_rx_index = rx_idx;
			Data.Gconn_tx_index = tx_idx;
			Data.Gconn_group_index = -1;
			Gconn_main.basicset();

			Gconn_main.viewFlow.setSelection(1);
			Gconn_main.viewFlow.setSelection(1);
			Gconn_main.viewFlow.setSelection(1);

		} else
		{

			// 刪除 TR 連線情境

			Mycommand sr = new Mycommand()
			{

				@Override
				public void command()
				{

					Turbo_View tv = new Turbo_View();
					tv.RxSituation_delete(Data.Gconn_name[Data.Gconn_LongClick_index], "all");
					Gconn_main.gconn_Activity.runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							Data.Gconn_delete(Data.Gconn_LongClick_index);
							Gconn_main.Gconn_Adapter.ref();
						}

					});

				}
			};
			sr.start();
		}
	}

	// 管理Rx群組成員的多選傾聽器(設定群組 與 清除Rx所屬群組)
	private OnClickListener Done_multiListener = new OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{

			for (int i = 0; i < Data.Rx_group.length; i++)
			{

				final String mac = Data.Rx_mac[i];

				if (ischeck[i])
				{
					if (Data.Rx_group[i] != null)
					{
						if (!Data.Rx_group[i].equals(g_name))
						{
							Data.Rx_group[i] = g_name;
							Mycommand sr = new Mycommand()
							{
								@Override
								public void command()
								{
									Turbo_View tv = new Turbo_View();
									tv.Group_set(mac, g_name);
									// 刷新介面
									Gconn_main.gconn_Activity.runOnUiThread(new Runnable()
									{
										@Override
										public void run()
										{
											Gconn_main.Gconn_GroupAdapter.ref();
										}
									});
								}
							};
							sr.start();
						}
					} else
					{
						Data.Rx_group[i] = g_name;
						Mycommand sr = new Mycommand()
						{
							@Override
							public void command()
							{
								Turbo_View tv = new Turbo_View();
								tv.Group_set(mac, g_name);
								// 刷新介面
								Gconn_main.gconn_Activity.runOnUiThread(new Runnable()
								{
									@Override
									public void run()
									{
										Gconn_main.Gconn_GroupAdapter.ref();
									}
								});
							}
						};
						sr.start();

					}

				} else
				{
					if (Data.Rx_group[i] != null)
					{
						if (Data.Rx_group[i].equals(g_name))
						{
							Data.Rx_group[i] = null;

							Mycommand srct = new Mycommand()
							{
								@Override
								public void command()
								{
									Turbo_View tv = new Turbo_View();
									tv.Group_set(mac, "_clear_");
									// 刷新介面
									Gconn_main.gconn_Activity.runOnUiThread(new Runnable()
									{
										@Override
										public void run()
										{
											Gconn_main.Gconn_GroupAdapter.ref();
										}
									});
								}
							};
							srct.start();
						}
					}
				}
			}

			dialog.dismiss();
		}
	};
	// 離開對話框
	private OnClickListener leave = new OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			dialog.dismiss();
		}

	};

}
