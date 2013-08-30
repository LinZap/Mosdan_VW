package Internet;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import Data.Data;
import android.util.Log;

/**
 * Version 0.3 說明 : 透過 Turbo_View.exe 的環境下使用JAVA連線到
 * call_serv.php，傳送參數進而對Server下達指令
 * 
 */

public class Turbo_View {
	private final boolean DEBUG = true;
	private final String uri = "http://" + Data.system_server
			+ "/call_serv.php";
	private ArrayList<NameValuePair> arg;

	/**
	 * 掃描Tx端
	 * 
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String Do_searchTx() {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "search tx"));
		if (DEBUG)
			Log.i("cmd", "search tx");
		return execute(arg);
	}

	/**
	 * 掃描Rx端
	 * 
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String Do_searchRx() {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "search rx"));
		if (DEBUG)
			Log.i("cmd", "search rx");
		return execute(arg);
	}

	/**
	 * 啟用或關閉Tx/Rx端
	 * 
	 * @mac Tx/Rx 的mac位址
	 * @boo true為啟用; false為關閉
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String TR_power(String mac, boolean boo) {
		arg = new ArrayList<NameValuePair>();
		if (boo) {
			arg.add(new BasicNameValuePair("cmd", "access " + mac + " y"));
			if (DEBUG)
				Log.i("cmd", "access " + mac + " y");
		} else {
			arg.add(new BasicNameValuePair("cmd", "access " + mac + " n"));
			if (DEBUG)
				Log.i("cmd", "access " + mac + " n");
		}
		return execute(arg);
	}

	/**
	 * 設定 Tx/Rx端名稱
	 * 
	 * @BUG 中文存入SQL中自動轉成2進位,取出時需自行轉換
	 * @mac Tx/Rx 的mac位址
	 * @new_name 欲設定的名稱
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String TR_rename(String mac, String new_name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "comment " + mac + " " + new_name));
		if (DEBUG)
			Log.i("cmd", "comment " + mac + " " + new_name);
		return execute(arg);
	}

	/**
	 * 附加Tx/Rx端說明
	 * 
	 * @mac Tx/Rx 的mac位址
	 * @explain 附加說明的字串
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String TR_explain(String mac, String explain) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "explain_t " + mac + " "
				+ explain));
		if (DEBUG)
			Log.i("cmd", "explain_t " + mac + " " + explain);
		return execute(arg);
	}

	/**
	 * 刪除Tx/Rx端
	 * 
	 * @BUG 無效果 v0.2
	 * @mac Tx/Rx 的mac位址
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String TR_delete(String mac) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "delete " + mac));
		if (DEBUG)
			Log.i("cmd", "delete " + mac);
		return execute(arg);
	}

	/**
	 * 將Tx/Rx端 設回初始設定
	 * 
	 * @BUG 無任何效果 v0.2
	 * @mac Tx/Rx 的mac位址
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String TR_reset(String mac) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "default " + mac));
		if (DEBUG)
			Log.i("cmd", "default " + mac);
		return execute(arg);
	}

	/**
	 * 重新開機Tx/Rx端
	 * 
	 * @mac Tx/Rx 的mac位址
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String TR_reboot(String mac) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "reboot " + mac));
		if (DEBUG)
			Log.i("cmd", "reboot " + mac);
		return execute(arg);
	}

	/**
	 * 設定Tx端模式 @ 關機狀態無法被修改
	 * 
	 * @Tx_mac Tx端的mac位址
	 * @mode 0 為 Graphic模式; 1為 Video模式
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String Tx_setMode(String Tx_mac, int mode) {
		arg = new ArrayList<NameValuePair>();
		if (mode == 0) {
			arg.add(new BasicNameValuePair("cmd", "txmode " + Tx_mac
					+ " Graphic"));
			if (DEBUG)
				Log.i("cmd", "txmode " + Tx_mac + " Graphic");
		}

		else {
			arg.add(new BasicNameValuePair("cmd", "txmode " + Tx_mac + " Video"));
			if (DEBUG)
				Log.i("cmd", "txmode " + Tx_mac + " Video");
		}
		return execute(arg);
	}

	/**
	 * 設定Tx端ATI模式 @ 關機狀態無法被修改
	 * 
	 * @Tx_mac Tx端的mac位址
	 * @mode 0為 OFF模式; 1為 Type 1模式; 2為 Type 2模式
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String Tx_setATIMode(String Tx_mac, int mode) {
		String m = "\"OFF\"";
		switch (mode) {
		case 1:
			m = "\"Type 1\"";
			break;
		case 2:
			m = "\"Type 2\"";
			break;
		default:
			m = "\"OFF\"";
		}
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "atimode " + Tx_mac + " " + m));
		if (DEBUG)
			Log.i("cmd", "atimode " + Tx_mac + " " + m);
		return execute(arg);
	}

	/**
	 * 設定Rx端連線到Tx端
	 * 
	 * @Rx_mac Rx端的mac位址
	 * @Tx_mac Tx端的mac位址，可為 null
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String Rx_connTx(String Rx_mac, String Tx_mac) {
		arg = new ArrayList<NameValuePair>();
		if (Tx_mac == null)
			Tx_mac = "None";
		arg.add(new BasicNameValuePair("cmd", "connect " + Rx_mac + " "
				+ Tx_mac));
		if (DEBUG)
			Log.i("cmd", "connect " + Rx_mac + " " + Tx_mac);
		return execute(arg);
	}

	/**
	 * 指定一個Rx端，顯示OSD字串3秒
	 * 
	 * @Rx_ip Rx端IP位置
	 * @str 欲顯示的OSD字串，範圍:[a-zA-Z0-9]
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String Rx_OSD(String Rx_ip, String str) {

		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_osd " + Rx_ip + " "
				+ String.valueOf(str)));
		if (DEBUG)
			Log.i("cmd", "vw_osd " + Rx_ip + " " + String.valueOf(str));
		return execute(arg);

	}

	/**
	 * 增加或設定Rx端連到Tx端的情境
	 * 
	 * @name 情境名稱
	 * @Rx_mac Rx端的mac位址
	 * @Tx_mac Tx端的mac位址，可為 null
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String RxSituation_set(String name, String Rx_mac, String Tx_mac) {
		if (Tx_mac == null)
			Tx_mac = "None";
		if (name == "")
			name = "None";
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "sit_tr_add " + name + " "
				+ Rx_mac + " " + Tx_mac));
		if (DEBUG)
			Log.i("cmd", "sit_tr_add " + name + " " + Rx_mac + " " + Tx_mac);
		return execute(arg);
	}

	/**
	 * 在Rx端連到Tx端情境中，載入一個指定的情境
	 * 
	 * @name 情境名稱
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String RxSituation_start(String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "sit_tr_load " + name));
		if (DEBUG)
			Log.i("cmd", "sit_tr_load " + name);
		return execute(arg);
	}

	/**
	 * 在Rx端連到Tx端的情境中，刪除一Rx或全部端
	 * 
	 * @name 情境名稱
	 * @Rx_mac Rx端的mac位址，可以輸入 all 字串來刪除全部Rx端
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String RxSituation_delete(String name, String Rx_mac) {

		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "sit_tr_add " + name + " "
				+ Rx_mac));
		if (DEBUG)
			Log.i("cmd", "sit_tr_add " + name + " " + Rx_mac);
		return execute(arg);

	}

	/**
	 * 設定一個Rx端到指定的Rx群組中
	 * 
	 * @Rx_mac Rx端的mac位址
	 * @name Rx群組名稱，若名稱為 _clear_ ，則表示清除Rx端所屬群組
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String Group_set(String Rx_mac, String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "rx_group_update " + Rx_mac + " "
				+ name));
		if (DEBUG)
			Log.i("cmd", "rx_group_update " + Rx_mac + " " + name);
		return execute(arg);
	}

	/**
	 * 清空一個Rx群組中所有資料
	 * 
	 * @name Rx群組名稱
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String Group_clear(String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "rx_group_clear  " + name));
		if (DEBUG)
			Log.i("cmd", "rx_group_clear  " + name);
		return execute(arg);
	}

	/**
	 * 設定一個Rx群組連線到指定的Tx端
	 * 
	 * @name Rx群組名稱
	 * @Tx_mac Tx端的mac位址
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String Group_connTx(String name, String Tx_mac) {
		if (Tx_mac == null)
			Tx_mac = "None";
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "rx_group_conn " + name + " "
				+ Tx_mac));
		if (DEBUG)
			Log.i("cmd", "rx_group_conn " + name + " " + Tx_mac);
		return execute(arg);
	}

	/**
	 * 指定一個Rx端所屬的情境
	 * 
	 * @Rx_mac Rx端mac位址
	 * @VWid 情境ID (範圍在 1~30 之間)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_Rx_set(String Rx_mac, String VWid) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_group " + Rx_mac + " " + VWid));
		if (DEBUG)
			Log.i("cmd", "vw_group " + Rx_mac + " " + VWid);
		return execute(arg);

	}

	/**
	 * 指定一個Rx端，在所屬電視牆縱軸(垂直Y)的位置
	 * 
	 * @BUG 無效果 v0.2
	 * @Rx_mac Rx端mac位址
	 * @y 縱軸(垂直Y)的位置 (範圍在 1~30之間)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_Rx_locY(String Rx_mac, int y) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_row " + Rx_mac + " " + y));
		if (DEBUG)
			Log.i("cmd", "vw_row " + Rx_mac + " " + y);
		return execute(arg);
	}

	/**
	 * 指定一個Rx端，在所屬電視牆縱軸(水平X)的位置
	 * 
	 * @Rx_mac Rx端mac位址
	 * @x 橫軸(水平X)的位置(範圍在 1~30之間)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_Rx_locX(String Rx_mac, int x) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_column " + Rx_mac + " " + x));
		if (DEBUG)
			Log.i("cmd", "vw_column " + Rx_mac + " " + x);
		return execute(arg);
	}

	/**
	 * 指定一個Rx端，在所屬電視牆中的水平微調
	 * 
	 * @BUG 無效果 v0.2
	 * @Rx_mac Rx端mac位址
	 * @num 幅度 (範圍在-1000~1000)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_Rx_tuneX(String Rx_mac, int num) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_hTune " + Rx_mac + " "
				+ String.valueOf(num)));
		if (DEBUG)
			Log.i("cmd", "vw_hTune " + Rx_mac + " " + String.valueOf(num));
		return execute(arg);
	}

	/**
	 * 指定一個Rx端，在所屬電視牆中的垂直微調
	 * 
	 * @BUG 無效果 v0.2
	 * @Rx_mac Rx端mac位址
	 * @num 幅度 (範圍在-1000~1000)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_Rx_tuneY(String Rx_mac, int num) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_vTune " + Rx_mac + " "
				+ String.valueOf(num)));
		if (DEBUG)
			Log.i("cmd", "vw_hScale " + Rx_mac + " " + String.valueOf(num));
		return execute(arg);
	}

	/**
	 * 指定一個Rx端，在所屬電視牆中的水平延展
	 * 
	 * @Rx_mac Rx端mac位址
	 * @num 幅度 (範圍在 -10000~10000)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_Rx_scaX(String Rx_mac, int num) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_hScale " + Rx_mac + " "
				+ String.valueOf(num)));

		if (DEBUG)
			Log.i("cmd", "vw_hScale " + Rx_mac + " " + String.valueOf(num));
		return execute(arg);
	}

	/**
	 * 指定一個Rx端，在所屬電視牆中的垂直延展
	 * 
	 * @Rx_mac Rx端mac位址
	 * @num 幅度 (範圍在 -10000~10000)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_Rx_scaY(String Rx_mac, int num) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_vScale " + Rx_mac + " "
				+ String.valueOf(num)));
		if (DEBUG)
			Log.i("cmd", "vw_vScale " + Rx_mac + " " + String.valueOf(num));
		return execute(arg);
	}

	/**
	 * 指定一個Rx端，在所屬電視牆中的撕裂延展
	 * 
	 * @Rx_mac Rx端mac位址
	 * @num 幅度 (範圍在 -30000~30000)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_Rx_TearDelay(String Rx_mac, int num) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_tDelay " + Rx_mac + " "
				+ String.valueOf(num)));
		if (DEBUG)
			Log.i("cmd", "vw_tDelay " + Rx_mac + " " + String.valueOf(num));
		return execute(arg);
	}

	/**
	 * 指定一個情境ID連線到指定的Tx端
	 * 
	 * @VWid 情境ID (ID範圍在 1~30 之間)
	 * @Tx_mac Tx端的mac位址，可為 null
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_connTx(String VWid, String Tx_mac) {
		if (Tx_mac == null)
			Tx_mac = "None";
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_group_srctx " + VWid + " "
				+ Tx_mac));
		if (DEBUG)
			Log.i("cmd", "vw_group_srctx " + VWid + " " + Tx_mac);
		return execute(arg);
	}

	/**
	 * 指定一個情境ID，設定其畫面橫軸(水平Y)組成數量
	 * 
	 * @VWid 情境ID (ID範圍在 1~30 之間)
	 * @num 組成數量 (範圍在 1~30之間)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_Y(String VWid, int num) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_group_row " + VWid + " "
				+ num));
		if (DEBUG)
			Log.i("cmd", "vw_group_row " + VWid + " " + num);
		return execute(arg);
	}

	/**
	 * 指定一個情境ID，設定其畫面縱軸(垂直X)組成數量
	 * 
	 * @VWid 情境ID (ID範圍在 1~30 之間)
	 * @num 組成數量 (範圍在 1~30 之間)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_X(String VWid, int num) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_group_column " + VWid + " "
				+ num));
		if (DEBUG)
			Log.i("cmd", "vw_group_column " + VWid + " " + num);
		return execute(arg);
	}

	/**
	 * 指定一個情境ID，設定其多主機數量
	 * 
	 * @VWid 情境ID (ID範圍在 1~30 之間)
	 * @num 多主機數量 (範圍在 0~30之間)
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_multiHost(String VWid, int num) {

		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_group_multihost " + VWid
				+ " " + String.valueOf(num)));
		if (DEBUG)
			Log.i("cmd",
					"vw_group_multihost " + VWid + " " + String.valueOf(num));
		return execute(arg);
	}

	/**
	 * 儲存目前的電視牆到指定的電視牆情境
	 * 
	 * @name 電視牆情境名稱
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_save(String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_sit_save " + name));
		if (DEBUG)
			Log.i("cmd", "vw_sit_save " + name);
		return execute(arg);
	}

	/**
	 * 刪除指定的電視牆所有情境(包含其底下所有成員)
	 * 
	 * @name 電視牆情境名稱
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_delete(String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_sit_delete " + name));
		if (DEBUG)
			Log.i("cmd", "vw_sit_delete " + name);
		return execute(arg);
	}

	/**
	 * 載入指定的電視牆情境
	 * 
	 * @name 電視牆情境名稱
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String VW_load(String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_sit_load " + name));
		if (DEBUG)
			Log.i("cmd", "vw_sit_load " + name);
		return execute(arg);
	}

	/**
	 * 匯入參數到伺服器
	 * 
	 * @argment 匯入伺服器的參數列表
	 * @return 不為 404 字串 則表示執行成功
	 */
	public String Do_importARG(String[] argment) {

		try {
			arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("cmd", "sys_import start"));
			if (DEBUG)
				Log.i("cmd", "sys_import start");
			execute(arg);

			for (String s : argment) {
				arg = new ArrayList<NameValuePair>();
				arg.add(new BasicNameValuePair("cmd", s));
				execute(arg);
			}

			arg = new ArrayList<NameValuePair>();
			arg.add(new BasicNameValuePair("cmd", "sys_import save"));
			execute(arg);
			return "0";
		} catch (Exception e) {
			return "404";
		}
	}

	/**
	 * 判斷字串是否為OSD字串 規則為 字串範圍:[a-zA-Z0-9]
	 * 
	 * @str 欲檢查的字串
	 * @return 不為 404 字串 則表示執行成功
	 */
	public boolean isOSD_String(String str) {
		boolean boo = false;
		for (int i = 0; i < str.length(); i++) {
			boo = false;
			char c = str.charAt(i);
			if (c >= 'a' && c <= 'z')
				boo = true;
			if (c >= 'A' && c <= 'Z')
				boo = true;
			if (c >= '0' && c <= '9')
				boo = true;
		}
		return boo;
	}

	/**
	 * 內部方法:以POST方法，傳送指令到指定的PHP頁面端，PHP頁面端再將參數寫入Server執行
	 * 
	 * @arg 設定好的參數
	 * @return 不為 404 字串 則表示執行成功
	 */
	private String execute(ArrayList<NameValuePair> arg) {
		HttpPost httpPost = new HttpPost(uri);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(arg, HTTP.UTF_8));
			HttpResponse hr = new DefaultHttpClient().execute(httpPost);
			if (hr.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(hr.getEntity());
			} else
				return "404";
		} catch (IOException e) {
			return "404";
		}

	}

}
