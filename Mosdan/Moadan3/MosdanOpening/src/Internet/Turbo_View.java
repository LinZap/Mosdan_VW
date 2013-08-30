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
 * Version 0.3 ���� : �z�L Turbo_View.exe �����ҤU�ϥ�JAVA�s�u��
 * call_serv.php�A�ǰe�Ѽƶi�ӹ�Server�U�F���O
 * 
 */

public class Turbo_View {
	private final boolean DEBUG = true;
	private final String uri = "http://" + Data.system_server
			+ "/call_serv.php";
	private ArrayList<NameValuePair> arg;

	/**
	 * ���yTx��
	 * 
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String Do_searchTx() {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "search tx"));
		if (DEBUG)
			Log.i("cmd", "search tx");
		return execute(arg);
	}

	/**
	 * ���yRx��
	 * 
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String Do_searchRx() {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "search rx"));
		if (DEBUG)
			Log.i("cmd", "search rx");
		return execute(arg);
	}

	/**
	 * �ҥΩ�����Tx/Rx��
	 * 
	 * @mac Tx/Rx ��mac��}
	 * @boo true���ҥ�; false������
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * �]�w Tx/Rx�ݦW��
	 * 
	 * @BUG ����s�JSQL���۰��ন2�i��,���X�ɻݦۦ��ഫ
	 * @mac Tx/Rx ��mac��}
	 * @new_name ���]�w���W��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String TR_rename(String mac, String new_name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "comment " + mac + " " + new_name));
		if (DEBUG)
			Log.i("cmd", "comment " + mac + " " + new_name);
		return execute(arg);
	}

	/**
	 * ���[Tx/Rx�ݻ���
	 * 
	 * @mac Tx/Rx ��mac��}
	 * @explain ���[�������r��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * �R��Tx/Rx��
	 * 
	 * @BUG �L�ĪG v0.2
	 * @mac Tx/Rx ��mac��}
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String TR_delete(String mac) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "delete " + mac));
		if (DEBUG)
			Log.i("cmd", "delete " + mac);
		return execute(arg);
	}

	/**
	 * �NTx/Rx�� �]�^��l�]�w
	 * 
	 * @BUG �L����ĪG v0.2
	 * @mac Tx/Rx ��mac��}
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String TR_reset(String mac) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "default " + mac));
		if (DEBUG)
			Log.i("cmd", "default " + mac);
		return execute(arg);
	}

	/**
	 * ���s�}��Tx/Rx��
	 * 
	 * @mac Tx/Rx ��mac��}
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String TR_reboot(String mac) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "reboot " + mac));
		if (DEBUG)
			Log.i("cmd", "reboot " + mac);
		return execute(arg);
	}

	/**
	 * �]�wTx�ݼҦ� @ �������A�L�k�Q�ק�
	 * 
	 * @Tx_mac Tx�ݪ�mac��}
	 * @mode 0 �� Graphic�Ҧ�; 1�� Video�Ҧ�
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * �]�wTx��ATI�Ҧ� @ �������A�L�k�Q�ק�
	 * 
	 * @Tx_mac Tx�ݪ�mac��}
	 * @mode 0�� OFF�Ҧ�; 1�� Type 1�Ҧ�; 2�� Type 2�Ҧ�
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * �]�wRx�ݳs�u��Tx��
	 * 
	 * @Rx_mac Rx�ݪ�mac��}
	 * @Tx_mac Tx�ݪ�mac��}�A�i�� null
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ���w�@��Rx�ݡA���OSD�r��3��
	 * 
	 * @Rx_ip Rx��IP��m
	 * @str ����ܪ�OSD�r��A�d��:[a-zA-Z0-9]
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * �W�[�γ]�wRx�ݳs��Tx�ݪ�����
	 * 
	 * @name ���ҦW��
	 * @Rx_mac Rx�ݪ�mac��}
	 * @Tx_mac Tx�ݪ�mac��}�A�i�� null
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * �bRx�ݳs��Tx�ݱ��Ҥ��A���J�@�ӫ��w������
	 * 
	 * @name ���ҦW��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String RxSituation_start(String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "sit_tr_load " + name));
		if (DEBUG)
			Log.i("cmd", "sit_tr_load " + name);
		return execute(arg);
	}

	/**
	 * �bRx�ݳs��Tx�ݪ����Ҥ��A�R���@Rx�Υ�����
	 * 
	 * @name ���ҦW��
	 * @Rx_mac Rx�ݪ�mac��}�A�i�H��J all �r��ӧR������Rx��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * �]�w�@��Rx�ݨ���w��Rx�s�դ�
	 * 
	 * @Rx_mac Rx�ݪ�mac��}
	 * @name Rx�s�զW�١A�Y�W�٬� _clear_ �A�h��ܲM��Rx�ݩ��ݸs��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * �M�Ť@��Rx�s�դ��Ҧ����
	 * 
	 * @name Rx�s�զW��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String Group_clear(String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "rx_group_clear  " + name));
		if (DEBUG)
			Log.i("cmd", "rx_group_clear  " + name);
		return execute(arg);
	}

	/**
	 * �]�w�@��Rx�s�ճs�u����w��Tx��
	 * 
	 * @name Rx�s�զW��
	 * @Tx_mac Tx�ݪ�mac��}
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ���w�@��Rx�ݩ��ݪ�����
	 * 
	 * @Rx_mac Rx��mac��}
	 * @VWid ����ID (�d��b 1~30 ����)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String VW_Rx_set(String Rx_mac, String VWid) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_group " + Rx_mac + " " + VWid));
		if (DEBUG)
			Log.i("cmd", "vw_group " + Rx_mac + " " + VWid);
		return execute(arg);

	}

	/**
	 * ���w�@��Rx�ݡA�b���ݹq�����a�b(����Y)����m
	 * 
	 * @BUG �L�ĪG v0.2
	 * @Rx_mac Rx��mac��}
	 * @y �a�b(����Y)����m (�d��b 1~30����)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String VW_Rx_locY(String Rx_mac, int y) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_row " + Rx_mac + " " + y));
		if (DEBUG)
			Log.i("cmd", "vw_row " + Rx_mac + " " + y);
		return execute(arg);
	}

	/**
	 * ���w�@��Rx�ݡA�b���ݹq�����a�b(����X)����m
	 * 
	 * @Rx_mac Rx��mac��}
	 * @x ��b(����X)����m(�d��b 1~30����)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String VW_Rx_locX(String Rx_mac, int x) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_column " + Rx_mac + " " + x));
		if (DEBUG)
			Log.i("cmd", "vw_column " + Rx_mac + " " + x);
		return execute(arg);
	}

	/**
	 * ���w�@��Rx�ݡA�b���ݹq���𤤪������L��
	 * 
	 * @BUG �L�ĪG v0.2
	 * @Rx_mac Rx��mac��}
	 * @num �T�� (�d��b-1000~1000)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ���w�@��Rx�ݡA�b���ݹq���𤤪������L��
	 * 
	 * @BUG �L�ĪG v0.2
	 * @Rx_mac Rx��mac��}
	 * @num �T�� (�d��b-1000~1000)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ���w�@��Rx�ݡA�b���ݹq���𤤪��������i
	 * 
	 * @Rx_mac Rx��mac��}
	 * @num �T�� (�d��b -10000~10000)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ���w�@��Rx�ݡA�b���ݹq���𤤪��������i
	 * 
	 * @Rx_mac Rx��mac��}
	 * @num �T�� (�d��b -10000~10000)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ���w�@��Rx�ݡA�b���ݹq���𤤪��������i
	 * 
	 * @Rx_mac Rx��mac��}
	 * @num �T�� (�d��b -30000~30000)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ���w�@�ӱ���ID�s�u����w��Tx��
	 * 
	 * @VWid ����ID (ID�d��b 1~30 ����)
	 * @Tx_mac Tx�ݪ�mac��}�A�i�� null
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ���w�@�ӱ���ID�A�]�w��e����b(����Y)�զ��ƶq
	 * 
	 * @VWid ����ID (ID�d��b 1~30 ����)
	 * @num �զ��ƶq (�d��b 1~30����)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ���w�@�ӱ���ID�A�]�w��e���a�b(����X)�զ��ƶq
	 * 
	 * @VWid ����ID (ID�d��b 1~30 ����)
	 * @num �զ��ƶq (�d��b 1~30 ����)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ���w�@�ӱ���ID�A�]�w��h�D���ƶq
	 * 
	 * @VWid ����ID (ID�d��b 1~30 ����)
	 * @num �h�D���ƶq (�d��b 0~30����)
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * �x�s�ثe���q�������w���q���𱡹�
	 * 
	 * @name �q���𱡹ҦW��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String VW_save(String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_sit_save " + name));
		if (DEBUG)
			Log.i("cmd", "vw_sit_save " + name);
		return execute(arg);
	}

	/**
	 * �R�����w���q����Ҧ�����(�]�t�䩳�U�Ҧ�����)
	 * 
	 * @name �q���𱡹ҦW��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String VW_delete(String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_sit_delete " + name));
		if (DEBUG)
			Log.i("cmd", "vw_sit_delete " + name);
		return execute(arg);
	}

	/**
	 * ���J���w���q���𱡹�
	 * 
	 * @name �q���𱡹ҦW��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
	 */
	public String VW_load(String name) {
		arg = new ArrayList<NameValuePair>();
		arg.add(new BasicNameValuePair("cmd", "vw_sit_load " + name));
		if (DEBUG)
			Log.i("cmd", "vw_sit_load " + name);
		return execute(arg);
	}

	/**
	 * �פJ�Ѽƨ���A��
	 * 
	 * @argment �פJ���A�����ѼƦC��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * �P�_�r��O�_��OSD�r�� �W�h�� �r��d��:[a-zA-Z0-9]
	 * 
	 * @str ���ˬd���r��
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
	 * ������k:�HPOST��k�A�ǰe���O����w��PHP�����ݡAPHP�����ݦA�N�ѼƼg�JServer����
	 * 
	 * @arg �]�w�n���Ѽ�
	 * @return ���� 404 �r�� �h��ܰ��榨�\
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
