package ui;

import Data.Data;
import TV.Mosdan2.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Gconn_GroupAdapter extends BaseAdapter {
	private int[] layColor = { Color.rgb(255,157,157),
			Color.rgb(255,203,45), Color.rgb(121,188,255) };
	private String[] group_name;
	private LayoutInflater mInflater;
	
	public Gconn_GroupAdapter(Context context, String[] group_name) {
		this.group_name = group_name;
		mInflater = LayoutInflater.from(context);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (group_name != null) ? group_name.length : 0;
	}
	@Override
	public Object getItem(int arg0) {

		return group_name[arg0];
	}
	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void ref(){
		group_name = Data.get_Rx_Group_name();
		notifyDataSetChanged();
	}
	@Override
	public View getView(int p, View view, ViewGroup parent) {

		view = mInflater.inflate(R.layout.gconn_group_list, null);
		TextView tx_TextView = (TextView) view.findViewById(R.id.gconn_group_name);
		LinearLayout colorLayout = (LinearLayout) view
				.findViewById(R.id.gconn_color_layout);
		colorLayout.setBackgroundColor(getColor());
		idx += 1;

		if (group_name != null) 
			tx_TextView.setText(group_name[p]);
		return view;
	}

	private int idx = 0;
	private int getColor() {
		if (idx >= layColor.length)
			idx = 0;
		return layColor[idx];
	}

}
