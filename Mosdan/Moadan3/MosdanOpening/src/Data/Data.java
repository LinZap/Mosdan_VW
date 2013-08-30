package Data;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;
import Internet.SQL;
import TV.Mosdan2.R;

// data storage

public class Data {

	// system command lock
	private static boolean lock = false;
	// system auto to get data
	public static boolean isauto_getData = false;

	// only for a thread used
	public static synchronized boolean islock() {
		return lock;
	}

	// only for a thread set
	public static synchronized void setlock(boolean boo) {
		lock = boo;
	}

	public static String[] Gconn_title = { "儲存的連線", "一般連線", "接收端群組" };
	public static String[] VW_title = { "電視牆情境", "已儲存" };

	// system server path
	public static String system_server = "169.254.2.2";

	// public clock to dalay command
	// public static Timer publicTimer;
	// system first loading
	public static boolean isloading = false;
	// system control
	public static int view_display = -1;
	// user's history
	public static final int view_Home = 0;
	public static final int view_Rx = 1;
	public static final int view_Tx = 2;

	// listView index
	public static int index_situation = 0;
	public static int index_rx = 0;
	public static int index_tx = 0;

	// expandablelistview
	public static int groupSelected = 0;
	public static int childSelected = 0;

	// change situation
	// VW_name(目前所載入的情境群組名稱)
	public static String vw_name = "";
	// Tx data
	public static String[][] Tx_data;
	public static String[] Tx_name = { "T1", "T2" };
	public static Integer[] Tx_status = { R.drawable.not_connection,
			R.drawable.not_connection };
	public static String[] Tx_mode = { "Video", "Video" };
	public static String[] Tx_ati_mode = { "OFF", "OFF" };
	public static String[] Tx_explain = { "yyy", "GGF" };
	public static String[] Tx_mac = { "x15185185", "541514" };
	public static String[] Tx_ip = { "d515151", "d516147147" };
	// Rx data
	public static String[][] Rx_data;
	public static String[] Rx_mac = { "d515151", "d516147147" };
	public static String[] Rx_ip = { "d515151", "d516147147" };
	public static Integer[] Rx_status = { R.drawable.not_connection,
			R.drawable.not_connection };
	public static String[] Rx_tx = { "d515151", "d516147147" };
	public static String[] Rx_name = { "sssssssssssssssssssssssssssssssssss",
			"yyyyyyyyyyyyyyyyyyyyyyyyyy" };
	public static String[] Rx_explain = { "dddddd", "expladsvsd" };
	public static String[] Rx_sitsution = { "1", "1" };
	public static String[] Rx_group = { "qqq", "www" };
	public static boolean[] Rx_sit_isChanged = { false, false };
	public static Integer[] Rx_locX = { 1, 1 };
	public static Integer[] Rx_locY = { 1, 1 };
	public static Integer[] Rx_tuneX = { 0, 0 };
	public static Integer[] Rx_tuneY = { 0, 0 };
	public static Integer[] Rx_scaleX = { 0, 0 };
	public static Integer[] Rx_scaleY = { 0, 0 };
	public static Integer[] Rx_tdelay = { 0, 0 };
	// Situation Data
	// 30組
	public static String[][] Situation_data;
	public static String[] Situation_name = { "1", "2", "3", "4" };
	// 來源
	public static String[] Situation_srctx = { "x15185185", "541514", "None",
			"None" };
	public static Integer[] Situation_bulidx = { 1, 1, 1, 1 };
	public static Integer[] Situation_bulidy = { 1, 1, 1, 1 };
	public static Integer[] Situation_multihost = { 0, 0, 0, 0 };
	public static Integer[] Situation_status = { R.drawable.not_connection,
			R.drawable.not_connection, R.drawable.not_connection,
			R.drawable.not_connection };

	public static int Gconn_rx_index = -1;
	public static int Gconn_tx_index = -1;
	public static int Gconn_group_index = -1;
	public static int Gconn_LongClick_index = -1;

	// Gconn Data (尚未實作搜尋實際DB)
	public static String[][] Gconn_data;
	public static String[] Gconn_name = { "www", "qqq", "qwd", "qdwqwd" };
	public static String[] Gconn_tx = { "???", "541514", "x15185185", "541514" };
	public static String[] Gconn_rx = { "???", "d516147147", "d515151",
			"d516147147" };

