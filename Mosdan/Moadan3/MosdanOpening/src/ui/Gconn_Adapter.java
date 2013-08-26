package ui;

import TV.Mosdan2.R;
import Data.Data;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Gconn_Adapter extends BaseAdapter {
	private int[] layColor = { Color.rgb(255, 157, 157),
			Color.rgb(255, 203, 45), Color.rgb(121, 188, 255) };
	private String[] tx_name;
	private String[] rx_name;
	private LayoutInflater mInflater;

	public Gconn_Adapter(Context context, String[] tx, String[] rx) {

		tx_name = tx;
		rx_name = rx;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (tx_name != null) ? tx_name.length : 0;
	}

	@Override
	public Object getItem(int arg0) {

		return tx_name[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int p, View view, ViewGroup parent) {

		view = mInflater.inflate(R.layout.gconn_list, null);
		TextView tx_TextView = (TextView) view.findViewById(R.id.gconn_name_tx);
		TextView rx_TextView = (TextView) view.findViewById(R.id.gconn_name_rx);
		LinearLayout colorLayout = (LinearLayout) view
				.findViewById(R.id.gconn_color_layout);

		colorLayout.setBackgroundColor(getColor());
		idx += 1;

		if (tx_name != null && rx_name != null) {
			tx_TextView.setText(tx_name[p]);
			rx_TextView.setText(rx_name[p]);
		}

		return view;
	}
	
	

	public  void ref() {
		idx = 0;
		rx_name =Data.Gconn_rx;
		tx_name = Data.Gconn_tx;
		notifyDataSetChanged();
	}

	private int idx = 0;

	private int getColor() {
		if (idx >= layColor.length)
			idx = 0;
		return layColor[idx];
	}

}
