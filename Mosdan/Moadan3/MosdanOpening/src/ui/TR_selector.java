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
	{ "������", "�����ݸs��" };
	private String[] m_group =
	{ "�޲z����", "�R���s��" };
	private String[] m_tr =
	{ "���J�s�u", "�R��" };

	private boolean[] ischeck;

	public TR_selector(Context c)
	{
		super(c);
		this.c = c;
	}

	// �]�w�����Ѽ�(mode=0 ���,��l �h��)
	/**
	 * @title ��ܮؼ��D
	 * @mode ���0 �h��1
	 * @data ��ܪ����
	 * @control =0 ���Tx��(���)
	 * @control =1 ���Rx�ݬO��@�٬O�s��(���)
	 * @control =2 ��ܳ�@Rx��(���)
	 * @control =3 ��ܳ�@�s��(���)
	 * @control =4 ��ܺ޲z�����ΧR���s�ոs��(���)
	 * @control =5 ��ܺ޲z�s�զ���(�h��),�����h�[ g_name �Ѽ�
	 * @control =6 ��ܸ��JTR�s�u�άO�R���s�u(���)
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

		// �]�w�U����s,�O�_���T�w
		if (control == 5)
			setPositiveButton("�T�w", Done_multiListener);

		setNegativeButton("����", leave);
	}

	// �إ߲M����
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
			// �w������ Rx�s�� ������
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

	// �h��ܵ��G
	private OnMultiChoiceClickListener multilistener = new OnMultiChoiceClickListener()
	{
		public void onClick(DialogInterface dialog, int which, boolean isChecked)
		{
			ischeck[which] = isChecked;
		}
	};

	// ��ܳ��Item����ť��
	private OnClickListener Select_item_listener = new OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			// ����,���N�������}�äϬM
			switch (control)
			{

			case 0:
				Data.Gconn_tx_index = which;
				// ��s
				Gconn_main.basicset();
				dialog.dismiss();
				break;

			// �߰� Rx��� OR �s��
			case 1:
				if (which == 0)
				{
					TR_selector D_single_rx = new TR_selector(c);
					D_single_rx.setDetail("��ܱ�����", 2, null);
					D_single_rx.show();
					dialog.dismiss();
					break;
				} else
				{
					TR_selector D_single_g = new TR_selector(c);
					D_single_g.setDetail("��ܱ����ݸs��", 3, null);
					D_single_g.show();
					dialog.dismiss();
					break;
				}

				// �߰ݭ��ӳ�@Rx��
			case 2:
				Data.Gconn_rx_index = which;
				Data.Gconn_group_index = -1;
				// ��s
				Gconn_main.basicset();
				dialog.dismiss();
				break;

			// �߰ݸ��JTR�s�u�]�w�ΧR��
			case 6:
				do_group_things(dialog, which);
				dialog.dismiss();
				break;

			// �߰ݭ���Rx�s��
			case 3:
				Data.Gconn_group_index = which;
				Data.Gconn_rx_index = -1;
				// ��s
				Gconn_main.basicset();
				dialog.dismiss();
				break;

			// �߰ݽs��s�զ��� OR �R���s��
			case 4:
				if (which == 0)
				{

					TR_selector edit_mem = new TR_selector(c);
					edit_mem.setDetail("�޲z�s�զ���", 5, g_name);
					edit_mem.show();
					// ��s
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

		// �N���Ҹ��J�� Gconn_main �����e��
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

			// �R�� TR �s�u����

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

	// �޲zRx�s�զ������h���ť��(�]�w�s�� �P �M��Rx���ݸs��)
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
									// ��s����
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
								// ��s����
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
									// ��s����
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
	// ���}��ܮ�
	private OnClickListener leave = new OnClickListener()
	{
		public void onClick(DialogInterface dialog, int which)
		{
			dialog.dismiss();
		}

	};

}