	// VW
	public static String[][] VW_data = {
			{ "br%26%26%26Ggfddff", "1", "001703A0007F", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "2", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "3", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "4", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "5", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "6", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "7", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "8", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "9", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "10", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "11", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "12", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "13", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "14", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "15", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "16", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "17", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "18", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "19", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "20", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "21", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "22", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "23", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "24", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "25", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "26", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "27", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "28", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "29", "None", "1", "1", "0", },
			{ "br%26%26%26Ggfddff", "30", "None", "1", "1", "0", },
			{ "defaultmosdan", "1", "None", "1", "1", "0", },
			{ "defaultmosdan", "2", "None", "1", "1", "0", },
			{ "defaultmosdan", "3", "None", "1", "1", "0", },
			{ "defaultmosdan", "4", "None", "1", "1", "0", },
			{ "defaultmosdan", "5", "None", "1", "1", "0", },
			{ "defaultmosdan", "6", "None", "1", "1", "0", },
			{ "defaultmosdan", "7", "None", "1", "1", "0", },
			{ "defaultmosdan", "8", "None", "1", "1", "0", },
			{ "defaultmosdan", "9", "None", "1", "1", "0", },
			{ "defaultmosdan", "10", "None", "1", "1", "0", },
			{ "defaultmosdan", "11", "None", "1", "1", "0", },
			{ "defaultmosdan", "12", "None", "1", "1", "0", },
			{ "defaultmosdan", "13", "None", "1", "1", "0", },
			{ "defaultmosdan", "14", "None", "1", "1", "0", },
			{ "defaultmosdan", "15", "None", "1", "1", "0", },
			{ "defaultmosdan", "16", "None", "1", "1", "0", },
			{ "defaultmosdan", "17", "None", "1", "1", "0", },
			{ "defaultmosdan", "18", "None", "1", "1", "0", },
			{ "defaultmosdan", "19", "None", "1", "1", "0", },
			{ "defaultmosdan", "20", "None", "1", "1", "0", },
			{ "defaultmosdan", "21", "None", "1", "1", "0", },
			{ "defaultmosdan", "22", "None", "1", "1", "0", },
			{ "defaultmosdan", "23", "None", "1", "1", "0", },
			{ "defaultmosdan", "24", "None", "1", "1", "0", },
			{ "defaultmosdan", "25", "None", "1", "1", "0", },
			{ "defaultmosdan", "26", "None", "1", "1", "0", },
			{ "defaultmosdan", "27", "None", "1", "1", "0", },
			{ "defaultmosdan", "28", "None", "1", "1", "0", },
			{ "defaultmosdan", "29", "None", "1", "1", "0", },
			{ "defaultmosdan", "30", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "1", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "2", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "3", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "4", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "5", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "6", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "7", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "8", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "9", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "10", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "11", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "12", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "13", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "14", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "15", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "16", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "17", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "18", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "19", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "20", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "21", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "22", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "23", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "24", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "25", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "26", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "27", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "28", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "29", "None", "1", "1", "0", },
			{ "vw%26%26%26hello", "30", "None", "1", "1", "0", }, };
	public static Integer[] VW_pic;

	/**
	 * 取得廣播或電視牆情境名稱
	 * 
	 * @mode=0 vw
	 * @mode=1 br
	 * */
	public static String[] getVW_BR_name(int mode) {
		if (VW_data != null) {

			ArrayList<String> name = new ArrayList<String>();

			for (int i = 0; i < VW_data.length; i++) {

				if (!VW_data[i][0].equals("defaultmosdan")) {

					String type = (mode == 0) ? "vw" : "br";

					String s[] = VW_data[i][0].split("%26%26%26");
					if (s[0].equals(type)) {
						if (!name.contains(s[1]))
							name.add(s[1]);
					}

				}

			}
			VW_pic = new Integer[name.size()];
			for (int j = 0; j < VW_pic.length; j++)
				VW_pic[j] = R.drawable.vw_icon;

			return name.toArray(new String[name.size()]);
		}
		return new String[0];
	}

	// 儲存TR情境
	public static void Gconn_save(String iname, String itx, String irx) {
		int length = (Gconn_name == null) ? 0 : Gconn_name.length;
		String[] name = new String[length + 1];
		String[] tx = new String[length + 1];
		String[] rx = new String[length + 1];

		for (int i = 0; i < length; i++) {
			name[i] = Gconn_name[i];
			tx[i] = Gconn_tx[i];
			rx[i] = Gconn_rx[i];
		}
		name[length] = iname;
		tx[length] = itx;
		rx[length] = irx;

		Gconn_name = name;
		Gconn_tx = tx;
		Gconn_rx = rx;
	}

	// 刪除TR情境
	public static void Gconn_delete(int idx) {
		String[] name = new String[Gconn_name.length - 1];
		String[] tx = new String[Gconn_name.length - 1];
		String[] rx = new String[Gconn_name.length - 1];
		int j = 0;
		for (int i = 0; i < Gconn_name.length; i++) {
			if (i != idx) {
				name[j] = Gconn_name[i];
				tx[j] = Gconn_tx[i];
				rx[j] = Gconn_rx[i];
				j += 1;
			}
		}
		Gconn_name = name;
		Gconn_tx = tx;
		Gconn_rx = rx;
	}

	// 搜尋已儲存的TR連線情境
	public static boolean getGconnData() {
		SQL s = new SQL();
		Gconn_data = s.getResult("select * from situation_tr");
		if (Gconn_data == null) {
			return false;
		} else if (Gconn_data[0][0].equals("404")) {
			Gconn_null();
			return true;
		} else {
			parseGconnData();
			return true;
		}
	}

	public static boolean getTxData() {
		SQL s = new SQL();
		Tx_data = s.getResult("select * from transmitter");
		if (Tx_data == null) {
			return false;
		} else if (Tx_data[0][0].equals("404")) {
			Tx_null();
			return true;
		} else {
			parseTxData();
			return true;
		}
	}

	public static boolean getRxData() {
		SQL s = new SQL();
		Rx_data = s.getResult("select * from receiver");
		if (Rx_data == null) {
			return false;
		} else if (Rx_data[0][0].equals("404")) {
			Rx_null();
			return true;
		} else {
			parseRxData();
			return true;
		}
	}

	public static boolean getSituationData() {
		SQL s = new SQL();
		Situation_data = s.getResult("select * from vw_group");
		if (Situation_data == null) {
			return false;
		} else if (Situation_data[0][0].equals("404")) {
			Situation_null();
			return true;
		} else {
			parseSituationData();
			return true;
		}
	}

	// 取得以儲存的 電視牆 情境 列表
	public static boolean getVWData() {

		SQL s = new SQL();
		VW_data = s.getResult("select * from vw_sit_group");
		if (VW_data == null) {
			return false;
		} else if (VW_data[0][0].equals("404")) {
			VW_null();
			return true;
		}

		return true;
	}

	// 解析 TR 連線情境的資料
	private static void parseGconnData() {
		int len = Gconn_data.length;

		Gconn_rx = new String[len];
		Gconn_tx = new String[len];
		Gconn_name = new String[len];

		for (int i = 0; i < len; i++) {
			Gconn_name[i] = Data.Gconn_data[i][0];
			Gconn_rx[i] = Data.Gconn_data[i][1];
			Gconn_tx[i] = Data.Gconn_data[i][2];
		}
	}

	// 解析Tx資料
	private static void parseTxData() {

		int len = Tx_data.length;
		Tx_ati_mode = new String[len];
		Tx_explain = new String[len];
		Tx_ip = new String[len];
		Tx_mac = new String[len];
		Tx_mode = new String[len];
		Tx_name = new String[len];
		Tx_status = new Integer[len];

		for (int i = 0; i < len; i++) {

			Data.Tx_mac[i] = Data.Tx_data[i][0];
			Data.Tx_ip[i] = Data.Tx_data[i][1];

			if (Data.Tx_data[i][2].equals("s_idle"))
				Data.Tx_status[i] = R.drawable.attach;
			else if (Data.Tx_data[i][2].equals("s_loss"))
				Data.Tx_status[i] = R.drawable.not_connection;
			else
				Data.Tx_status[i] = R.drawable.connection;

			Data.Tx_mode[i] = Data.Tx_data[i][4];
			Data.Tx_ati_mode[i] = Data.Tx_data[i][5];
			Data.Tx_name[i] = (Data.Tx_data[i][7].equals(" ")) ? Data.Tx_data[i][0]
					: Data.Tx_data[i][7];
			Data.Tx_explain[i] = Data.Tx_data[i][8];

		}

	}

	// 解析Rx資料
	private static void parseRxData() {

		int row_num = Data.Rx_data.length;
		Data.Rx_explain = new String[row_num];
		Data.Rx_ip = new String[row_num];
		Data.Rx_locX = new Integer[row_num];
		Data.Rx_locY = new Integer[row_num];
		Data.Rx_mac = new String[row_num];
		Data.Rx_name = new String[row_num];
		Data.Rx_sitsution = new String[row_num];
		Data.Rx_status = new Integer[row_num];
		Data.Rx_sit_isChanged = new boolean[row_num];
		Data.Rx_scaleX = new Integer[row_num];
		Data.Rx_scaleY = new Integer[row_num];
		Data.Rx_tuneX = new Integer[row_num];
		Data.Rx_tuneY = new Integer[row_num];
		Data.Rx_tdelay = new Integer[row_num];
		Data.Rx_tx = new String[row_num];
		Data.Rx_group = new String[row_num];

		for (int i = 0; i < row_num; i++) {
			Data.Rx_mac[i] = Data.Rx_data[i][0];
			Data.Rx_ip[i] = Data.Rx_data[i][1];

			Log.i("RX_IP" + i, Data.Rx_data[i][1]);

			if (Data.Rx_data[i][2].equals("s_srv_on"))
				Data.Rx_status[i] = R.drawable.connection;
			else if (Data.Rx_data[i][2].equals("s_loss"))
				Data.Rx_status[i] = R.drawable.not_connection;
			else
				Data.Rx_status[i] = R.drawable.attach;

			Data.Rx_tx[i] = Data.Rx_data[i][4].equals(" ") ? ""
					: Data.Rx_data[i][4];
			;
			Data.Rx_name[i] = Data.Rx_data[i][5].equals(" ") ? Data.Rx_data[i][0]
					: Data.Rx_data[i][5];
			Data.Rx_explain[i] = Data.Rx_data[i][6].equals(" ") ? ""
					: Data.Rx_data[i][6];
			Data.Rx_group[i] = Data.Rx_data[i][8].equals(" ") ? null
					: Data.Rx_data[i][8];
			Data.Rx_sitsution[i] = Data.Rx_data[i][10].equals(" ") ? ""
					: Data.Rx_data[i][10];
			Data.Rx_locX[i] = Integer.valueOf(Data.Rx_data[i][11]);
			Data.Rx_locY[i] = Integer.valueOf(Data.Rx_data[i][12]);
			Data.Rx_tuneX[i] = Integer.parseInt(Data.Rx_data[i][13]);
			Data.Rx_tuneY[i] = Integer.parseInt(Data.Rx_data[i][14]);
			Data.Rx_scaleX[i] = Integer.parseInt(Data.Rx_data[i][15]);
			Data.Rx_scaleY[i] = Integer.parseInt(Data.Rx_data[i][16]);
			Data.Rx_tdelay[i] = Integer.parseInt(Data.Rx_data[i][17]);
			Data.Rx_sit_isChanged[i] = false;
		}

	}

	// 解析30個情境資料
	private static void parseSituationData() {

		int row_num = Data.Situation_data.length;
		Data.Situation_name = new String[row_num];
		Data.Situation_srctx = new String[row_num];
		Data.Situation_bulidx = new Integer[row_num];
		Data.Situation_bulidy = new Integer[row_num];
		Data.Situation_multihost = new Integer[row_num];
		Data.Situation_status = new Integer[row_num];
		for (int i = 0; i < row_num; i++) {
			Data.Situation_name[i] = Data.Situation_data[i][0];
			Data.Situation_srctx[i] = Data.Situation_data[i][1];
			Data.Situation_bulidx[i] = Integer
					.parseInt(Data.Situation_data[i][2]);
			Data.Situation_bulidy[i] = Integer
					.parseInt(Data.Situation_data[i][3]);
			Data.Situation_multihost[i] = Integer
					.parseInt(Data.Situation_data[i][4]);
			Data.Situation_status[i] = R.drawable.setblue;
		}

	}

	private static void Gconn_null() {
		Gconn_rx = new String[0];
		Gconn_tx = new String[0];
		Gconn_name = new String[0];
	}

	private static void VW_null() {
		VW_data = new String[0][0];
	}

	private static void Tx_null() {
		Tx_ati_mode = new String[0];
		Tx_explain = new String[0];
		Tx_ip = new String[0];
		Tx_mac = new String[0];
		Tx_mode = new String[0];
		Tx_name = new String[0];
		Tx_status = new Integer[0];
	}

	private static void Rx_null() {
		Rx_explain = new String[0];
		Rx_ip = new String[0];
		Rx_locX = new Integer[0];
		Rx_locY = new Integer[0];
		Rx_mac = new String[0];
		Rx_name = new String[0];
		Rx_sitsution = new String[0];
		Rx_status = new Integer[0];
		Rx_sit_isChanged = new boolean[0];
		Rx_scaleX = new Integer[0];
		Rx_scaleY = new Integer[0];
		Rx_tuneX = new Integer[0];
		Rx_tuneY = new Integer[0];
		Rx_tdelay = new Integer[0];
		Rx_tx = new String[0];
		Rx_group = new String[0];
	}

	private static void Situation_null() {
		Data.Situation_name = new String[0];
		Data.Situation_srctx = new String[0];
		Data.Situation_bulidx = new Integer[0];
		Data.Situation_bulidy = new Integer[0];
		Data.Situation_multihost = new Integer[0];
		Data.Situation_status = new Integer[0];
	}

	// 解析群組列表(呼叫Ref會自動呼叫)
	public static String[] get_Rx_Group_name() {
		if (Rx_group != null) {
			ArrayList<String> gruop_name = new ArrayList<String>();
			for (int i = 0; i < Rx_group.length; i++)
				if (Rx_group[i] != null)
					if (!gruop_name.contains(Rx_group[i]))
						gruop_name.add(Rx_group[i]);

			return gruop_name.toArray(new String[gruop_name.size()]);
		}
		return null;
	}

	// 輸入群組名稱,取得其所有成員索引陣列
	public static Integer[] get_Rx_Group_member(String Group_name) {

		if (Rx_group != null) {
			ArrayList<Integer> gruop_member = new ArrayList<Integer>();
			for (int i = 0; i < Rx_group.length; i++)
				if (Rx_group[i] != null)
					if (Rx_group[i].equals(Group_name))
						gruop_member.add(i);
			if (gruop_member.size() > 0)
				return gruop_member.toArray(new Integer[gruop_member.size()]);
		}

		return null;
	}

	// 刪除群組
	public static void delete_group(String s) {
		for (int i = 0; i < Rx_group.length; i++)
			if (Rx_group[i].equals(s))
				Rx_group[i] = null;

	}

	// 組成情境成員的索引陣列 回傳
	private static Integer[] group_rx_idx;

	private static void parseSituation_Member(String situation_name) {

		if (Rx_sitsution.length > 0) {
			ArrayList<Integer> idxArrayList = new ArrayList<Integer>();
			for (int i = 0; i < Rx_sitsution.length; i++) {
				if (Rx_sitsution[i].equals(situation_name)) {
					idxArrayList.add(i);
				}
			}
			group_rx_idx = idxArrayList
					.toArray(new Integer[idxArrayList.size()]);
		} else {
			group_rx_idx = new Integer[0];
		}
	}

	// 取得解析過後的成員
	public static Integer[] get_Situation_Member_idx(String situation_name) {
		parseSituation_Member(situation_name);
		return group_rx_idx;
	}

	// 自動刷新資料
	public static void auto_refreah_data() {
		
		
		new Thread() {
			@Override
			public void run() {

				if (auto_refreah_data_Timer != null)
					auto_refreah_data_Timer.cancel();
				if (auto_refreah_data_TimerTask != null)
					auto_refreah_data_TimerTask.cancel();

				auto_refreah_data_Timer = new Timer();
				auto_refreah_data_TimerTask = new TimerTask() {

					@Override
					public void run() {
						
						if(isauto_getData){					
							Thread a = new Thread(){
								@Override
								public void run(){
									getRxData();
								}};
							a.start();
							try {
								a.join();
							} catch (InterruptedException e) {}
								
							Thread b = new Thread(){
								@Override
								public void run(){
									getTxData();
								}};
							b.start();
							try {
								b.join();
							} catch (InterruptedException e) {}
							
							
							Thread c = new Thread(){
								@Override
								public void run(){
									getSituationData();
								}};
							c.start();
							try {
								c.join();
							} catch (InterruptedException e) {}
							
							
							Thread d = new Thread(){
								@Override
								public void run(){
									getVWData();
								}};
							d.start();
							try {
								d.join();
							} catch (InterruptedException e) {}			
							
							
							Thread f = new Thread(){
								@Override
								public void run(){
									getGconnData();
								}};
							f.start();
							try {
								f.join();
							} catch (InterruptedException e) {}			
							
							parseRxData();
							parseTxData();
							parseSituationData();
							parseGconnData();
							
						}			
					}
				};

				
				
				auto_refreah_data_Timer.schedule(auto_refreah_data_TimerTask, 1000, 15000);

			}
		}.start();
	}

	private static Timer auto_refreah_data_Timer;
	private static TimerTask auto_refreah_data_TimerTask;
}
